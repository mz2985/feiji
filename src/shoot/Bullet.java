package shoot;


import java.awt.image.BufferedImage;

/**
 * �ӵ�
 * @author Administrator
 *
 */
public class Bullet extends FlyingObject{

	private static BufferedImage images;
	//��̬�����
	static {
		images = loadImage("bullet.png");
	}
	
	private int speed;//�ٶ�
	/**���췽��*/
	public Bullet(int x, int y){
		super(8, 14, x, y);
		speed = 2;
	}
	/**�ӵ����ƶ�*/
	public void step(){
		this.y -= speed;
	}
	/**��дgetImage()������ȡ��ͼƬ*/
	@Override
	public BufferedImage getImage() {
		if (isLife()) {
			return images;
		}else if(isDead()){
			state = REMOVE;//�޸�״̬Ϊɾ��״̬
		}
		return null;
	}
	/**����Ƿ�Խ��*/
	@Override
	public boolean outOfBounds() {
		return this.y <= -this.height;
	}
	
}

