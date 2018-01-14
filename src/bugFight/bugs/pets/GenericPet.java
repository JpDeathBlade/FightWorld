/**
 * 
 */

package bugFight.bugs.pets;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import bugFight.bugs.Grunt;
import bugFight.bugs.Hunter;

/**
 * @author notonetothink
 * 
 */
public abstract class GenericPet
{
	private int remainingUses;

	private Image myImage;

	private static Map<String, Image> imageCache = new HashMap<String, Image>();

	public GenericPet()
	{
		myImage = imageCache.get(getClass().getSimpleName());
		if (myImage == null)
		{
			try
			{
				myImage = ImageIO.read(ClassLoader
						.getSystemResource("bugFight/bugs/pets/"
								+ getClass().getSimpleName()));
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			imageCache.put(getClass().getSimpleName(), myImage);
		}
	}

	int getRemainingUses()
	{
		return remainingUses;
	}

	Image getImage()
	{
		return myImage;
	}

	public abstract void use(Hunter user, Grunt target);
}
