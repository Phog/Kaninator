/**
 * The game engine package.
 * Implements all the generic services needed for a 2d game. 
 */
package kaninator.mechanics;

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
	
	/**
	 * @param _canvas The canvas which actually draws the rendered data.
	 * @param _gui The overlay GUI which will always be on top.
	 */
	public Camera(Canvas _canvas, GUI _gui)
	{
		canvas = _canvas;
		gui = _gui;
	}
	
	/**
	 * Parses the GUI and sends the elements to the canvas.
	 * Positions the elements belonging to the same subsection underneath each other.
	 * @see kaninator.mechanics.GUI
	 * @see kaninator.graphics.VisibleElement
	 */
	public void renderGUI()
	{
		//Loop through each subsection of the gui
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
			{
				Drawable drawables[] = gui.getSection(i, j);

				//Estimates a screen position for the elements
				int x = i * canvas.getWidth()/2;
				int y = j * canvas.getHeight()/2;

				
				//Loops through each element in the subsection
				//and positions them underneath each other
				int offset = 0;
				for(Drawable drawable : drawables)
				{	
					int draw_x = x;
					int draw_y = y;
					switch(i)
					{
						case 0:
							draw_x += gui.getPadding();
							break;
						case 1:
							draw_x -= drawable.getWidth()/2;
							break;
						case 2:
							draw_x -= drawable.getWidth() + gui.getPadding();
					}
					
					switch(j)
					{
						case 0:
								draw_y += offset + gui.getPadding();
							break;
						case 1:
								draw_y = y + offset - drawable.getHeight();
							break;
						case 2:
								draw_y = y - offset - drawable.getHeight() - gui.getPadding();
					}
					
					canvas.addElement(new VisibleElement(drawable, draw_x, draw_y));
					offset += drawable.getHeight() + 1;
				}
			}
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
		
		//TODO: main drawing thing loop... yeah
		
		renderGUI();
		canvas.draw();
	}
}
