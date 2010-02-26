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
		path = filepath;
		
		menu.setTitle(new Text("High Scores!", "Impact", 32, Font.BOLD, Color.WHITE));
		
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
			
			if(lineScore.score > 0)
				scores.add(lineScore);
		}
		
	}
	
	
	/**
	 * Attempts to write the current scores into the file located at [path].
	 */
	private void writeScores()
	{
		if(path == null)
			return;
		
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
	 * @see kaninator.game.Highscore#writeScores()
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
	
	
	/**
	 * Main method for testing purposes. Prints every test and if it succeeds, if it fails then it breaks the execution.
	 * @param args Ignored here.
	 */
	public static void main(String[] args)
	{
		try
		{
			System.out.println("Testing construction phase..");			
			//invalid constructor call: invalid file
			Highscore failScore = new Highscore(null, null, null, null, "invalidtestfile");
			if(failScore == null)
				failedTest("Couldn't create invalid Highscore object.");
			System.out.print("..");
			
			if(failScore.scores == null)
				failedTest("Scores-ArrayList null in valid Highsore object");
			System.out.print("..");
			
			if(failScore.scores.size() != 0)
				failedTest("Score count not 0 in invalid Highscore object (not possible)");
			System.out.print("..");
			
			//invalid constructor call: invalid file name
			failScore = new Highscore(null, null, null, null, "/&&@!MEJAHÖIDJIÖK/\\~åäö><|^ÖGFFFÖÖ");
			if(failScore == null)
				failedTest("Couldn't create invalid Highscore object.");
			System.out.print("..");
			
			if(failScore.scores == null)
				failedTest("Scores-ArrayList null in valid Highsore object");
			System.out.print("..");
			
			if(failScore.scores.size() != 0)
				failedTest("Score count not 0 in invalid Highscore object (not possible)");
			System.out.print("..");	
		
			//valid constructor call
			Highscore validScore = new Highscore(null, null, null, null, "testscores.scr");
			if(validScore == null)
				failedTest("Couldn't create valid Highscore object.");
			System.out.print("..");
			
			if(validScore.scores == null)
				failedTest("Scores-ArrayList null in valid Highsore object");
			System.out.print("..");
			
			if(validScore.scores.size() < 3)
				failedTest("Score count less than 3 in test-file (not possible)");
			System.out.print("..");
			
			for(int i = 0; i < validScore.scores.size() - 1; i++)
				if(validScore.scores.get(i).compareTo(validScore.scores.get(i + 1)) == 1)
					failedTest("Valid scores out of order.");
			System.out.println(".. Test Ok!");
			
			System.out.println("Testing addScore..");
			//valid file
			validScore.addScore(500);
			if(validScore.scores.size() < 3)
				failedTest("Score count less than 3 in test-file after add score");
			System.out.print("..");
			
			for(int i = 0; i < validScore.scores.size() - 1; i++)
				if(validScore.scores.get(i).compareTo(validScore.scores.get(i + 1)) == 1)
					failedTest("Valid scores out of order after add score.");
			System.out.print("..");
			
			//checking if a file has been written
			Highscore cmpScore = new Highscore(null, null, null, null, "testscores.scr");
			System.out.print("..");
			
			for(int i = 0; i < cmpScore.scores.size(); i++)
			{
				if(cmpScore.scores.get(i).compareTo(validScore.scores.get(i)) != 0)
					failedTest("Valid scores not matching with the scores file.");
				System.out.print("..");
			}
			
			//invalid file
			failScore.addScore(500);
			if(failScore.scores.size() != 1 && failScore.scores.size() != 0)
				failedTest("Score count invalid after add score");
			
			//checking if a file has been written
			cmpScore = new Highscore(null, null, null, null, "/&&@!MEJAHÖIDJIÖK/\\~åäö><|^ÖGFFFÖÖ");
			System.out.print("..");
			
			for(int i = 0; i < cmpScore.scores.size(); i++)
			{
				if(cmpScore.scores.get(i).compareTo(validScore.scores.get(i)) == 0)
					failedTest("FailScores matching with a non-existent file (?)");
				System.out.print("..");
			}
			System.out.println(".. Test Ok!");
			
			
		}
		catch (Exception e)
		{
			failedTest("Unknown exception: " + e);
		}
		System.out.println("TESTS: OK");
	}
	
	/**
	 * Gets called if a test fails. Testing purposes only. Prints out the failed test and exits the program.
	 * @param test A string describing the test that failed.
	 */
	private static void failedTest(String test)
	{
		System.out.println("TEST FAILED: " + test);
		System.exit(0);
	}

}
