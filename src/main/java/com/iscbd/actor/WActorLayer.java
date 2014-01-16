package com.iscbd.wingmaps.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
import com.iscbd.wingmaps.category.WActorCategory;
import com.iscbd.wingmaps.map.WMap;
import com.iscbd.wingmaps.map.WPoint;
import com.iscbd.wingmaps.marker.WMarker;

public class WActorLayer extends Overlay implements IMyLocationOverlay, LocationListener, SensorEventListener{
	
	//private static final Logger logger = LoggerFactory.getLogger(MyLocationOverlay.class);
	
	//	Location
	//private Runnable runOnFirstFix = null;
	//private LinkedList<Runnable> runOnFirstFix = new LinkedList<Runnable>();
    private Location lastFix = null;
    private final LocationManager mLocationManager;
	private final SensorManager mSensorManager;
	
	List<WActorListener> listeners = new ArrayList<WActorListener>();
	
	private int actor_tapped = -1;
	
	private Location mLocation;

	private long mLocationUpdateMinTime = 0;
	private float mLocationUpdateMinDistance = 0.0f;
	
	public LocationListenerProxy mLocationListener = null;
	public SensorEventListenerProxy mSensorListener = null;
	
	private float mAzimuth = Float.NaN;
	
	private final LinkedList<Runnable> mRunOnFirstFix = new LinkedList<Runnable>();
	
    private boolean myLocationEnabled;
    private boolean compassEnabled = false;
    private boolean mFollow = false;		// Follow Main Actor
    private boolean mFollowFix = false;
    private boolean mAdjust = false;		// Adjust map to actors
    private boolean mAdjustFix = false;
	
	private WActor main_actor;
	private int main_actor_index = 0;
	private WMap wmap;
	private boolean integrate;		// this layer is integrate with map_view
	
	
	Context context;
	WMarker defaultMarker;
	
	private boolean visibility=true;
	private boolean isActivate=true;
	private Integer order;
	private boolean action;
	
	ArrayList<WActor> actors = new ArrayList<WActor>();
	ArrayList<WActor> actors_ordered = (ArrayList<WActor>) actors.clone();
	int actors_number;
	
	// FLAGS
	public static final int FIRST_FARTHEST_ACTOR = 1;
	public static final int SECOND_FARTHEST_ACTOR = 2;
	
	//***************************************************************
	//
	//		L I S T E N E R
	//
	//***************************************************************
	public interface WActorListener
	{
		void onActorTapped(WActor actor_tapped, int actor_index);
	}
	
	// Counter to follow and adjust map
	final CountDownTimer counter = new CountDownTimer(8000 , 1000) { 
		public void onTick(long millisUntilFinished) { }
		public void onFinish() {
			// When the counter finish change the center and zoom
			if(mFollow)
				wmap.setCenter(getMainActor().getPosition());
			if(mAdjust)
			{
				WPoint first_farthest = actors.get(getFarthestActor(FIRST_FARTHEST_ACTOR)).getPosition();
				wmap.adjustMap(first_farthest);
				wmap.invalidate();
			}
			else
				
		    counter.start(); 
		}
		};
	
	
	//int activity;		// 0 -> actors haven't influence on the map
						// 1 -> Only main actor has influence on the map
						// 2 -> Only second actors have influence on the map
						// 3 -> Main actor and second actos, both of them have influence on the map
	
	
	
	//***************************************************
	//
	//			C O N S T R U C T O R S
	//
	//***************************************************
	/**
	 * 
	 */
	/*public WActorLayer(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		marker = defaultMarker;
		sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
	}*/
	
