package com.iscbd.wingmaps.actor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.google.android.maps.MapView;
import com.google.android.maps.Projection;
import com.iscbd.wingmaps.category.WActorCategory;
import com.iscbd.wingmaps.map.WPoint;
import com.iscbd.wingmaps.marker.WMarker;

/**
 * 
 * @author Juan Antonio Jimenez Lopez
 *
 */
@SuppressWarnings("deprecation")
public class WActor implements Comparable{
	
	Drawable mMarker;
    
	// actor visibility
	private boolean visibility = true;
	
	// Position of actor
	private WPoint position;
	private static WPoint position_initial = new WPoint(37913350,-4717208);
	
	// Actor avatar & icon
	private boolean icon_created;
	private boolean avatar_created;
	
	// Precision in meters about actor position
	private double precision;
	
	// actor id
	private int id;
	
	// intent when the actor is tapped
	Intent intent;
	
	// Actor name
	private String name;
	
	// 
	private float orientation = 0;			// used by secondary actors
	private float degrees_offset = 0;		// marker offset
	private boolean isOriented = false;
	
	// Information about the actor
	private String information;
	private boolean main_actor;			// true if actor is the main actor, false in other case.
	private String phone_number = "";
	
	// actor's twitter account 
	private String twitter = "";
	
	// actor category
	private WActorCategory category;
	private boolean isCategorized = false;;
	
	// actor's marker and avatar
	WMarker icon;
	WMarker avatar;
	boolean avatar_showed = false;
	
	public static final int MAX_MARKER_WIDTH = 70;
	public static final int MAX_MARKER_HEIGHT = 70;
	public static final int MAX_AVATAR_WIDTH = 70;
	public static final int MAX_AVATAR_HEIGHT = 70;
	
	// Extra actor's information
	private String email;
	private String facebook;
	
	//***************************************************
	//
	//		C O N S T R U C T O R S
	//
	//***************************************************
	public WActor(Context context, int id_p, String name_p, String information_p, boolean main_actor_p, WMarker marker_p)
	{
		id = id_p;
		name = name_p;
		precision = 0;
		information = information_p;
		icon_created = true;
		avatar_created = false;
		icon = marker_p;
		mMarker = marker_p.getDrawable();
		main_actor = main_actor_p;
	}
	
	public WActor(Context context, int id_p, String name_p, String information_p, WActorCategory category_p, boolean main_actor_p, WMarker marker_p)
	{
		id = id_p;
		name = name_p;
		precision = 0;
		information = information_p;
		icon_created = true;
		avatar_created = false;
		icon = marker_p;
		main_actor = main_actor_p;
		setCategory(category_p);
		mMarker = marker_p.getDrawable();
	}
	
	/**
	 * 
	 * @param context
	 * @param id_p
	 * @param name_p
	 * @param information_p
	 * @param position_p
	 * @param main_actor_p
	 * @param icon_p
	 * @param category_p
	 */
	public WActor(Context context, int id_p, String name_p, String information_p, WPoint position_p, boolean main_actor_p, WMarker marker_p){
		id = id_p;
		name = name_p;
		position = position_p;
		precision = 0;
		information = information_p;
		icon_created = true;
		avatar_created = false;
		icon = marker_p;
		main_actor = main_actor_p;
		mMarker = marker_p.getDrawable();
	}
	
	/**
	 * 
	 * @param context
	 * @param id_p
	 * @param name_p
	 * @param information_p
	 * @param position_p
	 * @param main_actor_p
	 * @param icon_p
	 * @param category_p
	 */
	public WActor(Context context, int id_p, String name_p, String information_p, WPoint position_p, boolean main_actor_p, WMarker marker_p, WActorCategory category_p){
		id = id_p;
		name = name_p;
		position = position_p;
		precision = 0;
		information = information_p;
		icon_created = true;
		avatar_created = false;
		main_actor = main_actor_p;
		setCategory(category_p);
		icon = marker_p;
		mMarker = marker_p.getDrawable();
	}
	
	/**
	 * 
	 * @param context
	 * @param id_p
	 * @param name_p
	 * @param information_p
	 * @param position_p
	 * @param main_actor_p
	 * @param icon_p
	 * @param category_p
	 * @param precision_p
	 */
	public WActor(Context context, int id_p, String name_p, String information_p, WPoint position_p, boolean main_actor_p, WMarker marker_p, WActorCategory category_p,  double precision_p){
		id = id_p;
		name = name_p;
		position = position_p;
		precision = precision_p;
		information = new String("");
		icon_created = true;
		avatar_created = false;
		main_actor = main_actor_p;
		setCategory(category_p);
		icon = marker_p;
		mMarker = marker_p.getDrawable();
	}
	
