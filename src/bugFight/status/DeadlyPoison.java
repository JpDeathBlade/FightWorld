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
public class DeadlyPoison extends TimedModifier
{
	private static final double PERCENT = .05;

	private Grunt giver;

	public DeadlyPoison(Grunt g)
	{
		super();
		this.giver = g;
	}

	public DeadlyPoison(Grunt g, int turns)
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