package com.iscbd.wingmaps.info;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.iscbd.wingmaps.actor.WActor;
import com.iscbd.wingmaps.area.WArea;
import com.iscbd.wingmaps.map.WMap;
import com.iscbd.wingmaps.map.WPoint;
import com.iscbd.wingmaps.place.WPlace;

/**
 * @author Juan Antonio Jimenez Lopez < ruchin10@gmail.com >
 *
 */
public class WInfoLayer extends Overlay {

	private boolean activate = true;
	private List<WBubble> bubbles = new ArrayList<WBubble>();
	Context context;
	private List<WRoute> routes = new ArrayList<WRoute>();
	private boolean visibility = true;
	private WMap wmap;
	
	/**
	 * Creates a new info layer
	 * @param wmap_p
	 */
	public WInfoLayer(WMap wmap_p) {
		//super(boundCenterBottom(button_default));
		wmap = wmap_p;
		context = wmap.getContext();
		wmap.addOverlay(this);
	}
	
	/**
	 * Adds a new bubble into the layer
	 * @param actor
	 * @param bubble_type
	 */
	public void addBubble(WActor actor, int bubble_type)
	{
		WBubble bubble_aux = new WBubble(actor, bubble_type);
		
		bubbles.add(bubble_aux);
	}
	
	/**
	 * Adds a new bubble using an anctor
	 * @param actor
	 */
	public void addBubble(WActor actor)
	{
		WBubble bubble_aux = new WBubble(actor, WBubble.TEXT_BUBBLE);
		
		bubbles.add(bubble_aux);
	}

	/**
	 * Adds a new bubble created previously
	 * @param bubble_p
	 */
	public void addBubble(WBubble bubble_p)
	{
		bubbles.add(bubble_p);
	}
	
	/**
	 * Adds a new bubble using a place and specifying the bubble type
	 * @param place
	 * @param bubble_type
	 */
	public void addBubble(WPlace place, int bubble_type)
	{
		WBubble bubble_aux = new WBubble(place, bubble_type);
		bubbles.add(bubble_aux);
	}
	
	/**
	 * Adds a new bubble using a place
	 * @param place
	 */
	public void addBubble(WPlace place)
	{
		WBubble bubble_aux = new WBubble(place, WBubble.TEXT_BUBBLE);
		
		bubbles.add(bubble_aux);
	}
	
	/**
	 * Adds a new bubble using a geographical position (WPoin) and specifying the bubble type
	 * @param position_p
	 * @param text_p
	 */
	public void addBubble(WPoint position_p, String text_p)
	{
		bubbles.add(new WBubble(position_p, text_p));
	}
	
	/**
	 * Adds a new bubble using an area
	 * @param zone
	 */
	public void addBubble(WArea area)
	{
		bubbles.add(new WBubble(area, WBubble.TEXT_ICON_INFO_BUBBLE));
	}
	
	/**
	 * Adds a new bubble using an area and specifying the bubble type
	 * @param zone
	 * @param bubble_type
	 */
	public void addBubble(WArea area, int bubble_type)
	{
		bubbles.add(new WBubble(area, bubble_type));
	}
	
	
	//****************************************************************
	//
	//		R O U T E S    M E T H O D S
	//
	//****************************************************************
	
	/**
	 * Create a new route using two WActor
	 * @param from_p
	 * @param to_p
	 */
	public void addRoute(WActor from_p, WActor to_p)
	{
		routes.add(new WRoute(from_p,to_p));
	}
	
	/**
	 * Create a new route using two WActor and specifying the travel mode
	 * @param from_p
	 * @param to_p
	 * @param travel_mode
	 */
	public void addRoute(WActor from_p, WActor to_p, String travel_mode)
	{
		routes.add(new WRoute(from_p,to_p,travel_mode));
	}
	
	/**
	 * Create a new route using WActor and WPlace
	 * @param from_p
	 * @param to_p
	 */
	public void addRoute(WActor from_p, WPlace to_p)
	{
		routes.add(new WRoute(from_p,to_p));
	}
	
	/**
	 * Create a new route using WActor, WPlace and specifying the travel mode
	 * @param from_p
	 * @param to_p
	 * @param travel_mode
	 */
	public void addRoute(WActor from_p, WPlace to_p, String travel_mode)
	{
		routes.add(new WRoute(from_p,to_p,travel_mode));
	}
	
	/**
	 * Create a new route using WPlace and WActor
	 * @param from_p
	 * @param to_p
	 */
	public void addRoute(WPlace from_p, WActor to_p)
	{
		routes.add(new WRoute(from_p,to_p));
	}
	