	/**
	 * 
	 * @param context
	 * @param id_p
	 * @param name_p
	 * @param information_p
	 * @param position_p
	 * @param main_actor_p
	 * @param icon_p
	 * @param category_p
	 * @param precision_p
	 * @param avatar_p
	 */
	public WActor(Context context, int id_p, String name_p, String information_p, WPoint position_p, boolean main_actor_p, 
			WMarker marker_p, WActorCategory category_p, double precision_p, WMarker avatar_p){
		id = id_p;
		name = name_p;
		position = position_p;
		precision = precision_p;
		information = new String("");
		icon_created = true;
		avatar_created = true;
		main_actor = main_actor_p;
		setCategory(category_p);
		icon = marker_p;
		avatar = avatar_p;
		mMarker = marker_p.getDrawable();
	}
	
	/**
	 * 
	 * @param context
	 * @param id_p
	 * @param name_p
	 * @param information_p
	 * @param position_p
	 * @param main_actor_p
	 * @param icon_p
	 * @param category_p
	 * @param precision_p
	 * @param avatar_p
	 * @param twitter_p
	 */
	public WActor(Context context, int id_p, String name_p, String information_p, WPoint position_p, boolean main_actor_p, 
			WMarker marker_p, WActorCategory category_p, double precision_p, WMarker avatar_p, String twitter_p){
		id = id_p;
		name = name_p;
		position = position_p;
		precision = precision_p;
		information = information_p;
		twitter = twitter_p;
		main_actor = main_actor_p;
		setCategory(category_p);
		icon = marker_p;
		avatar = avatar_p;
		mMarker = marker_p.getDrawable();
	}
	
	
	//***************************************************
	//
	//			M E T H O D S
	//
	//***************************************************
	public boolean isCategorized()
	{
		return isCategorized;
	}
	
