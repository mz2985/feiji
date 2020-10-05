package shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * ��գ�
 * 
 *
 */
public class Sky extends FlyingObject {

	private static BufferedImage images;

	// ��̬�����
	static {
		images = loadImage("background.png");
	}

	private int speed;// �ٶ�
	public int y1;// �ڶ���ͼƬ������

	/** ���췽�� */
	public Sky() {
		super(ShootMain.SCREEN_WIDTH, ShootMain.SCREEN_HEIGHT, 0, 0);
		speed = 1;
		y1 = -ShootMain.SCREEN_HEIGHT;
	}

	/** ����ƶ� */
	public void step() {
		y += speed;
		y1 += speed;
		if (y >= ShootMain.SCREEN_HEIGHT) {
			y = -ShootMain.SCREEN_HEIGHT;
		}
		if (y1 >= ShootMain.SCREEN_HEIGHT) {
			y1 = -ShootMain.SCREEN_HEIGHT;
		}
	}

	@Override
	public BufferedImage getImage() {
		return images;
	}

	/** ��д�����еķ��� */
	@Override
	public void paintObject(Graphics g) {
		g.drawImage(getImage(), x, y, null);
		g.drawImage(getImage(), x, y1, null);
	}

}

