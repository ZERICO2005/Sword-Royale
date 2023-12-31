
public class Vector {
	private double pX;
	private double pY;
	private double angle;
	
	Vector(int x, int y) {
		set((double)x,(double)y);
		this.angle = 0.0;
	}
	Vector(double x, double y) {
		set(x,y);
		this.angle = 0.0;
	}
	
	double getX() { return pX; }
	double getY() { return pY; }
	
	void move(double speed, double delta, Vector target) {
		if (target == null) {
			return;
		}
		angle = Math.atan2(target.pY - pY, target.pX - pX);
		
		double vX = speed * Math.cos(angle);
		double vY = speed * Math.sin(angle);
		pX += vX * delta;
		pY += vY * delta;
	}
	void move(double speed, double angle, double delta) {
		double vX = speed * Math.cos(angle);
		double vY = speed * Math.sin(angle);
		pX += vX * delta;
		pY += vY * delta;
		this.angle = angle;
	}
	
	public void set(double x, double y) {
		this.pX = x;
		this.pY = y;
	}
	
	public static double Mag(double x, double y) {
		return Math.sqrt(x * x + y * y);
	}
	
	public static double distance(Vector a, Vector b) {
		double dA = Math.abs(a.pX - b.pX);
		double dB = Math.abs(a.pY - b.pY);
		return Math.sqrt(dA * dA + dB * dB);
	}
	
	double angle() {
		return angle;
	}
	
	double angle(Vector point) {
		if (point == null) {
			return 0.0;
		}
		return Math.atan2(point.pY - pY, point.pX - pX);
	}
	public static double angle(double x, double y) {
		return Math.atan2(y, x);
	}
	
	public void push(double speed, double delta, Vector point) {
		if (point == null) {
			return;
		}
		double theta = Math.atan2(point.pY - pY, point.pX - pX) + Math.PI;
		
		double vX = speed * Math.cos(theta);
		double vY = speed * Math.sin(theta);
		pX += vX * delta;
		pY += vY * delta;
	}
}
