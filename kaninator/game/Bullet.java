/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import java.util.Iterator;
import java.util.LinkedList;

import kaninator.io.MapFactory;
import kaninator.mechanics.DynamicObject;

/**
 * Used to represent the projectiles in the game. They contain DynamicObjects 
 * and maintain their original velocities until they collide with something 
 * (StaticObject or DynamicObject). They should only be constructed by the Gun class.
 * @author phedman
 * @see kaninator.mechanics.DynamicObject
 * @see kaninator.game.Gun
 */
public class Bullet
{
	private static final int BULLET_RESOLUTION = 6;
	private Map map;
	private DynamicObject model, shadow;
	private double vel_x, vel_y, vel_height;
	private boolean done;
	
	/**
	 * Constructs a Bullet object, initializes the velocities and the DynamicObjects associated with it.
	 * For internal use only, should only be called from the Gun class.
	 * @param _model The DynamicObject used to represent the projectile itself.
	 * @param _shadow The DynamicObject used to represent the shadow of the projectile.
	 * @param _map The map that will be used for height checking to see if the projectile collides with StaticObjects.
	 * @param wielder The DynamicObject that wields the gun, used to initialize the projectiles coordinates.
	 * @param _vel_x The velocity on the internal, isometric, x-axis.
	 * @param _vel_y The velocity on the internal, isometric, y-axis.
	 * @param _vel_height The vertical velocity.
	 */
	protected Bullet(DynamicObject _model, DynamicObject _shadow, Map _map, DynamicObject wielder, double _vel_x, double _vel_y,  double _vel_height)
	{
		double offset_x = _vel_x * (Math.random() - 0.5);
		double offset_y = _vel_y * (Math.random() - 0.5);
		
		model = _model;
		map = _map;
		
		shadow = _shadow;
		shadow.setPos(wielder.get_x() + offset_x, wielder.get_y() + offset_y);
		shadow.setHeight(wielder.getHeight());
		model.setPos(wielder.get_x() + offset_x, wielder.get_y() + offset_y);
		model.setHeight(wielder.getHeight());
		
		vel_x = _vel_x;
		vel_y = _vel_y;
		vel_height = _vel_height;
		
		done = false;
	}
	
	/**
	 * Observes if the projectile has hit a StaticObject, 
	 * otherwise it advances the projectiles position iteratively to check
	 * if it collides with a DynamicObject. If the projectile hits one of the Zombies it will call the kill method on it.
	 * @param targets The List of Zombies targeted by the projectiles which we will perform collision detection against.
	 * @see kaninator.game.Zombie
	 */
	public void observe(LinkedList<Zombie> targets)
	{	
		for(int i = 0; i < BULLET_RESOLUTION; i++)
		{
			if(model.getHeight() < map.getHeight(model) - MapFactory.getTileHeight()/2.0)
			{
				done = true;
				return;
			}
			
			model.setPos(model.get_x() + vel_x / BULLET_RESOLUTION, model.get_y() + vel_y / BULLET_RESOLUTION);
			model.setHeight(model.getHeight() + vel_height / BULLET_RESOLUTION);	
			
			for(Iterator<Zombie> it = targets.iterator(); it.hasNext();)
			{
				Zombie target = it.next();
				DynamicObject targetObj = target.getMainObject();
				if(model.collide(targetObj))
				{
					done = true;
					target.kill();
					return;
				}
			}
		}
		shadow.setPos(model.get_x(), model.get_y());
		shadow.setHeight(map.getHeight(shadow));
	}
	
	
	/**
	 * Used to check if the projectile still is active after the observe action. 
	 * In essence has it hit an object and needs to be destroyed.
	 * @return true if the projectile has hit a target and needs to be destroyed, otherwise it returns false.
	 */
	public boolean update()
	{
		if(done)
			return true;	
	
		return false;
	}
	
	/**
	 * Gets the main DynamicObject, representing the projectile itself.
	 * @return The DynamicObject for the projectile.
	 */
	public DynamicObject getMainObject()
	{
		return model;
	}
	
	/**
	 * Gets the main DynamicObject for the shadow associated with the projectile.
	 * @return The DynamicObject for the shadow.
	 */
	public DynamicObject getShadow()
	{
		return shadow;
	}
}
