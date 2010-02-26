/**
 * Wrapper package for the 2D library used for the graphics.
 */
package kaninator.graphics;

import java.awt.geom.AffineTransform;
import java.awt.image.*;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The only way to create Animations.
 * Uses a HashMap to keep track of the Animations that have already been loaded in order to avoid duplicates.
 * Also handles error checking internally, effectively returning a dummy Animation if the loading fails.
 * @author phedman
 * @see kaninator.graphics.Animation
 */
public final class AnimationFactory
{
	private static HashMap<String, ArrayList<Animation>> animMap = new HashMap<String, ArrayList<Animation>>();
	
	/**
	 * Internal method which creates an Animation from a BufferedImage containing the animation sheet.
	 * And then adds it to the ArrayList containing the different animation states.
	 * @param buffer The BufferedImage containing the animation sheet.
	 * @param mirror Whether the animation should be mirrored or not, effectively adding the mirrored animation to the ArrayList as well.
	 * @param height The height of a single animation frame.
	 * @param width The width of a single animation frame.
	 * @param column The column of the animation sheet we want to create an Animation from.
	 * @param speed The speed which the created Animation will run at.
	 * @return A single Animation.
	 * @see kaninator.graphics.Animation
	 */
	private static Animation createAnimation(BufferedImage buffer, boolean mirror, int height, int width, int column, double speed)
	{
		ArrayList<Drawable> drawList = new ArrayList<Drawable>();
		if(buffer != null)
		{
			AffineTransform at = AffineTransform.getScaleInstance(-1, 1); 
			at.translate(-width, 0);
			AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			
			int numRow = buffer.getHeight()/height;
			for(int i = 0; i < numRow; i++)
			{
				BufferedImage subImage = buffer.getSubimage(column * width, i * height, width, height);
				drawList.add(new Image((mirror) ? op.filter(subImage, null) : subImage));
			}
		}
	
		return new Animation(drawList, speed);
	}
	
	/**
	 * Internal method that creates an ArrayList of Animations from the sprite sheet contained at the file path. 
	 * Handles errors internally and returns a dummy ArrayList of animations with the size 0 if the sprite sheet cannot be found or read.
	 * @param filepath The path to the image file containing the sprite sheet.
	 * @param mirror Whether the the animations after the first two should be mirrored as well. (The first two are the top/down directions and consequently cannot be mirrored)
	 * @param height The height of a single animation frame.
	 * @param width The width of a single animation frame.
	 * @param speed The speed which the created Animations will run at.
	 * @return  An ArrayList of Animations. If unsuccessful it returns a dummy ArrayList of Animations of the size 0.
	 * @see kaninator.graphics.Animation
	 */
	private static ArrayList<Animation> createAnimations(String filepath, boolean mirror, int height, int width, double speed)
	{
		ArrayList<Animation> returnList = new ArrayList<Animation>();
		try
		{
			if(filepath == null)
				throw new IOException("ERR: File not found: " + filepath);
			
			URL url = returnList.getClass().getResource(filepath);
			if(url == null)
				throw new IOException("ERR: File not found: " + filepath);
			
			BufferedImage buffer = ImageIO.read(url);
			int numCol = buffer.getWidth()/width;
			for(int i = 0; i < numCol; i++)
				returnList.add(createAnimation(buffer, false, height, width, i, speed));
			
			if(mirror && numCol >= 2)
			{
				for(int i = 2; i < numCol; i++)
					returnList.add(createAnimation(buffer, true, height, width, i, speed));
			}
		}
		catch(IOException e)
		{
			System.out.println("ERR: Spritesheet not found: " + filepath + "\n" + e);
		}
		
		return returnList;
	}
	
	
	/**
	 * Updates the transparency values for all the Animations that the AnimationFactory knows about.
	 */
	protected static void updateTransparencies()
	{
		for(ArrayList<Animation> animList : animMap.values())
			for(Animation anim : animList)
				anim.update();
	}
	
	
	/**
	 * Wraps a Drawable in the necessary layers to create a single-frame Animation from it.
	 * Used to simplify the passing of a single Drawable to a DynamicObject.
	 * @param drawable The Drawable object to be wrapped in an Animation.
	 * @return A single frame Animation containing the Drawable.
	 * @see kaninator.graphics.Drawable
	 * @see kaninator.mechanics.DynamicObject
	 * @see kaninator.graphics.Animation
	 */
	public static ArrayList<Animation> createAnimations(Drawable drawable)
	{
		ArrayList<Animation> animList = new ArrayList<Animation>();
		if(drawable != null)
		{
			ArrayList<Drawable> drawList = new ArrayList<Drawable>();
			drawList.add(drawable);
			
			Animation anim = new Animation(drawList, 0.0);
			animList.add(anim);
		}
		
		return animList;
	}
	
