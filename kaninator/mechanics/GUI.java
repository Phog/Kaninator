/**
 * The game engine package.
 * Implements all the generic services needed for a 2d game. 
 */
package kaninator.mechanics;

import java.util.*;
import kaninator.graphics.*;

/**
 * Provides an overlay functionality.
 * Used for the in-game GUI and the menu. Consists of Drawable objects.
 * 
 * Divides the screen into 9 subsections like this:<br />
 * [0,0][1,0][2,0]<br />
 * [0,1][1,1][2,1]<br />
 * [0,2][1,2][2,2]<br />
 * 
 * Drawable objects added to a subsection will be positioned underneath the previous elements.
 * @see kaninator.graphics.Drawable
 * @author phedman
 */
public class GUI
{
	private Queue<Drawable> drawables[][];
	private int padding;
	
	/**
	 * Creates a two dimensional array of Queues in order to store the drawables for each screen section.
	 * @see kaninator.graphics.Drawable
	 * @see java.util.Queue
	 */
	public GUI()
	{
		padding = 0;
		drawables = new Queue[3][3];
		
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				drawables[i][j] = new LinkedList<Drawable>();
	}
	
	/**
	 * Sets the amount of padding between the gui and the target area.
	 * @param _padding The amount of padding in pixels.
	 */
	public void setPadding(int _padding)
	{
		if(_padding < 0)
			return;
		
		padding = _padding;
	}
	
	
	/**
	 * Gets the amount of padding between the gui and the target area.
	 */
	public int getPadding()
	{
		return padding;
	}
	
	/**
	 * Removes all the Drawable objects contained in the section.
	 * @param x The x coordinate for the section.
	 * @param y The y coordinate for the section.
	 * @see kaninator.graphics.Drawable
	 */
	public void clearSection(int x, int y)
	{
		drawables[x][y].clear();
	}
	
	/**
	 * Adds a Drawable object to the section.
	 * @param drawable The drawable object to be added.
	 * @param x The x coordinate for the section.
	 * @param y The y coordinate for the section.
	 */
	public void addToSection(Drawable drawable, int x, int y)
	{
		drawables[x][y].add(drawable);
	}
	
	/**
	 * Returns an array containing the drawables in the section.
	 * @param x The x coordinate for the section.
	 * @param y The y coordinate for the section.
	 * @return An array containing all the drawables in the current section.
	 */
	public Drawable[] getSection(int x, int y)
	{
		Drawable elements[] = new Drawable[drawables[x][y].size()];
		
		Iterator<Drawable> it = drawables[x][y].iterator();
		for(int i = 0; i < elements.length; i++)
			elements[i] =  it.next();
		
		
		return elements;
	}
}
