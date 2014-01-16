package com.iscbd.wingmaps.map;

import android.content.Context;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.Overlay;
import com.iscbd.wingmaps.actor.WActor;
import com.iscbd.wingmaps.actor.WActorLayer;
import com.iscbd.wingmaps.area.WAreaLayer;
import com.iscbd.wingmaps.info.WInfoLayer;
import com.iscbd.wingmaps.place.WPlaceLayer;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Juan Antonio Jimenez Lopez < ruchin10@gmail.com >
 *
 */
public class WMap{
	/**
	 * Interface to catch tap events on the map
	 * @author Juan Antonio Jimenez Lopez < ruchin10@gmail.com >
	 *
	 */
	public interface WMapListener
	{
		/**
		 * Receives a WPoint where user has tapped
		 * @param hitpoint
		 */
		void onMapTapped(WPoint hitpoint);
	}
	
	/**
	 * WMap's private class to use compass functions
	 * @author Juan Antonio Jimenez Lopez < ruchin10@gmail.com >
	 *
	 */
	private class WOverlay extends MyLocationOverlay{
		ArrayList<WMapListener> listeners = new ArrayList<WMapListener>();
		public WOverlay(Context context, WView WView) {
			super(context, WView);
			
		}
		@Override
		public boolean onTap(GeoPoint hitpoint, MapView WView)
		{
			for(int i=0; i<listeners.size(); i++)
				listeners.get(i).onMapTapped(WPoint.toWPoint(hitpoint));
			return super.onTap(hitpoint, WView);
			//return false;
		}
		/**
		 * Sets a new WMapListener to the listeners list
		 * @param listener
		 */
		public void setWMapListener(WMapListener listener)
		{
			listeners.add(listener);
		}
		
	}
	
	private int map_type=0;		// 0->Map  1->Satellite   2->hybrid
	private MapController map_controller = null;
	private WView map_view;
	private int visibility;		// 0-> Visible		4-> Invisible
	Context context;
	private int num_actor_layers=0;
	private int num_place_layers=0;
	
	private WOverlay compass;
	
	//ArrayList<WMapListener> listeners = new ArrayList<WMapListener>();
	
	public static final int SATELLITE_MAP_TYPE = 1;
	public static final int CLASSIC_MAP_TYPE = 0;
	
	private List<Overlay> initial = new ArrayList<Overlay>();
	private List<WPlaceLayer> place_layers = new ArrayList<WPlaceLayer>();
	private List<WActorLayer> actor_layers = new ArrayList<WActorLayer>();
	private List<WInfoLayer> info_layers = new ArrayList<WInfoLayer>();
	private List<WAreaLayer> area_layers = new ArrayList<WAreaLayer>();
	
	//********************************************
	//
	// 		C O N S T R U C T O R S	
	//
	//********************************************
	/**
	 * WMap constructor (default)
	 * @param context_p Context which the map is running
	 * @param map_p		WView Object
	 */
	public WMap(Context context_p, WView map_p){
		map_view = map_p;
		context = context_p;
		map_controller = map_view.getController();
		// Set the default center & zoom
		WPoint center = new WPoint((int)(37884722),(int)(-4778889));
		map_controller.setCenter(center);
		try {
			setZoom(6);
		} catch (WMapException e) {}
		// Create compass overlay
		compass = new WOverlay(context,map_view);
		map_view.getOverlays().add(compass);
		//map_view.postInvalidate();
		initial = map_view.getOverlays();
	}
	
	/**
	 * WMap Constructor with map center (WPoint)
	 * @param context_p Context which the map is running
	 * @param map_p		WView Object
	 * @param center_p	WMap's geographical center
	 */
	public WMap(Context context_p, WView map_p, WPoint center_p){
		map_view = map_p;
		context = context_p;
		map_controller = map_view.getController();
		map_controller.setCenter(center_p);
		// Setting default zoom
		try {
			setZoom(6);
		} catch (WMapException e) {}
		compass = new WOverlay(context,map_view);
		map_view.getOverlays().add(compass);
		//map_view.postInvalidate();
		initial = map_view.getOverlays();
	}
	
