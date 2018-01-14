/**
 * @author Ben Shecter and Joe Stuhr
 * @version 1.00
 * @file Egg.java
 * 
 * @Date Created: Jan 22, 2008
 * @Date Last Edited: Jan 24, 2008
 *
 * Comments: Creates an Egg that can hatch a baby bug.
 */

package bugFight.bugs;

import info.gridworld.actor.Actor;
import info.gridworld.actor.Rock;

import java.awt.Color;
import java.util.ArrayList;

import bugFight.status.DeadlyPoison;
import bugFight.status.Modifier;
import bugFight.status.Poison;

/**
 * This class represents an egg. It can hatch into a baby or poison surrounding
 * bugs.
 * 
 * @author Joe Stuhr
 */
public class Egg extends Grunt
{
	/**
	 * Age of the egg (time untill hatch).
	 */
	private int age;

	/**
	 * True if the egg is poisonous.
	 */
	private boolean poison;

	/**
	 * The class that the baby will be set to grow to.
	 */
	private Class destiny;

	/**
	 * Default constructor. - Creates a red Egg facing in a random direction.
	 */
	public Egg()
	{
		this(Color.RED);
	}

	/**
	 * Simple constructor. - Creates a bugColor Egg facing in a random
	 * direction.
	 * 
	 * @param bugColor -
	 *            sets the bugs initial color.
	 */
	public Egg(Color bugColor)
	{
		this(bugColor, 0);
	}

	/**
	 * Simple constructor with class
	 * 
	 * @param bugColor
	 *            The color of the Egg
	 * @param cl
	 *            The class the resulting baby will grow into.
	 */
	public Egg(Color bugColor, Class cl)
	{
		this(bugColor, 0, cl);
	}

	/**
	 * Detailed constructor. - Places a bugColor Egg facing in direction.
	 * 
	 * @param bugColor -
	 *            sets the bugs initial color.
	 * @param direction -
	 *            sets the direction the bug is facing.
	 */
	public Egg(Color bugColor, int direction)
	{
		this(bugColor, direction, Grunt.class);
	}

	/**
	 * Detailed constructor with class.
	 * 
	 * @param bugColor
	 *            The color of the Egg
	 * @param direction
	 *            The direction of the Egg
	 * @param cl
	 *            The class the resulting Baby will grow into.
	 */
	public Egg(Color bugColor, int direction, Class cl)
	{
		super(bugColor, 0);
		age = randGen.nextInt(10) + 12;
		destiny = cl;
		if (randGen.nextInt(4) == 0)
			poison = true;
		else
			poison = false;
		if (destiny.equals(Queen.class))
			poison = false;
	}

	/**
	 * Egg ages.
	 */
	@Override
	public void act()
	{
		if (!isFainted())
		{
			if (age >= 0)
				age--;
			else
				hatch();
			if (poison == true)
				setColor(getColor().darker());
		}
		else
			decay();
	}

	/**
	 * Overriden so that bugs do not get statuses.
	 * 
	 * @param status
	 *            Status to be added
	 */
	@Override
	public void addStatus(Modifier status)
	{
		return;
	}

	/**
	 * Controls the the dead bug, removing it after decaying. Poison eggs
	 * explode when it is time fr them to be removed.
	 */
	@Override
	protected void decay()
	{
		if (!isFainted()) // If the bug is alive, ends
			return;
		downTime++; // Downtime updated
		if (poison == true)
			setColor(getColor().darker());
		if (downTime > myMaxDownTime) // If the downtime is maxed out
		{
			if (poison == true)
				hatch();
			else
				removeSelfFromGrid(); // Removes self from grid
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bugFight.bugs.Grunt#die()
	 */
	@Override
	public void die()
	{
		Color temp = getColor();
		super.die();
		if (poison)
			setColor(temp);
	}

	/**
	 * Makes the egg hatch, or if poisonous poison the bugs nearby.
	 */
	public void hatch()
	{
		if (poison == true)
		{
			ArrayList<Actor> targets = getGrid().getNeighbors(getLocation());
			for (Actor a : targets)
			{
				if (a instanceof Grunt)
				{
					Grunt g = (Grunt) a;
					if (randGen.nextInt(4) == 0)
						g.addStatus(new DeadlyPoison(this, 10));
					else
						g.addStatus(new Poison(this, 10));
				}
				else if (!(a instanceof Rock))
					a.removeSelfFromGrid();
			}
			this.removeSelfFromGrid();
		}
		else
		{
			Baby baby = new Baby(initalColor, destiny);
			baby.putSelfInGrid(getGrid(), getLocation());
			baby.setDirection(getDirection());
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
		damageFactor = 0;
		defenseFactor = 2;
	}

	/**
	 * Lets you find out if an egg is poisonous.
	 * 
	 * @return True if poisonous, false otherwise.
	 */
	public boolean isPoison()
	{
		return poison;
	}
}