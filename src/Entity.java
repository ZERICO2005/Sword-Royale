	import java.awt.Color;
import java.util.Random;
public class Entity {
	Weapon weapon;
	public Sprite sprite;
	int team;
	String name;
	double health = 100.0;
	double healthMax = 100.0;
	Vector position;
	Entity target;
	
	public void loadSprite(String name, double size) {
		Color color;
		if (team == 1) { //Generates a random shade of Blue, Red, or Green (Neutral) depending on the team
			color = new Color(0,(int) Math.floor(Math.random() * 96),(int) Math.floor(Math.random() * 128) + 128,64); //Blue
		} else if (team == 2) {
			color = new Color((int) Math.floor(Math.random() * 128) + 128,(int) Math.floor(Math.random() * 96),0,64); //Red
		} else {
			color = new Color((int) Math.floor(Math.random() * 32),(int) Math.floor(Math.random() * 128) + 128,(int) Math.floor(Math.random() * 32),64); //Green
		}
		sprite = new Sprite("images/" + name + ".png",size, color);
	}
	
	Entity(String name, int team, int x, int y, double size) {
		this.name = name;
		this.team = team;
		position = new Vector(x,y);
		loadSprite(name, size);
	}
	
	public double getX() { return position.getX(); }
	public double getY() { return position.getY(); }
	public Vector getPos() { return position; }
	public String getName() { return name; }
	public int getTeam() { return team; }
	
	//Updates

	void fire() {
		if (Data.exists(target) == false) {
			target = Data.locateNearestEnemy(this);
		} else if (Vector.distance(position,target.getPos()) > 256.0) { // Search for new target if current target is too far away
			target = Data.locateNearestEnemy(this);
		}
		if (weapon == null) {
			return;
		}
		if (weapon.inRange(position,target) == true) {
			weapon.attack(position,target);
			return;
		}
	}
	
	void check() { return; }
	void move(double delta) { return; }
	
	public double getBottom() {
		return position.getY() + (sprite.getY() / 2);
	}
	
	void draw() {
		if (Main.debug == true) {
			if (weapon != null) {
				Main.drawRange(position.getX(), position.getY(), weapon.range);
			}	
		}
		
		sprite.render(position.getX(), position.getY());
		int healthX = (int) (sprite.getX() / 3);
		if (healthX < 12) {
			healthX = 12;
		}
		int healthY = (int) (sprite.getX() / 16); //Size based on sprites width
		if (healthY < 3) {
			healthY = 3;
		}
		int x = (int)(position.getX() + (sprite.getX() / 2)) - ((healthX + (int)sprite.getX()) / 2);
		int y = (int)(position.getY() - (sprite.getY() / 2)) - healthY - 4;
		
		int col;
		col = (int)Math.round(255.0 * (health / healthMax));
		if ((health / healthMax) > 1.0) {
			col = 255;
		} else if ((health / healthMax) < 0.0) {
			col = 0;
		}
		
		int r = 255 - col;
		int g = col;
		int b = col / 5;
		Main.graphics.setColor(new Color(r,g,b));
		Main.graphics.fillRect(x, y, healthX, healthY);
	}
	
	void display(int x, int y, int sizeX, int sizeY) {
		sprite.display(x, y, sizeX, sizeY);
	}
	
	void takeDamage(double damage) {
		health -= damage;
		if (health <= 0.0) {
			if (name == "BattleRam") {
				Data.addEntity(new Troop("SwordMan",getTeam(),(int)position.getX() - 20,(int)position.getY()));
				Data.addEntity(new Troop("SwordMan",getTeam(),(int)position.getX() + 20,(int)position.getY()));
			}
			Data.removeEntity(this);
			if (name == "Castle") {
				Main.endGame(getTeam());
			}
		}
	}
}

class Troop extends Entity {
	double speed = 25.0; //maximum travel speed
	double acceleration = 20.0; //acceleration speed
	//double mass = 5000.0; //deceleration speed
	
	private void loadStats(String name) {
		//Loads properties based on name
		if (name.equals("SwordMan")) {
			weapon = new Sword(1.0, 40.0, 15.0);
			healthMax = 100;
			speed = 25.0;
			acceleration = 20.0;
		}
		if (name.equals("Archer")) {
			weapon = new Arrow("Arrow",3.0, 120.0, 25.0, 70.0);
			healthMax = 80;
			speed = 32.0;
			acceleration = 24.0;
		}
		if (name.equals("Knight")) {
			weapon = new Sword(1.4, 30.0, 55.0);
			healthMax = 150;
			speed = 20.0;
			acceleration = 10.0;
		}
		if (name.equals("Bandit")) {
			weapon = new Sword(0.8, 50.0, 10.0);
			healthMax = 60;
			speed = 70.0;
			acceleration = 40.0;
		}
		if (name.equals("Wizard")) {
			weapon = new Fragment("FireBall",5.0, 100.0, 45.0, 40.0, 60.0);
			healthMax = 90;
			speed = 18.0;
			acceleration = 12.0;
		}
		if (name.equals("BattleRam")) {
			weapon = new Sword(1.0, 20.0, 200.0);
			healthMax = 80;
			speed = 60.0;
			acceleration = 10.0;
		}
		if (name.equals("Helicopter")) {
			weapon = new Arrow("RPG",2.0, 80.0, 15.0, 175.0);
			healthMax = 200;
			speed = 30.0;
			acceleration = 20.0;
		}
//		if (name.equals("Rocket")) {
//			Data.splashDamage(this, 80.0, 80.0);
//			Data.removeEntity(this);
//		}
		health = healthMax;
	}

