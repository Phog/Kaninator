/**
 * Wrapper package for the 2D library used for the graphics.
 */
package kaninator.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.Image;
import java.awt.image.MemoryImageSource;
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
		 * 
		 */
		private static final long serialVersionUID = 1L;

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
					drawable.draw(g, elem.get_x(), elem.get_y() - elem.get_height());
				}
			}
			
			g.dispose();
		}
		
	}
	
	private static final Cursor HIDDEN_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
												Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16, 16, new int[16 * 16], 0, 16)),
												new Point(0, 0),
												"HIDDEN_CURSOR");
	private static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
	
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
	public Screen(JFrame _frame, Dimension size, boolean fullscreen, String title)
	{
		drawList = new LinkedList<VisibleElement>();
		
		frame = _frame;
		canvas = new InternalCanvas();
		canvas.setIgnoreRepaint(true);
		canvas.setDoubleBuffered(true);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(size);
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
	 * Wraps the getWidth() method in JPanel.
	 * @return The width of the actual drawable area.
	 */
	public int getWidth()
	{
		return canvas.getWidth();
	}
	
	/**
	 * Wraps the getHeight() method in JPanel.
	 * @return The height of the actual drawable area.
	 */
	public int getHeight()
	{
		return canvas.getHeight();
	}
	
	/**
	 * Wraps the getHeight() method in JFrame.
	 * @return The height of the window.
	 */
	public int getResHeight()
	{
		return frame.getHeight();
	}
	
	/**
	 * Wraps the getHeight() method in JFrame.
	 * @return The width of the window.
	 */
	public int getResWidth()
	{
		return frame.getWidth();
	}
	
	/**
	 * Sets the size of the window. Effectively the resolution.
	 * @param size The dimension object containing the new size of the window.
	 */
	public void setSize(Dimension size)
	{
		frame.setSize(size);
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
	
	public void hideCursor(boolean hide)
	{
		if(hide)
			canvas.setCursor(HIDDEN_CURSOR);
		else
			canvas.setCursor(DEFAULT_CURSOR);
	}
	
	/**
	 * Sends the repaint signal to the JPanel, effectively drawing the game.
	 */
	public void draw()
	{
		canvas.repaint();
	}
}
