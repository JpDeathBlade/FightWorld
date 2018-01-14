package bugFight.spells;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bugFight.bugs.Grunt;

public class Evaluator
{
	// various regular expressions
	public static Pattern ifPattern = Pattern
			.compile("\\Aif *\\(([a-zA-Z0-9\\.=><! ()%]+)\\)\\Z");

	public static Pattern conditional = Pattern
			.compile("\\A([a-zA-Z0-9\\.()%]+) *((?:!=)|(?:==)||(?:<=?)||(?:>=?)) *([a-zA-Z0-9\\.()%]+)\\Z");

	public static Pattern isRandomCall = Pattern
			.compile("\\Arand\\(([0-9]{1,5})\\)\\Z");

	public static Pattern isFunctionCall = Pattern
			.compile("\\A((?:caster)||(?:target))\\.((?:%hp)|(?:[a-zA-Z]+))\\(\\)\\Z");

	public static Pattern isBoolean = Pattern.compile(
			"\\A(?:true)|(?:false)\\Z", Pattern.CASE_INSENSITIVE);

	public static Pattern isNumeric = Pattern
			.compile("\\A(?:[0-9]+)|(?:\\.[0-9]+)|(?:[0-9]+\\.[0-9]+)\\Z");

	static Random randGen = new Random();

	static Grunt caster;

	static Grunt target;

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Scanner scan = new Scanner(System.in);
		Grunt a = new Grunt();
		Grunt b = new Grunt();
		while (true)
		{
			System.out.println(eval(scan.nextLine(), a, b));
		}
	}

	public static boolean eval(String input, Grunt cas, Grunt tar)
	{
		Matcher m = null;
		caster = cas;
		target = tar;

		m = ifPattern.matcher(input);
		if (m.matches())
		{
			m = conditional.matcher(m.group(1));
			if (m.matches())
			{
				Object a = null, b = null;
				Matcher m2 = isNumeric.matcher(m.group(1));
				if (m2.matches())
					a = Double.valueOf(m.group(1));
				else
				{
					m2 = isBoolean.matcher(m.group(1));
					if (m2.matches())
						a = Boolean.valueOf(m.group(1));
					else
					{
						m2 = isRandomCall.matcher(m.group(1));
						if (m2.matches())
							a = Double.valueOf(randGen.nextInt(Integer
									.parseInt(m2.group(1))));
						else
						{
							m2 = isFunctionCall.matcher(m.group(1));
							if (m2.matches())
								a = doFunctionCall(m.group(1));
						}
					}
				}

				m2 = isNumeric.matcher(m.group(3));
				if (m2.matches())
					b = Double.valueOf(m.group(3));
				else
				{
					m2 = isBoolean.matcher(m.group(3));
					if (m2.matches())
						b = Boolean.valueOf(m.group(3));
					else
					{
						m2 = isRandomCall.matcher(m.group(3));
						if (m2.matches())
							b = randGen.nextInt(Integer.parseInt(m.group(3)));
						else
						{
							m2 = isFunctionCall.matcher(m.group(3));
							if (m2.matches())
								b = doFunctionCall(m.group(3));
						}
					}
				}
				// System.out.println("a: " + a);
				// System.out.println("b: " + b);
				if (a != null && b != null)
				{
					String t = m.group(2);
					try
					{
						boolean result = false;
						if (t.equals("=="))
							result = equal(a, b);
						else if (t.equals("!="))
							result = notEqual(a, b);
						else if (Comparable.class
								.isAssignableFrom(a.getClass())
								&& Comparable.class.isAssignableFrom(b
										.getClass()))
						{
							Comparable ac = (Comparable) a, bc = (Comparable) b;
							if (t.equals(">"))
								result = greaterThan(ac, bc);
							else if (t.equals(">="))
								result = greaterThanEquals(ac, bc);
							else if (t.equals("<"))
								result = lessThan(ac, bc);
							else if (t.equals("<="))
								result = lessThanEquals(ac, bc);
						}

						return result;
					} catch (ClassCastException e)
					{
						System.out.println("a: " + a.getClass().getName());
						System.out.println("b: " + b.getClass().getName());
						System.out.println("bad cast");
					}
				}
			}
			else
				System.out.println("conditional: false");
		}
		else
			System.out.println("if: false");
		return false;
	}

	public static Object doFunctionCall(String quiery)
	{
		Matcher m = isFunctionCall.matcher(quiery);
		if (m.matches())
		{
			Grunt g = null;
			if (m.group(1).equals("caster"))
				g = caster;
			else
				g = target;
			if (m.group(2).equals("%hp"))
				return Double.valueOf(100 * g.getHp() / (double) g.getMaxHp());
			Class cl = g.getClass();
			try
			{
				Method func = cl.getMethod(m.group(2));
				Object ret = func.invoke(g,(Object)null);
				if (ret.getClass().equals(Integer.class))
					ret = Double.valueOf(((Integer) ret).doubleValue());
				return ret;
			} catch (SecurityException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static boolean lessThanEquals(Comparable a, Comparable b)
	{
		if (a.compareTo(b) == 0)
			return true;
		if (a.compareTo(b) == -1)
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public static boolean lessThan(Comparable a, Comparable b)
	{
		if (a.compareTo(b) == -1)
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public static boolean greaterThanEquals(Comparable a, Comparable b)
	{
		if (a.compareTo(b) == 0)
			return true;
		if (a.compareTo(b) == 1)
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public static boolean greaterThan(Comparable a, Comparable b)
	{
		if (a.compareTo(b) == 1)
			return true;
		return false;
	}

	public static boolean equal(Object a, Object b)
	{
		return a.equals(b);
	}

	public static boolean notEqual(Object a, Object b)
	{
		return !a.equals(b);
	}
}