package com.iscbd.wingmaps.marker;

import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * 
 * @author Juan Antonio Jimenez Lopez < ruchin10@gmail.com >
 *
 */
public class WMarker {
	Bitmap icon;

	private Paint defaultPaint;
	private Paint arrowPaint;
	
	float degrees_offset = 0;
	
	// hit margin error
	private float hit_margin_error = 8;
	
	
	// marker position
	private float marker_center_modifier_x;
	private float marker_center_modifier_y;
	public static final int MARKER_CENTER = 1;
	public static final int MARKER_BOTTOM = 2;
	public static final int MARKER_LEFT = 3;
	public static final int MARKER_RIGHT = 4;
	public static final int MARKER_UP = 5;
	public static final int MARKER_CORNER_BOTTOM_LEFT = 6;
	public static final int MARKER_CORNER_BOTTOM_RIGHT = 7;
	public static final int MARKER_CORNER_TOP_LEFT = 8;
	public static final int MARKER_CORNER_TOP_RIGHT = 9;
	private int marker_position = MARKER_BOTTOM;
	
	
	// *****************************************************
	//
	//		C O N S T R U C T O R S
	//
	// *****************************************************
	public WMarker(Context context_p, int resource)
	{
		defaultPaint = new Paint();
		arrowPaint = new Paint();
		icon = BitmapFactory.decodeResource(context_p.getResources(), resource);
		arrowPaint.setARGB(255, 0, 0, 0);
		defaultPaint.setARGB(255, 0, 0, 0);
		setMarkerPosition(marker_position);
	}
	
