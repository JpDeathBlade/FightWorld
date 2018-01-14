/**
 * GenericSpell.java
 * 
 * @author Ben Shecter
 * @version Dec 19, 2007
 */
package bugFight.spells;

import info.gridworld.actor.Actor;
import info.gridworld.grid.Location;
import bugFight.bugs.Grunt;

/**
 * @author notonetothink
 * 
 */
public abstract class GenericSpell extends Actor
{
	protected Grunt caster;

	public GenericSpell(Grunt g)
	{
		this.caster = g;
	}

	@Override
	public void act()
	{
		Location next = getLocation().getAdjacentLocation(getDirection());
		if (getGrid().isValid(next))
		{
			Actor a = getGrid().get(next);
			if (a != null)
			{
				if (a instanceof Grunt)
				{
					Grunt g = ((Grunt) a);
					effect(g);

					if (g.isFainted())
						caster.addExperience(g.getExperienceValue());
				}
				else if (a instanceof GenericSpell)
					a.removeSelfFromGrid();
				removeSelfFromGrid();
			}
			else
			{
				moveTo(next);
			}
		}
		else
			removeSelfFromGrid();
	}

	protected void effect()
	{
		effect((Grunt) getGrid().get(
				getLocation().getAdjacentLocation(getDirection())));
	}

	public abstract void effect(Grunt g);
}