	/**
	 * WMap Constructor with map center (WPoint) and zoom
	 * @param context_p 	Context which the map is running
	 * @param map_p			WView Object
	 * @param center_p		WMap's geographical center
	 * @param zoom_p		WMap's geographical zoom
	 * @throws WMapException
	 */
	public WMap(Context context_p, WView map_p, WPoint center_p, int zoom_p) throws WMapException{
		map_view = map_p;
		context = context_p;
		map_controller = map_view.getController();
		map_controller.setCenter(center_p);
		setZoom(zoom_p);
		compass = new WOverlay(context,map_view);
		map_view.getOverlays().add(compass);
		
		initial = map_view.getOverlays();
	}
	
	/**
	 * WMap Constructor with map center (WPoint), zoom and map type
	 * @param context_p 	Context which the map is running
	 * @param map_p			WView Object
	 * @param center_p		WMap's geographical center
	 * @param zoom_p		WMap's geographical zoom
	 * @param map_type_p	WMap's 
	 * @throws WMapException
	 */
	public WMap(Context context_p, WView map_p, WPoint center_p, int zoom_p, int map_type_p) throws WMapException{
		map_view = map_p;
		context = context_p;
		map_controller = map_view.getController();
		map_controller.setCenter(center_p);
		setZoom(zoom_p);
		map_type = map_type_p;
		setMapType(map_type);
		compass = new WOverlay(context,map_view);
		map_view.getOverlays().add(compass);
		// Checking zoom
		map_view.postInvalidate();
		initial = map_view.getOverlays();
	}
	
	/**
	 * WMap constructor with latitude and longitude
	 * @param context_p 	Context which the map is running
	 * @param map_p			WView Object
	 * @param center_lat_e6	WMap's geographical center latitude
	 * @param center_lon_e6	WMap's geographical center longitude
	 * @throws WMapException
	 */
	public WMap(Context context_p, WView map_p, double center_lat, double center_lon) throws WMapException{
		context = context_p;
		// checking map center, notify if the WPoint isn't correct
		WPoint center_aux = new WPoint((int)(center_lat*1E6), (int)(center_lon*1E6));
		if(Math.abs(center_lat) > 90.0 || Math.abs(center_lon) > 179.0)
			throw new WMapException("Center out of range. Center changed to (" + center_aux.getLatitudeE6() +", " + center_aux.getLongitudeE6() +").");
		
		map_view = map_p;
		map_controller = map_view.getController();
		map_controller.setCenter(center_aux);
		map_controller.setZoom(6);
		
		compass = new WOverlay(context,map_view);
		map_view.getOverlays().add(compass);
		
		map_view.postInvalidate();
		
		initial = map_view.getOverlays();
	}
	
