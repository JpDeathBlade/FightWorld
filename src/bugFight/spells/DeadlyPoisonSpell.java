/**
 * @author Ben Shecter and Joe Stuhr
 * @version 1.00
 * @file DeadlyPoisonSpell.java
 * 
 * @Date Created: Dec 19, 2007
 * @Date Last Edited: Jan 25, 2008
 *
 * Comments: Adds the deadly poison status to a bug.
 */

package bugFight.spells;

import java.awt.Color;

import bugFight.bugs.Grunt;

public class DeadlyPoisonSpell extends GenericSpell
{
	/**
	 * Creates a black spell that casts deadly poison on the other bugs.
	 * 
	 * @param g -
	 *            the bug that casted the spell.
	 */
	public DeadlyPoisonSpell(Grunt g)
	{
		super(g);
		setColor(Color.BLACK);
	}

	/**
	 * Hurts other bugs by adding deadly poison to it, gives experience to the
	 * caster.
	 * 
	 * @param g -
	 *            grunt to cast the spell on.
	 */
	@Override
	public void effect(Grunt g)
	{
		g.addStatus(new bugFight.status.DeadlyPoison(caster, 5));

	}

}