package shoot;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 大敌机：
 */
public class BigAirplane extends FlyingObject implements Enemy{
	private static BufferedImage[] images;
	// 静态代码块
	static {
		images = new BufferedImage[5];
		for (int i = 0; i < images.length; i++) {
			images[i] = loadImage("bigplane" + i + ".png");
		}
	}

	int speed;// 速度

	/** 构造方法 */
	public BigAirplane() {
		super(99, 89);
		this.speed = 2;
	}

	public void step(){
		this.y += speed;
	}
	
	// 得到图片
	int index = 1;

	@Override
	public BufferedImage getImage() {// 10M
		if (isLife()) {
			return images[0];
		} else if (isDead()) {// 图片的切换
			BufferedImage img = images[index++];
			if (index == images.length) {
				state = REMOVE;
			}
			return img;
		}
		/*
		 * images[index++]; index = 1; 10M images[1] index = 2 返回images[1] 20M
		 * images[2] index = 3 返回images[2] 30M images[3] index = 4 返回images[3]
		 * 40M images[4] index = 5 返回images[4]
		 */
		return null;
	}

	/**重写得分接口*/
	@Override
	public int getScore() {
		return 3;
	}

}


