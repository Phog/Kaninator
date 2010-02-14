/**
 * Wrapper package for the 2D library used for the graphics.
 */
package kaninator.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

import java.util.List;
import java.util.*;

/**
 * Implements the Canvas interface with the Java2D library.
 * Takes a number of VisibleElement objects and draws them to the screen with the Java2D API.
 * Also responsible for creating and initializing the window.
 * @see kaninator.graphics.Canvas
 * @see kaninator.graphics.VisibleElement
 * @see javax.swing.JFrame
 * @author phedman
 */
public class Screen implements Canvas
{	

	private class InternalCanvas extends JPanel
	{
		/**
		 * Draws the VisibleElements contained in the drawing queue in order.
		 */
		public void paintComponent(Graphics graphics)
		{
			super.paintComponent(graphics);
			Graphics2D g = (Graphics2D)graphics;//bufferStrat.getDrawGraphics();
			
			g.setColor(clearColor);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			synchronized(canvas)
			{
				for(VisibleElement elem : drawList)
				{
					//Unwrap the drawable contained in the VisibleElement and draw it to the screen
					Drawable drawable = elem.getDrawable();
					drawable.draw(g, elem.get_x(), elem.get_y());
				}
			}
			
			g.dispose();
		}
		
	}
	
	private JFrame frame;
	private Color clearColor;
	private LinkedList<VisibleElement> drawList;
	private InternalCanvas canvas;
	
	/**
	 * Creates and initializes a window and shows it on the screen.
	 * @param width Window width
	 * @param height Window height
	 * @param fullscreen Fullscreen mode? true/false
	 * @param title The window title
	 */
	public Screen(JFrame _frame, int width, int height, boolean fullscreen, String title)
	{
		drawList = new LinkedList<VisibleElement>();
		
		frame = _frame;
		canvas = new InternalCanvas();
		canvas.setIgnoreRepaint(true);
		canvas.setDoubleBuffered(true);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setTitle(title);
		frame.add(canvas);
		
		frame.setResizable(false);
		frame.setVisible(true);
	
		clearColor = new Color(0, 0, 0);
	}

	/**
	 * Adds a VisibleElement to the tail of the drawing queue.
	 * @see kaninator.graphics.VisibleElement
	 */
	/* (non-Javadoc)
	 * @see kaninator.graphics.Canvas#addDrawable(kaninator.graphics.Drawable)
	 */
	public void addElement(VisibleElement elem)
	{
		synchronized(canvas)
		{
			drawList.addLast(elem);
		}
	}

	/**
	 * Empties the drawing queue.
	 */
	public void clear()
	{
		synchronized(canvas)
		{
			drawList.clear();
		}
	}
	
	/**
	 * Clears the top of the drawing queue.
	 * @param n The number of elements to clear.
	 */
	public void clearTop(int n)
	{
		synchronized(canvas)
		{
			for(int i = 0; i < n; i++)
				drawList.removeLast();
		}
	}

	/**
	 * Wraps the getWidth() method in JFrame.
	 * @return The width of the actual drawable area.
	 */
	public int getWidth()
	{
		return canvas.getWidth();
	}
	
	/**
	 * Wraps the getHeight() method in JFrame.
	 * @return The height of the actual drawable area.
	 */
	public int getHeight()
	{
		return canvas.getHeight();
	}
	
	/**
	 * Getter for the window insets
	 * @return The top/left border size.
	 */
	public Dimension getInsets()
	{
		Dimension insets = new Dimension();
		
		insets.width = (frame.getWidth() - canvas.getWidth()) / 2;
		insets.height = frame.getHeight() - canvas.getHeight() - insets.width;
		
		return insets;
	}
	
	public void draw()
	{
		canvas.repaint();
	}

}
