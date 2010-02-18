/**
 * 
 */
package kaninator.graphics;

import java.awt.*;

/**
 * @author phedman
 *
 */
public class Shadow implements Drawable
{
	private Color color;
	private double radius;
	
	public Shadow(double _radius)
	{
		color = new Color(0.0f, 0.0f, 0.0f, 0.50f);
		radius = _radius;
	}

	public void draw(Graphics2D g, int x, int y)
	{
		g.setColor(color);
		g.fillOval(x, y, (int)radius, (int)(radius/2));
	}


	public int getHeight()
	{
		return (int)(radius/2);
	}

	public int getWidth()
	{
		return (int)radius;
	}

}
