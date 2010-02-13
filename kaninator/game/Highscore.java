/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import kaninator.mechanics.*;
import kaninator.graphics.*;
import kaninator.io.*;

import java.awt.event.*;
import java.awt.Color;
import java.awt.Font;

/**
 * The settings state.
 * The player can view the high scores in this state.
 * @author phedman
 */
public class Highscore extends GameState
{
	private Menu menu;
	
	public Highscore(Camera _camera, GUI _gui, Keyboard _keyboard, Mouse _mouse)
	{
		super(_camera, _gui, _keyboard, _mouse);
		
		menu = new Menu(_gui);
		
		menu.setTitle(new Text("High Scores!", "Arial", 32, Font.BOLD, Color.WHITE));
		
		menu.addEntry(new Text("Peter - 1337!", "Arial", 32, Font.BOLD, Color.WHITE),
						new Text("Peter - 1337!", "Arial", 32, Font.BOLD, Color.WHITE));
		
		menu.addEntry(new Text("Arno - 6!", "Arial", 32, Font.BOLD, Color.WHITE),
						new Text("Arno - 6!", "Arial", 32, Font.BOLD, Color.WHITE));
		
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
			try {Thread.sleep(66);} catch(Exception e){}
			
			menu.setPosition(m_x, m_y);
			
			if(keyboard.isPressed(KeyEvent.VK_DOWN))
				menu.moveDown();
			if(keyboard.isPressed(KeyEvent.VK_UP))
				menu.moveUp();
			
			if(keyboard.isPressed(KeyEvent.VK_SPACE) || keyboard.isPressed(KeyEvent.VK_ENTER) || mouse.isPressed(0))
				retvalue = menu.select();
			
			if(retvalue == 2)
				break;
			
		}
		
		camera.clearGUI();
		menu.clear();

		return 0;
	}

}
