package com.iscbd.wingmaps.area;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.iscbd.wingmaps.map.WMap;
import com.iscbd.wingmaps.map.WPoint;
import com.iscbd.wingmaps.map.WView;

/**
 * 
 * @author Juan Antonio Jimenez Lopez < ruchin10@gmail.com >
 *
 */
public class WAreaLayer extends Overlay {

	WMap wmap;
	Context context;
	
	List<WArea> areas = new ArrayList<WArea>();
	
	ArrayList<WAreaListener> listeners = new ArrayList<WAreaListener>();
	private boolean visibility = true;
	private boolean activate = true;
	public boolean isActivate() {
		return activate;
	}
	public void setActivate(boolean activate) {
		this.activate = activate;
	}

	//***********************************************************
	//
	//		E V E N T S
	//
	//***********************************************************
	/**
	 * 
	 * @author Juan Antonio Jimenez Lopez < ruchin10@gmail.com >
	 *
	 */
	public interface WAreaListener
	{
		void onAreaTapped(WArea areaTapped, int area_index);
	}
	
	//*************************************************************
	//
	//		C O N S T R U C T O R S
	//
	//*************************************************************
	/**
	 * Juan Antonio Jimenez Lopez < ruchin10@gmail.com >
	 * @param wmap_p
	 */
	public WAreaLayer(WMap wmap_p)
	{
		wmap = wmap_p;
		wmap_p.getContext();
		wmap.addOverlay(this);
	}
	
	//*************************************************************
	//
	//		M E T H O D S
	//
	//*************************************************************
	/**
	 * Returns the number of areas added in this layer
	 * @return
	 */
	public int getAreasNumber()
	{
		return areas.size();
	}
	
	//********* ADD AREAS
	/**
	 * Add to layer a created area
	 * @param area_p
	 */
	public void addArea(WArea area_p)
	{
		areas.add(area_p);
	}
	
	/**
	 * Creates and adds a new polygon area
	 * @param id_p
	 * @param points_p
	 */
	public void addArea(int id_p, List<WPoint> points_p)
	{
		areas.add(new WArea(id_p, points_p));
	}
	
	/**
	 * Creates and adds a new polygon area
	 * @param id_p
	 * @param lat
	 * @param lon
	 */
	public void addArea(int id_p, List<Float> lat, List<Float> lon)
	{
		areas.add(new WArea(id_p, lat, lon));
	}
	
	/**
	 * Creates and adds a new radial area
	 * @param id_p
	 * @param center_p
	 * @param radius_p
	 */
	public void addArea(int id_p, WPoint center_p, float radius_p)
	{
		areas.add(new WArea(id_p, center_p,radius_p));
	}
	
	/**
	 * Creates and adds a new radial area
	 * @param id_p
	 * @param latitudeE6
	 * @param longitudeE6
	 * @param radius_p
	 */
	public void addArea(int id_p, int latitudeE6, int longitudeE6, float radius_p)
	{
		areas.add(new WArea(id_p, new WPoint(latitudeE6, longitudeE6),radius_p));
	}
	
	/**
	 * Creates and adds a new radial area
	 * @param id_p
	 * @param latitude_p
	 * @param longitude_p
	 * @param radius_p
	 */
	public void addArea(int id_p, double latitude_p, double longitude_p , float radius_p)
	{
		areas.add(new WArea(id_p, new WPoint((int)(latitude_p*1E6), (int)(longitude_p*1E6)),radius_p));
	}
	
	//***** WITH NAME
	/**
	 * Creates and adds a new polygon area
	 * @param id_p
	 * @param name_p
	 * @param points_p
	 */
	public void addArea(int id_p, String name_p, List<WPoint> points_p)
	{
		areas.add(new WArea(id_p, name_p, points_p));
	}
	
	/**
	 * Creates and adds a new polygon area
	 * @param id_p
	 * @param name_p
	 * @param lat
	 * @param lon
	 */
	public void addArea(int id_p, String name_p, List<Float> lat, List<Float> lon)
	{
		areas.add(new WArea(id_p, name_p, lat, lon));
	}
	
	/**
	 * Creates and adds a new radial area
	 * @param id_p
	 * @param name_p
	 * @param center_p
	 * @param radius_p
	 */
	public void addArea(int id_p, String name_p, WPoint center_p, float radius_p)
	{
		areas.add(new WArea(id_p, name_p, center_p,radius_p));
	}
	
	/**
	 * Creates and adds a new radial area
	 * @param id_p
	 * @param name_p
	 * @param latitudeE6
	 * @param longitudeE6
	 * @param radius_p
	 */
	public void addArea(int id_p, String name_p, int latitudeE6, int longitudeE6, float radius_p)
	{
		areas.add(new WArea(id_p, name_p, new WPoint(latitudeE6, longitudeE6),radius_p));
	}
	
