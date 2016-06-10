package com.droneemployee.client.common;

public class LatLngAlt {
	public LatLngAlt(){
		lat = 0;
		lon = 0;
		alt = 0;
	}

	public LatLngAlt(double lat, double lon, double alt){
		this.lat = lat;
		this.lon = lon;
		this.alt = alt;
	}

	@Override
	public String toString(){
		return "LatLngAlt(" + lat + ", " + lon + ", " + alt + ")";
	}

	public double lat;
	public double lon;
	public double alt;
}
