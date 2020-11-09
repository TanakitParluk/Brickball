package newProject;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;


class startGame extends JFrame{
	screenGame screen = new screenGame();
	
	public startGame() {
		add(screen);
		setTitle("Brick crusher!!!");
		setSize(700,600);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		new startGame();
	}
	
	

}
