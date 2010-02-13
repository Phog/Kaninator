/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import kaninator.mechanics.*;
import kaninator.graphics.*;
import kaninator.io.*;

import java.awt.*;
import java.awt.event.*;

/**
 * The main menu state.
 * The first state the game will be in. The user can jump to all the other states from this state.
 * @author phedman
 * @see kaninator.game.GameState
 */
public class Menu extends GameState
{
	private int menuPos;
	private Color colors[];
	
	
	public Menu(Camera _camera, GUI _gui, Keyboard _keyboard, Mouse _mouse)
	{
		super(_camera, _gui, _keyboard, _mouse);
		
		menuPos = 0;
		colors = new Color[4];
	}
	
	
	
	private void renderMenu()
	{
		gui.clearSection(1, 0);
		gui.clearSection(1, 1);
		gui.clearSection(0, 2);
		
		gui.addToSection(new Text("Kaninator!", "Arial", 32, Font.BOLD, Color.WHITE), 1, 0);
		gui.addToSection(new Text("x: " + mouse.get_x() + " y: " + mouse.get_y(), "Arial", 16, Font.PLAIN, Color.BLUE), 0, 2);
		int pos = 0;
		
		gui.addToSection(new Text("New Game!", "Arial", 24, Font.BOLD, colors[pos++]), 1, 1);
		gui.addToSection(new Text("Settings!", "Arial", 24, Font.BOLD, colors[pos++]), 1, 1);
		gui.addToSection(new Text("High Score!", "Arial", 24, Font.BOLD, colors[pos++]), 1, 1);
		gui.addToSection(new Text("Exit!", "Arial", 24, Font.BOLD, colors[pos++]), 1, 1);
	}
	
	
	/* (non-Javadoc)
	 * @see kaninator.game.GameState#doState()
	 */
	@Override
	public int doState()
	{
		
		colors[0] = Color.RED;
		for(int i = 1; i < colors.length; i++)
			colors[i] = Color.WHITE;
		
		menuPos = 0;
		int retvalue = -1;
		renderMenu();
		camera.render();
		
		try {Thread.sleep(166);} catch(Exception e){}
		
		while(true)
		{
			if(keyboard.isPressed(KeyEvent.VK_DOWN))
			{
				
				colors[menuPos] = Color.WHITE;
				
				if(menuPos < colors.length - 1)
					menuPos++;
				
				colors[menuPos] = Color.RED;
				
			}
			if(keyboard.isPressed(KeyEvent.VK_UP))
			{
				
				colors[menuPos] = Color.WHITE;
				
				if(menuPos > 0)
					menuPos--;
				
				colors[menuPos] = Color.RED;

			}
			if(keyboard.isPressed(KeyEvent.VK_SPACE) || keyboard.isPressed(KeyEvent.VK_ENTER))
			{
				if(menuPos == 1)
				{
					retvalue = 1;
					break;
				}
				else if(menuPos == 3)
				{
					break;
				}
			}
			
			renderMenu();
			camera.render();
			try {Thread.sleep(66);} catch(Exception e){}
		}
		
		gui.clearSection(1, 0);
		gui.clearSection(1, 1);
		
		return retvalue;
	}

}
