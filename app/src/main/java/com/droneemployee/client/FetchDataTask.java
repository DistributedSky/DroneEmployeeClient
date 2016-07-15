package com.droneemployee.client;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.droneemployee.client.common.Coordinate;
import com.droneemployee.client.common.DroneATC;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by simon on 15.07.16.
 */
public class FetchDataTask extends LockUiAsyncTask<Void, DroneATC> {
    public FetchDataTask(final MainActivity activity) {
        super(activity, "Search ATC",
                new LockUiAsyncTask.Execute<Void, DroneATC>() {
                    @Override
                    public DroneATC execute(Void aVoid) {
                        GoogleApiClient client = activity.getApiClient();
                        Log.i(TAG, "in LockUiAsyncTask.Execute.execute");
                        if (client.isConnected()) {
                            Location location =
                                    LocationServices.FusedLocationApi.getLastLocation(client);
                            Coordinate myLocation = new Coordinate(
                                    location.getLatitude(),
                                    location.getLongitude());
                            return activity.getAtcCommunicator().fetchDroneAtc(myLocation);
                        }
                        return null;
                    }
                },
                new LockUiAsyncTask.ProcessResult<DroneATC>() {
                    @Override
                    public void execute(DroneATC atc) {
                        Log.i(TAG, "in LockUiAsyncTask.ProcessResult.execute");
                        if (atc == null) {
                            activity.showWarningMessage("Load FAIL");
                        } else {
                            activity.setDroneAtc(atc);
                            activity.getMapTools().renderPoligon(atc.getPerimeter());
                        }
                    }
                });
    }
}