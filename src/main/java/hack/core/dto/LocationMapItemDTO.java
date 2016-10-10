package hack.core.dto;

public class LocationMapItemDTO {

	private String ip;
	private double distance;
	private LocationMapItemType type;
	private int x;
	private int y;
	private boolean centrePoint;

	public LocationMapItemDTO() {
		super();
	}


	public LocationMapItemDTO(String ip, double distance, LocationMapItemType type, int x, int y, boolean centrePoint) {
		super();
		this.ip = ip;
		this.distance = distance;
		this.type = type;
		this.x = x;
		this.y = y;
		this.centrePoint = centrePoint;
	}


	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public LocationMapItemType getType() {
		return type;
	}

	public void setType(LocationMapItemType type) {
		this.type = type;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}


	public boolean isCentrePoint() {
		return centrePoint;
	}


	public void setCentrePoint(boolean centrePoint) {
		this.centrePoint = centrePoint;
	}

}
