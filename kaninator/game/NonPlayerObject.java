/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import java.util.ArrayList;

import kaninator.graphics.Animation;
import kaninator.graphics.Drawable;
import kaninator.graphics.Shadow;
import kaninator.mechanics.DynamicObject;

/**
 * @author phedman
 */
public class NonPlayerObject
{
	private static final int MAX_ACTIVE_DISTANCE = 196;
	private static final int MIN_DISTANCE_BETWEEN = 48;
	private static final int MAX_STRAFE_DIFFERENCE = 8;
	
	private Map map;
	private DynamicObject model, shadow;
	private DynamicObject player;
	private double distance;
	
	public NonPlayerObject(ArrayList<Animation> animations, Map _map, DynamicObject _player, double x, double y, double radius_constant)
	{
		if(animations == null || animations.size() == 0)
			return;
		
		map = _map;
		
		double radius = animations.get(0).getWidth()/radius_constant;
		model = new DynamicObject(animations, radius, map);
		model.setPos(x, y);
		
		ArrayList<Drawable> shadowList = new ArrayList<Drawable>();
		shadowList.add(new Shadow(radius * 2));
		Animation shadowAnim = new Animation(shadowList, 0.0);
		ArrayList<Animation> shadowAnimList = new ArrayList<Animation>();
		shadowAnimList.add(shadowAnim);
		
		shadow = new DynamicObject(shadowAnimList, radius, map);
		shadow.setPos(x, y);
		
		player = _player;
		distance = Double.MAX_VALUE;
	}
	
	private double distanceTo(DynamicObject other)
	{
		double distTo = Math.pow((other.get_x() - model.get_x()),2) + Math.pow((other.get_y() - model.get_y()),2);
		return Math.sqrt(distTo);
	}
	
	private void follow(DynamicObject target)
	{
		double delta_x = target.get_x() - model.get_x();
		double delta_y = target.get_y() - model.get_y();
		if(Math.abs(Math.abs(delta_x) - Math.abs(delta_y)) < MAX_STRAFE_DIFFERENCE)
		{
			if(delta_x > 0 && delta_y > 0)
			{
				model.move_y(1);
				model.move_x(1);
				model.setState(0);
			}
			else if(delta_x < 0 && delta_y > 0)
			{
				model.move_y(1);
				model.move_x(-1);
				model.setState(5);
			}
			else if(delta_x > 0 && delta_y < 0)
			{
				model.move_y(-1);
				model.move_x(1);
				model.setState(2);
			}
			else
			{
				model.move_y(-1);	
				model.move_x(-1);
				model.setState(1);
			}
		}
		else if(Math.abs(delta_x) > Math.abs(delta_y))
		{
			if(delta_x > 0)
			{
				model.move_x(1);
				model.move_y(0);
				model.setState(4);
			}
			else
			{
				model.move_x(-1);
				model.move_y(0);
				model.setState(6);
			}
		}
		else
		{
			if(delta_y > 0)
			{
				model.move_y(1);
				model.move_x(0);
				model.setState(7);
			}
			else
			{
				model.move_y(-1);
				model.move_x(0);
				model.setState(3);
			}
		}
	}
	
	public void observe()
	{
		distance = distanceTo(player);
	}
	
	public void act(ArrayList<NonPlayerObject> others)
	{
		NonPlayerObject leader = null;
		for(NonPlayerObject otherone : others)
		{
			if(distanceTo(otherone.getDynamicObjects().get(1)) < MIN_DISTANCE_BETWEEN)
				if(distance > otherone.distance)
					leader = otherone;
		}
		
		if(distance < MAX_ACTIVE_DISTANCE)
		{
			if(leader != null)
			{
				model.setVel_x(leader.getDynamicObjects().get(1).getVel_x());
				model.setVel_y(leader.getDynamicObjects().get(1).getVel_y());
				model.setState(leader.getDynamicObjects().get(1).getState());
				model.getAnimation().advance();
			}
			else
			{
				follow(player);
				model.getAnimation().advance();
			}
		}
		else
		{
			model.move_x(0);
			model.move_y(0);
			model.reset();
		}
		
		model.update();
		
		shadow.setHeight(map.getHeight(model));
		shadow.setPos(model.get_x(), model.get_y());
	}
	
	public ArrayList<DynamicObject> getDynamicObjects()
	{
		ArrayList<DynamicObject> objects = new ArrayList<DynamicObject>();
		objects.add(shadow);
		objects.add(model);
		return objects;
	}
}
