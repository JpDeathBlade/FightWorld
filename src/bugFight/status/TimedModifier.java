/**
 * TimedModifier.java
 * 
 * @author Ben Shecter
 * @version Dec 19, 2007
 */
package bugFight.status;

/**
 * @author notonetothink
 * 
 */
public class TimedModifier extends Modifier
{
	int turns;

	int turnCount;

	public TimedModifier()
	{
		this(10);
	}

	public TimedModifier(int turns)
	{
		this.turns = turns;
		turnCount = 0;
	}

	@Override
	public void passTurn()
	{
		turnCount++;
		if (turnCount == turns)
			clear();
	}
}