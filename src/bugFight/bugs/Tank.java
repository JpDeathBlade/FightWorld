/**
 * @author Ben Shecter and Joe Stuhr
 * @version 1.00
 * @file Tank.java
 * 
 * @Date Created: Dec 15, 2007
 * @Date Last Edited: Jan 24, 2008
 *
 * Comments: Creates a Tank bug that acts as a blocking grunt bug.
 */

package bugFight.bugs;

import java.awt.Color;

/**
 * A defensive bug.
 * 
 * @author Joe Stuhr
 */
public class Tank extends Grunt
{
	/**
	 * Default constructor. - Creates a red bug facing in a random direction.
	 */
	public Tank()
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
	public Tank(Color bugColor)
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
	public Tank(Color bugColor, int direction)
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
		maxHp = 100; // Bugs maximum hit points, Default - 50
		damageFactor = 1; // Damage a bug can deal, Default - 5
		defenseFactor = 7; // Defense of a bug (0 to defFactor)
		runHpPercent = .1;
	}
}