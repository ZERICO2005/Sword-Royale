
public class ClickArea {
	private int x0,y0,x1,y1;
	ClickArea(int x0, int y0, int x1, int y1) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
	}
	void setClickArea(int x0, int y0, int x1, int y1) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
	}
	boolean testClick() {
		if (Main.clickSafe == false) {
			return false;
		}
		if (Main.clickX < x0 || Main.clickX > x1) {
			return false;
		}
		if (Main.clickY < y0 || Main.clickY > y1) {
			return false;
		}
		Main.clickChecked = true;
		return true;
	}
}
