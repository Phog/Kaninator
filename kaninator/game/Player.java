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
	public static final int MOVE_UP = 1, MOVE_DOWN = 2, MOVE_LEFT = 4, MOVE_RIGHT = 8, MOVE_JUMP = 16;
	private static double PLAYER_SPEED = 4.0;
	
	private Map map;
	private Model model;
	private int moveState;
	
	public Player(ArrayList<Animation> animations, Map _map, double x, double y, double radius_constant)
	{
		if(animations == null || animations.size() == 0)
			return;
		
		map = _map;
		model = new Model(animations, map, x, y, radius_constant, PLAYER_SPEED);
		
		moveState = 0;
	}
	
	public void update(ArrayList<DynamicObject> others)
	{
		if(moveState > 0)
			model.advanceAnimation();
		
		model.update();	
		
		if(others == null)
			return;
			
		for(DynamicObject other : others)
		{
			if(other.collide(model.getModel()))
			{
			}
		}	
	}
	
	public void move()
	{
		if((moveState & MOVE_UP) > 0)
		{
			if((moveState & MOVE_LEFT) > 0)
			{
				model.move_y(0);
				model.move_x(-1);
				model.setState(6);
			}
			else if((moveState & MOVE_RIGHT) > 0)
			{
				model.move_y(-1);
				model.move_x(0);
				model.setState(3);
			}
			else
			{
				model.move_y(-1);
				model.move_x(-1);
				model.setState(1);
			}
		}
		else if((moveState & MOVE_DOWN) > 0)
		{
			if((moveState & MOVE_LEFT) > 0)
			{
				model.move_y(1);
				model.move_x(0);
				model.setState(7);
			}
			else if((moveState & MOVE_RIGHT) > 0)
			{
				model.move_y(0);
				model.move_x(1);
				model.setState(4);
			}
			else
			{
				model.move_y(1);
				model.move_x(1);
				model.setState(0);
			}
		}
		else
		{	
			if((moveState & MOVE_LEFT) > 0)
			{
				model.move_x(-1);
				model.move_y(1);
				model.setState(5);
			}
			else if((moveState & MOVE_RIGHT) > 0)
			{
				model.move_x(1);
				model.move_y(-1);
				model.setState(2);
			}
			else
			{
				model.move_y(0);
				model.move_x(0);
				model.reset();
			}
		}
	
		if((moveState & MOVE_JUMP) > 0)
			model.jump();
	}	
	
	public void setMove(boolean on, int flag)
	{
		if(on)
			moveState |= flag;
		else
			moveState &= ~flag;
	}
	
	public ArrayList<DynamicObject> getDynamicObjects()
	{
		return model.getDynamicObjects();
	}
}
