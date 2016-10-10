package hack.core.models;

import org.apache.maven.shared.utils.StringUtils;

public class Coord {

	private String type;
	private int[] coordinates;
	private int x;
	private int y;

	public Coord() {
		super();
		coordinates = new int[2];
		type = "Point";
	}

	public Coord(int x, int y) {
		super();
		coordinates = new int[2];
		setX(x);
		setY(y);
		type = "Point";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int[] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(int[] coordinates) {
		this.coordinates = coordinates;
	}

	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.coordinates[0] = x;
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int y) {
		this.coordinates[1] = y;
		this.y = y;
	}

	@Override
	public String toString() {
		return "[ " + StringUtils.leftPad("" + getX(), 5, " ") + " , " + StringUtils.leftPad("" + getY(), 5, " ")
				+ " ]";
	}

	public String generateID() {
		return getX() + "_" + getY();
	}

}