	/**
	 * WMap constructor with latitude, longitude & zoom
	 * @param context_p 	Context which the map is running
	 * @param map_p			WView Object
	 * @param center_lat_e6	WMap's geographical center latitude
	 * @param center_lon_e6	WMap's geographical center longitude
	 * @param zoom_p		WMap's zoom
	 * @throws WMapException
	 */
	public WMap(Context context_p, WView map_p, double center_lat, double center_lon, int zoom_p) throws WMapException{
		map_view = map_p;
		map_controller = map_view.getController();
		context = context_p;
		// checking map center and zoom center, notify if the center or zoom aren't correct
		WPoint center_aux = new WPoint((int)(center_lat*1E6), (int)(center_lon*1E6));
		map_controller.setCenter(center_aux);
		if(Math.abs(center_lat) > 90.0 || Math.abs(center_lon) > 179.0)
			// center error and zoom under limit
			if(zoom_p < 1){
				map_controller.setZoom(1);
				throw new WMapException("Center and Zoom out of range. Zoom changed to 1 and Center changed to (" + (center_aux.getLatitudeE6()/1E6) +", " + (center_aux.getLongitudeE6()/1E6) +").");
			}
			// center error and zoom upper limit
			else if(zoom_p > 21){
				map_controller.setZoom(21);
				throw new WMapException("Center and Zoom out of range. Zoom changed to 21 and Center changed to (" + (center_aux.getLatitudeE6()/1E6) +", " + (center_aux.getLongitudeE6()/1E6) +").");
			}
			// center error, zoom ok
			else{
				map_controller.setZoom(zoom_p);
				throw new WMapException("Center out of range. Center changed to (" + (center_aux.getLatitudeE6()/1E6) +", " + (center_aux.getLongitudeE6()/1E6) +").");
			}
		// center ok, zoom under limit
		else if(zoom_p < 1){
			map_controller.setZoom(1);
			throw new WMapException("Center and Zoom out of range. Zoom changed to 1.");
		}
		// center ok, zoom upper limit
		else if(zoom_p > 21){
			map_controller.setZoom(21);
			throw new WMapException("Center and Zoom out of range. Zoom changed to 21.");
		}
		// center and zoom ok
		else
			map_controller.setZoom(zoom_p);
		
		compass = new WOverlay(context,map_view);
		map_view.getOverlays().add(compass);
		
		map_view.postInvalidate();
		
		initial = map_view.getOverlays();
	}
	
	/**
	 * WMap constructor with latitude, longitude, zoom & map type
	 * @param context_p 	Context which the map is running
	 * @param map_p			WView Object
	 * @param center_lat_e6	WMap's geographical center latitude
	 * @param center_lon_e6	WMap's geographical center longitude
	 * @param zoom_p		WMap's zoom
	 * @param map_type_p	WMap's map type
	 * @throws WMapException
	 */
	public WMap(Context context_p, WView map_p, double center_lat, double center_lon, int zoom_p, int map_type_p) throws WMapException{
		map_view = map_p;
		map_controller = map_view.getController();
		context = context_p;
		// checking map center, zoom center and map type, notify if the center or zoom aren't correct
		WPoint center_aux = new WPoint((int)(center_lat*1E6), (int)(center_lon*1E6));
		map_controller.setCenter(center_aux);
	
		if(map_type_p == CLASSIC_MAP_TYPE || map_type_p == SATELLITE_MAP_TYPE)
		{
			// --- map_type OK
			setMapType(map_type_p);
			if(Math.abs(center_lat) > 90.0 || Math.abs(center_lon) > 179.0){
				// center error and zoom under limit
				if(zoom_p < 1){
					map_controller.setZoom(1);
					throw new WMapException("Center and Zoom out of range. Zoom changed to 1 and Center changed to (" + (center_aux.getLatitudeE6()/1E6) +", " + (center_aux.getLongitudeE6()/1E6) +").");
				}
				// center error and zoom upper limit
				else if(zoom_p > 21){
					map_controller.setZoom(21);
					throw new WMapException("Center and Zoom out of range. Zoom changed to 21 and Center changed to (" + (center_aux.getLatitudeE6()/1E6) +", " + (center_aux.getLongitudeE6()/1E6) +").");
				}
				// center error, zoom ok
				else{
					map_controller.setZoom(zoom_p);
					throw new WMapException("Center out of range. Center changed to (" + (center_aux.getLatitudeE6()/1E6) +", " + (center_aux.getLongitudeE6()/1E6) +").");
				}
			}
			// center ok, zoom under limit
			else if(zoom_p < 1){
				map_controller.setZoom(1);
				throw new WMapException("Center and Zoom out of range. Zoom changed to 1.");
			}
			// center ok, zoom upper limit
			else if(zoom_p > 21){
				map_controller.setZoom(21);
				throw new WMapException("Center and Zoom out of range. Zoom changed to 21.");
			}
			// center and zoom ok
			else
				map_controller.setZoom(zoom_p);
		}
		// --- map_type error
		else
		{
			if(Math.abs(center_lat) > 90.0 || Math.abs(center_lon) > 179.0){
				// center error and zoom under limit
				if(zoom_p < 1){
					map_controller.setZoom(1);
					throw new WMapException("Map type, Center and Zoom out of range. Zoom changed to 1 and Center changed to (" + (center_aux.getLatitudeE6()/1E6) +", " + (center_aux.getLongitudeE6()/1E6) +").");
				}
				// center error and zoom upper limit
				else if(zoom_p > 21){
					map_controller.setZoom(21);
					throw new WMapException("Map type, Center and Zoom out of range. Zoom changed to 21 and Center changed to (" + (center_aux.getLatitudeE6()/1E6) +", " + (center_aux.getLongitudeE6()/1E6) +").");
				}
				// center error, zoom ok
				else{
					map_controller.setZoom(zoom_p);
					throw new WMapException("Map type, Center out of range. Center changed to (" + (center_aux.getLatitudeE6()/1E6) +", " + (center_aux.getLongitudeE6()/1E6) +").");
				}
			}
			// center ok, zoom under limit
			else if(zoom_p < 1){
				map_controller.setZoom(1);
				throw new WMapException("Map type, Center and Zoom out of range. Zoom changed to 1.");
			}
			// center ok, zoom upper limit
			else if(zoom_p > 21){
				map_controller.setZoom(21);
				throw new WMapException("Map type, Center and Zoom out of range. Zoom changed to 21.");
			}
			// center and zoom ok
			else
				map_controller.setZoom(zoom_p);
		}
		
		compass = new WOverlay(context,map_view);
		map_view.getOverlays().add(compass);
		
		map_view.postInvalidate();
		
		initial = map_view.getOverlays();
	}
	
