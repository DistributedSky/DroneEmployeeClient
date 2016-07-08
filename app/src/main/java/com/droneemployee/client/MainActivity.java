package com.droneemployee.client;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.droneemployee.client.common.DroneATC;
import com.droneemployee.client.common.ATCCommunicator;
import com.droneemployee.client.common.FakeATCCommunicator;
import com.droneemployee.client.common.NetATCCommunicator;
import com.droneemployee.client.common.Task;
import com.google.android.gms.maps.SupportMapFragment;
import com.droneemployee.client.common.Drone;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private class FetchDataTask extends AsyncTask<Void, Void, DroneATC> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Search ATC");
            progressDialog.setMessage("Please, wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected DroneATC doInBackground(Void... params) {
            return atcCommunicator.fetchDroneAtc();
        }
        @Override
        protected void onPostExecute(DroneATC atc) {
            if(atc == null){
                Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout),
                            "Load FAIL", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null);
                snackbar.getView().setBackgroundColor(Color.rgb(0xE0, 0x00, 0x00));
                snackbar.show();
            } else {
                droneAtc = atc;
                mapTools.renderPoligon(droneAtc.getPerimeter());
            }
            progressDialog.dismiss();
        }
    }

    private static final String TAG = "MainActivity";

    private Menu sideMenu;
    private MapTools mapTools;
    private ATCCommunicator atcCommunicator;
    private DroneATC droneAtc;
    private SwitchButton switchButton;

    private HashMap<Integer, Integer> taskIndexItemIdMap;
    private int itemIndex;

    private SharedTaskIndex sharedTaskIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: REFACTOR this method
        //Initialize main part
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Fragment manager initialize
        FragmentManager fragmentManager = getSupportFragmentManager();

        //Employee initialize
        this.taskIndexItemIdMap = new HashMap<>();
        this.itemIndex = SharedTaskIndex.NOTSET;

        //MapTools initialize
        this.mapTools = new MapTools((SupportMapFragment) fragmentManager.
                findFragmentById(R.id.location_map));

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

        //===
        this.sharedTaskIndex = new SharedTaskIndex();
        sharedTaskIndex.addObserver(mapTools);
        sharedTaskIndex.addObserver(switchButton);
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
                Snackbar.make(findViewById(R.id.coordinator_layout),
                        "Has already settings", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null)
                        .show();
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
            Log.i(TAG, "UPLOAD ALL TASKS!");
            for (Integer itemIndex : taskIndexItemIdMap.keySet()) {
                sideMenu.removeItem(itemIndex);
            }
            taskIndexItemIdMap.clear();
            itemIndex = SharedTaskIndex.NOTSET;
            sharedTaskIndex.updateCurrentTask(itemIndex);

            mapTools.uploadTasks();
        } else if (id == R.id.action_find_atc) {
            if(atcCommunicator == null){
                Snackbar.make(findViewById(R.id.coordinator_layout),
                            "Need enter the settings", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null)
                        .show();
            } else if(droneAtc == null) {
                new FetchDataTask().execute();
            } else {
                Snackbar.make(findViewById(R.id.coordinator_layout),
                        "Has already ATC", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null)
                    .show();
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
                Snackbar.make(findViewById(R.id.coordinator_layout),
                        "Need find ATC", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null)
                        .show();
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
}