	public WActorLayer(WMap wmap_p, WMarker defaultMarker_p, Context context) {
		  this.context = context;
		  //sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
	      //locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		  mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		  mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		  wmap = wmap_p;
		  wmap.addOverlay(this);
		  counter.start();
		  
		  mFollow = false;		// Follow Main Actor
		  mFollowFix = false;
		  mAdjust = false;		// Adjust map to actors
		  mAdjustFix = false;
		  
		  WPoint geoloc = new WPoint(37913350,-4717208);
		  this.enableMyLocation();
		  if(mLocation != null)
			  geoloc = new WPoint((int)(mLocation.getLatitude()*1E6), (int)(mLocation.getLongitude()*1E6));
		  
		  defaultMarker = defaultMarker_p;
		  main_actor = new WActor(context, 
					-1, 
					"User", 
					"", 
					geoloc,
					true, 
					defaultMarker);
		  
		  actors.add(main_actor);
		  actors_number++;
		  actors_ordered = getActorsOrderedByLatitude();
		  this.disableMyLocation();
		  
		  
	}
	
	//***************************************************
	//
	//			M E T H O D S
	//
	//***************************************************
	
	
	//************************************
	//	Getter & Setter
	//************************************
	/**
	 * 
	 * @return
	 */
	public boolean isFollowEnabled() {
		return mFollow;
	}

	
	//*************************************
	//	Methods from Superclass/Interfaces
	//*************************************
	@Override
	public void onLocationChanged(final Location location) {
		mLocation = location;
		for (final Runnable runnable : mRunOnFirstFix) {
			new Thread(runnable).start();
		}
		mRunOnFirstFix.clear();
		actors.get(0).setPosition(new WPoint((int)(location.getLatitude()*1E6), (int)(location.getLongitude()*1E6)));
	}

	@Override
	public void onProviderDisabled(final String provider) {
	}

	@Override
	public void onProviderEnabled(final String provider) {
	}

	@Override
	public void onStatusChanged(final String provider, final int status, final Bundle extras) {
	}
	
     
    //***********************************
    //		Methods
    //***********************************
	/**
	 * 
	 */
     public void disableMyLocation() {
         //getLocationManager().removeUpdates(this);
    	 if (mLocationListener != null) {
 			mLocationListener.stopListening();
 		}
    	 mLocationListener = null;

 		// Update the screen to see changes take effect
 		if (wmap.getMapview() != null) {
 			wmap.postInvalidate();
 		}
	 }
	 
     /**
      * 
      */
	 public boolean enableMyLocation() {
		 boolean result = true;

			if (mLocationListener == null) {
				mLocationListener = new LocationListenerProxy(mLocationManager);
				result = mLocationListener.startListening(this, mLocationUpdateMinTime,
						mLocationUpdateMinDistance);
			}
			
			mLocation = mLocationManager.getLastKnownLocation(mLocationManager.GPS_PROVIDER);
			
			// Update the screen to see changes take effect
			if (wmap != null) {
				if(mFollow)
				{
					if(mLocation != null)
						wmap.setCenter(new WPoint((int)(mLocation.getLatitude()*1E6), (int)(mLocation.getLongitude()*1E6)));
				}
				//wmap.postInvalidate();
			}

			return result;
	 }
	 
	 public boolean runOnFirstFix(Runnable runnable) {
	         if(myLocationEnabled) {
	                 runnable.run();
	                 return true;
	         } else {
	        	 mRunOnFirstFix.addLast(runnable);
	                 return false;
	         }
	 }
	 
	 private LocationManager getLocationManager() {
	         return this.mLocationManager; 
	 }
  
	
	 private ArrayList<WActor> getActorsOrderedByLatitude()
	 {
		 ArrayList<WActor> aux = (ArrayList<WActor>) actors.clone();
		 Collections.sort(aux);
		 return aux;
	 }
	 
