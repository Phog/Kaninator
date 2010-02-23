/**
 * Wrapper package for the 2D library used for the graphics.
 */
package kaninator.graphics;

import java.awt.*;
import java.awt.font.*;

/**
 * A drawable text object.
 * Makes it possible to draw lines of text with the Drawable interface.
 * @see kaninator.graphics.Drawable
 * @author phedman
 */
public class Text implements Drawable
{
	private String text;
	private Font font;

	private TextLayout layout;
	private Color color;
	
	/**
	 * Creates the Drawable text.
	 * Initializes the font and creates the metrics for getting height and with.
	 * @param _text The text string to be drawn
	 * @param _font The name of the font
	 * @param _size The size of the font
	 * @param _style The style of the font using the flags from java.awt.Font
	 * @param _color The color of the text to be drawn
	 * @see kaninator.graphics.Drawable
	 * @see java.awt.Font
	 * @see java.awt.FontMetrics
	 */
	public Text(String _text, String _font, int _size, int _style, Color _color)
	{
		color = _color;
		text = _text;
		font = new Font(_font, _style, _size);
		
		layout = new TextLayout(text, font, new FontRenderContext(null, false, false));
	}
	
	/**
	 * Draws the text string to the coordinates in the parameters.
	 * @param g The graphics context the string will be drawn to.
	 * @param x The x coordinate for the string
	 * @param y The y coordinate for the string
	 */
	public void draw(Graphics2D g, int x, int y)
	{
		g.setFont(font);
		g.setColor(color);
		g.drawString(text, x, y + getHeight());
	}

	/**
	 * Getter for the height of the text string.
	 * @return The height of the string.
	 */
	public int getHeight()
	{
		return (int)layout.getBounds().getHeight();
	}
	
	/**
	 * Getter for the width of the text string.
	 * @return The width of the string.
	 */
	public int getWidth()
	{
		return (int)layout.getBounds().getWidth();
	}
	
	/**
	 * Setter for the text to be displayed.
	 * @param _text The text you want the Text object to draw.
	 */
	public void setText(String _text)
	{
		text = _text;
	}
	
	/**
	 * Dummy method needed to implement the Drawable interface.
	 */
	public void update()
	{	
	}
}
