/**
 * Input from the keyboard and mouse and file input/output are wrapped
 * in this package to keep the design modular.
 */
package kaninator.io;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import kaninator.game.Map;
import kaninator.game.MapException;
import kaninator.graphics.Drawable;
import kaninator.graphics.ImageFactory;
import kaninator.mechanics.*;


/**
 * Creates a Map object from a text file. Also creates the StaticObjects contained in the Map.
 * Reads a text file consisting of numbers with whitespaces between them.
 * The odd numbers represent the height of the current tile, the even ones represent type of the tile.
 * Example:<br />
 * 0 0  0 0  0 0<br />
 * 0 0  3 1  0 0<br />
 * 0 0  0 0  0 0<br />
 * This represents a map of 9 tiles, the one in the middle is at a height of 3 and is of the tile type 0.
 * The rest are at the height 0 and of the type 0.
 * @author phedman
 * @see kaninator.game.Map
 * @see kaninator.mechanics.StaticObject
 */
public final class MapLoader
{
	private static final double TILE_SIZE = 64.0;
	private static final double TILE_HEIGHT = 32.0;
	
	private static Drawable flat, ne, nw, n, e, se, s, sw, w;
	
	/**
	 * Returns the internal size (in the isometric coordinate system) of a tile.
	 * The size is set as a constant.
	 * @return The size of the tile in isometric coordinate units.
	 */
	public static double getTileSize()
	{
		return TILE_SIZE;
	}
	
	/**
	 * Returns the internal height (in the isometric coordinate system) of a tile.
	 * The size is set as a constant.
	 * @return The height of the tile in isometric coordinate units.
	 */
	public static double getTileHeight()
	{
		return TILE_HEIGHT;
	}
	
	/**
	 * Reads the file found at the filepath, parses it, creates the necessary StaticObjects, and creates a game map from them.
	 * @param filepath The path that points to the map file.
	 * @return A the Map created from the text file.
	 * @throws IOException If the file is not found or cannot be read.
	 */
	public static Map readMap(String filepath) throws MapException
	{	
		ArrayList<ArrayList<StaticObject>> objects = new ArrayList<ArrayList<StaticObject>>();

		URL url = objects.getClass().getResource(filepath);
		Scanner parser;
		try
		{
			if(url == null)
				throw new IOException("ERR: File not found: " + filepath);
			
			parser = new Scanner(url.openStream());
		}
		catch(IOException e)
		{
			throw new MapException("Couldn't load map: \n" + e);
		}
		
		loadTiles();
		int y = 0;
		while(parser.hasNext())
		{
			String line = parser.nextLine();
			
			if(line.length() == 0 || line.charAt(0) == '#')
				continue;
			
			ArrayList<StaticObject> tileList = parseLine(line, y);
			
			objects.add(tileList);
			y++;
		}
		
		parser.close();
		return new Map(objects);
	}
	
	
	/**
	 * Initializes the images for the tiles.
	 */
	private static void loadTiles()
	{
		flat = ImageFactory.getImage("/resources/flat.png");
		nw = flat;
		ne =  ImageFactory.getImage("/resources/ne.png");
		n = ImageFactory.getImage("/resources/n.png");
		e =  ImageFactory.getImage("/resources/e.png");
		se = ImageFactory.getImage("/resources/se.png");
		s = ImageFactory.getImage("/resources/s.png");
		sw = ImageFactory.getImage("/resources/sw.png");
		w = ImageFactory.getImage("/resources/w.png");
	}
	
	/**
	 * Parses a single line of map data, creates the needed StaticObjects and adds them to an ArrayList.
	 * @param line The line containing the map data.
	 * @param y The number of the line.
	 * @return An ArrayList containing the newly created StaticObjects that correspond to the map data in the line.
	 */
	private static ArrayList<StaticObject> parseLine(String line, int y)
	{
		Scanner lineScr = new Scanner(line);
		ArrayList<StaticObject> tileList = new ArrayList<StaticObject>();
		
		int x = 0;
		while(lineScr.hasNextInt())
		{
			double height = lineScr.nextInt() * TILE_HEIGHT;
			if(!lineScr.hasNextInt())
			{
				System.out.println("PARSING ERROR, LINE DIDN'T MATCH");
				continue;
			}
			
			switch(lineScr.nextInt())
			{
				case 0:
					tileList.add(new FlatTile(flat, flat, height, x, y));
					break;
				case 1:
					tileList.add(new NWestSlope(nw, flat, height, x, y));
					break;
				case 2:
					tileList.add(new NorthSlope(n, flat, height, x, y));
					break;
				case 3:
					tileList.add(new NEastSlope(ne, flat, height, x, y));
					break;
				case 4:
					tileList.add(new EastSlope(e, flat, height, x, y));
					break;
				case 5:
					tileList.add(new SEastSlope(se, flat, height, x, y));
					break;
				case 6:
					tileList.add(new SouthSlope(s, flat, height, x, y));
					break;
				case 7:
					tileList.add(new SWestSlope(sw, flat, height, x, y));
					break;
				case 8:
					tileList.add(new WestSlope(w, flat, height, x, y));
					break;
			}
			x++;
		}
		
		return tileList;
	}
	
}
