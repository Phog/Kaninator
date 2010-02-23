/**
 * Wrapper package for the Java sound library
 */
package kaninator.sound;

import java.io.IOException;
import java.util.HashMap;

/**
 * The only way to create SoundClips.
 * Uses a HashMap to keep track of the Clips that have already been loaded in order to avoid duplicates.
 * Also handles error checking internally, effectively returning a dummy SoundClip if the loading fails.
 * @author phedman
 * @see kaninator.sound.SoundClip
 */
public class SoundFactory
{
	private static HashMap<String, SoundClip> soundMap = new HashMap<String, SoundClip>();
	private static SoundClip notFound =  new SoundClip();
	
	/**
	 * Returns the SoundClip located at the file path. 
	 * If the SoundClip previously has been loaded it automatically returns it from the internal HashMap.
	 * If the SoundClip cannot be found in the HashMap or at the file path, then it returns a dummy sound clip containing no data.
	 * @param filepath The path the sound file is located at
	 * @return A SoundClip containing the sound in question, or a dummy object if the loading failed.
	 * @see kaninator.sound.SoundClip
	 */
	public static SoundClip getClip(String filepath)
	{
		SoundClip clip = soundMap.get(filepath);
		
		if(clip != null)
			return clip;
		
		try
		{
			clip = new SoundClip(filepath);
		}
		catch(IOException e)
		{
			System.out.println("ERR: Couldn't load soundclip: "+ filepath + "\n" + e);
			return notFound;
		}
		
		soundMap.put(filepath, clip);
		return clip;
	}
}
