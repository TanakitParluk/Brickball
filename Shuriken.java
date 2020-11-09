package newProject;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Shuriken implements Runnable {
	public int x;
	public int y;
	public int delay;
	int angle = 0;
    int move = 0;
    boolean flag = false;
    int speed = 5;
    int xNew = 0;

	Thread t;
	public Image shuriken;
	public Shuriken(int x, int y) {
		this.x = x;
		this.y = y;
		this.shuriken = new ImageIcon("pic/shuriken.png").getImage();
	}
	public void setPosition(int x, int y){
		angle = (x - this.x)/39;
		move = 1;
	}
	public void start(int delay){
		this.delay = delay;
		t = new Thread(this);
		t.start();
	}
	
	public void setXnew(int x){
		xNew = x;
		if(xNew >= 700) xNew = 690;
		else if(xNew <= 0) xNew = 15;
	}
	public void checkDown(){
		if(y > 500 && speed != 0 && !flag){
			flag = true;
			y = 500;
		}
	}
	
	public void run() {
		try{
			while(true){
				checkDown();
				if(!flag){
					x += angle;	
					if(move == 1) y-=speed;
					else y+=speed;
					if (x < 0) angle =  -angle; //left
		            if (y < 0) move = 2; // top 
		            if (x > 670) angle = -angle; //right
				}else {
					if(x-xNew <= 10 && x-xNew >= -10 && speed != 0){
						x = xNew;
						y = 500;
						speed = 0;
					}
					else if(x > xNew) x -= speed;
					else x += speed;
					
				}
				t.sleep(delay);
			}
		}catch(Exception e){}
	}


}
