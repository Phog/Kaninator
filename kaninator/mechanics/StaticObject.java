/**
 * The game engine package.
 * Implements all the generic services needed for a 2d game. 
 */
package kaninator.mechanics;

import kaninator.graphics.Drawable;
import kaninator.io.MapLoader;

/**
 * @author phedman
 */
public abstract class StaticObject
{
	private static final int DEPTH_OFFSET_X = 101;
	private static final int DEPTH_OFFSET_Y = 100;
	protected double height;
	private Drawable tile, lower;

	public StaticObject(Drawable _tile, Drawable _lower, double _height)
	{
		tile = _tile;
		lower = _lower;
		height = _height;
	}
	
	public Drawable getDrawable()
	{
		return tile;
	}
	
	public Drawable getLowerDrawable()
	{
		return lower;
	}
	
	public int renderHeight()
	{
		return (int)height;
	}
	
	public int getDepth(int x, int y)
	{
		return x * DEPTH_OFFSET_X + y * DEPTH_OFFSET_Y + (int)(height / MapLoader.getTileHeight());
	}
	
	public int render_x(int x, int y)
	{
		return (x - 1) * (int)MapLoader.getTileSize() - y * (int)MapLoader.getTileSize();
	}
	
	public int render_y(int x, int y)
	{
		return	y * (int)MapLoader.getTileHeight() + x * (int)MapLoader.getTileHeight();
	}
	
	public abstract double getHeight(double x, double y);
}
