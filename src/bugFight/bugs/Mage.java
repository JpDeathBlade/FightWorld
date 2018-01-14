/**
 * @author Ben Shecter and Joe Stuhr
 * @version 6.32
 * @file Mage.java
 * 
 * @Date Created: Dec 19, 2007
 * @Date Last Edited: Jan 24, 2008
 *
 * Comments: Creates a Mage bug that acts as a grunt bug that can
 * cast spells.
 */

package bugFight.bugs;

import info.gridworld.actor.Actor;
import info.gridworld.grid.Location;

import java.awt.Color;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import bugFight.armor.ArmorClass;
import bugFight.spells.GenericSpell;
import bugFight.spells.SpellClass;

/**
 * This class defines a bug that attacks with spells.
 * 
 * @author Ben Shecter
 */
public class Mage extends Grunt
{
	/**
	 * Constructor of the spell to attack with.
	 */
	private Constructor<? extends GenericSpell> mySpell;

	/**
	 * Manages what spells the bug can shoot.
	 */
	private SpellClass spellCl;

	/**
	 * Default constructor. - Creates a red bug facing in a random direction.
	 */
	public Mage()
	{
		super();
		spellCl = SpellClass.getSet(getClass().getSimpleName());
	}

	/**
	 * Simple constructor. - Creates a bugColor bug facing in a random
	 * direction.
	 * 
	 * @param bugColor -
	 *            sets the bugs initial color.
	 */
	public Mage(Color bugColor)
	{
		super(bugColor);
		spellCl = SpellClass.getSet(getClass().getSimpleName());
	}

	/**
	 * Detailed constructor. - Creates a bugColor bug facing in direction.
	 * 
	 * @param bugColor -
	 *            sets the bugs initial color.
	 * @param direction -
	 *            sets the direction the bug is facing.
	 */
	public Mage(Color bugColor, int direction)
	{
		super(bugColor, direction);
		spellCl = SpellClass.getSet(getClass().getSimpleName());
	}

	/**
	 * Creates the set spell and places it into the grid.
	 */
	@Override
	public void attack()
	{
		Location next = getLocation().getAdjacentLocation(getDirection());
		if (!getGrid().isValid(next))
			return;

		GenericSpell p = null;
		try
		{
			if (mySpell != null)
				p = mySpell.newInstance(this);
		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} catch (InstantiationException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}

		if (p != null)
		{
			p.putSelfInGrid(getGrid(), next);
			p.setDirection(getDirection());
			addExperience(1);
		}

	}

	/**
	 * Replaces this bug with the appropriate undead variant.
	 * 
	 * @param color
	 *            Color of the bug to be created.
	 */
	public void becomeUndead(Color color)
	{
		this.armorCl = ArmorClass.getSet(getClass().getSimpleName());
		
		Undead dead = new Undead(color, getDirection(), experience, armorCl);
		dead.putSelfInGrid(getGrid(), getLocation());
		dead.setDirection(getDirection());
	}
	
	/**
	 * Checks the spaces in front of the bug to see if the bug can attack.
	 * 
	 * @return true if the bug can attack, false otherwise.
	 */
	@Override
	public boolean canAttack()
	{
		int heal = ((int) (Math.random() * 10) - 2);
		
		if ((bugHp <= .2 * maxHp) && (Math.random() < .6))
			this.addHp(heal);
		
		spellCl = spellCl.evolve(getLevel());
		boolean clear = false;
		Location next = getLocation().getAdjacentLocation(getDirection());
		if (getGrid().isValid(next))
			clear = getGrid().get(next) == null;
		if (clear && lineCheck(getLocation(), getDirection(), 0))
			return true;
		return false;
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
		damageFactor = 5;
		defenseFactor = 3;
		runHpPercent = .3;
	}

	/**
	 * Recursively checks to see if a bug can attack. If it can picks the spell
	 * to use.
	 * 
	 * @param l -
	 *            location of the next check.
	 * @param direction -
	 *            direction of the check.
	 * @param distance -
	 *            distance from the mage.
	 * 
	 * @return true if the mage can attack, false otherwise.
	 */
	private boolean lineCheck(Location l, int direction, int distance)
	{
		if (distance > 5)
			return false;
		Location next = l.getAdjacentLocation(direction);
		if (!getGrid().isValid(next))
			return false;
		Actor a = getGrid().get(next);
		if (a != null)
		{
			if (a instanceof GenericSpell
					&& a.getDirection() == getDirection()
							+ Location.HALF_CIRCLE)
				return true;
			if (!(a instanceof Grunt))
				return false;
			Grunt g = (Grunt) a;
			if (g.isFainted())
				return false;
			mySpell = null;
			if (randGen.nextInt(10) > 1)
			{
				if (g.getInitColor() != this.getInitColor())
					mySpell = spellCl.getEnemySpellConstructor(this, g);
				else
					mySpell = spellCl.getAllySpellConstructor(this, g);
			}
			return mySpell != null;
		}
		return lineCheck(next, direction, distance + 1);
	}
}