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
	private ArrayList<ArrayList<Queue<Drawable>>> drawables;
	private int padding;
	private int width, height;
	
	/**
	 * Creates a two dimensional array of Queues in order to store the drawables for each screen section.
	 * @see kaninator.graphics.Drawable
	 * @see java.util.Queue
	 */
	public GUI(int _width, int _height)
	{
		padding = 0;
		drawables = new ArrayList<ArrayList<Queue<Drawable>>>();
		
		width = _width;
		height = _height;
		
		for(int i = 0; i < 3; i++)
		{
			drawables.add(new ArrayList<Queue<Drawable>>());
			for(int j = 0; j < 3; j++)
				drawables.get(i).add(new LinkedList<Drawable>());
		}
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
	 * Gets the number of elements in the GUI
	 */
	public int size()
	{
		int size = 0;
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				size += drawables.get(i).get(j).size();
		
		return size;
	}
	
	/**
	 * Removes all the Drawable objects contained in the section.
	 * @param x The x coordinate for the section.
	 * @param y The y coordinate for the section.
	 * @see kaninator.graphics.Drawable
	 */
	public void clearSection(int x, int y)
	{
		drawables.get(x).get(y).clear();
	}
	
	/**
	 * Adds a Drawable object to the section.
	 * @param drawable The drawable object to be added.
	 * @param x The x coordinate for the section.
	 * @param y The y coordinate for the section.
	 */
	public void addToSection(Drawable drawable, int x, int y)
	{
		drawables.get(x).get(y).add(drawable);
	}
	
	/**
	 * Checks if the coordinates provided are inside of any element in the screen section.
	 * @param i Screen section x coordinate
	 * @param j Screen section y coordinate
	 * @param x Pointer x coordinate
	 * @param y Pointer y coordinate
	 */
	public int touchesElement(int i, int j, int x, int y)
	{	
		int num = 0;
		int offset = 0;
		
		for(Drawable drawable : drawables.get(i).get(j))
		{	
			int draw_x = getRealX(i, drawable);
			int draw_y = getRealY(j, offset, drawable);

			if(x > draw_x && x < draw_x + drawable.getWidth()
					&& y > draw_y && y < draw_y + drawable.getHeight())
				return num;

			offset += drawable.getHeight() + 1;
			num++;
		}
		
		return -1;
	}
	
	/**
	 * 	Parses the GUI and returns an array of VisibleElements that can be passed to the screen.
	 *  Positions the elements belonging to the same subsection underneath each other.
	 */
	public ArrayList<VisibleElement> render()
	{
		ArrayList<VisibleElement> elements = new ArrayList<VisibleElement>();
		
		//Loop through each subsection of the gui
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
			{		
				//Loops through each element in the subsection
				//and positions them underneath each other
				int offset = 0;
				for(Drawable drawable : drawables.get(i).get(j))
				{	
					//Estimates a screen position for the elements
					int draw_x = getRealX(i, drawable);
					int draw_y = getRealY(j , offset, drawable);
					
					elements.add(new VisibleElement(drawable, draw_x, draw_y, 0));
					offset += drawable.getHeight() + 1;
				}
			}
		return elements;
	}
	
	/**
	 * Calculates the on-screen x coordinate for the element
	 * @param i The x coordinate of the screen section
	 * @param drawable The element in question
	 * @return The on-screen x coordinate for the element
	 */
	private int getRealX(int i, Drawable drawable)	
	{
		int draw_x = i * width/2;
		switch(i)
		{
			case 0:
				draw_x += padding;
				break;
			case 1:
				draw_x -= drawable.getWidth()/2;
				break;
			case 2:
				draw_x -= drawable.getWidth() + padding;
		}
		
		return draw_x;
	}
	
	/**
	 * Calculates the on-screen y coordinate for the element
	 * @param j The y coordinate of the screen section
	 * @param offset The y offset into the screen section.
	 * @param drawable The element in question
	 * @return
	 */
	private int getRealY(int j, int offset, Drawable drawable)
	{
		int draw_y = j * height/2;
		switch(j)
		{
			case 0:
					draw_y += offset + padding;
				break;
			case 1:
					draw_y = draw_y + offset - drawable.getHeight();
				break;
			case 2:
					draw_y = draw_y - offset - drawable.getHeight() - padding;
		}
		
		return draw_y;
	}
}
