/**
 * @author Ben Shecter
 * @version 1.00
 * @file Runner.java
 * 
 * @Date Created: Jan 23, 2008
 * @Date Last Edited: Feb 19, 2008
 *
 * Comments: Generates the intro box for the game.
 */

import bugFight.gui.IntroBox;

/**
 * This class brings up the IntroBox
 * 
 * @author Ben Shecter
 */

class Runner
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		IntroBox intro = new IntroBox();
		intro.setVisible(true);
	}
}