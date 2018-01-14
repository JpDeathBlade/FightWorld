/**
 * 
 */
package bugFight.bugs;

import java.awt.Color;
import java.util.ArrayList;

import bugFight.bugs.pets.GenericPet;

/**
 * @author notonetothink
 * 
 */
public class Hunter extends Grunt
{

	ArrayList<GenericPet> pets;

	public Hunter()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public Hunter(Color bugColor, int direction)
	{
		super(bugColor, direction);
		// TODO Auto-generated constructor stub
	}

	public Hunter(Color bugColor)
	{
		super(bugColor);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bugFight.bugs.Grunt#initStats()
	 */
	@Override
	protected void initStats()
	{
		pets = new ArrayList<GenericPet>();
		super.initStats();
	}

	// @Override
	// public void doAction()
	// {
	// if(pets.size() < 4 && randGen.nextDouble() < .5)
	// addPet();
	// super.doAction();
	// }

	public void addPet()
	{

	}

}