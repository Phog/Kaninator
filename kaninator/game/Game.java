/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import kaninator.mechanics.*;
import kaninator.graphics.*;
import kaninator.io.*;
import kaninator.sound.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;

/**
 * The game state.
 * The actual game is played in this state. The game loop resides here.
 * @author phedman
 * @see kaninator.game.GameState
 */
public class Game extends GameState
{
	private static final int MAX_ZOMBIES = 66;
	private static final int TIME_POINTS_RATIO = 25;
	private static final double ZOMBIE_SPAWN_PROBABILITY = 0.85;
	
	private ArrayList<Animation> zombAnim;
	private SoundClip squirt;

	private LinkedList<Zombie> enemies;
	private LinkedList<DynamicObject> enemyList;
	private LinkedList<DynamicObject> objects;
	
	private Player player;
	private Canvas canvas;
	private Map map;
	private Gun gun;
	private GoreFactory gore;
	private Text hud;
	
	private int score;
	private long framesAlive;
	
	/**
	 * Initializes the game.r
	 * Loads the map using MapFactory, creates animations for the player and the
	 * zombies using AnimationFactory and positions them randomly.
	 * @param _camera The camera class used to render the internal objects to a 2 dimensional screen.
	 * @param _gui The gui class used for overlays.
	 * @param _keyboard The keyboard class for key input.
	 * @param _mouse Mouse input.
	 * @see kaninator.graphics.AnimationFactory
	 * @see kaninator.io.MapFactory
	 */
	public Game(Camera _camera, GUI _gui, Keyboard _keyboard, Mouse _mouse, Canvas _canvas, String mapPath) throws GameException
	{
		super(_camera, _gui, _keyboard, _mouse);
		canvas = _canvas;
		score = 0;
		framesAlive = 0;
		
		//load files
		try
		{
			map = MapFactory.readMap(mapPath);
			ArrayList<Animation> playerAnim = AnimationFactory.getAnimations("/resources/theSheet.png", true, 64, 64, 0.30);
			ArrayList<Animation> gunAnim = AnimationFactory.getAnimations("/resources/gunSheet.png", true, 32, 32, 0.0);
			ArrayList<Animation> crosshairAnim = AnimationFactory.createAnimations(ImageFactory.getImage("/resources/crosshair.png"));
			ArrayList<Animation> headGore = AnimationFactory.createAnimations(ImageFactory.getImage("/resources/gore1.png"));
			ArrayList<Animation> boneGore = AnimationFactory.createAnimations(ImageFactory.getImage("/resources/gore2.png"));			
			
			Drawable bullet = ImageFactory.getImage("/resources/bullet.png");
			SoundClip shotgun = SoundFactory.getClip("/resources/shotgun.wav");
			SoundClip ow = SoundFactory.getClip("/resources/ow.wav");
			squirt = SoundFactory.getClip("/resources/squirt.wav");
			
			//create objects
			objects = new LinkedList<DynamicObject>();
			gun = new Gun(gunAnim, shotgun, map, bullet, objects, 35.0);
			gore = new GoreFactory(map, objects, headGore, boneGore);//TODO: add parameters
			player = new Player(playerAnim, crosshairAnim, ow, map, gun, 0, 0, 5.0);
		}
		catch(ModelException e)
		{
			throw new GameException("Couldn't create game objects:\n" + e);
		}
		catch(MapException e)
		{
			throw new GameException("Load the map:\n" + e);
		}

		hud = new Text("HP: " + player.getHp() + " Score: " + score, "Impact", 16, Font.PLAIN, Color.RED);
		
		//create enemies
		zombAnim = AnimationFactory.getAnimations("/resources/zombSheet.png", true, 64, 64, 0.25);
		enemies = new LinkedList<Zombie>();
		enemyList = new LinkedList<DynamicObject>();
	}
	
	/**
	 * Gets the score the player has managed to achieve. Should be called after the game is over.
	 * @return The score the player has achieved in the current game.
	 */
	public int getScore()
	{
		return score;
	}
	
