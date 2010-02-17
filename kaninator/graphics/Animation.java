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

	private double currentFrame, speed;
	ArrayList<Image> frames;
	
	public Animation(ArrayList<Image> _frames, double _speed)
	{
		frames = _frames;
		currentFrame = 0;
		speed = _speed;
	}
	
	public void draw(Graphics2D g, int x, int y)
	{
		frames.get((int)currentFrame).draw(g, x, y);
		
		currentFrame += speed;
		if(currentFrame >= frames.size())
			currentFrame = 0;
	}

	public int getHeight()
	{
		return (frames == null || frames.size() == 0) ? 0 : frames.get((int)currentFrame).getHeight();
	}

	public int getWidth()
	{
		return (frames == null || frames.size() == 0) ? 0 : frames.get((int)currentFrame).getWidth();
	}

	public void reset()
	{
		currentFrame = 0.0;
	}

}
