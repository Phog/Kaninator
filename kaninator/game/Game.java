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
			Drawable image = new Image("/resources/test.png");
			ArrayList<Drawable> list = new ArrayList<Drawable>();
			list.add(image);
			player = new Player(list, 5.0, 5.0, 4.0);
			
			
			Drawable tile = new Image("/resources/tile.png");
			StaticObject obj = new FlatTile(tile, 0.0);
			
			ArrayList<StaticObject> row = new ArrayList<StaticObject>();
			for(int i = 0; i < 32; i++)
				row.add(obj);
			ArrayList<ArrayList<StaticObject>> mapList = new ArrayList<ArrayList<StaticObject>>();
			for(int i = 0; i < 32 ; i++)
				mapList.add(row);
			
			map = new Map(mapList);
		}
		catch(IOException e)
		{
			System.out.println("IMAGE NOT FOUND: /resources/test.png");
		}
	}
	
	/* (non-Javadoc)
	 * @see kaninator.game.GameState#doState()
	 */
	@Override
	public int doState()
	{
		
		gui.addToSection(new Text("Game!!", "Tahoma", 16, Font.BOLD, Color.GREEN), 0, 0);
		
		ArrayList<DynamicObject> playerObjects = player.getDynamicObjects();
		
		for(DynamicObject obj : playerObjects)
			camera.addObject(obj);
		
		camera.setTiles(map.getTiles());
		
		while(true)
		{
			player.update(null);
			
			camera.render();
			camera.renderGUI();
			
			if(keyboard.isPressed(KeyEvent.VK_ESCAPE))
				break;
			
			movePlayer();
			
			try {Thread.sleep(33);} catch(Exception e){}
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
			}
			else if(keyboard.isPressed(KeyEvent.VK_D))
			{
				player.move_y(-1);
				player.move_x(0);
			}
			else
			{
				player.move_y(-1);
				player.move_x(-1);
			}
		}
		else if(keyboard.isPressed(KeyEvent.VK_S))
		{
			if(keyboard.isPressed(KeyEvent.VK_A))
			{
				player.move_y(1);
				player.move_x(0);
			}
			else if(keyboard.isPressed(KeyEvent.VK_D))
			{
				player.move_y(0);
				player.move_x(1);
			}
			else
			{
				player.move_y(1);
				player.move_x(1);
			}
		}
		else
		{	
			if(keyboard.isPressed(KeyEvent.VK_A))
			{
				player.move_x(-1);
				player.move_y(1);
			}
			else if(keyboard.isPressed(KeyEvent.VK_D))
			{
				player.move_x(1);
				player.move_y(-1);
			}
			else
			{
				player.move_y(0);
				player.move_x(0);
			}
		}
		

		
		if(keyboard.isPressed(KeyEvent.VK_SPACE))
			player.jump();
	}

}
