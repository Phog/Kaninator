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
	
	
	public Game(Camera _camera, GUI _gui, Keyboard _keyboard, Mouse _mouse)
	{
		super(_camera, _gui, _keyboard, _mouse);
		
		try
		{
			map = MapLoader.readMap("/resources/testmap.map");
			ArrayList<Animation> list = AnimationFactory.createAnimations("/resources/theSheet.png", true, 64, 64, 0.30);

			player = new Player(list, map, 0, 0, 4.0);
			enemies = new ArrayList<NonPlayerObject>();
			enemyList = new ArrayList<DynamicObject>();
			for(int i = 0; i < 5; i++)
			{
				ArrayList<Animation> newList = AnimationFactory.cloneAnimations(list);
				NonPlayerObject enemy = new NonPlayerObject(newList, map, player.getDynamicObjects().get(1), Math.random()*400 + 128, Math.random()*400 + 128, 4.0);
				enemies.add(enemy);
				enemyList.add(enemy.getDynamicObjects().get(1));
			}
		}
		catch(IOException e)
		{
			System.out.println("FILE ERROR: " + e);
		}
	}

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
				npo.act();
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
	
	private void movePlayer()
	{
		player.setMove(keyboard.isPressed(KeyEvent.VK_W), Player.MOVE_UP);
		player.setMove(keyboard.isPressed(KeyEvent.VK_S), Player.MOVE_DOWN);
		player.setMove(keyboard.isPressed(KeyEvent.VK_A), Player.MOVE_LEFT);
		player.setMove(keyboard.isPressed(KeyEvent.VK_D), Player.MOVE_RIGHT);
		player.setMove(keyboard.isPressed(KeyEvent.VK_SPACE), Player.MOVE_JUMP);
	}

}
