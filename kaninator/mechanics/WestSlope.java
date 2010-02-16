/**
 * The game engine package.
 * Implements all the generic services needed for a 2d game. 
 */
package kaninator.mechanics;

import kaninator.graphics.Drawable;

/**
 * @author phedman
 *
 */
public class WestSlope extends StaticObject
{
	public WestSlope(Drawable _tile, double _height)
	{
		super(_tile, _height);
	}
	
	/* (non-Javadoc)
	 * @see kaninator.mechanics.StaticObject#getHeight(double, double)
	 */
	@Override
	public double getHeight(double x, double y)
	{
		return height - TILE_HEIGHT + x / 2.0;
	}

}