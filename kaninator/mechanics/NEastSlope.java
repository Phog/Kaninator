/**
 * The game engine package.
 * Implements all the generic services needed for a 2d game. 
 */
package kaninator.mechanics;

import kaninator.graphics.Drawable;

/**
 * @author phedman
 */
public class NEastSlope extends StaticObject
{
	public NEastSlope(Drawable _tile, double _height)
	{
		super(_tile, _height);
	}
	
	/* (non-Javadoc)
	 * @see kaninator.mechanics.StaticObject#getHeight(double, double)
	 */
	@Override
	public double getHeight(double x, double y)
	{
		double tempHeight = (x - y);
		
		if(tempHeight > 0 )
			return height - TILE_HEIGHT;
		
		return height - TILE_HEIGHT - tempHeight/2.0;
	}

}
