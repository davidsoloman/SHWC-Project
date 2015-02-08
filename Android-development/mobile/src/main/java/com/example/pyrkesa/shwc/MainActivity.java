package com.example.pyrkesa.shwc;

/*
 * MainActivity : This class is used to manage iBeacon service and all fragments of the application.
 * Moreover the communication with the Android Wear devices is performed on this file thanks to the
 * Google API.
 *
 * Copyright (c) 2015 Pierre-Yves Rancien, Alassane Diagne, Axel Francart, Clément Casasreales, Andreas Roche
 *
 * Copyright (c) 2013 Estimote, Inc.
 *
 * This file is part of SHWC.
 *
 * SHWC is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SHWC is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SHWC. If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact : projetshwc@gmail.com
 */

import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;
import com.example.pyrkesa.com.example.pyrkesa.home.Actuator;
import com.example.pyrkesa.com.example.pyrkesa.home.Device;
import com.example.pyrkesa.com.example.pyrkesa.home.Room;
import com.example.pyrkesa.com.example.pyrkesa.home.RoomBeacon;
import com.example.pyrkesa.com.example.pyrkesa.home.Scenario;
import com.example.pyrkesa.frag.HomeFragment;
import com.example.pyrkesa.frag.RoomSettingFragment;
import com.example.pyrkesa.frag.ScenarioFragment;
import com.example.pyrkesa.frag.User_Fragment;
import com.example.pyrkesa.singleton.Box;
import com.example.pyrkesa.singleton.ModelFactory;
import com.example.pyrkesa.singleton.Cmd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, MessageApi.MessageListener{

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    //protected RelativeLayout _completeLayout, _activityLayout;
    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    // Progress Dialog
    private ProgressDialog pDialog;

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    public Box box=null;
    private BeaconManager beaconManager;
    private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);
    private static final int REQUEST_ENABLE_BT = 1234;
    private static String api;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_NUMB_USERS = "numb_users";
    private static final String TAG_ROOMS = "rooms";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_AREA = "area";
    private static final String TAG_BEACONS = "beacons";
    private static final String TAG_BEACON_RADIUS = "radius";
    private static final String TAG_DEVICES = "devices";
    private static final String TAG_MAC = "mac";
    private static final String TAG_BEACON_NAME = "name";
    private static final String TAG_DEVICE_ID = "id";
    private static final String TAG_TYPE = "type";
    private static final String TAG_DEVICE_NAME = "name";


    private static String url_all_rooms = "";

    // products JSONArray
    JSONArray rooms = null;
    public static String url_disconnect="";

    public static GoogleApiClient mGoogleApiClient;
    public static final String PATH_DISMISS = "/dismissnotification";
    public static final String PATH_CMD_DEVICE = "/cmd/device";
    public static final String TAG_DISMISS="TAGDISMISS";

    ModelFactory model=(ModelFactory)ModelFactory.getContext();
    public static Handler scenar=new Handler();
    public static Bundle x = new Bundle();
    public static Cmd temp = new Cmd("SET","0");
    public static Message msgObj = scenar.obtainMessage();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);

        url_disconnect=model.api_url+"Users/disconnect_user.php";
        setContentView(R.layout.main_activity_phone);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);// load icons from

      /*  TextView loc_value = (TextView) findViewById(R.id.loc_value);
        loc_value.setText(model.current_room);*/

        set(navMenuTitles, navMenuIcons, 0);

        displayView(0);


        box=model.getBox();
        api=model.api_url;
        url_all_rooms=api+"Rooms/get_all_rooms.php";
        Log.d("API URL",url_all_rooms);
        new LoadAllRooms().execute(url_all_rooms);

       /* final Handler scenarioHandler = new Handler() {
            @Override
            public void handleMessage (Message msg) {

                String cmd = msg.getData().getString("cmd");


                if(cmd!=null){

                        ModelFactory model=(ModelFactory)ModelFactory.getContext();
                        model.box.Action(cmd);
                }


            }
        };*/

        // Configure BeaconManager.
        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener(){
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
                // Note that results are not delivered on UI thread.
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // Note that beacons reported here are already sorted by estimated
                        // distance between device and beacon.
                        ModelFactory model=(ModelFactory)ModelFactory.getContext();

                        if(model.LoadRoomsSuccess)
                        {
                            //getActionBar().setSubtitle("Found beacons: " + beacons.size()+ " Numb rooms : "+ model.rooms.size());

                            for(Beacon b : beacons)
                            {
                                for(Room r : model.rooms)
                                {
                                    if(r.name!=model.current_room) {
                                        for (RoomBeacon rb : r.getRoomBeacons()) {
                                            Log.d("MAC :", rb.mac);
                                            Log.d("MAC instant :", b.getMacAddress());
                                            Log.d("Room name :", r.name);
                                            //Log.d("Rayon room :", Double.toString(rb.));
                                            Log.d("Distance beacon :", Double.toString(Utils.computeAccuracy(b)));
                                            if (rb.mac.equalsIgnoreCase(b.getMacAddress())) {
                                                if (Utils.computeAccuracy(b) < rb.radius) {
                                                    Log.d("LOC:", "Localisation : " + r.name);
                                                    Log.d("LOC:", "Rayon  : " + rb.radius);

                                                    if(model.current_page==0)
                                                    {
                                                        TextView loc_value = (TextView) findViewById(R.id.loc_value);
                                                        model.current_room = r.name;
                                                        loc_value.setText(model.current_room);
                                                        TextView box_value = (TextView) findViewById(R.id.boxStatus);
                                                        TextView box_name = (TextView) findViewById(R.id.boxName);
                                                        if(model.box.status)
                                                        {
                                                            box_value.setText("Disponible");
                                                            box_name.setText(model.box.name);
                                                        }else
                                                        {
                                                            box_value.setText("Indisponible");
                                                            box_name.setText(". . .");
                                                        }


                                                    }

                                                    //r.notifyWatch(MainActivity.this);
                                                    r.notifyWearableWatch();

                                                }
                                            }
                                        }
                                    }else
                                    {
                                        Boolean outofroom=false;

                                        for (RoomBeacon rb : r.getRoomBeacons()) {

                                            if (rb.mac.equalsIgnoreCase(b.getMacAddress())) {
                                                if (Utils.computeAccuracy(b) > rb.radius) {
                                                    outofroom=true;

                                                    /**** event out ****/

/*
                                                        for(Device d : r.getRoomDevices())
                                                        {
                                                            switch (d.type)
                                                            {
                                                                case 1:

                                                                    x.putString("cmd", d.getJSONcmd(temp).toString());
                                                                    msgObj.setData(x);
                                                                    scenar.sendMessage(msgObj);

                                                                break;
                                                                default:
                                                            }
                                                        }
*/


                                                    /*******************/
                                                }
                                            }

                                        }
                                        if(outofroom)
                                        {
                                            if(model.current_page==0)
                                            {
                                                TextView loc_value = (TextView) findViewById(R.id.loc_value);
                                                model.current_room = ". . .";
                                                loc_value.setText(model.current_room);
                                                Room outroom=new Room();
                                                outroom.name="...";
                                                outroom.setRoomDevices(new ArrayList<Device>());
                                                outroom.setRoomBeacons(new ArrayList<RoomBeacon>());
                                                outroom.ID=-1;
                                                outroom.area=0;
                                                outroom.notifyWearableWatch();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
            }
        });


        if( getIntent().getBooleanExtra("Exit me", false)){
            finish();
        }

    }



    public void set(String[] navMenuTitles, TypedArray navMenuIcons, int NumActivity) {
        if((NumActivity >= 0)&&(NumActivity <= 4))
            mTitle = navMenuTitles[NumActivity];
        else
            mTitle = getTitle();

        mDrawerTitle = "Menu";
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items
        if (navMenuIcons == null) {
            for (int i = 0; i < navMenuTitles.length; i++) {
                navDrawerItems.add(new NavDrawerItem(navMenuTitles[i]));
            }
        } else {
            for (int i = 0; i < navMenuTitles.length; i++) {
                navDrawerItems.add(new NavDrawerItem(navMenuTitles[i],
                        navMenuIcons.getResourceId(i, -1)));
            }
        }

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        // getSupportActionBar().setIcon(R.drawable.ic_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.mini_logo, // nav menu toggle icon
                R.string.app_name, // nav drawer open - description for
                // accessibility
                R.string.app_name // nav drawer close - description for
                // accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
               invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity_phone, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.quit) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Exit me", true);
            startActivity(intent);


        }

        if (id == R.id.log_out){

            String android_id = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            new LogOut().execute(android_id);

        }
        return super.onOptionsItemSelected(item);
    }

/*
   @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        }

        return super.onOptionsItemSelected(item);
    }*/

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        // boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        // menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new HomeFragment();
                mTitle="SHWC";
                break;
            case 1:
                fragment = new ScenarioFragment();
                mTitle="Définir scénarios";
                break;
            case 2:
                fragment = new RoomSettingFragment();
                mTitle="Réglage salle";
                break;
            case 3:
                fragment = new User_Fragment();
                mTitle="Gestion des utilisateurs";
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.test, fragment, "");
            transaction.commit();
            model.current_page=position;

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            //getActionBar().setTitle(mNavigationDrawerItemTitles[position]);
           // getActionBar().setTitle(position);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

        // update selected item and title, then close the drawer
        //mDrawerList.setItemChecked(position, true);
        //mDrawerList.setSelection(position);
        //mDrawerLayout.closeDrawer(mDrawerList);


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LogOut extends AsyncTask<String, Void, Boolean> {

        /**
         * Before starting background thread Show Progress Dialog
         * */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Déconnexion ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected Boolean doInBackground(String... android_id) {


            LoginActivity.UserAuthenticate=false;


            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("ANDROID_ID",android_id[0]));
            // getting JSON string from URL
            JSONObject json3 = jParser.makeHttpRequest(url_disconnect,"POST", params);

            try{
                if(json3.getInt(TAG_SUCCESS)==1)
                {
                    model.UserAuthenticate=false;
                    LoginActivity.UserAuthenticate=false;
                    return true;
                }else{
                    return false;
                }
            }catch(JSONException e)
            {
                e.printStackTrace();
            }

            return false;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(final Boolean result) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                private Boolean success=result;
                //private ArrayList<String> AllBoxes = result;
                public void run() {

                    if(success)
                    {
                        Intent loginpage = new Intent(getApplicationContext(),LoginActivity.class);
                        loginpage.putExtra("logout",true);
                        loginpage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginpage);

                        finish();
                    }

                    /*Spinner mySpinner = (Spinner) findViewById(R.id.box_choice);
                    // Spinner adapter
                    mySpinner
                            .setAdapter(new ArrayAdapter<String>(LoginActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    AllBoxes));*/
                }


            });

        }

    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllRooms extends AsyncTask<String, Void, Boolean> {

        /**
         * Before starting background thread Show Progress Dialog
         * */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Initialisation ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        /**
         * getting All products from url
         * */
        protected Boolean doInBackground(String... url) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url[0], "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All rooms: ", json.toString());


            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if(success==1) {

                    ModelFactory model = (ModelFactory) ModelFactory.getContext();

                    rooms = json.getJSONArray(TAG_ROOMS);


                    // looping through All Products
                    for (int i = 0; i < rooms.length(); i++) {
                        Room room = new Room();
                        JSONObject c = rooms.getJSONObject(i);


                        // Storing each json item in variable
                        int id = c.getInt(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        int numb_users = c.getInt(TAG_NUMB_USERS);
                        double area = c.getDouble(TAG_AREA);

                        room.ID = id;
                        room.name = name;
                        room.numb_users = numb_users;
                        room.area = area;


                        JSONArray beaconss = c.getJSONArray(TAG_BEACONS);
                        ArrayList<RoomBeacon> allRoomBeacons = new ArrayList<RoomBeacon>();

                        for (int x = 0; x < beaconss.length(); x++) {
                            JSONObject b = beaconss.getJSONObject(x);

                            RoomBeacon temp = new RoomBeacon();

                            temp.mac = b.getString(TAG_MAC);
                            temp.name = b.getString(TAG_BEACON_NAME);
                            temp.radius = b.getDouble(TAG_BEACON_RADIUS);

                            allRoomBeacons.add(temp);
                        }
                        room.setRoomBeacons(allRoomBeacons);


                        JSONArray devices = c.getJSONArray(TAG_DEVICES);
                        ArrayList<Device> allDevices = new ArrayList<Device>();

                        for (int z = 0; z < devices.length(); z++) {
                            JSONObject u = devices.getJSONObject(z);

                            switch (u.getInt(TAG_TYPE)) {
                                case 1:
                                    // Actionneur
                                    Actuator temp = new Actuator();
                                    temp.id = u.getString(TAG_DEVICE_ID);
                                    temp.name = u.getString(TAG_DEVICE_NAME);
                                    temp.type = u.getInt(TAG_TYPE);
                                    temp.status = "0";
                                    allDevices.add(temp);
                                    break;
                                default:
                            }

                        }
                        room.setRoomDevices(allDevices);

                        // adding each child node to HashMap key => value
                        /*map.put(TAG_PID, id);
                        map.put(TAG_NAME, name);*/

                        // adding HashList to ArrayList

                        model.rooms.add(room);


                    }
                    model.LoadRoomsSuccess = true;
                    return true;
                }else{
                    return false;
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return false;

            }

        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(final Boolean result) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();

        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissNotification();

        beaconManager.disconnect();


        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }



    }

    @Override
    protected void onStart() {
        super.onStart();
        /** wearable part **/
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(Wearable.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();

                if (!mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
        /*******************/

        // Check if device supports Bluetooth Low Energy.
        if (!beaconManager.hasBluetooth()) {
            Toast.makeText(this, "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
            return;
        }

        // If Bluetooth is not enabled, let user enable it.
        if (!beaconManager.isBluetoothEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            connectToService();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();

        if(model.current_page==0)
        {
            TextView loc_value = (TextView) findViewById(R.id.loc_value);
            loc_value.setText(model.current_room);
            TextView box_value = (TextView) findViewById(R.id.boxStatus);
            TextView box_name = (TextView) findViewById(R.id.boxName);
            if(model.box.status)
            {
                box_value.setText("Disponible");
                box_name.setText(model.box.name);
            }else
            {
                box_value.setText("Indisponible");
                box_name.setText(". . .");
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                connectToService();
            } else {
                Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_LONG).show();
                getActionBar().setSubtitle("Bluetooth not enabled");
            }
        }

    }


    private void connectToService() {
        //getActionBar().setSubtitle("Scanning...");
        //adapter.replaceWith(Collections.<Beacon>emptyList());
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
                } catch (RemoteException e) {
                    Toast.makeText(MainActivity.this, "Cannot start ranging, something terrible happened",
                            Toast.LENGTH_LONG).show();
                    //Log.e(TAG, "Cannot start ranging", e);
                }
            }
        });
    }

    private void dismissNotification() {
        if (mGoogleApiClient.isConnected()) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    NodeApi.GetConnectedNodesResult nodes =
                            Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
                    for (Node node : nodes.getNodes()) {
                        MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                                mGoogleApiClient, node.getId(), PATH_DISMISS, null).await();
                        if (!result.getStatus().isSuccess()) {
                            Log.e(TAG_DISMISS, "ERROR: failed to send Message: " + result.getStatus());
                        }
                    }
                    return null;
                }
            }.execute();
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(PATH_CMD_DEVICE)) {

            //Toast.makeText(this,"ALLO !", Toast.LENGTH_LONG).show();
            String cmdJSON=new String(messageEvent.getData());
            Log.d("CMD_MSG_FROM_WEAR",cmdJSON);

            model.box.Action(cmdJSON);

        }
    }

    /* wearable part */
    @Override
    public void onConnected(Bundle bundle) {
        Wearable.MessageApi.addListener( mGoogleApiClient, this );
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }



    /**************/


}




