package com.simon.droneemployeeclient.droneflat;

public class LatLngAlt {
	public LatLngAlt(){
		mLat = 0;
		mLon = 0;
		mAlt = 0;
	}

	public LatLngAlt(double lat, double lon, double alt){
		mLat = lat;
		mLon = lon;
		mAlt = alt;
	}

	@Override
	public String toString(){
		return "LatLngAlt(" + mLat + ", " + mLon + ", " + mAlt + ")";
	}

	public double mLat;
	public double mLon;
	public double mAlt;
}
