/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import kaninator.mechanics.*;
import kaninator.io.*;
/**
 * An abstract class for the state machine.
 * It contains all the connections the different states requires and
 * the interface they need to implement.
 * @author phedman
 */
public abstract class GameState
{
	protected Camera camera;
	protected GUI gui;
	protected Keyboard keyboard;
	protected Mouse mouse;

	/**
	 * Initializes the dependencies for objects inheriting this class.
	 * @param _camera Used for rendering the internal objects to the screen.
	 * @param _gui Used for manipulating an overlay GUI.
	 * @param _keyboard Used for getting user input from the keyboard.
	 * @param _mouse Used for getting user input from the mouse.
	 */
	public GameState(Camera _camera, GUI _gui, Keyboard _keyboard, Mouse _mouse)
	{
		camera = _camera;
		gui = _gui;
		keyboard = _keyboard;
		mouse = _mouse;
	}
	
	
	/**
	 * Performs the required operation of this state.
	 * @return The index to the next state to be performed.
	 */
	public abstract int doState();
}
