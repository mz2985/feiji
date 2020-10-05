package shoot;


import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * ���ࣺ������
 *
 */
public abstract class FlyingObject {

	// ��Ա����
	protected int width;// ��
	protected int height;// ��
	protected int x;// x����
	protected int y;// y����
	// �������״̬
	public static final int LIFE = 0;// ���
	public static final int DEAD = 1;// over
	public static final int REMOVE = 2;// ɾ��
	public int state = LIFE;// ��ǰ״̬Ϊ���

	/** �ṩ���ˣ�С�л�+��л�+�۷䣩 */
	public FlyingObject(int width, int height) {
		this.width = width;
		this.height = height;
		Random rand = new Random();
		// С�л����ֵ�λ��
		this.x = rand.nextInt(ShootMain.SCREEN_WIDTH - this.width);
		this.y = -this.height;
	}

	/** �ṩӢ�ۻ�+�ӵ�+��յĹ��췽�� */
	public FlyingObject(int width, int height, int x, int y) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}

	/** �ƶ��ķ��� */
	public void step() {
	}

	/** ��ȡͼƬ */
	public static BufferedImage loadImage(String fileName) {
		try {
			// ͬ��֮�ڵ�ͼƬ��ȡ
			BufferedImage img = ImageIO.read(FlyingObject.class.getResource(fileName));
			return img;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/** ��ȡͼƬ */
	public abstract BufferedImage getImage();

	/** ��ͼƬ g:���� */
	public void paintObject(Graphics g) {
		g.drawImage(this.getImage(), this.x, this.y, null);
	}

	/** �жϵ�ǰ״̬�ǲ��Ǵ��� */
	public boolean isLife() {
		return state == LIFE;
	}

	/** �жϵ�ǰ״̬�ǲ���over�� */
	public boolean isDead() {
		return state == DEAD;
	}

	/** �жϵ�ǰ״̬�ǲ���ɾ���� */
	public boolean isRemove() {
		return state == REMOVE;
	}

	/** ���Խ��ķ��� */
	public boolean outOfBounds() {
		return this.y >= ShootMain.SCREEN_HEIGHT;
	}

	/* ʵ���ӵ�����˷�����ײ other:�ӵ����� */
	public boolean hit(FlyingObject other) {
		int x1 = this.x - other.width;
		int y1 = this.y - other.height;
		int x2 = this.x + this.width;
		int y2 = this.y + this.height;
		int x = other.x;
		int y = other.y;
		return x >= x1 && x <= x2 && y >= y1 && y <= y2;
	}

	/** ������over */
	public void goDead() {
		state = DEAD;// ������״̬�޸�ΪDAED

	}

	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}

}