	/**
	 * Creates and adds a new radial area
	 * @param id_p
	 * @param name_p
	 * @param latitude_p
	 * @param longitude_p
	 * @param radius_p
	 */
	public void addArea(int id_p, String name_p, double latitude_p, double longitude_p , float radius_p)
	{
		areas.add(new WArea(id_p, name_p, new WPoint((int)(latitude_p*1E6), (int)(longitude_p*1E6)),radius_p));
	}
	
		
	//***** WITH NAME & INFO
	/**
	 * Creates and adds a new polygon area
	 * @param id_p
	 * @param name_p
	 * @param points_p
	 * @param info_p
	 */
	public void addArea(int id_p, String name_p, List<WPoint> points_p, String info_p)
	{
		areas.add(new WArea(id_p, name_p, points_p, info_p));
	}
	
	/**
	 * Creates and adds a new polygon area
	 * @param id_p
	 * @param name_p
	 * @param lat
	 * @param lon
	 * @param info_p
	 */
	public void addArea(int id_p, String name_p, List<Float> lat, List<Float> lon, String info_p)
	{
		areas.add(new WArea(id_p, name_p, lat, lon, info_p));
	}
	
	/**
	 * Creates and adds a new radial area
	 * @param id_p
	 * @param name_p
	 * @param center_p
	 * @param radius_p
	 * @param info_p
	 */
	public void addArea(int id_p, String name_p, WPoint center_p, float radius_p, String info_p)
	{
		areas.add(new WArea(id_p, name_p, center_p, radius_p, info_p));
	}
	
	/**
	 * Creates and adds a new radial area
	 * @param id_p
	 * @param name_p
	 * @param latitudeE6
	 * @param longitudeE6
	 * @param radius_p
	 * @param info_p
	 */
	public void addArea(int id_p, String name_p, int latitudeE6, int longitudeE6, float radius_p, String info_p)
	{
		areas.add(new WArea(id_p, name_p, new WPoint(latitudeE6, longitudeE6),radius_p, info_p));
	}
	
	/**
	 * Creates and adds a new radial area
	 * @param id_p
	 * @param name_p
	 * @param latitude_p
	 * @param longitude_p
	 * @param radius_p
	 * @param info_p
	 */
	public void addArea(int id_p, String name_p, double latitude_p, double longitude_p , float radius_p, String info_p)
	{
		areas.add(new WArea(id_p, name_p, new WPoint((int)(latitude_p*1E6), (int)(longitude_p*1E6)),radius_p, info_p));
	}
	
		
	//*************
	/**
	 * Returns the area specified by his index
	 * @param index_p
	 * @return
	 */
	public WArea getAreaByIndex(int index_p)
	{
		return areas.get(index_p);
	}
	
	/**
	 * REturns the area specified by his ID
	 * @param id_p
	 * @return
	 */
	public WArea getAreaById(int id_p)
	{
		WArea arearet = null;
		for(int i=0; i<this.getAreasNumber(); i++)
		{
			if(areas.get(i).getId() == id_p)
			{
				arearet = areas.get(i);
				break;
			}
		}
		return arearet;
	}
	
	@Override
	public boolean onTap(GeoPoint hitpoint, MapView mapView)
	{
		boolean return_value = super.onTap(hitpoint, mapView);
		if(isVisible())
			if(isActivate())
			{
				int index = this.getAreasNumber()-1; 
				for(index = this.getAreasNumber()-1; index >= 0; index--){
					if(areas.get(index).isInside(WPoint.toWPoint(hitpoint), mapView))
					{
						return_value = true;
						break;
					}
				}
				
				if(index < this.getAreasNumber() && index >= 0)
				{
					for(int i=0; i < listeners.size(); i++)
						listeners.get(i).onAreaTapped(getAreaByIndex(index), index);
				}
				//return super.onTap(hitpoint, mapView);
				//return true;
			}
		return return_value;
	}
	
	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) 
	{
		if(isVisible())
			for(int i=0; i<areas.size(); i++)
			{
				areas.get(i).draw(canvas, mapView, shadow, when);
			}
		return false;
	}
	
	/**
	 * Returns True if this layer is visible
	 * @return
	 */
	public boolean isVisible(){
		return visibility;
	}
	
	/**
	 * Sets a new visibility to this layer
	 * @param cond
	 */
	public void setVisibility(boolean cond)
	{
		visibility = cond;
	}
	
	/**
	 * Sets a new listener to catch tap events created on the areas
	 * @param listener
	 */
	public void setWAreaListener(WAreaListener listener)
	{
		listeners.add(listener);
	}
	
	/**
	 * Deletes from the layer the area specified by himself
	 * @param area_p
	 */
	public void deleteArea(WArea area_p)
	{
		areas.remove(area_p);
	}
	
	/**
	 * Deletes an area using his id
	 * @param id_p
	 */
	public void deleteAreaById(int id_p)
	{
		for(WArea aux : areas){
			if(aux.getId() == id_p){
				areas.remove(aux);
				break;
			}
		}
	}
	/**
	 * Deletes an area using his index
	 * @param index_p
	 */
	public void deleteAreaByIndex(int index_p){
		areas.remove(index_p);
	}
}
