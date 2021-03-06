/**
 * The game engine package.
 * Implements all the generic services needed for a 2d game. 
 */
package kaninator.mechanics;

import kaninator.io.MapFactory;
import kaninator.graphics.Drawable;

/**
 * A subclass of StaticObject representing a North-Eastward slope.
 * @author phedman
 * @see kaninator.mechanics.StaticObject
 */
public class NEastSlope extends StaticObject
{
	/**
	 * Constructs a NEastSlope and sets the tile position of the object and the Drawables it is associated with by passing the parameters to the parent constructor.
	 * @param _tile The Drawable the object actually represents.
	 * @param _lower The Drawbles to be piled under it in order to create columns.
	 * @param _height The height the StaticObject will reside at
	 * @param x The x coordinate in tile coordinates (isometric, 1 unit per tile)
	 * @param y The y coordinate in tile coordinates (isometric, 1 unit per tile)
	 */
	public NEastSlope(Drawable _tile, Drawable _lower, double _height, int x, int y)
	{
		super(_tile, _lower, _height, x , y);
	}
	
	/**
	 * Calculates the height at the position located at x, y (isometric coordinates) on the tile.
	 * @param x The x coordinate on the tile.
	 * @param y The y coordinate on the tile.
	 * @return The height at (x,y)
	 */
	public double getHeight(double x, double y)
	{
		double tempHeight = (x - y);
		
		if(tempHeight > 0 )
			return height - MapFactory.getTileHeight();
		
		return height - MapFactory.getTileHeight() - tempHeight/2.0;
	}

}
