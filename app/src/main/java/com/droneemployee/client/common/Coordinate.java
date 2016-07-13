package com.droneemployee.client.common;

public class Coordinate {
	public Coordinate(){
		lat = 0;
		lng = 0;
		alt = 0;
	}

	public Coordinate(double lat, double lng, double alt){
		this.lat = lat;
		this.lng = lng;
		this.alt = alt;
	}

	public Coordinate(double lat, double lng) {
        this(lat, lng, 0);
    }

	@Override
	public String toString(){
		return "Coordinate(" + lat + ", " + lng + ", " + alt + ")";
	}

	public double lat;
	public double lng;
	public double alt;
}
