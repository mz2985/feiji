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
 * ���������ࣺ
 *
 */
public class ShootMain extends JPanel {

	public static final int SCREEN_WIDTH = 400;
	public static final int SCREEN_HEIGHT = 700;
	
	//��ʼ״̬START/RUNNING/PAUSE/GAME_OVER
	//Ĭ����ʼ״̬
	/*
	 * ������¼���
	 * ��ʼ--����   ����---��ʼ
	 * ����ƶ��¼���
	 * ����״̬������----����
	 * ��ͣ״̬��������Ļ�ﻮ����
	 * ����״̬�����⵽�ڵ�ʱ��
	 * ����״̬��Ӣ�ۻ�������Ϊ0
	 */
	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int PAUSE = 2;
	public static final int GAME_OVER = 3;
	public int state = START;//Ĭ��״̬
	public static BufferedImage start;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	//��̬�����
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

	/** �������˶��� */
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

	/** ʵ�ֵ����볡 */
	int enterIndex = 0;
	public void enterAction() {
		enterIndex++;
		if (enterIndex % 40 == 0) {
			// ��ȡ������
			FlyingObject f = nextOne();
			// ������ӵ���������һλ��
			flys = Arrays.copyOf(flys, flys.length + 1);
			flys[flys.length - 1] = f;
		}
	}

	/** �ӵ��볡 */
	int shootIndex = 0;
	public void shootAction() {
		shootIndex++;
		if (shootIndex % 40 == 0) {
			Bullet[] bs = hero.shoot();// ��ȡ�ӵ�����
			bullets = Arrays.copyOf(bullets, bullets.length + bs.length);
			// ���������ӵ�����ŵ�Դ�����е����һ��Ԫ��
			System.arraycopy(bs, 0, bullets, bullets.length - bs.length,
					bs.length);
		}
	}

	/** �������ƶ� */
	public void stepAction() {
		sky.step();
		for (int i = 0; i < flys.length; i++) {
			flys[i].step();
		}
		for (int i = 0; i < bullets.length; i++) {
			bullets[i].step();
		}
	}

