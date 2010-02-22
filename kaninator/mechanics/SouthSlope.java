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

	public SouthSlope(Drawable _tile, Drawable _lower, double _height, int x, int y)
	{
		super(_tile, _lower, _height, x , y);
	}
	
	public double getHeight(double x, double y)
	{
		 return height - y / 2.0;
		
	}

}
