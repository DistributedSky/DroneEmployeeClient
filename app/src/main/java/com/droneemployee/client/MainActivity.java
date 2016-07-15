package com.droneemployee.client;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.droneemployee.client.common.Coordinate;
import com.droneemployee.client.common.DroneATC;
import com.droneemployee.client.common.ATCCommunicator;
import com.droneemployee.client.common.FakeATCCommunicator;
import com.droneemployee.client.common.NetATCCommunicator;
import com.droneemployee.client.common.Task;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.droneemployee.client.common.Drone;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MainActivity";

    private Menu sideMenu;
    private MapTools mapTools;
    private GoogleApiClient apiClient;
    private ATCCommunicator atcCommunicator;
    private DroneATC droneAtc;
    private SwitchButton switchButton;
    private SharedTaskIndex sharedTaskIndex;

    private HashMap<Integer, Integer> taskIndexItemIdMap = new HashMap<>();
    private int itemIndex = SharedTaskIndex.NOTSET;

    public MapTools getMapTools() { return mapTools; }
    public GoogleApiClient getApiClient() { return apiClient; }
    public ATCCommunicator getAtcCommunicator() { return atcCommunicator; }
    public void setDroneAtc(DroneATC atc) { this.droneAtc = atc; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: REFACTOR this method
        //Initialize main part
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Fragment manager initialize
        FragmentManager fragmentManager = getSupportFragmentManager();

        //MapTools initialize
        this.mapTools = new MapTools((SupportMapFragment) fragmentManager.
                findFragmentById(R.id.location_map));

        //Google API client initialize
        apiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        //Toolbar initialize
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Floating button initialize
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        this.switchButton = new SwitchButton(
                fab,
                getResources().getDrawable(R.drawable.ic_done_light),
                getResources().getDrawable(R.drawable.ic_add_light),
                mapTools);
        fab.setOnClickListener(switchButton);

        //???
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Initialize NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        this.sideMenu = navigationView.getMenu();

        //Initialize SharedTaskIndex
        this.sharedTaskIndex = new SharedTaskIndex();
        sharedTaskIndex.addObserver(mapTools);
        sharedTaskIndex.addObserver(switchButton);
    }

    @Override
    protected void onStart() {
        apiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        apiClient.disconnect();
        super.onStop();
    }

    //TODO: something in this method
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    //TODO: something in this method
    @Override
    public void onConnectionSuspended(int i) {

    }

    //TODO: processing fail
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if(atcCommunicator != null){
                showMessage("Has already settings");
                return true;
            }
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setView(promptView);

            final EditText editAddress = (EditText) promptView.findViewById(R.id.edit_address);
            // setup a dialog window
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String address = editAddress.getText().toString();
                            if(address.equals("FAKEPLEASE")) {
                                Log.i(TAG, "set fake fetcher");
                                atcCommunicator = new FakeATCCommunicator();
                            } else {
                                if(!address.substring(0,7).equals("http://")) {
                                    address = "http://" + address;
                                }
                                Log.i(TAG, "new address: " + address);
                                atcCommunicator = new NetATCCommunicator(address);
                            }
                        }
                    })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            // create an alert dialog
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();

            return true;
        } else if (id == R.id.action_send_tasks) {
            if(atcCommunicator != null) {
                Log.i(TAG, "UPLOAD ALL TASKS!");

                for (Integer itemIndex : taskIndexItemIdMap.keySet()) {
                    sideMenu.removeItem(itemIndex);
                }
                taskIndexItemIdMap.clear();
                itemIndex = SharedTaskIndex.NOTSET;
                sharedTaskIndex.updateCurrentTask(itemIndex);
                new SendDataTask(this).run(mapTools.uploadTasks());
            } else {
                showMessage("Nothing to send");
            }
        } else if (id == R.id.action_find_atc) {
            if(atcCommunicator == null){
                showMessage("Need enter the settings");
            } else if(droneAtc == null) {
                new FetchDataTask(this).run();
            } else {
                showMessage("Has already ATC");
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        Log.i(TAG, "IN onNavigationItemSelected ID: " + id);

        if (id == R.id.nav_buy) {
            Log.i(TAG, "Select nav_buy");
            switchButton.off();
            if(droneAtc == null) {
                showMessage("Need find ATC");
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
            List<String> allId = droneAtc.getDronesIds();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Available DRONES:");
            builder.setItems(allId.toArray(new String[allId.size()]),
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Drone drone = droneAtc.getDrones().get(which);
                    Log.i(TAG, "Select: " + String.valueOf(drone));

                    Task task = new Task(atcCommunicator.buyTicket(drone));
                    Log.i(TAG, "IN MainActivity.onNavigationItemSelected(): task id = "
                            + task.hashCode());
                    mapTools.loadTask(task);
                    itemIndex++;
                    sharedTaskIndex.updateCurrentTask(itemIndex);

                    sideMenu.add(0, drone.hashCode(), 0, drone.getAddress());
                    taskIndexItemIdMap.put(drone.hashCode(), itemIndex);

                    droneAtc.getDrones().remove(which);
                }
            });
            builder.show();
            return true;
        } else if (taskIndexItemIdMap.containsKey(id)) {
            int taskIndex = taskIndexItemIdMap.get(id);
            Log.i(TAG, "Select TASK_INDEX: " + taskIndex);
            sharedTaskIndex.updateCurrentTask(taskIndex);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void showMessage(String message) {
        Snackbar.make(findViewById(R.id.coordinator_layout),
                message, Snackbar.LENGTH_SHORT)
                //.setAction("Action", null)
                .show();
    }

    void showWarningMessage(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout),
                message, Snackbar.LENGTH_SHORT);
                //.setAction("Action", null);
        snackbar.getView().setBackgroundColor(Color.rgb(0xE0, 0x00, 0x00));
        snackbar.show();
    }
}