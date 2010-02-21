/**
 * The game engine package.
 * Implements all the generic services needed for a 2d game. 
 */
package kaninator.mechanics;

import kaninator.io.MapLoader;
import kaninator.graphics.Drawable;
import kaninator.io.MapLoader;

/**
 * @author phedman
 *
 */
public class SWestSlope extends StaticObject
{

	public SWestSlope(Drawable _tile, Drawable _lower, double _height, int x, int y)
	{
		super(_tile, _lower, _height, x , y);
	}
	
	/* (non-Javadoc)
	 * @see kaninator.mechanics.StaticObject#getHeight(double, double)
	 */
	@Override
	public double getHeight(double x, double y)
	{
		double tempHeight = (x - y);
		
		if(tempHeight < 0)
			return height - MapLoader.getTileHeight();

		return height -MapLoader.getTileHeight() + tempHeight/2;
	}

}
