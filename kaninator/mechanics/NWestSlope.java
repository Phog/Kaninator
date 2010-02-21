/**
 * The game engine package.
 * Implements all the generic services needed for a 2d game. 
 */
package kaninator.mechanics;

import kaninator.io.MapLoader;
import kaninator.graphics.Drawable;

/**
 * @author phedman
 */
public class NWestSlope extends StaticObject
{
	
	public NWestSlope(Drawable _tile, Drawable _lower, double _height, int x, int y)
	{
		super(_tile, _lower, _height - MapLoader.getTileHeight(), x, y);
	}
	
	/* (non-Javadoc)
	 * @see kaninator.mechanics.StaticObject#getHeight(double, double)
	 */
	@Override
	public double getHeight(double x, double y)
	{
		double tempHeight = (x + y)/2.0;
		
		if(tempHeight < MapLoader.getTileHeight())
			return height;
		
		return height - MapLoader.getTileHeight() + tempHeight;
	}

}
