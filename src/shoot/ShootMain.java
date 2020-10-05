package shoot;




import java.awt.AWTError;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 主函数的类：
 *
 */
public class ShootMain extends JPanel {

	public static final int SCREEN_WIDTH = 400;
	public static final int SCREEN_HEIGHT = 700;
	
	//起始状态START/RUNNING/PAUSE/GAME_OVER
	//默认起始状态
	/*
	 * 鼠标点击事件：
	 * 开始--运行   结束---开始
	 * 鼠标移动事件：
	 * 启动状态：结束----启动
	 * 暂停状态：鼠标从屏幕里划到外
	 * 运行状态：从外到内的时候
	 * 结束状态：英雄机的生命为0
	 */
	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int PAUSE = 2;
	public static final int GAME_OVER = 3;
	public int state = START;//默认状态
	public static BufferedImage start;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	//静态代码块
	static{
		start = FlyingObject.loadImage("start.png");
		pause = FlyingObject.loadImage("pause.png");
		gameover = FlyingObject.loadImage("gameover"
				+ ".png");
	}
	
	private Sky sky = new Sky();
	private Hero hero = new Hero();
	private FlyingObject[] flys = {};
	private Bullet[] bullets = {};

	/** 产生敌人对象 */
	public FlyingObject nextOne() {
		Random rand = new Random();
		int type = rand.nextInt(20);
		if (type < 5) {
			return new Airplane();
		} else if (type < 12) {
			return new Bee();
		} else {
			return new BigAirplane();
		}
	}

	/** 实现敌人入场 */
	int enterIndex = 0;
	public void enterAction() {
		enterIndex++;
		if (enterIndex % 40 == 0) {
			// 获取到敌人
			FlyingObject f = nextOne();
			// 敌人添加到数组的最后一位上
			flys = Arrays.copyOf(flys, flys.length + 1);
			flys[flys.length - 1] = f;
		}
	}

	/** 子弹入场 */
	int shootIndex = 0;
	public void shootAction() {
		shootIndex++;
		if (shootIndex % 40 == 0) {
			Bullet[] bs = hero.shoot();// 获取子弹数组
			bullets = Arrays.copyOf(bullets, bullets.length + bs.length);
			// 将产生的子弹数组放到源数组中的最后一个元素
			System.arraycopy(bs, 0, bullets, bullets.length - bs.length,
					bs.length);
		}
	}

	/** 飞行物移动 */
	public void stepAction() {
		sky.step();
		for (int i = 0; i < flys.length; i++) {
			flys[i].step();
		}
		for (int i = 0; i < bullets.length; i++) {
			bullets[i].step();
		}
	}

