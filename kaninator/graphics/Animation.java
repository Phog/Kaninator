/**
 * Wrapper package for the 2D library used for the graphics.
 */
package kaninator.graphics;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author phedman
 */
public class Animation implements Drawable
{
	private boolean lock;
	private double currentFrame, speed;
	ArrayList<Drawable> frames;
	
	public Animation(ArrayList<Drawable> _frames, double _speed)
	{
		lock = false;
		frames = _frames;
		currentFrame = 0;
		speed = _speed;
	}
	
	public void draw(Graphics2D g, int x, int y)
	{
		frames.get((int)currentFrame).draw(g, x, y);
	}

	public int getHeight()
	{
		return (frames == null || frames.size() == 0) ? 0 : frames.get((int)currentFrame).getHeight();
	}

	public int getWidth()
	{
		return (frames == null || frames.size() == 0) ? 0 : frames.get((int)currentFrame).getWidth();
	}

	public void advance()
	{
		currentFrame += speed;
		
		if(currentFrame >= frames.size() && lock)
			currentFrame -= speed;
		else if(currentFrame >= frames.size())
			currentFrame = 0;
	}
	
	public void setLock(boolean _lock)
	{
		lock = _lock;
	}
	
	public void setSpeed(double _speed)
	{
		speed = _speed;
	}
	
	public void reset()
	{
		currentFrame = 0.0;
	}

	public Animation clone()
	{
		return new Animation(frames, speed);
	}
}
