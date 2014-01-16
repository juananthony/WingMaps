package com.iscbd.wingmaps.info;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.google.android.maps.MapView;
import com.google.android.maps.Projection;
import com.iscbd.wingmaps.actor.WActor;
import com.iscbd.wingmaps.area.WArea;
import com.iscbd.wingmaps.map.WPoint;
import com.iscbd.wingmaps.place.WPlace;

/**
 * 
 * @author Juan Antonio Jimenez Lopez < ruchin10@gmail.com >
 *
 */
public class WBubble {
	
	private String text;
	//public ArrayList<WField> fields = new ArrayList<WField>();
	private int bubble_type;
	private int type_box;
	public final static int RECTANGLE_BOX = 0;
	public final static int CURVED_BOX = 1;
	private int curved_radious = 20;
	
	private WFieldGroup field_group;
	
	WPoint position;
	
	private boolean is_showed = false;
	
	private WActor actor;
	private WPlace place;
	private boolean isAreaBubble = false;
	private boolean isPointBubble = false;
	
	// Differents paint
	private Paint backpaint;
	private Paint edgepaint;
	private Paint textpaint;
	private Paint infopaint;
	private Paint keypaint;
	private Paint iconpaint;
	
	private int text_name_size = 25;
	private int text_info_size = 17;
	private int text_size = -1;
	private float text_width = -1;
	private int edge = 2;
	
	private int arrow_width = 20;
	private int arrow_height = 20;
	private int marker_height;
	
	private int margin_left = 15;
	private int margin_right = 15;
	private int margin_top = 15;
	private int margin_bottom = 15;
	
	private int margin_icon_name = 10;	// between icon a name_text and information
	private int margin_name_info = 10;	// between name and information
	private int margin_info_button = 10;	// between information and buttons
	private int margin_buttons = 15;	// between buttons
	private int margin_key_info = 7;	// between information keys and information lines
	
	private float interior_width;
	private float interior_height;
	
	private int num_min_chars_line = 10;
	private float min_text_width = 100;
	
	private Bitmap icon;
	
	// Default colors
	private static final int BACKGROUND_ALFA = 255;
	private static final int BACKGROUND_RED = 240;
	private static final int BACKGROUND_GREEN = 240;
	private static final int BACKGROUND_BLUE = 240;
	
	private static final int EDGE_ALFA = 255;
	private static final int EDGE_RED = 0;
	private static final int EDGE_GREEN = 0;
	private static final int EDGE_BLUE = 0;
	
	private static final int TEXT_ALFA = 255;
	private static final int TEXT_RED = 0;
	private static final int TEXT_GREEN = 0;
	private static final int TEXT_BLUE = 0;
	
	public static final int TEXT_BUBBLE = 0;
	public static final int TEXT_INFO_BUBBLE = 1;
	public static final int TEXT_ICON_BUBBLE = 2;
	public static final int TEXT_ICON_INFO_BUBBLE = 3;
	public static final int TEXT_ICON_INFO_BUTTON_BUBBLE = 4;
	
	private static final int MAX_ICON_WIDTH = 100;
	private static final int MAX_ICON_HEIGHT = 100;
	
	private static final int MAX_BUTTON_WIDTH = 80;
	private static final int MAX_BUTTON_HEIGHT = 80;
		
	public List<WButton> buttons = new ArrayList<WButton>();
	
	//*******************************************************
	//
	//		F I E L D    C L A S S 
	//
	//*******************************************************
	private class WFieldGroup
	{
		public ArrayList<WField> fields;
		float height;
		float width;
		
		WFieldGroup(){
			fields = new ArrayList<WField>();
			float height = 0;
			float width = 0;
		}
		
		public void addField(String key, String info)
		{
			float width_aux = width;
			// inside the width value could be modify
			fields.add(new WField(key, info, createLines(info)));
			if(width_aux != width)
			{
				recalculateFields();
			}
			//calculateSize();
			recalculateFields();
			calculateSize();
		}
		
		public void recalculateFields()
		{
			for(int i=0; i< fields.size(); i++){
				fields.set(i, new WField(fields.get(i).getKey(), fields.get(i).getOriginalInfo(), createLines(fields.get(i).getOriginalInfo())));
			}
			calculateSize();
		}
		
		/**
		 * 
		 */
		public void calculateSize(){
			float aux_height = 0;
			float aux_width = -1;
			
			// get max height
			for(WField aux_field : fields){
				if(aux_field.getWidth() > aux_width)
					aux_width = aux_field.getWidth();
				aux_height += aux_field.getNumberLines()*infopaint.getTextSize() + keypaint.getTextSize() + margin_name_info;
			}
			height = aux_height;
			width = aux_width;
		}
		
		public float getHeight()
		{
			return height;
		}
		
		public float getWidth()
		{
			return width;
		}
		
		public void setWidth(float width_p)
		{
			width = width_p;
			if(!fields.isEmpty())
				this.recalculateFields();
		}
		
		public void setWidth(int width_p)
		{
			width = width_p;
			if(!fields.isEmpty())
				this.recalculateFields();
		}
		
		public int size()
		{
			return fields.size();
		}
		
		public WField get(int index_p)
		{
			return fields.get(index_p);
		}
		
