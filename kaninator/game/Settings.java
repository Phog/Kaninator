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
 * The settings state.
 * The player can change the options for the game in this state.
 * @author phedman
 * @see kaninator.game.GameState
 */
public class Settings extends GameState
{
	public Settings(Camera _camera, GUI _gui, Keyboard _keyboard, Mouse _mouse)
	{
		super(_camera, _gui, _keyboard, _mouse);
	}
	
	
	
	private void renderMenu()
	{
		gui.clearSection(1, 0);
		gui.clearSection(1, 1);
		
		gui.addToSection(new Text("Settings!", "Arial", 32, Font.BOLD, Color.WHITE), 1, 0);
		gui.addToSection(new Text("Töttöröö!", "Arial", 24, Font.BOLD, Color.WHITE), 1, 1);
		gui.addToSection(new Text("Hip hei!", "Arial", 24, Font.BOLD, Color.WHITE), 1, 1);
		gui.addToSection(new Text("Doopah!", "Arial", 24, Font.BOLD, Color.WHITE), 1, 1);
		gui.addToSection(new Text("Back!", "Arial", 24, Font.BOLD,Color.RED), 1, 1);
	}
	
	
	/* (non-Javadoc)
	 * @see kaninator.game.GameState#doState()
	 */
	@Override
	public int doState()
	{

		renderMenu();
		camera.render();
		try {Thread.sleep(166);} catch(Exception e){}
		
		while(!keyboard.isPressed(KeyEvent.VK_SPACE) && !keyboard.isPressed(KeyEvent.VK_ENTER))
		{
			try {Thread.sleep(66);} catch(Exception e){}
		}

		return 0;
	}

}
