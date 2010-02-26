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
public final class MapFactory
{
	private static final double TILE_SIZE = 64.0;
	private static final double TILE_HEIGHT = 32.0;
	
	private static Drawable flat = null, ne = null, nw = null, n = null, e = null, se = null, s = null, sw = null, w = null;
	
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


		Scanner parser;
		try
		{		
			URL url = objects.getClass().getResource(filepath);
			
			if(url == null)
				throw new IOException("ERR: File not found: " + filepath);
			
			parser = new Scanner(url.openStream());
		}
		catch(Exception e)
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
		ArrayList<StaticObject> tileList = new ArrayList<StaticObject>();
		
		if(line == null)
			return tileList;
		
		Scanner lineScr = new Scanner(line);
		int x = 0;
		while(lineScr.hasNext())
		{
			if(!lineScr.hasNextInt())	
			{
				lineScr.next();
				continue;
			}
			
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
	
	/**
	 * Main method for testing purposes. Prints every test and if it succeeds, if it fails then it breaks the execution.
	 * @param args Ignored here.
	 */
	public static void main(String[] args) 
	{
		try
		{
			System.out.println("Testing construction phase..");
			if (flat != null || ne != null || nw != null || n != null || 
				e != null || se != null || s != null || sw != null || w != null)
				failedTest("Tile-drawables not null initially.");
			System.out.print("..");
			
			loadTiles();
			if (flat == null || ne == null || nw == null || n == null || 
					e == null || se == null || s == null || sw == null || w == null)
				failedTest("Tile-drawables still null after initialization.");
			System.out.print("..");
			
			if(flat.getHeight() != 96 || flat.getWidth() != 128 ||
					ne.getHeight() != 96 || ne.getWidth() != 128 ||
					nw.getHeight() != 96 || nw.getWidth() != 128 ||
					n.getHeight() != 96 || n.getWidth() != 128 ||
					e.getHeight() != 96 || e.getWidth() != 128 ||
					se.getHeight() != 96 || se.getWidth() != 128 ||
					s.getHeight() != 96 || s.getWidth() != 128 ||
					sw.getHeight() != 96 || sw.getWidth() != 128 ||
					w.getHeight() != 96 || w.getWidth() != 128)
				failedTest("Invalid dimensions for Tile-drawables");
			System.out.println(".. Test Ok!");
			
			System.out.println("Testing parseLine method..");
			//valid call
			ArrayList<StaticObject> mapRow = parseLine("0 0  0 0  0 0  0 0  0 0", 0);
			if(mapRow == null)
				failedTest("parseLine returned null instead of valid row");
			System.out.print("..");
			
			if(mapRow.size() != 5)
				failedTest("parseLine returned map row of invalid length (" + mapRow.size() + ")");
			System.out.print("..");
			
			//check against flat objects, they should have a uniform height.
			for(StaticObject obj : mapRow)
				if(obj == null || obj.getHeight(0, 0) != 0 || obj.getHeight(64, 64) != 0)
					failedTest("parseLine returned map row with invalid StaticObjects");
			System.out.print("..");
			
			//invalid call: invalid string
			ArrayList<StaticObject> failRow = parseLine("KISSAT KOIRIA GFFÖÖGKFD 0 0  0 0 1337 ffgds 0 0", 0);
			if(failRow == null)
				failedTest("parseLine with invalid string returned null instead of semi-valid row");
			System.out.print("..");
			
			if(failRow.size() != 3)
				failedTest("parseLine with invalid string returned map row of invalid length (" + failRow.size() + ")");
			System.out.print("..");
			
			//check against flat objects, they should have a uniform height.
			for(StaticObject obj : mapRow)
				if(obj == null || obj.getHeight(0, 0) != 0 || obj.getHeight(64, 64) != 0)
					failedTest("parseLine with invalid string returned map row with invalid StaticObjects");
			System.out.print("..");
			
			//invalid call: null string
			failRow = parseLine(null, 0);
			if(failRow == null)
				failedTest("parseLine with null string returned null instead of semi-valid row");
			System.out.print("..");
			
			if(failRow.size() != 0)
				failedTest("parseLine with null string returned map row of invalid length(" + failRow.size() + ")");
			System.out.println(".. Test Ok!");
			
			System.out.println("Testing readMap method..");
			//valid call
			Map map = readMap("/resources/testmap.map");
			if(map == null)
				failedTest("readMap returned null instead of a valid map.");
			System.out.print("..");
			
			if(map.getTiles().size() != 8)
				failedTest("readMap returned of invalid size instead of a valid map.");
			System.out.print("..");
			
			for(ArrayList<StaticObject> row : map.getTiles())
			{
				if(row.size() != 6)
					failedTest("readMap returned map of invalid size instead of a valid map.");
				
				for(StaticObject obj : row)
					if(obj == null || obj.getHeight(0, 0) != 0 || obj.getHeight(64, 64) != 0)
						failedTest("readMap returned map with invalid StaticObjects");
				System.out.print("..");
			}

			
			//invalid call: invalid string
			try
			{
				readMap("GFFÖÖÖGKFKLD KOIRAT%=)(");
				failedTest("readMap didn't throw exception for faulty filename.");
			}
			catch(MapException e)
			{
				System.out.print(e);
				System.out.print("..");
			}
			
			//invalid call: null string
			try
			{
				readMap(null);
				failedTest("readMap didn't throw exception for null filename.");
			}
			catch(MapException e)
			{
				System.out.print(e);
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
	
