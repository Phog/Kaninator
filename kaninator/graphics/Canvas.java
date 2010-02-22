/**
 * Wrapper package for the 2D library used for the graphics.
 */
package kaninator.graphics;

import java.awt.Dimension;

/**
 * An interface for a class that initializes a window and can draw to it
 * @author phedman
 */
public interface Canvas
{

	/**
	 * Clears the drawing queue.
	 */
	public void clear();
	
	/**
	 * Clears the top of the drawing queue.
	 * @param n The number of elements to clear.
	 */
	public void clearTop(int n);
	
	/**
	 * Adds a new object that implements the VisibleElement interface to the drawing queue.
	 * @param element The new VisibleElement object to be added to the tail of the queue.
	 * @see kaninator.graphics.VisibleElement
	 */
	public void addElement(VisibleElement element);
	
	/**
	 * Draws the drawing queue in order to the screen.
	 */
	public void draw();
	
	/**
	 * Getter for the width of the drawable area.
	 * @return The width of the drawable area.
	 */
	public int getWidth();
	
	/**
	 * Getter for the height of the drawable area.
	 * @return The height of the drawable area..
	 */
	public int getHeight();
	
	/**
	 * Getter for the window width
	 * @return The width of the window.
	 */
	public int getResWidth();

	/** 
	 * Getter for the window height
	 * @return The height of the window.
	 */
	public int getResHeight();
	
	/**
	 * Getter for the window insets
	 * @return The top/left border size.
	 */
	public Dimension getInsets();
	
	/**
	 * Sets the size of the window to the size contained in the dimension.
	 * @param size The Dimension object containing the size of the screen.
	 * @see java.awt.Dimension
	 */
	public void setSize(Dimension size);
	
	/**
	 * Hides alternatively shows the mouse cursor when above the window.
	 * @param hide True to hide, false to show the cursor.
	 */
	public void hideCursor(boolean hide);
}
