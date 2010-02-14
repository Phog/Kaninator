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
public class NorthSlope extends StaticObject
{

	public NorthSlope(Drawable _tile, double _height)
	{
		super(_tile, _height);
	}
	
	/* (non-Javadoc)
	 * @see kaninator.mechanics.StaticObject#getHeight(double, double)
	 */
	@Override
	public double getHeight(double x, double y)
	{
		return height - TILE_HEIGHT +  y / 2.0;
	}

}
