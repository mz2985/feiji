package shoot;


import java.awt.image.BufferedImage;

/**
 * 子弹
 * @author Administrator
 *
 */
public class Bullet extends FlyingObject{

	private static BufferedImage images;
	//静态代码块
	static {
		images = loadImage("bullet.png");
	}
	
	private int speed;//速度
	/**构造方法*/
	public Bullet(int x, int y){
		super(8, 14, x, y);
		speed = 2;
	}
	/**子弹的移动*/
	public void step(){
		this.y -= speed;
	}
	/**重写getImage()方法获取到图片*/
	@Override
	public BufferedImage getImage() {
		if (isLife()) {
			return images;
		}else if(isDead()){
			state = REMOVE;//修改状态为删除状态
		}
		return null;
	}
	/**检测是否越界*/
	@Override
	public boolean outOfBounds() {
		return this.y <= -this.height;
	}
	
}

