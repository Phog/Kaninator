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
	private int x, y;
	private ArrayList<DynamicObject> objects;
	
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
	
	public void addObject(DynamicObject object)
	{
		objects.add(object);
	}
	
	public void clearObjects()
	{
		objects.clear();
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
		
		for(DynamicObject object : objects)
			canvas.addElement(new VisibleElement(object.getDrawable(), object.render_x() - x, object.render_y() - y));
		
		
		//TODO: main drawing thing loop... yeah
	}
}