		public ArrayList<String> createLines(String info_original)
		{
			ArrayList<String> lines = new ArrayList<String>();
			String[] words = info_original.split(" ");
			int line_index = 0;
			
			for(String aux_word : words){
				float word_width = infopaint.measureText(aux_word);
				float line_width = 0;
				float space_width = infopaint.measureText(" ");
				if((lines.size()-1) == line_index)
					line_width = infopaint.measureText(lines.get(line_index));
				// word + line < width
				if((word_width + line_width + space_width) < width){
					if((lines.size()-1) == line_index)
						lines.set(line_index, lines.get(line_index) + " " + aux_word);
					else{
						//line_index++;
						lines.add(aux_word);
					}
					//lines.a(line_index) += " " + aux_word;
				}
				else if((word_width + line_width + space_width) == width){
					if((lines.size()-1) == line_index){
						lines.set(line_index, lines.get(line_index) + " " + aux_word);
						line_index++;
					}
					else{
						lines.add(aux_word);
					}
				}
				else if((word_width + line_width + space_width) > width){
					line_index++;
					lines.add(aux_word);
				}
				else if((word_width) > width){
					width = word_width;
				}
				else{
					lines.add(aux_word);
					line_index++;
				}
			}
			
			
			return lines;
		}
		
	}
	
	
	/**
	 * 
	 * @author juananthony
	 *
	 */
	private class WField
	{
		private String key;
		private ArrayList<String> info = new ArrayList<String>();
		private String original_info;
		
		WField(String key_p, String info_p)
		{
			key = key_p;
			original_info = info_p;
			createInfo(info_p, key, info);
		}
		
		WField(String key_p, String info_p, ArrayList<String> info_array)
		{
			key = key_p;
			original_info = info_p;
			info = (ArrayList<String>) info_array.clone();
		}
		
		public float getHeight()
		{
			return infopaint.getTextSize()*info.size() + keypaint.getTextSize();
		}
		
		public float getWidth(){
			float aux_width = -1;
			for(String aux : info){
				if(infopaint.measureText(aux) > aux_width)
					aux_width = infopaint.measureText(aux);
			}
			if(keypaint.measureText(key) > aux_width)
				aux_width = keypaint.measureText(key);
			return aux_width;
		}
		
		public String getOriginalInfo()
		{
			return original_info;
		}
		
		public String getKey()
		{
			return key;
		}
		
		public List<String> getInfo()
		{
			return info;
		}
		
		public void setKey(String key_p)
		{
			key = key_p;
		}
		
		public void setInfo(String info_p)
		{
			info = new ArrayList<String>();
			createInfo(info_p, key,info);
		}
		
		public int getNumberLines()
		{
			return 1+info.size();
		}
	}
	
	
	
	//*******************************************************
	//
	//		B U T T O N    C L A S S 
	//
	//*******************************************************
	/**
	 * 
	 * @author Juan Antonio Jimenez Lopez < ruchin10@gmail.com >
	 *
	 */
	public class WButton
	{
		private WBubble parent;
		private Bitmap button_icon;
		private int button_width, button_height;
		private float x,y;
		private Intent intent;
		private final static int hitmargin = 0;
		
		
		//   ***   C O N S T R U C T O R
		/**
		 * 
		 * @param parent_p
		 * @param icon_p
		 * @param position
		 * @param intent_p
		 */
		WButton(WBubble parent_p, Bitmap icon_p, WPoint position, Intent intent_p)
		{
			parent = parent_p;
			setIcon(icon_p);
			button_width = button_icon.getWidth();
			button_height = button_icon.getHeight();
			intent = intent_p;
		}
		
		//  ***   M E T H O D S
		private void calculateXY(MapView mapView, WPoint position_p, int index_p)
		{
			Projection projection = mapView.getProjection();
			Point point = new Point();
			projection.toPixels(position_p, point);
			
			x = point.x;
			y = point.y;
			
			if(isActorBubble())
				y = y - arrow_height - margin_bottom - actor.getIcon().getHeight() - button_icon.getHeight();
			else if(isPlaceBubble())
				y = y - arrow_height - margin_bottom - place.getIcon().getHeight() - button_icon.getHeight();
				
			x = x + interior_width/2 - margin_right - button_icon.getWidth();
			for(int i = 0; i < buttons.size(); i++)
			{
				if(i != index_p)
					x = x - parent.buttons.get(i).getBitmap().getWidth() - margin_buttons;
				if(i >= index_p)
					break;
			}
		}
		
		public boolean isHit(WPoint position_p, MapView mapView)
		{
			Projection projection = mapView.getProjection();
			Point hitpoint = new Point();
			projection.toPixels(position_p, hitpoint);
			
			if(hitpoint.x > (x - hitmargin) && hitpoint.x < (x+button_icon.getWidth() + hitmargin) 
					&& hitpoint.y > (y - hitmargin) && hitpoint.y < (y+button_icon.getHeight() + hitmargin))
			{
				return true;
			}
			else
				return false;
		}
		
		public float getX()
		{
			return x;
		}
		
		public float getY()
		{
			return y;
		}
		
		public Intent getIntent()
		{
			return intent;
		}
		
		public void setIcon(Bitmap icon_p)
		{
			Bitmap final_icon;
				
			// resize image
			double icon_prop = ((float) icon_p.getWidth()) / ((float)icon_p.getHeight());
			if(icon_p.getHeight() >= icon_p.getWidth())
			{
				if(icon_p.getHeight() >= MAX_BUTTON_HEIGHT)
				{
					double aux_width = MAX_BUTTON_HEIGHT * icon_prop;
					final_icon = Bitmap.createScaledBitmap(icon_p, (int) aux_width, MAX_BUTTON_HEIGHT, false);
				}
				else
					final_icon = icon_p;
			}
			else
			{
				if(icon_p.getWidth() >= MAX_BUTTON_WIDTH)
				{
					double aux_height = (MAX_BUTTON_WIDTH / icon_prop);
					final_icon = Bitmap.createScaledBitmap(icon_p, MAX_BUTTON_WIDTH, (int) aux_height, false);
				}
				else
					final_icon = icon_p;
			}
				
					
			button_icon = final_icon;
		}
		
		public Bitmap getBitmap()
		{
			return button_icon;
		}
		
