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
 * Contains all the relevant functions for moving a model around in the game.
 */
public class Model
{
	private double normalSpeed;
	private double diagonalSpeed;
	private double jumpSpeed = 13;
	private double gravity = 1.5;
	
	private double vel_x, vel_y, vel_height;
	private DynamicObject model, shadow;
	private Map map;
	
	public Model(ArrayList <Animation> animations, Map _map, double x, double y, double radius_constant, double speed) throws Exception
	{
		if(animations == null || animations.size() < 1)
			throw new Exception("ERR: Model animation doesn't exist");
		
		map = _map;
		
		double radius = animations.get(0).getWidth()/radius_constant;
		model = new DynamicObject(animations, radius);
		model.setPos(x, y);

		ArrayList<Animation> shadowAnimList = AnimationFactory.createAnimations(new Shadow(radius * 2));
	
		shadow = new DynamicObject(shadowAnimList, radius);
		shadow.setPos(x, y);
		
		normalSpeed = speed;
		diagonalSpeed = Math.sqrt(normalSpeed*normalSpeed/2.0);
	}

	public void update()
	{
		vel_height -= gravity;
		model.move_vert(vel_height);
		double mapHeight = map.getHeight(model);
		if(vel_height <= 0.0 && model.getHeight()<= mapHeight)
		{
			vel_height = 0.0;
			model.setHeight(mapHeight);
		}

		double old_x = model.get_x();
		double old_y = model.get_y();
		model.move_x(vel_x);
		if(map.getHeight(model) >= model.getHeight() + MapLoader.getTileHeight()/2.0)
			model.setPos(old_x, model.get_y());
		
		model.move_y(vel_y);
		if(map.getHeight(model) >= model.getHeight() + MapLoader.getTileHeight()/2.0)
			model.setPos(model.get_x(), old_y);
		
		shadow.setHeight(mapHeight);
		shadow.setPos(model.get_x(), model.get_y());
	}
	
	public void move_x(int direction)
	{
		if(Math.abs(vel_x) <= 0.001 || Math.abs(vel_y) <= 0.001)
		{
			vel_x = normalSpeed * direction;
		}
		else
		{
			vel_y = diagonalSpeed * Math.signum(vel_y);
			vel_x = diagonalSpeed * direction;
		}
	}
	
	public void move_y(int direction)
	{
		if(Math.abs(vel_x) <= 0.001 || Math.abs(vel_y) <= 0.001)
		{
			vel_y = normalSpeed * direction;
		}
		else
		{
			vel_x = diagonalSpeed * Math.signum(vel_x);
			vel_y = diagonalSpeed * direction;
		}
	}
	
	public void setVelX(double _vel_x)
	{
		vel_x = _vel_x;
	}
	
	public void setVelY(double _vel_y)
	{
		vel_y = _vel_y;
	}
	
	public double getVelX()
	{
		return vel_x;
	}
	
	public double getVelY()
	{
		return vel_y;
	}
	
	public void jump()
	{
		if(vel_height == 0.0 && Math.abs(model.getHeight() - map.getHeight(model)) < normalSpeed)
			vel_height = jumpSpeed;
	}
	
	public int getState()
	{
		return model.getState();
	}
	
	public void setState(int _state)
	{
		model.setState(_state);
	}
	
	public void advanceAnimation()
	{
		model.getAnimation().advance();
	}
	
	public void reset()
	{
		model.getAnimation().reset();
	}
	
	public DynamicObject getModel()
	{
		return model;
	}
	
	public LinkedList<DynamicObject> getDynamicObjects()
	{
		LinkedList<DynamicObject> objects = new LinkedList<DynamicObject>();
		objects.add(shadow);
		objects.add(model);
		return objects;
	}
}
