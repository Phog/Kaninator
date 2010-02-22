/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import java.util.ArrayList;
import java.util.LinkedList;

import kaninator.sound.SoundClip;
import kaninator.graphics.Animation;
import kaninator.mechanics.DynamicObject;

/**
 * @author phedman
 */
public class Zombie
{
	private static final int MAX_ACTIVE_DISTANCE = 333;
	private static final int MIN_DISTANCE_BETWEEN = 48;
	private static final int MAX_STRAFE_DIFFERENCE = 8;
	private static final double ZOMBIE_SPEED = 5.0;
	
	private Map map;
	private Model model;
	private DynamicObject player;
	private SoundClip squirt;
	private double distance;
	private boolean dead;
	
	public Zombie(ArrayList<Animation> animations, Map _map, SoundClip _squirt, DynamicObject _player, double x, double y, double radius_constant)
	{
		if(animations == null || animations.size() == 0)
			return;
		
		map = _map;
		model = new Model(animations, map, x, y, radius_constant, ZOMBIE_SPEED);
		squirt = _squirt;
		
		player = _player;
		distance = Double.MAX_VALUE;
		dead = false;
	}
	
	private double distanceTo(DynamicObject other)
	{
		double distTo = Math.pow((other.get_x() - model.getModel().get_x()),2) + Math.pow((other.get_y() - model.getModel().get_y()),2);
		return Math.sqrt(distTo);
	}
	
	private void follow(DynamicObject target)
	{
		double delta_x = target.get_x() - model.getModel().get_x();
		double delta_y = target.get_y() - model.getModel().get_y();
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
	
	public void kill()
	{
		dead = true;
		squirt.playClip();
	}
	
	public boolean act(LinkedList<Zombie> others)
	{
		if(dead)
			return true;
		
		Zombie leader = null;
		for(Zombie otherone : others)
		{
			if(distanceTo(otherone.getMainObject()) < MIN_DISTANCE_BETWEEN)
				if(distance > otherone.distance)
					leader = otherone;
		}
		
		if(distance < MAX_ACTIVE_DISTANCE)
		{
			if(leader != null)
			{
				model.setVelX(leader.model.getVelX());
				model.setVelY(leader.model.getVelY());
				model.setState(leader.model.getModel().getState());
				model.advanceAnimation();
			}
			else
			{
				follow(player);
				model.advanceAnimation();
			}
		}
		else
		{
			model.move_x(0);
			model.move_y(0);
			model.reset();
		}
		
		model.update();
		return false;
	}
	
	public DynamicObject getMainObject()
	{
		return model.getModel();
	}
	
	public LinkedList<DynamicObject> getDynamicObjects()
	{
		return model.getDynamicObjects();
	}
}