	private WActor getActorTapped(WPoint hitpoint, MapView mapView)
	{
		int actor_index=-1;
		WActor actor_tapped = null;
		actors_ordered = getActorsOrderedByLatitude();
		Point point_hit = new Point();
		Projection projection = mapView.getProjection();
		projection.toPixels(hitpoint, point_hit);
		for(int i=actors_ordered.size()-1; i>=0; i--)
		{
			if(actors_ordered.get(i).isHit(point_hit.x,point_hit.y,mapView))
			{
				actor_tapped = actors_ordered.get(i);
				break;
			}
		}
		return actor_tapped;
	}
	
	
	@Override
	public boolean onTap(GeoPoint hitpoint, MapView mapView)
	{
		if(isVisible())
			if(isActive())
			{
				WActor actor_tapped = getActorTapped(WPoint.toWPoint(hitpoint), mapView);
				if(actor_tapped != null)
				{
					for(int i=0; i < listeners.size(); i++)
						listeners.get(i).onActorTapped(actor_tapped, actors.indexOf(actor_tapped));
					return true;
				}
				else
					return super.onTap(hitpoint, mapView);
			}
			else
				return super.onTap(hitpoint, mapView);
		else
			return super.onTap(hitpoint, mapView);
	}	
	
	public boolean isActive()
	{
		return isActivate;
	}
	
	public void setActive(boolean arg)
	{
		isActivate = arg;
	}
	
	/**
	 * 
	 * @param id_p Actor's ID
	 * @param name_p Actor name
	 * @param information_p Actor information
	 */
	/*public void addActor(int id_p, String name_p, String information_p){
		/*if(getActorNumber() == 0){
			main_actor = new WActor(context, 
									id_p, 
									name_p, 
									information_p, 
									true, 
									marker);
			
			actors.add(main_actor);
		}
		else{*/
		/*	WActor new_actor = new WActor(context, 
										  id_p, 
										  name_p, 
										  information_p,
										  false, 
										  defaultMarker);
			
			actors.add(new_actor);
		//}
		actors_number++;
		this.populate();
	}*/
	
	/**
	 * 
	 * @param id_p
	 * @param name_p
	 * @param information_p
	 * @param category_p
	 */
	public void addActor(int id_p, String name_p, String information_p, WActorCategory category_p){
		WActor new_actor = new WActor(context, 
										  id_p, 
										  name_p, 
										  information_p,
										  category_p,
										  false, 
										  defaultMarker);
			
			actors.add(new_actor);
		//}
		actors_number++;
	}
	
	/**
	 * Create an actor with ID
	 * @param id_p Actor's id
	 * @param name_p Actor name
	 * @param information_p Actor information
	 * @param category_p Actor category
	 * @param marker_p Marker of this actor
	 */
	public void addActor(int id_p, String name_p, String information_p, WActorCategory category_p, WMarker marker_p){
		WActor new_actor = new WActor(context, 
										  id_p, 
										  name_p, 
										  information_p,
										  category_p,
										  false, 
										  marker_p);
			
			actors.add(new_actor);
		actors_number++;
	}
	
	/**
	 * 
	 * @param id_p
	 * @param name_p
	 * @param position_p
	 */
	public void addActor(int id_p, String name_p, String information_p, WPoint position_p){
		WActor new_actor = new WActor(context, 
										  id_p, 
										  name_p, 
										  information_p, 
										  position_p, 
										  false, 
										  defaultMarker);
			
			actors.add(new_actor);
		//}
		actors_number++;
		actors_ordered = getActorsOrderedByLatitude();
	}
	
	public void addActor(int id_p, String name_p, String information_p, WPoint position_p, WMarker marker_p)
	{
		WActor new_actor = new WActor(context, 
										id_p, 
										name_p, 
										information_p,
										position_p,
										false, 
										marker_p);
		actors.add(new_actor);
		actors_number++;
		actors_ordered = getActorsOrderedByLatitude();
	}
	
	/**
	 * Create a new actor in the layer using his ID, name, info, position and category.
	 * @param id_p Actor's ID
	 * @param name_p Name
	 * @param information_p Actor Information
	 * @param position_p Actor Position
	 * @param category_p Category which this WActor participates
	 */
	public void addActor(int id_p, String name_p, String information_p, WPoint position_p, WActorCategory category_p){
		WActor new_actor = new WActor(context, 
										  id_p, 
										  name_p, 
										  information_p, 
										  position_p, 
										  false, 
										  defaultMarker,
										  category_p);
			
			actors.add(new_actor);
		//}
		actors_number++;
		actors_ordered = getActorsOrderedByLatitude();
	}
	
