/**
 * 
 */
package bugFight.grid;

import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.Location;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import bugFight.bugs.Grunt;

/**
 * Defines a grid that maintains information based upon colors of grunt
 * inhabitants.
 * 
 * @author Ben Shecter
 */
public class ColorGrid<E> extends BoundedGrid<E>
{
	Map<Color, ArrayList<Grunt>> colorCounter;

	/**
	 * @param rows
	 * @param cols
	 */
	public ColorGrid(int rows, int cols)
	{
		super(rows, cols);
		colorCounter = new HashMap<Color, ArrayList<Grunt>>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.gridworld.grid.BoundedGrid#put(info.gridworld.grid.Location,
	 *      java.lang.Object)
	 */
	@Override
	public E put(Location loc, E obj)
	{
		if (obj instanceof Grunt)
		{
			Grunt g = (Grunt) obj;
			ArrayList<Grunt> colorTracker = colorCounter.get(g.getInitColor());
			if (colorTracker == null)
			{
				colorTracker = new ArrayList<Grunt>();
				colorCounter.put(g.getInitColor(), colorTracker);
			}
			colorTracker.add(g);
		}
		E old = super.put(loc, obj);
		if (old != null && old instanceof Grunt)
		{
			Grunt g = (Grunt) obj;
			colorCounter.get(g.getInitColor()).remove(g);
		}
		return old;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.gridworld.grid.BoundedGrid#remove(info.gridworld.grid.Location)
	 */
	@Override
	public E remove(Location loc)
	{
		// TODO Auto-generated method stub
		E old = super.remove(loc);
		if (old != null && old instanceof Grunt)
		{
			Grunt g = (Grunt) old;
			ArrayList<Grunt> temp = colorCounter.get(g.getInitColor());
			temp.remove(old);
			if (temp.isEmpty())
				colorCounter.remove(g.getInitColor());
		}
		return old;
	}

	public int getCountForColor(Color color)
	{
		ArrayList<Grunt> temp = colorCounter.get(color);
		if (temp == null)
			return 0;
		return temp.size();
	}

	public ArrayList<Grunt> getMembersOfColor(Color color)
	{
		return colorCounter.get(color);
	}

	public int getColorCount()
	{
		return colorCounter.size();
	}

	public Set<Color> getGridColors()
	{
		return colorCounter.keySet();
	}

}