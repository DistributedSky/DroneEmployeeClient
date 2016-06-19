package com.droneemployee.client.common;

import java.io.IOException;

public class Drone {
	public enum State {
        AVAILABLE, BUSY;
        static State toState(String string) throws IOException {
            if(string.equals("AVAILABLE")){
                return AVAILABLE;
            } else if (string.equals("BUSY")){
                return BUSY;
            } else {
                throw new IOException("Can't convert. Bad string: " + string);
            }
        }
    }

    private String mAddress;
    private State mState;
    private Coordinate mLastPosition;

    public Drone(String address) {
        this(address, null, State.AVAILABLE);
    }

    public Drone(String address, Coordinate lastPosition, State state){
		this.mAddress = address;
        this.mState = state;
        this.mLastPosition = lastPosition;
	}

    public String getAddress() {
        return mAddress;
    }

    public State getState() {
        return mState;
    }

    public Coordinate getLastPosition(){
        return mLastPosition;
    }

	@Override
	public String toString(){
        return "Drone(" + mAddress + ", " + mState + ", " +
                (mLastPosition != null? mLastPosition: "null") + ")";
	}
}