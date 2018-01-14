/**
 * @author Ben Shecter and Joe Stuhr
 * @version 1.00
 * @file DamageSpell.java
 * 
 * @Date Created: Dec 19, 2007
 * @Date Last Edited: Jan 25, 2008
 *
 * Comments: Creates a damage spell that removes health from any bug it hits.
 */

package bugFight.spells;

import java.awt.Color;
import java.util.Random;

import bugFight.bugs.Grunt;

public class DamageSpell extends GenericSpell
{
	/**
	 * Generates random numbers for attacks, damage, etc.
	 */
	protected static Random randGen = new Random();

	/**
	 * Creates a red spell that hurts the other bugs.
	 * 
	 * @param g -
	 *            the bug that casted the spell.
	 */
	public DamageSpell(Grunt g)
	{
		super(g);
		setColor(Color.RED);
	}

	/**
	 * Hurts other bugs, gives experience to the caster.
	 * 
	 * @param g -
	 *            grunt to cast the spell on.
	 */
	@Override
	public void effect(Grunt g)
	{
		int temp = g.getHp()
				- (randGen.nextInt(caster.getDamageFactor()) + caster
						.getDamageFactor() / 2);
		g.setHp(temp);

	}

}