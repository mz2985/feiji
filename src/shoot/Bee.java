package shoot;


import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * �۷䣺
 * 
 *
 */
public class Bee extends FlyingObject implements Award{

	private static BufferedImage[] images;

	// ��̬�����
	static {
		images = new BufferedImage[5];
		for (int i = 0; i < images.length; i++) {
			images[i] = loadImage("bee" + i + ".png");
		}
	}

	private int xSpeed;// x������ٶ�
	private int ySpeed;// y������ٶ�
	private int awardType;// ��ȡ����������

	public Bee() {
		super(29, 39);
		xSpeed = 1;
		ySpeed = 2;
		Random rand = new Random();
		awardType = rand.nextInt(2);// 0 1
	}

	public void step() {
		x += xSpeed;
		y += ySpeed;
		if (x < 0 || x >= ShootMain.SCREEN_WIDTH - this.width) {
			xSpeed *= -1;
		}

	}

	// �õ�ͼƬ
	int index = 1;

	@Override
	public BufferedImage getImage() {// 10M
		if (isLife()) {
			return images[0];
		} else if (isDead()) {// ͼƬ���л�
			BufferedImage img = images[index++];
			if (index == images.length) {
				state = REMOVE;
			}
			return img;
		}

		return null;
	}

	@Override
	public int getAwardType() {
		// TODO Auto-generated method stub
		return awardType;
	}

}
