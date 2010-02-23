/**
 * Wrapper package for the 2D library used for the graphics.
 */
package kaninator.graphics;

import java.awt.geom.AffineTransform;
import java.awt.image.*;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author phedman
 */
public final class AnimationFactory
{
	private static HashMap<String, ArrayList<Animation>> animMap = new HashMap<String, ArrayList<Animation>>();
	
	private static Animation createAnimation(BufferedImage buffer, boolean mirror, int height, int width, int column, double speed)
	{
		ArrayList<Drawable> drawList = new ArrayList<Drawable>();
		AffineTransform at = AffineTransform.getScaleInstance(-1, 1); 
		at.translate(-width, 0);
		AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		
		int numRow = buffer.getHeight()/height;
		for(int i = 0; i < numRow; i++)
		{
			BufferedImage subImage = buffer.getSubimage(column * width, i * height, width, height);
			drawList.add(new Image((mirror) ? op.filter(subImage, null) : subImage));
		}
	
		return new Animation(drawList, speed);
	}

	public static ArrayList<Animation> createAnimations(Drawable drawable)
	{
		ArrayList<Drawable> drawList = new ArrayList<Drawable>();
		drawList.add(drawable);
		
		Animation anim = new Animation(drawList, 0.0);
		ArrayList<Animation> animList = new ArrayList<Animation>();
		animList.add(anim);
		
		return animList;
	}
	
	private static ArrayList<Animation> createAnimations(String filepath, boolean mirror, int height, int width, double speed)
	{
		ArrayList<Animation> returnList = new ArrayList<Animation>();
		try
			{
			BufferedImage buffer = ImageIO.read(returnList.getClass().getResource(filepath));
			
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
			//TODO: ERROR HANDLING
		}
		
		return returnList;
	}
	
	public static void updateTransparencies()
	{
		for(ArrayList<Animation> animList : animMap.values())
			for(Animation anim : animList)
				anim.update();
	}
	
	public static ArrayList<Animation> getAnimations(String filepath, boolean mirror, int height, int width, double speed)
	{
		if(animMap.containsKey(filepath))
			return cloneAnimations(animMap.get(filepath));
		
		ArrayList<Animation> animations = createAnimations(filepath, mirror, height, width, speed);
		animMap.put(filepath, animations);
		return animations;
	}
	
	public static ArrayList<Animation> cloneAnimations(ArrayList<Animation> anims)
	{
		ArrayList<Animation> retAnims = new ArrayList<Animation>();
		
		for(Animation anim : anims)
			retAnims.add(anim.clone());
		
		return retAnims;
	}
}
