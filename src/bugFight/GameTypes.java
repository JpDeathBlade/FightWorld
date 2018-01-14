/**
 * @author Ben Shecter
 * @version 1.00
 * @file GameTypes.java
 * 
 * @Date Created: Jan 23, 2008
 * @Date Last Edited: Feb 19, 2008
 *
 * Comments: Generates the game chosen by the player in the
 *           intro box.
 */

package bugFight;

import info.gridworld.actor.Actor;
import info.gridworld.actor.ActorWorld;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

import java.awt.Color;
import java.util.ArrayList;

import bugFight.bugs.Egg;
import bugFight.bugs.Grunt;
import bugFight.bugs.Mage;
import bugFight.bugs.Queen;
import bugFight.bugs.Tank;
import bugFight.bugs.Warrior;
import bugFight.grid.ColorGrid;

/**
 * The methods of this class reprecent game types to be started.
 * 
 * @author Ben Shecter
 */
public class GameTypes
{
	/**
	 * Used to set the amount of bugs for Quick Start.
	 */
	private static int getNum(Class cl)
	{
		int num = 2;

		if (cl.equals(Grunt.class))
			num = 4;
		else if (cl.equals(Queen.class))
			num = 1;

		return num;
	}
	
	/**
	 * Creates the Quick Start game. Uses getNum to randomly place
	 * X amount of bugs in the grid.
	 */
	@SuppressWarnings("unchecked")
	public static void quickStart()
	{
		ArrayList<Actor> actors = new ArrayList<Actor>();
		Color[] colors = { Color.BLUE, Color.RED, 
						   Color.GREEN, Color.YELLOW,
						 };
		Class[] classes = { Grunt.class, Warrior.class, 
							Tank.class, Mage.class,
							Queen.class,
						  };
		Class[] cArgs = { Color.class };

		for (Color c : colors)
		{
			for (Class cl : classes)
			{
				try
				{
					for (int i = 0; i < getNum(cl); i++)
						actors.add((Actor) cl.getConstructor(cArgs)
								.newInstance(c));
				} catch (Exception e)
				{
				}
			}
		}

		/**
		 * Resizes the grid to house all the bugs efficently. - About 2.5
		 * squares per bug
		 */
		Grid<Actor> g = null;
		int squares = (int) (2.5 * actors.size());

		if (squares % 2 == 1)
			squares++;
		squares = (int) Math.sqrt(squares);
		if (squares > 10)
			g = new ColorGrid<Actor>(squares, squares);
		else
			g = new ColorGrid<Actor>(10, 10);

		/**
		 * Creates and places all the actors inside the grid.
		 */
		ActorWorld world = new ActorWorld(g);
		for (Actor a : actors)
			world.add(a);

		/**
		 * Draws the grid to the screen.
		 */
		world.show();
	}

	/**
	 * Creates the game of Four Corners. Four eggs, colored Red, Blue,
	 * Green, and Yellow placed in each corner.
	 */
	public static void fourCorners()
	{
		Grid<Actor> g = new ColorGrid<Actor>(10, 10);
		ActorWorld world = new ActorWorld(g);
		
		world.add(new Location(0, 0), new Egg(Color.RED, Queen.class));
		world.add(new Location(9, 0), new Egg(Color.BLUE, Queen.class));
		world.add(new Location(0, 9), new Egg(Color.GREEN, Queen.class));
		world.add(new Location(9, 9), new Egg(Color.YELLOW, Queen.class));
		
		world.show();
	}
	
	/**
	 * Creates the game of Chess. Places Red and Blue bugs in a chess like
	 * setup.
	 */
	public static void chess()
	{
		Grid<Actor> g = new ColorGrid<Actor>(9, 9);
		ActorWorld world = new ActorWorld(g);
		
		for(int x = 1; x < 8; x++)
		{
			world.add(new Location(1, x), new Grunt(Color.RED, 180));
			world.add(new Location(7, x), new Grunt(Color.BLUE, 0));
		}
		
		world.add(new Location(0, 0), new Egg(Color.RED, 180));
		world.add(new Location(0, 1), new Warrior(Color.RED, 180));
		world.add(new Location(0, 2), new Mage(Color.RED, 180));
		world.add(new Location(0, 3), new Tank(Color.RED, 180));
		world.add(new Location(0, 4), new Queen(Color.RED, 180));
		world.add(new Location(0, 5), new Tank(Color.RED, 180));
		world.add(new Location(0, 6), new Mage(Color.RED, 180));
		world.add(new Location(0, 7), new Warrior(Color.RED, 180));
		world.add(new Location(0, 8), new Egg(Color.RED, 180));
		
		world.add(new Location(8, 0), new Egg(Color.BLUE, 0));
		world.add(new Location(8, 1), new Warrior(Color.BLUE, 0));
		world.add(new Location(8, 2), new Mage(Color.BLUE, 0));
		world.add(new Location(8, 3), new Tank(Color.BLUE, 0));
		world.add(new Location(8, 4), new Queen(Color.BLUE, 0));
		world.add(new Location(8, 5), new Tank(Color.BLUE, 0));
		world.add(new Location(8, 6), new Mage(Color.BLUE, 0));
		world.add(new Location(8, 7), new Warrior(Color.BLUE, 0));
		world.add(new Location(8, 8), new Egg(Color.BLUE, 0));
		
		world.show();
	}
}