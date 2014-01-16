package com.iscbd.wingmaps.area;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.location.Location;

import com.google.android.maps.MapView;
import com.google.android.maps.Projection;
import com.iscbd.wingmaps.map.WPoint;
import com.iscbd.wingmaps.map.WView;

/**
 * 
 * @author Juan Antonio Jimenez Lopez < ruchin10@gmail.com >
 *
 */
public class WArea {
	private int id;
	private String name;
	private String info;
	private int area_type;
	private List<WPoint> points = new ArrayList<WPoint>();
	private Paint areapaint;
	private Paint edgepaint;
	
	private WPoint center;
	private float radius;
	
	private static final int DEFAULT_ALFA = 40;
	private static final int DEFAULT_RED = 255;
	private static final int DEFAULT_GREEN = 0;
	private static final int DEFAULT_BLUE = 0;
	
	public static final int CIRCULAR_AREA = 0;
	public static final int POLYGON_AREA = 1;
	
	//*************************************************************
	//
	//		C O N S T R U C T O R S
	//
	//*************************************************************
	/**
	 * Constructor of a new radial area
	 * @param id_p
	 * @param center_p
	 * @param radius_p
	 */
	public WArea(int id_p, WPoint center_p, float radius_p)
	{
		id=id_p;
		center = center_p;
		radius = radius_p;		//in meters
		area_type = CIRCULAR_AREA;
		areapaint = new Paint();
		edgepaint = new Paint();
		this.setColor(DEFAULT_ALFA, DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
		edgepaint.setStrokeWidth(3);
		edgepaint.setStyle(Paint.Style.STROKE);
		name = "Area " + id;
	}
	
	/**
	 * Constructor of a new polygonal area
	 * @param id_p
	 * @param points_p
	 */
	public WArea(int id_p, List<WPoint> points_p)
	{
		id=id_p;
		area_type = POLYGON_AREA;
		points = points_p;
		center = calculateCenter();
		areapaint = new Paint();
		edgepaint = new Paint();
		this.setColor(DEFAULT_ALFA, DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
		edgepaint.setStrokeWidth(3);
		edgepaint.setStyle(Paint.Style.STROKE);
		name = "Area " + id;
	}

	/**
	 * Constructor of a new polygon area using latitudes and longitudes
	 * @param id_p
	 * @param lat
	 * @param lon
	 */
	public WArea(int id_p, List<Float> lat, List<Float> lon)
	{
		id = id_p;
		area_type = POLYGON_AREA;
		int minsize;
		if(lat.size() < lon.size())
			minsize = lat.size();
		else
			minsize = lon.size();
		for(int i=0; i < minsize; i++)
		{
			points.add(new WPoint((int)(lat.get(i)*1E6),(int)(lon.get(i)*1E6)));
		}
		center = calculateCenter();
		areapaint = new Paint();
		edgepaint = new Paint();
		edgepaint.setStrokeWidth(3);
		edgepaint.setStyle(Paint.Style.STROKE);
		this.setColor(100, 255, 0, 0);
		name = "Area " + id;
	}
	
	/**
	 * Constructor of a new radial area
	 * @param id_p
	 * @param name_p
	 * @param center_p
	 * @param radius_p
	 */
	public WArea(int id_p, String name_p, WPoint center_p, float radius_p)
	{
		id=id_p;
		center = center_p;
		radius = radius_p;		//in meters
		area_type = CIRCULAR_AREA;
		areapaint = new Paint();
		edgepaint = new Paint();
		this.setColor(DEFAULT_ALFA, DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
		edgepaint.setStrokeWidth(3);
		edgepaint.setStyle(Paint.Style.STROKE);
		name = name_p;
	}
	
	/**
	 * Constructor of a new polygon area
	 * @param id_p
	 * @param name_p
	 * @param points_p
	 */
	public WArea(int id_p, String name_p, List<WPoint> points_p)
	{
		id=id_p;
		area_type = POLYGON_AREA;
		points = points_p;
		center = calculateCenter();
		areapaint = new Paint();
		edgepaint = new Paint();
		this.setColor(DEFAULT_ALFA, DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
		edgepaint.setStrokeWidth(3);
		edgepaint.setStyle(Paint.Style.STROKE);
		name = name_p;
	}

	/**
	 * Constructor of a new polygon area
	 * @param id_p
	 * @param name_p
	 * @param lat
	 * @param lon
	 */
	public WArea(int id_p, String name_p, List<Float> lat, List<Float> lon)
	{
		id = id_p;
		area_type = POLYGON_AREA;
		int minsize;
		if(lat.size() < lon.size())
			minsize = lat.size();
		else
			minsize = lon.size();
		for(int i=0; i < minsize; i++)
		{
			points.add(new WPoint((int)(lat.get(i)*1E6),(int)(lon.get(i)*1E6)));
		}
		center = calculateCenter();
		areapaint = new Paint();
		edgepaint = new Paint();
		edgepaint.setStrokeWidth(3);
		edgepaint.setStyle(Paint.Style.STROKE);
		this.setColor(100, 255, 0, 0);
		name = name_p;
	}
	
	/**
	 * Constructor of a new radial area
	 * @param id_p
	 * @param name_p
	 * @param center_p
	 * @param radius_p
	 * @param info_p
	 */
	public WArea(int id_p, String name_p, WPoint center_p, float radius_p, String info_p)
	{
		id=id_p;
		center = center_p;
		radius = radius_p;		//in meters
		area_type = CIRCULAR_AREA;
		areapaint = new Paint();
		edgepaint = new Paint();
		this.setColor(DEFAULT_ALFA, DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
		edgepaint.setStrokeWidth(3);
		edgepaint.setStyle(Paint.Style.STROKE);
		name = name_p;
		info = info_p;
	}
	
	/**
	 * Constructor of a new polygon area
	 * @param id_p
	 * @param name_p
	 * @param points_p
	 * @param info_p
	 */
	public WArea(int id_p, String name_p, List<WPoint> points_p, String info_p)
	{
		id=id_p;
		area_type = POLYGON_AREA;
		points = points_p;
		center = calculateCenter();
		areapaint = new Paint();
		edgepaint = new Paint();
		this.setColor(DEFAULT_ALFA, DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
		edgepaint.setStrokeWidth(3);
		edgepaint.setStyle(Paint.Style.STROKE);
		name = name_p;
		info = info_p;
	}

	/**
	 * Constructor of a new polygon area
	 * @param id_p
	 * @param name_p
	 * @param lat
	 * @param lon
	 * @param info_p
	 */
	public WArea(int id_p, String name_p, List<Float> lat, List<Float> lon, String info_p)
	{
		id = id_p;
		area_type = POLYGON_AREA;
		int minsize;
		if(lat.size() < lon.size())
			minsize = lat.size();
		else
			minsize = lon.size();
		for(int i=0; i < minsize; i++)
		{
			points.add(new WPoint((int)(lat.get(i)*1E6),(int)(lon.get(i)*1E6)));
		}
		center = calculateCenter();
		areapaint = new Paint();
		edgepaint = new Paint();
		edgepaint.setStrokeWidth(3);
		edgepaint.setStyle(Paint.Style.STROKE);
		this.setColor(100, 255, 0, 0);
		name = name_p;
		info = info_p;
	}
	
	//*************************************************************
	//
	//		M E T H O D S
	//
	//*************************************************************
	/**
	 * Returns the area's name
	 * @return
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Sets a new area name
	 * @param name_p
	 */
	public void setName(String name_p)
	{
		name = name_p;
	}
	
	/**
	 * Returns the area's center
	 * @return
	 */
	public WPoint getCenter()
	{
		return center;
	}
	
	/**
	 * Returns teh area's id
	 * @return
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * Returns the area's map type
	 * @return
	 */
	public int getAreaType()
	{
		return area_type;
	}
	
	/**
	 * REturns the radial area radius if the WArea is radial area; Null otherwise
	 * @return
	 */
	public float getRadius()
	{
		return radius;
	}
	
	/**
	 * Sets a new area's radius
	 * @param radius_p
	 */
	public void setRadius(float radius_p)
	{
		if(area_type == POLYGON_AREA){
			center = this.calculateCenter();
			points = null;
		}
		area_type = CIRCULAR_AREA;
		radius = radius_p;
	}
	
	/**
	 * Returns the wpoints list that defines the polygon area
	 * @return
	 */
	public List<WPoint> getPoints()
	{
		return points;
	}
	
	/**
	 * Returns the area surface in square meters
	 * @return
	 */
	public double getSurface()
	{
		double surface=0;
		
		switch(area_type)
		{
		case POLYGON_AREA:
			surface = SphericalPolygonAreaMeters2();
			if(surface<1000000.0)
				surface = planarPolygonAreaMeters2();
			break;
			
		case CIRCULAR_AREA:
			surface = (float) (Math.PI * Math.pow(radius, 2));
			break;
		}
		return surface;
	}
	
	/**
	 * Method use to get the area surface
	 * @return
	 */
	private float SphericalPolygonAreaMeters2()
	{
		float metersPerDegree = (float) (2.0 * Math.PI * 6367460.0 / 360.0);
		float radiansPerDegree = (float) (Math.PI/180.0);
		float earthRadiusMeters = (float) 6367460.0;
		float totalAngle=0;
		for(int i=0; i<points.size(); i++)
		{
			int j=(i+1)%points.size();
			int k=(i+2)%points.size();
			totalAngle += Angle(points.get(i), points.get(j), points.get(k));
		}
		float planarTotalAngle = (float) ((points.size() - 2)*180.0);
		float sphericalExcess = totalAngle-planarTotalAngle;
		if(sphericalExcess>420.0)
		{
			totalAngle=(float) (points.size() * 360.0 - totalAngle);
			sphericalExcess=totalAngle-planarTotalAngle;
		}
		else if(sphericalExcess>300.0&&sphericalExcess<420.0)
		{
			sphericalExcess = (float) Math.abs(360.0-sphericalExcess);
		}
		return sphericalExcess*radiansPerDegree*earthRadiusMeters*earthRadiusMeters;
	}
	
	/**
	 * Method use to get area surface
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return
	 */
	private float Angle(WPoint p1, WPoint p2, WPoint p3)
	{
		float bearing21 = Bearing(p2,p1);
		float bearing23 = Bearing(p2,p3);
		float angle = bearing21-bearing23;
		if(angle<0.0)
			angle+=360.0;
		return angle;
	}
	
	/**
	 * Method use to get area surface
	 * @param from
	 * @param to
	 * @return
	 */
	private float Bearing(WPoint from, WPoint to)
	{
		float radiansPerDegree = (float) (Math.PI/180.0);
		float degreesPerRadian=(float) (180.0/Math.PI);
		float lat1 = (float) ((from.getLatitudeE6()/1E6)*radiansPerDegree);
		float lon1 = (float) ((from.getLongitudeE6()/1E6)*radiansPerDegree);
		float lat2 = (float) ((to.getLatitudeE6()/1E6)*radiansPerDegree);
		float lon2 = (float) ((to.getLongitudeE6()/1E6)*radiansPerDegree);
		float angle = (float) -Math.atan2(Math.sin(lon1-lon2)*Math.cos(lat2),Math.cos(lat1)*Math.sin(lat2)-Math.sin(lat1)*Math.cos(lat2)*Math.cos(lon1-lon2));
		if(angle<0.0)
			angle+=Math.PI*2.0;
		angle=angle*degreesPerRadian;
		return angle;
	}
	
	/**
	 * Method use to get area surface
	 * @return
	 */
	private double planarPolygonAreaMeters2()
	{
		double metersPerDegree = (double) (2.0 * Math.PI * 6367460.0 / 360.0);
		double radiansPerDegree = (double) (Math.PI/180.0);
		double a = (float) 0.0;
		int n = points.size();
		
		for(int i=0; i < n; i++)
		{
			int j = (i+1)%(points.size());
			double xi = (double) ((points.get(i).getLongitudeE6()/1E6) * metersPerDegree * Math.cos((points.get(i).getLatitudeE6())*radiansPerDegree));
			double yi = (double) ((points.get(i).getLatitudeE6()/1E6) * metersPerDegree );
			double xj = (double) ((points.get(j).getLongitudeE6()/1E6) * metersPerDegree * Math.cos((points.get(j).getLatitudeE6())*radiansPerDegree) );
			double yj = (double) ((points.get(j).getLatitudeE6()/1E6) * metersPerDegree );
			a += xi*yj-xj*yi;
		}
		return (double) (Math.abs(a/2.0)/1E3);
	}
	
	/**
	 * Sets a new backgroud paint and new edge paint using the parameters
	 * @param alfa_p
	 * @param red_p
	 * @param green_p
	 * @param blue_p
	 */
	public void setColor(int alfa_p, int red_p, int green_p, int blue_p)
	{
		int alfa_aux = alfa_p+40;
		int red_aux = red_p+40;
		int green_aux = green_p+40;
		int blue_aux = blue_p+40;
		
		if(alfa_aux > 255) 
			alfa_aux = 255;
		if(alfa_aux < 0) 
			alfa_aux = 0;
		if(red_aux > 255) 
			red_aux = 255;
		if(red_aux < 0) 
			red_aux = 0;
		if(green_aux > 255) 
			green_aux = 255;
		if(green_aux < 0) 
			green_aux = 0;
		if(blue_aux > 255) 
			blue_aux = 255;
		if(blue_aux < 0) 
			blue_aux = 0;
		
		this.setColorArea(alfa_p, red_p, green_p, blue_p);
		this.setColorEdge(alfa_aux, red_aux, green_aux, blue_aux);
	}
	
	/**
	 * Sets the color of the backgroud
	 * @param alfa_p
	 * @param red_p
	 * @param green_p
	 * @param blue_p
	 */
	public void setColorArea(int alfa_p, int red_p, int green_p, int blue_p)
	{
		areapaint.setARGB(alfa_p, red_p, green_p, blue_p);
	}
	
	/**
	 * Sets the color of the area edge
	 * @param alfa_p
	 * @param red_p
	 * @param green_p
	 * @param blue_p
	 */
	public void setColorEdge(int alfa_p, int red_p, int green_p, int blue_p)
	{
		edgepaint.setARGB(alfa_p, red_p, green_p, blue_p);
	}
	
	/**
	 * Draw the area properly
	 * @param canvas
	 * @param mapView
	 * @param shadow
	 * @param when
	 * @return
	 */
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) 
	{
		Path areapath = new Path();
		Projection projection = mapView.getProjection();
		Point point = new Point();
		Point point2 = new Point();
		switch(area_type)
		{
		case POLYGON_AREA:
			projection.toPixels(points.get(0), point);
			areapath.moveTo(point.x, point.y);
			
			for(int i = 1; i<points.size(); i++)
			{
				projection.toPixels(points.get(i), point);
				areapath.lineTo(point.x, point.y);
			}
			areapath.close();
			canvas.drawPath(areapath, areapaint);
			
			// DRAWING ZONE EDGES
			for(int i = 0; i<points.size(); i++)
			{
				if(i != points.size()-1)
				{
					projection.toPixels(points.get(i), point);
					projection.toPixels(points.get(i+1), point2);
					canvas.drawLine(point.x, point.y, point2.x, point2.y, edgepaint);
				}
				else
				{
					projection.toPixels(points.get(i), point);
					projection.toPixels(points.get(0), point2);
					canvas.drawLine(point.x, point.y, point2.x, point2.y, edgepaint);
				}
			}
			break;
			
		case CIRCULAR_AREA:
			projection.toPixels(center, point);
			
			//canvas.drawCircle(point.x, point.y, metersInPixels(mapView,radius), areapaint);
			canvas.drawCircle(point.x, point.y,(int) (mapView.getProjection().metersToEquatorPixels(radius) * (1/ Math.cos(Math.toRadians(center.getLatitudeE6()/1E6)))), areapaint);
			canvas.drawCircle(point.x, point.y,(int) (mapView.getProjection().metersToEquatorPixels(radius) * (1/ Math.cos(Math.toRadians(center.getLatitudeE6()/1E6)))), edgepaint);
			break;
		}
		
		return shadow;
	}
	
	/**
	 * Scale a value (in meters) using a WView
	 * @param mapView WView which calculate the proportions
	 * @param radius_p Radious in meters to get this value in pixels
	 * @return the pixels in scale
	 */
	private float metersInPixels(WView mapView, float meters_p)
	{
		Double aux_radious = new Double(100*((4*591657550.5/1E6)/2));
		
		Integer prop = new Integer((int)Math.pow((double)2,(double)mapView.getZoomLevel()-1));
		
		aux_radious = aux_radious / prop;
		
		return (float)(meters_p/aux_radious.floatValue());
	}
	
	/**
	 * Calculates the polygon area center
	 * @return
	 */
	public WPoint calculateCenter()
	{
		int lat = 0;
		int lon = 0;
		for(int i=0; i<points.size(); i++)
		{
			lat += points.get(i).getLatitudeE6();
			lon += points.get(i).getLongitudeE6();
		}
		lat = lat / points.size();
		lon = lon / points.size();
		
		return new WPoint(lat,lon);
	}
	
	/**
	 * Returns true if the WPoint is inside in the area using a WView; Returns false if the point is not inside
	 * @param hitpoint_p
	 * @param mapView
	 * @return
	 */
	public boolean isInside(WPoint hitpoint_p, WView mapView)
	{
		boolean return_value = false;
		
		Projection projection = mapView.getProjection();
		Point hitpoint = new Point();
		Point point = new Point();
		Point point2 = new Point();
		switch(area_type)
		{
		case CIRCULAR_AREA:
			projection.toPixels(hitpoint_p, hitpoint);
			projection.toPixels(center, point2);
			double distance = Math.sqrt(Math.pow(hitpoint.x-point2.x, 2) + Math.pow(hitpoint.y - point2.y,2));
			if (distance < (projection.metersToEquatorPixels(radius) * (1/ Math.cos(Math.toRadians(center.getLatitudeE6()/1E6)))))
			{
				return_value = true;
			}
			
			break;
			
		case POLYGON_AREA:
			int number_segments = 0;		// number of segments in the same latitude
			int left_segments = 0;			// number of segments in the same latitude with more longitude value than hitpoint
			projection.toPixels(hitpoint_p, hitpoint);
			
			// we get every segment of the polygon
			List<Float> interior_segments = new ArrayList<Float>();

			for(int i=0; i<points.size(); i++)
			{
				if(i != points.size()-1)
				{
					projection.toPixels(points.get(i), point);
					projection.toPixels(points.get(i+1), point2);
				}
				else
				{
					projection.toPixels(points.get(i), point);
					projection.toPixels(points.get(0), point2);
				}
				if((point.y < hitpoint.y && point2.y > hitpoint.y) || (point2.y < hitpoint.y && point.y > hitpoint.y))
				{
					number_segments++;
					float x = (hitpoint.y - point.y)* (point2.x - point.x)/(point2.y-point.y) + point.x;
					interior_segments.add(x);
				}
			}
			
			
			// sort the list with the segments
			Collections.sort(interior_segments, new Comparator(){
				@Override
				public int compare(Object lhs, Object rhs) {
					Float o1 = (Float) lhs;
					Float o2 = (Float) rhs;
					return o1.compareTo(o2);
				}
			});

			
			// we search if the hitpoint is inside or outside
			for(int i=0; i < interior_segments.size()-1; i++)
			{
				if((interior_segments.get(i) < hitpoint.x) && (hitpoint.x < interior_segments.get(i+1)))
				{
					if(i%2 == 0)
					{
						return_value = true;
					}
					else
					{
						return_value = false;
					}
				}
			}
			
			break;
		}
		
		return return_value;
	}
	
	/**
	 * Returns true if the WPoint is inside in the area using a MapView; Returns false if the point is not inside
	 * @param hitpoint_p
	 * @param mapView
	 * @return
	 */
	public boolean isInside(WPoint hitpoint_p, MapView mapView)
	{
		boolean return_value = false;
		
		Projection projection = mapView.getProjection();
		Point hitpoint = new Point();
		Point point = new Point();
		Point point2 = new Point();
		switch(area_type)
		{
		case CIRCULAR_AREA:
			projection.toPixels(hitpoint_p, hitpoint);
			projection.toPixels(center, point2);
			double distance = Math.sqrt(Math.pow(hitpoint.x-point2.x, 2) + Math.pow(hitpoint.y - point2.y,2));
			if (distance < (projection.metersToEquatorPixels(radius) * (1/ Math.cos(Math.toRadians(center.getLatitudeE6()/1E6)))))
			{
				return_value = true;
			}
			
			break;
			
		case POLYGON_AREA:
			int number_segments = 0;		// number of segments in the same latitude
			int left_segments = 0;			// number of segments in the same latitude with more longitude value than hitpoint
			projection.toPixels(hitpoint_p, hitpoint);
			// we get every segment of the polygon
			List<Float> interior_segments = new ArrayList<Float>();

			for(int i=0; i<points.size(); i++)
			{
				if(i != points.size()-1)
				{
					projection.toPixels(points.get(i), point);
					projection.toPixels(points.get(i+1), point2);
				}
				else
				{
					projection.toPixels(points.get(i), point);
					projection.toPixels(points.get(0), point2);
				}
				if((point.y < hitpoint.y && point2.y > hitpoint.y) || (point2.y < hitpoint.y && point.y > hitpoint.y))
				{
					number_segments++;
					float x = (hitpoint.y - point.y)* (point2.x - point.x)/(point2.y-point.y) + point.x;
					interior_segments.add(x);
				}
			}
			
			
			// sort the list with the segments
			Collections.sort(interior_segments, new Comparator(){
				@Override
				public int compare(Object lhs, Object rhs) {
					Float o1 = (Float) lhs;
					Float o2 = (Float) rhs;
					return o1.compareTo(o2);
				}
			});

			
			// we search if the hitpoint is inside or outside
			for(int i=0; i < interior_segments.size()-1; i++)
			{
				if((interior_segments.get(i) < hitpoint.x) && (hitpoint.x < interior_segments.get(i+1)))
				{
					if(i%2 == 0)
					{
						return_value = true;
					}
					else
					{
						return_value = false;
					}
				}
			}
			
			break;
		}
		
		return return_value;
	}
	
	/**
	 * Sets a new area polygon. If the area_type is CIRCULAR_AREA, it is change to POLYGON_AREA
	 * @param points_p
	 */
	public void setPoints(ArrayList<WPoint> points_p){
		points = points_p;
		area_type = POLYGON_AREA;
	}

	/**
	 * Returns the area's information
	 * @return
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * Sets a new area's information
	 * @param info
	 */
	public void setInfo(String info) {
		this.info = info;
	}
	
	/**
	 * Sets a new area's center if area_type is CIRCULAR_AREA
	 * @param center_p
	 */
	public void setCenter(WPoint center_p){
		center = center_p;
	}
	
}