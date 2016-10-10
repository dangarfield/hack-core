package hack.core.dto;

import java.util.Date;

import hack.core.models.Location;

public class LocationStealMoneyDTO {

	private Location location;
	private Date cooldownTime;

	public LocationStealMoneyDTO() {
		super();
	}

	public LocationStealMoneyDTO(Location location, Date cooldownTime) {
		super();
		this.location = location;
		this.cooldownTime = cooldownTime;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Date getCooldownTime() {
		return cooldownTime;
	}

	public void setCooldownTime(Date cooldownTime) {
		this.cooldownTime = cooldownTime;
	}


}