	/**
	 * Returns an ArrayList of Animations created from the sprite sheet contained at the file path.
	 * If the ArrayList of Animations previously has been loaded it automatically returns it from the internal HashMap.
	 * If the ArrayList of Animations cannot be found in the HashMap or at the file path, then it returns a dummy ArrayList of Animations of the size 0.
	 * @param filepath The path to the image file containing the sprite sheet.
	 * @param mirror Whether the the animations after the first two should be mirrored as well. (The first two are the top/down directions and consequently cannot be mirrored)
	 * @param height The height of a single animation frame.
	 * @param width The width of a single animation frame.
	 * @param speed The speed which the created Animations will run at.
	 * @return An ArrayList of Animations. If unsuccessful it returns a dummy ArrayList of Animations of the size 0.
	 * @see kaninator.graphics.Animation
	 */
	public static ArrayList<Animation> getAnimations(String filepath, boolean mirror, int height, int width, double speed)
	{
		if(animMap.containsKey(filepath))
			return cloneAnimations(animMap.get(filepath));
		
		ArrayList<Animation> animations = createAnimations(filepath, mirror, height, width, speed);
		
		if(animations != null && animations.size() > 0)
			animMap.put(filepath, animations);
		
		return animations;
	}
	
	/**
	 * Clones an ArrayList of Animations, the cloned ArrayList of Animation uses the same Images as the original one, but has individual states.
	 * @param anims The ArrayList of Animations you want to clone.
	 * @return A cloned ArrayList of Animations.
	 * @see kaninator.graphics.Animation
	 */
	public static ArrayList<Animation> cloneAnimations(ArrayList<Animation> anims)
	{
		ArrayList<Animation> retAnims = new ArrayList<Animation>();
		
		if(anims != null)
			for(Animation anim : anims)
				retAnims.add(anim.clone());
		
		return retAnims;
	}
	
