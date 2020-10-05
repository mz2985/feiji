package shoot;


import java.awt.image.BufferedImage;

/**
 * Ӣ�ۻ���
 * 
 * @author Administrator
 *
 */
public class Hero extends FlyingObject{

	private static BufferedImage[] images;
	//��̬�����
	static {
		images = new BufferedImage[2];
		for (int i = 0; i < images.length; i++) {
			images[i] = loadImage("hero" + i + ".png");
		}
	}
	//��Ա����
	private int life;//����ֵ
	private int doubleFire;//����
	
	/**���췽��*/
	public Hero() {
		super(97, 124, 140, 400);
		life = 6;
		doubleFire = 0;//��������
	}
	/**Ӣ�ۻ����ƶ�:���������ƶ�����*/
	public void movedTo(int x, int y){
		this.x = x - this.width/2;
		this.y = y - this.height/2;
	}
	/**Ӣ�ۻ����ƶ�*/
	public void step(){
		System.out.println("ͼƬ���л�");
	}
	int index = 0;
	@Override
	public BufferedImage getImage() {
//		return images[0];
		return images[index++/10%images.length];
	}
	
	/**Ӣ�ۻ������ӵ�*/
	public Bullet[] shoot(){
		int xStep = this.width/4;
		int yStep = 15;
		if (doubleFire > 0) {//˫������
			Bullet[] bs = new Bullet[2];
			bs[0] = new Bullet(this.x + xStep * 1, this.y - yStep);
			bs[1] = new Bullet(this.x + xStep * 3, this.y - yStep);
			doubleFire -= 2;
			return bs;
		}else{
			Bullet[] bs = new Bullet[1];
			bs[0] = new Bullet(this.x + xStep * 2, this.y - yStep);
			return bs;
		}
	}
	
	
	public void addDoubleFire(){
		doubleFire += 10;
	}
	
	public void addLife(){
		life++;
	}
	/**�������*/
	public int getLife(){
		return life;
	}
	
	/**��������*/
	public void substractLife(){
		life--;
	}
	/**��ջ���*/
	public void clearDoubleFire(){
		doubleFire = 0;
	}
	
}


