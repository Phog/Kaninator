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
 * @author phedman
 */
public final class ImageFactory
{
	private static HashMap<String, Image> imageMap = new HashMap<String, Image>();
	private static Text notFound =  new Text("Image not found!", "Tahoma", 12, Font.PLAIN, Color.RED);
	private static int transparency = Transparency.BITMASK;
	
	public static int getTransparency()
	{
		return transparency;
	}
	
	public static void toggleTransparency()
	{
		if(transparency == Transparency.BITMASK)
			transparency = Transparency.TRANSLUCENT;
		else
			transparency = Transparency.BITMASK;
		
		updateTransparencies();
		AnimationFactory.updateTransparencies();
	}
	
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
			System.out.println("ERR: IMAGE NOT FOUND: "+ filepath + " " + e);
			return notFound;
		}
		
		imageMap.put(filepath, retImage);
		return retImage;
	}
	
	private static void updateTransparencies()
	{
		for(Image img : imageMap.values())
			img.update();
	}
}
