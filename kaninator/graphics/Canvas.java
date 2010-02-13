/**
 * Wrapper package for the 2D library used for the graphics.
 */
package kaninator.graphics;

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
	 * Adds a new object that implements the VisibleElement interface to the drawing queue.
	 * @param element The new VisibleElement object to be added to the tail of the queue.
	 * @see kaninator.graphics.VisibleElement
	 */
	public void addElement(VisibleElement element);
	
	/**
	 * Draws the drawing queue in order to the scren.
	 */
	public void draw();
	
	/**
	 * Getter for the window width
	 * @return The width of the screen.
	 */
	public int getWidth();
	
	/**
	 * Getter for the window height
	 * @return The height of the screen.
	 */
	public int getHeight();
}