/**
 * The game engine package.
 * Implements all the generic services needed for a 2d game. 
 */
package kaninator.mechanics;

import java.util.ArrayList;

import kaninator.graphics.Drawable;

/**
 * Implements menu functionality for the GameState objects.
 * Stores Drawable objects for both the active and passive states of the menu options.
 * Takes both a pointer input and key input. (setPosition respectively moveUp/moveDown)
 * @author phedman
 * @see kaninator.game.GameState
 */
public class Menu
{
	private GUI gui;

	private ArrayList<Drawable> passiveEntries;
	private ArrayList<Drawable> activeEntries;
	private Drawable title;
	
	private int position;
	
	
	/**
	 * Initializes the containers.
	 * @param _gui The gui object the menu will be drawn to
	 */
	public Menu(GUI _gui)
	{
		gui = _gui;
		
		passiveEntries = new ArrayList<Drawable>();
		activeEntries = new ArrayList<Drawable>();
		title = null;
	}
	
	
	/**
	 * Sets the title of the menu.
	 * @param element The element that will be used as the title.
	 */
	public void setTitle(Drawable element)
	{
		title = element;
	}
	
	
	/**
	 * Adds a new entry to the menu section. Takes an image for the passive state of the 
	 * element and one for the active(selected) state of the element.
	 * @param passive The Drawable object to be displayed when the element is inactive.
	 * @param active The Drawable object to be displayed when the element is active(selected).
	 */
	public void addEntry(Drawable passive, Drawable active)
	{
		passiveEntries.add(passive);
		activeEntries.add(active);
	}
	
	/**
	 * Clears the screensections in the gui which contain the menu.
	 */
	public void clear()
	{
		gui.clearSection(1, 0);
		gui.clearSection(1, 1);
	}

	/**
	 * Clears the menu entries completely leaving the menu blank.
	 */
	public void clearEntries()
	{
		passiveEntries.clear();
		activeEntries.clear();
		
	}
	
	/**
	 * Renders the menu to the gui object.
	 */
	public void render()
	{
		clear();
		
		gui.addToSection(title, 1, 0);
		
		for(int i = 0; i < passiveEntries.size(); i++)
		{
			if(i == position)
				gui.addToSection(activeEntries.get(i), 1, 1);
			else
				gui.addToSection(passiveEntries.get(i), 1, 1);
		}
	}
	
	/**
	 * Moves the selected menu entry up one step. Returns to the bottom if overflow happens.
	 */
	public void moveUp()
	{
		position--;
		
		if(position < 0)
			position += passiveEntries.size();
	}
	
	/**
	 * Moves the selected menu entry down one step. Returns to the top if overflow happens.
	 */
	public void moveDown()
	{
		position++;
		
		if(position >= passiveEntries.size())
			position -= passiveEntries.size();
	}
	
	
	/**
	 * If the x and y coordinates point to a menu element, then set the menu position to that element,
	 * otherwise do nothing.
	 * @param x The x coordinate of the pointer.
	 * @param y The y coordinate of the pointer.
	 */
	public void setPosition(int x, int y)
	{
		int pos = gui.touchesElement(1, 1, x, y);
		if(pos != -1)
			position = pos;
	}
	
	/**
	 *  Returns the current menu position.
	 * @return The menu position.
	 */
	public int select()
	{
		return position;
	}
	
	
}