	/**
	 * 
	 * @param id_p
	 * @param name_p
	 * @param position_p
	 * @param precision_p
	 */
	public void addActor(int id_p, String name_p, String information_p, WPoint position_p, WActorCategory category_p, double precision_p){
		WActor new_actor = new WActor(context, 
										  id_p, 
										  name_p, 
										  information_p, 
										  position_p, 
										  false, 
										  defaultMarker, 
										  category_p, 
										  precision_p);
			
			actors.add(new_actor);
		//}
		actors_number++;
		actors_ordered = getActorsOrderedByLatitude();
	}
	
	/**
	 * 
	 * @param id_p
	 * @param name_p
	 * @param position_p
	 * @param precision_p
	 * @param icon_p
	 */
	public void addActor(int id_p, String name_p, String information_p, WPoint position_p, WActorCategory category_p, double precision_p, 
			WMarker marker_p){
		WActor new_actor = new WActor(context, 
										  id_p, 
										  name_p, 
										  information_p, 
										  position_p, 
										  false, 
										  marker_p, 
										  category_p,
										  precision_p);
			
			actors.add(new_actor);
		//}
		actors_number++;
		actors_ordered = getActorsOrderedByLatitude();
	}
	
	/**
	 * 
	 * @param id_p
	 * @param name_p
	 * @param position_p
	 * @param precision_p
	 * @param icon_p
	 * @param avatar_p
	 */
	public void addActor(int id_p, String name_p, String information_p, WPoint position_p, WActorCategory category_p, double precision_p, 
			WMarker marker_p, WMarker avatar_p){
		
		//WMarker avatar = new WMarker(avatar_p);
		WMarker avatar = avatar_p;
		
		WActor new_actor = new WActor(context, 
									  id_p, 
									  name_p, 
									  information_p, 
									  position_p, 
									  false, 
									  marker_p, 
									  category_p,
									  precision_p, 
									  avatar);
		actors.add(new_actor);
		
		actors_number++;
		actors_ordered = getActorsOrderedByLatitude();
	}
	
	/**
	 * 
	 * @return actors_number
	 */
	public int getActorNumber(){
		return actors.size();
	}
	/**
	 * 
	 * @param actor
	 * @return
	 */
	public int addActor(WActor actor){
		if(getActorNumber()==0)
			main_actor = actor;
		else
			actors.add(actor);
		actors_number++;
		return actors.indexOf(actor);
	}
	
	/**
	 * 
	 * @param index
	 * @return Actor referenced by index
	 */
	public WActor getActorByIndex(int index){
		return actors.get(index);
	}
	
	/**
	 * 
	 * @return main_actor
	 */
	public WActor getMainActor(){
		return actors.get(main_actor_index);
	}

	
	/**
	 * 
	 * @return boolean visibility
	 */
	public boolean isVisible(){
		return visibility;
	}
	
	/**
	 * 
	 * @param visibility_p
	 */
	public void setVisibility(boolean visibility_p){
		visibility = visibility_p;
	}
	
	/**
	 * 
	 * @return
	 */
	public Integer getOrder(){
		return order;
	}
	
	/**
	 * 
	 * @param order_p
	 */
	public void setOrder(Integer order_p){
		order = order_p;
	}
	
	public void setMap(WMap map_p){
		wmap = map_p;
		context = wmap.getContext();
		integrate = true;
		//location = new WLocation(context,wmap.getMapview(),this);
	}
	
	public boolean isIntegrate(){
		return integrate;
	}
	
