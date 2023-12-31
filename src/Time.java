
public class Time {
	
	//This is the only class that will access System.nanoTime, that way all time related functions will be in sync with each other
	
	//Time in seconds
	private static double time;
	private static double delta;
	private static double startTime;
	
	//Time in nanoseconds
	private static long frameTime;
	private static long systemTime;
	private static long systemDelta;
	private static long frameStart;
	
	private static long startTimeStamp;
	
	static public void init(double fps) {
		startTimeStamp = System.nanoTime();
		startTime = (double)startTimeStamp / 1000000000.0;
		
		frameTime = (long)(1000000000.0 / fps);
		frameStart = System.nanoTime();
		
		do {
			systemTime = System.nanoTime();
			systemDelta = systemTime - frameStart;
		} while (systemDelta < frameTime);
		
		time = (double)systemTime / 1000000000.0;
		delta = (double)systemDelta / 1000000000.0;
		frameStart = System.nanoTime();
	}
	
	static public void reset() { //Resets the games start time
		startTimeStamp = System.nanoTime();
		startTime = (double)startTimeStamp / 1000000000.0;
	}
	
	public static void waitForFrame() {
		do {
			systemTime = System.nanoTime();
			systemDelta = systemTime - frameStart;
		} while (systemDelta < frameTime);
		
		time = (double)systemTime / 1000000000.0;
		delta = (double)systemDelta / 1000000000.0;
		frameStart = System.nanoTime();
	}
	
	public static double delta() {
		return delta;
	}
	public static double time() {
		return time;
	}
	public static double startTime() {
		return time - startTime;
	}
	public static double compare(double timer) {
		return (time - timer);
	}
	public boolean ready(double timer, double delay) {
		if (time - timer > delay) {
			timer = Time.time();
			return true;
		};
		return false;
	}
}
