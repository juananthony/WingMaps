package com.iscbd.wingmaps.info;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;

import com.google.android.maps.MapView;
import com.google.android.maps.Projection;
import com.iscbd.wingmaps.actor.WActor;
import com.iscbd.wingmaps.map.WPoint;
import com.iscbd.wingmaps.place.WPlace;

/**
 * 
 * @author Juan Antonio Jimenez Lopez < ruchin10@gmail.com >
 *
 */
public class WRoute {
	private WPoint from;
	private WPoint to;
	private List<WRouteSteps> steps = new ArrayList<WRouteSteps>();
	private JSONObject json;
	private String distance;
	private String duration;
	private Integer num_steps;
	private String travel_mode;
	private Paint p = new Paint();
	private Paint p2 = new Paint();
	private Paint p3 = new Paint();
	private Paint p4 = new Paint();
	private Double route_width;
	
	private static final int DEFAULT_ALFA = 255;
	private static final int DEFAULT_RED = 0;
	private static final int DEFAULT_GREEN = 0;
	private static final int DEFAULT_BLUE = 255;
	
	//*******************************************************
	//
	//		C O N S T R U C T O R S
	//
	//*******************************************************
	/**
	 * WRoute constructor using two WPoint
	 * @param from_p
	 * @param to_p
	 */
	public WRoute(WPoint from_p, WPoint to_p)
	{
		from = from_p;
		to = to_p;
		
		this.setRouteColor(DEFAULT_ALFA, DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
		
		// create the query to Google Directions
		String query = buildQuery(from,to,"driving");
    	// receive the json from google directions
    	String json_string = requestJSON(query);
		// read json
		try {
			readJSON(json_string);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * WRoute constructor using two WActor
	 * @param from_p
	 * @param to_p
	 */
	public WRoute(WActor from_p, WActor to_p)
	{
		from = from_p.getPosition();
		to = to_p.getPosition();
		
		this.setRouteColor(DEFAULT_ALFA, DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
		
		// create the query to Google Directions
		String query = buildQuery(from,to,"driving");
    	// receive the json from google directions
    	String json_string = requestJSON(query);
		// read json
		try {
			readJSON(json_string);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * WRoute constructor using WActor and WPlace
	 * @param from_p
	 * @param to_p
	 */
	public WRoute(WActor from_p, WPlace to_p)
	{
		from = from_p.getPosition();
		to = to_p.getPosition();
		
		this.setRouteColor(DEFAULT_ALFA, DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
		
		// create the query to Google Directions
		String query = buildQuery(from,to,"driving");
    	// receive the json from google directions
    	String json_string = requestJSON(query);
		// read json
		try {
			readJSON(json_string);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * WRoute constructor using a WPlace and WActor
	 * @param from_p
	 * @param to_p
	 */
	public WRoute(WPlace from_p, WActor to_p)
	{
		from = from_p.getPosition();
		to = to_p.getPosition();
		
		this.setRouteColor(DEFAULT_ALFA, DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
		
		// create the query to Google Directions
		String query = buildQuery(from,to,"driving");
    	// receive the json from google directions
    	String json_string = requestJSON(query);
		// read json
		try {
			readJSON(json_string);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * WRoute constructor using two WPlace
	 * @param from_p
	 * @param to_p
	 */
	public WRoute(WPlace from_p, WPlace to_p)
	{
		from = from_p.getPosition();
		to = to_p.getPosition();
		
		this.setRouteColor(DEFAULT_ALFA, DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
		
		// create the query to Google Directions
		String query = buildQuery(from,to,"driving");
    	// receive the json from google directions
    	String json_string = requestJSON(query);
		// read json
		try {
			readJSON(json_string);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	//************
	//************ WITH TRAVEL MODE
	
	
	/**
	 * WRoute constructor using two WPoint and specified travel mode
	 * @param from_p
	 * @param to_p
	 */
	public WRoute(WPoint from_p, WPoint to_p, String travel_mode_p)
	{
		from = from_p;
		to = to_p;
		
		this.setRouteColor(DEFAULT_ALFA, DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
		
		if(travel_mode_p == "driving" || travel_mode_p == "walking" || travel_mode_p == "bicycling")
		{
			travel_mode = travel_mode_p;
		}
		else
		{
			travel_mode = "driving";
		}
		
		// create the query to Google Directions
		String query = buildQuery(from,to,travel_mode_p);
    	// receive the json from google directions
    	String json_string = requestJSON(query);
		// read json
		try {
			readJSON(json_string);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * WRoute constructor using two WActor and specified travel mode
	 * @param from_p
	 * @param to_p
	 */
	public WRoute(WActor from_p, WActor to_p, String travel_mode_p)
	{
		from = from_p.getPosition();
		to = to_p.getPosition();
		
		this.setRouteColor(DEFAULT_ALFA, DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
		
		if(travel_mode_p == "driving" || travel_mode_p == "walking" || travel_mode_p == "bicycling")
		{
			travel_mode = travel_mode_p;
		}
		else
		{
			travel_mode = "driving";
		}
		
		// create the query to Google Directions
		String query = buildQuery(from,to,travel_mode);
    	// receive the json from google directions
    	String json_string = requestJSON(query);
		// read json
		try {
			readJSON(json_string);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * WRoute constructor using WActor and WPlace and specified travel mode
	 * @param from_p
	 * @param to_p
	 */
	public WRoute(WActor from_p, WPlace to_p, String travel_mode_p)
	{
		from = from_p.getPosition();
		to = to_p.getPosition();
		
		this.setRouteColor(DEFAULT_ALFA, DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
		
		if(travel_mode_p == "driving" || travel_mode_p == "walking" || travel_mode_p == "bicycling")
		{
			travel_mode = travel_mode_p;
		}
		else
		{
			travel_mode = "driving";
		}
		
		// create the query to Google Directions
		String query = buildQuery(from,to,travel_mode);
    	// receive the json from google directions
    	String json_string = requestJSON(query);
		// read json
		try {
			readJSON(json_string);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * WRoute constructor using a WPlace, WActor and specified travel mode
	 * @param from_p
	 * @param to_p
	 */
	public WRoute(WPlace from_p, WActor to_p, String travel_mode_p)
	{
		from = from_p.getPosition();
		to = to_p.getPosition();
		
		this.setRouteColor(DEFAULT_ALFA, DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
		
		if(travel_mode_p == "driving" || travel_mode_p == "walking" || travel_mode_p == "bicycling")
		{
			travel_mode = travel_mode_p;
		}
		else
		{
			travel_mode = "driving";
		}
		
		// create the query to Google Directions
		String query = buildQuery(from,to,travel_mode);
    	// receive the json from google directions
    	String json_string = requestJSON(query);
		// read json
		try {
			readJSON(json_string);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * WRoute constructor using two WPlace and specified travel mode
	 * @param from_p
	 * @param to_p
	 */
	public WRoute(WPlace from_p, WPlace to_p, String travel_mode_p)
	{
		from = from_p.getPosition();
		to = to_p.getPosition();
		
		this.setRouteColor(DEFAULT_ALFA, DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
		
		if(travel_mode_p == "driving" || travel_mode_p == "walking" || travel_mode_p == "bicycling")
		{
			travel_mode = travel_mode_p;
		}
		else
		{
			travel_mode = "driving";
		}
		
		// create the query to Google Directions
		String query = buildQuery(from,to,travel_mode);
    	// receive the json from google directions
    	String json_string = requestJSON(query);
		// read json
		try {
			readJSON(json_string);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	//***************************************************************
	//
	//		M E T H O D S
	//
	//***************************************************************
	/**
	 * Set a new route color using ARGB
	 * @param alfa_p
	 * @param red_p
	 * @param green_p
	 * @param blue_p
	 */
	public void setRouteColor(int alfa_p, int red_p, int green_p, int blue_p)
	{
		int alfa = alfa_p;
		int red = red_p;
		int red2 = red;
		int red3 = red+100;
		int red4 = red;
		int green = green_p;
		int green2 = green;
		int green3 = green+100;
		int green4 = green+200;
		int blue = blue_p;
		int blue2 = blue;
		int blue3 = blue+100;
		int blue4 = blue+200;
		
		// red limit control
		if(red < 0) red = 0;
		else if(red > 255)	red = 255;
		if(red < 0) red = 0;
		else if(red2 > 255)	red2 = 255;
		if(red3 < 0) red3 = 0;
		else if(red3 > 255)	red3 = 255;
		if(red4 < 0) red4 = 0;
		else if(red4 > 255)	red4 = 255;
		
		// green limit control
		if(green < 0) green = 0;
		else if(green > 255)	green = 255;
		if(green2 < 0) green2 = 0;
		else if(green2 > 255)	green2 = 255;
		if(green3 < 0) green3 = 0;
		else if(green3 > 255)	green3 = 255;
		if(green4 < 0) green4 = 0;
		else if(green4 > 255)	green4 = 255;
		
		// blue limit control
		if(blue < 0) blue = 0;
		else if(blue > 255)	blue = 255;
		if(blue2 < 0) blue2 = 0;
		else if(blue2 > 255)	blue2 = 255;
		if(blue3 < 0) blue3 = 0;
		else if(blue3 > 255)	blue3 = 255;
		if(blue4 < 0) blue4 = 0;
		else if(blue4 > 255)	blue4 = 255;
		
		// set the brush
		p.setARGB(alfa, red, green, blue);
		p2.setARGB(alfa, red2, green2, blue2);
		p3.setARGB(255, red3, green3, blue3);
		p4.setARGB(255, red4, green4, blue4);
	}
	
	/**
	 * Build a http query to get a json result
	 * @param from_p
	 * @param to_p
	 * @param mode_p
	 * @return
	 */
	private String buildQuery(WPoint from_p, WPoint to_p, String mode_p)
	{
		String query = new String("https://maps.googleapis.com/maps/api/directions/json?origin=");
    	query = query + from.getLatitudeE6()/1E6 + "," + from.getLongitudeE6()/1E6;
    	query = query + "&destination=" + to.getLatitudeE6()/1E6;
    	query = query + "," + to.getLongitudeE6()/1E6;
    	query = query + "&sensor=false&mode="+mode_p;
    	return query;
	}
	
	/**
	 * Send the http query to get a json
	 * @param query
	 * @return
	 */
	private String requestJSON(String query)
	{
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(query);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				System.out.println("Failed to download json-file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
	
	
	/**
	 * Decode a json string into a json object
	 * @param json_p
	 * @throws JSONException
	 */
	private void readJSON(String json_p) throws JSONException{
		json = new JSONObject(json_p);
		
		JSONObject better_route = json.getJSONArray("routes").getJSONObject(0);
		JSONArray steps_aux = better_route.getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
		
		distance = better_route.getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getString("text");
		duration = better_route.getJSONArray("legs").getJSONObject(0).getJSONObject("duration").getString("text");
		Double start_lat = new Double(better_route.getJSONArray("legs").getJSONObject(0).getJSONObject("start_location").getString("lat"));
		Double start_lon = new Double(better_route.getJSONArray("legs").getJSONObject(0).getJSONObject("start_location").getString("lng"));
		Double end_lat = new Double(better_route.getJSONArray("legs").getJSONObject(0).getJSONObject("end_location").getString("lat"));
		Double end_lon = new Double(better_route.getJSONArray("legs").getJSONObject(0).getJSONObject("end_location").getString("lng"));
		from = new WPoint((int)(1E6*start_lat.doubleValue()),(int)(1E6*start_lon.doubleValue()));
		to = new WPoint((int)(1E6*end_lat.doubleValue()),(int)(1E6*end_lon.doubleValue()));
		
		num_steps = steps_aux.length();
		
		// tomamos la informacion de cada paso
		for(int i=0; i<num_steps; i++){
			// localizamos el paso actual dentro del JSON
			JSONObject actual_step = steps_aux.getJSONObject(i);
			
			Double start_lat_aux = new Double(actual_step.getJSONObject("start_location").getString("lat"));
			Double start_lon_aux = new Double(actual_step.getJSONObject("start_location").getString("lng"));
			Double end_lat_aux = new Double(actual_step.getJSONObject("end_location").getString("lat"));
			Double end_lon_aux = new Double(actual_step.getJSONObject("end_location").getString("lng"));
			
			WPoint start_paso = new WPoint((int)(1E6*start_lat_aux.doubleValue()),(int)(1E6*start_lon_aux.doubleValue()));
			WPoint end_paso = new WPoint((int)(1E6*end_lat_aux.doubleValue()),(int)(1E6*end_lon_aux.doubleValue()));
			
			String instructions_aux = new String(actual_step.getString("html_instructions"));
			String travelMode_aux = new String(actual_step.getString("travel_mode")); 
			
			String polyline = new String(actual_step.getJSONObject("polyline").getString("points"));
			
			WRouteSteps aux_step = new WRouteSteps(actual_step.getJSONObject("distance").getString("text"),
					actual_step.getJSONObject("duration").getString("text"),start_paso, end_paso, instructions_aux, travelMode_aux, polyline);
			
			// steps = new ;
			steps.add(aux_step);
		}
		
	}
	
	
	/**
	 * Returns the starting position of the route
	 * @return
	 */
	public WPoint getFrom(){
		return from;
	}
	
	/**
	 * Returns the ending position of the route
	 * @return
	 */
	public WPoint getTo(){
		return to;
	}
	
	/**
	 * Returns the list with all route's steps
	 * @return
	 */
	public List<WRouteSteps> getsteps(){
		return steps;
	}
	
	/**
	 * Returns a step using the index
	 * @param i
	 * @return
	 */
	public WRouteSteps getRouteSteps(int i){
		return steps.get(i);
	}
	
	/**
	 * Returns the route's duration
	 * @return
	 */
	public String getDuration(){
		return duration;
	}
	
	/**
	 * Returns the route's travel mode
	 * @return
	 */
	public String getTravelMode(){
		return travel_mode;
	}
	
	/**
	 * Returns the route's distance
	 * @return
	 */
	public String getDistance(){
		return distance;
	}
	
	/**
	 * Returns the number of route steps
	 * @return
	 */
	public Integer getNumSteps(){
		return num_steps;
	}
	
	/**
	 * Returns the specified step polyline
	 * @param i
	 * @return
	 */
	public List<WPoint> getPolyline(int i)
	{
		return steps.get(i).getPolyline();
	}
	
	
	
	/**
	 * Draws the route properly
	 * @param canvas
	 * @param map_view
	 * @param shadow
	 * @param when
	 */
	public void draw(Canvas canvas, MapView map_view, boolean shadow, long when)
	{
		int anchura, anchura2, anchura3;
		
		route_width = new Double(100*((4*591657550.5/1E6)/2));
		Integer prop = new Integer((int)Math.pow((double)2,(double)map_view.getZoomLevel()-1));
		route_width = route_width / prop;
		
		Projection projection = map_view.getProjection();
		
		if(map_view.getZoomLevel() < 15)
		{
			anchura = 50;
			anchura2 = 40;
			anchura3 = 4;
		}
		else
		{
			anchura = 25;
			anchura2 = 20;
			anchura3 = 2;
		}
		
		// asignamos la anchura dependiendo del zoom del mapa
		//p.setStrokeWidth(anchura/route_width.floatValue());
		p.setStrokeWidth(10);
		p.setStyle(Paint.Style.STROKE);
		
		Path path_route = new Path();
		
		Point aux_point = new Point();
		projection.toPixels(steps.get(0).getFrom(), aux_point);
		path_route.moveTo(aux_point.x, aux_point.y);
		projection.toPixels(steps.get(0).getTo(), aux_point);
		path_route.lineTo(aux_point.x, aux_point.y);
		
		for(int i=1; i<this.getNumSteps(); i++){
			aux_point = new Point();
			
			projection.toPixels(steps.get(i).getFrom(), aux_point);
			path_route.lineTo(aux_point.x, aux_point.y);
			
			for(WPoint aux_w : steps.get(i).polyline)
			{
				projection.toPixels(aux_w, aux_point);
				path_route.lineTo(aux_point.x, aux_point.y);
			}
			projection.toPixels(steps.get(i).getTo(), aux_point);
			path_route.lineTo(aux_point.x, aux_point.y);
		}
		
		
		path_route.setFillType(Path.FillType.WINDING);
		canvas.drawPath(path_route, p);
		
	}
	
	/**
	 * Sets a new route starting position
	 * @param from_p
	 */
	public void setFrom(WPoint from_p)
	{
		from = from_p;
		
		this.setRouteColor(DEFAULT_ALFA, DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
		
		// create the query to Google Directions
		String query = buildQuery(from,to,travel_mode);
    	// receive the json from google directions
    	String json_string = requestJSON(query);
		// read json
		try {
			readJSON(json_string);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets a new route starting position
	 * @param from_p
	 */
	public void setFrom(WActor from_p)
	{
		from = from_p.getPosition();
		
		this.setRouteColor(DEFAULT_ALFA, DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
		
		// create the query to Google Directions
		String query = buildQuery(from,to,travel_mode);
    	// receive the json from google directions
    	String json_string = requestJSON(query);
		// read json
		try {
			readJSON(json_string);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets a new route starting position
	 * @param from_p
	 */
	public void setFrom(WPlace from_p)
	{
		from = from_p.getPosition();
		
		this.setRouteColor(DEFAULT_ALFA, DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
		
		// create the query to Google Directions
		String query = buildQuery(from,to,travel_mode);
    	// receive the json from google directions
    	String json_string = requestJSON(query);
		// read json
		try {
			readJSON(json_string);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets a new route ending position
	 * @param to_p
	 */
	public void setTo(WPoint to_p)
	{
		to = to_p;
		
		this.setRouteColor(DEFAULT_ALFA, DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
		
		// create the query to Google Directions
		String query = buildQuery(from,to,travel_mode);
    	// receive the json from google directions
    	String json_string = requestJSON(query);
		// read json
		try {
			readJSON(json_string);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets a new route ending position
	 * @param to_p
	 */
	public void setTo(WActor to_p)
	{
		to = to_p.getPosition();
		
		this.setRouteColor(DEFAULT_ALFA, DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
		
		// create the query to Google Directions
		String query = buildQuery(from,to,travel_mode);
    	// receive the json from google directions
    	String json_string = requestJSON(query);
		// read json
		try {
			readJSON(json_string);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets a new route ending position
	 * @param to_p
	 */
	public void setTo(WPlace to_p)
	{
		to = to_p.getPosition();
		
		this.setRouteColor(DEFAULT_ALFA, DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
		
		// create the query to Google Directions
		String query = buildQuery(from,to,travel_mode);
    	// receive the json from google directions
    	String json_string = requestJSON(query);
		// read json
		try {
			readJSON(json_string);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets a new route's travel mode
	 * @param travel_mode_p
	 */
	public void setTravelmode(String travel_mode_p){
		
		if(travel_mode_p == "driving" || travel_mode_p == "walking" || travel_mode_p == "bicycling")
		{
			travel_mode = travel_mode_p;
		}
		else
		{
			travel_mode = "driving";
		}
		
		// create the query to Google Directions
				String query = buildQuery(from,to,travel_mode);
		    	// receive the json from google directions
		    	String json_string = requestJSON(query);
				// read json
				try {
					readJSON(json_string);
				} catch (JSONException e) {
					e.printStackTrace();
				}
	}
}
