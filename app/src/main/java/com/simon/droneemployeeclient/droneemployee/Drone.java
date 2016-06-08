package com.simon.droneemployeeclient.droneemployee;

public class Drone {
	public enum State { AVAILABLE, BUSY }

    public Drone(String address) {
        this(address, State.AVAILABLE, null);
    }

    public Drone(String address, State state, LatLngAlt lastPosition){
		mAddress = address;
        mState = state;
        mLastPosition = lastPosition;
	}

    public String getAddress() {
        return mAddress;
    }

    public State getState() {
        return mState;
    }

    public LatLngAlt getLastPosition(){
        return mLastPosition;
    }

	@Override
	public String toString(){
        return "Drone(" + mAddress + ", " + mState + ", " +
                (mLastPosition != null? mLastPosition: "null") + ")";
	}

	private String mAddress;
    private State mState;
    private LatLngAlt mLastPosition;
}