/**
 * Contains all the high-level game functions and implements the actual gameplay.
 */
package kaninator.game;

import kaninator.mechanics.*;
import kaninator.graphics.*;
import java.util.*;

public class GoreFactory
{
	private static final int GORE_TTYL = 225;
	private static final int NUM_GORE = 4;
	private static final double GORE_SPEED = 6.0;
	
	private Map map;
	private LinkedList<Gore> gore;
	private LinkedList<DynamicObject> goreObjects;
	private ArrayList<Animation> headGore;
	private ArrayList<Animation> boneGore;
	
	public GoreFactory(Map _map, LinkedList<DynamicObject> _goreObjects, ArrayList<Animation> _headGore, ArrayList<Animation> _boneGore) throws ModelException
	{
		map = _map;
		goreObjects = _goreObjects;
		
		gore = new LinkedList<Gore>();
		
		headGore = _headGore;
		boneGore = _boneGore;
		
		if(headGore == null || boneGore == null || headGore.size() == 0 || boneGore.size() == 0)
			throw new ModelException("ERR: Gore animation empty");
	}
	
	public void updateGore()
	{
		for(Iterator<Gore> i = gore.iterator(); i.hasNext();)
		{
			Gore g = i.next();
			if(g.update())
			{
				i.remove();
				goreObjects.remove(g.getMainObject());
			}
		}
	}
	
	public void gorify(DynamicObject obj)
	{
		try
		{
			Gore head = new Gore(AnimationFactory.cloneAnimations(headGore), map, obj, 0.0, 0.0, GORE_TTYL);
			gore.add(head);
			goreObjects.add(head.getMainObject());
			for(int i = 0; i < NUM_GORE; i++)
			{
				double speed_x = GORE_SPEED * (Math.random() - 0.5);
				double speed_y = GORE_SPEED * (Math.random() - 0.5);
				Gore bone = new Gore(AnimationFactory.cloneAnimations(boneGore), map, obj, speed_x, speed_y, GORE_TTYL);
				gore.add(bone);
				goreObjects.add(bone.getMainObject());
			}
		}
		catch(ModelException e)
		{
			System.out.println("ERR: Couldn't gorify: " + e);
		}
	}
}
