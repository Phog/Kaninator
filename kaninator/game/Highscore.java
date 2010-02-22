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

/**
 * The settings state.
 * The player can view the high scores in this state.
 * @author phedman
 */
public class Highscore extends GameState
{
	private static final int MAX_SCORES = 6;
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

	public Highscore(Camera _camera, GUI _gui, Keyboard _keyboard, Mouse _mouse, String filepath)
	{
		super(_camera, _gui, _keyboard, _mouse);
		
		menu = new Menu(_gui);
		scores = new ArrayList<Score>();
		
		menu.setTitle(new Text("High Scores!", "Impact", 32, Font.BOLD, Color.WHITE));
		URL url = this.getClass().getResource(filepath);
		
		if(url != null)
			path = url.getPath();
		else
			path = " ";
		readScores();
		
	/*	menu.addEntry(new Text("Peter - 1337!", "Impact", 32, Font.BOLD, Color.WHITE),
						new Text("Peter - 1337!", "Impact", 32, Font.BOLD, Color.WHITE));
		
		menu.addEntry(new Text("Arno - 6!", "Impact", 32, Font.BOLD, Color.WHITE),
						new Text("Arno - 6!", "Impact", 32, Font.BOLD, Color.WHITE));
		
*/

	}
		
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
			System.out.println("High score file not found: " + e);
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
	
	private void writeScores()
	{
		PrintWriter scoreFile;
		try
		{
			scoreFile = new PrintWriter(new File(path));
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Could not create/edit file: " + e);
			return;
		}
		
		for(Score s : scores)
			scoreFile.println(s);
		
		scoreFile.close();
	}
	
	public void addScore(String name, int score)
	{
		Score newScore = new Score();
		newScore.name = name;
		newScore.score = score;
		scores.add(newScore);
		
		Collections.sort(scores);
		writeScores();
	}
	
	private void render()
	{
		camera.clearGUI();
		menu.render();
		camera.renderGUI();
	}
	
	
	/**
	 * High scores menu, implemented using the Menu class.
	 * @see kaninator.mechanics.menu 
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