	/**
	 *  The game loop. Sends the DynamicObjects (Player & NonPlayerObjects) to the camera,
	 *  enters the game loop (update objects -> update camera -> update player -> change the
	 *  player coordinates -> loop). After the game loop is done it clears up the objects from
	 *  the camera.
	 *  Returns to the main menu if the player cancels the game, otherwise the high score menu.
	 */
	public int doState()
	{
		int retValue = Kaninator.MAIN_MENU;
		canvas.hideCursor(true);
		gui.addToSection(hud, 0, 0);
		
		camera.setPlayerObjects(player.getDynamicObjects());
		camera.setEnemyObjects(enemyList);
		camera.setOtherObjects(objects);
		camera.setTiles(map.getTiles());
		
		long oldTime = System.currentTimeMillis();
		while(true)
		{
			if(keyboard.isPressed(KeyEvent.VK_ESCAPE))
				break;
			
			spawnZombies();
			
			gun.observeBullets(enemies);
			for(Iterator<Zombie> i = enemies.iterator(); i.hasNext();)
			{
				Zombie npo = i.next();
				npo.observe();
			}
			
			camera.follow(player.getMainObject());
			camera.render();
			camera.renderGUI();
			
			gore.updateGore();
			gun.updateBullets();
			for(Iterator<Zombie> i = enemies.iterator(); i.hasNext();)
			{
				Zombie npo = i.next();
				if(npo.act(enemies))
				{
					gore.gorify(npo.getMainObject());
					score += framesAlive / TIME_POINTS_RATIO;
					i.remove();
					for(DynamicObject obj : npo.getDynamicObjects())
						enemyList.remove(obj);
				}
			}
			
			if(player.update(enemies))
			{
				retValue = Kaninator.GAME_OVER;
				break;
			}
			
			movePlayer();
			player.move();
			framesAlive++;
			hud.setText("HP: " + player.getHp() + " Score: " + score);
			
			try 
			{
				long sleepTime = Kaninator.FRAME_DELAY - (System.currentTimeMillis() - oldTime);
				if(sleepTime > 0)
					Thread.sleep(sleepTime);
			} 
			catch(InterruptedException e)
			{
				System.out.println("Frame sleep interrupted: " + e);
			}
			oldTime = System.currentTimeMillis();
		}
		
		gui.clearSection(0, 0);
		camera.clearPlayerObjects();
		camera.clearEnemyObjects();
		camera.clearOtherObjects();
		canvas.hideCursor(false);
		
		return retValue;
	}
	
	/**
	 * Has a ZOMBIE_SPAWN_PROBABILITY chance to spawn a random amount of Zombies at random positions on the map.
	 * The amount of Zombies can never exceed MAX_ZOMBIES. 
	 */
	private void spawnZombies()
	{
		try
		{
			if(enemies.size() < MAX_ZOMBIES && Math.random() > ZOMBIE_SPAWN_PROBABILITY)
			{
				int numZombies = (int)(Math.random() * (MAX_ZOMBIES - enemies.size()));

				for(int i = 0; i < numZombies; i++)
				{
					double pos_y = Math.random() * map.getTiles().size() * MapFactory.getTileSize();
					double pos_x = Math.random() * map.getTiles().get(0).size() * MapFactory.getTileSize();
					Zombie enemy = new Zombie(AnimationFactory.cloneAnimations(zombAnim), map, squirt, player.getMainObject(), pos_x, pos_y, 5.0);
					enemies.add(enemy);
					for(DynamicObject obj: enemy.getDynamicObjects())
						enemyList.add(obj);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("ERR: Couldn't spawn zombies:\n" + e);
			System.exit(0);
		}
	}
	
	/**
	 * Passes the keyboard input to the player.
	 */
	private void movePlayer()
	{
		player.setMove(keyboard.isPressed(KeyEvent.VK_W), Player.MOVE_UP);
		player.setMove(keyboard.isPressed(KeyEvent.VK_S), Player.MOVE_DOWN);
		player.setMove(keyboard.isPressed(KeyEvent.VK_A), Player.MOVE_LEFT);
		player.setMove(keyboard.isPressed(KeyEvent.VK_D), Player.MOVE_RIGHT);
		player.setMove(keyboard.isPressed(KeyEvent.VK_SPACE), Player.MOVE_JUMP);

		player.aimGun(mouse.get_x() + camera.get_x(), mouse.get_y() + camera.get_y());
		if(mouse.isPressed(0))
			player.fire();
	}

}
