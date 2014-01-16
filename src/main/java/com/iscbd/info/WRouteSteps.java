package com.iscbd.wingmaps.info;

import java.util.ArrayList;
import java.util.List;

import com.iscbd.wingmaps.map.WPoint;

/**
 * 
 * @author Juan Antonio Jimenez Lopez < ruchin10@gmail.com >
 *
 */
public class WRouteSteps {
	private String distance;
	private String duration;
	private WPoint start;
	private WPoint end;
	private String instruction;
	private String travel_mode;
	List<WPoint> polyline;
	
	
	//***********************************************************
	//
	//		C O N S T R U C T O R S
	//
	//***********************************************************
	
	/***********************************************************
	 * Route step constructor
	 * @param distance_p
	 * @param duration_p
	 * @param start_p
	 * @param end_p
	 * @param instruction_p
	 * @param travel_mode_p
	 * @param polyline_p
	 */
	public WRouteSteps(String distance_p, String duration_p, WPoint start_p, WPoint end_p, String instruction_p, String travel_mode_p, String polyline_p)
	{
		distance = distance_p;
		duration = duration_p;
		start = start_p;
		end = end_p;
		instruction = instruction_p;
		travel_mode = travel_mode_p;
		
		/*** BUILD POLYLINE ***/
		polyline = decodePoly(polyline_p);
	}
	
	/**
	 * Returns the polyine which define the step 
	 * @return
	 */
	public List<WPoint> getPolyline(){
		return polyline;
	}
	
	/**
	 * Returns the size of polyline
	 * @return
	 */
	public int getPolylineSize(){
		return polyline.size();
	}
	
	/**
	 * Returns the step's distance
	 * @return
	 */
	public String getDistance(){
		return distance;
	}
	
	/**
	 * Returns the step's duration
	 * @return
	 */
	public String getDuration(){
		return duration;
	}
	
	/**
	 * Returns the starting position of the step
	 * @return
	 */
	public WPoint getFrom(){
		return start;
	}
	
	/**
	 * Returns the ending position of the step
	 * @return
	 */
	public WPoint getTo(){
		return end;
	}
	
	/**
	 * Returns the step instructions
	 * @return
	 */
	public String getInstruction(){
		return instruction;
	}
	
	/**
	 * Returns the travel mode of the step
	 * @return
	 */
	public String getTravelMode(){
		return travel_mode;
	}
	
	/**
	 * Decode a JSON's polyline into WPoint List
	 * @param encoded
	 * @return
	 */
	private List<WPoint> decodePoly(String encoded) {

	    List<WPoint> poly = new ArrayList<WPoint>();
	    int index = 0, len = encoded.length();
	    int lat = 0, lng = 0;

	    while (index < len) {
	        int b, shift = 0, result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lat += dlat;

	        shift = 0;
	        result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lng += dlng;

	        WPoint p = new WPoint((int) (((double) lat / 1E5) * 1E6),
	             (int) (((double) lng / 1E5) * 1E6));
	        poly.add(p);
	    }

	    return poly;
	}
}
