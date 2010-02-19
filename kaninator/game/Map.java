/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import kaninator.mechanics.*;
import kaninator.io.MapLoader;
import java.util.ArrayList;

/**
 * @author phedman
 */
public class Map
{
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
		double obj_x = obj.get_x();
		double obj_y = obj.get_y();
		
		if(obj_x < 0 || obj_y < 0)
			return Double.MAX_VALUE;
		
		int tile_x = (int)(obj_x/MapLoader.getTileSize());
		int tile_y = (int)(obj_y/MapLoader.getTileSize());
		
		if(tile_y < 0 || tile_y >= tiles.size())
			return Double.MAX_VALUE;
		
		ArrayList<StaticObject> rowList = tiles.get(tile_y);
		
		if(tile_x < 0 || tile_x >= rowList.size())
			return Double.MAX_VALUE;
		
		double delta_x = obj_x - tile_x * MapLoader.getTileSize();
		double delta_y = obj_y - tile_y * MapLoader.getTileSize();
		
		return rowList.get(tile_x).getHeight(delta_x,delta_y);
		
	}

}