	/**
	 * Create a new route using WPlace, WActor and specifying the travel mode
	 * @param from_p
	 * @param to_p
	 * @param travel_mode
	 */
	public void addRoute(WPlace from_p, WActor to_p, String travel_mode)
	{
		routes.add(new WRoute(from_p,to_p,travel_mode));
	}
	
	/**
	 * Create a new route using two WPlace
	 * @param from_p
	 * @param to_p
	 */
	public void addRoute(WPlace from_p, WPlace to_p)
	{
		routes.add(new WRoute(from_p,to_p));
	}
	
	/**
	 * Create a new route using 
	 * @param from_p
	 * @param to_p
	 * @param travel_mode
	 */
	public void addRoute(WPlace from_p, WPlace to_p, String travel_mode)
	{
		routes.add(new WRoute(from_p,to_p,travel_mode));
	}
	
	/**
	 * Create a new route using two WPoint
	 * @param from_p
	 * @param to_p
	 */
	public void addRoute(WPoint from_p, WPoint to_p)
	{
		routes.add(new WRoute(from_p,to_p));
	}
	
	/**
	 * Create a new route using two WPoint and specifying the travel mode
	 * @param from_p
	 * @param to_p
	 * @param travel_mode
	 */
	public void addRoute(WPoint from_p, WPoint to_p, String travel_mode)
	{
		routes.add(new WRoute(from_p,to_p,travel_mode));
	}
	
	/**
	 * delete a specify bubble using the associated WActor
	 * @param actor
	 */
	public void deleteBubble(WActor actor)
	{
		if(bubbles.size() > 0)
		{
			for(int i=0; i < bubbles.size(); i++)
			{
				if(bubbles.get(i).isActorBubble())
				{
					if(bubbles.get(i).getActor() == actor)
						bubbles.remove(i);
				}
			}
		}
	}
	
	/**
	 * Deletes a specify route from the layer
	 * @param route_p
	 */
	public void deleteRoute(WRoute route_p)
	{
		routes.remove(route_p);
	}
	
	
	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) 
	{
		if(isVisible())
		{
			// DRAW ROUTES
			for(int i=0; i<routes.size(); i++)
				routes.get(i).draw(canvas, mapView, shadow, when);
			
			// DRAW BUBBLES
			for(int i=0; i<bubbles.size(); i++)
				bubbles.get(i).draw(canvas, mapView, shadow, when);
			
			return false;
		}
		else
			return true;
	}
	
	/**
	 * Returns True if the layer is activated; Returns False otherwise.
	 * @return
	 */
	public boolean isActivate() {
		return activate;
	}
	
	/**
	 * Returns True if the layer is visible; Returns False otherwise.
	 * @return
	 */
	public boolean isVisible()
	{
		return visibility;
	}

	
	@Override
	public boolean onTap(GeoPoint hitpoint, MapView mapView)
	{
		boolean return_value = super.onTap(hitpoint, mapView);
		if(isVisible())
			if(isActivate())
			{
				if(bubbles != null)
				{
					for(int i=bubbles.size()-1; i>=0; i--)
					{
						if(bubbles.get(i).buttons != null)
						{
							for(int j=0; j < bubbles.get(i).buttons.size(); j++)
							{
								if(bubbles.get(i).buttons.get(j) != null)
								{
									if(bubbles.get(i).buttons.get(j).isHit(WPoint.toWPoint(hitpoint), mapView))
									{
										context.startActivity(bubbles.get(i).buttons.get(j).getIntent());
										return_value = true;
									}
								}
								
							}
						
						}
					}
				}
			}
		return return_value;
	}
	
	//****************************************************************
	//
	//		B U B B L E S    M E T H O D S
	//
	//****************************************************************
	/**
	 * Remove all bubbles in the WInfoLayer
	 */
	public void removeAllBubbles()
	{
		if(bubbles != null)
			bubbles = new ArrayList<WBubble>();
	}

	/**
	 * Remove all routes in the WInfoLayer
	 */
	public void removeAllRoutes()
	{
		routes = new ArrayList<WRoute>();
	}
	
	/**
	 * Deletes a bubbles using the associated WPlace
	 * @param place
	 */
	public void removeBubble(WPlace place)
	{
		if(bubbles.size() > 0)
		{
			for(int i=0; i < bubbles.size(); i++)
			{
				if(bubbles.get(i).isPlaceBubble())
				{
					if(bubbles.get(i).getPlace() == place)
						bubbles.remove(i);
				}
			}
		}
	}
	
	/**
	 * Sets the layer as activate if activate is True; 
	 * @param activate
	 */
	public void setActivate(boolean activate) {
		this.activate = activate;
	}
	
	/**
	 * If cond is True, the layer will be visible; If cond is False, the layer will be invisible
	 * @param cond
	 */
	public void setVisibility(boolean cond)
	{
		visibility = cond;
	}

}
