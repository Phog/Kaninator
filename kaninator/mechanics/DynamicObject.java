/**
 * The game engine package.
 * Implements all the generic services needed for a 2d game. 
 */
package kaninator.mechanics;

import java.util.ArrayList;
import kaninator.graphics.Animation;

/**
 * @author phedman
 */
public class DynamicObject
{	
	private int state;
	private double x, y, h, radius;
	private ArrayList<Animation> animations;
	
	public DynamicObject(ArrayList<Animation> _animations, double _radius)
	{
		animations = _animations;
		radius = _radius;
		
		x = y = h = 0.0;
		state = 0;
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
	
	public void setPos(double _x, double _y)
	{
		x = _x;
		y = _y;
	}
	
	public void setHeight(double _h)
	{
		h = _h;
	}
	
	public void move_y(double amount)
	{
		y += amount;
	}
	
	public void move_x(double amount)
	{
		x += amount;
	}
	
	public void move_vert(double amount)
	{
		h += amount;
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
	
	public Animation getAnimation()
	{
		return animations.get(state);
	}
}
