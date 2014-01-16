package com.iscbd.wingmaps.place;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
import com.iscbd.wingmaps.actor.WActorLayer.WActorListener;
import com.iscbd.wingmaps.category.WPlaceCategory;
import com.iscbd.wingmaps.map.WMap;
import com.iscbd.wingmaps.map.WPoint;
import com.iscbd.wingmaps.marker.WMarker;

/**
 * 
 * @author Juan Antonio Jimenez Lopez < ruchin10@gmail.com >
 *
 */
public class WPlaceLayer extends Overlay{
	
	private WMarker defaultMarker;
	private ArrayList<WPlace> places = new ArrayList<WPlace>();
	private ArrayList<WPlace> places_ordered = (ArrayList<WPlace>) places.clone();
	private boolean visibility = true;
	private Context context;
	private WMap wmap;
	private boolean integrate = false;
	

	private boolean activate = true;
	public boolean isActivate() {
		return activate;
	}

	public void setActivate(boolean activate) {
		this.activate = activate;
	}
	
	ArrayList<WPlaceListener> listeners = new ArrayList<WPlaceListener>();
	
	
	
	
	//***********************************************************
	//
	//		E V E N T S
	//
	//***********************************************************
	public interface WPlaceListener
	{
		void onPlaceTapped(WPlace placeTapped, int place_index);
	}

	//***********************************************************Ä
	//
	//		C O N S T R U C T O R S
	//
	//***********************************************************
	public WPlaceLayer(WMap wmap_p, WMarker defaultMarker_p) {
		defaultMarker = defaultMarker_p;
		context = wmap_p.getContext();
		wmap = wmap_p;
		wmap.addOverlay(this);
	}
	
	public WPlaceLayer(WMap wmap_p, Drawable defaultMarker_p) {
		defaultMarker = new WMarker(defaultMarker_p);
		context = wmap_p.getContext();
		wmap = wmap_p;
		wmap.addOverlay(this);
	}
	
	public WPlaceLayer(WMap wmap_p, Bitmap defaultMarker_p) {
		defaultMarker = new WMarker(defaultMarker_p);
		context = wmap_p.getContext();;
		wmap = wmap_p;
		wmap.addOverlay(this);
	}
	
	//***********************************************************
	//
	//		M E T H O D S
	//
	//***********************************************************
	public void addPlace(WPlace place_p)
	{
		places.add(place_p);
		places_ordered = getPlacesOrderedByLatitude();
	}
	
	public void addPlace(int id_p, WPoint position_p, String name_p, String info_p)
	{
		WPlace new_place = new WPlace(id_p,position_p,name_p,info_p,context,defaultMarker);
		addPlace(new_place);
		places_ordered = getPlacesOrderedByLatitude();
	}
	
	public void addPlace(int id_p, double lat_p, double lon_p, String name_p, String info_p)
	{
		WPlace new_place = new WPlace(id_p,lat_p,lon_p,name_p,info_p,context,defaultMarker);
		addPlace(new_place);
		places_ordered = getPlacesOrderedByLatitude();
	}
	
	public void addPlace(int id_p, int latE6, int lonE6, String name_p, String info_p)
	{
		WPlace new_place = new WPlace(id_p,latE6,lonE6,name_p,info_p,context,defaultMarker);
		addPlace(new_place);
		places_ordered = getPlacesOrderedByLatitude();
	}

	//******
	
	public void addPlace(int id_p, WPoint position_p, String name_p, String info_p, WMarker marker_p)
	{
		WPlace new_place = new WPlace(id_p,position_p,name_p,info_p,context,marker_p);
		addPlace(new_place);
		places_ordered = getPlacesOrderedByLatitude();
	}
	
	public void addPlace(int id_p, double lat_p, double lon_p, String name_p, String info_p, WMarker marker_p)
	{
		WPlace new_place = new WPlace(id_p,lat_p,lon_p,name_p,info_p,context,marker_p);
		addPlace(new_place);
		places_ordered = getPlacesOrderedByLatitude();
	}
	
	public void addPlace(int id_p, int latE6, int lonE6, String name_p, String info_p, WMarker marker_p)
	{
		WPlace new_place = new WPlace(id_p,latE6,lonE6,name_p,info_p,context,marker_p);
		addPlace(new_place);
		places_ordered = getPlacesOrderedByLatitude();
	}
	
	//******
	
