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
	public static int forceTransparency = Transparency.BITMASK;
	public static Dimension resolutions[]  = {new Dimension(640, 480),
										new Dimension(800, 480),
										new Dimension(800, 600),
										new Dimension(1024, 600),
										new Dimension(1024, 768)};
	private Screen screen;
	private Menu menu;
	private Text resolutionOn, resolutionOff, transparencyOn, transparencyOff;
	private int resIndex;
	
	public Settings(Camera _camera, GUI _gui, Keyboard _keyboard, Mouse _mouse, Screen _screen)
	{
		super(_camera, _gui, _keyboard, _mouse);
		
		menu = new Menu(_gui);
		screen = _screen;
		
		menu.setTitle(new Text("Settings!", "Impact", 32, Font.BOLD, Color.WHITE));
		
		resolutionOff = new Text("Resolution: " + screen.getResWidth() + "x" + screen.getResHeight(), "Impact", 32, Font.BOLD, Color.WHITE);
		resolutionOn = new Text("Resolution: " + screen.getResWidth() + "x" + screen.getResHeight(), "Impact", 32, Font.BOLD, Color.RED);
		
		transparencyOff = new Text("Transparency: " + ((forceTransparency == Transparency.BITMASK) ? 
										"BITMASK" : "ALPHA"), "Impact", 32, Font.BOLD, Color.WHITE);
		transparencyOn = new Text("Transparency: " + ((forceTransparency == Transparency.BITMASK) ? 
										"BITMASK" : "ALPHA"), "Impact", 32, Font.BOLD, Color.RED);
		
		menu.addEntry(resolutionOff, resolutionOn);
		menu.addEntry(transparencyOff, transparencyOn);
		menu.addEntry(new Text("Menu!", "Impact", 32, Font.BOLD, Color.WHITE),
						new Text("Menu!", "Impact", 32, Font.BOLD, Color.RED));
		resIndex = 0;

	}
		
	private void render()
	{
		camera.clearGUI();
		menu.render();
		camera.renderGUI();
	}
	
	
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
					resIndex++;
					if(resIndex >= resolutions.length)
						resIndex = 0;
					
					screen.setSize(resolutions[resIndex]);
					
					resolutionOff.setText("Resolution: " + screen.getResWidth() + "x" + screen.getResHeight());
					resolutionOn.setText("Resolution: " + screen.getResWidth() + "x" + screen.getResHeight());
				}
				else if(retValue == 1)
				{
					if(forceTransparency == Transparency.BITMASK)
						forceTransparency = Transparency.TRANSLUCENT;
					else
						forceTransparency = Transparency.BITMASK;
					
					transparencyOff.setText("Transparency: " + ((forceTransparency == Transparency.BITMASK) ? 
											"BITMASK" : "ALPHA"));
					transparencyOn.setText("Transparency: " + ((forceTransparency == Transparency.BITMASK) ? 
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
