/**
 * Wrapper package for the 2D library used for the graphics.
 */
package kaninator.graphics;

import java.util.HashMap;
import java.awt.Color;
import java.awt.Font;
import java.awt.Transparency;
import java.io.IOException;

/**
 * The only way to create Images.
 * Uses a HashMap to keep track of the Images that have already been loaded in order to avoid duplicates.
 * Also handles error checking internally, effectively returning a dummy Drawable if the loading fails.
 * @author phedman
 * @see kaninator.graphics.Image
 * @see kaninator.graphics.Drawable
 */
public final class ImageFactory
{
	private static HashMap<String, Image> imageMap = new HashMap<String, Image>();
	private static Text notFound =  new Text("Image not found!", "Tahoma", 12, Font.PLAIN, Color.RED);
	private static int transparency = Transparency.BITMASK;
	
	
	/**
	 * Getter for the current transparency value. (TRANSLUCENT or BITMASK)
	 * All images are forced to use this transparency value for performance reasons.
	 * @return An integer representing the current transparency value.
	 */
	public static int getTransparency()
	{
		return transparency;
	}
	
	
	/**
	 * Toggles the transparency value for all the images. (TRANSLUCENT or BITMASK)
	 * Used for peformance reasons, BITMASK is fast on linux, TRANSLUCENT is fast on windows.
	 */
	public static void toggleTransparency()
	{
		if(transparency == Transparency.BITMASK)
			transparency = Transparency.TRANSLUCENT;
		else
			transparency = Transparency.BITMASK;
		
		updateTransparencies();
		AnimationFactory.updateTransparencies();
	}
	
	/**
	 * Returns the Image located at the file path. 
	 * If the Image previously has been loaded it automatically returns it from the internal HashMap.
	 * If the Image cannot be found in the HashMap or at the file path, then it returns a dummy Drawable containing the text "Image not found".
	 * @param filepath The path the image file is located at
	 * @return A Image containing the image in question, or a dummy object if the loading failed.
	 * @see kaninator.graphics.Image
	 * @see kaninator.graphics.Drawable
	 */
	public static Drawable getImage(String filepath)
	{
		Image retImage = imageMap.get(filepath);
		
		if(retImage != null)
			return retImage;
		
		try
		{
			retImage = new Image(filepath);
		}
		catch(IOException e)
		{
			System.out.println("ERR: Image not found: " + filepath + "\n" + e);
			return notFound;
		}
		
		imageMap.put(filepath, retImage);
		return retImage;
	}
	
	
	/**
	 * Updates the transparency values for all the Images that the ImageFactory knows about.
	 */
	private static void updateTransparencies()
	{
		for(Image img : imageMap.values())
			img.update();
	}
}
