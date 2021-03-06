/**
 * Wrapper package for the 2D library used for the graphics.
 */
package kaninator.graphics;

import javax.swing.*;
import java.awt.*;
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
	/**
	 * The canvas onto which the game will be drawn.
	 * Extending JPanel turned out to be much faster than using BufferStrategy.
	 * @author phedman
	 * @see javax.swing.JPanel
	 */
	private class InternalCanvas extends JPanel
	{
		/**
		 * Default serialVersionUID.
		 * Suppresses a warning.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Draws the VisibleElements contained in the drawing queue in order.
		 */
		public void paintComponent(Graphics graphics)
		{
			super.paintComponent(graphics);
			Graphics2D g = (Graphics2D)graphics;
			
			g.setColor(clearColor);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			synchronized(canvas)
			{
				for(VisibleElement elem : drawList)
				{
					//Unwrap the drawable contained in the VisibleElement and draw it to the screen
					Drawable drawable = elem.getDrawable();
					drawable.draw(g, elem.get_x(), elem.get_y() - elem.getHeight());
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
	private Dimension sizes[];
	private int resIndex;

	
	/**
	 * Creates and initializes a window and shows it on the screen.
	 * @param _sizes An array of Dimensions containing the allowed resolutions
	 * @param title The window title
	 * @see java.awt.Dimension
	 */
	public Screen(JFrame _frame, Dimension _sizes[], String title)
	{
		resIndex = 0;
		sizes = _sizes;
		drawList = new LinkedList<VisibleElement>();
		
		frame = _frame;
		canvas = new InternalCanvas();
		canvas.setIgnoreRepaint(true);
		canvas.setDoubleBuffered(true);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(sizes[resIndex]);
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
	 * Changes the window size according to the values in an internal array.
	 */
	public void changeSize()
	{
		resIndex++;
		if(resIndex >= sizes.length)
			resIndex = 0;
		
		frame.setSize(sizes[resIndex]);
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
	
	/**
	 * Hides alternatively shows the mouse cursor when above the window.
	 * @param hide True to hide, false to show the cursor.
	 */
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
