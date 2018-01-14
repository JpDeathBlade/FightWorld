/**
 * @author Ben Shecter and Joe Stuhr
 * @version 1.00
 * @file Grunt.java
 * 
 * @Date Created: Dec 12, 2007
 * @Date Last Edited: Feb 19, 2008
 *
 * Comments: Grunt.java is the base for all the bugs in FightWorld. The
 * Grunt bug controls all of the methods within each bug (extending them
 * to the other bugs). Through this the Grunt bug can be edited over and
 * over without having to edit the other bugs.
 * 
 * The basics of the bug are held within the act() method. The bug can
 * attack, run away, move, level up, and die. As the bug attacks it gains
 * experience. This is used to increase the bugs stats, armor, and weapons.
 * The bug can also move around to find something to attack or to run away
 * from danger. If the bugs health hits zero the bug dies, or decays, until
 * it disappears. From there the bug has a chance to turn into an Undead bug.
 */

package bugFight.bugs;

import info.gridworld.actor.Actor;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import bugFight.armor.ArmorClass;
import bugFight.grid.ColorGrid;
import bugFight.status.Modifier;
import bugFight.status.StatusManager;
import bugFight.weapons.WeaponClass;

/**
 * Basis of the fighting bug class heriarchy. All bugs descend from this class.
 * 
 * @author Ben Shecter
 */
public class Grunt extends Actor
{
	/**
	 * Used as a base to calculate down time for bug before it becomes undead or
	 * is removed.
	 */
	public static final int BASE_DOWN_TIME = 10;

	/**
	 * Generates random numbers for attacks, damage, etc.
	 */
	protected static Random randGen = new Random();

	/**
	 * Variable to hold the bug's armor class.
	 */
	protected ArmorClass armorCl;

	/**
	 * The bugs current hit points.
	 */
	protected int bugHp;

	/**
	 * Represents the bug's raw attack strength.
	 */
	protected double damageFactor;

	/**
	 * Represents the bug's raw defensive ability.
	 */
	protected double defenseFactor;

	/**
	 * Tracks how long the bug had been dead.
	 */
	protected int downTime;

	/**
	 * Stores the bug's experience points.
	 */
	protected int experience;

	/**
	 * True if the bug is dead.
	 */
	protected boolean fainted;

	/**
	 * The team of the bug.
	 */
	protected Color initalColor;

	/**
	 * Stores the bugs current level.
	 */
	protected int level;

	/**
	 * The maximum amount of hit points of the bug.
	 */
	protected int maxHp;

	/**
	 * How long a bug decays before being removed.
	 */
	protected int myMaxDownTime;

	/**
	 * The bug's status manager.
	 */
	protected StatusManager myStatus;

	/**
	 * If the percentage of the bug's hp falls below this value, it may run.
	 */
	protected double runHpPercent;

	/**
	 * Variable to hold the bug's WeaponClass.
	 */
	protected WeaponClass weaponCl;

	/**
	 * Default constructor. - Creates a red bug facing in a random direction.
	 */
	public Grunt()
	{
		this(Color.RED);
	}

	/**
	 * Simple constructor. - Creates a bugColor bug facing in a random
	 * direction.
	 * 
	 * @param bugColor -
	 *            sets the bug's initial color.
	 */
	public Grunt(Color bugColor)
	{
		this(bugColor, -1);
	}

	/**
	 * Detailed constructor. - Creates a bugColor bug facing in direction.
	 * 
	 * @param bugColor -
	 *            sets the bug's initial color.
	 * @param direction -
	 *            sets the direction the bug is facing.
	 */
	public Grunt(Color bugColor, int direction)
	{
		initStats(); // initalize various variables
		if (direction == -1) // if direction is unspecified.
			direction = randGen.nextInt(4) * 90;
		setColor(bugColor);
		initalColor = bugColor;
		bugHp = maxHp;
		fainted = false;
		downTime = 0;
		setDirection(direction);
		myStatus = new StatusManager(this);
		this.experience = 0;
		this.level = 1;
		this.armorCl = ArmorClass.getSet(getClass().getSimpleName()); // get
																		// the
																		// armor
																		// class
		this.weaponCl = WeaponClass.getSet(getClass().getSimpleName()); // get
																		// the
																		// weapon
																		// class
	}

	/**
	 * Controls the bug's acions.
	 */
	@Override
	public void act()
	{
		if (!fainted)
		{
			myStatus.runPreAction(); // PreAction status takes effect

			if (canAttack())
			{
				if (shouldRun())
					runAway();
				else
					attack();
			}
			else if (canMove())
			{
				normalMove();
			}
			else
				turn();

			myStatus.runPostAction(); // PostAction status takes effect
			myStatus.turnPassed(); // Time based status updated
		}
		else
			decay();
	}