	public void addPlace(int id_p, WPoint position_p, String name_p,
			String info_p, WPlaceCategory category_p) {
		WPlace new_place = new WPlace(id_p,position_p,name_p,info_p,context,defaultMarker);
		new_place.setCategory(category_p);
		addPlace(new_place);
		places_ordered = getPlacesOrderedByLatitude();
	}
	
	public void addPlace(int id_p, double lat_p, double lon_p, String name_p,
			String info_p, WPlaceCategory category_p) {
		WPlace new_place = new WPlace(id_p,lat_p,lon_p,name_p,info_p,context,defaultMarker);
		new_place.setCategory(category_p);
		addPlace(new_place);
		places_ordered = getPlacesOrderedByLatitude();
	}
	
	public void addPlace(int id_p, int latE6, int lonE6, String name_p,
			String info_p, WPlaceCategory category_p) {
		WPlace new_place = new WPlace(id_p,latE6,lonE6,name_p,info_p,context,defaultMarker);
		new_place.setCategory(category_p);
		addPlace(new_place);
		places_ordered = getPlacesOrderedByLatitude();
	}
	
	//*****
	
	public void setMap(WMap wmap_p)
	{
		wmap = wmap_p;
		context = wmap.getContext();
		integrate = true;
		//location = new WLocation(context,wmap.getMapview(),this);
	}

	public int getPlacesNumber()
	{
		return places.size();
	}
	
	public WPlace getPlaceByIndex(int index_p)
	{
		return places.get(index_p);
	}
	
	public WPlace getPlaceByID(int id_p)
	{
		int i;
		for(i=0; i < this.getPlacesNumber(); i++)
		{
			if(places.get(i).getId() == id_p){
				break;
			}
				
		}
		return places.get(i);
	}
	
	public void deletePlaceByIndex(int index_p)
	{
		this.deletePlace(this.getPlaceByIndex(index_p));
	}
	
	
	public void deletePlaceByID(int id_p)
	{
		this.deletePlace(this.getPlaceByID(id_p));
	}
	
	public void deletePlace(WPlace place_p)
	{
		places.remove(place_p);
		places_ordered = getPlacesOrderedByLatitude();
	}
	
	
	public int getIndexById(int id_p)
	{
		int return_p = -1;
		for(int i=0; i<this.getPlacesNumber(); i++)
		{
			if(places.get(i).getId() == id_p)
			{
				return_p = i;
				break;
			}
		}
		return return_p;
	}
	
	//***********************************************************
	//
	//		M E T H O D S
	//
	//***********************************************************
	private ArrayList<WPlace> getPlacesOrderedByLatitude()
	 {
		 ArrayList<WPlace> aux = (ArrayList<WPlace>) places.clone();
		 Collections.sort(aux);
		 return aux;
	 }
	 
	private WPlace getPlaceTapped(WPoint hitpoint, MapView mapView)
	{
		WPlace place_tapped = null;
		places_ordered = getPlacesOrderedByLatitude();
		Point point_hit = new Point();
		Projection projection = mapView.getProjection();
		projection.toPixels(hitpoint, point_hit);
		for(int i=places_ordered.size()-1; i>=0; i--)
		{
			if(places_ordered.get(i).isHit(point_hit.x,point_hit.y,mapView))
			{
				place_tapped = places_ordered.get(i);
				break;
			}
		}
		return place_tapped;
	}

	
	@Override
	public boolean onTap(GeoPoint hitpoint, MapView mapView)
	{
		if(isVisible())
			if(isActivate())
			{
			WPlace place_tapped = getPlaceTapped(WPoint.toWPoint(hitpoint), mapView);
			if(place_tapped != null)
			{
				for(int i=0; i < listeners.size(); i++)
				{
					listeners.get(i).onPlaceTapped(place_tapped, places.indexOf(place_tapped));
				}
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
	
	public void setWPlaceListener(WPlaceListener listener)
	{
		listeners.add(listener);
	}

	public boolean isVisible()
	{
		return visibility;
	}
	
	public void setVisibility(boolean cond)
	{
		visibility = cond;
	}
	
	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) 
	{
		Paint p = new Paint();
		
		//p.setColor(Color.BLUE);
		p.setARGB(255, 0, 0, 0);
		
		if(isVisible())
				if(shadow==false)
				{
					for(int i=0; i < this.getPlacesNumber(); i++)
					{
						places_ordered.get(i).draw(canvas, mapView, shadow, when);
					}
					//wmap.postInvalidate();
					wmap.postInvalidate();
				}
		
		return false;
	}

	
}