		public int getWidth()
		{
			return button_icon.getWidth();
		}
		
		public int getHeight()
		{
			return button_icon.getHeight();
		}
		
	}
	
	//*******************************************************
	//
	//		C O N S T R U C T O R S
	//
	//*******************************************************
	/**
	 * Create a bubble in a position
	 * @param position_p
	 * @param text_p
	 */
	public WBubble(WPoint position_p, String text_p)
	{
		bubble_type = TEXT_BUBBLE;
		//actor = actor_p;
		text = text_p;
		position = position_p;
		marker_height = 0;
		field_group = new WFieldGroup();
		type_box = CURVED_BOX;
		
		isPointBubble = true;
		
		backpaint = new Paint();
		edgepaint = new Paint();
		textpaint = new Paint();
		infopaint = new Paint();
		iconpaint = new Paint();
		keypaint = new Paint();
		iconpaint.setARGB(255, 255, 255, 255);
		this.setBackgroundARGB(BACKGROUND_ALFA, BACKGROUND_RED, BACKGROUND_GREEN, BACKGROUND_BLUE);
		this.setEdgeARGB(EDGE_ALFA, EDGE_RED, EDGE_GREEN, EDGE_BLUE);
		this.setTextARGB(TEXT_ALFA, TEXT_RED, TEXT_GREEN, TEXT_BLUE);
		this.setInfoARGB(TEXT_ALFA, TEXT_RED, TEXT_GREEN, TEXT_BLUE);
		
		this.setTextNameSize(text_name_size);
		this.setInfoNameSize(text_info_size);
		
		this.calculateInteriorSizes();
		field_group.setWidth(text_width);
		
		//List<String> aux = new ArrayList<String>();
		//aux.add("");
		//createInfo("","",aux);
		//text_width = textpaint.measureText(text);
		this.calculateInteriorSizes();
		field_group.setWidth(text_width);
	}

	/**
	 * Create a bubble using a WActor
	 * @param actor_p
	 * @param bubble_type_p
	 */
	public WBubble(WActor actor_p, int bubble_type_p)
	{
		bubble_type = bubble_type_p;
		actor = actor_p;
		text = actor.getName();
		position = actor_p.getPosition();
		field_group = new WFieldGroup();
		if(actor.isAvatar())
			setIcon(actor.getAvatar().getBitmap());
		else
			setIcon(actor.getIcon().getBitmap());
		
		marker_height = actor.getIcon().getBitmap().getHeight();
		
		type_box = CURVED_BOX;
		
		backpaint = new Paint();
		edgepaint = new Paint();
		textpaint = new Paint();
		infopaint = new Paint();
		iconpaint = new Paint();
		keypaint = new Paint();
		iconpaint.setARGB(255, 255, 255, 255);
		this.setBackgroundARGB(BACKGROUND_ALFA, BACKGROUND_RED, BACKGROUND_GREEN, BACKGROUND_BLUE);
		this.setEdgeARGB(EDGE_ALFA, EDGE_RED, EDGE_GREEN, EDGE_BLUE);
		this.setTextARGB(TEXT_ALFA, TEXT_RED, TEXT_GREEN, TEXT_BLUE);
		this.setInfoARGB(TEXT_ALFA, TEXT_RED, TEXT_GREEN, TEXT_BLUE);
		
		this.setTextNameSize(text_name_size);
		this.setInfoNameSize(text_info_size);
		
		this.calculateInteriorSizes();
		field_group.setWidth(text_width);
		
		//createInfo(actor.getInformation());
		if(actor.getPhoneNumber() != "")
			addField("Teléfono", actor.getPhoneNumber());
		if(actor.getTwitter() != "")
			addField("Twitter", actor.getTwitter());
		if(actor.getInformation() != "")
			addField("Información adicional", actor.getInformation());
		
		this.calculateInteriorSizes();
		//field_group.setWidth(text_width);
	}
	
	/**
	 * Creates a new WBubble using a WPlace
	 * @param place_p
	 * @param bubble_type_p
	 */
	public WBubble(WPlace place_p, int bubble_type_p)
	{
		bubble_type = bubble_type_p;
		place = place_p;
		text = place.getName();
		position = place.getPosition();
		setIcon(place.getIcon().getBitmap());
		
		field_group = new WFieldGroup();
		
		marker_height = place.getIcon().getBitmap().getHeight();
		
		type_box = CURVED_BOX;
		
		backpaint = new Paint();
		edgepaint = new Paint();
		textpaint = new Paint();
		infopaint = new Paint();
		iconpaint = new Paint();
		keypaint = new Paint();
		iconpaint.setARGB(255, 255, 255, 255);
		this.setBackgroundARGB(BACKGROUND_ALFA, BACKGROUND_RED, BACKGROUND_GREEN, BACKGROUND_BLUE);
		this.setEdgeARGB(EDGE_ALFA, EDGE_RED, EDGE_GREEN, EDGE_BLUE);
		this.setTextARGB(TEXT_ALFA, TEXT_RED, TEXT_GREEN, TEXT_BLUE);
		this.setInfoARGB(TEXT_ALFA, TEXT_RED, TEXT_GREEN, TEXT_BLUE);
		
		this.setTextNameSize(text_name_size);
		this.setInfoNameSize(text_info_size);
		
		this.calculateInteriorSizes();
		field_group.setWidth(text_width);
		
		//createInfo(place.getInformation());
		if(place.getAddress() != "")
			addField("Dirección", place.getAddress()); 
		if(place.getPhoneNumber() != "")
			addField("Teléfono", place.getPhoneNumber());
		if(place.getTwitter() != "")
			addField("Twitter", place.getTwitter());
		if(place.getInformation() != "")
			addField("Información adicional", place.getInformation());
		
		this.calculateInteriorSizes();
		//field_group.setWidth(text_width);
		//this.calculateInteriorSizes();
	}
	
