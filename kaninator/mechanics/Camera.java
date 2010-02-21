/**
 * The game engine package.
 * Implements all the generic services needed for a 2d game. 
 */
package kaninator.mechanics;

import java.util.*;

import kaninator.graphics.*;
import kaninator.io.MapLoader;

/**
 * Renders the internal game objects so they can be drawn two dimensionally.
 * Responsible for converting the internal three-dimensional coordinates of the objects, both static and
 * dynamic, to a 2-dimensional representation, and sorting them so they will be drawn in the correct order
 * by an object that implements the Canvas interface.
 * @author phedman
 * @see kaninator.graphics.Canvas
 */
public class Camera
{
	private Canvas canvas;
	private GUI gui;

	private static final double FOLLOW_SPEED = 4.0;
	private static final double FOLLOW_BORDER_SIZE = 1.0/4.0;
	private static final int OFF_TOP = 1, OFF_BOTTOM = 2, OFF_LEFT = 4, OFF_RIGHT = 8;

	private int x, y, numHorizontalTiles;;
	private ArrayList<DynamicObject> objects;
	private ArrayList<ArrayList<StaticObject>> tiles;
	private TreeMap<Integer, ArrayList<VisibleElement>> orderedObjects;


	/**
	 * @param _canvas The canvas which actually draws the rendered data.
	 * @param _gui The overlay GUI which will always be on top.
	 */
	public Camera(Canvas _canvas, GUI _gui)
	{
		canvas = _canvas;
		gui = _gui;

		x = y = 0;
		numHorizontalTiles = (int)(canvas.getWidth()/(MapLoader.getTileSize() * 2));

		objects = new ArrayList<DynamicObject>();
		orderedObjects = new TreeMap<Integer, ArrayList<VisibleElement>>();
		tiles = null;
	}

	/**
	 * Parses the GUI and sends the elements to the canvas.
	 * @see kaninator.mechanics.GUI
	 * @see kaninator.graphics.VisibleElement
	 */
	public void renderGUI()
	{
		ArrayList<VisibleElement> guiElems = gui.render();

		for(VisibleElement elem : guiElems)
			canvas.addElement(elem);

		canvas.draw();
	}

	/**
	 * Clears the GUI from the canvas so it can be redrawn.
	 */
	public void clearGUI()
	{
		canvas.clearTop(gui.size());
	}
	
	/**
	 * Adds a DynamicObject for the camera to handle.
	 * @param object The DynamicObject you want to show on screen.
	 */
	public void addObject(DynamicObject object)
	{
		objects.add(object);
	}

	/**
	 * Clears the DynamicObjects the camera is aware of.
	 */
	public void clearObjects()
	{
		objects.clear();
	}

	/**
	 * Sets the static objects the camera is aware of.
	 * @param _tiles A 2 dimensional ArrayList of StaticObjects
	 */
	public void setTiles(ArrayList<ArrayList<StaticObject>> _tiles)
	{
		tiles = _tiles;
	}

	/**
	 * Makes the camera smoothly follow a dynamic object. Should be called every frame.
	 * @param obj The object you want the camera to follow.
	 */
	public void follow(DynamicObject obj)
	{
		double d_x = obj.render_x() - x;
		double d_y = obj.render_y() - obj.getHeight() - y;

		if(d_x < canvas.getWidth() * FOLLOW_BORDER_SIZE)
			x -= (canvas.getWidth() * FOLLOW_BORDER_SIZE - d_x)/FOLLOW_SPEED;
		else if(d_x > canvas.getWidth() - canvas.getWidth() * FOLLOW_BORDER_SIZE)
			x += (d_x - (canvas.getWidth() - canvas.getWidth() * FOLLOW_BORDER_SIZE))/FOLLOW_SPEED;

		if(d_y < canvas.getHeight() * FOLLOW_BORDER_SIZE)
			y -= (canvas.getHeight() * FOLLOW_BORDER_SIZE - d_y)/FOLLOW_SPEED;
		else if(d_y > canvas.getHeight() - canvas.getHeight() * FOLLOW_BORDER_SIZE)
			y += (d_y - (canvas.getHeight() - canvas.getHeight() * FOLLOW_BORDER_SIZE))/FOLLOW_SPEED;
	}

