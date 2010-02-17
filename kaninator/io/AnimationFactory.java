/**
 * Input from the keyboard and mouse and file input/output are wrapped
 * in this package to keep the design modular.
 */
package kaninator.io;

import kaninator.graphics.*;

import java.awt.geom.AffineTransform;
import java.awt.image.*;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;


/**
 * @author phedman
 */
public final class AnimationFactory
{
	private static Animation createAnimation(BufferedImage buffer, boolean mirror, int height, int width, int column, double speed)
	{
		ArrayList<Image> drawList = new ArrayList<Image>();
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

	
	public static ArrayList<Drawable> createAnimations(String filepath, boolean mirror, int height, int width, double speed) throws IOException 
	{
		ArrayList<Drawable> returnList = new ArrayList<Drawable>();
		BufferedImage buffer = ImageIO.read(returnList.getClass().getResource(filepath));
		
		int numCol = buffer.getWidth()/width;
		for(int i = 0; i < numCol; i++)
			returnList.add(createAnimation(buffer, false, height, width, i, speed));
		
		if(mirror && numCol >= 2)
		{
			for(int i = 2; i < numCol; i++)
				returnList.add(createAnimation(buffer, true, height, width, i, speed));
		}
		
		return returnList;
	}
}
