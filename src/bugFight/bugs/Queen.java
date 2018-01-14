/**
 * @author Ben Shecter and Joe Stuhr
 * @version 1.00
 * @file Queen.java
 * 
 * @Date Created: Jan 22, 2008
 * @Date Last Edited: Jan 24, 2008
 *
 * Comments: Creates a Queen that can lay eggs.
 */

package bugFight.bugs;

import info.gridworld.actor.Actor;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

import java.awt.Color;
import java.util.ArrayList;

import bugFight.grid.ColorGrid;

/**
 * A bug that can lay eggs.
 * 
 * @author Joe Stuhr
 */
public class Queen extends Grunt
{
	/**
	 * This array controls what eggs may grow into other than grunts.
	 */
	private static Class[] layableClasses = { Warrior.class, Mage.class,
			Tank.class };

	/**
	 * Default constructor. - Creates a red bug facing in a random direction.
	 */
	public Queen()
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
	public Queen(Color bugColor)
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
	public Queen(Color bugColor, int direction)
	{
		super(bugColor, direction);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bugFight.bugs.Grunt#becomeUndead(java.awt.Color)
	 */
	@Override
	public void becomeUndead(Color color)
	{
		QueenUndead dead = new QueenUndead(color, getDirection(), experience,
				armorCl);
		dead.putSelfInGrid(getGrid(), getLocation());
		dead.setDirection(getDirection());
	}

	/**
	 * Overriden so that the queen may lay eggs while moving.
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

		if (gr.getOccupiedLocations().size() < (gr.getNumCols()
				* gr.getNumRows() * .60)
				&& Math.random() <= .2)
		{// if there is enough space and there is a 1/5 chance of laying an
			// egg
			Egg egg = null;
			if (gr instanceof ColorGrid) // this is here so that later
											// "smart" laying can be
											// implemented.
			{
				if (Math.random() < .5) // half of the eggs will be grunts
					egg = new Egg(getInitColor());
				else
				{ // otherwise, make an egg that will hacth into one of the
					// layable classes
					egg = new Egg(getInitColor(), layableClasses[randGen
							.nextInt(layableClasses.length)]);
				}
			}
			else
			{
				egg = new Egg(getColor());
			}
			egg.putSelfInGrid(gr, loc);
			this.addExperience(1);
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
		maxHp = 60;
		damageFactor = 3;
		defenseFactor = 4;
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

		if (underAttack && (Math.random() < .5))
			return true;
		return false;
	}
}