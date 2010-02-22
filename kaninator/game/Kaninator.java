/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import kaninator.graphics.Image;
import kaninator.graphics.Screen;
import kaninator.mechanics.*;
import kaninator.io.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * This is the main class of the game.
 * The main method is a state machine containing four states: Main, Settings, Highscore and Game.
 * @author phedman
 */
public class Kaninator
{
	public static final int SENTINEL = -1, NEW_GAME = 0, RESUME_GAME = 1, SETTINGS = 2, HIGH_SCORES = 3, MAIN_MENU = 4, GAME_OVER = 5;
	public static final int FRAME_DELAY = 1000/30;
	public static final int DEBOUNCE_DELAY = 1000/5;
	/**
	 * The main function 
	 * @param args The command line parameters passed to the program  
	 */
	public static void main(String args[])
	{
		//All the possible states of the game
		GameState states[] = new GameState[4];
		
		Image background = null;
		try
		{
			background = new Image("/resources/background.jpg");
		}
		catch(Exception e)
		{	
		}
		
		//Creates the depencies used in the different stages
		JFrame frame = new JFrame();
		Screen screen = new Screen(frame, Settings.resolutions[0], false, "Kaninator 0.01");
		GUI gui = new GUI(screen);
		Camera camera = new Camera(screen, gui, background);
		
		Keyboard keyboard = new Keyboard();
		Mouse mouse = new Mouse(screen.getInsets());
		frame.addKeyListener(keyboard);
		frame.addMouseListener(mouse.getMouseKeys());
		frame.addMouseMotionListener(mouse.getMouseMotion());
		
		
		//Initializes the different states
		Game game = null; //The game itself
		Settings settings  = new Settings(camera, gui, keyboard, mouse, screen); //The settings menu
		Highscore highscore = new Highscore(camera, gui, keyboard, mouse, "/resources/highscores.scr"); //The high scores menu
		Main main = new Main(camera, gui, keyboard, mouse); //The main menu
		
		//And store them in the state array
		states[0] = game; 
		states[1] = settings;
		states[2] = highscore;
		states[3] = main;
		
		//Loops through the states until one returns the sentinel
		int stateIndex = MAIN_MENU;
		while(stateIndex > SENTINEL)
		{
			//clear up the keystates to avoid keys getting stuck if the windows goes out of focus
			keyboard.clear();
			mouse.clear();
			switch(stateIndex)
			{
				case RESUME_GAME:
						stateIndex--;
						if(states[0] != null)
							break;
				case NEW_GAME:
						states[0] = game = new Game(camera, gui, keyboard, mouse, screen);
						break;
				case GAME_OVER:
						String name = JOptionPane.showInputDialog(null, "Game Over!", "Enter your name:", JOptionPane.QUESTION_MESSAGE);
						int score = game.getScore();
						highscore.addScore(name, score);
						states[0] = game = null;
						stateIndex = HIGH_SCORES;
				default:
						stateIndex--;
				
			}
			stateIndex = states[stateIndex].doState();
		} 
		
		System.exit(0);
	}
}
