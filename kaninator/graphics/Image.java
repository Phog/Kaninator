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
	
	
	/**
	 * Attempts to load an image from a file.
	 * Throws IOException if unsuccessful.
	 * @param filepath Path to the image file
	 * @throws IOException Thrown if the file isn't found or if it is invalid.
	 */
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
	
	public Image(BufferedImage _buffer)
	{
		//Obtain the current system graphical settings
		gfxConf = GraphicsEnvironment.
	    getLocalGraphicsEnvironment().getDefaultScreenDevice().
	    getDefaultConfiguration();
	
		buffer = _buffer;
		moveToVram();
		maintainImg();
	}
	
	/**
	 * Draws the image to the coordinates in the parameters.
	 * @param g The graphics context the image will be drawn to.
	 * @param x The x coordinate for the image
	 * @param y The y coordinate for the image
	 */
	public void draw(Graphics2D g, int x, int y)
	{
		maintainImg();
		g.drawImage(vramImg, x, y, null);
	}

	/**
	 * Getter for the height of the image.
	 * @return The height of the image.
	 */
	public int getHeight()
	{
		return buffer.getHeight();
	}

	/**
	 * Getter for the width of the image.
	 * @return The width of the image.
	 */
	public int getWidth()
	{
		return buffer.getWidth();
	}
	
	/**
	 * Creates a VolatileImage from the BufferedImage.
	 * Effectively copying the image to vram.
	 * @see java.awt.image.VolatileImage
	 * @see java.awt.image.BufferedImage
	 */
	private void moveToVram()
	{
	    //Create new VolatileImage
	    vramImg = gfxConf.createCompatibleVolatileImage(buffer.getWidth(),
	    			buffer.getHeight(), buffer.getTransparency());//Transparency.BITMASK);

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
	
	/**
	 * Checks if the VolatileImage is valid, if not it attempts to recreate it.
	 * @see java.awt.image.VolatileImage
	 * @see java.awt.image.BufferedImage
	 */
	private void maintainImg()
	{
		if(vramImg.contentsLost())
		{
			moveToVram();
			maintainImg();
		}
	}

}
