public class RelativeCoordinate {
	private int dx, dy;
	public RelativeCoordinate(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
		verify();
	}
	
	public RelativeCoordinate(int x1, int y1, int x2, int y2) {
		dx = x2 - x1;
		dy = y2 - y1;
		verify();
	}
	
	public int getDx() {
		return dx;
	}
	
	public int getDy() {
		return dy;
	}
	
	private void verify() {
		if(dx < -1 || dx > 1 || dy < -1 || dy > 1) {
			throw new RuntimeException("fuck");
		}
	}
}