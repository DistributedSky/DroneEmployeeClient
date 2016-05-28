package com.simon.droneemployeeclient.drone;

import java.util.ArrayList;

public class Drone {
	public Drone(String address){
		maddress = address;
		mroute = new ArrayList<Waypoint>();
	}

	public void addWaypoint(Waypoint waypoint){
		mroute.add(waypoint);
	}

	public Waypoint getWaypoint(int i){
		return mroute.get(i);
	}

	@Override
	public String toString(){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Drone(");
		stringBuffer.append("address=" + maddress);
		stringBuffer.append(", route=" + mroute);
		stringBuffer.append(")");
		return stringBuffer.toString();
	}

	private ArrayList<Waypoint> mroute = null;
	private String maddress = null;
}