	//*****************************************
	//
	//			M E T H O D S
	//
	//*****************************************
	/**
	 * Return the WMap's geographical center
	 * @return
	 */
	public WPoint getCenter(){
		return WPoint.toWPoint(map_view.getMapCenter());
	}
	
	/**
	 * Set a new map center
	 * @param center_p Object WPoint to use as WMap's geographical center
	 */
	public void setCenter(WPoint center_p){
		map_controller.animateTo(center_p);
		map_controller.setCenter(center_p);
		//////map_view.postInvalidate();
	}
	
	/**
	 * Set a new map center
	 * @param latitude_E6	Int value to use as WMap's geographical center latitude
	 * @param longitude_E6	Int value to use as WMap's geographical center longitude
	 * @throws WMapException
	 */
	public void setCenter(double latitude, double longitude) throws WMapException{
		WPoint center_aux = new WPoint((int)(latitude*1E6), (int)(longitude*1E6));
		// Move the map to the new center
		map_controller.animateTo(center_aux);
		map_controller.setCenter(center_aux);
		// checking the new center to notify any error
		if(Math.abs(latitude) > 90.0 || Math.abs(longitude) > 179.0)
			throw new WMapException("Center out of range. Center changed to (" + (center_aux.getLatitudeE6()/1E6) +", " + (center_aux.getLongitudeE6()/1E6) +").");
	}
	
	/**
	 * Returns the actual zoom
	 * @return 
	 */
	public Integer getZoom(){
		return map_view.getZoomLevel();
	}
	
	/**
	 * Sets a new zoom to use in WMap
	 * @param zoom_p Integer value between 1 and 21.
	 */
	public void setZoom(int zoom_p) throws WMapException{
		if(zoom_p < 1){
			map_controller.setZoom(1);
			throw new WMapException("Zoom out of range. Zoom changed to 1.");
		}
		// center ok, zoom upper limit
		else if(zoom_p > 21){
			map_controller.setZoom(21);
			throw new WMapException("Zoom out of range. Zoom changed to 21.");
		}
		else
			map_controller.setZoom(zoom_p);
	}
	
