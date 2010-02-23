/**
 * Wrapper package for the 2D library used for the graphics.
 */
package kaninator.graphics;

import java.awt.Graphics2D;
/**
 * An interface for an element that can be drawn to a object which implements the Canvas interface.
 * @author phedman
 * @see kaninator.graphics.Canvas
 */
public interface Drawable
{
	/**
	 * Performs the drawing. If the element is an animation it will also advance the animation.
	 */
	public void draw(Graphics2D g, int x, int y);
	
	/**
	 * Getter for the width of the drawable
	 * @return The width of the drawable.
	 */
	public int getWidth();
	
	/**
	 * Getter for the height of the drawable
	 * @return The height of the drawable.
	 */
	public int getHeight();
	
	
	/**
	 * Updates the drawable with the new transparency information.
	 */
	public void update();
}
