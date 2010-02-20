/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import kaninator.mechanics.*;
import kaninator.graphics.*;
import kaninator.io.*;

import java.util.ArrayList;

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

	private Player player;
	private ArrayList<NonPlayerObject> enemies;
	private ArrayList<DynamicObject> enemyList;
	private Map map;
	
	/**
	 * Initializes the game.
	 * Loads the map using MapLoader, creates animations for the player and the
	 * zombies using AnimationFactory and positions them randomly.
	 * @param _camera The camera class used to render the internal objects to a 2 dimensional screen.
	 * @param _gui The gui class used for overlays.
	 * @param _keyboard The keyboard class for key input.
	 * @param _mouse Mouse input.
	 * @see kaninator.io.AnimationFactory
	 * @see kaninator.io.MapLoader
	 */
	public Game(Camera _camera, GUI _gui, Keyboard _keyboard, Mouse _mouse)
	{
		super(_camera, _gui, _keyboard, _mouse);
		
		try
		{
			//load files
			map = MapLoader.readMap("/resources/testmap.map");
			ArrayList<Animation> list = AnimationFactory.createAnimations("/resources/theSheet.png", true, 64, 64, 0.30);
			
			player = new Player(list, map, 0, 0, 4.0);
			
			//create enemies
			ArrayList<Animation> enemyAnim = AnimationFactory.createAnimations("/resources/zombSheet.png", true, 64, 64, 0.30);
			enemies = new ArrayList<NonPlayerObject>();
			enemyList = new ArrayList<DynamicObject>();
			ArrayList<Animation> newList = enemyAnim;
			for(int i = 0; i < 5; i++)
			{
				NonPlayerObject enemy = new NonPlayerObject(newList, map, player.getDynamicObjects().get(1), Math.random()*400 + 128, Math.random()*400 + 128, 4.0);
				enemies.add(enemy);
				enemyList.add(enemy.getDynamicObjects().get(1));
				newList = AnimationFactory.cloneAnimations(enemyAnim);
			}
		}
		catch(IOException e)
		{
			System.out.println("FILE ERROR: " + e);
		}
	}
	
	/**
	 *  The game loop. Sends the DynamicObjects (Player & NonPlayerObjects) to the camera,
	 *  enters the game loop (update objects -> update player -> update camera -> change the
	 *  player coordinates -> loop). After the gameloop is done it clears up the objects from
	 *  the camera.
	 */
	public int doState()
	{
		
		gui.addToSection(new Text("Game!!", "Tahoma", 16, Font.BOLD, Color.GREEN), 0, 0);
		
		for(DynamicObject object : player.getDynamicObjects())
			camera.addObject(object);
		
		for(NonPlayerObject npo : enemies)
			for(DynamicObject object : npo.getDynamicObjects())
				camera.addObject(object);
		
		camera.setTiles(map.getTiles());
		
		while(true)
		{
			for(NonPlayerObject npo : enemies)
			{
				npo.observe();
				npo.act(enemies);
			}
			player.update(enemyList);
			camera.follow(player.getDynamicObjects().get(1));
			
			camera.render();
			camera.renderGUI();
			
			if(keyboard.isPressed(KeyEvent.VK_ESCAPE))
				break;
			
			movePlayer();
			player.move();
			
			try {Thread.sleep(1000/40);} catch(Exception e){}
		}
		
		gui.clearSection(0, 0);
		camera.clearObjects();
		
		return 4;
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
	}

}
