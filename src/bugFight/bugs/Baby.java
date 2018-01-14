/**
 * @author Ben Shecter and Joe Stuhr
 * @version 1.00
 * @file Baby.java
 * 
 * @Date Created: Jan 22, 2008
 * @Date Last Edited: Jan 24, 2008
 *
 * Comments: Creates a baby bug.
 */

package bugFight.bugs;

import info.gridworld.grid.Location;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * This class represents a baby bug. The baby can "grow" into a specified class.
 * 
 * @author Joe Stuhr
 */
public class Baby extends Grunt
{
	/**
	 * Age of the baby.
	 */
	private int age;

	/**
	 * The class that the baby will "grow" into.
	 */
	private Class destiny;

	/**
	 * Default constructor. - Creates a red baby facing in a random direction.
	 */
	public Baby()
	{
		this(Color.RED);
	}

	/**
	 * Simple constructor. - Creates a bugColor baby facing in a random
	 * direction.
	 * 
	 * @param bugColor -
	 *            sets the baby's initial color.
	 */
	public Baby(Color bugColor)
	{
		this(bugColor, -1);
	}

	/**
	 * Simple constructor with class.
	 * 
	 * @param bugColor
	 *            The color of the baby to be created
	 * @param cl
	 *            The class that the baby will "grow" into.
	 */
	public Baby(Color bugColor, Class cl)
	{
		this(bugColor, -1, cl);
	}

	/**
	 * Detailed constructor. - Places a bugColor baby facing direction.
	 * 
	 * @param bugColor -
	 *            sets the bugs initial color.
	 * @param direction -
	 *            sets the direction of the baby.
	 */
	public Baby(Color bugColor, int direction)
	{
		this(bugColor, direction, Grunt.class);
	}

	/**
	 * Detailed constructor with class.
	 * 
	 * @param bugColor
	 *            The color of the Baby to be created.
	 * @param direction
	 *            The direction the baby is to face.
	 * @param cl
	 *            The class the baby is to "grow" into.
	 */
	public Baby(Color bugColor, int direction, Class cl)
	{
		super(bugColor, direction);
		age = randGen.nextInt(10) + 12;
		destiny = cl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bugFight.bugs.Grunt#act()
	 */
	@Override
	public void act()
	{
		if (!isFainted())
		{
			if (age >= 0)
				age--;
			else
				grow();
		}

		super.act();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bugFight.bugs.Grunt#becomeUndead(java.awt.Color)
	 */
	@Override
	public void becomeUndead(Color color)
	{
		BabyUndead dead = new BabyUndead(color);
		dead.putSelfInGrid(getGrid(), getLocation());
		dead.setDirection(getDirection());
	}

	/**
	 * Make the baby "grow"
	 */
	public void grow()
	{
		Grunt grunt = null;
		try
		{
			grunt = (Grunt) destiny.getConstructor(Color.class).newInstance(
					getInitColor());
			grunt.putSelfInGrid(getGrid(), getLocation());
			grunt.setDirection(getDirection());
		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} catch (SecurityException e)
		{
			e.printStackTrace();
		} catch (InstantiationException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			e.printStackTrace();
		} catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bugFight.bugs.Grunt#initStats()
	 */
	@Override
	protected void initStats()
	{
		maxHp = 20;
		damageFactor = 1;
		defenseFactor = 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bugFight.bugs.Grunt#shouldRun()
	 */
	@Override
	public boolean shouldRun()
	{
		ArrayList<Location> temp = getGrid().getOccupiedAdjacentLocations(
				getLocation());
		ArrayList<Grunt> others = new ArrayList<Grunt>();
		for (Location l : temp)
			if (getGrid().get(l) instanceof Grunt)
				others.add((Grunt) getGrid().get(l));
		boolean underAttack = false;
		for (int i = 0; i < others.size() && !underAttack; i++)
			if (others.get(i).canAttack())
				underAttack = true;

		if (underAttack && (Math.random() < .8))
			return true;
		return false;
	}

}