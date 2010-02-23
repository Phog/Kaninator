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
import java.io.IOException;

/**
 * The game state.
 * The actual game is played in this state. The gameloop resides here.
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
	private LinkedList<DynamicObject> bullets;
	
	private Player player;
	private Canvas canvas;
	private Map map;
	private Gun gun;
	private Text hud;
	
	private int score;
	private long framesAlive;
	
	/**
	 * Initializes the game.
	 * Loads the map using MapLoader, creates animations for the player and the
	 * zombies using AnimationFactory and positions them randomly.
	 * @param _camera The camera class used to render the internal objects to a 2 dimensional screen.
	 * @param _gui The gui class used for overlays.
	 * @param _keyboard The keyboard class for key input.
	 * @param _mouse Mouse input.
	 * @see kaninator.graphics.AnimationFactory
	 * @see kaninator.io.MapLoader
	 */
	public Game(Camera _camera, GUI _gui, Keyboard _keyboard, Mouse _mouse, Canvas _canvas, String mapPath) throws IOException
	{
		super(_camera, _gui, _keyboard, _mouse);
		canvas = _canvas;
		score = 0;
		framesAlive = 0;
		
		//load files
		map = MapLoader.readMap(mapPath);
		ArrayList<Animation> playerAnim = AnimationFactory.getAnimations("/resources/theSheet.png", true, 64, 64, 0.30);
		ArrayList<Animation> gunAnim = AnimationFactory.getAnimations("/resources/gunSheet.png", true, 32, 32, 0.0);
		ArrayList<Animation> crosshairAnim = AnimationFactory.createAnimations(ImageFactory.getImage("/resources/crosshair.png"));
		
		Drawable bullet = ImageFactory.getImage("/resources/bullet.png");
		SoundClip shotgun = SoundFactory.getClip("/resources/shotgun.wav");
		SoundClip ow = SoundFactory.getClip("/resources/ow.wav");
		squirt = SoundFactory.getClip("/resources/squirt.wav");
		
		//create objects
		bullets = new LinkedList<DynamicObject>();
		try
		{
			gun = new Gun(gunAnim, shotgun, map, bullet, bullets, 25.0);
			player = new Player(playerAnim, crosshairAnim, ow, map, gun, 0, 0, 5.0);
		}
		catch(Exception e)
		{
			throw new IOException("Couldn't create game objects:\n" + e);
		}

		hud = new Text("HP: " + player.getHp() + " Score: " + score, "Impact", 16, Font.PLAIN, Color.RED);
		
		//create enemies
		zombAnim = AnimationFactory.getAnimations("/resources/zombSheet.png", true, 64, 64, 0.25);
		enemies = new LinkedList<Zombie>();
		enemyList = new LinkedList<DynamicObject>();
	}
	
	public int getScore()
	{
		return score;
	}
	
	/**
	 *  The game loop. Sends the DynamicObjects (Player & NonPlayerObjects) to the camera,
	 *  enters the game loop (update objects -> update player -> update camera -> change the
	 *  player coordinates -> loop). After the gameloop is done it clears up the objects from
	 *  the camera.
	 */
	public int doState()
	{
		int retValue = Kaninator.MAIN_MENU;
		canvas.hideCursor(true);
		gui.addToSection(hud, 0, 0);
		
		camera.setPlayerObjects(player.getDynamicObjects());
		camera.setEnemyObjects(enemyList);
		camera.setBulletObjects(bullets);
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
			
			gun.updateBullets();
			for(Iterator<Zombie> i = enemies.iterator(); i.hasNext();)
			{
				Zombie npo = i.next();
				if(npo.act(enemies))
				{
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
			finally
			{
				oldTime = System.currentTimeMillis();
			}
		}
		
		gui.clearSection(0, 0);
		camera.clearPlayerObjects();
		camera.clearEnemyObjects();
		camera.clearBulletObjects();
		canvas.hideCursor(false);
		
		return retValue;
	}
	
	
	private void spawnZombies()
	{
		try
		{
			if(enemies.size() < MAX_ZOMBIES && Math.random() > ZOMBIE_SPAWN_PROBABILITY)
			{
				int numZombies = (int)(Math.random() * (MAX_ZOMBIES - enemies.size()));
				ArrayList<Animation> newList = AnimationFactory.cloneAnimations(zombAnim);
				
				for(int i = 0; i < numZombies; i++)
				{
					double pos_y = Math.random() * map.getTiles().size() * MapLoader.getTileSize();
					double pos_x = Math.random() * map.getTiles().get(0).size() * MapLoader.getTileSize();
					Zombie enemy = new Zombie(newList, map, squirt, player.getMainObject(), pos_x, pos_y, 5.0);
					enemies.add(enemy);
					for(DynamicObject obj: enemy.getDynamicObjects())
						enemyList.add(obj);
					newList = AnimationFactory.cloneAnimations(zombAnim);
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
