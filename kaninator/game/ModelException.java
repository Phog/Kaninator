/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import java.io.IOException;

/**
 * The exception that gets thrown whenever a model is corrupt or
 * cannot be found.
 */
public class ModelException extends IOException
{
	/**
	 * Default serialVersionUID, suppresses a warning.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Needed to construct the exception.
	 * @param string The error string for the exception.
	 */
	public ModelException(String string)
	{
		super(string);
	}	
}
