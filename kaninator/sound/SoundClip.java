/**
 * Wrapper package for the Java sound library
 */
package kaninator.sound;

import javax.sound.sampled.*;

import java.io.*;


/**
 * Primitive sound clip class for sound effects.
 * @author phedman
 */
public class SoundClip
{
	Clip clip;
	/**
	 * Attempts to load a sound clip from a file
	 * Throws IOException if unsuccessful.
	 * @param filepath Path to the sound file
	 * @throws IOException Thrown if the file isn't found, if it is invalid or can't be loaded. 
	 */
	public SoundClip(String filepath) throws IOException
	{
		try
		{
		    File soundFile = new File(this.getClass().getResource(filepath).getPath());
		    AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);
		    DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
		    clip = (Clip)AudioSystem.getLine(info);
		    clip.open(sound);
		}
		catch(UnsupportedAudioFileException e)
		{
			System.out.println("Sound format invalid: " + e);
			throw new IOException("Invalid format");
		}
		catch(LineUnavailableException e)
		{
			System.out.println("Couldn't reserve sound line: " + e);
			throw new IOException("Couldn't reserve sound line");
		}
	}
	
	public void playClip()
	{
		if(clip != null)
		{
			clip.setFramePosition(0);
			clip.start();
		}
	}
}
