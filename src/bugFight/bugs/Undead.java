/**
 * @author Ben Shecter and Joe Stuhr
 * @version 1.00
 * @file Undead.java
 * 
 * @Date Created: Dec 15, 2007
 * @Date Last Edited: Jan 24, 2008
 *
 * Comments: Creates an Undead bug that eats other dead bugs.
 */

package bugFight.bugs;

import info.gridworld.actor.Actor;
import info.gridworld.grid.Location;

import java.awt.Color;

import bugFight.armor.ArmorClass;

/**
 * A zombie bug.
 * 
 * @author Joe Stuhr
 */
public class Undead extends Grunt
{
	/**
	 * Default constructor. - Creates a red bug facing in a random direction.
	 */
	public Undead()
	{
		super();
	}

	/**
	 * Simple constructor. - Creates a bugColor bug facing in a random
	 * direction.
	 * 
	 * @param bugColor -
	 *            sets the bugs initial color.
	 */
	public Undead(Color bugColor)
	{
		super(bugColor);
	}

	/**
	 * Simple constructor. - Creates a bugColor bug facing in direction.
	 * 
	 * @param bugColor -
	 *            sets the bugs initial color.
	 * @param direction -
	 *            sets the direction the bug is facing.
	 */
	public Undead(Color bugColor, int direction)
	{
		super(bugColor, direction);
	}

	/**
	 * Main constructor. - Creates a bugColor bug facing in direction. - Keeps
	 * some experience and the armor of previous bug.
	 * 
	 * @param bugColor -
	 *            sets the bugs initial color.
	 * @param direction -
	 *            sets the direction the bug is facing.
	 * @param exp -
	 *            gives the Undead bug the other bugs experience.
	 * @param ArmorClass -
	 *            gives the Undead bug the other bugs armor.
	 */
	public Undead(Color bugColor, int direction, int exp, ArmorClass armor)
	{
		super(bugColor, direction);
		initStats(exp, armor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bugFight.bugs.Grunt#act()
	 */
	@Override
	public void act()
	{
		if (canEat())
			eat();
		else
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
		Undead dead = new Undead(color, getDirection(), experience, armorCl);
		dead.putSelfInGrid(getGrid(), getLocation());
		dead.setDirection(getDirection());
	}

	/**
	 * Checks to see if it can eat.
	 * 
	 * @return true if the bug can eat, false otherwise.
	 */
	public boolean canEat()
	{
		if (isFainted())
			return false;

		Location l = getLocation().getAdjacentLocation(getDirection());

		if (getGrid().isValid(l))
		{
			Actor a = getGrid().get(l);

			if (a instanceof Grunt && !(a instanceof Undead))
			{
				Grunt g = (Grunt) a;
				if (g.getInitColor() == getInitColor())
					return false;
				if (g.isFainted())
				{
					if (Math.random() < .8)
						return true;
					else
						return false;
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bugFight.bugs.Grunt#decay()
	 */
	@Override
	protected void decay()
	{
		if (!isFainted()) // If the bug is alive, ends
			return;
		downTime++; // Downtime updated
		if (downTime > myMaxDownTime) // If the downtime is maxed out
			removeSelfFromGrid(); // Removes self from grid
		else if (downTime == myMaxDownTime) // On last step...
		{
			int ressurection = randGen.nextInt(300); // Generates a random
														// number

			if (ressurection == 151) // If the number is equal to 5 (1/10
										// chance)
			{
				becomeUndead(getInitColor());
			}
		}
	}

	/**
	 * Takes a "bite" of a dead bug, raising the Undead bugs health.
	 */
	public void eat()
	{
		Actor a = getGrid().get(
				getLocation().getAdjacentLocation(getDirection()));
		if (a instanceof Grunt)
		{
			Grunt g = (Grunt) a;
			if (g.isFainted())
			{
				addHp(randGen.nextInt(g.getMaxHp() / 10) + 1);
			}
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
		maxHp = 70;
		damageFactor = 3;
		defenseFactor = 6;
		runHpPercent = .15;

	}

	/**
	 * Used to set the bugs base stats.
	 * 
	 * @param exp -
	 *            experience of the other bug.
	 * @param armor -
	 *            armor of the other bug.
	 */
	protected void initStats(int exp, ArmorClass armor)
	{
		maxHp = 70;
		damageFactor = 3;
		defenseFactor = 6;
		runHpPercent = .15;

		addExperience(exp / 2);
		armorCl = armor;

	}
}