package com.iscbd.wingmaps.place;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;
import com.iscbd.wingmaps.category.WPlaceCategory;
import com.iscbd.wingmaps.map.WPoint;
import com.iscbd.wingmaps.marker.WMarker;

/**
 * 
 * @author Juan Antonio Jimenez Lopez < ruchin10@gmail.com >
 *
 */
public class WPlace implements Comparable {
	Drawable mMarker;
	WPlaceCategory category = null;
	boolean isCategorized = false;
	int id;
	WMarker avatar;
	boolean avatar_showed = false;;
	WMarker icon;
	String name;
	String information;
	WPoint position;
	String address;
	Context context;
	
	boolean visibility = true;
	
	String phone_number = "";
	String twitter = "";
	
	public static final int MAX_MARKER_WIDTH = 70;
	public static final int MAX_MARKER_HEIGHT = 70;
	public static final int MAX_AVATAR_WIDTH = 70;
	public static final int MAX_AVATAR_HEIGHT = 70;
	
	//***********************************************************
	//
	//		C O N S T R U C T O R S
	//
	//***********************************************************
	
	/**
	 * Place constructor using his id, position (WPoint), name, information, and marker
	 * @param id_p
	 * @param position_p
	 * @param name_p
	 * @param info_p
	 * @param context_p
	 * @param marker_p
	 */
	public WPlace(int id_p, WPoint position_p, String name_p, String info_p, Context context_p, WMarker marker_p) {
		context = context_p;
		id = id_p;
		position = position_p;
		name = name_p;
		information = info_p;
		icon = marker_p;
		address = position.getAddress();
	}
	
	/**
	 * Place constructor using his id, position (latitude & longitude), name, information, and marker
	 * @param id_p
	 * @param lat_p
	 * @param lon_p
	 * @param name_p
	 * @param info_p
	 * @param context_p
	 * @param marker_p
	 */
	public WPlace(int id_p, double lat_p, double lon_p, String name_p, String info_p, Context context_p, WMarker marker_p) {
		context = context_p;
		id = id_p;
		position = new WPoint(lat_p*1E6, lon_p*1E6);
		name = name_p;
		information = info_p;
		icon = marker_p;
		address = position.getAddress();
	}
	
	/**
	 * Place constructor using his id, position (latitude & longitude in E6), name, information, and marker
	 * @param id_p
	 * @param latE6
	 * @param lonE6
	 * @param name_p
	 * @param info_p
	 * @param context_p
	 * @param marker_p
	 */
	public WPlace(int id_p, int latE6, int lonE6, String name_p, String info_p, Context context_p, WMarker marker_p) {
		context = context_p;
		id = id_p;
		position = new WPoint(latE6, lonE6);
		name = name_p;
		information = info_p;
		icon = marker_p;
		address = position.getAddress();
	}
		
	//***********************************************************
	//
	//		M E T H O D S
	//
	//***********************************************************
	/**
	 * Receives a pixels coordinates (x,y) and return True if the place marker is inside this position
	 * @param x
	 * @param y
	 * @param mapView
	 * @return
	 */
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
	
	/**
	 * Compare the latitude between two places to show the places orders by latitude
	 */
	public int compareTo(Object o) {
		WPlace place2 = (WPlace) o;
		
		if(this.position.getLatitudeE6() < place2.position.getLatitudeE6())
			return 1;
		else if(this.position.getLatitudeE6() == place2.position.getLatitudeE6())
			return 0;
		else 
			return -1;
		
	}
	
	/**
	 * Sets a new place avatar 
	 * @param avatar_p
	 */
	public void setAvatar(WMarker avatar_p)
	{
		avatar = avatar_p;
		avatar_showed = true;
		avatar.checkDimensions(MAX_AVATAR_WIDTH, MAX_AVATAR_HEIGHT);
	}
	
	/**
	 * True to show the avatar instead of marker; false otherwise
	 * @param cond
	 */
	public void setAvatarShow(boolean cond)
	{
		avatar_showed = cond;
	}
	