	/**
	 * Tweet to actor using his index
	 * @param index
	 */
//	public void tweetToActorByIndex(int index)
//	{
//		if(isIntegrate()){
//			Intent intent = new Intent(wmap.context, WTwitter.class);
//			String msg = "@" + getActorByIndex(index).getTwitter() + " Este tweet es de prueba desde API-WingMaps.";
//			Bundle bundle = new Bundle();
//			//bundle.putParcelableArray("com.iscbd.wingmaps", items);
//			bundle.putString("MSG", msg);
//			intent.putExtras(bundle);
//			((Activity) wmap.context).startActivityForResult(intent, Activity.RESULT_OK);
//		}
//	}
//	
//	/**
//	 * Tweet to actor using his ID
//	 * @param id_p
//	 */
//	public void tweetToActorByID(int id_p)
//	{
//		if(isIntegrate()){
//			Intent intent = new Intent(wmap.context, WTwitter.class);
//			String msg = "@" + getActorByID(id_p).getTwitter() + " Este tweet es de prueba desde API-WingMaps.";
//			Bundle bundle = new Bundle();
//			//bundle.putParcelableArray("com.iscbd.wingmaps", items);
//			bundle.putString("MSG", msg);
//			intent.putExtras(bundle);
//			((Activity) wmap.context).startActivityForResult(intent, Activity.RESULT_OK);
//		}
//	}

	
    
    /**
     * 
     * @return
     */
    public boolean isCompassEnabled() {
        return compassEnabled;
    }
    
    /**
     * 
     * @return
     */
	@Override
	public boolean enableCompass() {
		boolean result = true;
		compassEnabled = true;
		if (mSensorListener == null) {
			mSensorListener = new SensorEventListenerProxy(mSensorManager);
			result = mSensorListener.startListening(this, Sensor.TYPE_ORIENTATION,
					SensorManager.SENSOR_DELAY_UI);
		}

		// Update the screen to see changes take effect
		if (wmap.getMapview() != null) {
			//wmap.postInvalidate();
		}

		return result;
	}
    
    /**
     * 
     */
    public synchronized void disableCompass() {
        if (compassEnabled)
            mSensorManager.unregisterListener(this);
        compassEnabled = false;
        lastFix = null;
    }
    
