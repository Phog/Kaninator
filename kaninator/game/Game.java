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
	private Map map;
	
	public Game(Camera _camera, GUI _gui, Keyboard _keyboard, Mouse _mouse)
	{
		super(_camera, _gui, _keyboard, _mouse);
		
		try
		{
			map = MapLoader.readMap("/resources/testmap.map");
			ArrayList<Animation> list = AnimationFactory.createAnimations("/resources/theSheet.png", true, 64, 64, 0.30);

			player = new Player(list, map, 0, 0, 4.0);
		}
		catch(IOException e)
		{
			System.out.println("IMAGE NOT FOUND: " + e);
		}
	}

	public int doState()
	{
		
		gui.addToSection(new Text("Game!!", "Tahoma", 16, Font.BOLD, Color.GREEN), 0, 0);
		
		for(DynamicObject object : player.getDynamicObjects())
			camera.addObject(object);
		
		camera.setTiles(map.getTiles());
		
		while(true)
		{
			player.update(null);
			camera.follow(player.getDynamicObjects().get(1));
			
			camera.render();
			camera.renderGUI();
			
			if(keyboard.isPressed(KeyEvent.VK_ESCAPE))
				break;
			
			movePlayer();
			
			try {Thread.sleep(1000/40);} catch(Exception e){}
		}
		
		gui.clearSection(0, 0);
		camera.clearObjects();
		
		return 3;
	}
	
	private void movePlayer()
	{
		
		if(keyboard.isPressed(KeyEvent.VK_W))
		{
			if(keyboard.isPressed(KeyEvent.VK_A))
			{
				player.move_y(0);
				player.move_x(-1);
				player.setState(6);
			}
			else if(keyboard.isPressed(KeyEvent.VK_D))
			{
				player.move_y(-1);
				player.move_x(0);
				player.setState(3);
			}
			else
			{
				player.move_y(-1);
				player.move_x(-1);
				player.setState(1);
			}
		}
		else if(keyboard.isPressed(KeyEvent.VK_S))
		{
			if(keyboard.isPressed(KeyEvent.VK_A))
			{
				player.move_y(1);
				player.move_x(0);
				player.setState(7);
			}
			else if(keyboard.isPressed(KeyEvent.VK_D))
			{
				player.move_y(0);
				player.move_x(1);
				player.setState(4);
			}
			else
			{
				player.move_y(1);
				player.move_x(1);
				player.setState(0);
			}
		}
		else
		{	
			if(keyboard.isPressed(KeyEvent.VK_A))
			{
				player.move_x(-1);
				player.move_y(1);
				player.setState(5);
			}
			else if(keyboard.isPressed(KeyEvent.VK_D))
			{
				player.move_x(1);
				player.move_y(-1);
				player.setState(2);
			}
			else
			{
				player.move_y(0);
				player.move_x(0);
				player.stop();
			}
		}
		

		
		if(keyboard.isPressed(KeyEvent.VK_SPACE))
			player.jump();
	}

}