	/**
	 * Returns true if avatar is showed
	 * @return
	 */
	public boolean isAvatarShowed()
	{
		return avatar_showed;
	}
	
	/**
	 * Sets a new place marker
	 * @param marker_p
	 */
	public void setMarker(WMarker marker_p)
	{
		icon = marker_p;
		icon.checkDimensions(MAX_MARKER_WIDTH, MAX_MARKER_HEIGHT);
		mMarker = marker_p.getDrawable();
	}
	
	
	/**
	 * Sets a new place's id
	 * @param id_p
	 */
	public void setId(int id_p)
	{
		id = id_p;
	}
	
	/**
	 * Returns the place's id
	 * @return
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * Sets a new place's name
	 * @param name_p
	 */
	public void setName(String name_p)
	{
		name = name_p;
	}
	
	/**
	 * Returns a the place's name
	 * @return
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Sets a new place's category (WPlaceCategory object)
	 * @param category_p
	 */
	public void setCategory(WPlaceCategory category_p)
	{
		category = category_p;
		isCategorized = true;
	}
	
	/**
	 * Returns True if place has a WPlaceCategory.
	 * @return
	 */
	public boolean isCategorized()
	{
		return isCategorized;
	}
	
	/**
	 * Removes the place's category
	 */
	public void removeCategory()
	{
		category = null;
		isCategorized = false;
	}
	
	/**
	 * Returns the place's category
	 * @return
	 */
	public WPlaceCategory getCategory()
	{
		return category;
	}
	
	/**
	 * If place is visible, returns True; False otherwise
	 * @return
	 */
	public boolean isVisible()
	{
		return visibility;
	}
	
	/**
	 * Sets as visible place if visibility_p is True; False to set invisible.
	 * @param visibility_p
	 */
	public void setVisibility(boolean visibility_p)
	{
		visibility = visibility_p;
	}
	
	/**
	 * Sets a n
	 * @param position_p
	 */
	/*public void setPosition(WPoint position_p)
	{
		position = position_p;
	}*/

	/**
	 * Returns the place's geographical position
	 * @return
	 */
	public WPoint getPosition()
	{
		return position;
	}
	
	/**
	 * Returns the place's address 
	 * @return
	 */
	public String getAddress()
	{
		//address = position.getAddress();
		return address;
	}
	
	/**
	 * Returns the place's avatar; returns Null if the place hasn't avatar.
	 * @return
	 */
	public WMarker getAvatar()
	{
		return avatar;
	}
	
	/**
	 * Set a new place's twitter profile
	 * @param twitter_p
	 */
	public void setTwitter(String twitter_p)
	{
		twitter = twitter_p;
	}
	
	/**
	 * Returns the place's twitter profile; Null if place hasn't twitter profile
	 * @return
	 */
	public String getTwitter()
	{
		return twitter;
	}
	
	/**
	 * Sets a new place's phone number
	 * @param phone_number_p
	 */
	public void setPhoneNumber(String phone_number_p)
	{
		phone_number = phone_number_p;
	}
	
	/**
	 * Returns the place's phone number; Null if place hasn't phone number 
	 * @return
	 */
	public String getPhoneNumber()
	{
		return phone_number;
	}
	
	/**
	 * Returns place's icon (or marker)
	 * @return
	 */
	public WMarker getIcon()
	{
		return icon;
	}
	
	/**
	 * Draws the place properly.
	 * @param canvas
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
				if(isCategorized())
					category.getMarker().draw(point_p.x, point_p.y, canvas, mapView);
				else
					icon.draw(point_p.x, point_p.y, canvas, mapView);
		}
		return true;
	}
   
   /**
    * Returns the place's information
    * @return
    */
   public String getInformation()
   {
	   return information;
   }
   
   /**
    * Sets a new place's information
    * @param info_p
    */
   public void setInformation(String info_p)
   {
	   information = info_p;
   }

}
