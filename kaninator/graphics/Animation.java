/**
 * Wrapper package for the 2D library used for the graphics.
 */
package kaninator.graphics;

import java.awt.*;
import java.util.ArrayList;

/**
 * The class representing animations in the engine. An Animation is
 * a series of Drawables we iterate through while drawing them. Animation also implements
 * the Drawable interface and can consequently be drawn by a Canvas object.
 * Animations should only be constructed via the AnimationFactory to prevent duplicates.
 * @author phedman
 * @see kaninator.graphics.AnimationFactory
 * @see kaninator.graphics.Drawable
 * @see kaninator.graphics.Canvas
 */
public class Animation implements Drawable
{
	private boolean lock;
	private double currentFrame, speed;
	private ArrayList<Drawable> frames;
	
	/**
	 * Constructs an Animation from an ArrayList of drawables and sets the Animation speed.
	 * For internal use only. Use AnimationFactory to create Animations.
	 * @param _frames The Drawables that will make up the Animation in the end.
	 * @param _speed The speed (in frames) the animation will advance each time it's advanced.
	 */
	protected Animation(ArrayList<Drawable> _frames, double _speed)
	{
		lock = false;
		frames = _frames;
		currentFrame = 0;
		speed = _speed;
	}
	
	/**
	 * Returns the internal collection of frames, used for testing purposes only.
	 * @return The collection containing the different frames of the Animation.
	 */
	protected ArrayList<Drawable> getFrames()
	{
		return frames;
	}
	
	/**
	 * Draws the active frame of the Animation to the coordinates in the parameters.
	 * @param g The graphics context the Animation will be drawn to.
	 * @param x The x coordinate for the Animation.
	 * @param y The y coordinate for the Animation.
	 */
	public void draw(Graphics2D g, int x, int y)
	{
		frames.get((int)currentFrame).draw(g, x, y);
	}

	/**
	 * Getter for the height of the current frame of the Animation.
	 * @return The height of the current frame of the Animation.
	 */
	public int getHeight()
	{
		return (frames == null || frames.size() == 0) ? 0 : frames.get((int)currentFrame).getHeight();
	}
	
	/**
	 * Getter for the width of the current frame of the Animation.
	 * @return The width of the current frame of the Animation.
	 */
	public int getWidth()
	{
		return (frames == null || frames.size() == 0) ? 0 : frames.get((int)currentFrame).getWidth();
	}

	/**
	 * Advances the animation [speed] amount of frames.
	 * If the current frame exceeds or meets the maximum amount 
	 * of frames in the animation, then the animation will reset
	 * itself or if the lock is set to true, lock itself to the
	 * last frame of the Animation.
	 */
	public void advance()
	{
		currentFrame += speed;
		
		if(currentFrame >= frames.size() && lock)
			currentFrame -= speed;
		else if(currentFrame >= frames.size())
			currentFrame = 0;
	}
	
	/**
	 * Sets the Animation lock. In essence deciding if the animation will
	 * loop or just get stuck at the last frame.
	 * @param _lock True if the animation should be locked at the last frame, False if the Animation should loop.
	 */
	public void setLock(boolean _lock)
	{
		lock = _lock;
	}
	
	/**
	 * Sets the Animation speed. In essence the amount of frames the 
	 * animation will advance each time it is advanced.
	 * @param _speed The amount of frames the animation should advance each time it is advanced.
	 */
	public void setSpeed(double _speed)
	{
		if(_speed < frames.size())
			speed = _speed;
	}
	
	
	/**
	 * Resets the animation to the first frame.
	 */
	public void reset()
	{
		currentFrame = 0.0;
	}

	/**
	 * Updates all the drawables in the Animation.
	 *  Effectively reloading them into VRAM (and changing their transparency settings).
	 */
	public void update()
	{
		for(Drawable frame : frames)
			frame.update();
	}
	
	/**
	 * Clones the animation.
	 * Effectively creating a new animation with the same image data, but with different headers.
	 * @return An identical, but still independent, Animation.
	 */
	public Animation clone()
	{
		return new Animation(frames, speed);
	}
}