	Troop(String name, int team, int x, int y) {
		super(name, team, x, y,0.7);
		loadStats(name);
	}
	Troop(String name, int team) {
		super(name, team, 0, 0,0.7);
	}
	
	//Position
	private double pX;
	private double pY;
	
	private double vX;
	private double vY;
	
	private double angle;
	
	void fire() {
		if (name == "BattleRam") { //Battle Ram is special because it won't change targets
			target = Data.locateNearestEnemy(this,"Castle");
			if (Data.exists(target) == false) {
				return;
			}
			if (weapon.inRange(position,target) == true) {
				weapon.attack(position,target);
				Data.addEntity(new Entity("SwordMan",getTeam(),(int)position.getX() - 20,(int)position.getY(),0.7));
				Data.addEntity(new Entity("SwordMan",getTeam(),(int)position.getX() + 20,(int)position.getY(),0.7));
				Data.removeEntity(this);
				return;
			}
		} else {
			super.fire();
		}
	}
	
	void move(double delta) {
		if (Data.exists(target) == false) {
			return;
		}
		if (weapon == null) {
			return;
		}
		if (weapon.inRange(position,target) == true) {
			vX = 0.0;
			vY = 0.0;
			return;
		}
		
		// This function will activate within 10 pixels of the river (300-420)
		// Targeting a few pixels ahead of the bridge instead of the troops target
		// Except for when the troop is attacking another troop, or when the Troop is a flying helicopter
		if (position.getY() >= 300 && position.getY() <= 420 && (weapon.inRange(position,target) == false) && (name != "Helicopter")) {
			int bX; //Bridge X coordinate
			int bY; //Bridge Y coordinate
			if (position.getX() <= 360) {
				bX = 130; //Left Bridge
			} else {
				bX = 720 - 130; //Right Bridge
			}
			if (position.angle(target.getPos()) >= 0.0 && position.angle(target.getPos()) <= Math.PI) { //180.0 - 360.0
				bY = 430; //Moving Downwards
			} else {
				bY = 290; //Moving Upwards
			}
			position.move(Vector.Mag(vX,vY), delta, new Vector(bX,bY)); //Top Left
		} else {
			position.move(Vector.Mag(vX,vY), delta, target.getPos());
		}
		angle = position.angle();
		

		
		if (Vector.Mag(vX,vY) <= speed) {
			vX += acceleration * Math.cos(angle) * delta;
			vY += acceleration * Math.sin(angle) * delta;
			if (Vector.Mag(vX,vY) > speed) { //Upon overshoot
				vX = speed * Math.cos(angle);
				vY = speed * Math.sin(angle);
			}
			antiBlob();
			return;
		}
	}
	
	
	private void antiBlob() {
		double space = 36.0;
		Entity nearestAlly = Data.locateNearestAlly(this);
		if (nearestAlly == null) {
			return;
		}
		Vector nearPos = nearestAlly.getPos();
		double nX = nearPos.getX();
		double nY = nearPos.getY();
		double dis = Vector.distance(position, nearestAlly.getPos());
		if (dis < space) {
			double theta = Math.atan2(nY - pY, nX - pX);
			
			double sX = (space - dis) * Math.cos(theta);
			double sY = (space - dis) * Math.sin(theta);
			pX -= sX;
			pY -= sY;
		}	
	}
}

class Castle extends Entity {
	
	Castle(String name, int team, int x, int y) {
		super(name, team, x, y, 0.6);
		health = 1200.0;
		healthMax = 1200.0;
		weapon = new Arrow("CannonBall",3.5,200.0,21.0,105.0);
	}
	//The Castle often hides troops since it is very large
	//To counteract this, the getBottom() function is overridden to ensure the castles are always drawn at the bottom
	public double getBottom() {
		return -9999.0;
	}
}
class Projectile extends Entity {
	//Position
	protected double angle;
	
	protected double speed;
	protected double damage;
	
	Projectile(String name, Entity target, int x, int y, double damage, double speed) {
		super(name, 0, x, y, 0.8);
		//System.out.printf("%d %d %d\n",x,y,target.team);
		this.target = target;
		this.damage = damage;
		this.speed = speed;
	}
	
	void fire() {
		if (Data.exists(target) == false) {
			//System.out.println("No target, removing");
			Data.removeEntity(this);
			return;
		}
		
		if (Vector.distance(position,target.getPos()) < 6.0) {
			//System.out.printf("Shot Target %s %d %f\n", target.getName(),target.team,Vector.distance(position,target.getPos()));
			target.takeDamage(damage);
			Data.removeEntity(this);
			return;
		}
	}
	
	void move(double delta) {
		if (target == null) {
			return;
		}
		if (Data.exists(target) == false) {
			return;
		}
		position.move(speed, delta, target.getPos());
		angle = position.angle();
	}
	
	void draw() {
		sprite.render(position.getX(), position.getY());
	}
}

class Explosive extends Projectile {
	double splashRange;
	Explosive(String name, Entity target, int x, int y, double damage, double speed, double splashRange) {
		super(name, target, x, y, damage, speed);
		this.splashRange = splashRange;
		System.out.println(splashRange);
	}
	
	void fire() {
		if (Data.exists(target) == false) {
			//System.out.println("No target, removing");
			Data.removeEntity(this);
			return;
		}
		
		if (Vector.distance(position,target.getPos()) < 6.0) {
			Data.splashDamage(target,damage,splashRange);
			Data.removeEntity(this);
			return;
		}
	}
	
	void move(double delta) {
		if (target == null) {
			return;
		}
		if (Data.exists(target) == false) {
			return;
		}
		position.move(speed, delta, target.getPos());
		angle = position.angle();
	}
	
	String name = null;
	Sprite sprite = null;
}