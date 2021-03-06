/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import kaninator.mechanics.*;
import kaninator.mechanics.Menu;
import kaninator.graphics.Canvas;
import kaninator.graphics.ImageFactory;
import kaninator.graphics.Text;
import kaninator.io.*;

import java.awt.*;

/**
 * The settings state.
 * The player can change the options for the game in this state.
 * @author phedman
 * @see kaninator.game.GameState
 */
public class Settings extends GameState
{
	private Canvas canvas;
	private Menu menu;
	private Text resolutionOn, resolutionOff, transparencyOn, transparencyOff;
	
	
	/**
	 * Creates the settings menu for the game, screen resolution and transparency settings are set from here.
	 * @param _camera The camera used to render the menu.
	 * @param _gui The gui taking care of the menu.
	 * @param _keyboard Used for input
	 * @param _mouse Used for input
	 * @param _canvas Used to set the size of the window.
	 * @see kaninator.graphics.Canvas
	 */
	public Settings(Camera _camera, GUI _gui, Keyboard _keyboard, Mouse _mouse, Canvas _canvas)
	{
		super(_camera, _gui, _keyboard, _mouse);
		
		menu = new Menu(_gui);
		canvas = _canvas;
		
		menu.setTitle(new Text("Settings!", "Impact", 32, Font.BOLD, Color.WHITE));
		
		resolutionOff = new Text("Resolution: " + canvas.getResWidth() + "x" + canvas.getResHeight(), "Impact", 32, Font.BOLD, Color.WHITE);
		resolutionOn = new Text("Resolution: " + canvas.getResWidth() + "x" + canvas.getResHeight(), "Impact", 32, Font.BOLD, Color.RED);
		
		transparencyOff = new Text("Transparency: " + ((ImageFactory.getTransparency() == Transparency.BITMASK) ? 
										"BITMASK" : "ALPHA"), "Impact", 32, Font.BOLD, Color.WHITE);
		transparencyOn = new Text("Transparency: " + ((ImageFactory.getTransparency() == Transparency.BITMASK) ? 
										"BITMASK" : "ALPHA"), "Impact", 32, Font.BOLD, Color.RED);
		
		menu.addEntry(resolutionOff, resolutionOn);
		menu.addEntry(transparencyOff, transparencyOn);
		menu.addEntry(new Text("Menu!", "Impact", 32, Font.BOLD, Color.WHITE),
						new Text("Menu!", "Impact", 32, Font.BOLD, Color.RED));
	}
	
	/**
	 * Renders the menu to the screen.
	 */
	private void render()
	{
		camera.clearGUI();
		menu.render();
		camera.renderGUI();
	}
	
	/**
	 * The method containing the settings loop.
	 * Returns to the main menu when done.
	 * @see kaninator.game.Main
	 */
	public int doState()
	{
		int retValue = 0;
		int m_x = 0;
		int m_y = 0;
		
		while(true)
		{
			render();
	
			m_x = mouse.get_x();
			m_y = mouse.get_y();
			try {Thread.sleep(Kaninator.FRAME_DELAY);} catch(Exception e){}
			
			menu.setPosition(m_x, m_y);
			
			if(mouse.isPressed(0))
			{
				retValue = menu.select();
				try {Thread.sleep(Kaninator.DEBOUNCE_DELAY);} catch(Exception e){}
				
				if(retValue == 0)
				{
					canvas.changeSize();
					
					resolutionOff.setText("Resolution: " + canvas.getResWidth() + "x" + canvas.getResHeight());
					resolutionOn.setText("Resolution: " + canvas.getResWidth() + "x" + canvas.getResHeight());
				}
				else if(retValue == 1)
				{
					ImageFactory.toggleTransparency();
					
					transparencyOff.setText("Transparency: " + ((ImageFactory.getTransparency()  == Transparency.BITMASK) ? 
											"BITMASK" : "ALPHA"));
					transparencyOn.setText("Transparency: " + ((ImageFactory.getTransparency()  == Transparency.BITMASK) ? 
											"BITMASK" : "ALPHA"));	
				}
				else
				{
					break;
				}
			}
			
		}
		
		camera.clearGUI();
		menu.clear();

		return Kaninator.MAIN_MENU;
	}

}
