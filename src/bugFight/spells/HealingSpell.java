/**
 * @author Ben Shecter and Joe Stuhr
 * @version 1.00
 * @file HealingSpell.java
 * 
 * @Date Created: Dec 19, 2007
 * @Date Last Edited: Jan 25, 2008
 *
 * Comments: Creates a healing spell that adds health from any bug it hits.
 */

package bugFight.spells;

import java.awt.Color;
import java.util.Random;

import bugFight.bugs.Grunt;

public class HealingSpell extends GenericSpell
{
	/**
	 * Generates random numbers for attacks, damage, etc.
	 */
	protected static Random randGen = new Random();

	/**
	 * Creates a purple spell that heals the other bugs.
	 * 
	 * @param g -
	 *            the bug that casted the spell.
	 */
	public HealingSpell(Grunt g)
	{
		super(g);
		setColor(Color.MAGENTA);
	}

	/**
	 * Gets the value of the spell
	 * 
	 * @return the amount the bug can heal
	 */
	public int getValue()
	{
		return (randGen.nextInt(caster.getDamageFactor()) + caster
				.getDamageFactor() / 2);
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
		caster.addExperience(1);
	}
}