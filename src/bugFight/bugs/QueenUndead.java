/**
 * @author Ben Shecter and Joe Stuhr
 * @version 1.00
 * @file QueenUndead.java
 * 
 * @Date Created: Jan 24, 2008
 * @Date Last Edited: Jan 24, 2008
 *
 * Comments: Creates a Queen Undead that can lay eggs after feeding.
 */

package bugFight.bugs;

import info.gridworld.actor.Actor;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

import java.awt.Color;

import bugFight.armor.ArmorClass;

/**
 * An undead bug that lays undead eggs.
 * 
 * @author Joe Stuhr
 */
public class QueenUndead extends Undead
{
	/**
	 * A counter to store how many eggs the bug can lay.
	 */
	private int layEggs;

	/**
	 * Default constructor. - Creates a red bug facing in a random direction.
	 */
	public QueenUndead()
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
	public QueenUndead(Color bugColor)
	{
		super(bugColor);
	}

	/**
	 * Detailed constructor. - Creates a bugColor bug facing in direction.
	 * 
	 * @param bugColor -
	 *            sets the bugs initial color.
	 * @param direction -
	 *            sets the direction the bug is facing.
	 */
	public QueenUndead(Color bugColor, int direction)
	{
		super(bugColor, direction);
	}

	/**
	 * Main constructor. - Places a bugColor bug facing in direction. - Keeps
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
	public QueenUndead(Color bugColor, int direction, int exp, ArmorClass armor)
	{
		super(bugColor, direction);
		initStats(exp, armor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bugFight.bugs.Undead#becomeUndead(java.awt.Color)
	 */
	@Override
	public void becomeUndead(Color color)
	{
		QueenUndead dead = new QueenUndead(color, getDirection(), experience,
				armorCl);
		dead.putSelfInGrid(getGrid(), getLocation());
		dead.setDirection(getDirection());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bugFight.bugs.Undead#canEat()
	 */
	public boolean canEat()
	{
		boolean ret = super.canEat();
		if (ret && maxHp == bugHp)
			ret = false;
		return ret;
	}

	/**
	 * Overridden so that it can lay eggs.
	 * 
	 * @see bugFight.bugs.Grunt#directMove()
	 */
	@Override
	protected void directMove()
	{
		Grid<Actor> gr = getGrid(); // Gets the grid
		if (gr == null) // If there is no grid...
			return;
		Location loc = getLocation(); // Stores bugs current location
		Location next = loc.getAdjacentLocation(getDirection()); // Get
																	// attacking
																	// location
		if (gr.isValid(next)) // If there's nothing in the square...
			moveTo(next); // Moves to the square
		else
			removeSelfFromGrid();

		if (layEggs > 0 && Math.random() < .35)
		{
			EggUndead egg = new EggUndead(getColor());
			egg.putSelfInGrid(gr, loc);
			this.addExperience(1);
			layEggs -= 2;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bugFight.bugs.Undead#eat()
	 */
	public void eat()
	{
		super.eat();
		layEggs++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bugFight.bugs.Undead#initStats()
	 */
	@Override
	protected void initStats()
	{
		maxHp = 70;
		damageFactor = 3;
		defenseFactor = 5;
		runHpPercent = .15;
		layEggs = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bugFight.bugs.Undead#initStats(int, bugFight.armor.ArmorClass)
	 */
	protected void initStats(int exp, ArmorClass armor)
	{
		initStats();

		addExperience(exp / 2);
		armorCl = armor;
		layEggs = 0;
	}
}