	/**
	 * Clears the Canvas object and sends new VisbleElements to it.
	 * Parses the internal game data and sends it to a Canvas object to be drawn.
	 * @see Canvas
	 * @see VisibleElement
	 */
	public void render()
	{
		canvas.clear();
		orderedObjects.clear();

		orderStatics();
		orderDynamics();

		for(ArrayList<VisibleElement> list : orderedObjects.values())
		{
			for(VisibleElement e : list)
				canvas.addElement(e);
		}	  
	}
	
	/**
	 * Checks if the object is off screen, returns the on what side it is as well.
	 * @param x The leftmost x coordinate of the object
	 * @param y The topmost y coordinate of the object
	 * @param imgWidth The width of the object
	 * @param imgHeight The height of the object
	 * @return 0 if the object is on the screen, any combination of (OFF_TOP , OFF_BOTTOM, OFF_LEFT, OFF RIGHT) if it isn't.
	 */
	private int offScreen(int x, int y, int h, int imgWidth, int imgHeight)
	{
		int retValue = 0;
		
		if(y + imgHeight - h < 0)
			retValue |= OFF_TOP;
		if(y - h > canvas.getHeight())
			retValue |= OFF_BOTTOM;
		if(x + imgWidth < 0)
			retValue |= OFF_LEFT;
		if(x > canvas.getWidth())
			retValue |= OFF_RIGHT;
		
		return retValue;
	}
	
	/**
	 * Loops through the DynamicObjects, creates VisibleElements from them
	 * and orders them according to their depth.
	 */
	private void orderDynamics()
	{
		for(DynamicObject object : objects)
		{
			int key = object.getDepth();
			int obj_x = object.render_x() - x;
			int obj_y = object.render_y() - y;
			
			if(offScreen(obj_x, obj_y, (int)object.getHeight(), object.getAnimation().getWidth(), object.getAnimation().getHeight()) > 0)
				continue;
			
			ArrayList<VisibleElement> list = orderedObjects.get(key);
			if(list == null)
			{
				list = new ArrayList<VisibleElement>();
				orderedObjects.put(key, list);	 
			}
			list.add(new VisibleElement(object.getAnimation(),
					obj_x, obj_y,
					(int)object.getHeight()));	
		}
	}
	
	/**
	 * Loops through the StaticObjects, creates VisibleElements from them
	 * and orders them according to their depth.
	 */
	private void orderStatics()
	{
		int camIndex_x = x / (int)MapLoader.getTileSize();
		int camIndex_y = y / (int)MapLoader.getTileHeight();
		
		//Minimum internal y coordinate for the tiles
		int min_y = (camIndex_y + camIndex_x - 1)/2;
		if(min_y < 0)
			min_y = 0;
		
		//Minimum internal x coordinate for the tiles
		int min_x = (camIndex_y - camIndex_x + 1)/2 - numHorizontalTiles;
		if(min_x < 0)
			min_x = 0;

		for(int i = min_x; i < tiles.size(); i++)
		{
			//If we are below the screen, then don't bother drawing
			if(tiles.get(i).get(0).render_y() - tiles.get(i).get(0).renderHeight() - y > canvas.getHeight())
				break;
			
			for(int j = min_y; j < tiles.get(i).size(); j++)
			{
				StaticObject object = tiles.get(i).get(j);
				//Calculate screen coordinates and depth
				int obj_x = object.render_x() - x;
				int obj_y =  object.render_y() - y;
				int key = object.getDepth();

				//If we already are below the screen, then stop drawing this column
				if((offScreen(obj_x, obj_y, object.renderHeight(), (int)MapLoader.getTileSize() * 2, (int)MapLoader.getTileSize()) & OFF_BOTTOM) > 0)
					break;

				//If there are no tiles with the same depth value, create a new array for the depth value
				ArrayList<VisibleElement> list = orderedObjects.get(key);
				if(list == null)
				{
					list = new ArrayList<VisibleElement>();
					orderedObjects.put(key, list);	 
				}

				//Fill the lower tiles first
				for(int height = 0; height < (int)object.renderHeight(); height+=32)
				{
					if(offScreen(obj_x, obj_y, height, (int)MapLoader.getTileSize() * 2, (int)MapLoader.getTileSize()) == 0)
						list.add(new VisibleElement(object.getLowerDrawable(),
								obj_x, obj_y,
								height));
				}
				//And top it off with the highest one
				list.add(new VisibleElement(object.getDrawable(),
						obj_x, obj_y,
						object.renderHeight()));
			}
		}
	}
}