	/**
	 * Returns the WMap's map type. Integer value that's is return can be CLASSIC_MAP_TYPE or SATELLITE_MAP_TYPE
	 * @return
	 */
	public int getMaptype(){
		return map_type;
	}
	
	/**
	 * Set a new map type
	 * @param map_type_p Integer value which could be 0 (CLASSIC_MAP_TYPE) or 1 (SATELLITE_MAP_TYPE)
	 * @throws WMapException
	 */
	public void setMapType(int map_type_p) throws WMapException{
		if(map_type_p == 0 || map_type_p == 1){
			map_type = map_type_p;
			if (map_type == 0)
				map_view.setSatellite(false);
			else
				map_view.setSatellite(true);
		}
		else
			throw new WMapException("Map type not defined.");
	}
	
	/**
	 * Show the zoom controls on the WView
	 * @param on If this boolean value is True, zoom controls are show. On the other hand, False don't show the controls.
	 */
	public void showZoomControls(boolean on){
		map_view.setBuiltInZoomControls(on);
	}
	
	/**
	 * Integrate a WingMaps's Layer in WMap.
	 * @param overlay_p WActorLayer object to include in this WMap object.
	 */
	public void addOverlay(WActorLayer overlay_p)
	{
		overlay_p.setMap(this);
		num_actor_layers++;
		
		actor_layers.add(overlay_p);
		
		List<Overlay> aux = new ArrayList<Overlay>();
		aux.addAll(place_layers);
		aux.addAll(actor_layers);
		aux.addAll(info_layers);
		map_view.getOverlays().equals(aux);
		
		//erase all actors
		if(actor_layers != null)
			for(int i=0; i<actor_layers.size(); i++)
			{
				map_view.getOverlays().remove(actor_layers.get(i));
			}
		actor_layers.add(overlay_p);
		

		//erase all places
		/*if(place_layers != null)
			for(int i=0; i<place_layers.size(); i++)
			{
				map_view.getOverlays().remove(place_layers.get(i));
			}*/
		
		//erase all info
		if(info_layers != null)
			for(int i=0; i<info_layers.size(); i++)
			{
				map_view.getOverlays().remove(info_layers.get(i));
			}
		
		// update overlay list
		if(actor_layers != null)
			for(int i=0; i<actor_layers.size(); i++)
			{
				map_view.getOverlays().add(actor_layers.get(i));
			}
		
		if(info_layers != null)
			for(int i=0; i<info_layers.size(); i++)
			{
				map_view.getOverlays().add(info_layers.get(i));
			}
		//map_view.invalidate();
	}
	
	/**
	 * Integrate a WingMaps's Layer in WMap.
	 * @param overlay_p WAreaLayer object to include in this WMap object.
	 */
	public void addOverlay(WAreaLayer overlay_p)
	{
		//erase all areas
		if(area_layers != null)
			for(int i=0; i< area_layers.size(); i++)
			{
				map_view.getOverlays().remove(area_layers.get(i));
			}
		area_layers.add(overlay_p);
		
		//erase all places
		if(place_layers != null)
			for(int i=0; i< place_layers.size(); i++)
			{
				map_view.getOverlays().remove(place_layers.get(i));
			}
		
		//erase all actors
				if(actor_layers != null)
					for(int i=0; i<actor_layers.size(); i++)
					{
						map_view.getOverlays().remove(actor_layers.get(i));
					}
		
		//erase all info
		if(info_layers != null)
			for(int i=0; i<info_layers.size(); i++)
			{
				map_view.getOverlays().remove(info_layers.get(i));
			}
		
		// update overlay list
		if(area_layers != null)
			for(int i=0; i<area_layers.size(); i++)
			{
				map_view.getOverlays().add(area_layers.get(i));
			}
		
		
		if(actor_layers != null)
			for(int i=0; i<actor_layers.size(); i++)
			{
				map_view.getOverlays().add(actor_layers.get(i));
			}
		
		// update overlay list
		if(place_layers != null)
			for(int i=0; i<place_layers.size(); i++)
			{
				map_view.getOverlays().add(place_layers.get(i));
			}
		
		if(info_layers != null)
			for(int i=0; i<info_layers.size(); i++)
			{
				map_view.getOverlays().add(info_layers.get(i));
			}
		
		//map_view.invalidate();
	}
	
