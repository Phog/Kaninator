/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import kaninator.graphics.Animation;
import kaninator.graphics.AnimationFactory;
import kaninator.graphics.Drawable;
import kaninator.graphics.Shadow;
import kaninator.io.*;
import kaninator.sound.SoundClip;
import kaninator.mechanics.DynamicObject;

/**
 * @author phedman
 */
public class Gun
{
	private static final double BULLET_RADIUS = 16.0;
	private static final double SHOOT_DELAY = 30.0;
	private static final int MAX_STRAFE_DIFFERENCE = 64;

	private ArrayList<Animation> bullet;
	private ArrayList<Animation> shadow;
	private LinkedList<Bullet> bullets;
	private LinkedList<DynamicObject> bulletObjects;
	private DynamicObject wielder;
	private DynamicObject model;
	private Map map;
	private SoundClip sound;
	
	private double delta_x, delta_y, delta_height, speed, delay, offset_x, offset_y;
	
	public Gun(ArrayList<Animation> animList, SoundClip _sound, Map _map, Drawable _bullet, LinkedList<DynamicObject> _bulletObjects, double _speed)
	{
		model = new DynamicObject(animList, 0.0);
		bulletObjects = _bulletObjects;
		bullets = new LinkedList<Bullet>();
		bullet = AnimationFactory.createAnimations(_bullet);
		shadow = AnimationFactory.createAnimations(new Shadow(BULLET_RADIUS));
		
		sound = _sound;
		map = _map;
		speed = _speed;
		wielder = null;
		delay = offset_x = offset_y = 0.0;
	}
	
	public void setWielder(DynamicObject _wielder)
	{
		wielder = _wielder;
	}
	
	public void aimAt(double x, double y, double height)
	{
		double length = Math.sqrt(x*x + y*y + height*height);

		x /= length;
		y /= length;
		height /= length;

		x *= speed;
		y *= speed;
		height *= speed;
		
		delta_x = x;
		delta_y = y;
		delta_height = height;
		
		if(delay > 0)
			delay -= 1.0;
	}
	
	public void aimModel(double vec_x, double vec_y)
	{
		double length = Math.sqrt(vec_x*vec_x + vec_y*vec_y);
		offset_x = vec_x / length;
		offset_y = vec_y / length;
		
		if(Math.abs(Math.abs(vec_x) - Math.abs(vec_y)) < MAX_STRAFE_DIFFERENCE)
		{
			if(vec_x > 0 && vec_y > 0)
				model.setState(0);
			else if(vec_x < 0 && vec_y > 0)
				model.setState(5);
			else if(vec_x > 0 && vec_y < 0)
				model.setState(2);
			else
				model.setState(1);
		}
		else if(Math.abs(vec_x) > Math.abs(vec_y))
		{
			if(vec_x > 0)
				model.setState(4);
			else
				model.setState(6);
		}
		else
		{
			if(vec_y > 0)
				model.setState(7);
			else
				model.setState(3);
		}
	}
	
	public void update()
	{	
		model.setPos(wielder.get_x(), wielder.get_y());
		model.setPosOffset(offset_x * model.getAnimation().getWidth()/3, offset_y * model.getAnimation().getHeight()/4);
		
		switch(model.getState())
		{
			case 1:
			case 3:
			case 6:
				model.setHeight(wielder.getHeight());
				model.setHeightOffset(wielder.getAnimation().getHeight()/5);
				break;
			default:
				model.setHeight(wielder.getHeight() + wielder.getAnimation().getHeight()/2);
				model.setHeightOffset(-wielder.getAnimation().getHeight()/3);
				break;
		}
	}
	
	public void shoot()
	{
		if(delay <= 0)
		{
			sound.playClip();
			Bullet bul = new Bullet(new DynamicObject(bullet, BULLET_RADIUS), new DynamicObject(shadow, BULLET_RADIUS), map, wielder, delta_x, delta_y, delta_height);
			bullets.add(bul);
			bulletObjects.add(bul.getShadow());
			bulletObjects.add(bul.getMainObject());
			delay += SHOOT_DELAY;
		}
	}
	
	public void observeBullets(LinkedList<Zombie> targets)
	{
		for(Iterator<Bullet> i = bullets.iterator(); i.hasNext();)
		{
			Bullet bul = i.next();
			bul.observe(targets);
		}
	}
	
	public void updateBullets()
	{
		for(Iterator<Bullet> i = bullets.iterator(); i.hasNext();)
		{
			Bullet bul = i.next();
			if(bul.update())
			{
				i.remove();
				bulletObjects.remove(bul.getMainObject());
				bulletObjects.remove(bul.getShadow());
			}
			
		}
	}
	
	public DynamicObject getMainObject()
	{
		return model;
	}
}
