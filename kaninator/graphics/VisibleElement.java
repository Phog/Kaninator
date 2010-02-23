/**
 * Wrapper package for the 2D library used for the graphics.
 */
package kaninator.graphics;

/**
 * An intermediate form of the visible objects
 * Wraps the Drawable objects together with the on screen coordinates
 * so they can be drawn by a Canvas object.
 * @see kaninator.graphics.Drawable
 * @see kaninator.graphics.Canvas
 * @author phedman
 */
public class VisibleElement
{
	private Drawable drawable;
	private int x, y, height;
	
	/**
	 * Wraps a drawable object with the onscreen coordinates x, y, and the height it is positioned at.
	 * @param _drawable The drawable in question.
	 * @param _x The x-coordinate for the element.
	 * @param _y The y-coordinate for the element.
	 * @param _height The height the element is positioned at.
	 */
	public VisibleElement(Drawable _drawable, int _x, int _y, int _height)
	{
		x = _x;
		y = _y;
		height = _height;
		drawable = _drawable;
	}
	
	/**
	 * Getter method.
	 * @return The wrapped Drawable object.
	 */
	public Drawable getDrawable()
	{
		return drawable;
	}
	
	/**
	 * Getter method.
	 * @return The x-coordinate for the element.
	 */
	public int get_x()
	{
		return x;
	}
	
	/**
	 * Getter method.
	 * @return The y-coordinate for the element.
	 */
	public int get_y()
	{
		return y;
	}
	
	/**
	 * Getter method.
	 * @return The height the element is positioned at.
	 */
	public int getHeight()
	{
		return height;
	}
}
