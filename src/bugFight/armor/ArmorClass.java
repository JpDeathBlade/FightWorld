/**
 * @author Ben Shecter
 * @version 1.00
 * @file ArmorClass.java
 * 
 * @Date Created: Jan 13, 2008
 * @Date Last Edited: Feb 19, 2008
 *
 * Comments: Reads the properties file and gives each bug its
 *           correct armor based on its given level.
 */

package bugFight.armor;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Random;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

/**
 * This class is the runtime interface to the information contained in a
 * properties file in the bugFight.armor package.
 * 
 * @author Ben Shecter
 */
public class ArmorClass
{
	private static Random randGen = new Random();
	
	/**
	 * Cache of instances of this class to increase memory effiency.
	 */
	static Map<String, ArmorClass> classCache = new HashMap<String, ArmorClass>();

	/**
	 * Cache of images used in the ArmorClass
	 */
	static Map<String, Image> imageCache = new HashMap<String, Image>();

	/**
	 * Prefix for level keys in the file.
	 */
	private final static String prefix = "level-";

	/**
	 * The instance's resource bundle
	 */
	private ResourceBundle properties;

	/**
	 * The array that contains information on the ArmorClasses's levels
	 */
	private LevelInfo[] attributes;

	/**
	 * Private class to hold level information.
	 * 
	 * @author Ben Shecter
	 */
	private class LevelInfo
	{
		/**
		 * Minimum lvl to use this picture/value
		 */
		final int minLevel;

		final Image picture;

		final int value;

		/**
		 * @param minLevel
		 *            The minimum lvl
		 * @param picture
		 *            The picture
		 * @param value
		 *            the value
		 */
		public LevelInfo(int minLevel, Image picture, int value)
		{
			this.minLevel = minLevel;
			this.picture = picture;
			this.value = value;
		}

	}

	/**
	 * Create a new instance of ArmorClass. This is private so cacheing or
	 * instances can be used.
	 * 
	 * @param set
	 *            The name of the set being constructed.
	 * @param prop
	 *            The resource bundle it i based off of.
	 */
	private ArmorClass(String set, ResourceBundle prop)
	{
		properties = prop;
		attributes = new LevelInfo[Integer.parseInt(properties
				.getString("levels"))];// find the number of levels in the set
		for (int i = 0; i < attributes.length; i++)// load all the levels in
		{
			Image tmp = imageCache.get(properties.getString(prefix + i
					+ ".image"));// get the image for the level from cache if
									// possible
			if (tmp == null)// if not cached, try to load it.
			{
				try
				{
					tmp = ImageIO.read(ClassLoader
							.getSystemResource("bugFight/armor/images/"
									+ properties.getString(prefix + i
											+ ".image")));
					imageCache.put(set, tmp); // cache the image
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			attributes[i] = new LevelInfo(Integer.parseInt(properties
					.getString(prefix + i + ".minLevel")), tmp, Integer
					.parseInt(properties.getString(prefix + i + ".value")));
			// create the LevelInfo object to hold the info on this level
		}
		classCache.put(set, this); // cache this set.
	}

	/**
	 * This method is used ot obtain instances of ArmorClasses. If a set has
	 * been constructed previously, the cached set is returned.
	 * 
	 * @param name
	 *            The name of the set to be created
	 * @return The ArmorClass representing the specified set.
	 */
	public static ArmorClass getSet(String name)
	{
		ArmorClass ret = classCache.get(name); // search cache for set
		if (ret == null) // if it isn't there
		{
			ResourceBundle rb; // attempt to load the resource bundle
			try
			{
				rb = ResourceBundle.getBundle("bugFight/armor/" + name);
			} catch (MissingResourceException err)
			{
				return null; // if the bundle is not there, then there is no
								// ArmorClass to be made
			}
			ret = new ArmorClass(name, rb); // Create the ArmorClass instance
			classCache.put(name, ret); // add it to the cache
		}
		return ret; // return the ArmorClass instance
	}

	/**
	 * Get the defensive value for the specified level.
	 * 
	 * @param level
	 *            The level to find the def value for
	 * @return The Defense value at the specified bug level.
	 */
	public int getDefValue(int level)
	{
		int index = attributes.length - 1;
		while (attributes[index].minLevel > level)
			index--;
		return attributes[index].value;
	}

	/**
	 * Get the image of the armor to be used at the specified level.
	 * 
	 * @param level
	 *            The level to find the image for.
	 * @return The image to be used.
	 */
	public Image getImage(int level)
	{
		int index = attributes.length - 1;
		while (attributes[index].minLevel > level)
			index--;
		return attributes[index].picture;
	}
	
	public ArmorClass evolve(int bugLevel)
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
