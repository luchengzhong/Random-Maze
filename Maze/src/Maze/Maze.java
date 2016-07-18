package Maze;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Maze {
	public static void main(String[] args) {
		WelcomeFrame frame = new WelcomeFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

class PlayWav {
	private AudioInputStream ais = null;
	private AudioFormat af = null;
	private DataLine.Info dli = null;
	private SourceDataLine sdl = null;

	// private final String source="scare.wav";

	public PlayWav(String source) {
		try {
			ais = AudioSystem.getAudioInputStream(new File(source));
			af = ais.getFormat();
			dli = new DataLine.Info(SourceDataLine.class, af);
			sdl = (SourceDataLine) AudioSystem.getLine(dli);
		} catch (Exception ex) {
			System.err.println(ex);
		}
	}

	public void play() {
		byte[] buff = new byte[1024];
		int len = 0;
		try {
			sdl.open(af, 1024);
			sdl.start();
			while ((len = ais.read(buff)) > 0) {
				sdl.write(buff, 0, len);
			}
			ais.close();
			sdl.drain();
			sdl.close();
		} catch (Exception ex) {
			System.err.println(ex);
		}
	}
}

class WelcomeFrame extends JFrame implements ActionListener{
	ImageIcon easyIcon = new ImageIcon("images/easy.jpg");
	ImageIcon normalIcon = new ImageIcon("images/middle.jpg");
	ImageIcon hardIcon = new ImageIcon("images/difficult.jpg");
	JButton easy = new JButton(easyIcon);
	JButton normal = new JButton(normalIcon);
	JButton hard = new JButton(hardIcon);
	StartPanel p = new StartPanel();
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public int n = 10;
	public WelcomeFrame(){
		setTitle("阿狸走迷宫");
			//   setBounds((screenSize.width-DEFAULT_WIDTH)/2,(screenSize.height-DEFAULT_HEIGHT)/2,DEFAULT_WIDTH, DEFAULT_HEIGHT);
		   		setResizable(false);
			   setBounds((screenSize.width - 500) / 2,(screenSize.height - 500) / 2,500,500);
			   // add panel to frame
			   easy.addActionListener(this);
			   normal.addActionListener(this);
			   hard.addActionListener(this);
			   easyIcon.setImage(easyIcon.getImage().getScaledInstance(100,40,Image.SCALE_DEFAULT));
			   easy.setBounds(360, 270, 100, 40);
			   p.add(easy);
		       normalIcon.setImage(normalIcon.getImage().getScaledInstance(100,40,Image.SCALE_DEFAULT));
			   normal.setBounds(360, 330, 100, 40);
			   p.add(normal);
			   hardIcon.setImage(hardIcon.getImage().getScaledInstance(100,40,Image.SCALE_DEFAULT));
			   hard.setBounds(360, 390, 100, 40);
			   p.add(hard);
			   add(p);
			   addWindowListener(new WindowAdapter(){
					public void windowClosing(WindowEvent e)
					{  
						JOptionPane.showMessageDialog(null, "感谢使用！！\n\n作者：钟路成，杨萧玉，赵恒，赵逸雪，张月");
						System.exit(0);	
					}
				});
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JButton t = (JButton)arg0.getSource();
		if(t == easy){
			n=5;
			size = 130;
		}
		else if(t == normal){
			n=10;
			size = 67;
		}
		else if(t == hard){
			n=20;
			size = 33;
		}
		GameFrame gf = new GameFrame(n,size);
		dispose();
		gf.setVisible(true);
	}
	int size;
}

class GameFrame extends JFrame implements ActionListener {
	JPanel welcome = new JPanel();
	mainpanel mainjp;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	public GameFrame(int n, int size) {
//		panel.setLayout(null);
		panel.add(back);
//		back.setBounds(0, 0, 130, 35);
		back.setPreferredSize(new Dimension(140,35));
		backIcon.setImage(backIcon.getImage().getScaledInstance(140,35,Image.SCALE_DEFAULT));
		setResizable(false);
		back.addActionListener(this);
		back.setFocusable(false);
		setTitle("阿狸走迷宫");
		this.n = n;
		this.size = size;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DEFAULT_WIDTH = size * n + 172;
		DEFAULT_HEIGHT = size * n + 50;
		setBounds((screenSize.width - DEFAULT_WIDTH) / 2,
				(screenSize.height - DEFAULT_HEIGHT) / 2 - 20, DEFAULT_WIDTH,
				DEFAULT_HEIGHT);
		mainjp = new mainpanel(n, size);
		mainjp.setFocusable(false);
		add(mainjp);
		creat c = new creat();
		c.start();
		mainjp.buttonp.add(back);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{  
				JOptionPane.showMessageDialog(null, "感谢使用！！\n\n作者：钟路成，杨萧玉，赵恒，赵逸雪，张月");
				System.exit(0);	
			}
		});
	}

	int size;
	public int n = 10;
	public int DEFAULT_WIDTH = size * n + 172;
	public int DEFAULT_HEIGHT = size * n + 50;

	class creat extends Thread {
		public void run() {
			int x;
			int y;
			int i = 0, j = 0;

			while ((i < DEFAULT_WIDTH) || (j < DEFAULT_HEIGHT)) {
				x = (screenSize.width - i) / 2;
				y = (screenSize.height - j) / 2;
				setBounds((screenSize.width - DEFAULT_WIDTH) / 2,
						(screenSize.height - DEFAULT_HEIGHT) / 2 - 20, i, j);
				i = i + 20;
				j = j + 20;
				try {
					sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			setBounds((screenSize.width - DEFAULT_WIDTH) / 2,
					(screenSize.height - DEFAULT_HEIGHT) / 2 - 20,
					DEFAULT_WIDTH, DEFAULT_HEIGHT);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JButton t = (JButton) arg0.getSource();
		if (t == back) {
			mainjp.close();
			WelcomeFrame frame = new WelcomeFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			dispose();
		}
	}

	private ImageIcon backIcon = new ImageIcon("images/back.jpg");
	JButton back = new JButton(backIcon);
	JPanel panel = new JPanel();
}

class mainpanel extends JPanel implements ActionListener {
	public GamePanel panel;
	JPanel eastp;
	JPanel buttonp = new JPanel();
	JPanel emptyp1;
	JPanel emptyp2;
	JPanel emptyp3;
	JPanel emptyp4;
	private JPanel panel1 = new JPanel();
	private JPanel panel2 = new JPanel();
	private JPanel panel3 = new JPanel();
	private ImageIcon restartIcon = new ImageIcon("images/restart.jpg");
	private ImageIcon getwayIcon = new ImageIcon("images/kaishi.jpg");
	private ImageIcon bestwayIcon = new ImageIcon("images/xianshi.jpg");
	JButton getway = new JButton();
	JButton restart = new JButton();
	JButton getbest = new JButton();
	
	public mainpanel(int n, int size) {
		panel1.setLayout(null);
		panel2.setLayout(null);
		panel3.setLayout(null);
		getway.setIcon(getwayIcon);
		restart.setIcon(restartIcon);
		getbest.setIcon(bestwayIcon);
//		restart.setSize(180,30);
		getwayIcon.setImage(getwayIcon.getImage().getScaledInstance(135,35,Image.SCALE_DEFAULT));
		restartIcon.setImage(restartIcon.getImage().getScaledInstance(135,35,Image.SCALE_DEFAULT));
		bestwayIcon.setImage(bestwayIcon.getImage().getScaledInstance(135,35,Image.SCALE_DEFAULT));
		panel1.add(restart);
		restart.setBounds(0, 0, 135, 35);
		panel2.add(getbest);
		getway.setBounds(0, 0, 135, 35);
		panel3.add(getway);
		getbest.setBounds(0, 0, 135, 35);
		setFocusable(false);
		Graph game = new Graph(n);// 等待传对象
		game.generate();
		panel = new GamePanel(game, size);
		eastp = new JPanel();
		buttonp = new JPanel();
		emptyp1 = new JPanel();
		emptyp2 = new JPanel();
		emptyp3 = new JPanel();
		emptyp4 = new JPanel();
		getway.addActionListener(this);
		restart.addActionListener(this);
		getbest.addActionListener(this);
		
		getbest.setFocusable(false);
		getway.setFocusable(false);
		restart.setFocusable(false);
		GridLayout g = new GridLayout(17, 1);
		buttonp.setLayout(g);
		buttonp.add(new JPanel());
		buttonp.add(panel1);
		buttonp.add(new JPanel());
//		buttonp.add(new JPanel());
		buttonp.add(panel2);
		buttonp.add(new JPanel());
		buttonp.add(panel3);
//		buttonp.add(new JPanel());
//		buttonp.add(new JPanel());
//		buttonp.add(new JPanel());
		buttonp.add(new JPanel());
		panel.setSize(200, 200);

		eastp.setLayout(new BorderLayout());
		eastp.setFocusable(false);
		eastp.add(panel, BorderLayout.CENTER);
		eastp.add(emptyp1, BorderLayout.EAST);
		eastp.add(emptyp2, BorderLayout.WEST);
		eastp.add(emptyp3, BorderLayout.SOUTH);
		eastp.add(emptyp4, BorderLayout.NORTH);

		setLayout(new BorderLayout());

		add(buttonp, BorderLayout.EAST);
		add(eastp, BorderLayout.CENTER);
		panel.requestFocus();
	}

	public void pgetfocus() {
		panel.requestFocus();
	}

	public void close() {
		panel.close();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton t = (JButton) e.getSource();
		if (t == getway) {
			panel.getway();
			panel.requestFocus();
		} else if (t == restart) {
			panel.newgame();
			panel.requestFocus();
		} else if (t == getbest) {
			panel.fsbest();
			panel.repaint();
		}
	}

	class findway extends Thread {
		public void run() {
			panel.getway();
		}
	}
}

class GamePanel extends JPanel {
	final int LEFT = 2;// 定义四个方向操作的值
	final int RIGHT = 0;
	final int UP = 3;
	final int DOWN = 1;
	boolean win = false;// 游戏胜利标志位
	int size = 50; // 单元格大小
	int potsize = 40;// 节点大小
	int steps = 0;// 记录步数
	int tryfail = -1;// 撞墙参数

	private JLabel lfinal = new JLabel();
	
	public void setgame(Graph g) {
		game = g;
	}

	public void close()// 关闭自动行走的进程
	{
		autogo = true;
	}

	public GamePanel(Graph g, int size) {
		setLayout(null);
		this.size = size;
		potsize = size - 10;
		setgame(g);
		try {
			image = ImageIO.read(new File("images/aaa.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		recpot = new Rectangle2D[game.N][game.N];
		recway = new Rectangle2D[count()];
		start();
		game.stack.push(game.sp[0][0].coor);// 将左上角的单元格坐标压入存储路径的栈
		getbestway();
		KeyHandler listener = new KeyHandler();
		addKeyListener(listener);// 添加键盘响应监听器
		setBackground(Color.DARK_GRAY);
		setFocusable(true);
		
		ifinal.setImage(ifinal.getImage().getScaledInstance(potsize,potsize,Image.SCALE_DEFAULT));
		lfinal.setIcon(ifinal);
		lfinal.setBounds((size - potsize) / 2 + ((g.N)-1) * size, (size - potsize) / 2 + ((g.N)-1) * size, potsize, potsize);
		add(lfinal);
	}

	public int count() {// 统计节点之间的路径总数
		int sum = 0;
		for (int i = 0; i < game.N; i++)
			for (int j = 0; j < game.N; j++) {
				sum += game.sp[i][j].left + game.sp[i][j].right
						+ game.sp[i][j].up + game.sp[i][j].down;
			}
		return sum;
	}

	public void start() {// 初始化迷宫矩阵坐标数据
		int k = 0;
		for (int i = 0; i < game.N; i++) {
			for (int j = 0; j < game.N; j++) {
				recpot[i][j] = new Rectangle2D.Double((size - potsize) / 2 + j
						* size, (size - potsize) / 2 + i * size, potsize,
						potsize);
				if (game.sp[i][j].left == 1)
					recway[k++] = new Rectangle2D.Double(j * size, i * size
							+ (size - potsize) / 2, (size - potsize) / 2,
							potsize);
				if (game.sp[i][j].right == 1)
					recway[k++] = new Rectangle2D.Double((j + 1) * size
							- (size - potsize) / 2, i * size + (size - potsize)
							/ 2, (size - potsize) / 2, potsize);
				if (game.sp[i][j].up == 1)
					recway[k++] = new Rectangle2D.Double(j * size
							+ (size - potsize) / 2, i * size, potsize,
							(size - potsize) / 2);
				if (game.sp[i][j].down == 1)
					recway[k++] = new Rectangle2D.Double(j * size
							+ (size - potsize) / 2, (i + 1) * size
							- (size - potsize) / 2, potsize,
							(size - potsize) / 2);
			}
		}
		person = new Rectangle2D.Double((size - potsize) / 2,
				(size - potsize) / 2, potsize, potsize);
	}

	public void paintComponent(Graphics g) {// 绘制迷宫的函数，每次repaint都会被调用
		super.paintComponent(g);
		int sum = count();
		Graphics2D g2 = (Graphics2D) g;
		int X, Y;
		for (int i = 0; i < game.N; i++) {// 绘制节点
			for (int j = 0; j < game.N; j++) {
				
					g2.setPaint(Color.WHITE);// 未被走过：白色
					g2.fill(recpot[i][j]);
					g2.draw(recpot[i][j]);
				if((i==game.N-1) &&(j==game.N-1)){
					break;
				}
		/*		if((!win)&&(i==game.N-1) &&(j==game.N-1)){
					X = (size - potsize) / 2 + j * size;
					Y = (size - potsize) / 2 + i * size;
					image = ifinal.getImage();
					g2.drawImage(image, X, Y, potsize + 1, potsize + 1, null);
				}*/
				if ((showbest) && (game.sp[i][j].best == 1)) {
					X = (size - potsize) / 2 + j * size;
					Y = (size - potsize) / 2 + i * size;
					image = ibest.getImage();
					g2.drawImage(image, X, Y, potsize + 1, potsize + 1, null);
				} 
				else if (game.sp[i][j].stepflag == 0) {
					X = (size - potsize) / 2 + j * size;
					Y = (size - potsize) / 2 + i * size;
					image = ipast.getImage();
					g2.drawImage(image, X, Y, potsize + 1, potsize + 1, null);// 在走过的路径上绘制脚印图片
				}
			}
		}
		for (int k = 0; k < sum; k++) {// 绘制路径
			g2.setPaint(Color.WHITE);
			g2.fill(recway[k]);
			g2.draw(recway[k]);
		}
		if (tryfail == -1) {// 绘制阿狸走路
			if(!win){
				switch (laststep) {
				case LEFT:
					image = ileft.getImage();
					break;
				case RIGHT:
					image = iright.getImage();
					break;
				case UP:
					image = iup.getImage();
					break;
				case DOWN:
					image = idown.getImage();
					break;
				}
				g2.drawImage(image, imagex, imagey, potsize, potsize, null);
			}
		} else {// 绘制阿狸撞墙
			switch (tryfail) {
			case LEFT:
				image = ifleft.getImage();
				break;
			case RIGHT:
				image = ifright.getImage();
				break;
			case UP:
				image = ifup.getImage();
				break;
			case DOWN:
				image = ifdown.getImage();
				break;
			}
			g2.drawImage(image, imagex, imagey, potsize, potsize, null);
		}
	}

	private void Move(int operation) {// 用于处理移动时的坐标计算和状态更改
		if (win)
			return;
		Coordinate temp = null;// 路径栈的缓冲项
		double X = person.getX(), Y = person.getY();
		int b = (int) ((person.getCenterX() - size / 2) / size);// 将具体坐标转化成行数和列数
		int a = (int) ((person.getCenterY() - size / 2) / size);
		switch (operation) {
		case LEFT:
			if (game.sp[a][b].left == 1) {
				laststep = LEFT;
				game.sp[a][b].stepflag = 0;
				steps++;
				if (game.stack.empty())
					game.stack.push(game.sp[a][b].coor);
				X -= size;
				temp = game.stack.pop();
				if ((game.stack.empty()) || (game.stack.lastElement().y != a)
						|| (game.stack.lastElement().x != b - 1)) {
					game.stack.push(temp);
					game.stack.push(game.sp[a][b - 1].coor);
					game.sp[a][b].flag[2] = 1;
				} else {
					game.sp[a][b].flag[2] = 0;
					game.sp[a][b - 1].flag[0] = 0;
					game.sp[a][b].stepflag = -1;
				}
				walk = new PlayMusic();
				walk.start();// 运行行走进程播放行走音乐
			} else {
				tryfail = LEFT;
				zq = new Wall();
				zq.start();// 运行撞墙进程播放撞墙音乐
				typed = true;
			}
			break;
		case RIGHT:
			if (game.sp[a][b].right == 1) {
				laststep = RIGHT;
				game.sp[a][b].stepflag = 0;
				steps++;
				if (game.stack.empty())
					game.stack.push(game.sp[a][b].coor);
				X += size;
				temp = game.stack.pop();
				if ((game.stack.empty()) || (game.stack.lastElement().y != a)
						|| (game.stack.lastElement().x != b + 1)) {
					game.stack.push(temp);
					game.stack.push(game.sp[a][b + 1].coor);
					game.sp[a][b].flag[0] = 1;
				} else {
					game.sp[a][b].flag[0] = 0;
					game.sp[a][b + 1].flag[2] = 0;
					game.sp[a][b].stepflag = -1;
				}
				walk = new PlayMusic();
				walk.start();
			} else {
				tryfail = RIGHT;
				zq = new Wall();
				zq.start();
				typed = true;
			}
			break;
		case UP:
			if (game.sp[a][b].up == 1) {
				laststep = UP;
				game.sp[a][b].stepflag = 0;
				steps++;

				if (game.stack.empty())
					game.stack.push(game.sp[a][b].coor);
				Y -= size;
				temp = game.stack.pop();
				if ((game.stack.empty())
						|| (game.stack.lastElement().y != a - 1)
						|| (game.stack.lastElement().x != b)) {
					game.stack.push(temp);
					game.stack.push(game.sp[a - 1][b].coor);
					game.sp[a][b].flag[3] = 1;
				} else {
					game.sp[a][b].flag[3] = 0;
					game.sp[a - 1][b].flag[1] = 0;
					game.sp[a][b].stepflag = -1;
				}
				walk = new PlayMusic();
				walk.start();
			} else {
				tryfail = UP;
				zq = new Wall();
				zq.start();
				typed = true;
			}
			break;
		case DOWN:
			if (game.sp[a][b].down == 1) {
				laststep = DOWN;
				game.sp[a][b].stepflag = 0;
				steps++;
				if (game.stack.empty())
					game.stack.push(game.sp[a][b].coor);
				Y += size;
				temp = game.stack.pop();
				if ((game.stack.empty())
						|| (game.stack.lastElement().y != a + 1)
						|| (game.stack.lastElement().x != b)) {
					game.stack.push(temp);
					game.stack.push(game.sp[a + 1][b].coor);
					game.sp[a][b].flag[1] = 1;
				} else {
					game.sp[a][b].flag[2] = 0;
					game.sp[a + 1][b].flag[3] = 0;
					game.sp[a][b].stepflag = -1;
				}
				walk = new PlayMusic();
				walk.start();
			} else {
				tryfail = DOWN;
				zq = new Wall();
				zq.start();
				typed = true;
			}
			break;
		}
		person.setRect(X, Y, potsize, potsize);// 用person来纪录阿狸的坐标，因为image不能setRect
		imagex = (int) X;
		imagey = (int) Y;
		if ((int) ((person.getCenterX() - size / 2) / size) == game.N - 1
				&& (int) ((person.getCenterY() - size / 2) / size) == game.N - 1) {
			win = true;// 胜利条件
			repaint();
			Win w = new Win();
			w.start();// 运行胜利进程播放胜利音乐
			iwin.setImage(iwin.getImage().getScaledInstance(potsize,potsize,Image.SCALE_DEFAULT));
			lfinal.setIcon(iwin);
			JOptionPane.showMessageDialog(this, "恭喜，你赢了！");
		}
		repaint();
	}

	private class KeyHandler implements KeyListener {
		public void keyPressed(KeyEvent event) {
			if ((typed == true) || (!autogo))
				return;
			int keyCode = event.getKeyCode();
			if (keyCode == KeyEvent.VK_LEFT)
				Move(LEFT);
			else if (keyCode == KeyEvent.VK_RIGHT)
				Move(RIGHT);
			else if (keyCode == KeyEvent.VK_UP)
				Move(UP);
			else if (keyCode == KeyEvent.VK_DOWN)
				Move(DOWN);
		}

		public void keyReleased(KeyEvent event) {
			tryfail = -1;
			typed = false;
			repaint();
		}

		public void keyTyped(KeyEvent event) {
		}
	}

	public int getsteps()// 返回纪录的步伐数
	{
		return steps;
	}

	public void getway() {
		if (!autogo) {
			autogo = true;
			return;
		} else {
			f = new findway();
			f.start();
		}
	}

	class findway extends Thread {
		int sleeptime = 300;

		public void run() {
			autogo = false;
			int i;
			int y, x;
			double Y, X;
			int flag = 0;
			Coordinate now;
			if (!game.stack.empty())
				now = game.stack.lastElement();
			else
				now = new Coordinate(0, 0);// 初始化,若栈中有则取栈中的坐标，没有则从头开始
			x = now.x;
			y = now.y;
			while (now != null) {
				if ((y == game.N - 1) && (x == game.N - 1)) {
					return;
				}
				flag = 0;
				for (i = 0; i < 4; i++) {
					if (game.sp[y][x].flag[i] == 0) {
						switch (i) {
						case 0:
							if ((game.sp[y][x].right == 1)
									&& (game.sp[y][x + 1].stepflag == -1)) {
								/*
								 * game.stack.push(game.sp[y][x].coor);
								 * game.sp[y][x].flag[i] = 1; //
								 * game.sp[y][x+1].flag[2] = 1;
								 */
								// game.stack.push(game.sp[y][x].coor);
								Move(RIGHT);
								try {
									sleep(sleeptime);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								now = game.sp[y][x + 1].coor;
								y = now.y;
								x = now.x;
								flag = 1;
							}
							break;
						case 1:
							if ((game.sp[y][x].down == 1)
									&& (game.sp[y + 1][x].stepflag == -1)) {
								/*
								 * game.stack.push(game.sp[y][x].coor);
								 * game.sp[y][x].flag[i] = 1; //
								 * game.sp[y][x+1].flag[3] = 1;
								 */
								// game.stack.push(game.sp[y][x].coor);
								Move(DOWN);
								try {
									sleep(sleeptime);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								now = game.sp[y + 1][x].coor;
								y = now.y;
								x = now.x;
								flag = 1;
							}
							break;
						case 2:
							if ((game.sp[y][x].left == 1)
									&& (game.sp[y][x - 1].stepflag == -1)) {
								/*
								 * game.stack.push(game.sp[y][x].coor);
								 * game.sp[y][x].flag[i] = 1; //
								 * game.sp[y][x+1].flag[0] = 1;
								 */
								// game.stack.push(game.sp[y][x].coor);
								Move(LEFT);
								try {
									sleep(sleeptime);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								now = game.sp[y][x - 1].coor;
								y = now.y;
								x = now.x;
								flag = 1;
							}
							break;
						case 3:
							if ((game.sp[y][x].up == 1)
									&& (game.sp[y - 1][x].stepflag == -1)) {
								/*
								 * game.stack.push(game.sp[y][x].coor);
								 * game.sp[y][x].flag[i] = 1; //
								 * game.sp[y][x+1].flag[1] = 1;
								 */
								// game.stack.push(game.sp[y][x].coor);
								Move(UP);
								try {
									sleep(sleeptime);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								now = game.sp[y - 1][x].coor;
								y = now.y;
								x = now.x;
								flag = 1;
							}
							break;
						default:
							break;
						}
					}
					if (flag == 1) {
						break;
					}
				}

				if (flag != 1) {
					if (game.stack.empty())
						now = null;
					else {
						// game.stack.pop();
						game.stack.pop();
						now = game.stack.lastElement();
						game.sp[y][x].stepflag = -1;
						if (y == now.y - 1)
							laststep = DOWN;
						else if (y == now.y + 1)
							laststep = UP;
						else if (x == now.x - 1)
							laststep = RIGHT;
						else if (x == now.x + 1)
							laststep = LEFT;
						y = now.y;
						x = now.x;
						X = (size - potsize) / 2 + x * size;
						Y = (size - potsize) / 2 + y * size;
						person.setRect(X, Y, potsize, potsize);
						imagex = (int) X;
						imagey = (int) Y;
						walk = new PlayMusic();
						walk.start();
						repaint();
						try {
							sleep(sleeptime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				if (autogo) {
					return;
				}
			}
			return;
		}
	}

	public void getbestway() {
		Graph g = game;
		int i;
		int y, x;
		double Y, X;
		int flag = 0;
		Coordinate now;
		if (!g.stack.empty())
			now = g.stack.lastElement();
		else
			now = new Coordinate(0, 0);// 初始化,若栈中有则取栈中的坐标，没有则从头开始
		x = now.x;
		y = now.y;
		while (now != null) {
			if ((y == g.N - 1) && (x == g.N - 1)) {
				while (!g.stack.empty()) {
					now = g.stack.pop();
					x = now.x;
					y = now.y;
					game.sp[y][x].best = 1;
					// System.out.println(y+","+x);
				}
				for (int k = 0; k < game.N; k++)
					for (int l = 0; l < game.N; l++) {
						for (int j = 0; j < 4; j++)
							game.sp[l][k].flag[j] = 0;
					}
				game.stack.push(new Coordinate(0, 0));
				return;
			}
			flag = 0;
			for (i = 0; i < 4; i++) {
				if (g.sp[y][x].flag[i] == 0) {
					switch (i) {
					case 0:
						if ((g.sp[y][x].right == 1)
								&& (g.sp[y][x + 1].stepflag == -1)) {
							g.sp[y][x].flag[i] = 1;
							g.sp[y][x + 1].flag[2] = 1;
							g.stack.push(g.sp[y][x + 1].coor);
							now = g.sp[y][x + 1].coor;
							y = now.y;
							x = now.x;
							flag = 1;
						}
						break;
					case 1:
						if ((g.sp[y][x].down == 1)
								&& (g.sp[y + 1][x].stepflag == -1)) {
							g.sp[y][x].flag[i] = 1;
							g.sp[y + 1][x].flag[3] = 1;
							g.stack.push(g.sp[y + 1][x].coor);
							now = g.sp[y + 1][x].coor;
							y = now.y;
							x = now.x;
							flag = 1;
						}
						break;
					case 2:
						if ((g.sp[y][x].left == 1)
								&& (g.sp[y][x - 1].stepflag == -1)) {
							g.sp[y][x].flag[i] = 1;
							g.sp[y][x - 1].flag[0] = 1;
							g.stack.push(g.sp[y][x - 1].coor);
							now = g.sp[y][x - 1].coor;
							y = now.y;
							x = now.x;
							flag = 1;
						}
						break;
					case 3:
						if ((g.sp[y][x].up == 1)
								&& (g.sp[y - 1][x].stepflag == -1)) {
							g.sp[y][x].flag[i] = 1;
							g.sp[y - 1][x].flag[1] = 1;
							g.stack.push(g.sp[y - 1][x].coor);
							now = g.sp[y - 1][x].coor;
							y = now.y;
							x = now.x;
							flag = 1;
						}
						break;
					default:
						break;
					}
				}
				if (flag == 1) {
					break;
				}
			}

			if (flag != 1) {
				if (g.stack.empty())
					now = null;
				else {
					// game.stack.pop();
					g.stack.pop();
					now = g.stack.lastElement();
					y = now.y;
					x = now.x;
				}
			}
		}
		return;
	}

	public boolean fsbest() {
		showbest = !showbest;
		return showbest;
	}

	public Graph newgame() {
		autogo = true;
		int n = game.N;
		imagex = (size - potsize) / 2;
		imagey = (size - potsize) / 2;
		game = new Graph(n);
		game.generate();
		start();
		laststep = DOWN;
		win = false;
		game.stack.push(game.sp[0][0].coor);
		getbestway();
		showbest = false;
		lfinal.setIcon(ifinal);
		repaint();
		return game;
	}

	private class PlayMusic extends Thread {
		@Override
		public void run() {
			bufa = new PlayWav("sounds/bufa.WAV");
			bufa.play();
		}
	}

	private class Wall extends Thread {
		@Override
		public void run() {
			zhuangqiang = new PlayWav("sounds/zhuangqiang.WAV");
			zhuangqiang.play();
		}
	}

	private class Win extends Thread {
		@Override
		public void run() {
			wingame = new PlayWav("sounds/win.Wav");
			wingame.play();
		}
	}

	private Graph game;
	Rectangle2D[][] recpot;
	Rectangle2D[] recway;
	Rectangle2D person;
	Coordinate[] path;
	private Image image = null;
	private ImageIcon ibest = new ImageIcon("images/best.png");
	private ImageIcon icon = new ImageIcon("images/aaa.jpg");
	private ImageIcon ileft = new ImageIcon("images/left.jpg");
	private ImageIcon iright = new ImageIcon("images/right.jpg");
	private ImageIcon iup = new ImageIcon("images/up.jpg");
	private ImageIcon idown = new ImageIcon("images/down.jpg");
	private ImageIcon ipast = new ImageIcon("images/past.jpg");
	private ImageIcon ifleft = new ImageIcon("images/fleft.jpg");
	private ImageIcon ifright = new ImageIcon("images/fright.jpg");
	private ImageIcon ifup = new ImageIcon("images/fup.jpg");
	private ImageIcon ifdown = new ImageIcon("images/fdown.jpg");
	private ImageIcon ifinal = new ImageIcon("images/final.gif");
	private ImageIcon iwin = new ImageIcon("images/hug.gif");
	
	private PlayWav bufa = new PlayWav("sounds/bufa.WAV");
	private PlayWav zhuangqiang = new PlayWav("sounds/zhuangqiang.WAV");
	private PlayMusic walk = new PlayMusic();
	private Wall zq = new Wall();
	private PlayWav wingame = new PlayWav("sounds/win.Wav");
	private findway f;
	private boolean showbest = false;
	private boolean autogo = true;
	private int laststep = DOWN;
	private int imagex = (size - potsize) / 2, imagey = (size - potsize) / 2;
	private boolean typed = false;
}

