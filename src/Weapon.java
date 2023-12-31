
public class Weapon {

	private Timer attackTimer;
	protected double range;
	protected double damage;
	
	Weapon(double attackRate, double range, double damage) {
		attackTimer = new Timer(attackRate);
		this.range = range;
		this.damage = damage;
	}
	
	public void attack(Vector position, Entity target) { //If no weapon
		return;
	}

	public boolean inRange(Vector position, Entity target) {
		if (Data.exists(target) == false) {
			return false;
		}
		if (Vector.distance(position,target.getPos()) <= range) {
			return true;
		}
		return false;
	}
	public boolean canAttack(Vector position, Entity target) {
		if (target == null) {
			return false;
		}
		if (Data.exists(target) == false) {
			return false;
		}
		if (inRange(position, target) == false) {
			return false;
		}
		if (attackTimer.ready()) {
			return true;
		}
		return false;
	}
	
}
class Sword extends Weapon {
	
	Sword(double attackRate, double range, double damage) {
		super(attackRate,range,damage);
	}
	
	public void attack(Vector position, Entity target) {
		if (canAttack(position,target)) {
			target.takeDamage(damage);
		}
	}
}
class Arrow extends Weapon {
	
	private String name;
	private double speed;
	Arrow(String name, double attackRate, double range, double arrowDamage, double arrowSpeed) {
		super(attackRate,range,arrowDamage);
		this.name = name;
		this.speed = arrowSpeed;
	}
	public void attack(Vector position, Entity target) {
		if (canAttack(position,target)) {
			int x = (int) position.getX();
			int y = (int) position.getY();
			Projectile arrow = new Projectile(name,target,x,y,damage,speed);
			Data.addEntity(arrow);
		}
	}

}
class Fragment extends Weapon {
	private String name;
	private double speed;
	private double splashRange;
	Fragment(String name, double attackRate, double range, double splashDamage, double speed, double splashRange) {
		super(attackRate,range,splashDamage);
		this.name = name;
		this.speed = speed;
		this.splashRange = splashRange;
	}
	public void attack(Vector position, Entity target) {
		if (canAttack(position,target)) {
			int x = (int) position.getX();
			int y = (int) position.getY();
			Explosive bomb = new Explosive(name,target,x,y,damage,speed,splashRange);
			Data.addEntity(bomb);
		}
	}

}