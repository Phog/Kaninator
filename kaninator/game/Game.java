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
			Drawable flat = new Image("/resources/flat.png");
			Drawable ne = new Image("/resources/ne.png");
			Drawable nw = flat;
			Drawable n = new Image("/resources/n.png");
			Drawable e = new Image("/resources/e.png");
			Drawable w = new Image("/resources/w.png");
			Drawable se = new Image("/resources/se.png");
			Drawable sw = new Image("/resources/sw.png");
			Drawable s = new Image("/resources/s.png");

			
			StaticObject lowFlat = new FlatTile(flat, 0.0);
			StaticObject highFlat = new FlatTile(flat, 32.0);
			StaticObject higherFlat = new FlatTile(flat, 64.0);
			
			StaticObject upNe = new NEastSlope(ne, 32.0);
			StaticObject upNw = new NWestSlope(nw, 32.0);
			StaticObject upN = new NorthSlope(n, 32.0);
			StaticObject upE = new EastSlope(e, 32.0);
			StaticObject upW = new WestSlope(w, 32.0);
			StaticObject upSe = new SEastSlope(se, 32.0);
			StaticObject upSw = new SWestSlope(sw, 32.0);
			StaticObject upS = new SouthSlope(s, 32.0);
			
			ArrayList<ArrayList<StaticObject>> mapList = new ArrayList<ArrayList<StaticObject>>();
			ArrayList<StaticObject> row;
			int intArray[][] = {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
								{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
								{0, 0, 0, 0, 0,17, 9, 9, 9, 9, 9, 9, 9, 0, 0, 0, 0, 0, 0, 0},
								{0, 0, 0, 0, 0,36,53,45,45,45,45,45,20, 0, 0, 0, 0, 0, 0, 0},
								{0, 0, 0, 0, 0,36,72,89,81,81,81,56,18, 0, 0, 0, 0, 0, 0, 0},
								{0, 0, 0, 0, 0,36,72,108,125,117,92,54,18, 0, 0, 0, 0, 0, 0, 0},
								{0, 0, 0, 0, 0,36,72,114,117,117,90,54,18, 0, 0, 0, 0, 0, 0, 0},
								{0, 0, 0, 0, 0,36,78,99,99,103,90,54,18, 0, 0, 0, 0, 0, 0, 0},
								{0, 0, 0, 0, 0,42,63,63,63,63,67,54,18, 0, 0, 0, 0, 0, 0, 0},
								{0, 0, 0, 0, 0,27,27,27,27,27,27,31,18, 0, 0, 0, 0, 0, 0, 0},
								{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
								{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
								{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
			for(int rowArray[] : intArray)
			{
				row = new ArrayList<StaticObject>();
				for(int tile : rowArray)
				{
					double height = tile / 9 * 32;
					switch(tile % 9)
					{
						case 0:
							row.add(new FlatTile(flat, height));
							break;
						case 1:
							row.add(new NWestSlope(nw, height));
							break;
						case 2:
							row.add(new NorthSlope(n, height));
							break;
						case 3:
							row.add(new NEastSlope(ne, height));
							break;
						case 4:
							row.add(new EastSlope(e, height));
							break;
						case 5:
							row.add(new SEastSlope(se, height));
							break;
						case 6:
							row.add(new SouthSlope(s, height));
							break;
						case 7:
							row.add(new SWestSlope(sw, height));
							break;
						case 8:
							row.add(new WestSlope(w, height));
							break;
					}
				}
				mapList.add(row);
			}
			
			map = new Map(mapList);
			
			Drawable image = new Image("/resources/test.png");
			ArrayList<Drawable> list = new ArrayList<Drawable>();
			list.add(image);
			player = new Player(list, map, 0, 0, 4.0);
		}
		catch(IOException e)
		{
			System.out.println("IMAGE NOT FOUND: " + e);
		}
	}
	
	/* (non-Javadoc)
	 * @see kaninator.game.GameState#doState()
	 */
	@Override
	public int doState()
	{
		
		gui.addToSection(new Text("Game!!", "Tahoma", 16, Font.BOLD, Color.GREEN), 0, 0);
		
		camera.setPlayer(player.getDynamicObjects());
		camera.setTiles(map.getTiles());
		
		while(true)
		{
			player.update(null);
			camera.follow(player.getDynamicObjects().get(0));
			
			camera.render();
			camera.renderGUI();
			
			if(keyboard.isPressed(KeyEvent.VK_ESCAPE))
				break;
			
			movePlayer();
			
			try {Thread.sleep(1000/30);} catch(Exception e){}
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
