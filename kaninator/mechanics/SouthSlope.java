/**
 * The game engine package.
 * Implements all the generic services needed for a 2d game. 
 */

package kaninator.mechanics;

import kaninator.graphics.Drawable;

/**
 * @author phedman

 */
public class SouthSlope extends StaticObject
{

	public SouthSlope(Drawable _tile, Drawable _lower, double _height)
	{
		super(_tile, _lower, _height);
	}
	
	/* (non-Javadoc)
	 * @see kaninator.mechanics.StaticObject#getHeight(double, double)
	 */
	@Override
	public double getHeight(double x, double y)
	{
		 return height - y / 2.0;
		
	}

}
