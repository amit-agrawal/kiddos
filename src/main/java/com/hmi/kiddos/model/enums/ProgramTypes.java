package com.hmi.kiddos.model.enums;

public enum ProgramTypes {
	CAMP("O", "Camp"),
	DC("D", "DC"),
	EC("O", "EC"),
	IC("D", "IC"),
	JKG("P", "Jr.  K.G."),
	NURSERY("P", "Nursery"),
	PG("P", "Play Group"),
	SKG("P", "Sr. K.G.");
	
	private String type;
	private String displayName;
	
	private ProgramTypes(String type, String displayName) {
		this.setType(type);
		this.displayName = displayName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
}
