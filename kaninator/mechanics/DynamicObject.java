/**
 * The game engine package.
 * Implements all the generic services needed for a 2d game. 
 */
package kaninator.mechanics;

import java.util.ArrayList;
import kaninator.graphics.Drawable;

/**
 * @author phedman
 */
public class DynamicObject
{	
	private int state;
	private double x, y, h, radius;
	private ArrayList<Drawable> drawables;
	
	public DynamicObject(ArrayList<Drawable> _drawables, double _radius)
	{
		drawables = _drawables;
		radius = _radius;
		
		x = y = h = 0.0;
		state = 0;
	}
	
	public boolean collide(DynamicObject other)
	{
		double middle_x = x + getDrawable().getWidth()/2.0;
		double middle_y = y + getDrawable().getHeight() - radius;
		
		double other_x = other.x + other.getDrawable().getWidth()/2.0;
		double other_y = other.y + other.getDrawable().getHeight() - other.radius;
		
		double distance = Math.pow((middle_x - other_x), 2) + Math.pow((middle_y - other_y), 2);
		distance = Math.sqrt(distance);
		
		return (distance <= (radius + other.radius) 
				&& Math.abs(h - other.h) <= other.getDrawable().getHeight());
	}
	
	public void setState(int _state)
	{
		if(_state < 0 || _state >= drawables.size())
			return;
		
		if(state != _state)
		{
			state = _state;
			drawables.get(state).reset();
		}
	}
	
	public void reset()
	{
		drawables.get(state).reset();
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
		left_x -= getDrawable().getWidth()/2.0;
		
		return (int)left_x;
	}
	
	public int render_y()
	{
		double top_y = (x + y)/2;
		top_y -= getDrawable().getHeight();
		
		return (int)top_y;
	}
	
	public Drawable getDrawable()
	{
		return drawables.get(state);
	}
}
