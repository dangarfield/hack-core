package hack.core.dto;

import java.util.List;

import hack.core.models.Location;

public class LocationMapDTO {

	private Location location;
	private List<LocationMapItemDTO> items;
	
	public LocationMapDTO() {
		super();
	}

	public LocationMapDTO(Location location, List<LocationMapItemDTO> items) {
		super();
		this.location = location;
		this.items = items;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public List<LocationMapItemDTO> getItems() {
		return items;
	}

	public void setItems(List<LocationMapItemDTO> items) {
		this.items = items;
	}


}
