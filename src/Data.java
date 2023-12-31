
public class Data {
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
	
	public static void clearEntityList() {
		do {
			removeEntity(end.getEntity());
		} while (length != 0);
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
	private static void insertionSwap(Node nodeA, Node nodeB) {

		if (start == nodeB) {
			start = nodeA;
		}
		if (end == nodeA) {
			end = nodeB;
		}
		
		Node nodeA1 = nodeA.getNext();
		Node nodeB0 = nodeB.getPrevious();
		
		nodeB0.setNext(nodeA);
		nodeA.setNext(nodeB);
		nodeB.setNext(nodeA1);
		
		nodeA.setPrevious(nodeB0);
		nodeB.setPrevious(nodeA);
		nodeA1.setPrevious(nodeB);
	}
	
	public static void sortEntity() {
		if (length <= 1) {
			return;
		}
		if (length == 2 && start.getEntity().getBottom() > end.getEntity().getBottom()) {
			start = start.getPrevious();
			end = end.getNext();
			return;
		}
		for (int y = 1; y < length; y++) {
			for (int x = y; x > 0; x--) {
				if (traverse(x).getEntity().getBottom() < traverse(x-1).getEntity().getBottom()) { //If the first entity is at a lower position
					insertionSwap(traverse(x),traverse(x-1));
				}
			}
		}
		
		double last = start.getEntity().getBottom();
		Node node = start;
		do {
			Entity entity = node.getEntity();
			if (entity.getBottom() < last) {
				System.out.println("error " + last);
			}
			last = entity.getBottom();
			node = node.getNext();
		} while(node != start);
	}
	
	//Other
	
	public static void splashDamage(Entity source, double damage, double splashRange) {
		Node node = start;
		do {
			Entity entity = node.getEntity();
			if (entity.team == source.team) {
				if (Vector.distance(entity.getPos(),source.getPos()) < splashRange) {
					entity.takeDamage(damage);
				}
			}
			node = node.getNext();
		} while(node != start);
	}
	
	public static Entity locateNearestAlly(Entity source) {
		Node node = start;
		Entity nearest = null;
		double distance = 600.0; //Maximum distance troops can see
		do {
			Entity entity = node.getEntity();
			if (entity.team == source.team && entity != source) {
				if (Vector.distance(source.position,entity.position) < distance) {
					nearest = entity;
					distance = Vector.distance(source.position,entity.position);
				}
			}
			node = node.getNext();
		} while(node != start);
		return nearest; //Could be null
	}
	public static Entity locateNearestAlly(Entity source, String name) {
		Node node = start;
		do {
			if (node.getEntity().getName() == name) {
				if (node.getEntity().getTeam() == source.getTeam()) {
					return node.getEntity();
				}
			}
			node = node.getNext();
		} while(node != start);
		return null;
	}
	
	
	public static Entity locateNearestEnemy(Entity source) {
		Node node = start;
		Entity nearest = null;
		double distance = 600.0; //Maximum distance troops can see
		do {
			Entity entity = node.getEntity();
			if (entity.team != source.team && entity.team != 0) {
				if (Vector.distance(source.getPos(),entity.getPos()) < distance) {
					nearest = entity;
					distance = Vector.distance(source.getPos(),entity.getPos());
				}
			}
			node = node.getNext();
		} while(node != start);
		if (nearest == null) {
			nearest = locateNearestEnemy(source,"Castle");
		}
		return nearest; //Could be null
	}
	public static Entity locateNearestEnemy(Entity source, String name) {
		Node node = start;
		do {
			if (node.getEntity().getName() == name) {
				if (node.getEntity().getTeam() != source.getTeam()) {
					return node.getEntity();
				}
			}
			node = node.getNext();
		} while(node != start);
		return null;
	}

	public static void fireEntity() { //Runs through attacks
		Node node = start;
		do {
			Entity entity = node.getEntity();
			entity.fire();
			node = node.getNext();
		} while(node != start);
	}
	public static void checkEntity() {
		Node node = start;
		do {
			Entity entity = node.getEntity();
			entity.check();
			node = node.getNext();
		} while(node != start);
	}
	public static void moveEntity() {
		Node node = start;
		do {
			Entity entity = node.getEntity();
			entity.move(Time.delta());
			node = node.getNext();
		} while(node != start);
	}
	public static void drawEntity() {
		Node node = start;
		do {
			Entity entity = node.getEntity();
			entity.draw();
			node = node.getNext();
		} while(node != start);
	}
}