	/**
	 * Adds experience to the bug, and levels the bug if appropriate.
	 * 
	 * @param amount
	 *            Amount of experience to give the bug.
	 */
	public void addExperience(int amount)
	{
		this.experience += amount;

		while ( this.level <30 && getExpForNextLvl() <= this.experience)
		{
			levelUp();
			if (weaponCl != null)
				weaponCl = weaponCl.evolve(getLevel());
			if (armorCl != null)
				armorCl = armorCl.evolve(getLevel());
		}
	}

	/**
	 * Get the Experience required to level up.
	 * 
	 * @return The experience required to level up.
	 */
	private int getExpForNextLvl()
	{
		return (int) ((2.0 / 9) * Math.pow(this.getLevel() + 1, 2)
				+ ((26.0 / 3) * (this.getLevel() + 1)) - (80.0 / 9));
	}

	/**
	 * Add to the bug's hp.
	 * 
	 * @param amount
	 *            Amount to heal the bug.
	 */
	public void addHp(int amount)
	{
		bugHp += amount;

		if (bugHp > maxHp)
			bugHp = maxHp;

		if (fainted)
		{
			revive();
		}
	}

	/**
	 * Adds a status to the bug.
	 * 
	 * @param status
	 *            Status to give the bug.
	 */
	public void addStatus(Modifier status)
	{
		myStatus.addStatus(status);
	}

	/**
	 * Attacks the bug that this bug is facing.
	 */
	public void attack()
	{
		Grid<Actor> gr = getGrid();
		if (gr == null)
			return;
		Location loc = getLocation();
		Location next = loc.getAdjacentLocation(getDirection());
		if (!gr.isValid(next))
			return;
		Actor neighbor = gr.get(next);
		if (neighbor instanceof Grunt)
		{
			Grunt g = ((Grunt) neighbor);
			int damage = getDamageFactor() + myStatus.getAttackMod();

			g.removeHp(damage);
			if (g.isFainted())
				this.addExperience(g.getExperienceValue());
		}
	}

	/**
	 * Replaces this bug with the appropriate undead variant.
	 * 
	 * @param color
	 *            Color of the bug to be created.
	 */
	public void becomeUndead(Color color)
	{
		Undead dead = new Undead(color, getDirection(), experience, armorCl);
		dead.putSelfInGrid(getGrid(), getLocation());
		dead.setDirection(getDirection());
	}

	/**
	 * Checksto see if the bug can attack.
	 * 
	 * @return True if the bug can attack, false otherwise.
	 */
	public boolean canAttack()
	{
		Grid<Actor> gr = getGrid(); // Gets the grid
		if (gr == null) // If there is no grid...
			return false;
		Location loc = getLocation(); // Stores bugs current location
		Location next = loc.getAdjacentLocation(getDirection()); // Get
																	// attacking
																	// location
		if (!gr.isValid(next)) // If there's nothing in the square...
			return false;
		Actor neighbor = gr.get(next); // Gets the actor from attacking square
		if (neighbor instanceof Grunt) // If the neighbor extends grunt...
		{ // wont attack non-FightWorld bugs
			Grunt n = (Grunt) neighbor;
			if (n.getInitColor() != initalColor) // If the bugs have
													// different colors
				return !n.isFainted(); // Return if the other bug is dead
		}

		return false; // Else returns false
	}

	/**
	 * Checks Location l to see if the bug can attack that location.
	 * 
	 * @return true if the bug can attack the specified location, false
	 *         otherwise.
	 */
	public boolean canAttack(Location l)
	{
		Grid<Actor> gr = getGrid(); // Gets the grid
		if (gr == null) // If there is no grid...
			return false;
		Location loc = getLocation(); // Stores bugs current location
		Location next = loc.getAdjacentLocation(getDirection()); // Get
																	// attacking
																	// location
		if (!gr.isValid(next)) // If there's nothing in the square...
			return false;
		return l.equals(next); // Return true if current location equals
		// next location
	}

	/**
	 * Checks to see if the bug can move.
	 * 
	 * @return true if the bug can move, false otherwise.
	 */
	public boolean canMove()
	{
		Grid<Actor> gr = getGrid(); // Gets the grid
		if (gr == null) // If there is no grid...
			return false;
		Location loc = getLocation(); // Stores bugs current location
		Location next = loc.getAdjacentLocation(getDirection()); // Get
																	// attacking
																	// location
		if (!gr.isValid(next)) // If there's nothing in the square...
			return false;
		Actor neighbor = gr.get(next); // Stores the new location
		return (neighbor == null); // Returns true if the bug can move
	}

