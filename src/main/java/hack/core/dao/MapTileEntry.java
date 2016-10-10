package hack.core.dao;

import org.bson.types.ObjectId;

import hack.core.models.Coord;

public class MapTileEntry {

	private String ip;
	private Coord coord;
	private ObjectId player;
	private boolean isNpc;

	public MapTileEntry() {
		super();
	}

	public MapTileEntry(String ip, Coord coord, ObjectId player, boolean isNpc) {
		super();
		this.ip = ip;
		this.coord = coord;
		this.player = player;
		this.isNpc = isNpc;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Coord getCoord() {
		return coord;
	}

	public void setCoord(Coord coord) {
		this.coord = coord;
	}

	public ObjectId getPlayer() {
		return player;
	}

	public void setPlayer(ObjectId player) {
		this.player = player;
	}

	public boolean isNpc() {
		return isNpc;
	}

	public void setNpc(boolean isNpc) {
		this.isNpc = isNpc;
	}


}
