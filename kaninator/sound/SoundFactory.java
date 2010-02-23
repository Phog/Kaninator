/**
 * Wrapper package for the Java sound library
 */
package kaninator.sound;

import java.awt.Color;
import java.awt.Font;
import java.awt.Transparency;
import java.io.IOException;
import java.util.HashMap;

import kaninator.graphics.AnimationFactory;
import kaninator.graphics.Drawable;
import kaninator.graphics.Image;
import kaninator.graphics.Text;

/**
 * 
 * @author phedman
 */
public class SoundFactory
{
	private static HashMap<String, SoundClip> soundMap = new HashMap<String, SoundClip>();
	private static SoundClip notFound =  new SoundClip();
	
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
			System.out.println("ERR: COULDN'T LOAD SOUND CLIP: "+ filepath + " " + e);
			return notFound;
		}
		
		soundMap.put(filepath, clip);
		return clip;
	}
}
