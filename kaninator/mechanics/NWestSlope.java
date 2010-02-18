/**
 * The game engine package.
 * Implements all the generic services needed for a 2d game. 
 */
package kaninator.mechanics;

import kaninator.graphics.Drawable;

/**
 * @author phedman
 */
public class NWestSlope extends StaticObject
{
	
	public NWestSlope(Drawable _tile, Drawable _lower, double _height)
	{
		super(_tile, _lower, _height - TILE_HEIGHT);
	}
	
	/* (non-Javadoc)
	 * @see kaninator.mechanics.StaticObject#getHeight(double, double)
	 */
	@Override
	public double getHeight(double x, double y)
	{
		double tempHeight = (x + y)/2.0;
		
		if(tempHeight < TILE_HEIGHT)
			return height;
		
		return height - TILE_HEIGHT + tempHeight;
	}

}