	/**
	 * Main method for testing purposes. Prints every test and if it succeeds, if it fails then it breaks the execution.
	 * @param args Ignored here.
	 */
	public static void main(String args[])
	{
		try
		{
			System.out.println("Testing construction phase...");
			if(animMap == null)
				failedTest("Couldn't construct the HashMap.");
			System.out.print("..");
			
			if(!animMap.isEmpty())
				failedTest("HashMap initially not empty");
			System.out.print("..");
			
			//try to update empty HashMap
			updateTransparencies();
			System.out.println(".. Test Ok!");

			System.out.println("Testing drawable wrapper method...");
			//valid parameters
			ArrayList<Animation> testAnim = createAnimations(new Shadow(32.0));
			if(testAnim == null || testAnim.size() != 1)
				failedTest("Couldn't create valid ArrayList of Animations (Drawable wrapper method)");
			System.out.print("..");
			
			for(Animation anim : testAnim)
			{
				if(anim.getFrames().size() != 1)
					failedTest("Couldn't create valid ArrayList of Animations (Drawable wrapper method)");
				System.out.print("..");
				
				if(anim.getHeight() != 16.0 || anim.getWidth() != 32.0)
					failedTest("Dimensions invalid for Animation (Drawable wrapper method)");
				System.out.print("..");
			}
			
			//null parameter
			ArrayList<Animation> failAnim = createAnimations(null);
			if(failAnim != null && failAnim.size() > 0)
				failedTest("Created invalid Animation instead of a dummy one. (Drawable wrapper method)");
			System.out.println(".. Test Ok!");
			
			System.out.println("Testing spritesheet loading method...");
			//valid parameters
			testAnim = createAnimations("/resources/theSheet.png", true, 64, 64, 0.3);
			if(testAnim == null || testAnim.size() <= 0 || testAnim.size() > 8)
				failedTest("Couldn't create valid ArrayList of Animations (Spritesheet method)");
			System.out.print("..");
			
			for(Animation anim : testAnim)
			{
				if(anim.getFrames().size() != 3)
					failedTest("Couldn't create valid Animation (Spritesheet method)");
				System.out.print("..");
				
				if(anim.getHeight() != 64 || anim.getWidth() != 64)
					failedTest("Dimensions invalid for Animation (Spritesheet method)");
				System.out.print("..");
			}
			
			//null string
			failAnim = createAnimations(null, true, 64, 64, 0.3);
			if(failAnim != null && failAnim.size() > 0)
				failedTest("Created invalid Animation instead of a dummy one. (Spritesheet method)");
			System.out.print("..");
			
			//invalid path
			failAnim = createAnimations("töttöröödz", true, 64, 64, 0.3);
			if(failAnim != null && failAnim.size() > 0)
				failedTest("Created invalid Animation instead of a dummy one. (Spritesheet method)");
			System.out.println(".. Test Ok!");
			
			System.out.println("Testing cloning method...");
			//valid clone
			ArrayList<Animation> cloneAnim = cloneAnimations(testAnim);
			if(cloneAnim == testAnim)
				failedTest("Clone operation failed, references were the same. (Cloning method)");
			System.out.print("..");
			
			if(cloneAnim.size() != testAnim.size())
				failedTest("Cloned Animation different than original. (Cloning method)");
			System.out.print("..");
			
			for(int i = 0; i < testAnim.size(); i++)
			{
				if(testAnim.get(i).getFrames().size() != cloneAnim.get(i).getFrames().size())
					failedTest("Cloned Animation different than original. (Cloning method)");
				System.out.print("..");
				
				if(testAnim.get(i).getHeight() != cloneAnim.get(i).getHeight() ||
						testAnim.get(i).getWidth() != cloneAnim.get(i).getWidth())
					failedTest("Dimensions invalid for cloned Animation (Cloning method)");
				System.out.print("..");
			}
			
			//invalid clone. dummy animation
			failAnim = cloneAnimations(failAnim);
			if(failAnim != null && failAnim.size() > 0)
				failedTest("Clone became an invalid Animation instead of a dummy one. (Cloning method)");
			System.out.print("..");
			
			//invalid clone. null parameter
			failAnim = cloneAnimations(null);
			if(failAnim != null && failAnim.size() > 0)
				failedTest("Clone became an invalid Animation instead of a dummy one. (Cloning method)");
			System.out.println(".. Test Ok!");
			
			System.out.println("Testing HashMap method...");
			//valid parameters
			testAnim = getAnimations("/resources/theSheet.png", true, 64, 64, 0.3);
			if(testAnim == null || testAnim.size() <= 0 || testAnim.size() > 8)
				failedTest("Couldn't create valid animation (HashMap method)");
			System.out.print("..");
			
			for(Animation anim : testAnim)
			{
				if(anim.getFrames().size() != 3)
					failedTest("Couldn't create valid ArrayList of Animations (HashMap method)");
				System.out.print("..");
				
				if(anim.getHeight() != 64 || anim.getWidth() != 64)
					failedTest("Dimensions invalid for animation (HashMap method)");
				System.out.print("..");
			}
			
			if(animMap.size() != 1)
				failedTest("HashMap size invalid (" + animMap.size() + ") (HashMap method)");
			System.out.print("..");
			
			//try to update nonempty HashMap
			updateTransparencies();
			System.out.print("..");
			
			//null string
			failAnim = getAnimations(null, true, 64, 64, 0.3);
			if(failAnim != null && failAnim.size() > 0)
				failedTest("Created invalid Animation instead of a dummy one. (HashMap method)");
			System.out.print("..");
			
			//invalid path
			failAnim = getAnimations("töttöröödz", true, 64, 64, 0.3);
			if(failAnim != null && failAnim.size() > 0)
				failedTest("Created invalid Animation instead of a dummy one. (HashMap method)");
			System.out.print("..");
			
			//valid HashMap clone.
			cloneAnim = getAnimations("/resources/theSheet.png", true, 64, 64, 0.3);
			if(cloneAnim == testAnim)
				failedTest("Clone operation failed, references were the same. (HashMap method)");
			System.out.print("..");
			
			if(cloneAnim.size() != testAnim.size())
				failedTest("Cloned Animation different than original. (HashMap method)");
			System.out.print("..");
			
			for(int i = 0; i < testAnim.size(); i++)
			{
				if(testAnim.get(i).getFrames().size() != cloneAnim.get(i).getFrames().size())
					failedTest("Cloned animation different than original. (HashMap method)");
				System.out.print("..");
				
				if(testAnim.get(i).getHeight() != cloneAnim.get(i).getHeight() ||
						testAnim.get(i).getWidth() != cloneAnim.get(i).getWidth())
					failedTest("Dimensions invalid for cloned Animation (HashMap method)");
				System.out.print("..");
			}
			
			if(animMap.size() != 1)
				failedTest("HashMap size invalid (" + animMap.size() + ") (HashMap method)");
			System.out.println(".. Test Ok!");

			System.out.println("Testing BufferedImage method...");
			//valid parameters
			Animation test = createAnimation(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB), false, 64, 64, 0, 0.33);
			if(test == null || test.getFrames().size() != 1)
				failedTest("Couldn't create valid animation (BufferedImage method)");
			System.out.print("..");
			
			if(test.getHeight() != 64 || test.getWidth() != 64)
				failedTest("Animation dimensions invalid (BufferedImage method)");
			System.out.print("..");
			
			//null parameter
			Animation fail = createAnimation(null, false, 64, 64, 0, 0.33);
			if(fail != null && fail.getFrames().size() > 0)
				failedTest("Created invalid Animation instead of a dummy one. (BufferedImage method)");
			System.out.print("..");
			
			//invalid offsets
			fail = createAnimation(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB), false, 1337, 1337, 15, 0.33);
			if(fail != null && fail.getFrames().size() > 0)
				failedTest("Created invalid Animation instead of a dummy one. (BufferedImage method)");
			System.out.println(".. Test Ok!");
		}
		catch(Exception e)
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
