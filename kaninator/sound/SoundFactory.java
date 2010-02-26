/**
 * Wrapper package for the Java sound library
 */
package kaninator.sound;

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
		catch(Exception e)
		{
			System.out.println("ERR: Couldn't load soundclip: "+ filepath + "\n" + e);
			return notFound;
		}
		
		soundMap.put(filepath, clip);
		return clip;
	}
	
	/**
	 * Main method for testing purposes. Prints every test and if it succeeds, if it fails then it breaks the execution.
	 * @param args Ignored here.
	 */
	public static void main(String[] args)
	{
		try
		{
			System.out.println("Testing construction phase..");	
			if (soundMap == null)
				failedTest("Couldn't construct the HashMap.");
			System.out.print("..");
			
			if (!soundMap.isEmpty())
				failedTest("HashMap initially not empty.");
			System.out.print("..");
			
			if (notFound == null)
				failedTest("Failed creating the dummy sound.");
			System.out.println(".. Test Ok!");
			
			System.out.println("Testing getClip method..");
			//valid call
			SoundClip clip = getClip("/resources/ow.wav");
			if(clip == null)
				failedTest("getClip returned null instead of valid sound.");
			System.out.print("..");
			
			if(soundMap.size() != 1)
				failedTest("HashMap has an invalid size after getClip: (" + soundMap.size() + ")");
			System.out.print("..");
			
			//valid clone
			SoundClip clone = getClip("/resources/ow.wav");
			if(clone == null)
				failedTest("getClip returned null instead of valid clone.");
			System.out.print("..");
			
			if(clone != clip)
				failedTest("getClip returned new clip instead of clone.");
			System.out.print("..");
			
			if(soundMap.size() != 1)
				failedTest("HashMap has an invalid size after getClip clone: (" + soundMap.size() + ")");
			System.out.println(".. Test Ok!");
			
			//invalid call: invalid string
			SoundClip fail = getClip("GÖFÖÖÖÖ DJDJÖ MÖRM:");
			if(fail == null)
				failedTest("getClip returned null clip instead of dummy clip when passed invalid string.");
			System.out.print("..");
			
			if(fail != notFound)
				failedTest("getClip didn't return dummy clip when passed invalid string.");
			System.out.print("..");
			
			//invalid call: null string
			fail = getClip(null);
			if(fail == null)
				failedTest("getClip returned null clip instead of dummy clip when passed null string.");
			System.out.print("..");
			
			if(fail != notFound)
				failedTest("getClip didn't return dummy clip when passed null string.");
			System.out.print("..");
			
			if(soundMap.size() != 1)
				failedTest("HashMap has an invalid size after invalid getClip calls: (" + soundMap.size() + ")");
			System.out.println(".. Test Ok!");
				
		}
		catch (Exception e)
		{
			failedTest("Unknown exception: " + e);
		}
		System.out.println("TESTS: OK");
	}
	
	/**
	 * Gets called if a test fails. Testing purposes only. Prints out the failed test and exits the program.
	 * @param test A string describing the test that failed.
	 */
	private static void failedTest(String test)
	{
		System.out.println("TEST FAILED: " + test);
		System.exit(0);
	}
}
