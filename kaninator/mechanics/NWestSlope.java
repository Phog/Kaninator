/**
 * The game engine package.
 * Implements all the generic services needed for a 2d game. 
 */
package kaninator.mechanics;

import kaninator.io.MapLoader;
import kaninator.graphics.Drawable;

/**
 * A subclass of StaticObject representing a North-Westward slope.
 * @author phedman
 * @see kaninator.mechanics.StaticObject
 */
public class NWestSlope extends StaticObject
{
	/**
	 * Constructs a NWestSlope and sets the tile position of the object and the Drawables it is associated with by passing the parameters to the parent constructor.
	 * @param _tile The Drawable the object actually represents.
	 * @param _lower The Drawbles to be piled under it in order to create columns.
	 * @param _height The height the StaticObject will reside at
	 * @param x The x coordinate in tile coordinates (isometric, 1 unit per tile)
	 * @param y The y coordinate in tile coordinates (isometric, 1 unit per tile)
	 */
	public NWestSlope(Drawable _tile, Drawable _lower, double _height, int x, int y)
	{
		super(_tile, _lower, _height - MapLoader.getTileHeight(), x, y);
	}
	
	/**
	 * Calculates the height at the position located at x, y (isometric coordinates) on the tile.
	 * @param x The x coordinate on the tile.
	 * @param y The y coordinate on the tile.
	 * @return The height at (x,y)
	 */
	public double getHeight(double x, double y)
	{
		double tempHeight = (x + y)/2.0;
		
		if(tempHeight < MapLoader.getTileHeight())
			return height;
		
		return height - MapLoader.getTileHeight() + tempHeight;
	}

}
