/**
 * AttackUp.java
 * 
 * @author Ben Shecter
 * @version Dec 19, 2007
 */
package bugFight.status;

/**
 * @author notonetothink
 * 
 */
public class DefenseUp extends TimedModifier
{
	private int amount;

	public DefenseUp(int amount)
	{
		this(amount, 10);
	}

	public DefenseUp(int amount, int turns)
	{
		super(turns);
		this.amount = amount;
	}

	@Override
	public int getDefMod()
	{
		return amount;
	}

	@Override
	public int getType()
	{
		return Modifier.BUFF;
	}

}