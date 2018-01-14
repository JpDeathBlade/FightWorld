/**
 * @author Ben Shecter and Joe Stuhr
 * @version 1.00
 * @file PoisonSpell.java
 * 
 * @Date Created: Dec 19, 2007
 * @Date Last Edited: Jan 25, 2008
 *
 * Comments: Adds the poison status to a bug.
 */

package bugFight.spells;

import java.awt.Color;

import bugFight.bugs.Grunt;

public class PoisonSpell extends GenericSpell
{
	/**
	 * Creates a green spell that casts poison on the other bugs.
	 * 
	 * @param g -
	 *            the bug that casted the spell.
	 */
	public PoisonSpell(Grunt g)
	{
		super(g);
		setColor(Color.GREEN);
	}

	/**
	 * Hurts other bugs by adding poison to it, gives experience to the caster.
	 * 
	 * @param g -
	 *            grunt to cast the spell on.
	 */
	@Override
	public void effect(Grunt g)
	{
		g.addStatus(new bugFight.status.Poison(caster, 10));

	}
}