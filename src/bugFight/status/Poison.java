/**
 * Poison.java
 * 
 * @author Ben Shecter
 * @version Dec 19, 2007
 */
package bugFight.status;

import bugFight.bugs.Grunt;

/**
 * @author notonetothink
 * 
 */
public class Poison extends TimedModifier
{
	private static final double PERCENT = .0125;

	private Grunt giver;

	public Poison(Grunt g)
	{
		super();
		this.giver = g;
	}

	public Poison(Grunt g, int turns)
	{
		super(turns);
		this.giver = g;
	}

	@Override
	public void act(Grunt g)
	{
		if (!g.isFainted())
		{
			g.setHp(g.getHp() - (int) (g.getMaxHp() * PERCENT));
			if (g.isFainted())
				giver.addExperience(g.getExperienceValue());
		}
	}

	@Override
	public int getType()
	{
		return Modifier.POST_ACTION;
	}
}