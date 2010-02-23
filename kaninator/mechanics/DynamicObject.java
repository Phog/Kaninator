/**
 * The game engine package.
 * Implements all the generic services needed for a 2d game. 
 */
package kaninator.mechanics;

import java.util.ArrayList;
import kaninator.io.MapLoader;
import kaninator.graphics.Animation;

/**
 * This is the class responsible for movement in the internal 3-Dimensional coordinate systems and animations.
 * Everything that moves and/or is animated contains a DynamicObject. DynamicObjects can perform collision checks on each other.
 * @author phedman
 */
public class DynamicObject
{		
	private static final int DEPTH_OFFSET_X = 101;
	private static final int DEPTH_OFFSET_Y = 100;
	
	private double x, y, h, radius, offsetHeight, offset_x, offset_y;
	
	private int state;
	private ArrayList<Animation> animations;
	
	/**
	 * Creates a DynamicObject from a set of animations, the radius is used to specify the collision checks.
	 * @param _animations The animations which will be displayed when the DynamicObject is rendered.
	 * @param _radius The radius to be used in collision detection.
	 */
	public DynamicObject(ArrayList<Animation> _animations, double _radius)
	{
		animations = _animations;
		radius = _radius;
		
		x = y = h = offsetHeight = offset_x = offset_y = state = 0;
	}
	
	/**
	 * Checks against a collision with another DynamicObject
	 * @param other The DynamicObject to check against.
	 * @return True if the objects collide, otherwise false.
	 */
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
	
	/**
	 * Set a height offset in order to fool the depth checking algorithms in Camera.
	 * @param _offset The offset to be added to the height of the object.
	 * @see kaninator.mechanics.Camera
	 */
	public void setHeightOffset(double _offset)
	{
		offsetHeight = _offset;
	}
	
	/**
	 * Sets the height coordinate of the dynamic object.
	 * @param height The new height for the object.
	 */
	public void setHeight(double height)
	{
		h = height;
	}
	
	/**
	 * Sets the position for the DynamicObject in the internal 3-Dimensional coordinate system.
	 * @param _x The new x coordinate.
	 * @param _y The new Y coordinate.
	 */
	public void setPos(double _x, double _y)
	{
		x = _x;
		y = _y;
	}
	
	/**
	 * Set a position offset in order to fool the depth checking algorithms in Camera.
	 * @param _offset_x The offset to be added to the x coordinate of the object.
	 * @param _offset_y The offset to be added to the y coordinate of the object.
	 * @see kaninator.mechanics.Camera
	 */
	public void setPosOffset(double _offset_x, double _offset_y)
	{
		offset_x = _offset_x;
		offset_y = _offset_y;
	}
	
	/**
	 * Moves the DynamicObject vertically.
	 * @param vel_vert The amount of the movement.
	 */
	public void move_vert(double vel_vert)
	{
		h += vel_vert;
	}
	
	/**
	 * Moves the DynamicObject on the internal x-axis
	 * @param vel_x The amount of the movement.
	 */
	public void move_x(double vel_x)
	{
		x += vel_x;
	}
	
	/**
	 * Moves the DynamicObject  on the internal y-axis
	 * @param vel_y The amount of the movement.
	 */
	public void move_y(double vel_y)
	{
		y += vel_y;
	}
	
	/**
	 * Getter for the current height of the object. Is fooled by the heightOffset.
	 * @return The height of the object
	 */
	public double getHeight()
	{
		return h + offsetHeight;
	}
	
	/**
	 * Getter for the current x-coordinate of the object. Is fooled by the x-offset.
	 * @return The x-coordinate of the object.
	 */
	public double get_x()
	{
		return x + offset_x;
	}
	/**
	 * 
	 * Getter for the current y-coordinate of the object. Is fooled by the x-offset.
	 * @return The y-coordinate of the object.
	 */
	public double get_y()
	{
		return y + offset_y;
	}
	
	
	/**
	 * Renders the internal coordinates to a 2-dimensional x coordinate to be able to draw it on the screen.
	 * @return The x-coordinate on the screen.
	 */
	public int render_x()
	{
		double left_x = (get_x() - get_y());
		left_x -= getAnimation().getWidth()/2.0;
		
		return (int)left_x;
	}
	
	/**
	 * Renders the internal coordinates to a 2-dimensional y coordinate to be able to draw it on the screen.
	 * @return The y-coordinate on the screen.
	 */
	public int render_y()
	{
		double top_y = (get_x() + get_y())/2;
		top_y -= getAnimation().getHeight();
		
		return (int)top_y;
	}
	
	/**
	 * Calculates the depth of the object so that the Camera can sort out the drawing order.
	 * @return The depth of the object.
	 * @see kaninator.mechanics.Camera
	 */
	public int getDepth()
	{
		int depth = (int)y / (int)MapLoader.getTileSize() * DEPTH_OFFSET_Y;
		depth = depth + (int)x / (int)MapLoader.getTileSize() * DEPTH_OFFSET_X;
		depth = depth + (int)h / (int)MapLoader.getTileHeight() + 1; 
		return depth;
	}
	
	
	/**
	 * Gets the current state (read animation state) of the DynamicObject.
	 * @return The current state of the DynamicObject.
	 */
	public int getState()
	{
		return state;
	}
	
	/**
	 * Sets the current state (read animation state) of the DynamicObject.
	 * @param _state The new state of the object.
	 */
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
	
	/**
	 * Resets the current animation to frame 0.
	 * @see kaninator.graphics.Animation
	 */
	public void reset()
	{
		animations.get(state).reset();
	}
	
	
	/**
	 * Getter for the current animation.
	 * @return The current Animation object.
	 * @see kaninator.graphics.Animation
	 */
	public Animation getAnimation()
	{
		return animations.get(state);
	}
}
