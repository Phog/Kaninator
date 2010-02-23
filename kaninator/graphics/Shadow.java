/**
 * 
 */
package kaninator.graphics;

import java.awt.*;

/**
 * An object implementing the Drawable interface that basically draws a shadow.
 * In essence it draws a black ellipse with the alpha value of 0.5.
 * @author phedman
 * @see kaninator.graphics.Drawable
 */
public class Shadow implements Drawable
{
	private Color color;
	private double width;
	
	/**
	 * Constructs a Shadow from the given radius.
	 * @param _width The width of the ellipse.
	 */
	public Shadow(double _width)
	{
		color = new Color(0.0f, 0.0f, 0.0f, 0.50f);
		width = _width;
	}

	/**
	 * Draws the Shadow to the coordinates in the parameters.
	 * @param g The graphics context the image will be drawn to.
	 * @param x The x coordinate for the image
	 * @param y The y coordinate for the image
	 */
	public void draw(Graphics2D g, int x, int y)
	{
		g.setColor(color);
		g.fillOval(x, y, (int)width, (int)(width/2));
	}

	/**
	 * Getter for the height of the Shadow.
	 * @return The height of the Shadow.
	 */
	public int getHeight()
	{
		return (int)(width/2);
	}
	
	/**
	 * Getter for the width of the Shadow.
	 * @return The height of the Shadow.
	 */
	public int getWidth()
	{
		return (int)width;
	}
	
	/**
	 * Dummy method needed to implement the Drawable interface.
	 */
	public void update()
	{
	}

}
