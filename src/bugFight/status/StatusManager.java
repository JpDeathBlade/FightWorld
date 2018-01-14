/**
 * StatusManager.java
 * 
 * @author Ben Shecter
 * @version Dec 19, 2007
 */
package bugFight.status;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import bugFight.bugs.Grunt;

/**
 * @author notonetothink
 * 
 */
public class StatusManager
{
	private Grunt bug;

	private Modifier[] mods;

	public StatusManager(Grunt g)
	{
		bug = g;
		mods = new Modifier[5];
	}

	public int getAttackMod()
	{
		int sum = 0;
		if (mods[Modifier.BUFF] != null)
			sum += mods[Modifier.BUFF].getAttackMod();
		if (mods[Modifier.DEBUFF] != null)
			sum += mods[Modifier.DEBUFF].getAttackMod();
		return sum;
	}

	public int getDefMod()
	{
		int sum = 0;
		if (mods[Modifier.BUFF] != null)
			sum += mods[Modifier.BUFF].getDefMod();
		if (mods[Modifier.DEBUFF] != null)
			sum += mods[Modifier.DEBUFF].getDefMod();
		return sum;
	}

	public void runPreAction()
	{
		if (mods[Modifier.PRE_ACTION] != null)
			mods[Modifier.PRE_ACTION].act(bug);
	}

	public void runPostAction()
	{
		if (mods[Modifier.POST_ACTION] != null)
			mods[Modifier.POST_ACTION].act(bug);
	}

	public void addStatus(Modifier mod)
	{
		int type = mod.getType();
		if (type >= 0)
		{
			mods[type] = mod;
			mod.setStatusManager(this);
		}
	}

	public boolean hasModifier(Modifier m)
	{
		int type = m.getType();
		if (type >= 0)
			if (mods[type] != null)
				return mods[type].getClass().equals(m.getClass());
		return false;
	}

	public void removeStatus(Modifier mod)
	{
		int type = mod.getType();

		if (type >= 0)
			if (mods[type].getClass().equals(mod.getClass()))
				mods[type] = null;
	}

	public void removeAllStatus()
	{
		for (int i = 0; i < mods.length; i++)
			mods[i] = null;
	}

	public void turnPassed()
	{
		for (int i = 0; i < mods.length; i++)
			if (mods[i] != null)
				mods[i].passTurn();
	}

	public Image getStatusImage()
	{
		BufferedImage img = new BufferedImage(48, 16,
				BufferedImage.TYPE_INT_ARGB);
		if (!bug.isFainted())
		{
			Graphics2D g2 = img.createGraphics();
			if (mods[Modifier.BUFF] != null)
				g2
						.drawImage(mods[Modifier.BUFF].getImage(), 0, 0, 16,
								16, null);
			if (mods[Modifier.DEBUFF] != null)
				g2.drawImage(mods[Modifier.DEBUFF].getImage(), 16, 0, 16, 16,
						null);
			if (mods[Modifier.POST_ACTION] != null)
				g2.drawImage(mods[Modifier.POST_ACTION].getImage(), 32, 0, 16,
						16, null);
		}
		return img;
	}
}