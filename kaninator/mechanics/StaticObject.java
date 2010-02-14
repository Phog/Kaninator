/**
 * The game engine package.
 * Implements all the generic services needed for a 2d game. 
 */
package kaninator.mechanics;
import kaninator.graphics.Drawable;

/**
 * @author phedman
 */
public abstract class StaticObject
{
	private Drawable tile;

	public StaticObject(Drawable _tile)
	{
		tile = _tile;
	}
	
	public abstract double getHeight(double x, double y);
}
