/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import java.util.Iterator;
import java.util.LinkedList;

import kaninator.io.MapLoader;
import kaninator.mechanics.DynamicObject;

/**
 * @author phedman
 */
public class Bullet
{
	private static final int BULLET_RESOLUTION = 6;
	private Map map;
	private DynamicObject model;
	private double vel_x, vel_y, vel_height;
	private boolean done;
	
	public Bullet(DynamicObject _model, Map _map, DynamicObject wielder, double _vel_x, double _vel_y,  double _vel_height)
	{
		model = _model;
		map = _map;
		
		model.setPos(wielder.get_x(), wielder.get_y());
		model.setHeight(wielder.getHeight());
		
		vel_x = _vel_x;
		vel_y = _vel_y;
		vel_height = _vel_height;
		
		done = false;
	}
	
	public void observe(LinkedList<NonPlayerObject> targets)
	{	
		if(model.getHeight() < map.getHeight(model) - MapLoader.getTileHeight()/2.0)
		{
			done = true;
			return;
		}
		
		for(int i = 0; i < BULLET_RESOLUTION; i++)
		{
			model.setPos(model.get_x() + vel_x / BULLET_RESOLUTION, model.get_y() + vel_y / BULLET_RESOLUTION);
			model.setHeight(model.getHeight() + vel_height / BULLET_RESOLUTION);	
			
			for(Iterator<NonPlayerObject> it = targets.iterator(); it.hasNext();)
			{
				NonPlayerObject target = it.next();
				DynamicObject targetObj = target.getMainObject();
				if(model.collide(targetObj))
				{
					done = true;
					target.kill();
					return;
				}
			}
		}
	}
	
	public boolean update()
	{
		if(done)
			return true;	
	
		return false;
	}
	
	public DynamicObject getDynamicObject()
	{
		return model;
	}
}
