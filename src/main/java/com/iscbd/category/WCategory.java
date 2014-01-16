package com.iscbd.wingmaps.category;

import com.iscbd.wingmaps.marker.WMarker;

/**
 * 
 * @author Juan Antonio Jimenez Lopez < ruchin10@gmail.com >
 *
 */
public class WCategory {

	private String name;
	private int id;
	private String info;
	private WMarker marker;
	private boolean visibility=true;
	
	/**
	 * Category constructor
	 * @param id_p
	 * @param name_p
	 * @param info_p
	 * @param marker_p
	 */
	protected WCategory(int id_p, String name_p, String info_p, WMarker marker_p)
	{
		id = id_p;
		name = name_p;
		info = info_p;
		marker = marker_p;
	}
	
	/**
	 * REturns category's id
	 * @return
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * Returns category's name
	 * @return
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Returns category's information
	 * @return
	 */
	public String getInformation()
	{
		return info;
	}
	
	/**
	 * Returns category's maker
	 * @return
	 */
	public WMarker getMarker()
	{
		return marker;
	}
	
	/**
	 * Sets a new category's id
	 * @param id_p
	 */
	public void setId(int id_p)
	{
		id = id_p;
	}
	
	/**
	 * Sets a new category's name
	 * @param name_p
	 */
	public void setName(String name_p)
	{
		name = name_p;
	}
	
	/**
	 * sets a new category's information
	 * @param info_p
	 */
	public void setInformation(String info_p)
	{
		info = info_p;
	}
	
	/**
	 * Sets a new category's default marker
	 * @param marker_p
	 */
	public void setMarker(WMarker marker_p)
	{
		marker = marker_p;
	}
	
	/**
	 * Returns True if category is visible
	 * @return
	 */
	public boolean isVisible()
	{
		return visibility;
	}
	
	/**
	 * Sets this category visible if visibility_p is True; Invisible otherwise
	 * @param visibility_p
	 */
	public void setVisibility(boolean visibility_p)
	{
		visibility = visibility_p;
	}
}
