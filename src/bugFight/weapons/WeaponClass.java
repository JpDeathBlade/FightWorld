/**
 * @author Ben Shecter
 * @file ArmorClass.java
 * @version Jan 13, 2008
 * 
 */
package bugFight.weapons;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Random;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

public class WeaponClass
{
	private static Random randGen = new Random();

	static Map<String, WeaponClass> classCache = new HashMap<String, WeaponClass>();

	static Map<String, Image> imageCache = new HashMap<String, Image>();

	private final static String prefix = "level-";

	private ResourceBundle properties;

	private LevelInfo[] attributes;

	private class LevelInfo
	{
		final int minLevel;

		final Image picture;

		final int value;

		/**
		 * @param minLevel
		 * @param picture
		 * @param value
		 */
		public LevelInfo(int minLevel, Image picture, int value)
		{
			this.minLevel = minLevel;
			this.picture = picture;
			this.value = value;
		}
	}

	private WeaponClass(String set, ResourceBundle prop)
	{
		properties = prop;
		attributes = new LevelInfo[Integer.parseInt(properties
				.getString("levels"))];
		for (int i = 0; i < attributes.length; i++)
		{
			Image tmp = imageCache.get(properties.getString(prefix + i
					+ ".image"));
			if (tmp == null)
			{
				try
				{
					tmp = ImageIO.read(ClassLoader
							.getSystemResource("bugFight/weapons/images/"
									+ properties.getString(prefix + i
											+ ".image")));
					imageCache.put(set, tmp);
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			attributes[i] = new LevelInfo(Integer.parseInt(properties
					.getString(prefix + i + ".minLevel")), tmp, Integer
					.parseInt(properties.getString(prefix + i + ".value")));
		}
		classCache.put(set, this);
	}

	public static WeaponClass getSet(String name)
	{
		WeaponClass ret = classCache.get(name);
		if (ret == null)
		{
			ResourceBundle rb;
			try
			{
				rb = ResourceBundle.getBundle("bugFight/weapons/" + name);
			} catch (MissingResourceException err)
			{
				return null;
			}
			ret = new WeaponClass(name, rb);
			classCache.put(name, ret);
		}
		return ret;
	}

	public int getAtkValue(int level)
	{
		int index = attributes.length - 1;
		while (attributes[index].minLevel > level)
			index--;
		return attributes[index].value;
	}

	public Image getImage(int level)
	{
		int index = attributes.length - 1;
		while (attributes[index].minLevel > level)
			index--;
		return attributes[index].picture;
	}

	public WeaponClass evolve(int bugLevel)
	{
		try
		{
			String t = properties.getString("evolve");
			if (Boolean.parseBoolean(t) == false)
				return this;

			int lvl = Integer.parseInt(properties.getString("evolve.level"));
			if (bugLevel < lvl)
				return this;

			return getSet(properties.getString("evolve.options."
					+ randGen.nextInt(Integer.parseInt(properties
							.getString("evolve.options")))));

		} catch (MissingResourceException e)
		{
		}
		return this;
	}
}