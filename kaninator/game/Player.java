/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import java.util.ArrayList;
import java.util.LinkedList;

import kaninator.graphics.*;
import kaninator.io.MapLoader;
import kaninator.mechanics.DynamicObject;
import kaninator.sound.SoundClip;

/**
 * The Player class represents the player in the game. It contains the information 
 * about the player and uses a Model object for all the actions the player will perform. 
 * Ex: moving, jumping, aiming and shooting. The player contains a lot of DynamicObjects both directly and indirectly:
 * The gun, the crosshair, the animation itself and the shadow.
 * @author phedman
 */
public class Player
{
	public static final int MOVE_UP = 1, MOVE_DOWN = 2, MOVE_LEFT = 4, MOVE_RIGHT = 8, MOVE_JUMP = 16;
	private static final int HURT_DELAY = 30;
	private static final int HURT_AMOUNT = 13;
	private static double PLAYER_SPEED = 6.0;
	private static double MAX_AIM_UP = 5.0;
	
	private Gun gun;
	private DynamicObject crosshair;
	
	private Map map;
	private Model model;
	private SoundClip ow;
	private int moveState;
	private int hp, hurtDelay;
	
	/**
	 * Constructs the player object and initializes all the objects it is dependent on.
	 * Also sets the wielder of the gun object to be itself.
	 * @param playerAnim The ArrayList of Animations that build up the player model. Cannot be null or of the size 0.
	 * @param crosshairAnim The ArrayList of Animations that represent the Crosshair.
	 * @param _ow The sound to be played when the player is hit by a Zombie.
	 * @param _map The game map used for height checking.
	 * @param _gun The gun object.
	 * @param x The starting x coordinate for the player.
	 * @param y The starting y coordinate for the player.
	 * @param radius_constant How much the width of the Animations should be divided with to get a realistic estimate for the radius of the model.
	 * @throws Exception Exception If the animation is null or of the size 0, since the Model object cannot be created without these.
	 */
	public Player(ArrayList<Animation> playerAnim, ArrayList<Animation> crosshairAnim, SoundClip _ow, Map _map, Gun _gun, double x, double y, double radius_constant) throws ModelException
	{
		map = _map;
		model = new Model(playerAnim, map, x, y, radius_constant, PLAYER_SPEED);
		ow = _ow;
		
		crosshair = new DynamicObject(crosshairAnim, 0);
		gun = _gun;
		gun.setWielder(model.getModel());
		
		moveState = hurtDelay = 0;
		hp = 100;
		
	}
	
	
	/**
	 * Updates the player. Advances the animation if the player is moving. Calls update for the model and the gun.
	 * Performs collision detection with the Zombies provided as a parameter, calls hurt() if a collision occurs.
	 * @param others The LinkedList of Zombies to perform collision detection against.
	 * @return False if the player is still alive, True if the player is deceased and the game is over.
	 */
	public boolean update(LinkedList<Zombie> others)
	{
		if(moveState > 0)
			model.advanceAnimation();
		
		model.update();	
		gun.update();
		
		if(others == null)
			return (hp <= 0);
			
		for(Zombie other : others)
		{
			DynamicObject otherModel = other.getDynamicObjects().get(1);
			if(otherModel.collide(model.getModel()))
			{
				hurt(HURT_AMOUNT);
			}
		}	
	
		if(hurtDelay > 0)
			hurtDelay--;
		
		return (hp <= 0);
	}
	