	/**
	 * Integrate a WingMaps's Layer in WMap.
	 * @param overlay_p WPlaceLayer object to include in this WMap object.
	 */
	public void addOverlay(WPlaceLayer overlay_p)
	{
		overlay_p.setMap(this);
		num_place_layers++;
		
		//erase all places
		if(place_layers != null)
			for(int i=0; i< place_layers.size(); i++)
			{
				map_view.getOverlays().remove(place_layers.get(i));
			}
		place_layers.add(overlay_p);
		
		//erase all info
		if(info_layers != null)
			for(int i=0; i<info_layers.size(); i++)
			{
				map_view.getOverlays().remove(info_layers.get(i));
			}
		
		// update overlay list
		if(actor_layers != null)
			for(int i=0; i<actor_layers.size(); i++)
			{
				map_view.getOverlays().add(actor_layers.get(i));
			}
		
		if(place_layers != null)
			for(int i=0; i<place_layers.size(); i++)
			{
				map_view.getOverlays().add(place_layers.get(i));
			}
		
		if(info_layers != null)
			for(int i=0; i<info_layers.size(); i++)
			{
				map_view.getOverlays().add(info_layers.get(i));
			}
	}
	
	
	/**
	 * Integrate a WingMaps's Layer in WMap.
	 * @param overlay_p WInfoLayer object to include in this WMap object.
	 */
	public void addOverlay(WInfoLayer overlay_p)
	{
		info_layers.add(overlay_p);
		map_view.getOverlays().add(overlay_p);
	}
	
	/**
	 * Returns the WMap's visibility
	 * @return Returns True if WMap object is visible, False otherwise
	 */
	public boolean isVisible()
	{
		if(visibility == WView.VISIBLE)
			return false;
		else
			return true;
	}
	
	/**
	 * 
	 * @param cond Boolean value, if this value is True, WMap will be visible; if cond is False, WMap will be invisible.
	 */
	public void setVisible(boolean cond)
	{
		if (cond == true)
			visibility = WView.VISIBLE;
		else
			visibility = WView.INVISIBLE;
		map_view.setVisibility(visibility);
	}
	
	/**
	 * Change the map center and zoom to adjust the map to two points.
	 * @param left_p First WPoint to adjust
	 * @param right_p Second WPoint to adjust
	 */
	public void adjustMap(WPoint left_p, WPoint right_p)
	{
		// we calculate the new map center
		WPoint new_center = new WPoint((int)((right_p.getLatitudeE6() + left_p.getLatitudeE6())/ 2),(int)(((left_p.getLongitudeE6() + right_p.getLongitudeE6())/ 2)));
		setCenter(new_center);
		map_controller.zoomToSpan(Math.abs(left_p.getLatitudeE6()-right_p.getLatitudeE6()), Math.abs(left_p.getLongitudeE6()-right_p.getLongitudeE6()));
		////map_view.postInvalidate();
	}
	
	/**
	 * Adjust the map to show the farthest point and map center
	 * @param farthest_p  WPoint object to adjust the map.
	 */
	public synchronized void adjustMap(WPoint farthest_p)
	{
		int lat_span, lon_span;
		lat_span = 2*Math.abs(this.getCenter().getLatitudeE6() - farthest_p.getLatitudeE6());
		lon_span = 2*Math.abs(this.getCenter().getLongitudeE6() - farthest_p.getLongitudeE6());
		map_controller.zoomToSpan(lat_span, lon_span);
	}
	
