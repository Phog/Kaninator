/**
 * Input from the keyboard and mouse and file input/output are wrapped
 * in this package to keep the design modular.
 */
package kaninator.io;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import kaninator.game.Map;
import kaninator.graphics.Drawable;
import kaninator.graphics.Image;
import kaninator.mechanics.*;


/**
 * @author phedman
 */
public final class MapLoader
{

	public static Map readMap(String filepath) throws IOException
	{	
		Drawable flat = new Image("/resources/flat.png");
		Drawable ne = new Image("/resources/ne.png");
		Drawable nw = flat;
		Drawable n = new Image("/resources/n.png");
		Drawable e = new Image("/resources/e.png");
		Drawable w = new Image("/resources/w.png");
		Drawable se = new Image("/resources/se.png");
		Drawable sw = new Image("/resources/sw.png");
		Drawable s = new Image("/resources/s.png");
		
		ArrayList<ArrayList<StaticObject>> objects = new ArrayList<ArrayList<StaticObject>>();
		Scanner parser = new Scanner(new File(objects.getClass().getResource(filepath).getPath()));
		
		while(parser.hasNext())
		{
			String line = parser.nextLine();
			
			if(line.length() == 0 || line.charAt(0) == '#')
				continue;
			
			ArrayList<StaticObject> tileList = new ArrayList<StaticObject>();

			
			objects.add(tileList);
			
			Scanner lineScr = new Scanner(line);
			while(lineScr.hasNextInt())
			{
				int height = lineScr.nextInt() * 32;

				if(!lineScr.hasNextInt())
				{
					System.out.println("PARSING ERROR, LINE DIDN'T MATCH");
					continue;
				}
				
				switch(lineScr.nextInt())
				{
					case 0:
						tileList.add(new FlatTile(flat, flat, height));
						break;
					case 1:
						tileList.add(new NWestSlope(nw, flat, height - 32));
						break;
					case 2:
						tileList.add(new NorthSlope(n, flat, height));
						break;
					case 3:
						tileList.add(new NEastSlope(ne, flat, height));
						break;
					case 4:
						tileList.add(new EastSlope(e, flat, height));
						break;
					case 5:
						tileList.add(new SEastSlope(se, flat, height));
						break;
					case 6:
						tileList.add(new SouthSlope(s, flat, height));
						break;
					case 7:
						tileList.add(new SWestSlope(sw, flat, height));
						break;
					case 8:
						tileList.add(new WestSlope(w, flat, height));
						break;
				}
			}
		}
		
		parser.close();
		return new Map(objects);
	}
	
}
