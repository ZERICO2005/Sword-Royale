import java.awt.Color;

public class Deck {
	

	static double goldPerSecond = 18.0; // Adjusts how much gold the user gets per second
	
	static double gold = 0.0;
	static int clicked = -1;
	static ClickArea[] cardClick = new ClickArea[4];
	static ClickArea screenArea = new ClickArea(10,410,710,700);
			
	static void initCardDeck() {
		int x = Main.resX - 20 - 120;
		int y = 80;
		for (int i = 0; i < 4; i++) {
			cardClick[i] = new ClickArea(x,y,x+120,y+120);
			y += 120 + 20;
		}
	}
	
	static void refresh() {
		if (clicked != -1) {
			if (screenArea.testClick() == true) {
				if (gold >= getCardCost(traverse(clicked).getEntity().getName())) {
					gold -= getCardCost(traverse(clicked).getEntity().getName());
					useCard(clicked,Main.clickX,Main.clickY);
					clicked = -1;
				}
			} else {
				dragCard(traverse(clicked).getEntity());
			}
		}
		
		for (int i = 0; i < 4; i++) {
			if (cardClick[i].testClick()) {
				if (clicked == i) {
					clicked = -1;
				} else {
					clicked = i;
				}
			}
		}
		
		gold += Time.delta() * goldPerSecond;
		if (gold > 999.0) {
			gold = 999.0;
		}
		
		Main.graphics.setColor(new Color(128,128,128));
		Main.graphics.fillRect(Main.resX - 240, 0, 240, 60);
		int gX = 730;
		int gY = 10;
		Text.printText("Gold: ", gX, gY, 0x000000);
		Text.printInt((int)gold, 3, gX + 42,gY,0x000000);
		
		int tX = 730;
		int tY = 28;
		double timeLeft = 300.0 - Time.startTime(); // 5 Minutes
		int timeColor = 0x000000; //Black
		if (timeLeft < 5.0) {
			timeColor = 0xFF0000; //Bright Red
		} else if (timeLeft < 20.0) {
			timeColor = 0xAA0000; //Medium Red
		} else if (timeLeft < 60.0) {
			timeColor = 0x550000; //Dark Red
		}
		if (timeLeft > 0.0) {
			Text.printText("Time Left:  :  :  ", tX, tY, timeColor);
			Text.printInt((int)(timeLeft / 3600.0) % 10, 1, tX + 77,tY,timeColor);
			Text.printInt((int)(timeLeft / 60.0) % 60, 2, tX + 91,tY,timeColor);
			Text.printFloat(timeLeft % 60.0, 2, 2, tX + 112,tY,timeColor);
		} else {
			Text.printText("Time Left: 0:00:00", tX, tY, timeColor);
			Main.endGame(0); //Game ends in a draw
		}

		
		Main.graphics.setColor(new Color(192,192,192));
		Main.graphics.fillRect(Main.resX - 240, 60, 240, Main.resY - 60);
		Node node = start;
		int x = Main.resX - 20 - 120;
		int y = 80;
		int cardPrice = getCardCost(node.getEntity().getName());
		for (int i = 0; i < 4; i++) {
			if (clicked != i) {
				Main.graphics.setColor(new Color(255,255,255));
				Main.graphics.fillRect(x, y, 120, 120);
				node.getEntity().display(x,y,120,120);
			} else {
				Main.graphics.setColor(new Color(160,160,192));
				Main.graphics.fillRect(x, y, 120, 120);
				node.getEntity().display(x,y,120,120);
			}

			Main.graphics.setColor(new Color(0,0,0));
			Main.graphics.fillRect(x, y, 17, 12);
			Text.printInt(cardPrice, 2, x+2, y+2, 0xCCCC00);
			node = node.getNext();
			cardPrice = getCardCost(node.getEntity().getName());
			y += 120 + 20;
		}
		Main.graphics.setColor(new Color(255,255,255));
		Main.graphics.fillRect(x + 60, y, 60, 60);
		node.getEntity().display(x + 60, y, 60, 60);
		
		Main.graphics.setColor(new Color(0,0,0));
		Main.graphics.fillRect(x+60, y, 17, 12);
		Text.printInt(cardPrice, 2, x+62, y+2, 0xCCCC00);
		Text.printText("Next Card", 880, 626, 0x0000AA);

	}
	
	private static int getCardCost(String name) {
		int price = 10;
		if (name.equals("SwordMan")) {
			price = 20;
		}
		if (name.equals("Archer")) {
			price = 30;
		}
		if (name.equals("Knight")) {
			price = 55;
		}
		if (name.equals("Bandit")) {
			price = 40;
		}
		if (name.equals("Wizard")) {
			price = 70;
		}
		if (name.equals("BattleRam")) {
			price = 99;
		}
		if (name.equals("Helicopter")) {
			price = 80;
		}
//		if (name.equals("Rocket")) {
//			price = 100;
//		}
		return price;
	}
	
	public static void clearEntityList() {
		do {
			removeEntity(end.getEntity());
		} while (length != 0);
	}
	
