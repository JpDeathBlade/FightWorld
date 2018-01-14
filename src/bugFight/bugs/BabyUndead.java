/**
 * @author Ben Shecter and Joe Stuhr
 * @version 1.00
 * @file BabyUndead.java
 * 
 * @Date Created: Jan 22, 2008
 * @Date Last Edited: Jan 24, 2008
 *
 * Comments: Creates a baby undead bug.
 */

package bugFight.bugs;

import info.gridworld.actor.Actor;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

import java.awt.Color;
import java.util.ArrayList;

/**
 * An undead baby.
 * 
 * @author Joe Stuhr
 */
public class BabyUndead extends Grunt
{
	/**
	 * Default constructor. - Creates a red bug facing in a random direction.
	 */
	public BabyUndead()
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
	public BabyUndead(Color bugColor)
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
	public BabyUndead(Color bugColor, int direction)
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
		BabyUndead dead = new BabyUndead(color);
		dead.putSelfInGrid(getGrid(), getLocation());
		dead.setDirection(getDirection());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bugFight.bugs.Grunt#canAttack()
	 */
	@Override
	public boolean canAttack()
	{
		Grid<Actor> gr = getGrid(); // Gets the grid
		if (gr == null) // If there is no grid...
			return false;
		Location loc = getLocation(); // Stores bugs current location
		Location next = loc.getAdjacentLocation(getDirection()); // Get
																	// attacking
																	// location
		if (!gr.isValid(next)) // If there's nothing in the square...
			return false;
		Actor neighbor = gr.get(next); // Gets the actor from attacking square
		if (neighbor instanceof Grunt) // If the neighbor extends grunt...
		{ // wont attack non-FightWorld bugs
			Grunt n = (Grunt) neighbor;
			if (n.isFainted() && !(n instanceof Egg)) // take over a dead bug.
			{
				n.becomeUndead(getInitColor());
				this.removeSelfFromGrid();
			}
			if (n.getInitColor() != initalColor) // If the bugs have
													// different colors
				return !n.isFainted(); // Return if the other bug is dead
		}

		return false; // Else returns false
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

			if (ressurection == 151)
			{
				becomeUndead(getInitColor());
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
		maxHp = 20; // Bugs maximum hit points, Default - 50
		damageFactor = 1; // Damage a bug can deal, Default - 5
		defenseFactor = 2; // Defense of a bug (0 to defFactor)
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

		if ((Math.random() < .8) && underAttack)
			return true;
		return false;
	}
}