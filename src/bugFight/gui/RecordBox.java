/**
 * 
 */

package bugFight.gui;

import info.gridworld.world.World;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import bugFight.grid.ColorGrid;

/**
 * @author Joseph Stuhr
 * 
 */

@SuppressWarnings("serial")
public class RecordBox extends JFrame
{
	/**
	 * @author Joseph Stuhr
	 * 
	 */

	private JPanel contentPane;
	private ResourceBundle properties;
	private Timer timer;
	private ColorGrid cg;

	public RecordBox(World world)
	{	
		cg = (ColorGrid) world.getGrid();

		properties = ResourceBundle.getBundle("bugFight");
		
		URL appIconUrl = getClass().getResource("FightWorld.png");
		if (appIconUrl != null)
		{
			ImageIcon appIcon = new ImageIcon(appIconUrl);
			setIconImage(appIcon.getImage());
		}
		
		String title = properties.getString("recordTitle");
		setTitle(title);
		
		this.setBounds(560, 25, 200, 100);
		
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		draw();
		
		this.setResizable(false);
		
		timer = new Timer(10, new CountdownListener(this));
	}
	
	public class CountdownListener implements ActionListener
	{
		RecordBox parent;

		CountdownListener(RecordBox b)
		{
			parent = b;
		}

		public void actionPerformed(ActionEvent e)
		{
			parent.draw();
		}
	}
	
	public void draw()
	{
		contentPane.removeAll();
		
		contentPane.add(new JLabel("  Red : " + cg.getCountForColor(Color.RED),
				SwingConstants.CENTER));
		contentPane.add(new JLabel("  Blue : " + cg.getCountForColor(Color.BLUE),
				SwingConstants.CENTER));
		contentPane.add(new JLabel("  Yellow : " + cg.getCountForColor(Color.YELLOW),
				SwingConstants.CENTER));
		contentPane.add(new JLabel("  Green : " + cg.getCountForColor(Color.GREEN),
				SwingConstants.CENTER));
		
		contentPane.updateUI();
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
}