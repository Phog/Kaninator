/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import kaninator.graphics.Animation;
import kaninator.graphics.Drawable;
import kaninator.io.*;
import kaninator.mechanics.DynamicObject;

/**
 * @author phedman
 */
public class Gun
{
	private static final double BULLET_RADIUS = 16.0;
	private static final double SHOOT_DELAY = 25.0;
	
	private ArrayList<Animation> animList;
	private ArrayList<Animation> bullet;
	private LinkedList<Bullet> bullets;
	private LinkedList<DynamicObject> bulletObjects;
	private DynamicObject wielder;
	private Map map;
	
	private double delta_x, delta_y, delta_height, speed, delay;
	
	public Gun(ArrayList<Animation> _animList, Map _map, Drawable _bullet, LinkedList<DynamicObject> _bulletObjects, double _speed)
	{
		animList = _animList;
		bulletObjects = _bulletObjects;
		bullets = new LinkedList<Bullet>();
		bullet = AnimationFactory.createAnimations(_bullet);
		
		map = _map;
		speed = _speed;
		wielder = null;
		delay = 0.0;
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
	
	public void shoot()
	{
		if(delay <= 0)
		{
			Bullet bul = new Bullet(new DynamicObject(bullet, BULLET_RADIUS), map, wielder, delta_x, delta_y, delta_height);
			bullets.add(bul);
			bulletObjects.add(bul.getDynamicObject());
			delay += SHOOT_DELAY;
		}
	}
	
	public void observeBullets(LinkedList<NonPlayerObject> targets)
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
				bulletObjects.remove(bul.getDynamicObject());
			}
			
		}
	}
}
