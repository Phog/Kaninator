/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import kaninator.mechanics.*;
import kaninator.mechanics.Menu;
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
	private Menu menu;
	
	public Settings(Camera _camera, GUI _gui, Keyboard _keyboard, Mouse _mouse)
	{
		super(_camera, _gui, _keyboard, _mouse);
		
		menu = new Menu(_gui);
		
		menu.setTitle(new Text("Settings!", "Arial", 32, Font.BOLD, Color.WHITE));
		
		menu.addEntry(new Text("Resolution!", "Arial", 32, Font.BOLD, Color.WHITE),
						new Text("Resolution!", "Arial", 32, Font.BOLD, Color.RED));
		
		menu.addEntry(new Text("Fullscreen!", "Arial", 32, Font.BOLD, Color.WHITE),
						new Text("Fullscreen!", "Arial", 32, Font.BOLD, Color.RED));
		
		menu.addEntry(new Text("Töttöröö!", "Arial", 32, Font.BOLD, Color.WHITE),
						new Text("Töttöröö!", "Arial", 32, Font.BOLD, Color.RED));
		
		menu.addEntry(new Text("Menu!", "Arial", 32, Font.BOLD, Color.WHITE),
						new Text("Menu!", "Arial", 32, Font.BOLD, Color.RED));

	}
		
	private void render()
	{
		camera.clearGUI();
		menu.render();
		camera.renderGUI();
	}
	
	
	
	/* (non-Javadoc)
	 * @see kaninator.game.GameState#doState()
	 */
	@Override
	public int doState()
	{
		int retvalue = 0;
		int m_x = 0;
		int m_y = 0;
		
		while(true)
		{
			render();
	
			m_x = mouse.get_x();
			m_y = mouse.get_y();
			try {Thread.sleep(Kaninator.FRAME_DELAY);} catch(Exception e){}
			
			menu.setPosition(m_x, m_y);
			
			if(keyboard.isPressed(KeyEvent.VK_DOWN))
				menu.moveDown();
			if(keyboard.isPressed(KeyEvent.VK_UP))
				menu.moveUp();
			
			if(keyboard.isPressed(KeyEvent.VK_SPACE) || keyboard.isPressed(KeyEvent.VK_ENTER) || mouse.isPressed(0))
				retvalue = menu.select();
			
			if(retvalue == 3)
				break;
			
		}
		
		camera.clearGUI();
		menu.clear();

		return 4;
	}

}
