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
public class AttackUp extends TimedModifier
{
	private int amount;

	public AttackUp(int amount)
	{
		this(amount, 10);
	}

	public AttackUp(int amount, int turns)
	{
		super(turns);
		this.amount = amount;
	}

	@Override
	public int getAttackMod()
	{
		return amount;
	}

	@Override
	public int getType()
	{
		return Modifier.BUFF;
	}

}