	/**
	 * Draws the actor in the MapView if the orientation is disable
	 * @param canvas Canvas to draw the actor
	 * @param mapView 
	 * @param shadow
	 * @param when
	 * @return
	 */
    public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) 
	{
		Point point_p = new Point();
		Projection projection = mapView.getProjection();
		projection.toPixels(getPosition(), point_p);
		
		if(isVisible())
		{
			if(isAvatarShowed())
				avatar.draw(point_p.x, point_p.y, canvas, mapView);
			else
			{
				if(isCategorized())
				{
					if(category.isVisible())
						category.getMarker().draw(point_p.x, point_p.y, canvas, mapView);
				}
				else
					icon.draw(point_p.x, point_p.y, canvas, mapView);
			}
		}
		return true;
	}
	
	/**
	 * Draws the actor in the MapView if the orientation is enable
	 * @param canvas
	 * @param mapView
	 * @param shadow
	 * @param when
	 * @param compass
	 * @return
	 */
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when, float compass) 
	{
		Point point_p = new Point();
		Projection projection = mapView.getProjection();
		projection.toPixels(getPosition(), point_p);
		
		if(isVisible())
		{
			if(this.isMainActor() == false)
				compass = this.getOrientation();
			
			if(isAvatarShowed())
			{
				avatar.draw(point_p.x, point_p.y, canvas, mapView, compass);
			}
			else
			{
				
				if(isCategorized())
				{
					if(category.isVisible())
						category.getMarker().draw(point_p.x, point_p.y, canvas, mapView, compass);
				}
				else
				{
					icon.draw(point_p.x, point_p.y, canvas, mapView, compass);
				}
			}
		}
		return true;
	}
	
	
	/**
	 * Return the WActor's position
	 * @return position
	 */
	public WPoint getPosition(){
		return position;
	}
	
	/**
	 * Set the WActor's position
	 * @param position_p
	 */
	public void setPosition(WPoint position_p){
		position = position_p;
	}
	
	/**
	 * Returns the WActor's name
	 * @return name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Set the WActor's name
	 * @param name_p
	 */
	public void setName(String name_p){
		name = name_p;
	}
	
	/**
	 * Set the WActor's WMarker 
	 * @param marker_p WMarker object to set as a marker
	 */
	public void setMarker(WMarker marker_p)
	{
		icon = marker_p;
		icon.checkDimensions(MAX_MARKER_WIDTH, MAX_MARKER_HEIGHT);
	}
	

	public void setMarker(Drawable marker_p)
	{
		icon = new WMarker(marker_p);
		icon.checkDimensions(MAX_MARKER_WIDTH, MAX_MARKER_HEIGHT);
		mMarker = marker_p;
	}
	
	/**
	 * 
	 * @param marker_p
	 */
	public void setMarker(Bitmap marker_p)
	{
		icon = new WMarker(marker_p);
		icon.checkDimensions(MAX_MARKER_WIDTH, MAX_MARKER_HEIGHT);
	}
	
	/**
	 * 
	 * @param avatar_p
	 */
	public void setAvatar(WMarker avatar_p)
	{
		avatar = avatar_p;
		avatar_showed = true;
		avatar.checkDimensions(MAX_AVATAR_WIDTH, MAX_AVATAR_HEIGHT);
	}
	
	/**
	 * 
	 * @param avatar_p
	 */
	public void setAvatar(Drawable avatar_p)
	{
		avatar = new WMarker(avatar_p);
		avatar.checkDimensions(MAX_AVATAR_WIDTH, MAX_AVATAR_HEIGHT);
	}
	
	/**
	 * 
	 * @param avatar_p
	 */
	public void setAvatar(Bitmap avatar_p)
	{
		avatar = new WMarker(avatar_p);
		avatar.checkDimensions(MAX_AVATAR_WIDTH, MAX_AVATAR_HEIGHT);
	}
	
	public Drawable getMarker(int state)
	{
		return mMarker;
	}
	
	public WMarker getIcon()
	{
		return icon;
	}
	
	public WMarker getAvatar()
	{
		return avatar;
	}
	
	/**
	 * 
	 * @return id
	 */
	public int getId(){
		return id;
	}
	
	/**
	 * 
	 * @param id_p
	 */
	public void setId(int id_p){
		id = id_p;
	}
	
	/**
	 * 
	 * @return precision
	 */
	public double getPrecision(){
		return precision;
	}
	
	/**
	 * 
	 * @param precision_p
	 */
	public void setPrecision(double precision_p){
		precision = precision_p;
	}
	
	/**
	 * 
	 * @return information
	 */
	public String getInformation(){
		return information;
	}
	
	/**
	 * 
	 * @param information_p
	 */
	public void setInformation(String information_p){
		information = information_p;
	}
	
	/**
	 * 
	 * @return twitter
	 */
	public String getTwitter(){
		return twitter;
	}
	
	/**
	 * 
	 * @param twitter_p
	 */
	public void setTwitter(String twitter_p){
		twitter = twitter_p;
	}
	
	/**
	 * 
	 * @return visibility
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
	public WActorCategory getCategory(){
		return category;
	}
	
	/**
	 * 
	 * @param category_p
	 */
	public void setCategory(WActorCategory category_p){
		category = category_p;
		isCategorized = true;
	}
	
	public void removeCategory()
	{
		category = null;
		isCategorized = false;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isAvatar()
	{
		if(avatar != null)
			return true;
		else
			return false;
	}
	
	/**
	 * w
	 * @return
	 */
	public boolean isIcon()
	{
		return icon_created;
	}
	public boolean isMainActor()
	{
		return main_actor;
	}
	
	public void phoneCallToActor(Context context)
	{
		 /*String uri = "tel:" + phone_number.trim() ;
		 Intent intent = new Intent(Intent.ACTION_CALL);
		 intent.setData(uri.parse(uri));
		 activity.startActivity(intent);*/
		 Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone_number)); 
         context.startActivity(callIntent);
	}
	

	/**
	 * Set a new phone number.
	 * @param number_p Phone number to set
	 */
	public void setPhoneNumber(String number_p)
	{
		phone_number = number_p;
	}
	
	/**
	 * Return a String with the Actor's phone number
	 * @return Actor's phone number.
	 */
	public String getPhoneNumber()
	{
		return phone_number;
	}
	
	/**
	 * Set the secondary actors orientation.
	 * @param mAzimuth Degrees.
	 */
	public void setOrientation(float mAzimuth)
	{
		orientation = mAzimuth;
		isOriented = true;
	}
	
	/**
	 * Return the orientation field
	 * @return Actor orientation
	 */
	public float getOrientation()
	{
		return orientation;
	}
	
	/**
	 * 
	 * @param offset degrees offset created by marker
	 */
	public void setOffset(float offset)
	{
		degrees_offset = offset;
	}
	
	/**
	 * 
	 * @return
	 */
	public float getOffset()
	{
		return degrees_offset;
	}
	
	public boolean isOriented()
	{
		return isOriented;
	}

	public Intent getIntent()
	{
		return intent;
	}
	
	public void setIntent(Intent intent_p)
	{
		intent = intent_p;
	}
	
	public boolean isAvatarShowed()
	{
		return avatar_showed;
	}
	
	public void setAvatarShow(boolean cond)
	{
		avatar_showed = cond;
	}
	
	public int compareTo(Object o) {
		WActor actor2 = (WActor) o;
		
		if(this.position.getLatitudeE6() < actor2.position.getLatitudeE6())
			return 1;
		else if(this.position.getLatitudeE6() == actor2.position.getLatitudeE6())
			return 0;
		else 
			return -1;
		
	}
	
	public boolean isHit(float x, float y, MapView mapView)
	{
		boolean return_value=false;
		Projection projection = mapView.getProjection();
		Point p = new Point();
		projection.toPixels(position, p);
		if(isVisible())
		{
			if(isCategorized())
			{
				if(category.isVisible())
					return_value = category.getMarker().isHit(x, y, p.x, p.y);
			}
			else
			{
				if(isAvatarShowed())
				{
					return_value = avatar.isHit(x, y, p.x, p.y);
				}
				else
				{
					return_value = icon.isHit(x, y, p.x, p.y);
				}
			}
		}
		
		return return_value;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

}
