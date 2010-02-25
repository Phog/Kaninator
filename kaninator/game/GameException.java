package kaninator.game;

/**
 * The exception that gets thrown whenever exceptions occurred in the game state.
 */
public class GameException extends Exception
{
	/**
	 * Default serialVersionUID, suppresses a warning.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Needed to construct the exception.
	 * @param string The error string for the exception.
	 */
	public GameException(String string)
	{
		super(string);
	}
}
