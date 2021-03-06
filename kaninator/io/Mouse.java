/**
 * Input from the keyboard and mouse and file input/output are wrapped
 * in this package to keep the design modular.
 */
package kaninator.io;

import java.awt.Dimension;
import java.awt.event.*;

/**
 * Wraps the functionality in the awt/swing library and provides information about the keystates and the cursor position.
 * @see java.awt.event.MouseAdapter
 * @see java.awt.event.MouseMotionAdapter
 * @author phedman
 */
public class Mouse
{
	private boolean keyStates[];
	private int x, y;
	private MouseKeys mouseKeys;
	private MouseMotion mouseMotion;
	private Dimension insets;
	
	/**
	 * Extends the MouseAdapter class, stores the keystates in an boolean array.
	 * @see MouseAdapter
	 * @author phedman
	 */
	private class MouseKeys extends MouseAdapter
	{
		/**
		 * Overrides the mousePressed method, stores the keystates in an array of booleans.
		 */
		public void mousePressed(MouseEvent event)
		{
			switch(event.getButton())
			{
				case MouseEvent.BUTTON1:
					keyStates[0] = true;
					break;
				case MouseEvent.BUTTON2:
					keyStates[1] = true;
					break;
				case MouseEvent.BUTTON3:
					keyStates[2] = true;
					break;
			}
		}
		
		/**
		 * Overrides the mouseReleased method, stores the keystates in an array of booleans.
		 */
		public void mouseReleased(MouseEvent event)
		{
			switch(event.getButton())
			{
				case MouseEvent.BUTTON1:
					keyStates[0] = false;
					break;
				case MouseEvent.BUTTON2:
					keyStates[1] = false;
					break;
				case MouseEvent.BUTTON3:
					keyStates[2] = false;
					break;
			}
		}
	}
	
	
	/**
	 * Extends the MouseMotionAdapter class, stores the coordinates in integers.
	 * @author phedman
	 */
	private class MouseMotion extends MouseMotionAdapter
	{
		/**
		 * Overrides the mouseDragged method, stores the coordinates in integers.
		 * This method needs to be overriden as well as mouseMoved if we want to
		 * capture the mouse postion regardless if the buttons are pressed or not.
		 */
		public void mouseDragged(MouseEvent event)
		{
			x = event.getX();
			y = event.getY();
		}
		
		/**
		 * Overrides the mouseMoved method, stores the coordinates in integers.
		 * This method needs to be overriden as well as mouseDragged if we want to
		 * capture the mouse postion regardless if the buttons are pressed or not.
		 */
		public void mouseMoved(MouseEvent event)
		{
			x = event.getX();
			y = event.getY();
		}
	}
	
	/**
	 * Accessor method for the MouseKeys object.
	 * Used to communicate with the window we want to get events from.
	 * @return The MouseKeys object extending the MouseAdapter class.
	 */
	public MouseKeys getMouseKeys()
	{
		return mouseKeys;
	}
	
	/**
	 * Accessor method for the MouseMotion object.
	 * Used to communicate with the window we want to get events from.
	 * @return The MouseMotion object extending the MouseMotionAdapter class.
	 */
	public MouseMotion getMouseMotion()
	{
		return mouseMotion;
	}
	
	
	/**
	 * Creates all the member objects and initializes all the variables to 0 and false.
	 */
	public Mouse(Dimension _insets)
	{
		x = y = 0;
		keyStates = new boolean[3];
		insets = _insets;
		
		for(int i = 0; i < keyStates.length; i++)
			keyStates[i] = false;
		
		mouseKeys = new MouseKeys();
		mouseMotion = new MouseMotion();
	}
	
	/**
	 * Checks if a certain button is pressed at the moment.
	 * @param button The mouse button in question (0 - 2).
	 * @return the state of the button true=pressed, false=not pressed.
	 */
	public boolean isPressed(int button)
	{
		if(button < 0 || button >= keyStates.length)
			return false;
		
		return keyStates[button];
	}
	
	/**
	 * Getter method for the x-coordinate.
	 * @return The x-coordinate of the cursor in relation to the window.
	 */
	public int get_x()
	{
		return x - insets.width;
	}

	/**
	 * Getter method for the y-coordinate.
	 * @return The y-coordinate of the cursor in relation to the window.
	 */
	public int get_y()
	{
		return y - insets.height;
	}
	
	/**
	 * Checks against the values in the parameters if the mouse has been moved.
	 * @param _x The old x value to check against
	 * @param _y The old y value to check against
	 * @return True if the mouse has moved, otherwise false
	 */
	public boolean moved(int _x, int _y)
	{
		if(_x != x || _y != y)
			return true;
		else
			return false;
	}
	
	/**
	 * Clears the internal button states, setting them to false.
	 */
	public void clear()
	{
		for(int i = 0; i < keyStates.length; i++)
			keyStates[i] = false;
	}

}
