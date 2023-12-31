import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Main {
	
	static int resX = 960;
	static int resY = 720;
	static boolean debug = false;
	
	public static JFrame window;
	public static Canvas canvas;
	public static BufferStrategy bufferStrategy;
	public static Graphics2D graphics;

	private static boolean play = false;
	private static boolean quit = false;
	private static int winner = -1;
	
	private static double lastClickTime = 0.0;
	
	private static ClickArea startBox = new ClickArea(160,360,320,440);
	private static ClickArea quitBox = new ClickArea(640,360,800,440);
	
	public static void main(String[] args) {
		setup();
		Time.init(120.01); //Updates the game at up to 120 times per second
		Timer spawnTimer = new Timer(5.0);

		do {
			
			clear();
			
			if (debug == true) {
				debugDisplay(); //Displays FPS and other debug printers	
			}

			if (play == true) {
				if (spawnTimer.ready()) {
					Deck.enemyCard(); //Places a random enemy card
				}
				drawBackground();				
				updateGame(); //Draws and updated the entities
				Deck.refresh(); //Draws the deck GUI
			} else {
				drawOptions();
			}
			
			if (winner != -1) {
				winnerText();
			}
			
			update();
			
			if (winner != -1) { //Wait 5 seconds before going back to the menu after the game ends
				Timer pauseTimer = new Timer(5.0);
				Data.clearEntityList();
				while (true) {
					if (pauseTimer.ready()) {
						break;
					}
					Time.waitForFrame();
				}
				winner = -1;
			}
			
			Time.waitForFrame();
			if (clickChecked == true || Time.compare(lastClickTime) > 1.0) { //Also resets the mouse click if the previous click was more than 1 second ago
				clickSafe = false;
				clickChecked = false;
			}
		} while (quit == false);
	}
	
	private static void drawBackground() {
		graphics.setColor(new Color(60,180,15)); //Ground
		graphics.fillRect(0,0,720,720);
		graphics.setColor(new Color(0,120,240)); //Water
		graphics.fillRect(0,310,720,100);
		graphics.setColor(new Color(120,80,0)); //Bridges
		graphics.fillRect(80,310,80,100);
		graphics.fillRect(560,310,80,100);
		graphics.setColor(new Color(96,80,64)); //Border
		graphics.fillRect(0,0,720,20);
		graphics.fillRect(0,700,720,20);
	}
	private static void drawOptions() {
		graphics.setColor(new Color(224,224,224)); //Background
		graphics.fillRect(0,0,resX,resY);
		graphics.setColor(new Color(0,128,0)); //Title
		graphics.fillRect(360,200,240,80);
		Text.printText("Sword Royale!", 438,236,0x000000);
		graphics.setColor(new Color(32,32,32)); //Buttons
		graphics.fillRect(160,360,160,80);
		graphics.fillRect(640,360,160,80);
		Text.printText("Start Game", 201,396,0xE0E0E0);
		Text.printText("Leave Game", 681,396,0xE0E0E0);
		if (startBox.testClick() == true) {
			initGame();
		}
		if (quitBox.testClick() == true) {
			quit = true;
			graphics.setColor(new Color(0,0,0));
			graphics.fillRect(0,0,resX,resY);
			System.out.println("Finished, please close the window.");
			Text.printText("Finished, please close the window.", 361,356,0xE0E0E0);
		}
	}
	
	private static void initGame() {
		Time.reset(); //Resets clocks and start time
		loadData();
		Deck.initCardDeck();
		play = true;
		winner = -1;
	}
	public static void endGame(int team) {
		play = false;
		winner = team;
	}
	public static void winnerText() {
		if (winner == 2) {
			Main.graphics.setColor(new Color(128,128,255));
			Main.graphics.fillRect(280,200,160,80);
			Text.printText("Blue Team Wins!", 371,236,0x000000);
		} else if (winner == 1) {
			Main.graphics.setColor(new Color(255,128,128));
			Main.graphics.fillRect(280,200,160,80);
			Text.printText("Red Team Wins!", 249,236,0x000000);
		} else {
			Main.graphics.setColor(new Color(128,128,128));
			Main.graphics.fillRect(280,200,160,80);
			Text.printText("Draw, time ran out.", 293,236,0x000000);
		}
	}
	
	public static void loadData() {

		for (int i = 0; i < 6; i++) { //Spawns 6 SwordMen in random locations, 3 for each side
			Entity entity;
			if (i % 2 + 1 == 1) {
				entity = new Troop("SwordMan", 1, (int)(Math.random() * 600) + 60, (int)(Math.random() * 290) + 410);
			} else {
				entity = new Troop("SwordMan", 2, (int)(Math.random() * 600) + 60, (int)(Math.random() * 290) + 20);
			}
			
			Data.addEntity(entity);
		}
		Entity castleBlue = new Castle("Castle", 1, 360,628);
		Data.addEntity(castleBlue);
		Entity castleRed = new Castle("Castle", 2, 360,92);
		Data.addEntity(castleRed);
		
		Deck.addEntity(new Troop("SwordMan",0));
		Deck.addEntity(new Troop("Archer",0));
		Deck.addEntity(new Troop("Knight",0));
		Deck.addEntity(new Troop("Wizard",0));
		Deck.addEntity(new Troop("Bandit",0));
		Deck.addEntity(new Troop("BattleRam",0));
		Deck.addEntity(new Troop("Helicopter",0));
		//Deck.addEntity(new Troop("Rocket",0));
	}
	
	private static void updateGame() {
		//Runs these in order to make sure everything is in sync
		Data.fireEntity(); //Actovates the Entities Attacks
		Data.checkEntity(); //Checks that the entity is still alive
		Data.moveEntity(); //Moves Entities
		Data.sortEntity();
		Data.drawEntity();
	}
	
	private static void clear() {
		bufferStrategy = canvas.getBufferStrategy();
		graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
		canvas.paint(graphics);
		graphics.setColor(Color.black);
		graphics.fillRect(0, 0, 960, 720);
		if (debug == true) {
			graphics.setColor(Color.white);
			graphics.fillRect(0, 720, 960, 10);	
		}
	}
	
	private static void update() {
		bufferStrategy.show();
		graphics.dispose();
	}
	
	public static boolean clickChecked = false; //If a ClickArea detected a mouse click
	public static boolean clickSafe = false; //Weather or not the mouse click coordinates can be read
	public static int clickX = 0;
	public static int clickY = 0;
	
	private static void setup() {
		window = new JFrame("Clash Royale");
		
		if (debug == false) {
			window.setSize((resX) + 16, (resY) + 39);
		} else {
			window.setSize((resX) + 16, (resY) + 39 + 10);
		}
		
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		canvas = new Canvas();
	
		window.add(canvas);
		KeyBoard keyboard = new KeyBoard();
		canvas.addKeyListener(keyboard);
		canvas.setFocusable(true);
		canvas.createBufferStrategy(2); //Double Buffered
		
	    canvas.addMouseListener(new MouseListener() {
	        public void mousePressed(MouseEvent me) { }
	        public void mouseReleased(MouseEvent me) { }
	        public void mouseEntered(MouseEvent me) { }
	        public void mouseExited(MouseEvent me) { }
	        public void mouseClicked(MouseEvent me) { 
	          if(me.getButton() == MouseEvent.BUTTON1) { //Left Click
	        	  clickX = KeyBoard.getMouseX();
	        	  clickY = KeyBoard.getMouseY();
	        	  clickSafe = true;
	        	  lastClickTime = Time.time();
	          }
	          if(me.getButton() == MouseEvent.BUTTON2) { //Middle Click
	        	  
	          }
	          if(me.getButton() == MouseEvent.BUTTON3) { //Right Click

	          }
	        }
	    });
	}
	
	/* Debug Things */
	
	static Timer frameMax = new Timer(1.0/6.0);
	static double frameHigh = 0.0; //Keeps track of the longest frameTimes
	static double frameDisplay = 0.0;
	
	private static void debugDisplay() {
		graphics.setColor(new Color(255,255,255));
		graphics.fillRect(0, 720, 960, 10);
		if (Time.delta() > frameHigh) { //Records the highest frameTime
			frameHigh = Time.delta();
		}
		if (frameMax.ready()) {
			frameDisplay = frameHigh; //Changes the displayed value
			frameHigh = 0.0; //Resets highest value
		}
		int frameDisplayColor = 0x000000; //Black
		if (1.0 / frameDisplay < 29.0) {
			frameDisplayColor = 0xFF0000; //Bright Red
		} else if (1.0 / frameDisplay < 59.0) {
			frameDisplayColor = 0x800000; //Dark Red
		}
		
		Text.printFloat((1.0 / frameDisplay),3,2,1, frameDisplayColor);
		
		Text.printInt(Data.getCount(), 4, 80, 0x000000);
		Text.printInt(KeyBoard.getMouseX(),4,200,0x000000);
		Text.printInt(KeyBoard.getMouseY(),4,235,0x000000);
	}
	
	//Draws a circle around an Entity to show its range
	public static void drawRange(double x, double y, double size) {
		drawRange((int)x,(int)y,(int)size);
	}
	public static void drawRange(int x, int y, int radius) {
		Main.graphics.setColor(new Color(16,16,16));
		Main.graphics.drawOval(x - radius, y - radius, radius * 2, radius * 2);
	}
	
	/* Debug Things */
}
