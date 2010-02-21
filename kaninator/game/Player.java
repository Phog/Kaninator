/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import java.util.ArrayList;
import java.util.LinkedList;

import kaninator.graphics.*;
import kaninator.io.MapLoader;
import kaninator.mechanics.DynamicObject;

/**
 * @author phedman
 */
public class Player
{
	public static final int MOVE_UP = 1, MOVE_DOWN = 2, MOVE_LEFT = 4, MOVE_RIGHT = 8, MOVE_JUMP = 16;
	private static final int MAX_STRAFE_DIFFERENCE = 64;
	private static final int HURT_DELAY = 30;
	private static final int HURT_AMOUNT = 13;
	private static double PLAYER_SPEED = 6.0;
	private static double MAX_AIM_UP = 5.0;

	
	private Gun gun;
	private DynamicObject gunModel, crosshair;
	private double gunOffsetX, gunOffsetY;
	
	private Map map;
	private Model model;
	private int moveState;
	private int hp, hurtDelay;
	
	public Player(ArrayList<Animation> playerAnim, ArrayList<Animation> gunAnim, ArrayList<Animation> crosshairAnim, Map _map, Gun _gun, double x, double y, double radius_constant)
	{
		if(playerAnim == null || playerAnim.size() == 0 || gunAnim == null || gunAnim.size() == 0)
			return;
		
		map = _map;
		model = new Model(playerAnim, map, x, y, radius_constant, PLAYER_SPEED);
		
		crosshair = new DynamicObject(crosshairAnim, 0);
		gunModel = new DynamicObject(gunAnim, 0.0);
		gun = _gun;
		gun.setWielder(model.getModel());
		
		gunOffsetX = gunOffsetY = moveState = 0;
		hp = 100;
		hurtDelay = 0;
	}
	
	public boolean update(LinkedList<NonPlayerObject> others)
	{
		if(moveState > 0)
			model.advanceAnimation();
		
		model.update();	
		
		gunModel.setPos(model.getModel().get_x(), model.getModel().get_y());
		gunModel.setPosOffset(gunOffsetX * gunModel.getAnimation().getWidth()/3, gunOffsetY * gunModel.getAnimation().getHeight()/4);
		
		switch(gunModel.getState())
		{
			case 1:
			case 3:
			case 6:
				gunModel.setHeight(model.getModel().getHeight());
				gunModel.setHeightOffset(model.getModel().getAnimation().getHeight()/5);
				break;
			default:
				gunModel.setHeight(model.getModel().getHeight() + model.getModel().getAnimation().getHeight()/2);
				gunModel.setHeightOffset(-model.getModel().getAnimation().getHeight()/3);
				break;
		}
		
		
		if(others == null)
			return (hp <= 0);
			
		for(NonPlayerObject other : others)
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
		aimGunModel(vec_x, vec_y);
	}
	
	private void aimGunModel(double vec_x, double vec_y)
	{
		double length = Math.sqrt(vec_x*vec_x + vec_y*vec_y);
		gunOffsetX = vec_x / length;
		gunOffsetY = vec_y / length;
		
		if(Math.abs(Math.abs(vec_x) - Math.abs(vec_y)) < MAX_STRAFE_DIFFERENCE)
		{
			if(vec_x > 0 && vec_y > 0)
				gunModel.setState(0);
			else if(vec_x < 0 && vec_y > 0)
				gunModel.setState(5);
			else if(vec_x > 0 && vec_y < 0)
				gunModel.setState(2);
			else
				gunModel.setState(1);
		}
		else if(Math.abs(vec_x) > Math.abs(vec_y))
		{
			if(vec_x > 0)
				gunModel.setState(4);
			else
				gunModel.setState(6);
		}
		else
		{
			if(vec_y > 0)
				gunModel.setState(7);
			else
				gunModel.setState(3);
		}
	}
	
	public int getHp()
	{
		return hp;
	}
	
	public void hurt(int damage)
	{
		if(hurtDelay <= 0)
		{
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
		objects.addFirst(gunModel);
		objects.add(crosshair);
		return objects;
	}
}
