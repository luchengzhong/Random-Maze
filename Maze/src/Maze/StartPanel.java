package Maze;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

class StartPanel extends JPanel
{
	private ImageIcon zmg = new ImageIcon("images/2.gif");
	private ImageIcon ali = new ImageIcon("images/ali.gif");
	private ImageIcon yxy = new ImageIcon("images/yxy.gif");
	private ImageIcon zyx = new ImageIcon("images/zyx.gif");
	private ImageIcon zlc = new ImageIcon("images/zlc.gif");
	private ImageIcon zy = new ImageIcon("images/zy.gif");
	private ImageIcon ddd = new ImageIcon("images/DDD.gif");
	private JLabel zmgLabel = new JLabel();
	private JLabel aliLabel = new JLabel();
	private JLabel yxyLabel = new JLabel();
	private JLabel zyxLabel = new JLabel();
	private JLabel zlcLabel = new JLabel();
	private JLabel zyLabel = new JLabel();
	private JLabel dddLabel = new JLabel();
	private Image bg;
	public StartPanel()
	{
		setLayout(null);
		zmg.setImage(zmg.getImage().getScaledInstance(160,70,Image.SCALE_DEFAULT));
		ali.setImage(ali.getImage().getScaledInstance(120,80,Image.SCALE_DEFAULT));
		yxy.setImage(yxy.getImage().getScaledInstance(80,40,Image.SCALE_DEFAULT));
		zyx.setImage(zyx.getImage().getScaledInstance(80,40,Image.SCALE_DEFAULT));
		zlc.setImage(zlc.getImage().getScaledInstance(80,40,Image.SCALE_DEFAULT));
		zy.setImage(zy.getImage().getScaledInstance(90,50,Image.SCALE_DEFAULT));
		ddd.setImage(ddd.getImage().getScaledInstance(80,40,Image.SCALE_DEFAULT));
		zmgLabel.setIcon(zmg);
		aliLabel.setIcon(ali);
		yxyLabel.setIcon(yxy);
		zyxLabel.setIcon(zyx);
		zlcLabel.setIcon(zlc);
		zyLabel.setIcon(zy);
		dddLabel.setIcon(ddd);
		zmgLabel.setBounds(230, 150, 160, 70);
		aliLabel.setBounds(100, 100, 120, 80);
		yxyLabel.setBounds(10,20,80,40);
		zyxLabel.setBounds(90,20,80,40);
		zlcLabel.setBounds(180,20,80,40);
		zyLabel.setBounds(250,20,80,40);
		dddLabel.setBounds(340,20,80,40);
		add(zmgLabel);
		add(aliLabel);
		add(yxyLabel);
		add(zyxLabel);
		add(zlcLabel);
		add(zyLabel);
		add(dddLabel);
		try {
			bg = ImageIO.read(new File("images/startbg.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		g.drawImage(bg, 0, 0, 500, 500, null);
	}
}