	/**
	 * Creates a new WBubble using a WArea
	 * @param area_p
	 * @param bubble_type_p
	 */
	public WBubble(WArea area_p, int bubble_type_p)
	{
		bubble_type = bubble_type_p;
		
		text = area_p.getName();
		position = area_p.getCenter();
		
		marker_height = 0;
		
		type_box = CURVED_BOX;
		
		field_group = new WFieldGroup();
		
		isAreaBubble = true;
		
		backpaint = new Paint();
		edgepaint = new Paint();
		textpaint = new Paint();
		infopaint = new Paint();
		iconpaint = new Paint();
		keypaint = new Paint();
		iconpaint.setARGB(255, 255, 255, 255);
		this.setBackgroundARGB(BACKGROUND_ALFA, BACKGROUND_RED, BACKGROUND_GREEN, BACKGROUND_BLUE);
		this.setEdgeARGB(EDGE_ALFA, EDGE_RED, EDGE_GREEN, EDGE_BLUE);
		this.setTextARGB(TEXT_ALFA, TEXT_RED, TEXT_GREEN, TEXT_BLUE);
		this.setInfoARGB(TEXT_ALFA, TEXT_RED, TEXT_GREEN, TEXT_BLUE);
		
		this.setTextNameSize(text_name_size);
		this.setInfoNameSize(text_info_size);
		
		//String info_aux = "Center: (" + area_p.getCenter().getLatitudeE6() + ", " + area_p.getCenter().getLongitudeE6() +")"; 
		
		this.calculateInteriorSizes();
		field_group.setWidth(text_width);
		
		addField("Area",area_p.getSurface()+" m2");
		if(area_p.getAreaType() == WArea.CIRCULAR_AREA)
			addField("Radio",area_p.getRadius() + " m");
		
		this.calculateInteriorSizes();
		//field_group.setWidth(text_width);
	}
	
	//*******************************************************
	//
	//		M E T H O D S
	//
	//*******************************************************
	/**
	 * Add a new Field into the bubble using his key and information
	 * @param key_p
	 * @param info_p
	 */
	public void addField(String key_p, String info_p)
	{
		if(field_group != null)
			field_group.addField(key_p, info_p);
		if(field_group != null)
			this.calculateInteriorSizes();
	}
	
	/**
	 * Returns the WActor if the bubble is associated with an actor; Returns Null otherwise
	 * @return
	 */
	public WActor getActor()
	{
		if(isActorBubble())
		{
			return actor;
		}
		else
			return null;
	}
	
	/**
	 * Returns the WPlace if the bubble is associated with a place; Returns Null otherwise
	 * @return
	 */
	public WPlace getPlace()
	{
		if(isPlaceBubble())
		{
			return place;
		}
		else
			return null;
	}
	
	/**
	 * Adds a new button on the bubble
	 * @param icon_p
	 * @param intent_p
	 */
	public void addButton(Bitmap icon_p, Intent intent_p)
	{
		if(actor != null)
			buttons.add(new WButton(this, icon_p, actor.getPosition(), intent_p));
		else
			if(place != null)
				buttons.add(new WButton(this, icon_p, place.getPosition(), intent_p));
		calculateInteriorSizes();
		if(field_group != null)
			field_group.recalculateFields();
		bubble_type  = WBubble.TEXT_ICON_INFO_BUTTON_BUBBLE;
	}
	
	/**
	 * Adds a new button on the bubble
	 * @param drawable_p
	 * @param intent_p
	 */
	public void addButton(Drawable drawable_p, Intent intent_p)
	{
		Bitmap icon_p = ((BitmapDrawable) drawable_p).getBitmap();
		if(actor != null)
			buttons.add(new WButton(this, icon_p, actor.getPosition(), intent_p));
		else
			if(place != null)
				buttons.add(new WButton(this, icon_p, place.getPosition(), intent_p));
		calculateInteriorSizes();
		if(field_group != null)
			field_group.recalculateFields();
		bubble_type  = WBubble.TEXT_ICON_INFO_BUTTON_BUBBLE;
	}
	
	/**
	 * Returns the max height of all buttons
	 * @return
	 */
	public int getMaxButtonsHeight()
	{
		int max=-1;
		for(int i=0; i < buttons.size(); i++)
		{
			if(buttons.get(i).getHeight() > max)
				max = buttons.get(i).getHeight();
		}
		return max;
	}
	
	/**
	 * Sets a new bubble's icon
	 * @param icon_p
	 */
	public void setIcon(Bitmap icon_p)
	{
		Bitmap final_icon;
		
		// resize image
		double icon_prop = ((float) icon_p.getWidth()) / ((float)icon_p.getHeight());
		if(icon_p.getHeight() >= icon_p.getWidth())
		{
			if(icon_p.getHeight() >= MAX_ICON_HEIGHT)
			{
				double aux_width = MAX_ICON_HEIGHT * icon_prop;
				final_icon = Bitmap.createScaledBitmap(icon_p, (int) aux_width, MAX_ICON_HEIGHT, false);
			}
			else
				final_icon = icon_p;
		}
		else
		{
			if(icon_p.getWidth() >= MAX_ICON_WIDTH)
			{
				double aux_height = (MAX_ICON_WIDTH / icon_prop);
				final_icon = Bitmap.createScaledBitmap(icon_p, MAX_ICON_WIDTH, (int) aux_height, false);
			}
			else
				final_icon = icon_p;
		}
			
				
		icon = final_icon;
	}
	
	
	
