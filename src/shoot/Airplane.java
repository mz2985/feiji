package shoot;


import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
/**
 * С�л���
 * @author Administrator
 *
 */
public class Airplane extends FlyingObject implements Enemy{

	private static BufferedImage[] images;
	//��̬�����
	static {
		images = new BufferedImage[5];
		for (int i = 0; i < images.length; i++) {
			images[i] = loadImage("airplane" + i + ".png");
		}
	}
	private int speed;//�ٶ�
	
	/**���췽��*/
	public Airplane(){
		super(49, 36);
		this.speed = 5;
	}
	
	/**�ƶ�*/
	public void step(){
		this.y += speed;
	}

	//�õ�ͼƬ
	int index = 1;
	@Override
	public BufferedImage getImage() {//10M
		if (isLife()) {
			return images[0];
		}else if(isDead()){//ͼƬ���л�
			BufferedImage img = images[index++];
			if (index == images.length) {
				state = REMOVE;
			}
			return img;
		}
		/*images[index++];
		 *   index = 1;
		 *   10M  images[1] index = 2   ����images[1]
		 *   20M  images[2] index = 3   ����images[2]
		 *   30M  images[3] index = 4   ����images[3]
		 * 	 40M  images[4] index = 5   ����images[4]
		 * 
		 */
		return null;
	}

	@Override
	public int getScore() {
		return 1;
	}
}

