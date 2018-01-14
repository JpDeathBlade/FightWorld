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
public class AttackDown extends TimedModifier
{
	int amount;

	public AttackDown(int amount)
	{
		this(amount, 10);
	}

	public AttackDown(int amount, int turns)
	{
		super(turns);
		this.amount = amount;
	}

	@Override
	public int getAttackMod()
	{
		return -amount;
	}

	@Override
	public int getType()
	{
		return Modifier.DEBUFF;
	}
}