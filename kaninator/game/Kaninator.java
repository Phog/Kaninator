/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import kaninator.graphics.Screen;
import kaninator.mechanics.*;
import kaninator.io.*;

import java.awt.*;
import javax.swing.JFrame;

/**
 * This is the main class of the game.
 * The main method is a state machine containing four states: Menu, Settings, Highscore and Game.
 * @author phedman
 */
public class Kaninator
{
	private static final int SENTINEL = -1;

	/**
	 * The main function 
	 * @param args The command line parameters passed to the program
	 */
	public static void main(String args[])
	{
		//All the possible states of the game
		GameState states[] = new GameState[2];
		
		//Creates the depencies used in the different stages
		JFrame frame = new JFrame();
		Screen screen = new Screen(frame, 640, 480, false, "Kaninator 0.01");
		GUI gui = new GUI();
		gui.setPadding(25);
		Camera camera = new Camera(screen, gui);
		
		Keyboard keyboard = new Keyboard();
		Mouse mouse = new Mouse();
		frame.addKeyListener(keyboard);
		frame.addMouseListener(mouse.getMouseKeys());
		frame.addMouseMotionListener(mouse.getMouseMotion());
		
		
		//Initializes the different states
		states[0] = new Menu(camera, gui, keyboard, mouse); //The main menu
		states[1] = new Settings(camera, gui, keyboard, mouse); //The settings menu
		
		//Loops through the states until one returns the sentinel
		int stateIndex = 0;
		while(stateIndex != SENTINEL)
		{
			stateIndex = states[stateIndex].doState();
		} 
		
		System.exit(0);
	}
}