/**
 * Input from the keyboard and mouse and file input/output are wrapped
 * in this package to keep the design modular.
 */
package kaninator.io;

import java.awt.event.*;
import java.util.HashMap;

/**
 * Wraps the functionality in the awt/swing library and provides information about the keystates.
 * @see java.awt.event.KeyAdapter
 * @author phedman
 */
public class Keyboard extends KeyAdapter
{

	private HashMap<Integer, Boolean> keyStates;
	
	
	/**
	 * Initializes the keystates to false.
	 */
	public Keyboard()
	{
		keyStates = new HashMap<Integer, Boolean>();
	}
	
	/**
	 * Checks if a certain key is pressed at the moment.
	 * @param keyCode The virtual key number for the key in question.
	 * @return the state of the key true=pressed, false=not pressed.
	 */
	public boolean isPressed(int keyCode)
	{
		Boolean state = keyStates.get(keyCode);
		return (state == null) ? false : state.booleanValue();
	}
	
	/**
	 * Overrides the keyReleased method from KeyAdapter, updates the internal keystates accordingly.
	 */
	public void keyReleased(KeyEvent event)
	{
		keyStates.put(event.getKeyCode(), false);
	}
	
	/**
	 * Overrides the keyPressed method from KeyAdapter, updates the internal keystates accordingly.
	 */
	public void keyPressed(KeyEvent event)
	{
		keyStates.put(event.getKeyCode(), true);
	}
	
	/**
	 * Clears the internal keystates, setting them to false.
	 */
	public void clear()
	{
		keyStates.clear();
	}
}
