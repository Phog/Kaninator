/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import kaninator.graphics.Screen;
import kaninator.mechanics.*;
import kaninator.io.*;
import javax.swing.JFrame;

/**
 * This is the main class of the game.
 * The main method is a state machine containing four states: Main, Settings, Highscore and Game.
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
		GameState states[] = new GameState[4];
		
		//Creates the depencies used in the different stages
		JFrame frame = new JFrame();
		Screen screen = new Screen(frame, 800, 480, false, "Kaninator 0.01");
		GUI gui = new GUI(screen.getWidth(), screen.getHeight());
		Camera camera = new Camera(screen, gui);
		
		Keyboard keyboard = new Keyboard();
		Mouse mouse = new Mouse(screen.getInsets());
		frame.addKeyListener(keyboard);
		frame.addMouseListener(mouse.getMouseKeys());
		frame.addMouseMotionListener(mouse.getMouseMotion());
		
		
		//Initializes the different states
		states[0] = null;//new Game(camera, gui, keyboard, mouse); //The game itself
		states[1] = new Settings(camera, gui, keyboard, mouse); //The settings menu
		states[2] = new Highscore(camera, gui, keyboard, mouse); //The high scores menu
		states[3] = new Main(camera, gui, keyboard, mouse); //The main menu
		
		//Loops through the states until one returns the sentinel
		int stateIndex = 3;
		while(stateIndex > SENTINEL)
		{
			stateIndex = states[stateIndex].doState();
			if(stateIndex == 0 || stateIndex == 1 && states[0] == null)
				states[0] = new Game(camera, gui, keyboard, mouse); //The game itself
			if(stateIndex != 0)
				stateIndex--;

		} 
		
		System.exit(0);
	}
}