	/**
	 * Called when the bug is fainted. Bug is removed if it's downtime passes.
	 */
	protected void decay()
	{
		if (!isFainted()) // If the bug is alive, ends
			return;
		downTime++; // Downtime updated
		if (downTime > myMaxDownTime) // If the downtime is maxed out
			removeSelfFromGrid(); // Removes self from grid
		else if (downTime == myMaxDownTime) // On last step...
		{
			int ressurection = randGen.nextInt(10); // Generates a random number

			if (ressurection == 5) // If the number is equal to 5 (1/10 chance)
			{
				becomeUndead(getInitColor());
			}
		}
	}

	/**
	 * Called when the bug dies, sets variables appropriately.
	 */
	public void die()
	{
		bugHp = 0;
		fainted = true;
		myMaxDownTime = randGen.nextInt(BASE_DOWN_TIME - 4)
				+ (BASE_DOWN_TIME - 4);
		setColor(Color.DARK_GRAY);
		myStatus.removeAllStatus();
	}

	/**
	 * Moves the bug forward if possible.
	 */
	protected void directMove()
	{
		Grid<Actor> gr = getGrid(); // Gets the grid
		if (gr == null) // If there is no grid...
			return;
		Location loc = getLocation(); // Stores bugs current location
		Location next = loc.getAdjacentLocation(getDirection()); // Get
																	// attacking
																	// location
		if (gr.isValid(next)) // If there's nothing in the square...
			moveTo(next); // Moves to the square
		else
			removeSelfFromGrid();
	}

	/**
	 * Returns the effective damage factor.
	 * 
	 * @return The effective damage factor.
	 */
	public int getDamageFactor()
	{
		int ret = (int) damageFactor;
		if (weaponCl != null)
			ret += weaponCl.getAtkValue(getLevel());
		return ret;
	}

	/**
	 * Returns the effective defense factor.
	 * 
	 * @return The effective defense factor.
	 */
	public int getDefenseFactor()
	{
		int ret = (int) defenseFactor;
		if (armorCl != null)
			ret += armorCl.getDefValue(getLevel());
		return ret;
	}

	/**
	 * Returns the experience points the bug currently has.
	 * 
	 * @return The amount of experience this bug has.
	 */
	public int getExperience()
	{
		return experience;
	}

	/**
	 * Returns the experience value of this bug.
	 * 
	 * @return How much experience this bug is worth.
	 */
	public int getExperienceValue()
	{
		return this.level * 3 + 1;
	}

	/**
	 * Returns the current hit points of the bug.
	 * 
	 * @return How much hp the bug has.
	 */
	public int getHp()
	{
		return bugHp;
	}

	/**
	 * Returns the team of the bug.
	 * 
	 * @return The color team the bug is on.
	 */
	public Color getInitColor()
	{
		return initalColor;
	}

	/**
	 * Returns the current level of the bug
	 * 
	 * @return The bug's currnet level.
	 */
	public int getLevel()
	{
		return level;
	}

	/**
	 * Returns the max hit points of the bug.
	 * 
	 * @return The bug's maximum Hp.
	 */
	public int getMaxHp()
	{
		return maxHp;
	}

	/**
	 * Returns the overlay to be drawn over the bug.
	 * 
	 * @return The image to be drawn over the bug.
	 */
	public Image getOverlay()
	{
		BufferedImage b = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) b.getGraphics();

