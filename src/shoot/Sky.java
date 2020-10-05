package shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * 天空：
 * 
 *
 */
public class Sky extends FlyingObject {

	private static BufferedImage images;

	// 静态代码块
	static {
		images = loadImage("background.png");
	}

	private int speed;// 速度
	public int y1;// 第二张图片的坐标

	/** 构造方法 */
	public Sky() {
		super(ShootMain.SCREEN_WIDTH, ShootMain.SCREEN_HEIGHT, 0, 0);
		speed = 1;
		y1 = -ShootMain.SCREEN_HEIGHT;
	}

	/** 天空移动 */
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

	/** 重写父类中的方法 */
	@Override
	public void paintObject(Graphics g) {
		g.drawImage(getImage(), x, y, null);
		g.drawImage(getImage(), x, y1, null);
	}

}

