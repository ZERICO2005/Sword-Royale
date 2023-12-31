import java.awt.MouseInfo;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyBoard extends KeyAdapter {
	
	public static boolean[] key = new boolean[256];
	public void keyPressed(KeyEvent e) {
    	if (e.getKeyCode() < 256) {
        	key[e.getKeyCode()] = true;
    	}
    }
     
    public void keyReleased(KeyEvent e) {
    	if (e.getKeyCode() < 256) {
        	key[e.getKeyCode()] = false;
    	}
    }

	public static boolean scan(int input) {
		if (key[input] == true) {
			return true;
		}
		return false;
	}
	public static int getMouseX() {
		return MouseInfo.getPointerInfo().getLocation().x - 8 - Main.window.getX();
	}
	public static int getMouseY() {
		return MouseInfo.getPointerInfo().getLocation().y - 31 - Main.window.getY();
	}
}