	/**ɾ��Խ��ķ�����*/
	public void outOfBoundsAction(){
		int index = 0;//��Ų�Խ��������±꣬����
		//�½���Խ��ĵ�������
		FlyingObject[] flysLive = new FlyingObject[flys.length];
		for (int i = 0; i < flys.length; i++) {
			//��ȡ��ÿһ������
			FlyingObject f = flys[i];
			//�жϵ����Ƿ�Խ��
			if (!f.outOfBounds()) {//�����Խ�磬
				flysLive[index] = f;
				index++;
			}
			//����Խ��ĵ��˴�ŵ���Խ���������
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
	
	/**�ӵ�����˵���ײ*/
	int score = 0;//��ҵĵ÷ּ�¼
	public void bulletBangAction(){
		//�������е��ӵ�
		
		for (int i = 0; i < bullets.length; i++) {
			//��ȡÿһ���ӵ�
			Bullet b = bullets[i];
			//�������еĵ���
			for (int j = 0; j < flys.length; j++) {
				//��ȡÿһ������
				FlyingObject f = flys[j];
				//�ж���ײ
				if (f.isLife() && b.isLife() && f.hit(b)) {
					f.goDead();//����over
					b.goDead();//�ӵ�over
					
					if (f instanceof Enemy) {//���ײ�ϵ��ǵ����ܵ÷�
						Enemy e = (Enemy)f;
						score += e.getScore();
					}
					//���ײ�ϵ��ǽ���
					if (f instanceof Award) {
						Award a = (Award)f;
						int type = a.getAwardType();
						switch (type) {
						case Award.DOUBLE_FIRE://����
							hero.addDoubleFire();
							break;
						case Award.LIFE://����
							hero.addLife();
							break;
						}
					}
				}
			}
		}
	}
	
	/**Ӣ�ۻ�����˷�����ײ*/
	public void heroBangAction(){
		/*
		 * ˼·��
		 * 1������FlyingObject�е�hit()���������ײ
		 * 2������FlyingObject�е�goDead()����over
		 * 3����Hero�������һ������ʵ����ײ֮��
		 * substractLife()���������� clearDoubleFire()������0
		 * 4) ��run()������ʵ��Ӣ�ۻ�����˷�����ײ
		 * heroBangAction();
		 * �������˻�ȡ��ÿһ�����ˣ�
		 * �ж�ײ���ˣ�
		 * ����over,Ӣ�ۻ�������������ջ���
		 */
		//�������еĵ���
		for (int i = 0; i < flys.length; i++) {
			//��ȡÿһ������
			FlyingObject f = flys[i];
			if (hero.isLife() && f.isLife() && f.hit(hero)) {
				f.goDead();
				hero.substractLife();//��������
				hero.clearDoubleFire();//��ջ���
			}
		}
	}
	/**�����Ϸ�Ƿ����*/
	public void checkGameOverAction(){
		//�ж�Ӣ�ۻ�������ֵ��
		//���С��0�Ļ�����Ϸ�������޸�״̬
		if(hero.getLife() <= 0){
			state = GAME_OVER;
		}
	}
	/** ���Է��� */
	public void action() {// ����Խ�硢�����ָ���쳣
		//���������
		MouseAdapter ma = new MouseAdapter() {
			/**��д�����ƶ��¼�*/
			public void mouseMoved(MouseEvent e){
				int x = e.getX();
				int y = e.getY();
				hero.movedTo(x, y);
			}
			
			/**��д���ĵ���¼�*/
			@Override
			public void mouseClicked(MouseEvent e) {
				//���ݵ�ǰ״̬�Ĳ�ͬ��������Ӧ�Ĵ���
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
					state = START;//�޸�״̬Ϊ��ʼ״̬
					break;
				}
			}
			/**��д���������¼�*/
			@Override
			public void mouseEntered(MouseEvent e) {
				if (state == PAUSE) {
					state = RUNNING;
				}
			}
			/**��д�����Ƴ��¼�*/
			@Override
			public void mouseExited(MouseEvent e) {
				if (state == RUNNING) {
					state = PAUSE;
				}
			}
		};
		this.addMouseListener(ma);//�������Ĳ����¼�
		this.addMouseMotionListener(ma);//���������ƶ��¼�
		// ��ʱ������
		Timer timer = new Timer();
		int inters = 10;
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (state == RUNNING) {
					enterAction();// �����볡
					shootAction();// �ӵ��볡
					stepAction();// �������ƶ�
					outOfBoundsAction();//ɾ��Խ��ķ�����
					bulletBangAction();//�ӵ�����˵���ײ
					heroBangAction();//Ӣ�ۻ�����˷�����ײ
					checkGameOverAction();//�����Ϸ�Ƕ�����
				}
				repaint();// �ػ棬����paint����
			}
		}, inters, inters);// �ƻ�����
		/*
		 * 7.30 20 ��һ�� 10 ��һ��---�ڶ���
		 */
	}

	/** ������ */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		sky.paintObject(g);
		hero.paintObject(g);
		// ������
		for (int i = 0; i < flys.length; i++) {
			flys[i].paintObject(g);
		}
		for (int i = 0; i < bullets.length; i++) {
			bullets[i].paintObject(g);
		}
		
		g.drawString("������" + score, 30, 60);
		g.drawString("������" + hero.getLife(), 30, 80);
		
		switch (state) {//���ݲ�ͬ״̬����ͬ��״̬ͼ
		case START://��ʼ״̬������ͼ
			g.drawImage(start, 0, 0, null);
			break;
		case PAUSE://��ͣ״̬��ͼ
			g.drawImage(pause, 0, 0, null);
			break;
		case GAME_OVER://��Ϸ������ʱ��״̬ͼ
			g.drawImage(gameover, 0, 0, null);
			break;
		}
	}

	/** ���������������� */
	public static void main(String[] args) {
		ShootMain sm = new ShootMain();
		JFrame jf = new JFrame();
		jf.add(sm);// ������װ�ڴ�����
		jf.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);// ����Ĵ�С
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLocationRelativeTo(null);// ������ʾ
		jf.setVisible(true);
		sm.action();
	}
}