	/**
	 * Approximates an internal isometric point from the on-screen coordinates provided as parameters. 
	 * Aims the gun at that point and sets the crosshair position to that point as well.
	 * @param x The on screen x coordinate to be aimed at.
	 * @param y The on screen y coordinate to be aimed at.
	 */
	public void aimGun(int x, int y)
	{
		double mHeight =  model.getModel().getHeight();
		double iso_x = x / 2.0 + (y + mHeight);
		double iso_y = (y + mHeight) - x / 2.0;
		
		double vec_x = iso_x - model.getModel().get_x();
		double vec_y = iso_y - model.getModel().get_y();

		double height = (map.getHeightAt(iso_x, iso_y) - mHeight);
		if(height > MapLoader.getTileHeight() * MAX_AIM_UP)
			height = MapLoader.getTileHeight() * MAX_AIM_UP;
		
		crosshair.setPos(iso_x, iso_y);
		crosshair.setHeight(map.getHeightAt(iso_x, iso_y));
		
		gun.setAim(vec_x, vec_y, height);
		gun.aimModel(vec_x, vec_y);
	}
	
	/**
	 * Getter for the remaining hp for the player
	 * @return How much health points the player has left.
	 */
	public int getHp()
	{
		return hp;
	
	}
	
	/**
	 * Hurts the player the amount of damage given as a parameter. Plays the ow sound clip and
	 * sets a timer in order to prevent the player from getting hurt 30 times a second.
	 * @param damage
	 */
	public void hurt(int damage)
	{
		if(hurtDelay <= 0)
		{
			ow.playClip();
			hp -= damage;
			hurtDelay = HURT_DELAY;
		}
	}
	
	/**
	 * Fires the gun the player has.
	 */
	public void fire()
	{
		gun.shoot();
	}
	
	/**
	 * Sets the movement of the player according to the keys that have been pressed.
	 */
	public void move()
	{
		if((moveState & MOVE_UP) > 0)
		{
			if((moveState & MOVE_LEFT) > 0)
			{
				model.move_y(0);
				model.move_x(-1);
				model.setState(6);
			}
			else if((moveState & MOVE_RIGHT) > 0)
			{
				model.move_y(-1);
				model.move_x(0);
				model.setState(3);
			}
			else
			{
				model.move_y(-1);
				model.move_x(-1);
				model.setState(1);
			}
		}
		else if((moveState & MOVE_DOWN) > 0)
		{
			if((moveState & MOVE_LEFT) > 0)
			{
				model.move_y(1);
				model.move_x(0);
				model.setState(7);
			}
			else if((moveState & MOVE_RIGHT) > 0)
			{
				model.move_y(0);
				model.move_x(1);
				model.setState(4);
			}
			else
			{
				model.move_y(1);
				model.move_x(1);
				model.setState(0);
			}
		}
		else
		{	
			if((moveState & MOVE_LEFT) > 0)
			{
				model.move_x(-1);
				model.move_y(1);
				model.setState(5);
			}
			else if((moveState & MOVE_RIGHT) > 0)
			{
				model.move_x(1);
				model.move_y(-1);
				model.setState(2);
			}
			else
			{
				model.move_y(0);
				model.move_x(0);
				model.reset();
			}
		}
	
		if((moveState & MOVE_JUMP) > 0)
			model.jump();
	}	
	
	/**
	 * Accessor for the different movement flags.
	 * @param on Whether the flag should be turned on or off.
	 * @param flag What flag(s) that should be affected. (MOVE_UP, MOVE_DOWN, MOVE_LEFT, MOVE_RIGHT, MOVE_JUMP)
	 */
	public void setMove(boolean on, int flag)
	{
		if(on)
			moveState |= flag;
		else
			moveState &= ~flag;
	}
		
	/**
	 * Getter for the DynamicObject representing the player.
	 * @return The DynamicObject representing the player.
	 */
	public DynamicObject getMainObject()
	{
		return model.getModel();
	}
	
	/**
	 * Gets all the DynamicObjects associated with the player. Ie. The player model itself,its shadow the gun and the crosshair.
	 * @return A LinkedList of all the DynamicObjects associated with the player.
	 */
	public LinkedList<DynamicObject> getDynamicObjects()
	{
		LinkedList<DynamicObject> objects = model.getDynamicObjects();
		objects.addFirst(gun.getMainObject());
		objects.add(crosshair);
		return objects;
	}
}
