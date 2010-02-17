/**
 * The game engine package.
 * Implements all the generic services needed for a 2d game. 
 */
package kaninator.mechanics;
import kaninator.graphics.Drawable;

/**
 * @author phedman
 */
public abstract class StaticObject
{
	protected static final double TILE_SIZE = 64.0;
	protected static final double TILE_HEIGHT = 32.0;
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
	
	public int render_x(int x, int y)
	{
		return x * (int)TILE_SIZE - y * (int)TILE_SIZE;
	}
	
	public int render_y(int x, int y)
	{
		return	y * (int)TILE_HEIGHT + x * (int)TILE_HEIGHT;
	}
	
	public abstract double getHeight(double x, double y);
}
