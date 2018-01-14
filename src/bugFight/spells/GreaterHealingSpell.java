/**
 * @author Ben Shecter and Joe Stuhr
 * @version 1.00
 * @file GreaterHealingSpell.java
 * 
 * @Date Created: Jan 25, 2008
 * @Date Last Edited: Jan 25, 2008
 *
 * Comments: Creates a healing spell that adds health from any bug it hits.
 */

package bugFight.spells;

import java.awt.Color;
import java.util.Random;

import bugFight.bugs.Grunt;

public class GreaterHealingSpell extends GenericSpell
{
	/**
	 * Generates random numbers for attacks, damage, etc.
	 */
	protected static Random randGen = new Random();

	/**
	 * Creates a dark purple spell that heals the other bugs.
	 * 
	 * @param g -
	 *            the bug that casted the spell.
	 */
	public GreaterHealingSpell(Grunt g)
	{
		super(g);
		setColor(Color.MAGENTA);
		for (int x = 0; x < 3; x++)
			setColor(getColor().brighter());
	}

	/**
	 * Gets the value of the spell
	 * 
	 * @return the amount the bug can heal
	 */
	public int getValue()
	{
		return (randGen.nextInt(caster.getDamageFactor()) + caster
				.getDamageFactor());
	}

	/**
	 * Heals other bugs, gives experience to the caster.
	 * 
	 * @param g -
	 *            grunt to cast the spell on.
	 */
	@Override
	public void effect(Grunt g)
	{
		g.setHp(g.getHp() + getValue());
		caster.addExperience(2);
	}

}