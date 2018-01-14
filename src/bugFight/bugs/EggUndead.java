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
import bugFight.status.Poison;

/**
 * This class represents eggs laid by QueenUndead bugs. Will hatch into
 * BabyUndead or be poisonous.
 * 
 * @author Joe Stuhr
 */
public class EggUndead extends Egg
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
	 * Default constructor. - Creates a red EggUndead facing in a random
	 * direction.
	 */
	public EggUndead()
	{
		this(Color.RED);
	}

	/**
	 * Simple constructor. - Places a bugColor EggUndead facing in a random
	 * direction.
	 * 
	 * @param bugColor -
	 *            sets the bugs initial color.
	 */
	public EggUndead(Color bugColor)
	{
		this(bugColor, 0);
	}

	/**
	 * Main constructor. - Places a bugColor EggUndead facing in direction.
	 * 
	 * @param bugColor -
	 *            sets the bugs initial color.
	 * @param direction -
	 *            sets the direction the bug is facing.
	 */
	public EggUndead(Color bugColor, int direction)
	{
		super(bugColor, 0);
		age = randGen.nextInt(10) + 12;
		if (randGen.nextInt(3) == 0)
			poison = true;
		else
			poison = false;
	}

	/**
	 * Egg ages (dosen't move).
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
			BabyUndead baby = new BabyUndead(initalColor);
			baby.putSelfInGrid(getGrid(), getLocation());
			baby.setDirection(getDirection());
		}
	}
}