/**
 * 
 */

package bugFight.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import bugFight.GameTypes;

/**
 * @author notonetothink
 * 
 */

@SuppressWarnings("serial")
public class IntroBox extends JFrame
{
	/**
	 * @author notonetothink
	 * 
	 */
	public class ImagePane extends JComponent
	{
		Image img;

		public ImagePane(Image img)
		{
			super();
			this.img = img;
			this.setSize(img.getWidth(null), img.getHeight(null) + 10);
			this.setPreferredSize(new Dimension(img.getWidth(null), img
					.getHeight(null) + 10));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		@Override
		protected void paintComponent(Graphics g)
		{
			g.drawImage(img, 0, 0, null);
		}

	}

	public class QuickstartListener implements ActionListener
	{
		IntroBox parent;

		QuickstartListener(IntroBox b)
		{
			parent = b;
		}

		public void actionPerformed(ActionEvent e)
		{
			parent.quickStart();
		}
	}

	public class CountdownListener implements ActionListener
	{
		IntroBox parent;

		CountdownListener(IntroBox b)
		{
			parent = b;
		}

		public void actionPerformed(ActionEvent e)
		{
			parent.countDown();
		}
	}

	private Timer timer;

	private ResourceBundle properties;

	private int timeRemaining;

	private JPanel contentPane;

	private JButton quickstartButton;

	private int timeSpace;

	public IntroBox()
	{
		properties = ResourceBundle.getBundle("bugFight");
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		Image titleImage = null;
		
		URL appIconUrl = getClass().getResource("FightWorld.png");
		if (appIconUrl != null)
		{
			ImageIcon appIcon = new ImageIcon(appIconUrl);
			setIconImage(appIcon.getImage());
		}
		
		String title = "" + properties.getString("introTitle") + ": "
			+ properties.getString("version");
		setTitle(title);
		
		try
		{
			titleImage = ImageIO.read(ClassLoader.getSystemResource(properties
					.getString("titleImage")));
			setSize(titleImage.getWidth(null) + 50,
					titleImage.getHeight(null) + 60);

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timeRemaining = Integer.parseInt(properties.getString("timeout"));
		contentPane.add(new ImagePane(titleImage));
		contentPane.add(new JLabel("Authors: Ben Shecter & Joe Stuhr",
				SwingConstants.CENTER));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel buttonBar = new JPanel();
		buttonBar.setLayout(new BoxLayout(buttonBar, BoxLayout.X_AXIS));
		timeSpace = Integer.toString(timeRemaining).length();
		quickstartButton = new JButton();
		updateCountdown();
		quickstartButton.addActionListener(new QuickstartListener(this));
		buttonBar.add(Box.createHorizontalGlue());
		buttonBar.add(quickstartButton);
		JButton temp = new JButton("Four Corners");
		temp.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				killWindow();
				GameTypes.fourCorners();
			}

		});
		buttonBar.add(temp);
		temp = new JButton("Chess");
		temp.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				killWindow();
				GameTypes.chess();
			}

		});
		buttonBar.add(temp);
		temp = new JButton("Exit");
		temp.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent arg0)
			{
				System.exit(0);
			}

		});
		buttonBar.add(temp);
		buttonBar.add(Box.createHorizontalGlue());
		contentPane.add(buttonBar);
		timer = new Timer(1000, new CountdownListener(this));
		
		this.setLocation(25, 25);
		this.setResizable(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Window#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean b)
	{
		if (b)
			timer.start();
		else
			timer.stop();
		super.setVisible(b);
	}

	public void updateCountdown()
	{
		String filler = "";
		for (int i = 0; i < (timeSpace - Integer.toString(timeRemaining)
				.length()); i++)
			filler += "  ";
		quickstartButton.setText("Quick Start: " + filler + timeRemaining);
	}

	public void quickStart()
	{
		killWindow();
		bugFight.GameTypes.quickStart();
	}

	public void killWindow()
	{
		timer.stop();
		this.setVisible(false);
		this.dispose();
	}

	public void countDown()
	{
		timeRemaining--;
		if (timeRemaining == 0)
			quickStart();
		else
			updateCountdown();
	}
}