	/**
	 * Creates the info fields using bubble size
	 * @param info_p
	 */
	private void createInfo(String info_p, String key_p, List<String> textinfo)
	{
		String auxString = "";
		
		for(int i=0; i < num_min_chars_line; i++)
			auxString = auxString + "a";
		
		float name_width = textpaint.measureText(text);
		float info_width = this.getSizeMaxKeyField();
		if(info_width < keypaint.measureText(key_p))
			info_width = keypaint.measureText(key_p);
		
		int num_chars;
		
		if(name_width > info_width)
			text_width = name_width;
		else
			text_width = info_width;
		
		// name_width thiner than min_width
		if(text_width < min_text_width)
		{
			// calculate the number of chars to establish the best width
			for(num_chars = 1; num_chars < info_p.length(); num_chars++)
			{
				float aux_width = infopaint.measureText(info_p.substring(0, num_chars));
				if(aux_width > min_text_width)
				{
					text_width = aux_width;
					break;
				}
			}
		}
		else
		{
			if(name_width > info_width)
				text_width = name_width;
			else
				text_width = info_width;
			// calculate the number of chars to establish the best width
			for(num_chars = 1; num_chars < info_p.length(); num_chars++)
			{
				float aux_width = infopaint.measureText(info_p.substring(0, num_chars));
				if(aux_width > text_width)
				{
					break;
				}
			}
		}
	}
	
	/**
	 * Set a new text size to bubble's title
	 * @param size_p
	 */
	public void setTextNameSize(int size_p)
	{
		textpaint.setTextSize(size_p);
		textpaint.setTypeface(Typeface.DEFAULT_BOLD);
		this.calculateInteriorSizes();
	}
	
	/**
	 * Set a new size to the information fields
	 * @param size_p
	 */
	public void setInfoNameSize(int size_p)
	{
		infopaint.setTextSize(size_p);
		keypaint.setTextSize(size_p);
		//Typeface tf = Typeface.create("Helvetica",Typeface.DEFAULT_BOLD);
		keypaint.setTypeface(Typeface.DEFAULT_BOLD);
		this.calculateInteriorSizes();
	}
	
	/**
	 * Returns True if bubble is associated to an actor; Returns False otherwise
	 * @return
	 */
	public boolean isActorBubble()
	{
		if(actor != null)
			return true;
		else
			return false;
	}
	
	/**
	 * Returns True if bubble is associated to an area; Returns False otherwise
	 * @return
	 */
	public boolean isAreaBubble()
	{
		return isAreaBubble;
	}
	
	/**
	 * Returns True if bubble is associated to a point; Returns False otherwise
	 * @return
	 */
	public boolean isPointBubble()
	{
		return isPointBubble;
	}
	
	/**
	 * Returns True if bubble is associated to an place; Returns False otherwise
	 * @return
	 */
	public boolean isPlaceBubble()
	{
		if(place != null)
			return true;
		else
			return false;
	}
	
	/**
	 * Sets a new bubble background color using Alfa-RGB
	 * @param alfa_p
	 * @param red_p
	 * @param green_p
	 * @param blue_p
	 */
	public void setBackgroundARGB(int alfa_p, int red_p, int green_p, int blue_p)
	{
		backpaint.setARGB(alfa_p, red_p, green_p, blue_p);
	}
	
	/**
	 * Sets a new bubble background edge using Alfa-RGB
	 * @param alfa_p
	 * @param red_p
	 * @param green_p
	 * @param blue_p
	 */
	public void setEdgeARGB(int alfa_p, int red_p, int green_p, int blue_p)
	{
		edgepaint.setARGB(alfa_p, red_p, green_p, blue_p);
		if(field_group != null)
			field_group.recalculateFields();
	}
	
	/**
	 * Sets a new bubble title color using Alfa-RGB
	 * @param alfa_p
	 * @param red_p
	 * @param green_p
	 * @param blue_p
	 */
	public void setTextARGB(int alfa_p, int red_p, int green_p, int blue_p)
	{
		textpaint.setARGB(alfa_p, red_p, green_p, blue_p);
		if(field_group != null)
			field_group.recalculateFields();
	}
	
	/**
	 * Sets a new bubble info color using Alfa-RGB
	 * @param alfa_p
	 * @param red_p
	 * @param green_p
	 * @param blue_p
	 */
	public void setInfoARGB(int alfa_p, int red_p, int green_p, int blue_p)
	{
		infopaint.setARGB(alfa_p, red_p, green_p, blue_p);
		keypaint.setARGB(alfa_p, red_p, green_p, blue_p);
		if(field_group != null)
			field_group.recalculateFields();
	}
	
