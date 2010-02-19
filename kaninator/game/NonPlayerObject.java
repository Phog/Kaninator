/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import java.util.ArrayList;

import kaninator.graphics.Animation;
import kaninator.graphics.Drawable;
import kaninator.graphics.Shadow;
import kaninator.mechanics.DynamicObject;

/**
 * @author phedman
 */
public class NonPlayerObject
{
	private Map map;
	private DynamicObject model, shadow;
	private DynamicObject target;
	
	public NonPlayerObject(ArrayList<Animation> animations, Map _map, DynamicObject _target, double x, double y, double radius_constant)
	{
		if(animations == null || animations.size() == 0)
			return;
		
		map = _map;
		
		double radius = animations.get(0).getWidth()/radius_constant;
		model = new DynamicObject(animations, radius, map);
		model.setPos(x, y);
		
		ArrayList<Drawable> shadowList = new ArrayList<Drawable>();
		shadowList.add(new Shadow(radius * 2));
		Animation shadowAnim = new Animation(shadowList, 0.0);
		ArrayList<Animation> shadowAnimList = new ArrayList<Animation>();
		shadowAnimList.add(shadowAnim);
		
		shadow = new DynamicObject(shadowAnimList, radius, map);
		shadow.setPos(x, y);
		
		target = _target;
	}
	
	public void observe()
	{
		if(target.get_x() > model.get_x() && target.get_y() > model.get_y())
		{
			model.move_y(1);
			model.move_x(1);
			model.setState(0);
		}
		else if(target.get_x() < model.get_x() && target.get_y() > model.get_y())
		{
			model.move_y(1);
			model.move_x(-1);
			model.setState(5);
		}
		else if(target.get_x() > model.get_x() && target.get_y() < model.get_y())
		{
			model.move_y(-1);
			model.move_x(1);
			model.setState(2);
		}
		else
		{
			model.move_y(-1);	
			model.move_x(-1);
			model.setState(1);
		}
	}
	
	public void act()
	{
		model.getAnimation().advance();
		model.update();
		
		shadow.setHeight(map.getHeight(model));
		shadow.setPos(model.get_x(), model.get_y());
	}
	
	public ArrayList<DynamicObject> getDynamicObjects()
	{
		ArrayList<DynamicObject> objects = new ArrayList<DynamicObject>();
		objects.add(shadow);
		objects.add(model);
		return objects;
	}
}