	public static void dragCard(Entity card) {
		if (gold >= getCardCost(traverse(clicked).getEntity().getName())) {
			card.sprite.dragRender(KeyBoard.getMouseX(), KeyBoard.getMouseY(),true);
		} else {
			card.sprite.dragRender(KeyBoard.getMouseX(), KeyBoard.getMouseY(),false);
		}
	}
	
	public static void useCard(int index, int x, int y) {
		if (length < 5) {
			System.out.println("Critical Error: Deck is not long enough");
			return;
		}
		if (index < 0 || index >= 4) { //Cards 0,1,2,3 can be played, card 4 is preview
			return;
		}
		Node node = traverse(index);
		placeCard(node.getEntity(),KeyBoard.getMouseX(),KeyBoard.getMouseY());
		swap(node,traverse(4)); //Replace with preview card
		removeEntity(node.getEntity()); //Removes the played card, and moves the next preview card into place
		addEntity(node.getEntity()); //Adds the played card to the end of the list
	}
	
	private static void placeCard(Entity entity, int x, int y) {
		Entity place = new Troop(entity.getName(), 1, x, y);
		Data.addEntity(place);
	}
	
	public static void enemyCard() {
		Entity card;
		do {
			card = traverse((int)(Math.random() * length)).getEntity();
		} while (card.getName() == "Rocket");
		Entity place = new Troop(card.getName(), 2, (int)(Math.random() * 600) + 60, (int)(Math.random() * 290) + 20);
		Data.addEntity(place);
	}
	
	
	private static int length = 0;
	private static Node start;
	private static Node end;
	
	public static int getCount() { return length; }
	
	public static boolean exists(Entity entity) {
		if (entity == null) {
			return false;
		}
		Node node = start;
		do {
			if (node.getEntity() == entity) {
				return true;
			}
			node = node.getNext();
		} while(node != start);
		return false;
	}
	
	//Linked List Stuff
	
	private static Node find(Entity entity) {
		Node node = start;
		do {
			if (node.getEntity() == entity) {
				return node;
			}
			node = node.getNext();
		} while(node != start);
		return null;
	}
	
	private static Node traverse(int pos) {
		Node node = start;
		if (pos < 0) {
			for (int i = 0; i < Math.abs(pos); i++) {
				node = node.getPrevious();
			}
		} else {
			for (int i = 0; i < pos; i++) {
				node = node.getNext();
			}
		}
		return node;
	}
	
	public static void addEntity(Entity entity) {
		if (length == 0) {
			Node node = new Node(entity);
			node.setNext(node);
			node.setPrevious(node);
			start = node;
			end = node;
			length++;
			return;
		}
		Node node = new Node(start,end,entity);
		end.setNext(node);
		start.setPrevious(node);
		end = node;
		length++;
	}
	
	public static void removeEntity(Entity entity) {
		if (length == 0) {
			return;
		}
		if (length == 1) {
			start = null;
			end = null;
			length--;
			return;
		}
		Node node = find(entity);
		if (node == null) {
			return;
		}
		Node node0 = node.getPrevious();
		Node node1 = node.getNext();
		node0.setNext(node1);
		node1.setPrevious(node0);
		if (node == start) {
			start = node1;
		}
		if (node == end) {
			end = node0;
		}
		length--;
	}
	
	public static void swap(Node nodeA, Node nodeB) {
		if (nodeA == nodeB) {
			return;
		}
		if (start == nodeA) {
			start = nodeB;
		} else if (start == nodeB) {
			start = nodeA;
		}
		if (end == nodeA) {
			end = nodeB;
		} else if (end == nodeB) {
			end = nodeA;
		}
		if (length == 2) {
			return;
		}
		Node nodeA0 = nodeA.getPrevious();
		Node nodeA1 = nodeA.getNext();
		Node nodeB0 = nodeB.getPrevious();
		Node nodeB1 = nodeB.getNext();
		if (nodeA.getNext() != nodeB && nodeB.getNext() != nodeA) {
			nodeA0.setNext(nodeB);
			nodeA1.setPrevious(nodeB);
			nodeB.setNext(nodeA1);
			nodeB.setPrevious(nodeA0);
			nodeB0.setNext(nodeA);
			nodeB1.setPrevious(nodeA);
			nodeA.setNext(nodeB1);
			nodeA.setPrevious(nodeB0);
		} else {
			if (nodeA.getNext() == nodeB) {
				nodeA0.setNext(nodeB);
				nodeB.setNext(nodeA);
				nodeA.setNext(nodeB1);
				nodeB.setPrevious(nodeA0);
				nodeA.setPrevious(nodeB);
				nodeB1.setPrevious(nodeA);
			} else {
				nodeB0.setNext(nodeA);
				nodeA.setNext(nodeB);
				nodeB.setNext(nodeA1);
				nodeA.setPrevious(nodeB0);
				nodeB.setPrevious(nodeA);
				nodeA1.setPrevious(nodeB);
			}
		}
	}
	
}
