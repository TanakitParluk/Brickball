package newProject;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class screenGame extends JPanel implements MouseListener, Runnable{

	
	Image map = new ImageIcon("pic/wallpaper.png").getImage();
	Shuriken[] arrShu = new Shuriken[50];
	Thread t = new Thread(this);
	public Image shuriken = new ImageIcon("pic/shuriken.png").getImage();
	public Image brickPic = new ImageIcon("pic/brick.jpg").getImage();
	int xNew = 0;
	int yNew = 0;
	boolean flagShoot = false;
	int count = 0;
	int brick[][] = new int[12][14];
	boolean flagLose = false;
	boolean flagWin = false;
	
	
	screenGame(){
		setFocusable(true);
		addMouseListener(this);
		for(int i = 0; i < arrShu.length; i++){
			arrShu[i] = new Shuriken(700/2, 500);
		}
		for(int i = 0; i < 20; i++){
			int x = (int)(Math.random() * 6);
			int y = (int)(Math.random() * 14);
			if(brick[x][y] != 0) i--;
			else brick[x][y] = (int)(Math.random() * 100+1);
		}
		t.start();
	}
	
	
	
	public void run(){
		try{
			while(true){
				repaint();
			}
		}catch(Exception e){}
			
	}
	public void goDown(){
		for(int i = 11; i >= 0; i--){
			for(int j = 0; j < 14; j++){
				if(brick[i][j] != 0){
					if(i+1 < 12){
						brick[i+1][j] = brick[i][j];
						brick[i][j] = 0;
					}
				}
			}
		}
	}
	
	public void checkStat(){
		boolean flag = true;
		boolean flag2 = false;
			for(int i = 0; i < 12; i++){
				for(int j = 0; j < 14; j++){
					if(brick[i][j] != 0){
						flag = false;
						break;
					}
					if(i == 11 && brick[i][j] != 0) flag2 = true;
					
				}
				if(!flag) break;
			}
		if(flag) flagWin = true;
		else if(flag2) flagLose = true;
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", 2, 20));
		g.drawImage(map, 0, 0, getWidth(), getHeight(), this);
		for(int i = 0; i < 12; i++){
			for(int j = 0; j < 14; j++){
				if(brick[i][j] != 0){
					g.drawImage(brickPic, 0 + (j*50), 0 + (i*50), 50, 50, this);
					g.drawString(""+brick[i][j], (0 + (j*50)+15), (0 + (i*50)+30));
				}
			}
		}
		synchronized (arrShu) {
			for(int i = 0; i < arrShu.length; i++){
				g.drawImage(arrShu[i].shuriken, arrShu[i].x, arrShu[i].y, 50, 50, this);
				if(arrShu[i].flag){
					if(xNew == 0){
						xNew = arrShu[i].x;
						arrShu[i].setXnew(xNew);
					}else arrShu[i].setXnew(xNew);	
				}
			}
		}
		
			if(flagShoot){
				checkCanShootAgain();
			}
			checkColision();
			checkStat();
		if(flagLose) {
			g.setFont(new Font("Arial", 10, 50));
			g.drawString("LOSE", getWidth()/2-50, getHeight()/2);
		}else if(flagWin) {
			g.setFont(new Font("Arial", 10, 50));
			g.drawString("WIN", getWidth()/2-50, getHeight()/2);
		}
	}
	public boolean intersects(Shuriken shu, int i, int j) {
        int tw = 50;
        int th = 50;
        int rw = 50;
        int rh = 50;
        if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
            return false;
        }
        int tx = 0 + (j*50);
        int ty = 0 + (i*50);
        int rx = shu.x;
        int ry = shu.y;
        rw += rx;
        rh += ry;
        tw += tx;
        th += ty;
        //      overflow || intersect
        return ((rw < rx || rw > tx) &&
                (rh < ry || rh > ty) &&
                (tw < tx || tw > rx) &&
                (th < ty || th > ry));
    }
	
	public void checkColision(){
		boolean flag = false;
		for(int k = 0; k < arrShu.length; k++){
			for(int i = 0; i < 12; i++){
				for(int j = 0; j < 14; j++){
					if(brick[i][j] != 0){
						if(intersects(arrShu[k], i, j)){
							if(arrShu[i].x <= 0 + (j*50) || arrShu[i].x+ 1 >= 0 + (j*50) + 50){
                                arrShu[i].x = -arrShu[i].x;
                            }else{
                            	arrShu[i].y = -arrShu[i].y;
                            }
							brick[i][j] --;
							flag = true;
							break;
						}
					}
				}
				if(flag) break;
			}
		}
	}
	public void checkCanShootAgain(){
		int count = 0;
		for(int i = 0; i < arrShu.length; i++){
			if(arrShu[i].speed == 0) count++;
		}
		if(count >= 50){
			arrShu = new Shuriken[50];
			for(int i = 0; i < 50; i++) arrShu[i] = new Shuriken(xNew, 500);
			flagShoot = false;
			xNew = 0;
			goDown();
		}
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) {
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1){
			if(!flagLose && !flagWin && !flagShoot){
				int x = e.getX();
				int y = e.getY();
				for(int i = 0; i < arrShu.length; i++){
					arrShu[i].setPosition(x, y);
					arrShu[i].start(1+i);
				}
				flagShoot = true;
			}
			
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
