/**
 * Wrapper package for the 2D library used for the graphics.
 */
package kaninator.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
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
	private JFrame frame;
	private BufferStrategy bufferStrat;
	private Color clearColor;
	private LinkedList<VisibleElement> drawList;
	
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
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setTitle(title);
		frame.setResizable(false);
		frame.setVisible(true);
		
		frame.createBufferStrategy(2);
		bufferStrat = frame.getBufferStrategy();
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
		drawList.addLast(elem);
	}

	/**
	 * Empties the drawing queue.
	 */
	/* (non-Javadoc)
	 * @see kaninator.graphics.Canvas#clear()
	 */
	public void clear()
	{
		drawList.clear();
	}
	
	/**
	 * Clears the top of the drawing queue.
	 * @param n The number of elements to clear.
	 */
	public void clearTop(int n)
	{
		for(int i = 0; i < n; i++)
			drawList.removeLast();
	}

	/**
	 * Wraps the getWidth() method in JFrame.
	 * @return The width of the actual drawable area.
	 */
	public int getWidth()
	{
		return frame.getWidth();
	}
	
	/**
	 * Wraps the getHeight() method in JFrame.
	 * @return The height of the actual drawable area.
	 */
	public int getHeight()
	{
		return frame.getHeight();
	}
	
	/**
	 * Draws the VisibleElements contained in the drawing queue in order.
	 */
	/* (non-Javadoc)
	 * @see kaninator.graphics.Canvas#draw()
	 */
	public void draw()
	{
		Graphics2D g = (Graphics2D)bufferStrat.getDrawGraphics();
		
		g.setColor(clearColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		for(VisibleElement elem : drawList)
		{
			//Unwrap the drawable contained in the VisibleElement and draw it to the screen
			Drawable drawable = elem.getDrawable();
			drawable.draw(g, elem.get_x(), elem.get_y());

		}
		
		bufferStrat.show();
		g.dispose();

	}

}
