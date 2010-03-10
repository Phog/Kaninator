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
import kaninator.sound.SoundClip;
import kaninator.mechanics.DynamicObject;

/**
 * Represents the gun wielded by the player in the game. Creates Bullets and also has a
 * graphical representation of its own.
 * @author phedman
 * @see kaninator.game.Bullet
 */
public class Gun
{
	private static final double BULLET_RADIUS = 16.0;
	private static final double SHOOT_DELAY = 30.0;
	private static final double SPREAD_VALUE = 1.0 / 25.0;
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
	
	/**
	 * Constructs a gun. Associates it with the resources needed to display the gun, 
	 * play the a sound when it is fired and add new bullets to the game itself.
	 * @param animList The animations used to display the gun.
	 * @param _sound The sound to be played when the gun fires.
	 * @param _map Map used for aiming and height checking.
	 * @param _bullet The graphical representation of a bullet.
	 * @param _bulletObjects The list to which the bullets will be added.
	 * @param _speed The speed the bullets will travel at.
	 * @throws Exception If the animation is null or of the size 0, since the model cannot be created without these.
	 */
	public Gun(ArrayList<Animation> animList, SoundClip _sound, Map _map, Drawable _bullet, LinkedList<DynamicObject> _bulletObjects, double _speed) throws ModelException
	{
		if(animList == null || animList.size() < 1)
			throw new ModelException("ERR: Gun animation doesn't exist");
		
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
	
	/**
	 * Sets the wielder of the gun, to get the initial height and positions of the bullets.
	 * The gun won't function without a wielder.
	 * @param _wielder The DynamicObject that wields the gun.
	 */
	public void setWielder(DynamicObject _wielder)
	{
		wielder = _wielder;
	}
	
	/**
	 * Aims the gun into the direction made up by the coordinates in the parameters.
	 * In essence it treats the coordinates as a vector, normalizes them, and multiplies
	 * them with the speed of the bullet effectively creating a new aiming vector.
	 * It also decreases shoot delay for each method call.
	 * @param x X coordinate of the new aiming direction.
	 * @param y Y coordinate of the new aiming direction.
	 * @param height The height difference of the new aiming direction.
	 */
	public void setAim(double x, double y, double height)
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
	
	/**
	 * Updates the state (the current active animation) of the model so it corresponds with the
	 * new aiming vector.
	 * @param vec_x The x component of the aiming vector.
	 * @param vec_y The y component of the aiming vector.
	 */
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

	/**
	 * Updates the internal state of the gun. Setting its position to the wielders position.
	 * Tweaking the height and position of the DynamicObject with setHeightOffset
	 * respectively setPosOffset to preserve a reasonable drawing order.
	 * @see kaninator.mechanics.DynamicObject#setHeightOffset(double)
	 * @see kaninator.mechanics.DynamicObject#setPosOffset(double, double)
	 */
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
	
	/**
	 * Fires the gun. Effectively playing the SoundClip and adding a new Bullet object to the
	 * LinkedList of Bullets contained in Game. Also sets the shoot delay to SHOOT_DELAY. Will only
	 * fire if the shoot delay is zero, the shoot delay is decreased in setAim().
	 * @see kaninator.game.Gun#setAim(double, double, double)
	 * @see kaninator.game.Bullet
	 * @see kaninator.game.Game
	 */
	public void shoot()
	{
		if(delay <= 0)
		{
			sound.playClip();
			double rite_x = -delta_y;
			double rite_y = delta_x;
			
			for(int i = -2; i <= 2; i++)
			{
				Bullet bul = new Bullet(new DynamicObject(bullet, BULLET_RADIUS),
										new DynamicObject(shadow, BULLET_RADIUS),
										map, model,delta_x + rite_x * i * SPREAD_VALUE,
										delta_y + rite_y * i * SPREAD_VALUE, delta_height);
				bullets.add(bul);
				bulletObjects.add(bul.getShadow());
				bulletObjects.add(bul.getMainObject());
			}
			delay += SHOOT_DELAY;
		}
	}
	
	/**
	 * Loops through all the Bullets in the LinkedList and calls observe() for them:
	 * Effectively checking them for collisions and moving them.
	 * @param targets The LinkedList of Zombies that the Bullets should check against collisions with.
	 * @see kaninator.game.Zombie
	 * @see kaninator.game.Bullet#observe(LinkedList)
	 * @see kaninator.game.Bullet
	 */
	public void observeBullets(LinkedList<Zombie> targets)
	{
		for(Iterator<Bullet> i = bullets.iterator(); i.hasNext();)
		{
			Bullet bul = i.next();
			bul.observe(targets);
		}
	}
	
	/**
	 * Loops through all the Bullets in the LinkedList and calls update() for them:
	 * If they return true, then delete them from the list of Bullets, and their DynamicObjects
	 * from the list of DynamicObjects.
	 * @see kaninator.game.Bullet
	 * @see kaninator.game.Bullet#update()
	 */
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
	
	/**
	 * Gets the DynamicObject representing the Gun.
	 * @return The DynamicObject containing the Animations for the gun.
	 */
	public DynamicObject getMainObject()
	{
		return model;
	}
}