	public WMarker(Drawable marker_p){
		defaultPaint = new Paint();
		arrowPaint = new Paint();
		//icon = ((BitmapDrawable) marker_p).getBitmap();
		icon = Bitmap.createBitmap(marker_p.getIntrinsicWidth(), marker_p.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(icon); 
		marker_p.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		marker_p.draw(canvas);
		arrowPaint.setARGB(255, 0, 0, 0);
		defaultPaint.setARGB(255, 0, 0, 0);
		setMarkerPosition(marker_position);
	}
	
	public WMarker(Bitmap marker_p){
		defaultPaint = new Paint();
		arrowPaint = new Paint();
		icon = marker_p;
		arrowPaint.setARGB(255, 0, 0, 0);
		defaultPaint.setARGB(255, 0, 0, 0);
		setMarkerPosition(marker_position);
	}
	
	
	// *****************************************************
	//
	//		M E T H O D S
	//
	// *****************************************************
	/**
	 * Sets the offset in sexagesimal degrees clockwise between the top of the marker and north of the map.
	 * @param clockwise offset in sexagesimal degrees
	 */
	public void setOffset(float offset)
	{
		degrees_offset = offset;
	}
	
	/**
	 * Returns the offset in sexagesimal degrees clockwise between the top of the marker and north of the map.
	 * @return 
	 */
	public float getOffset()
	{
		return degrees_offset;
	}
	
	/**
	 * Returns the marker width
	 * @return
	 */
	public int getWidth()
	{
		return icon.getWidth();
	}
	
	/**
	 * Returns the marker height
	 * @return
	 */
	public int getHeight()
	{
		return icon.getHeight();
	}
	
	/**
	 * Establish the marker's position of the actor
	 * @param flag 
	 */
	public void setMarkerPosition(int flag)
	{
		switch(flag){
			// --------------------------
			case MARKER_CENTER:
				marker_center_modifier_x = - (icon.getWidth()/2);
				marker_center_modifier_y = - (icon.getHeight()/2);
				marker_position = flag;
				break;
				
			// --------------------------
			case MARKER_BOTTOM:
				marker_center_modifier_x = - (icon.getWidth()/2);
				marker_center_modifier_y = - (icon.getHeight());
				marker_position = flag;
				break;
			
			// --------------------------
			case MARKER_LEFT:
				marker_center_modifier_x = 0;
				marker_center_modifier_y = - (icon.getHeight()/2);
				marker_position = flag;
				break;
				
			// --------------------------
			case MARKER_RIGHT:
				marker_center_modifier_x = - (icon.getWidth());
				marker_center_modifier_y = - (icon.getHeight()/2);
				marker_position = flag;
				break;
				
			// --------------------------
			case MARKER_UP:
				marker_center_modifier_x = - (icon.getWidth()/2);
				marker_center_modifier_y = 0;
				marker_position = flag;
				break;
				
			// --------------------------
			case MARKER_CORNER_BOTTOM_LEFT:
				marker_center_modifier_x = 0;
				marker_center_modifier_y = - (icon.getHeight());
				marker_position = flag;
				break;
				
			// --------------------------
			case MARKER_CORNER_BOTTOM_RIGHT:
				marker_center_modifier_x = - (icon.getWidth());
				marker_center_modifier_y = - (icon.getHeight());
				marker_position = flag;
				break;
				
			// --------------------------
			case MARKER_CORNER_TOP_LEFT:
				marker_center_modifier_x = 0;
				marker_center_modifier_y = 0;
				marker_position = flag;
				break;
				
			// --------------------------
			case MARKER_CORNER_TOP_RIGHT:
				marker_center_modifier_x = - (icon.getWidth());
				marker_center_modifier_y = 0;
				marker_position = flag;
				break;
		}
	}
	
	public int getMarkerPosition()
	{
		return marker_position;
	}
	
	public float getXModifier()
	{
		return marker_center_modifier_x;
	}
	
	public float getYModifier()
	{
		return marker_center_modifier_y;
	}
	
	/**
	 * Change the Arrow's ARGB parameters
	 * @param alfa_p Alfa field of the arrow
	 * @param red_p Red field of the arrow
	 * @param green_p Green field of the arrow
	 * @param blue_p Blue field of the arrow
	 */
	public void setArrowARGB(int alfa_p, int red_p, int green_p, int blue_p)
	{
		arrowPaint.setARGB(alfa_p, red_p, green_p, blue_p);
	}
	
	/**
	 * 
	 * @param icon_p
	 */
	public void setIcon(Bitmap icon_p)
	{
		icon = icon_p;
	}
	
	/**
	 * 
	 * @param icon_p
	 */
	public void setIcon(Drawable icon_p)
	{
		icon = ((BitmapDrawable) icon_p).getBitmap();
	}
	
	public void checkDimensions(int max_width, int max_height)
	{
		Bitmap final_icon;
		
		// resize image
		double icon_prop = ((float) icon.getWidth()) / ((float)icon.getHeight());
		if(icon.getHeight() >= icon.getWidth())
		{
			if(icon.getHeight() >= max_height)
			{
				double aux_width = max_height * icon_prop;
				final_icon = Bitmap.createScaledBitmap(icon, (int) aux_width, max_height, false);
			}
			else
				final_icon = icon;
		}
		else
		{
			if(icon.getWidth() >= max_width)
			{
				double aux_height = (max_width / icon_prop);
				final_icon = Bitmap.createScaledBitmap(icon, max_width, (int) aux_height, false);
			}
			else
				final_icon = icon;
		}
			
				
		icon = final_icon;
	}
	
	/**
	 * 
	 * @return
	 */
	public Bitmap getBitmap()
	{
		return icon;
	}
	
	/**
	 * 
	 * @return
	 */
	public Drawable getDrawable()
	{
		Drawable d =new BitmapDrawable(icon);
		return d;
	}
	
	public boolean isHit(float hit_x, float hit_y, float x, float y)
	{
		boolean return_value = false;
		
		float top = y - hit_margin_error;
		float left = x - hit_margin_error;
		float right = x + hit_margin_error;
		float bottom = y + hit_margin_error;
		
		if(marker_position == this.MARKER_CENTER)
		{
			top += marker_center_modifier_y;
			left += marker_center_modifier_x;
			right -= marker_center_modifier_x;
			bottom -= marker_center_modifier_y;
		}
		
		if(marker_position == this.MARKER_BOTTOM)
		{
			top += marker_center_modifier_y;
			left += marker_center_modifier_x;
			right -= marker_center_modifier_x;
		}
		
		if(marker_position == this.MARKER_LEFT)
		{
			top += marker_center_modifier_y;
			right -= marker_center_modifier_x;
			bottom -= marker_center_modifier_y;
		}
		
		if(marker_position == this.MARKER_RIGHT)
		{
			top += marker_center_modifier_y;
			left += marker_center_modifier_x;
			bottom -= marker_center_modifier_y;
		}
		
		if(marker_position == this.MARKER_UP)
		{
			left += marker_center_modifier_x;
			right -= marker_center_modifier_x;
			bottom -= marker_center_modifier_y;
		}
		
		if(marker_position == this.MARKER_CORNER_BOTTOM_LEFT)
		{
			top += marker_center_modifier_y;
			right -= marker_center_modifier_x;
		}
		
		if(marker_position == this.MARKER_CORNER_BOTTOM_RIGHT)
		{
			top += marker_center_modifier_y;
			left += marker_center_modifier_x;
		}
		
		if(marker_position == this.MARKER_CORNER_TOP_LEFT)
		{
			right -= marker_center_modifier_x;
			bottom -= marker_center_modifier_y;
		}
		
		if(marker_position == this.MARKER_CORNER_TOP_RIGHT)
		{
			left += marker_center_modifier_x;
			bottom -= marker_center_modifier_y;
		}
		
		if(hit_x > left && hit_x  < right && hit_y > top && hit_y < bottom)
		{
			return_value = true;
		}
		
		return return_value;
	}
	
	// *****************************************************
	// *****************************************************
	//
	//		D R A W
	//
	// *****************************************************
	// *****************************************************
	/**
	 * Draw the WMarker on the MapView
	 * @param x Actor's X-position on the MapView
	 * @param y Actor's Y-position on the MapView
	 * @param canvas
	 * @param mapView
	 */
	public void draw(float x, float y, Canvas canvas, MapView mapView)
	{
		canvas.drawBitmap(icon, x + marker_center_modifier_x, y + marker_center_modifier_y, defaultPaint);
		//canvas.drawBitmap(icon, x, y, defaultPaint);
	}
	
	/**
	 * Draw the WMarker on the MapView with compass
	 * @param x Actor's X-position on the MapView
	 * @param y Actor's Y-position on the MapView
	 * @param canvas
	 * @param mapView
	 * @param compass 
	 */
	public void draw(float x, float y, Canvas canvas, MapView mapView, float compass)
	{
		compass += degrees_offset;
		
		if(marker_position == WMarker.MARKER_CENTER)
		{
			Matrix matrix = new Matrix();
			matrix.postRotate(compass, icon.getWidth()/2, icon.getHeight()/2);
			matrix.postTranslate(x + marker_center_modifier_x, y + marker_center_modifier_y);
			canvas.drawBitmap(icon, matrix, defaultPaint);
		}
		else
		{
			canvas.drawBitmap(icon, x + marker_center_modifier_x, y + marker_center_modifier_y, defaultPaint);
			
			// ----- ARROW -----
			float radiousBitmap = (float) (20+Math.sqrt(Math.pow(icon.getWidth(),2) + Math.pow(icon.getHeight(),2))/2);
			float radiousArrow = 30;
	
			float center_x = x + marker_center_modifier_x + icon.getWidth()/2;
			float center_y = y + marker_center_modifier_y + icon.getHeight()/2;
			
			float offX = (float) (radiousBitmap*Math.sin((compass)*2*Math.PI/360));
			float offY = (float) (radiousBitmap*Math.cos((compass)*2*Math.PI/360));
			
			float offX2 = (float) (radiousArrow*Math.sin((compass-180+17)*2*Math.PI/360));
			float offY2 = (float) (radiousArrow*Math.cos((compass-180+17)*2*Math.PI/360));
			float offX3 = (float) (radiousArrow*Math.sin((compass-180-17)*2*Math.PI/360));
			float offY3 = (float) (radiousArrow*Math.cos((compass-180-17)*2*Math.PI/360));
			float offX4 = (float) ((radiousArrow-9)*Math.sin((compass-180)*2*Math.PI/360));
			float offY4 = (float) ((radiousArrow-9)*Math.cos((compass-180)*2*Math.PI/360));
	
			float tip_x = center_x + offX;
			float tip_y = center_y - offY;
	
			float x2 = tip_x + offX2;
			float y2 = tip_y - offY2;
			float x3 = tip_x + offX3;
			float y3 = tip_y - offY3;
			float x4 = tip_x + offX4;
			float y4 = tip_y - offY4;
	
			Path path = new Path();
			Path path2 = new Path();
	
			path.moveTo(tip_x, tip_y);
			path.lineTo(x2, y2);
			path.lineTo(x4, y4);
			path.lineTo(x3, y3);
			path.lineTo(tip_x, tip_y);
	
			path.close();
	
			canvas.drawPath(path, arrowPaint);
		}
	}
}
