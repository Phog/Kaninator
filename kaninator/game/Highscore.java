/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import kaninator.mechanics.*;
import kaninator.graphics.*;
import kaninator.io.*;

import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 * The High score state.
 * The player can view the high scores in this state.
 * This class also implements the addScore method, responsible for adding new
 * high scores to the high scores file.
 * @author phedman
 */
public class Highscore extends GameState
{
	private static final int MAX_SCORES = 6;
	
	/**
	 * Nested class used for storing the HighScores and sorting them
	 * easily.
	 * @author phedman
	 */
	private class Score implements Comparable<Score>
	{
		String name;
		int score;
		
		public String toString()
		{
			return name + " " + score;
		}
		
		public int compareTo(Score other)
		{
			if(score > other.score)
				return -1;
			else if(score < other.score)
				return 1;
			else
				return 0;
		}
	}
	
	private ArrayList<Score> scores;
	private Menu menu;
	private String path;

	
	/**
	 * Creates a new high scores object and tries to load the latest scores from the file at the path provided in the parameters.
	 * @param _camera The camera class used to render the menu to the screen.
	 * @param _gui The gui class used for overlays.
	 * @param _keyboard The keyboard class for key input.
	 * @param _mouse Mouse input.
	 * @param filepath The file path at which the high scores file resides.
	 */
	public Highscore(Camera _camera, GUI _gui, Keyboard _keyboard, Mouse _mouse, String filepath)
	{
		super(_camera, _gui, _keyboard, _mouse);
		
		menu = new Menu(_gui);
		scores = new ArrayList<Score>();
		
		menu.setTitle(new Text("High Scores!", "Impact", 32, Font.BOLD, Color.WHITE));
		
		URL url = this.getClass().getResource(filepath);
		path = (url == null) ? filepath : url.getPath();
		
		readScores();
	}
		
	/**
	 * Attempts to read the high scores from the file located at [path].
	 */
	private void readScores()
	{
		scores.clear();
		Scanner input;
		try
		{
			input = new Scanner(new File(path));
		}
		catch(FileNotFoundException e)
		{
			System.out.println("ERR: High score file not found: " + e);
			System.out.println("Ignoring...");
			return;
		}
		
		while(input.hasNext())
		{
			String line = input.nextLine();
			Scanner lineScanner = new Scanner(line);
			
			Score lineScore = new Score();
			lineScore.score = 0;
			lineScore.name = "";
			
			while(lineScanner.hasNext())
			{
				if(lineScanner.hasNextInt())
				{
					lineScore.score = lineScanner.nextInt();
					break;
				}
				else
				{
					lineScore.name += lineScanner.next() + " ";
				}
			}
			
			scores.add(lineScore);
		}
		
	}
	
	
	/**
	 * Attempts to write the current scores into the file located at [path].
	 */
	private void writeScores()
	{
		PrintWriter scoreFile;
		try
		{
			scoreFile = new PrintWriter(new File(path));
		}
		catch(FileNotFoundException e)
		{
			System.out.println("ERR: Could not create/edit high score file: " + e);
			return;
		}
		
		for(Score s : scores)
			scoreFile.println(s);
		
		scoreFile.close();
	}
	
	
	/**
	 * Asks the player for a name with a dialog box, then adds that score to the ArrayList of Scores.
	 * Then sorts the scores and attempts to write them to the high scores file with writeScores().
	 * @param score The score the player has achieved
	 * @see kaninator.game.Highscore.writeScores
	 * @see kaninator.game.Highscore.Score
	 */
	public void addScore(int score)
	{
		Score newScore = new Score();
		newScore.name = JOptionPane.showInputDialog(null, "Game Over!", "Enter your name:", JOptionPane.WARNING_MESSAGE);
		
		if(newScore.name == null || newScore.name.length() < 1)
			return;
		
		newScore.score = score;
		scores.add(newScore);
		
		Collections.sort(scores);
		writeScores();
	}
	
	/**
	 * Renders the high scores menu to the screen.
	 */
	private void render()
	{
		camera.clearGUI();
		menu.render();
		camera.renderGUI();
	}
	
	
	/**
	 * High scores menu, implemented using the Menu class.
	 * Prints the high scores from the ArrayList of scores stored as an attribute.
	 * Returns to the main menu when done.
	 * @see kaninator.mechanics.Menu 
	 */
	public int doState()
	{
		int retvalue = -1;
		int m_x = 0;
		int m_y = 0;
		
		for(int i = 0; i < MAX_SCORES; i++)
		{
			if(i >= scores.size())
				break;
			
			Score s = scores.get(i);
			Text scoreEntry = new Text(s.toString(), "Impact", 32, Font.BOLD, Color.WHITE);
			menu.addEntry(scoreEntry, scoreEntry);
		}
		
		int targetValue = (MAX_SCORES < scores.size()) ?  MAX_SCORES : scores.size();
		
		menu.addEntry(new Text("Menu!", "Impact", 32, Font.BOLD, Color.WHITE),
						new Text("Menu!", "Impact", 32, Font.BOLD, Color.RED));
		
		while(true)
		{
			render();
			
			m_x = mouse.get_x();
			m_y = mouse.get_y();
			try {Thread.sleep(Kaninator.FRAME_DELAY);} catch(Exception e){}
			
			menu.setPosition(m_x, m_y);
			
			if(mouse.isPressed(0))
			{
				retvalue = menu.select();
				try {Thread.sleep(Kaninator.DEBOUNCE_DELAY);} catch(Exception e){}
			}
			
			if(retvalue == targetValue)
				break;
			
		}
		
		camera.clearGUI();
		menu.clear();
		menu.clearEntries();

		return Kaninator.MAIN_MENU;
	}

}
