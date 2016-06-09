package com.droneemployee.client.droneemployee;

public class Drone {
    private String address;
    private State state;
    private LatLngAlt lastPosition;

	public enum State { AVAILABLE, BUSY }

    public Drone(String address) {
        this(address, State.AVAILABLE, null);
    }

    public Drone(String address, State state, LatLngAlt lastPosition){
		this.address = address;
        this.state = state;
        this.lastPosition = lastPosition;
	}

    public String getAddress() {
        return address;
    }

    public State getState() {
        return state;
    }

    public LatLngAlt getLastPosition(){
        return lastPosition;
    }

	@Override
	public String toString(){
        return "Drone(" + address + ", " + state + ", " +
                (lastPosition != null? lastPosition : "null") + ")";
	}
}