	/**
	 * Recalculates the bubble interior size
	 */
	private void calculateInteriorSizes()
	{
		float name_width = textpaint.measureText(text);
		float info_width = 0;
		//if((bubble_type != TEXT_BUBBLE) && (bubble_type != TEXT_ICON_BUBBLE))
			if(field_group != null)
				info_width = field_group.getWidth();
		if(name_width > info_width)
			text_width = name_width;
		else
			text_width = info_width;
		
		switch(bubble_type)
		{
		case TEXT_BUBBLE:
			interior_width = text_width + margin_left + margin_right;
			interior_height = textpaint.getTextSize() + margin_top + margin_bottom;
			break;
			
		case TEXT_ICON_BUBBLE:
			interior_width = text_width + icon.getWidth() + margin_left + margin_right + margin_icon_name;
			if(textpaint.getTextSize() > icon.getHeight())
				interior_height = textpaint.getTextSize() + margin_top + margin_bottom;
			else
				interior_height = icon.getHeight() + margin_top + margin_bottom;
			break;
			
		case TEXT_INFO_BUBBLE:
			if(icon != null)
			{
				interior_width = text_width + icon.getWidth() + margin_left + margin_right + margin_icon_name;
				float info_and_name = textpaint.getTextSize() + margin_top + margin_bottom + margin_name_info;
				info_and_name += field_group.getHeight();
				/*for(int i=0; i< field_group.size(); i++)
				{
					info_and_name += (field_group.get(i).getNumberLines()+1) * (infopaint.getTextSize() + margin_name_info);
				}*/
				if(info_and_name > icon.getHeight())
					interior_height = info_and_name + margin_top + margin_bottom;
				else
					interior_height = icon.getHeight() + margin_top + margin_bottom;
			}
			else
			{
				interior_width = text_width + margin_left + margin_right;
				float info_and_name = textpaint.getTextSize() + margin_top + margin_bottom + margin_name_info;
				info_and_name += field_group.getHeight();
				/*for(int i=0; i< field_group.size(); i++)
				{
					info_and_name += (field_group.get(i).getNumberLines()+1) * (infopaint.getTextSize() + margin_name_info);
				}*/
				interior_height = info_and_name + margin_top + margin_bottom;
			}
			
			break;
					
		case TEXT_ICON_INFO_BUBBLE:
			if(icon != null)
			{
				interior_width = text_width + icon.getWidth() + margin_left + margin_right + margin_icon_name;
				float info_and_name = textpaint.getTextSize() + margin_top + margin_bottom + margin_name_info;
				info_and_name += field_group.getHeight();
				/*for(int i=0; i< field_group.size(); i++)
				{
					info_and_name += (field_group.get(i).getNumberLines()+1) * (infopaint.getTextSize() + margin_name_info);
				}*/
				if(info_and_name > icon.getHeight())
					interior_height = info_and_name + margin_top + margin_bottom;
				else
					interior_height = icon.getHeight() + margin_top + margin_bottom;
			}
			else
			{
				interior_width = text_width + margin_left + margin_right;
				float info_and_name = textpaint.getTextSize() + margin_top + margin_bottom + margin_name_info;
				info_and_name += field_group.getHeight();
				/*for(int i=0; i< field_group.size(); i++)
				{
					info_and_name += (field_group.get(i).getNumberLines()+1) * (infopaint.getTextSize() + margin_name_info);
				}*/
				interior_height = info_and_name + margin_top + margin_bottom;
			}
			
			break;
			
		case TEXT_ICON_INFO_BUTTON_BUBBLE:
			float interior_width_text = text_width + icon.getWidth() + margin_left + margin_right + margin_icon_name;
			float interior_width_button = icon.getWidth() + margin_left + margin_icon_name + margin_right + calculateButtonsWidth();
			if(interior_width_text > interior_width_button)
				interior_width = interior_width_text;
			else
				interior_width = interior_width_button;

			float info_name_icon = textpaint.getTextSize() + margin_name_info + getMaxButtonsHeight() + margin_info_button;
			info_name_icon += field_group.getHeight();
			/*for(int i=0; i< field_group.size(); i++)
			{
				info_name_icon += (field_group.get(i).getNumberLines()) * (infopaint.getTextSize() + margin_name_info);
			}*/
			if(info_name_icon > icon.getHeight())
				interior_height = info_name_icon + margin_top + margin_bottom;
			else
				interior_height = icon.getHeight() + margin_top + margin_bottom;
			break;
		}
	}
	
	/**
	 * Returns the largest info key
	 * @return
	 */
	public float getSizeMaxKeyField()
	{
		float max = 0;
		for(int i=0; i < field_group.size(); i++)
		{
			float aux_max = keypaint.measureText(field_group.get(i).getKey());
			if(aux_max > max)
				max = aux_max;
		}
		return max;
	}
	
