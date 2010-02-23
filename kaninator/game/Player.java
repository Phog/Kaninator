/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import java.util.ArrayList;
import java.util.LinkedList;

import kaninator.graphics.*;
import kaninator.io.MapLoader;
import kaninator.mechanics.DynamicObject;
import kaninator.sound.SoundClip;

/**
 * @author phedman
 */
public class Player
{
	public static final int MOVE_UP = 1, MOVE_DOWN = 2, MOVE_LEFT = 4, MOVE_RIGHT = 8, MOVE_JUMP = 16;
	private static final int HURT_DELAY = 30;
	private static final int HURT_AMOUNT = 13;
	private static double PLAYER_SPEED = 6.0;
	private static double MAX_AIM_UP = 5.0;
	
	private Gun gun;
	private DynamicObject crosshair;
	
	private Map map;
	private Model model;
	private SoundClip ow;
	private int moveState;
	private int hp, hurtDelay;
	
	public Player(ArrayList<Animation> playerAnim, ArrayList<Animation> crosshairAnim, SoundClip _ow, Map _map, Gun _gun, double x, double y, double radius_constant) throws Exception
	{
		map = _map;
		model = new Model(playerAnim, map, x, y, radius_constant, PLAYER_SPEED);
		ow = _ow;
		
		crosshair = new DynamicObject(crosshairAnim, 0);
		gun = _gun;
		gun.setWielder(model.getModel());
		
		moveState = hurtDelay = 0;
		hp = 100;
		
	}
	
	public boolean update(LinkedList<Zombie> others)
	{
		if(moveState > 0)
			model.advanceAnimation();
		
		model.update();	
		gun.update();
		
		if(others == null)
			return (hp <= 0);
			
		for(Zombie other : others)
		{
			DynamicObject otherModel = other.getDynamicObjects().get(1);
			if(otherModel.collide(model.getModel()))
			{
				hurt(HURT_AMOUNT);
			}
		}	
		
		if(hurtDelay > 0)
			hurtDelay--;
		
		return (hp <= 0);
	}
	
	public void aimGun(int x, int y)
	{
		double mHeight =  model.getModel().getHeight();
		double iso_x = x / 2.0 + (y + mHeight);
		double iso_y = (y + mHeight) - x / 2.0;
		
		double vec_x = iso_x - model.getModel().get_x();
		double vec_y = iso_y - model.getModel().get_y();
		
		double height = (map.getHeightAt(iso_x, iso_y) - mHeight);
		if(height > MapLoader.getTileHeight() * MAX_AIM_UP)
			height = MapLoader.getTileHeight() * MAX_AIM_UP;
		
		crosshair.setPos(iso_x, iso_y);
		crosshair.setHeight(map.getHeightAt(iso_x, iso_y));
		
		gun.aimAt(vec_x, vec_y, height);
		gun.aimModel(vec_x, vec_y);
	}
	
	public int getHp()
	{
		return hp;
	}
	
	public void hurt(int damage)
	{
		if(hurtDelay <= 0)
		{
			ow.playClip();
			hp -= damage;
			hurtDelay = HURT_DELAY;
		}
	}
	
	public void fire()
	{
		gun.shoot();
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
	
	public DynamicObject getMainObject()
	{
		return model.getModel();
	}
	
	public LinkedList<DynamicObject> getDynamicObjects()
	{
		LinkedList<DynamicObject> objects = model.getDynamicObjects();
		objects.addFirst(gun.getMainObject());
		objects.add(crosshair);
		return objects;
	}
}