	/**删除越界的飞行物*/
	public void outOfBoundsAction(){
		int index = 0;//存放不越界数组的下标，个数
		//新建不越界的敌人数组
		FlyingObject[] flysLive = new FlyingObject[flys.length];
		for (int i = 0; i < flys.length; i++) {
			//获取到每一个敌人
			FlyingObject f = flys[i];
			//判断敌人是否不越界
			if (!f.outOfBounds()) {//如果不越界，
				flysLive[index] = f;
				index++;
			}
			//将不越界的敌人存放到不越界的数组中
		}
		flys = Arrays.copyOf(flysLive, index);
		//System.out.println("index == " + index);
		index = 0;
		Bullet[] bulletLive = new Bullet[bullets.length];
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			if (!b.outOfBounds()) {
				bulletLive[index] = b;
				index++;
			}
		}
		bullets = Arrays.copyOf(bulletLive, index);
	}
	
	/**子弹与敌人的碰撞*/
	int score = 0;//玩家的得分记录
	public void bulletBangAction(){
		//遍历所有的子弹
		
		for (int i = 0; i < bullets.length; i++) {
			//获取每一个子弹
			Bullet b = bullets[i];
			//遍历所有的敌人
			for (int j = 0; j < flys.length; j++) {
				//获取每一个敌人
				FlyingObject f = flys[j];
				//判断碰撞
				if (f.isLife() && b.isLife() && f.hit(b)) {
					f.goDead();//敌人over
					b.goDead();//子弹over
					
					if (f instanceof Enemy) {//如果撞上的是敌人能得分
						Enemy e = (Enemy)f;
						score += e.getScore();
					}
					//如果撞上的是奖励
					if (f instanceof Award) {
						Award a = (Award)f;
						int type = a.getAwardType();
						switch (type) {
						case Award.DOUBLE_FIRE://火力
							hero.addDoubleFire();
							break;
						case Award.LIFE://生命
							hero.addLife();
							break;
						}
					}
				}
			}
		}
	}
	
	/**英雄机与敌人发生碰撞*/
	public void heroBangAction(){
		/*
		 * 思路：
		 * 1）借助FlyingObject中的hit()方法检测碰撞
		 * 2）借助FlyingObject中的goDead()方法over
		 * 3）在Hero类中设计一个方法实现碰撞之后
		 * substractLife()减少生命， clearDoubleFire()火力清0
		 * 4) 在run()方法中实现英雄机与敌人发生碰撞
		 * heroBangAction();
		 * 遍历敌人获取到每一个敌人：
		 * 判断撞上了：
		 * 敌人over,英雄机减少生命，清空火力
		 */
		//遍历所有的敌人
		for (int i = 0; i < flys.length; i++) {
			//获取每一个敌人
			FlyingObject f = flys[i];
			if (hero.isLife() && f.isLife() && f.hit(hero)) {
				f.goDead();
				hero.substractLife();//减少生命
				hero.clearDoubleFire();//清空火力
			}
		}
	}
	/**检测游戏是否结束*/
	public void checkGameOverAction(){
		//判断英雄机的生命值，
		//如果小于0的话，游戏结束，修改状态
		if(hero.getLife() <= 0){
			state = GAME_OVER;
		}
	}
	/** 测试方法 */
	public void action() {// 数组越界、数组空指针异常
		//鼠标适配器
		MouseAdapter ma = new MouseAdapter() {
			/**重写鼠标的移动事件*/
			public void mouseMoved(MouseEvent e){
				int x = e.getX();
				int y = e.getY();
				hero.movedTo(x, y);
			}
			
			/**重写鼠标的点击事件*/
			@Override
			public void mouseClicked(MouseEvent e) {
				//根据当前状态的不同，进行相应的处理
				switch (state) {
				case START:
					state = RUNNING;
					break;
				case GAME_OVER:
					score = 0;
					sky = new Sky();
					hero = new Hero();
					flys = new FlyingObject[0];
					bullets = new Bullet[0];
					state = START;//修改状态为开始状态
					break;
				}
			}
			/**重写鼠标的移入事件*/
			@Override
			public void mouseEntered(MouseEvent e) {
				if (state == PAUSE) {
					state = RUNNING;
				}
			}
			/**重写鼠标的移出事件*/
			@Override
			public void mouseExited(MouseEvent e) {
				if (state == RUNNING) {
					state = PAUSE;
				}
			}
		};
		this.addMouseListener(ma);//处理鼠标的操作事件
		this.addMouseMotionListener(ma);//处理鼠标的移动事件
		// 定时器对象
		Timer timer = new Timer();
		int inters = 10;
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (state == RUNNING) {
					enterAction();// 敌人入场
					shootAction();// 子弹入场
					stepAction();// 飞行物移动
					outOfBoundsAction();//删除越界的飞行物
					bulletBangAction();//子弹与敌人的碰撞
					heroBangAction();//英雄机与敌人发生碰撞
					checkGameOverAction();//检测游戏是都结束
				}
				repaint();// 重绘，调用paint方法
			}
		}, inters, inters);// 计划任务
		/*
		 * 7.30 20 第一次 10 第一次---第二次
		 */
	}

	/** 画对象 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		sky.paintObject(g);
		hero.paintObject(g);
		// 画敌人
		for (int i = 0; i < flys.length; i++) {
			flys[i].paintObject(g);
		}
		for (int i = 0; i < bullets.length; i++) {
			bullets[i].paintObject(g);
		}
		
		g.drawString("分数：" + score, 30, 60);
		g.drawString("生命：" + hero.getLife(), 30, 80);
		
		switch (state) {//根据不同状态画不同的状态图
		case START://开始状态的启动图
			g.drawImage(start, 0, 0, null);
			break;
		case PAUSE://暂停状态的图
			g.drawImage(pause, 0, 0, null);
			break;
		case GAME_OVER://游戏结束的时候状态图
			g.drawImage(gameover, 0, 0, null);
			break;
		}
	}

	/** 主函数：程序的入口 */
	public static void main(String[] args) {
		ShootMain sm = new ShootMain();
		JFrame jf = new JFrame();
		jf.add(sm);// 将画布装在窗体上
		jf.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);// 窗体的大小
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLocationRelativeTo(null);// 居中显示
		jf.setVisible(true);
		sm.action();
	}
}


