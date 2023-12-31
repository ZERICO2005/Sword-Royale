

public class Timer {
	
	private double timer;
	private double delay;
	
	Timer(double delay) {
		this.delay = delay;
		timer = Time.time();
	}
	
	public boolean ready() {
		if (Time.time() - timer > delay) {
			timer = Time.time();
			return true;
		};
		return false;
	}
}