    /**
     * 
     */
    public void requestLocation(){
    	mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 
                60000, 
                0, 
                this
            );
    }

	@Override
	public float getOrientation() {
		return mAzimuth;
	}

	@Override
	public Location getLastFix() {
		return mLocation;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			if (event.values != null) {
				mAzimuth = event.values[0];
				//wmap.postInvalidate();
			}
		}
	}
	
	@Override
	public boolean isMyLocationEnabled() {
		return mLocationListener != null;
	}
	
	/**
	 * Follow the main actor.
	 * @param condition If it is true, the map is going to follow the main actor
	 * @param fix If 'fix' is true, is impossible change the center of the map, if it is false, the center change every 8 seconds.
	 */
	public void setFollowMainActor(boolean condition, boolean fix)
	{
		if(fix)
		{
			mFollowFix = condition;
			mFollow = false;
		}
		else
		{
			if(condition)
				wmap.setCenter(getMainActor().getPosition());
			mFollow = condition;
			mFollowFix = false;
		}
	}
	
	/**
	 * 
	 * @param condition
	 * @param fix If it is true
	 */
	public void setAdjustMapToActors(boolean condition, boolean fix)
	{
		if(fix)
		{
			mAdjustFix = condition;
			mAdjust = false;
		}
		else
		{
			WPoint first_farthest = actors.get(getFarthestActor(FIRST_FARTHEST_ACTOR)).getPosition();
			if(condition)
				wmap.adjustMap(first_farthest);
			mAdjust = condition;
			mAdjustFix = false;
		}
		
	}
	
	/**
	 * Get the index of farthest (or second farthest) actor from the main actor.
	 * @param flag That flag allow to get the farthest actor (with 'FIRST_FARTHEST_ACTOR') from main actor or the second farthest actor (with 'SECOND_FARTHEST_ACTOR').
	 * @return The index of the farthest (or second farthest) actor from main actor.
	 */
	public int getFarthestActor(int flag)
	{
		int return_value = -1;
		int first_farthest = 1;
		int second_farthest = 2;
		double distance;
		double farthest_distance = -1;
		
		// looking for the farthests from main actor
		for(int i=1; i<this.getActorNumber(); i++){
			WPoint position_aux = this.getActorByIndex(i).getPosition();
			distance = Math.sqrt(
					Math.pow(position_aux.getLatitudeE6() - getMainActor().getPosition().getLatitudeE6(), 2)
					+
					Math.pow(position_aux.getLongitudeE6() - getMainActor().getPosition().getLongitudeE6(), 2));
			
			// check the distance
			if ( distance > farthest_distance)
			{
				farthest_distance = distance;
				second_farthest = first_farthest;
				first_farthest = i;
			}
		}
		
		// We return the value that user requests
		if(flag == FIRST_FARTHEST_ACTOR){
			return_value = first_farthest;
		}
		else if(flag == SECOND_FARTHEST_ACTOR){
			return_value = second_farthest;
		}
		return return_value;
	}
	
	
	//**************************************************
	//
	//		D R A W
	//
	//**************************************************
	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) 
	{
		Paint p = new Paint();
		Point point_to_draw = new Point();
		//Projection projection = mapView.getProjection();
		
		//p.setColor(Color.BLUE);
		p.setARGB(255, 0, 0, 0);
		
		if(isVisible())
				if(shadow==false)
				{
					if(mFollowFix)
					{
						mapView.getController().setCenter(getMainActor().getPosition());
						//wmap.invalidate();
					}
					
					// adjust the zoom to farthest actors
					if (mAdjustFix){
						WPoint first_farthest = actors.get(getFarthestActor(FIRST_FARTHEST_ACTOR)).getPosition();
						//wmap.adjustMap(first_farthest);
						mapView.getController().zoomToSpan(
								2*Math.abs(first_farthest.getLatitudeE6() - getMainActor().getPosition().getLatitudeE6()), 
								2*Math.abs(first_farthest.getLongitudeE6() - getMainActor().getPosition().getLongitudeE6()));
						//System.out.println("mAdjust");
						//wmap.invalidate();
					}
					
					/*
					for(int i=0; i < this.getActorNumber(); i++)
					{
						//Drawable icon = actors.get(i).getMarker(0)
						//projection.toPixels(actors.get(i).getPosition(),point_to_draw);
						if(compassEnabled && i==main_actor_index){
							actors.get(i).draw(canvas, mapView, shadow, when, mAzimuth);
						}
						else{
							if(i==main_actor_index)
								actors.get(i).draw(canvas, mapView, shadow, when);
							else if(actors.get(i).isOriented())
								actors.get(i).draw(canvas, mapView, shadow, when, mAzimuth);
							else
								actors.get(i).draw(canvas, mapView, shadow, when);
							
						}
						//actors.get(i).getMarker(0)
					}
					*/
					for(int i=0; i < this.getActorNumber(); i++)
					{
						if(compassEnabled && i==0){
							actors_ordered.get(i).draw(canvas, mapView, shadow, when, mAzimuth);
						}
						else{
							if(i==main_actor_index)
							{
								actors_ordered.get(i).draw(canvas, mapView, shadow, when);
							}
							else if(actors.get(i).isOriented())
								actors_ordered.get(i).draw(canvas, mapView, shadow, when, mAzimuth);
							else
								actors_ordered.get(i).draw(canvas, mapView, shadow, when);
							
						}
					}
					
					wmap.postInvalidate();
					//wmap.postInvalidate();
				}
		return shadow;
	}
	
	/**
	 * Change the actor category of all actors
	 * @param category_p Category to apply to all actors
	 */
	public void setCategory(WActorCategory category_p)
	{
		for(int i=0; i < this.getActorNumber(); i++)
		{
			actors.get(i).setCategory(category_p);
		}
	}
	
	/**
	 * Return the WActor using his ID field
	 * @param id_p Actor's id
	 * @return WActor Actor who match with id_p
	 */
	public WActor getActorByID(int id_p)
	{
		int i;
		for(i=0; i < this.getActorNumber(); i++)
		{
			if(actors.get(i).getId() == id_p){
				break;
			}
				
		}
		return actors.get(i);
	}
	
	/**
	 * Delete the actor specifies by his ID.
	 * @param id_p Actor's id.
	 */
	/*public void deleteActorByID(int id_p)
	{
		ArrayList<WActor> new_actors = new ArrayList<WActor>();
		
		for(int i=0; i<actors.size(); i++)
		{
			if(actors.get(i).getId() != id_p)
				new_actors.add(actors.get(i));
		}
		
		actors = new_actors;
		this.populate();
	}/*
	
	/**
	 * Delete the actor specifies by his position in the List.
	 * @param index Actor index in the actors list
	 */
	/*public void deleteActorByIndex(int index_p)
	{
		/*ArrayList<WActor> new_actors = new ArrayList<WActor>();
		
		for(int i=0; i<actors.size(); i++)
		{
			if(i != index_p)
				new_actors.add(actors.get(i));
		}
		
		actors = new_actors;*/
		/*actors.remove(index_p);
		this.populate();
	}*/
	
	public void deleteActor(WActor actor_p)
	{
		actors.remove(actor_p);
		actors_ordered = getActorsOrderedByLatitude();
	}
	
	/**
	 * Remove all actors in a category.
	 * @param category_p Category to remove.
	 */
	public void deleteCategory(WActorCategory category_p)
	{
		for(int i=0; i< this.getActorNumber(); i++)
		{
			if(actors.get(i).getCategory() == category_p)
			{
				actors.remove(i);
				// avoid two consecutive actores aren't deleted
				if(i!=main_actor_index)
					i=i-1;
			}
		}
	}
	
	/**
	 * This method returns the actor's index using his ID.
	 * @param id_p
	 * @return
	 */
	public int getIndexById(int id_p)
	{
		int return_p = -1;
		for(int i=0; i<this.getActorNumber(); i++)
		{
			if(actors.get(i).getId() == id_p)
			{
				return_p = i;
				break;
			}
		}
		return return_p;
	}
	
	/**
	 * Set a new main actor on the map based on his index.
	 * @param index_p Index to set the new main actor
	 */
	public void setMainActorByIndex(int index_p)
	{
		main_actor_index = index_p;
	}
	
	/**
	 * Set a new main actor on the map based on his ID.
	 * @param id_p Actor's ID
	 */
	public void setMainActorByID(int id_p)
	{
		main_actor_index = this.getIndexById(id_p);
	}
	

	public void setWActorListener(WActorListener listener)
	{
		listeners.add(listener);
	}
	
	@Override
	protected void finalize()
	{
		if(this.isMyLocationEnabled())
			this.disableMyLocation();
		if(this.isCompassEnabled())
			this.disableCompass();
		try {
			super.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param index_p
	 */
	public void deleteActorByIndex(int index_p)
	{
		this.deleteActor(this.getActorByIndex(index_p));
	}
	
	/**
	 * 
	 * @param id_p
	 */
	public void deleteActorByID(int id_p)
	{
		this.deleteActor(this.getActorByID(id_p));
	}
	
	/**
	 * 
	 * @param category_p
	 * @return
	 */
	public List<WActor> getActorByCategory(WActorCategory category_p)
	{
		List<WActor> return_actors = new ArrayList<WActor>();
		
		for(WActor aux_actor : actors)
		{
			if(aux_actor.getCategory() == category_p)
				return_actors.add(aux_actor);
		}
		
		return return_actors;
	}

}