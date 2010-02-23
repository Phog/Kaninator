/**
 * The game engine package.
 * Implements all the generic services needed for a 2d game. 
 */
package kaninator.mechanics;

import kaninator.graphics.Drawable;
import kaninator.io.MapLoader;

/**
 * An abstract class representing all the static objects in the game (ie. tiles).
 * They contain two Drawable objects, one which represents the tile itself, and one which all the tiles in the column under it.
 * They cannot be moved and are positioned according to a tiling system with an added height attribute.
 * They can report the height they represent at any coordinate of their surface.
 * @author phedman
 * @see kaninator.mechanics.DynamicObject
 * @see kaninator.graphics.Drawable
 */
public abstract class StaticObject
{
	private static final int DEPTH_OFFSET_X = 101;
	private static final int DEPTH_OFFSET_Y = 100;
	protected double height;
	private Drawable tile, lower;
	private int x, y;
	
	
	/**
	 * Constructs a StaticObject and sets the tile position of the object and the Drawables it is associated with.
	 * @param _tile The Drawable the object actually represents.
	 * @param _lower The Drawbles to be piled under it in order to create columns.
	 * @param _height The height the StaticObject will reside at
	 * @param _x The x coordinate in tile coordinates (isometric, 1 unit per tile)
	 * @param _y The y coordinate in tile coordinates (isometric, 1 unit per tile)
	 */
	public StaticObject(Drawable _tile, Drawable _lower, double _height, int _x, int _y)
	{
		tile = _tile;
		lower = _lower;
		height = _height;
		x = _x;
		y = _y;
	}
	
	/**
	 * Returns the Drawable associated with the StaticObject
	 * @return The main Drawable
	 */
	public Drawable getDrawable()
	{
		return tile;
	}
	
	/**
	 * Returns the Drawable used to build up the column under the StaticObject
	 * @return The colum Drawable.
	 */
	public Drawable getLowerDrawable()
	{
		return lower;
	}
	
	/**
	 * Returns the height the StaticObject is positioned at in screen coordinates.
	 * @return The height on screen.
	 */
	public int renderHeight()
	{
		return (int)height;
	}
	
	
	/**
	 * Calculates the depth of the StaticObject so that the Camera can sort out the drawing order.
	 * @return The depth of the StaticObject.
	 * @see kaninator.mechanics.Camera
	 */
	public int getDepth()
	{
		return x * DEPTH_OFFSET_X + y * DEPTH_OFFSET_Y + (int)(height / MapLoader.getTileHeight());
	}
	
	/**
	 * Renders the internal coordinates to a 2-dimensional x coordinate to be able to draw it on the screen.
	 * @return The x-coordinate on the screen.
	 */
	public int render_x()
	{
		return (x - 1) * (int)MapLoader.getTileSize() - y * (int)MapLoader.getTileSize();
	}
	
	/**
	 * Renders the internal coordinates to a 2-dimensional y coordinate to be able to draw it on the screen.
	 * @return The y-coordinate on the screen.
	 */
	public int render_y()
	{
		return	y * (int)MapLoader.getTileHeight() + x * (int)MapLoader.getTileHeight();
	}
	
	/**
	 * Calculates the height at the position located at x, y (isometric coordinates) on the tile.
	 * @param x The x coordinate on the tile.
	 * @param y The y coordinate on the tile.
	 * @return The height at (x,y)
	 */
	public abstract double getHeight(double x, double y);
}
