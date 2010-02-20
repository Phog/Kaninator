/**
 * The game engine package.
 * Implements all the generic services needed for a 2d game. 
 */
package kaninator.mechanics;

import java.util.ArrayList;

import kaninator.io.MapLoader;
import kaninator.game.Map;
import kaninator.graphics.Animation;

/**
 * @author phedman
 */
public class DynamicObject
{	
	private static final double NORMAL_SPEED = 4.0;
	private static final double DIAGONAL_SPEED = Math.sqrt(NORMAL_SPEED*NORMAL_SPEED/2.0);
	private static final double JUMP_SPEED = 13;
	private static final double GRAVITY = 1.5;
	
	private static final int DEPTH_OFFSET_X = 101;
	private static final int DEPTH_OFFSET_Y = 100;
	
	private double x, y, h, radius, vel_x, vel_y, vel_height;
	
	private int state;
	private Map map;
	private ArrayList<Animation> animations;
	
	public DynamicObject(ArrayList<Animation> _animations, double _radius, Map _map)
	{
		animations = _animations;
		radius = _radius;
		map = _map;
		
		x = y = h = vel_x = vel_y = vel_height = state = 0;
	}
	
	public boolean collide(DynamicObject other)
	{
		double middle_x = x + getAnimation().getWidth()/2.0;
		double middle_y = y + getAnimation().getHeight() - radius;
		
		double other_x = other.x + other.getAnimation().getWidth()/2.0;
		double other_y = other.y + other.getAnimation().getHeight() - other.radius;
		
		double distance = Math.pow((middle_x - other_x), 2) + Math.pow((middle_y - other_y), 2);
		distance = Math.sqrt(distance);
		
		return (distance <= (radius + other.radius) 
				&& Math.abs(h - other.h) <= other.getAnimation().getHeight());
	}
	
	public void update()
	{
		vel_height -= GRAVITY;
		h += vel_height;
		double mapHeight = map.getHeight(this);
		if(vel_height <= 0.0 && h <= mapHeight)
		{
			vel_height = 0.0;
			h = mapHeight;
		}

		double old_x = x;
		double old_y = y;
		x += vel_x;
		if(map.getHeight(this) >= h + MapLoader.getTileHeight()/2.0)
			x = old_x;
		
		y += vel_y;
		if(map.getHeight(this) >= h + MapLoader.getTileHeight()/2.0)
			y = old_y;
	}
	
	public void setHeight(double height)
	{
		h = height;
	}
	
	public void setPos(double _x, double _y)
	{
		x = _x;
		y = _y;
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
	
	public void setVel_x(double _vel_x)
	{
		vel_x = _vel_x;
	}
	
	public void setVel_y(double _vel_y)
	{
		vel_y = _vel_y;
	}
	
	public double getVel_x()
	{
		return vel_x;
	}
	
	public double getVel_y()
	{
		return vel_y;
	}
	
	public void jump()
	{
		if(vel_height == 0.0 && Math.abs(h - map.getHeight(this)) < NORMAL_SPEED)
			vel_height = JUMP_SPEED;
	}
	
	public double getHeight()
	{
		return h;
	}
	
	public double get_x()
	{
		return x;
	}
	
	public double get_y()
	{
		return y;
	}
	
	public int render_x()
	{
		double left_x = (x - y);
		left_x -= getAnimation().getWidth()/2.0;
		
		return (int)left_x;
	}
	
	public int render_y()
	{
		double top_y = (x + y)/2;
		top_y -= getAnimation().getHeight();
		
		return (int)top_y;
	}
	
	public int getDepth()
	{
		int depth = (int)y / (int)MapLoader.getTileSize() * DEPTH_OFFSET_Y;
		depth = depth + (int)x / (int)MapLoader.getTileSize() * DEPTH_OFFSET_X;
		depth = depth + (int)h / (int)MapLoader.getTileHeight() + 1; 
		return depth;
	}
	
	public int getState()
	{
		return state;
	}
	
	public void setState(int _state)
	{
		if(_state < 0 || _state >= animations.size())
			return;
		
		if(state != _state)
		{
			state = _state;
			animations.get(state).reset();
		}
	}
	
	public void reset()
	{
		animations.get(state).reset();
	}
	
	public Animation getAnimation()
	{
		return animations.get(state);
	}
}