	/**
	 * Calculates how much space spends the buttons array
	 * @return
	 */
	private float calculateButtonsWidth()
	{
		float width = 0;
		for(WButton button : buttons)
		{
			width += button.getWidth();
		}
		width += margin_buttons * (buttons.size() - 1);
		return width;
	}
	
	
	//*******************************************************
	//
	//		D R A W
	//
	//*******************************************************
	/**
	 * Draws the bubble properly
	 * @param canvas
	 * @param mapView
	 * @param shadow
	 * @param when
	 */
	public void draw(Canvas canvas, MapView mapView, boolean shadow, long when)
	{
		this.calculateInteriorSizes();
		field_group.recalculateFields();
		Projection projection = mapView.getProjection();
		Point point = new Point();
		Point top_point = new Point();
		if(isActorBubble())
			projection.toPixels(actor.getPosition(), point);
		else if(isPlaceBubble())
			projection.toPixels(place.getPosition(), point);
		else
			projection.toPixels(position, point);
		point.set(point.x, point.y - marker_height);
		
		if(!is_showed){
			mapView.getController().animateTo(projection.fromPixels(point.x, (int)((point.y + point.y-interior_height)/2)));
			//mapView.getController().setCenter(projection.fromPixels(point.x, (int)((point.y + point.y-interior_height)/2)));
			is_showed = true;
		}
		Path edgepath = new Path();
		Path boxpath = new Path();
		
		switch(type_box)
		{
		case RECTANGLE_BOX:
			// DRAW THE EDGE PATH
			edgepath.moveTo(point.x - interior_width/2 - edge,
					point.y - interior_height - arrow_height - edge);
			edgepath.lineTo(point.x + interior_width/2 + edge,
					point.y - interior_height - arrow_height - edge);
			edgepath.lineTo(point.x + interior_width/2 + edge,
					point.y - arrow_height + edge);
			
			// vertex part
			edgepath.lineTo(point.x + arrow_width/2,
					point.y - arrow_height + edge);
			edgepath.lineTo(point.x, point.y);
			edgepath.lineTo(point.x - arrow_width/2,
					point.y - arrow_height + edge);
			
			// back to box
			edgepath.lineTo(point.x - interior_width/2 - edge, 
					point.y - arrow_height + edge);
			edgepath.close();
			canvas.drawPath(edgepath, edgepaint);
			
			// DRAW THE BOX-BACKGROUND PATH
			boxpath.moveTo(point.x - interior_width/2 ,
					point.y - interior_height - arrow_height);
			boxpath.lineTo(point.x + interior_width/2,
					point.y - interior_height - arrow_height);
			boxpath.lineTo(point.x + interior_width/2,
					point.y - arrow_height);
			boxpath.lineTo(point.x - interior_width/2, 
					point.y - arrow_height);
			boxpath.close();
			canvas.drawPath(boxpath, backpaint);
			break;
			
			
		case CURVED_BOX:
			// DRAW THE EDGE PATH
			edgepath.moveTo(point.x - interior_width/2 - edge,
					point.y - interior_height - arrow_height - edge + curved_radious);
			edgepath.lineTo(point.x - interior_width/2 - edge + curved_radious,
					point.y - interior_height - arrow_height - edge + curved_radious);
			edgepath.lineTo(point.x - interior_width/2 - edge + curved_radious,
					point.y - interior_height - arrow_height - edge);
			
			edgepath.lineTo(point.x + interior_width/2 + edge - curved_radious,
					point.y - interior_height - arrow_height - edge);
			edgepath.lineTo(point.x + interior_width/2 + edge - curved_radious,
					point.y - interior_height - arrow_height - edge + curved_radious);
			edgepath.lineTo(point.x + interior_width/2 + edge,
					point.y - interior_height - arrow_height - edge + curved_radious);
			
			
			edgepath.lineTo(point.x + interior_width/2 + edge,
					point.y - arrow_height + edge - curved_radious);
			edgepath.lineTo(point.x + interior_width/2 + edge - curved_radious,
					point.y - arrow_height + edge - curved_radious);
			edgepath.lineTo(point.x + interior_width/2 + edge - curved_radious,
					point.y - arrow_height + edge);
			
			// arrow's part
			edgepath.lineTo(point.x + arrow_width/2,
					point.y - arrow_height + edge);
			edgepath.lineTo(point.x, point.y);
			edgepath.lineTo(point.x - arrow_width/2,
					point.y - arrow_height + edge);
			
			// back to box
			edgepath.lineTo(point.x - interior_width/2 - edge + curved_radious, 
					point.y - arrow_height + edge);
			edgepath.lineTo(point.x - interior_width/2 - edge + curved_radious, 
					point.y - arrow_height + edge - curved_radious);
			edgepath.lineTo(point.x - interior_width/2 - edge, 
					point.y - arrow_height + edge - curved_radious);
			edgepath.close();
			canvas.drawPath(edgepath, edgepaint);
			
			canvas.drawCircle(point.x - interior_width/2 - edge + curved_radious,
					point.y - interior_height - arrow_height - edge + curved_radious, 
					curved_radious, 
					edgepaint);
			canvas.drawCircle(point.x + interior_width/2 + edge - curved_radious,
					point.y - interior_height - arrow_height - edge + curved_radious, 
					curved_radious, 
					edgepaint);
			canvas.drawCircle(point.x + interior_width/2 + edge - curved_radious,
					point.y - arrow_height + edge - curved_radious, 
					curved_radious, 
					edgepaint);
			canvas.drawCircle(point.x - interior_width/2 - edge + curved_radious, 
					point.y - arrow_height + edge - curved_radious, 
					curved_radious, 
					edgepaint);
			
			//
			// DRAW THE BOX-BACKGROUND PATH
			//
			boxpath.moveTo(point.x - interior_width/2,
					point.y - interior_height - arrow_height + curved_radious);
			boxpath.lineTo(point.x - interior_width/2 + curved_radious,
					point.y - interior_height - arrow_height + curved_radious);
			boxpath.lineTo(point.x - interior_width/2 + curved_radious,
					point.y - interior_height - arrow_height);
			
			boxpath.lineTo(point.x + interior_width/2 - curved_radious,
					point.y - interior_height - arrow_height);
			boxpath.lineTo(point.x + interior_width/2 - curved_radious,
					point.y - interior_height - arrow_height + curved_radious);
			boxpath.lineTo(point.x + interior_width/2,
					point.y - interior_height - arrow_height + curved_radious);
			
			
			boxpath.lineTo(point.x + interior_width/2,
					point.y - arrow_height - curved_radious);
			boxpath.lineTo(point.x + interior_width/2 - curved_radious,
					point.y - arrow_height - curved_radious);
			boxpath.lineTo(point.x + interior_width/2 - curved_radious,
					point.y - arrow_height);
			
			boxpath.lineTo(point.x - interior_width/2 + curved_radious, 
					point.y - arrow_height);
			boxpath.lineTo(point.x - interior_width/2 + curved_radious, 
					point.y - arrow_height - curved_radious);
			boxpath.lineTo(point.x - interior_width/2, 
					point.y - arrow_height - curved_radious);
			
			boxpath.close();
			canvas.drawPath(boxpath, backpaint);
			
			canvas.drawCircle(point.x - interior_width/2 + curved_radious, 
					point.y - interior_height - arrow_height + curved_radious, 
					curved_radious, 
					backpaint);
			canvas.drawCircle(point.x + interior_width/2 - curved_radious,
					point.y - interior_height - arrow_height + curved_radious,
					curved_radious,
					backpaint);
			canvas.drawCircle(point.x + interior_width/2 - curved_radious,
					point.y - arrow_height - curved_radious,
					curved_radious,
					backpaint);
			canvas.drawCircle(point.x - interior_width/2 + curved_radious, 
					point.y - arrow_height - curved_radious, 
					curved_radious, 
					backpaint);
			break;
		}
		
		// DRAW THE TITLE
		float buttons_width = calculateButtonsWidth();
		if(text_width > buttons_width)
			canvas.drawText(text, point.x + interior_width/2 - margin_right - text_width , point.y - interior_height + margin_top + textpaint.getTextSize() - arrow_height, textpaint);
		else
			canvas.drawText(text, point.x + interior_width/2 - margin_right - buttons_width , point.y - interior_height + margin_top + textpaint.getTextSize() - arrow_height, textpaint);
		
		int num_lines=0;
		
		switch(bubble_type)
		{
		case TEXT_ICON_BUBBLE:
			canvas.drawBitmap(icon, point.x - interior_width/2 + margin_left, point.y-interior_height - arrow_height + margin_top, iconpaint);
			break;
			
		case TEXT_ICON_INFO_BUBBLE:
			// DRAW THE LINE BETWEEN NAME AND INFO
			canvas.drawLine(point.x + interior_width/2 - margin_right - text_width,
					point.y - interior_height + margin_top + textpaint.getTextSize() - arrow_height + margin_name_info/2,
					point.x + interior_width/2 - margin_right - text_width + text_width,
					point.y - interior_height + margin_top + textpaint.getTextSize() - arrow_height + margin_name_info/2,
					edgepaint);
			
			// DRAW INFO BOX
			
			for(int i=0; i < field_group.size(); i++)
			{
				canvas.drawText(field_group.get(i).getKey(), point.x + interior_width/2 - margin_right - text_width,
						point.y - interior_height + margin_top + textpaint.getTextSize() + (num_lines+1)*margin_name_info + (num_lines+1)*infopaint.getTextSize() - arrow_height, keypaint);
				num_lines++;
				for(int j=0; j < field_group.get(i).getNumberLines()-1; j++)
				{
					canvas.drawText(field_group.get(i).getInfo().get(j), point.x + interior_width/2 - margin_right - text_width,
							point.y - interior_height + margin_top + textpaint.getTextSize() + (num_lines+1)*margin_name_info + (num_lines+1)*infopaint.getTextSize() - arrow_height, infopaint);
			
				num_lines++;
				}
			}
			
			// DRAW ICON 
			if(icon != null)
				canvas.drawBitmap(icon, point.x - interior_width/2 + margin_left, point.y-interior_height - arrow_height + margin_top, iconpaint);
			break;
			
		case TEXT_ICON_INFO_BUTTON_BUBBLE:
			// DRAW THE LINE BETWEEN NAME AND INFO
			if(text_width > buttons_width)
				canvas.drawLine(point.x + interior_width/2 - margin_right - text_width,
						point.y - interior_height + margin_top + textpaint.getTextSize() - arrow_height + margin_name_info/2,
						point.x + interior_width/2 - margin_right - text_width + text_width,
						point.y - interior_height + margin_top + textpaint.getTextSize() - arrow_height + margin_name_info/2,
						edgepaint);
			else
				canvas.drawLine(point.x + interior_width/2 - margin_right - buttons_width,
						point.y - interior_height + margin_top + textpaint.getTextSize() - arrow_height + margin_name_info/2,
						point.x + interior_width/2 - margin_right,
						point.y - interior_height + margin_top + textpaint.getTextSize() - arrow_height + margin_name_info/2,
						edgepaint);
			
			// DRAW INFO BOX
			for(int i=0; i < field_group.size(); i++)
			{
				// if text_width is bigger than all buttons widht
				if(text_width > buttons_width)
					canvas.drawText(field_group.get(i).getKey(), point.x + interior_width/2 - margin_right - text_width,
							point.y - interior_height + margin_top + textpaint.getTextSize() + (num_lines+1)*margin_name_info + (num_lines+1)*infopaint.getTextSize() - arrow_height, keypaint);
				else
					canvas.drawText(field_group.get(i).getKey(), point.x + interior_width/2 - margin_right - buttons_width,
							point.y - interior_height + margin_top + textpaint.getTextSize() + (num_lines+1)*margin_name_info + (num_lines+1)*infopaint.getTextSize() - arrow_height, keypaint);
					
				num_lines++;
				for(int j=0; j< field_group.get(i).getNumberLines()-1; j++)
				{
					//num_lines++;
					if(text_width > buttons_width)
						canvas.drawText(field_group.get(i).getInfo().get(j), point.x + interior_width/2 - margin_right - text_width,
								point.y - interior_height + margin_top + textpaint.getTextSize() + (num_lines+1)*margin_name_info + (num_lines+1)*infopaint.getTextSize() - arrow_height, infopaint);
					else
						canvas.drawText(field_group.get(i).getInfo().get(j), point.x + interior_width/2 - margin_right - buttons_width,
								point.y - interior_height + margin_top + textpaint.getTextSize() + (num_lines+1)*margin_name_info + (num_lines+1)*infopaint.getTextSize() - arrow_height, infopaint);
					num_lines++;
				}
			}
			
			// DRAW ICON 
			if(icon != null)
				canvas.drawBitmap(icon, point.x - interior_width/2 + margin_left, point.y-interior_height - arrow_height + margin_top, iconpaint);
			
			// DRAW BUTTONS
			for(int i=0; i<buttons.size(); i++)
			{
				if(isActorBubble())
					buttons.get(i).calculateXY(mapView, actor.getPosition(), i);
				else if(isPlaceBubble())
					buttons.get(i).calculateXY(mapView, place.getPosition(), i);
			}
			for(int i=0; i<buttons.size(); i++)
			{
				canvas.drawBitmap(buttons.get(i).getBitmap(), buttons.get(i).getX(), buttons.get(i).getY(), edgepaint);
			}
			
			break;
		}
	}
	
}
