package com.droneemployee.client.common;

public class Coordinate {
	public Coordinate(){
		lat = 0;
		lon = 0;
		alt = 0;
	}

	public Coordinate(double lat, double lon, double alt){
		this.lat = lat;
		this.lon = lon;
		this.alt = alt;
	}

	public Coordinate(double lat, double lon) {
        this(lat, lon, 0);
    }

	@Override
	public String toString(){
		return "Coordinate(" + lat + ", " + lon + ", " + alt + ")";
	}

	public double lat;
	public double lon;
	public double alt;
}