		g2.translate(24, 24);
		g2.rotate(Math.toRadians(getDirection()));
		if (armorCl != null)
			g2.drawImage(armorCl.getImage(getLevel()), -24, -24, null);
		if (weaponCl != null)
			g2.drawImage(weaponCl.getImage(getLevel()), -24, -24, null);
		return b;
	}

	/**
	 * Returns the status manager of the bug.
	 * 
	 * @return The bug's StatusManager.
	 */
	public StatusManager getStatusManager()
	{
		return myStatus;
	}

	/**
	 * Checks to se if a bug has a status ailment of the given type.
	 * 
	 * @param status
	 *            instance of the status to be checked for
	 * @return true if the bug has the specified status, false otherwise.
	 */
	public boolean hasStatus(Modifier status)
	{
		return myStatus.hasModifier(status);
	}

	/**
	 * Used to set the bugs base stats.
	 */
	protected void initStats()
	{
		maxHp = 50;
		damageFactor = 2;
		defenseFactor = 2;
		runHpPercent = .3;
	}

	/**
	 * Find out if the bug fainted.
	 * 
	 * @return True if the bug is fainted, false otherwise.
	 */
	public boolean isFainted()
	{
		return fainted;
	}

	/**
	 * Levels the bug, increasing the bugs stats.
	 */
	protected void levelUp()
	{
		this.level++;
		this.bugHp += (int) (.1 * maxHp);
		this.maxHp *= 1.1;
		this.damageFactor *= 1.15;
		this.defenseFactor *= 1.15;
	}

	/**
	 * Causes the bug to randomly turn or move.
	 */
	private void normalMove()
	{
		if (Math.random() < .75)
			directMove();
		else if (Math.random() < .25)
			turn();
	}

	/**
	 * Remove HP from this bug.
	 * 
	 * @param damage
	 *            The amount og HP to remove.
	 */
	public void removeHp(int damage)
	{
		int maxNegate = (getDefenseFactor() + myStatus.getDefMod()) + 1;

		if (maxNegate <= 0)
			maxNegate = 1;
		int amount = damage - randGen.nextInt(maxNegate);
		// amount = the damage minus the defense of bug (randomized).

		if (amount < 0) // If amount is less then 0, amount set to 0
			amount = 0; // otherwise the bug would be healed.

		bugHp -= amount;

		if (bugHp <= 0)
		{
			bugHp = 0;
			die();
		}
	}

	/**
	 * Overriden so that if the bug is not in a grid, an error is not thrown.
	 */
	@Override
	public void removeSelfFromGrid()
	{
		Grid<Actor> gr = getGrid();
		if (gr == null)
			return;
		else
			super.removeSelfFromGrid();
	}

	/**
	 * Revives the bug.
	 */
	public void revive()
	{
		fainted = false;
		downTime = 0;
		if (bugHp == 0)
			bugHp = 1;
		setColor(initalColor);
	}

	/**
	 * Moves the bug away from danger if possible.
	 */
	private void runAway()
	{
		ArrayList<Location> locs = new ArrayList<Location>();
		Location temp = getLocation().getAdjacentLocation(getDirection());
		Grid<Actor> g = getGrid();
		if (g.isValid(temp) && g.get(temp) == null)
			locs.add(temp);
		temp = getLocation()
				.getAdjacentLocation(getDirection() + Location.LEFT);
		if (g.isValid(temp) && g.get(temp) == null)
			locs.add(temp);
		temp = getLocation().getAdjacentLocation(
				getDirection() + Location.RIGHT);
		if (g.isValid(temp) && g.get(temp) == null)
			locs.add(temp);
		if (locs.size() > 0)
		{
			setDirection(getLocation().getDirectionToward(
					locs.get(randGen.nextInt(locs.size()))));
			if (canMove())
				directMove();
		}
	}

	/**
	 * Sets the HP of the bug.
	 * 
	 * @param value
	 *            New HP value.
	 */
	public void setHp(int value)
	{
		bugHp = value;
		if (bugHp <= 0)
		{
			die();
		}
		else
		{
			if (bugHp > maxHp) // If bug's health is < max health
				bugHp = maxHp; // Health = max health
			if (this.isFainted())
				revive();
		}
	}

	/**
	 * Checks to see if the bug should run away.
	 * 
	 * @return true if the bug should run, false otherwise.
	 */
	public boolean shouldRun()
	{
		ArrayList<Location> temp = getGrid().getOccupiedAdjacentLocations(
				getLocation());
		// Gets all the surround locations with bugs
		ArrayList<Grunt> others = new ArrayList<Grunt>();
		// Creates a new array list of Grunt type
		for (Location l : temp)
			if (getGrid().get(l) instanceof Grunt) // If the bug is next to a
													// bug
				others.add((Grunt) getGrid().get(l)); // adds bug to the other
														// array
		boolean underAttack = false;
		for (int i = 0; i < others.size() && !underAttack; i++)
			if (others.get(i).canAttack(getLocation())) // If other bug can
														// attack this bug
				underAttack = true; // underAttack is set to true

		if (underAttack && (bugHp <= runHpPercent * maxHp)
				&& (Math.random() < .8))
			// If the bug's health is low and is under attack
			return true;
		return false;
	}

	/**
	 * Turns the bug left or right.
	 */
	private void turn()
	{
		Location current = getLocation();
		ArrayList<Location> targets = new ArrayList<Location>();
		if (getGrid() == null)
			return;
		if (getGrid().isValid(
				current.getAdjacentLocation(getDirection() + Location.LEFT)))
			targets.add(current.getAdjacentLocation(getDirection()
					+ Location.LEFT));
		if (getGrid().isValid(
				current.getAdjacentLocation(getDirection() + Location.RIGHT)))
			targets.add(current.getAdjacentLocation(getDirection()
					+ Location.RIGHT));
		if (!targets.isEmpty())
			setDirection(current.getDirectionToward(targets.get(randGen
					.nextInt(targets.size()))));

	}

	/**
	 * Get the number of bugs that are on this bug's team in the grid.
	 * 
	 * @return The number of bug on this bug's team.
	 */
	public int getColorCount()
	{
		Grid<Actor> g = getGrid();
		if (g == null || !(g instanceof ColorGrid))
			return 0;
		return ((ColorGrid) g).getCountForColor(getInitColor());
	}
}