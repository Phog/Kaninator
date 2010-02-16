/**
 * The game engine package.
 * Implements all the generic services needed for a 2d game. 
 */
package kaninator.mechanics;

import java.util.ArrayList;

import kaninator.graphics.*;

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
	
	private static final double FOLLOW_SPEED = 6.0;
	private static final double FOLLOW_BORDER_SIZE = 1.0/5.0;
	
	private int x, y;
	private ArrayList<DynamicObject> objects;
	private ArrayList<DynamicObject> player;
	private ArrayList<ArrayList<StaticObject>> tiles;
	
	/**
	 * @param _canvas The canvas which actually draws the rendered data.
	 * @param _gui The overlay GUI which will always be on top.
	 */
	public Camera(Canvas _canvas, GUI _gui)
	{
		canvas = _canvas;
		gui = _gui;
		
		x = y = 0;
		
		objects = new ArrayList<DynamicObject>();
		player = null;
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
	
	public void setPlayer(ArrayList<DynamicObject> _player)
	{
		player = _player;
	}
	
	public void addObject(DynamicObject object)
	{
		objects.add(object);
	}
	
	public void clearObjects()
	{
		objects.clear();
		player = null;
	}
	
	
	public void setTiles(ArrayList<ArrayList<StaticObject>> _tiles)
	{
		tiles = _tiles;
	}
	
	public void follow(DynamicObject obj)
	{
		double d_x = obj.render_x() - x;
		double d_y = obj.render_y() - y;

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
		
		int i = 0;
		for(ArrayList<StaticObject> rowList : tiles)
		{
			int j = 0;
			for(StaticObject object : rowList)
			{
				canvas.addElement(new VisibleElement(object.getDrawable(), object.render_x(j, i) - x, object.render_y(j, i) - y));
				j++;
			}
			i++;
		}
		
		for(DynamicObject object : objects)
			canvas.addElement(new VisibleElement(object.getDrawable(), object.render_x() - x, object.render_y() - y));
		
		if(player != null)
			for(DynamicObject object : player)
				canvas.addElement(new VisibleElement(object.getDrawable(), object.render_x() - x, object.render_y() - y));
		
		//TODO: main drawing thing loop... yeah
	}
}
