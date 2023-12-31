import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {
	
	private String name = null;
	BufferedImage img;
	private int resX;
	private int resY;
	
	private Color color;

	Sprite(int resX, int resY, Color color) {
		this.resX = resX;
		this.resY = resY;
		this.color = color;
	}
	Sprite(String name, Color color) {
		this.name = name;
		try {
		    img = ImageIO.read(new File(name));
			resX = img.getWidth();
			resY = img.getHeight();
			this.color = color;
		} catch (IOException e) {
			System.out.println("Failed to grab: " + name);
			System.out.println(e);
			img = null;
			resX = 48;
			resY = 64;
			this.color = new Color(255,255,255,200);
		}
	}
	Sprite(String name, double size, Color color) {
		this.name = name;
		try {
		    img = ImageIO.read(new File(name));
			resX = (int) (img.getWidth() * size);
			resY = (int) (img.getHeight() * size);
			this.color = color;
		} catch (IOException e) {
			System.out.println("Failed to grab: " + name);
			System.out.println(e);
			img = null;
			resX = (int) (48.0 * size);
			resY = (int) (64.0 * size);
			this.color = new Color(255,255,255,200);
		}
	}
	
	int getX() { return resX; }
	int getY() { return resY; }
	String getName() { return name; }
	
	void draw(int x, int y) {
		Main.graphics.setColor(color);
		Main.graphics.fillRect((x - (resX / 2)), (y - (resY / 2)), resX, resY);
	}
	void draw(double x, double y) { draw((int)x, (int)y); } //Handy Conversion
	
	void dragRender(int x, int y, boolean enoughGold) {
		if (img != null) {
			int maxY = 410;
			if (name == "rocket") { //Rocket can be placed almost anywhere
				maxY = 20;
			}
			if ((y < maxY || y > 700) || (x < 10 || x > 710)) {
				Main.graphics.setColor(new Color(255,64,64,200)); //Out of bounds
			} else if (enoughGold == false) {
				Main.graphics.setColor(new Color(255,255,64,200)); //Not enough gold
			} else {
				Main.graphics.setColor(new Color(64,64,255,200)); //Valid placement
			}
			Main.graphics.fillRect((x - (resX / 2)), (y - (resY / 2)), resX, resY);
			Main.graphics.drawImage(img, (x - (resX / 2)), (y - (resY / 2)), resX, resY, null);
		} else {
			Main.graphics.setColor(new Color(255,255,255,200));
			Main.graphics.fillRect((x - (resX / 2)), (y - (resY / 2)), resX, resY);
		}
	}
	
	void render(int x, int y) {
		if (img != null) {
			draw(x,y);
			Main.graphics.drawImage(img, (x - (resX / 2)), (y - (resY / 2)), resX, resY, null);
		} else {
			Main.graphics.setColor(new Color(255,255,255,200));
			Main.graphics.fillRect((x - (resX / 2)), (y - (resY / 2)), resX, resY);
		}
	}
	void render(double x, double y) { render((int)x, (int)y); } //Handy Conversion
	
	void display(int x, int y, int sizeX, int sizeY) {
		if (img != null) {
			//Main.graphics.setColor(color);
			//Main.graphics.fillRect(x, y, sizeX, sizeY);
			Main.graphics.drawImage(img, x, y, sizeX, sizeY, null);
		} else {
			Main.graphics.setColor(new Color(255,255,255,200));
			Main.graphics.fillRect(x, y, sizeX, sizeY);
		}
	}
}
