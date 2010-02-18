/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import java.util.ArrayList;
import kaninator.graphics.*;
import kaninator.mechanics.DynamicObject;

/**
 * @author phedman
 */
public class Player
{
	private static final double NORMAL_SPEED = 4.0;
	private static final double DIAGONAL_SPEED = Math.sqrt(NORMAL_SPEED*NORMAL_SPEED/2.0);
	private static final double JUMP_SPEED = 13;
	private static final double GRAVITY = 1.5;
	
	private Map map;
	private DynamicObject playerModel, shadow;
	private double vel_x, vel_y, vel_height;
	
	public Player(ArrayList<Animation> animations, Map _map, double x, double y, double radius_constant)
	{
		if(animations == null || animations.size() == 0)
			return;
		
		double radius = animations.get(0).getWidth()/radius_constant;
		playerModel = new DynamicObject(animations, radius);
		playerModel.setPos(x, y);
		
		ArrayList<Drawable> shadowList = new ArrayList<Drawable>();
		shadowList.add(new Shadow(radius * 2));
		Animation shadowAnim = new Animation(shadowList, 0.0);
		ArrayList<Animation> shadowAnimList = new ArrayList<Animation>();
		shadowAnimList.add(shadowAnim);
		
		shadow = new DynamicObject(shadowAnimList, radius);
		shadow.setPos(x, y);
		
		map = _map;
		
		vel_x = vel_y = vel_height = 0.0;
	}
	
	public void update(ArrayList<DynamicObject> others)
	{
		if(Math.abs(vel_x) >= 0.01 || Math.abs(vel_y) >= 0.01)
			playerModel.getAnimation().advance();
		
		vel_height -= GRAVITY;
		playerModel.move_vert(vel_height);
		if(vel_height <= 0.0 && playerModel.getHeight() <= map.getHeight(playerModel))
		{
			vel_height = 0.0;
			playerModel.setHeight(map.getHeight(playerModel));
		}
		
		double old_x = playerModel.get_x();
		double old_y = playerModel.get_y();

		shadow.setHeight(map.getHeight(playerModel));
		
		playerModel.move_x(vel_x);
		playerModel.move_y(vel_y);
		shadow.setPos(playerModel.get_x(), playerModel.get_y());
		
		if(map.getHeight(playerModel) >= playerModel.getHeight() + 16.0)
		{
			playerModel.setPos(old_x, old_y);
			shadow.setPos(old_x, old_y);
			return;
		}
		
		if(others == null)
			return;
			
		for(DynamicObject other : others)
		{
			if(other.collide(playerModel))
				playerModel.setPos(old_x, old_y);
		}	
	}
	
	public void jump()
	{
		if(vel_height == 0.0 && Math.abs(playerModel.getHeight() - map.getHeight(playerModel)) < 4.0)
		vel_height = JUMP_SPEED;
	}
	
	public void move_x(int direction)
	{
		if(Math.abs(vel_x) <= 0.001 || Math.abs(vel_y) <= 0.001)
		{
			vel_x = NORMAL_SPEED * direction;
		}
		else
		{
			vel_y = DIAGONAL_SPEED * Math.signum(vel_y);
			vel_x = DIAGONAL_SPEED * direction;
		}
	}
	
	public void move_y(int direction)
	{
		if(Math.abs(vel_x) <= 0.001 || Math.abs(vel_y) <= 0.001)
		{
			vel_y = NORMAL_SPEED * direction;
		}
		else
		{
			vel_x = DIAGONAL_SPEED * Math.signum(vel_x);
			vel_y = DIAGONAL_SPEED * direction;
		}
	}
	
	public void stop()
	{
		playerModel.reset();
	}
	
	public void setState(int state)
	{
		playerModel.setState(state);
	}
	
	public ArrayList<DynamicObject> getDynamicObjects()
	{
		ArrayList<DynamicObject> objects = new ArrayList<DynamicObject>();
		objects.add(shadow);
		objects.add(playerModel);
		return objects;
	}
}
