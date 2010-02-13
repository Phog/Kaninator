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
 * The main menu state.
 * The first state the game will be in. The user can jump to all the other states from this state.
 * @author phedman
 * @see kaninator.game.GameState
 */
public class Main extends GameState
{
	private Menu menu;
	
	
	public Main(Camera _camera, GUI _gui, Keyboard _keyboard, Mouse _mouse)
	{
		super(_camera, _gui, _keyboard, _mouse);
		
		menu = new Menu(_gui);
		
		menu.setTitle(new Text("Kaninator!", "Arial", 32, Font.BOLD, Color.WHITE));
		
		menu.addEntry(new Text("New Game!", "Arial", 32, Font.BOLD, Color.WHITE),
						new Text("New Game!", "Arial", 32, Font.BOLD, Color.RED));
		
		menu.addEntry(new Text("Settings!", "Arial", 32, Font.BOLD, Color.WHITE),
						new Text("Settings!", "Arial", 32, Font.BOLD, Color.RED));
		
		menu.addEntry(new Text("High Scores!", "Arial", 32, Font.BOLD, Color.WHITE),
						new Text("High Scores!", "Arial", 32, Font.BOLD, Color.RED));
		
		menu.addEntry(new Text("Exit!", "Arial", 32, Font.BOLD, Color.WHITE),
						new Text("Exit!", "Arial", 32, Font.BOLD, Color.RED));
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
		int m_x, m_y;
		m_x = m_y = 0;
		while(true)
		{
			render();
			
			if(mouse.moved(m_x, m_y))
			{
				m_x = mouse.get_x();
				m_y = mouse.get_y();
			}
			try {Thread.sleep(66);} catch(Exception e){}
			
			menu.setPosition(m_x, m_y);
			
			if(keyboard.isPressed(KeyEvent.VK_DOWN))
				menu.moveDown();
			if(keyboard.isPressed(KeyEvent.VK_UP))
				menu.moveUp();
			
			if(keyboard.isPressed(KeyEvent.VK_SPACE) || keyboard.isPressed(KeyEvent.VK_ENTER) || mouse.isPressed(0))
			{
				retvalue = menu.select();
				break;
			}

			
		}
		
		camera.clearGUI();
		menu.clear();
		
		return (retvalue == 3) ? -1 : retvalue;
	}

}
