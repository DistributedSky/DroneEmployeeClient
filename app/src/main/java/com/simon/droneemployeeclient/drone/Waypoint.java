package com.simon.droneemployeeclient.drone;

public class Waypoint {
	public Waypoint(){
		mlat = 0;
		mlon = 0;
		malt = 0;
	}

	public Waypoint(double lat, double lon, double alt){
		mlat = lat;
		mlon = lon;
		malt = alt;
	}

	@Override
	public String toString(){
		return "Waypoint(" + mlat + ", " + mlon + ", " + malt + ")";
	}

	private double mlat;
	private double mlon;
	private double malt;
}
