/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import java.util.ArrayList;
import kaninator.graphics.Drawable;
import kaninator.mechanics.DynamicObject;

/**
 * @author phedman
 */
public class Player
{
	private static final double NORMAL_SPEED = 4.0;
	private static final double DIAGONAL_SPEED = Math.sqrt(NORMAL_SPEED*NORMAL_SPEED/2.0);
	private static final double JUMP_SPEED = 15.0;
	private static final double GRAVITY = 2.0;
	
	private DynamicObject playerModel;
	private double vel_x, vel_y, vel_height;
	
	public Player(ArrayList<Drawable> drawables, double x, double y, double radius_constant)
	{
		if(drawables == null || drawables.size() == 0)
			return;
		
		double radius = drawables.get(0).getWidth()/radius_constant;
		playerModel = new DynamicObject(drawables, radius);
		playerModel.setPos(x, y);
		
		vel_x = vel_y = vel_height = 0.0;
	}
	
	public void update(ArrayList<DynamicObject> others)
	{
		if(vel_height <= 0.0 && playerModel.getHeight() <= 0.0)
		{
			vel_height = 0.0;
			playerModel.setHeight(0.0);
		}
		else
		{
			vel_height -= GRAVITY;
			playerModel.move_vert(vel_height);
		}
		
		double old_x = playerModel.get_x();
		double old_y = playerModel.get_y();
		
		playerModel.move_x(vel_x);
		playerModel.move_y(vel_y);
		
		for(DynamicObject other : others)
		{
			if(other.collide(playerModel))
				playerModel.setPos(old_x, old_y);
		}	
	}
	
	public void jump()
	{
		if(vel_height == 0.0)
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
	
	public ArrayList<DynamicObject> getDynamicObjects()
	{
		ArrayList<DynamicObject> objects = new ArrayList<DynamicObject>();
		objects.add(playerModel);
		return objects;
	}
}
