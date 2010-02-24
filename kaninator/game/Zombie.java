/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import java.util.ArrayList;
import java.util.LinkedList;

import kaninator.sound.SoundClip;
import kaninator.graphics.Animation;
import kaninator.mechanics.DynamicObject;

/**
 * The main enemy class of the game. The Zombies contain
 * model objects which represent them on screen. They can
 * also perform rudimentary AI, trying to flock up on the player.
 * @author phedman
 */
public class Zombie
{
	private static final int MAX_ACTIVE_DISTANCE = 333;
	private static final int MIN_DISTANCE_BETWEEN = 48;
	private static final int MAX_STRAFE_DIFFERENCE = 8;
	private static final double ZOMBIE_SPEED = 5.0;
	
	private Map map;
	private Model model;
	private DynamicObject player;
	private SoundClip squirt;
	private double distance;
	private boolean dead;
	
	/**
	 * Constructs a Zombie object and initializes all the objects it is dependent on.
	 * @param animations The ArrayList of Animations that build up the Zombie model. Cannot be null or of the size 0.
	 * @param _map The game map used for height checking.
	 * @param _squirt The sound to be played when the Zombie is hit by a bullet.
	 * @param _player The player object the Zombies are chasing.
	 * @param x The starting x coordinate for the player.
	 * @param y The starting y coordinate for the player.
	 * @param radius_constant How much the width of the Animations should be divided with to get a realistic estimate for the radius of the model.
	 * @throws Exception Exception If the animation is null or of the size 0, since the Model object cannot be created without these.
	 */
	public Zombie(ArrayList<Animation> animations, Map _map, SoundClip _squirt, DynamicObject _player, double x, double y, double radius_constant) throws Exception
	{
		map = _map;
		model = new Model(animations, map, x, y, radius_constant, ZOMBIE_SPEED);
		squirt = _squirt;
		
		player = _player;
		distance = Double.MAX_VALUE;
		dead = false;
	}
	
	/**
	 * Calculates the distance between the Zombie and the DynamicObject in the parameter
	 * @param other The DynamicObject the distance will be calculated to.
	 * @return The distance between the Zombie and the DynamicObject in the parameter.
	 */
	private double distanceTo(DynamicObject other)
	{
		double distTo = Math.pow((other.get_x() - model.getModel().get_x()),2) + Math.pow((other.get_y() - model.getModel().get_y()),2);
		return Math.sqrt(distTo);
	}
	
	/**
	 * Makes the >ombie follow the DynamicObject set as the target.
	 * Doesn't perform pathfinding or anything fancy, just brute force following, even if a wall is in the way.
	 * @param target The DynamicObject the Zombie should follow.
	 */
	private void follow(DynamicObject target)
	{
		double delta_x = target.get_x() - model.getModel().get_x();
		double delta_y = target.get_y() - model.getModel().get_y();
		if(Math.abs(Math.abs(delta_x) - Math.abs(delta_y)) < MAX_STRAFE_DIFFERENCE)
		{
			if(delta_x > 0 && delta_y > 0)
			{
				model.move_y(1);
				model.move_x(1);
				model.setState(0);
			}
			else if(delta_x < 0 && delta_y > 0)
			{
				model.move_y(1);
				model.move_x(-1);
				model.setState(5);
			}
			else if(delta_x > 0 && delta_y < 0)
			{
				model.move_y(-1);
				model.move_x(1);
				model.setState(2);
			}
			else
			{
				model.move_y(-1);	
				model.move_x(-1);
				model.setState(1);
			}
		}
		else if(Math.abs(delta_x) > Math.abs(delta_y))
		{
			if(delta_x > 0)
			{
				model.move_x(1);
				model.move_y(0);
				model.setState(4);
			}
			else
			{
				model.move_x(-1);
				model.move_y(0);
				model.setState(6);
			}
		}
		else
		{
			if(delta_y > 0)
			{
				model.move_y(1);
				model.move_x(0);
				model.setState(7);
			}
			else
			{
				model.move_y(-1);
				model.move_x(0);
				model.setState(3);
			}
		}
	}
	
	/**
	 * Calculates the distance between the Zombie and the player and stores it in 
	 * order to be able to compare distances with its peers later on.
	 */
	public void observe()
	{
		distance = distanceTo(player);
	}
	
	/**
	 * Kills the Zombie. Effectively setting the dead value to true and playing the squish sound.
	 */
	public void kill()
	{
		dead = true;
		squirt.playClip();
	}
	
	
	/**
	 * Performs the actions the Zombie should do. If the Zombie is already dead it returns true so
	 * the Zombie can be removed from the list in Game. The Zombie is inactive if the distance to the
	 * player is less than MAX_ACTIVE_DISTANCE, otherwise it compares the distances to the player with its
	 * peers and then mimics the Zombie with the closest distance to the player.
	 * @param others The other Zombies the Zombie collaborates with.
	 * @return true if the Zombie is dead, otherwise false.
	 */
	public boolean act(LinkedList<Zombie> others)
	{
		if(dead)
			return true;
		
		Zombie leader = null;
		if(distance < MAX_ACTIVE_DISTANCE)
		{
			for(Zombie otherone : others)
			{
				if(distanceTo(otherone.getMainObject()) < MIN_DISTANCE_BETWEEN)
					if(distance > otherone.distance)
						leader = otherone;
			}
			
			if(leader != null)
			{
				model.setVelX(leader.model.getVelX());
				model.setVelY(leader.model.getVelY());
				model.setState(leader.model.getModel().getState());
				model.advanceAnimation();
			}
			else
			{
				follow(player);
				model.advanceAnimation();
			}
		}
		else
		{
			model.move_x(0);
			model.move_y(0);
			model.reset();
		}
		
		model.update();
		return false;
	}
	
	/**
	 * Getter for the DynamicObject representing the Zombie.
	 * @return The DynamicObject representing the Zombie.
	 */
	public DynamicObject getMainObject()
	{
		return model.getModel();
	}
	
	/**
	 * Gets all the DynamicObjects associated with the Zombie. Ie. The Zombie model itself and its shadow.
	 * @return A LinkedList of all the DynamicObjects associated with the Zombie.
	 */
	public LinkedList<DynamicObject> getDynamicObjects()
	{
		return model.getDynamicObjects();
	}
}
