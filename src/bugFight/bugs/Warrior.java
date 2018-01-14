/**
 * @author Ben Shecter and Joe Stuhr
 * @version 1.00
 * @file Warrior.java
 * 
 * @Date Created: Dec 15, 2007
 * @Date Last Edited: Jan 24, 2008
 *
 * Comments: Creates a Warrior bug that acts as a stronger grunt bug.
 */

package bugFight.bugs;

import java.awt.Color;

/**
 * A powerful bug.
 * 
 * @author Joe Stuhr
 */
public class Warrior extends Grunt
{
	/**
	 * Default constructor. - Creates a red bug facing in a random direction.
	 */
	public Warrior()
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
	public Warrior(Color bugColor)
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
	public Warrior(Color bugColor, int direction)
	{
		super(bugColor, direction);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bugFight.bugs.Grunt#initStats()
	 */
	@Override
	protected void initStats()
	{
		maxHp = 60; // Bugs maximum hit points, Default - 50
		damageFactor = 5; // Damage a bug can deal, Default - 5
		defenseFactor = 3; // Defense of a bug (0 to defFactor)
		runHpPercent = .15;
	}
}