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
	private static final int DEPTH_OFFSET_X = 101;
	private static final int DEPTH_OFFSET_Y = 100;
	
	private double x, y, h, radius, offsetHeight, offset_x, offset_y;
	
	private int state;
	private ArrayList<Animation> animations;
	
	public DynamicObject(ArrayList<Animation> _animations, double _radius)
	{
		animations = _animations;
		radius = _radius;;
		
		x = y = h = offsetHeight = offset_x = offset_y = state = 0;
	}
	
	public boolean collide(DynamicObject other)
	{
		double middle_x = render_x() + getAnimation().getWidth()/2;
		double middle_y = render_y() + getAnimation().getHeight();
		
		double other_x = other.render_x() + other.getAnimation().getWidth()/2;
		double other_y = other.render_y() + getAnimation().getHeight();
		
		double distance = Math.sqrt((middle_x - other_x) * (middle_x - other_x) + (middle_y - other_y) * (middle_y - other_y));
		
		return (distance <= (radius + other.radius)
				&& Math.abs(h - other.h) <= other.getAnimation().getHeight());
	}
	
	public void setHeightOffset(double _offset)
	{
		offsetHeight = _offset;
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
	
	public void setPosOffset(double _offset_x, double _offset_y)
	{
		offset_x = _offset_x;
		offset_y = _offset_y;
	}
	
	public void move_vert(double vel_vert)
	{
		h += vel_vert;
	}
	
	public void move_x(double vel_x)
	{
		x += vel_x;
	}
	
	public void move_y(double vel_y)
	{
		y += vel_y;
	}
	
	public double getHeight()
	{
		return h + offsetHeight;
	}
	
	public double get_x()
	{
		return x + offset_x;
	}
	
	public double get_y()
	{
		return y + offset_y;
	}
	
	public int render_x()
	{
		double left_x = (get_x() - get_y());
		left_x -= getAnimation().getWidth()/2.0;
		
		return (int)left_x;
	}
	
	public int render_y()
	{
		double top_y = (get_x() + get_y())/2;
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
