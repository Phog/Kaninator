/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import kaninator.mechanics.*;
import kaninator.io.MapLoader;
import java.util.ArrayList;

/**
 * Contains the actual playing field, it consists of StaticObjects(the map tiles). 
 * It performs height checking for dynamic objects or just an arbitrary point in the
 * isometric coordinate system.
 * @author phedman
 */
public class Map
{
	private ArrayList<ArrayList<StaticObject>> tiles;
	
	/**
	 * Creates the map by storing the 2-dimensional ArrayList of StaticObjects as an attribute.
	 * @param _tiles A 2-Dimensional ArrayList of StaticObjects that builds up the map.
	 */
	public Map(ArrayList<ArrayList<StaticObject>> _tiles)
	{
		tiles = _tiles;
	}
	
	/**
	 * Getter for the 2-dimensional ArrayList of StaticObjects. Used for
	 * passing the StaticObjects to the Camera in order to render them to the screen.
	 * @return The 2-dimensional ArrayList of StaticObjects.
	 * @see kaninator.mechanics.Camera
	 */
	public ArrayList<ArrayList<StaticObject>> getTiles()
	{
		return tiles;
	}

	/**
	 * Calculates the height of a given (x,y) point in the internal, isometric, coordinate system according to the
	 * tiles stored in the map. It effectively calculates the tile the player is standing on and then passes the
	 * insets the the StaticObjects getHeight method which resolves the actual height.
	 * @param x The x coordinate of the point you want to calculate the height for.
	 * @param y The y coordinate of the point you want to calculate the height for.
	 * @return The height at the given (x,y) coordinate on the map.
	 * @see kaninator.mechanics.StaticObject
	 */
	public double getHeightAt(double x, double y)
	{
		if(x < 0 || y < 0)
			return Double.MAX_VALUE;
		
		int tile_x = (int)(x/MapLoader.getTileSize());
		int tile_y = (int)(y/MapLoader.getTileSize());
		
		if(tile_y < 0 || tile_y >= tiles.size())
			return Double.MAX_VALUE;
		
		ArrayList<StaticObject> rowList = tiles.get(tile_y);
		
		if(tile_x < 0 || tile_x >= rowList.size())
			return Double.MAX_VALUE;
		
		double delta_x = x - tile_x * MapLoader.getTileSize();
		double delta_y = y - tile_y * MapLoader.getTileSize();
		
		return rowList.get(tile_x).getHeight(delta_x,delta_y);
	}
	
	
	/**
	 * A wrapper method for the getHeightAt() method that actually takes a DynamicObject
	 * instead of coordinates.
	 * @param obj The DynamicObject for which you want to check the height of the map for.
	 * @return The height of the map at the position where the DynamicObject resides.
	 */
	public double getHeight(DynamicObject obj)
	{
		return getHeightAt(obj.get_x(), obj.get_y());
	}

}
