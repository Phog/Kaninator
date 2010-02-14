/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import kaninator.mechanics.*;
import java.util.ArrayList;

/**
 * @author phedman
 */
public class Map
{
	private static final double TILE_SIZE = 64.0;
	private ArrayList<ArrayList<StaticObject>> tiles;
	
	public Map(ArrayList<ArrayList<StaticObject>> _tiles)
	{
		tiles = _tiles;
	}
	
	public ArrayList<ArrayList<StaticObject>> getTiles()
	{
		return tiles;
	}

	public double getHeight(DynamicObject obj)
	{
		int tile_x = (int)(obj.get_x()/TILE_SIZE);
		int tile_y = (int)(obj.get_y()/TILE_SIZE);
		
		if(tile_y < 0 || tile_y >= tiles.size())
			return 0.0;
		
		ArrayList<StaticObject> rowList = tiles.get(tile_y);
		
		if(tile_x < 0 || tile_x >= rowList.size())
			return 0.0;
		
		double delta_x = obj.get_x() - tile_x * TILE_SIZE;
		double delta_y = obj.get_y() - tile_y * TILE_SIZE;
		
		return rowList.get(tile_x).getHeight(delta_x,delta_y);
		
	}

}
