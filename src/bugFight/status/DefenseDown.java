/**
 * DefenseDown.java
 * 
 * @author Ben Shecter
 * @version Dec 19, 2007
 */
package bugFight.status;

/**
 * @author notonetothink
 * 
 */
public class DefenseDown extends TimedModifier
{
	int amount;

	public DefenseDown(int amount)
	{
		this(amount, 10);
	}

	public DefenseDown(int amount, int turns)
	{
		super(turns);
		this.amount = amount;
	}

	@Override
	public int getDefMod()
	{
		return -amount;
	}

	@Override
	public int getType()
	{
		return Modifier.DEBUFF;
	}
}