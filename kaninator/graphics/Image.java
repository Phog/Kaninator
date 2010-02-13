/**
 * Wrapper package for the 2D library used for the graphics.
 */
package kaninator.graphics;

import java.awt.*;
import java.awt.image.*;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * A drawable image object.
 * Makes it possible to draw images with the drawable interface.
 * Uses VolatileImage from the java awt library.
 * @see kaninator.graphics.Drawable
 * @author phedman
 */
public class Image implements Drawable
{

	private BufferedImage buffer;
	private VolatileImage vramImg;
	private GraphicsConfiguration gfxConf;
	
	public Image(String filepath) throws IOException
	{
		//Obtain the current system graphical settings
		gfxConf = GraphicsEnvironment.
	    getLocalGraphicsEnvironment().getDefaultScreenDevice().
	    getDefaultConfiguration();
	
		buffer = ImageIO.read(this.getClass().getResource(filepath));
		moveToVram();
		maintainImg();
	}
	
	/* (non-Javadoc)
	 * @see kaninator.graphics.Drawable#draw(java.awt.Graphics2D, int, int)
	 */
	@Override
	public void draw(Graphics2D g, int x, int y)
	{
		maintainImg();
		g.drawImage(vramImg, x, y, null);
	}

	/* (non-Javadoc)
	 * @see kaninator.graphics.Drawable#getHeight()
	 */
	@Override
	public int getHeight()
	{
		return buffer.getHeight();
	}

	/* (non-Javadoc)
	 * @see kaninator.graphics.Drawable#getWidth()
	 */
	@Override
	public int getWidth()
	{
		return buffer.getWidth();
	}

	/* (non-Javadoc)
	 * @see kaninator.graphics.Drawable#reset()
	 */
	@Override
	public void reset()
	{
	}
	
	private void moveToVram()
	{
	    //Create new VolatileImage
	    vramImg = gfxConf.createCompatibleVolatileImage(buffer.getWidth(),
	    			buffer.getHeight(), buffer.getTransparency());

	    //Get drawing context into the image
	    Graphics2D g2d = (Graphics2D) vramImg.getGraphics();
	    
	    //Make transparent images possible
	    g2d.setComposite(AlphaComposite.Src);
	    g2d.setColor(new Color(0,0,0,0));
	    g2d.fillRect(0, 0, vramImg.getWidth(), vramImg.getHeight());
	
	    //Actually draw the image and dispose of context no longer needed
	    g2d.drawImage(buffer, 0, 0, null);
	    g2d.dispose();
	}
	
	private void maintainImg()
	{
		if(vramImg.contentsLost())
		{
			moveToVram();
			maintainImg();
		}
	}

}
