package com.droneemployee.client;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.SupportMapFragment;
import com.droneemployee.client.droneemployee.Drone;
import com.droneemployee.client.droneemployee.DroneList;
import com.droneemployee.client.droneemployee.DroneEmployeeBase;
import com.droneemployee.client.droneemployee.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Menu sideMenu;
    private FragmentManager fragmentManager;
    private MapTools mapTools;
    private DroneEmployeeBase droneEmployeeBase;
    private DroneList availableDrones;
    private ItemIdTaskMap itemIdTaskMap;
    private SwitchButton switchButton;

    private TaskDataMediator taskDataMediator;
    private TaskData taskData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: REFACTOR this method
        //Initialize main part
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Fragment manager initialize
        this.fragmentManager = getSupportFragmentManager();

        //MapTools initialize
        this.mapTools = new MapTools((SupportMapFragment) fragmentManager.
                findFragmentById(R.id.location_map));

        //Employee initialize
        this.droneEmployeeBase = new DroneEmployeeBase();
        this.availableDrones = droneEmployeeBase.loadAvailableDrones();
        this.itemIdTaskMap = new ItemIdTaskMap();

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
        taskData = new TaskData(droneEmployeeBase);
        //===
        this.taskDataMediator = new TaskDataMediator();
        taskDataMediator.registerObserver((TaskDataMediator.ChangeCurrentTaskObserver) mapTools);
        taskDataMediator.registerObserver((TaskDataMediator.AddRouteWaypointObserver) mapTools);
        taskDataMediator.registerObserver((TaskDataMediator.ChangeRouteWaypointObserver) mapTools);
        taskDataMediator.registerObserver((TaskDataMediator.LoadNewTaskObserver) mapTools);
        taskDataMediator.registerObserver((TaskDataMediator.UploadTasksObserver) mapTools);
        taskDataMediator.registerObserver(switchButton);
        taskDataMediator.registerObserver((TaskDataMediator.AddRouteWaypointObserver) taskData);
        taskDataMediator.registerObserver((TaskDataMediator.ChangeRouteWaypointObserver) taskData);
        taskDataMediator.registerObserver((TaskDataMediator.LoadNewTaskObserver) taskData);
        taskDataMediator.registerObserver((TaskDataMediator.UploadTasksObserver) taskData);
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
        /*
        for(Drone drone: availableDrones){
            menu.add(drone.getAddress());
        }
        */
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
            return true;
        } else if (id == R.id.action_send_tasks){
            for(Task task: itemIdTaskMap.values()){
                droneEmployeeBase.sendTask(task);
            }
            for(int key: itemIdTaskMap.keySet()){
                sideMenu.removeItem(key);
            }
            mapTools.getMap().clear();
            taskDataMediator.changeCurrentTask(null);
            itemIdTaskMap = new ItemIdTaskMap();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.i("LOGINFO", "IN onNavigationItemSelected ID: " + id);

        if (id == R.id.nav_buy) {
            Log.i("LOGINFO", "Select nav_buy");
            switchButton.off();
            ArrayList<String> allId = availableDrones.getAllId();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Available DRONES:");
            builder.setItems(allId.toArray(new String[allId.size()]),
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Drone drone = availableDrones.get(which);
                    Log.i("LOGINFO", String.valueOf(drone));

                    Task task = new Task(droneEmployeeBase.byTicket(drone));
                    taskDataMediator.loadNewTask(task);
                    taskDataMediator.changeCurrentTask(task);

                    MenuItem menuItem = sideMenu.add(0, drone.hashCode(), 0, drone.getAddress());
                    itemIdTaskMap.put(menuItem.getItemId(), task);
                    availableDrones.remove(which);
                }
            });
            builder.show();
            return true;
        } else if (itemIdTaskMap.containsKey(id)) {
            Task task = itemIdTaskMap.get(id);
            Log.i("LOGINFO", "Select TASK: " + task);
            taskDataMediator.changeCurrentTask(task);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}