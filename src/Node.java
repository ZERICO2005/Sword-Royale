
public class Node {
	private Node next;
	private Node previous;
	private Entity entity;
	
	Node(Node next, Node previous, Entity entity) {
		this.next = next;
		this.previous = previous;
		this.entity = entity;
	}
	Node(Entity entity) {
		this.entity = entity;
	}
	Entity getEntity() { return entity; }
	Node getNext() { return next; }
	Node getPrevious() { return previous; }
	void setEntity(Entity entity) { this.entity = entity; }
	void setNext(Node node) { this.next = node; }
	void setPrevious(Node node) { this.previous = node; }
}

/*


public class Node {
	private Node next;
	private Node previous;

	Node(Node next, Node previous) {
		this.next = next;
		this.previous = previous;
	}
	
	Node getNext() { return next; }
	Node getPrevious() { return previous; }
	
	void setNext(Node node) { this.next = node; }
	void setPrevious(Node node) { this.previous = node; }
}

class EntityNode extends Node {
	
	private Entity entity;
	
	EntityNode(Node next, Node previous, Entity entity) {
		super(next,previous);
		this.entity = entity;
	}
	EntityNode(Entity entity) {
		super(null,null);
		this.entity = entity;
	}
	Entity getEntity() { return entity; }
	void setEntity(Entity entity) { this.entity = entity; }
}

*/