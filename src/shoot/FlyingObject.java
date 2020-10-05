package shoot;


import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * 父类：飞行物
 *
 */
public abstract class FlyingObject {

	// 成员变量
	protected int width;// 宽
	protected int height;// 高
	protected int x;// x坐标
	protected int y;// y坐标
	// 设计三种状态
	public static final int LIFE = 0;// 存活
	public static final int DEAD = 1;// over
	public static final int REMOVE = 2;// 删除
	public int state = LIFE;// 当前状态为存活

	/** 提供敌人（小敌机+大敌机+蜜蜂） */
	public FlyingObject(int width, int height) {
		this.width = width;
		this.height = height;
		Random rand = new Random();
		// 小敌机出现的位置
		this.x = rand.nextInt(ShootMain.SCREEN_WIDTH - this.width);
		this.y = -this.height;
	}

	/** 提供英雄机+子弹+天空的构造方法 */
	public FlyingObject(int width, int height, int x, int y) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}

	/** 移动的方法 */
	public void step() {
	}

	/** 读取图片 */
	public static BufferedImage loadImage(String fileName) {
		try {
			// 同包之内的图片读取
			BufferedImage img = ImageIO.read(FlyingObject.class.getResource(fileName));
			return img;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/** 获取图片 */
	public abstract BufferedImage getImage();

	/** 画图片 g:画笔 */
	public void paintObject(Graphics g) {
		g.drawImage(this.getImage(), this.x, this.y, null);
	}

	/** 判断当前状态是不是存活的 */
	public boolean isLife() {
		return state == LIFE;
	}

	/** 判断当前状态是不是over的 */
	public boolean isDead() {
		return state == DEAD;
	}

	/** 判断当前状态是不是删除的 */
	public boolean isRemove() {
		return state == REMOVE;
	}

	/** 检测越界的方法 */
	public boolean outOfBounds() {
		return this.y >= ShootMain.SCREEN_HEIGHT;
	}

	/* 实现子弹与敌人发生碰撞 other:子弹敌人 */
	public boolean hit(FlyingObject other) {
		int x1 = this.x - other.width;
		int y1 = this.y - other.height;
		int x2 = this.x + this.width;
		int y2 = this.y + this.height;
		int x = other.x;
		int y = other.y;
		return x >= x1 && x <= x2 && y >= y1 && y <= y2;
	}

	/** 飞行物over */
	public void goDead() {
		state = DEAD;// 将对象状态修改为DAED

	}

	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}

}

