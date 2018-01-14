/**
 * @author Ben Shecter
 * @file ArmorClass.java
 * @version Jan 13, 2008
 * 
 */
package bugFight.spells;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import bugFight.bugs.Grunt;

public class SpellClass
{
	private static Random randGen = new Random();

	private static Map<String, SpellClass> classCache = new HashMap<String, SpellClass>();

	private static Map<String, Constructor> constructorCache = new HashMap<String, Constructor>();

	private Map<String, String> conditionalCache;

	private final static String prefix = "level-";

	private ResourceBundle properties;

	private LevelInfo[] attributes;

	private class LevelInfo
	{
		final int minLevel;

		final ArrayList<String> allySpells;

		final ArrayList<String> enemySpells;

		public LevelInfo(final int minLevel,
				final ArrayList<String> allySpells,
				final ArrayList<String> enemySpells)
		{
			this.minLevel = minLevel;
			this.allySpells = allySpells;
			this.enemySpells = enemySpells;
		}

	}

	private SpellClass(String set, ResourceBundle prop)
	{
		conditionalCache = new HashMap<String, String>();
		properties = prop;
		attributes = new LevelInfo[Integer.parseInt(properties
				.getString("levels"))];
		ClassLoader cLoader = ClassLoader.getSystemClassLoader();
		for (int i = 0; i < attributes.length; i++)
		{
			StringTokenizer st = new StringTokenizer(properties
					.getString(prefix + i + ".ally"));
			ArrayList<String> aTemp = new ArrayList<String>();
			while (st.hasMoreTokens())
			{
				String temp = st.nextToken();
				if (temp.contains(":"))
				{
					String[] at = temp.split(":");
					if (conditionalCache.get(at[1]) == null)
						conditionalCache.put(at[1], at[0]);
					temp = at[1];
				}
				try
				{
					if (constructorCache.get(temp) == null)
						constructorCache.put(temp, cLoader.loadClass(
								"bugFight.spells." + temp).getConstructor(
								Grunt.class));
					aTemp.add(temp);
				} catch (SecurityException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			st = new StringTokenizer(properties
					.getString(prefix + i + ".enemy"));
			ArrayList<String> eTemp = new ArrayList<String>();
			while (st.hasMoreTokens())
			{
				String temp = st.nextToken();
				if (temp.contains(":"))
				{
					String[] at = temp.split(":");
					if (conditionalCache.get(at[1]) == null)
						conditionalCache.put(at[1], at[0]);
					temp = at[1];
				}
				try
				{
					if (constructorCache.get(temp) == null)
						constructorCache.put(temp, cLoader.loadClass(
								"bugFight.spells." + temp).getConstructor(
								Grunt.class));
					eTemp.add(temp);
				} catch (SecurityException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			attributes[i] = new LevelInfo(Integer.parseInt(properties
					.getString(prefix + i + ".minLevel")), aTemp, eTemp);
		}
		classCache.put(set, this);
	}

	@SuppressWarnings("unchecked")
	public Constructor<? extends GenericSpell> getAllySpellConstructor(
			Grunt caster, Grunt target)
	{
		int index = attributes.length - 1;
		while (attributes[index].minLevel > caster.getLevel())
			index--;
		if (attributes[index].allySpells.size() == 0)
			return null;
		ArrayList<Constructor> temp = new ArrayList<Constructor>();
		for (String s : attributes[index].allySpells)
		{
			String cond = conditionalCache.get(s);
			if (cond == null)
				temp.add(constructorCache.get(s));
			else if (Evaluator.eval(cond, caster, target))
				temp.add(constructorCache.get(s));
		}
		if (temp.size() == 0)
			return null;
		return temp.get(randGen.nextInt(temp.size()));
	}

	@SuppressWarnings("unchecked")
	public Constructor<? extends GenericSpell> getEnemySpellConstructor(
			Grunt caster, Grunt target)
	{
		int index = attributes.length - 1;
		while (attributes[index].minLevel > caster.getLevel())
			index--;
		if (attributes[index].enemySpells.size() == 0)
			return null;
		ArrayList<Constructor> temp = new ArrayList<Constructor>();
		for (String s : attributes[index].enemySpells)
		{
			String cond = conditionalCache.get(s);
			if (cond == null)
				temp.add(constructorCache.get(s));
			else if (Evaluator.eval(cond, caster, target))
				temp.add(constructorCache.get(s));
		}
		if (temp.size() == 0)
			return null;
		return temp.get(randGen.nextInt(temp.size()));
	}

	public static SpellClass getSet(String name)
	{
		SpellClass ret = classCache.get(name);
		if (ret == null)
		{
			ResourceBundle rb;
			try
			{
				rb = ResourceBundle.getBundle("bugFight/spells/" + name);
			} catch (MissingResourceException err)
			{
				return null;
			}
			ret = new SpellClass(name, rb);
			classCache.put(name, ret);
		}
		return ret;
	}

	public SpellClass evolve(int bugLevel)
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