	/**
	 * Get the map scale in meters
	 * @return Map scale with the current Zoom
	 */
	public double getScale()
	{
		return (2*591657550.5) / Math.pow(2, getZoom());
	}
	
	/**
	 * Move the map center to the North.
	 */
	public void moveMapToUp()
	{
		WPoint new_center = new WPoint((int)(getCenter().getLatitudeE6()+0.6*getScale()), getCenter().getLongitudeE6());
		
		setCenter(new_center);
	}
	
	/**
	 * Move the map center to the South
	 */
	public void moveMapToDown()
	{
		WPoint new_center = new WPoint((int)(getCenter().getLatitudeE6()-0.6*getScale()), getCenter().getLongitudeE6());
		
		setCenter(new_center);
	}
	
	/**
	 * Move the map center to the East
	 */
	public void moveMapToRight()
	{
		WPoint new_center = new WPoint(getCenter().getLatitudeE6(), (int)(getCenter().getLongitudeE6()+0.6*getScale()));
		
		setCenter(new_center);
	}
	
	/**
	 * Move the map center to the West
	 */
	public void moveMapToLeft()
	{
		WPoint new_center = new WPoint(getCenter().getLatitudeE6(), (int)(getCenter().getLongitudeE6()-0.6*getScale()));
		setCenter(new_center);
	}
	
	/**
	 * Increase the map zoom in one unit
	 * @throws WMapException 
	 * 
	 */
	public void zoomIn() throws WMapException
	{
		setZoom(getZoom()+1);
	}
	
	/**
	 * Decrease the map zoom in one unit
	 * @throws WMapException 
	 * 
	 */
	public void zoomOut() throws WMapException
	{
		setZoom(getZoom()-1);
	}
	
	/**
	 * Show the compass in top-left corner of the screen
	 */
	public void showCompass()
	{
		compass.enableCompass();
	}
	
	/**
	 * Hide the compass in top-left corner of the screen
	 */
	public void hideCompass()
	{
		compass.disableCompass();
	}
	
	/**
	 * Returns the compass value of the device.
	 * @return
	 */
	public float getCompass(){
		if(compass.isCompassEnabled())
			return compass.getOrientation();
		else
			return -1;
	}
	
	/**
	 * Returns True if compass is enabled; False otherwise
	 * @return
	 */
	public boolean isCompassEnabled(){
		return compass.isCompassEnabled();
	}
	
	/**
	 * Change the map center and zoom to adjust the map to two actors.
	 * @param left_p First WActor to adjust
	 * @param right_p Second WActor to adjust
	 */
	public void adjustMapByActors(WActor actor1, WActor actor2)
	{
		adjustMap(actor1.getPosition(),actor2.getPosition());
	}
	
	/*public void adjustMapByPlace(WPlace place)
	{
		
	}*/	

	/**
	 * Returns the WMap's context
	 * @return
	 */
	public Context getContext()
	{
		return context;
	}
	
	/**
	 * Returns the WView which WMap is using.
	 * @return
	 */
	public WView getMapview(){
		return map_view;
	}
	
	/**
	 * Returns the MapController
	 * @return
	 */
	public MapController getMapController(){
		return map_controller;
	}

	public void postInvalidate() {
		//map_view.invalidate();
		map_view.postInvalidate();
	}
	
	public void invalidate() {
		map_view.invalidate();
	}
	
	/**
	 * Sets a WMapListener to control all tap events which are produce on the map.
	 * @param listener WMapListener
	 */
	public void setWMapListener(WMapListener listener)
	{
		compass.setWMapListener(listener);
	}
	
	
	public void disableResources()
	{
		try {
			for(WActorLayer layer : actor_layers)
			{
				if(layer.isCompassEnabled())
					layer.disableCompass();
				if(layer.isMyLocationEnabled())
					layer.disableMyLocation();
			}
			
			finalize();
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

