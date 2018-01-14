/**
 * Modifier.java
 * 
 * @author Ben Shecter
 * @version Dec 19, 2007
 */
package bugFight.status;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import bugFight.bugs.Grunt;

/**
 * @author notonetothink
 * 
 */
public abstract class Modifier
{
	public static final int CONTROLING = 0;

	public static final int BUFF = 1;

	public static final int DEBUFF = 2;

	public static final int PRE_ACTION = 3;

	public static final int POST_ACTION = 4;

	protected Image img;

	protected StatusManager myManager;

	Modifier()
	{
		try
		{
			img = ImageIO.read(ClassLoader.getSystemResource(getClass()
					.getName().replace('.', '/')
					+ ".png"));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void act(Grunt g)
	{
		return;
	}

	public int getAttackMod()
	{
		return 0;
	}

	public int getDefMod()
	{
		return 0;
	}

	public Image getImage()
	{
		return img;
	}

	public int getType()
	{
		return -1;
	}

	public void setStatusManager(StatusManager mgr)
	{
		myManager = mgr;
	}

	public void passTurn()
	{
		return;
	}

	public void clear()
	{
		if (myManager != null)
			myManager.removeStatus(this);
	}
}