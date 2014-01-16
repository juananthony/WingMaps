package com.iscbd.wingmaps.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;

/**
 * 
 * @author Juan Antonio Jimenez Lopez < ruchin10@gmail.com >
 *
 */
public class WPoint extends GeoPoint {

	private JSONObject json;
	private String address;
	
	/**
	 * 
	 * @param latitudeE6
	 * @param longitudeE6
	 */
	public WPoint(int latitudeE6, int longitudeE6) {
		super(latitudeE6, longitudeE6);
	}
	
	/**
	 * 
	 * @param latitude
	 * @param longitude
	 */
	public WPoint(double latitude, double longitude) {
		super((int)(latitude*1E6), (int)(longitude*1E6));
	}
	
	/**
	 * 
	 * @param arg0
	 * @return
	 */
	static public WPoint toWPoint(GeoPoint arg0)
	{
		return new WPoint(arg0.getLatitudeE6(), arg0.getLongitudeE6());
	}
	
	
	/**
	 * Build the query to get the address of this point
	 * @param lat_p latitude of the point
	 * @param lon_p longitude of the point
	 * @return returns the json query 
	 */
	public String buildQuery(double lat_p, double lon_p)
	{
		String query = new String("http://maps.googleapis.com/maps/api/geocode/json?latlng=");
		query = query + lat_p + "," + lon_p + "&sensor=false&language=es&region=es";
		return query;
	}
	
	/**
	 * Build the query to get the address of this point
	 * @param lat_p latitude of the point * 1E6
	 * @param lon_p longitude of the point * 1E6
	 * @return returns the json query 
	 */
	public String buildQuery(int lat_p, int lon_p)
	{
		String query = new String("http://maps.googleapis.com/maps/api/geocode/json?latlng=");
		query = query + (lat_p/1E6) + "," + (lon_p/1E6) + "&sensor=false&language=es&region=es";
		return query;
	}
	
	/**
	 * Build the json query to get the coordinates of this address
	 * @param address 
	 * @return returns the json query 
	 */
	public String buildQuery(String address)
	{
		String query = new String("http://maps.googleapis.com/maps/api/geocode/json?address=%22");
    	query = query + address + "%22&sensor=false&language=es&region=es";
    	return query;
	}
	
	public String getAddress()
	{
		// create the query to Google Directions
		String query = buildQuery(this.getLatitudeE6()/1E6,this.getLongitudeE6()/1E6);
    	// receive the json from google directions
    	String json_string = requestJSON(query);
		try {
			readJSON(json_string);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return address;
	}
	
	public String requestJSON(String query){
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
	
	
	public void readJSON(String json_p) throws JSONException{
		json = new JSONObject(json_p);
		address = json.getJSONArray("results").getJSONObject(0).getString("formatted_address");
	}
	
}
