package com.example.pyrkesa.com.example.pyrkesa.home;

/*
 * Room : This class is used to represent a room.
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

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import com.example.pyrkesa.shwc.JSONParser;
import com.example.pyrkesa.shwc.MainActivity;
import com.example.pyrkesa.shwc.R;
import com.example.pyrkesa.singleton.ModelFactory;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;



public class Room {
    public int ID;
    public String name;
    public double area;
    public int numb_users;
    private ArrayList<RoomBeacon> RoomBeacons=new ArrayList<RoomBeacon>();
    private ArrayList<Device> RoomDevices=new ArrayList<Device>();
    private final String PATH="/SHWC/NOTIFROOM";
    private final String KEY_TITLE="PAGE_TITLE";
    private final String KEY_BACKGROUND="BACKGROUND";
    private final String KEY_ROOM_DEVICES="ROOMDEVICES";
    private final String KEY_ROOM_NAME="ROOMNAME";
    private String TAG="Wearable shwc :";
    private JSONObject roomJSON=new JSONObject();

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    public List<RoomBeacon> getRoomBeacons() {
        return RoomBeacons;
    }

    public void setRoomBeacons(ArrayList<RoomBeacon> roomBeacons) {
        RoomBeacons = roomBeacons;
    }

    public List<Device> getRoomDevices() {
        return RoomDevices;
    }

    public void setRoomDevices(ArrayList<Device> roomDevices) {
        RoomDevices = roomDevices;
    }

    public JSONObject getJSONObject(JSONArray devicesArray)
    {
        try{
            roomJSON.put("id",this.ID);
            roomJSON.put("name",this.name);
            roomJSON.put("area",this.area);
            roomJSON.put("numb_users",this.numb_users);
            roomJSON.put("devices",devicesArray);
            return roomJSON;
        }catch(JSONException e)
        {
            e.printStackTrace();
        }
        return roomJSON;
    }

    public Void notifyWearableWatch()
    {
        new LoadDevicesStatus().execute(this);
        return null;
    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadDevicesStatus extends AsyncTask<Room, Void, Room> {

        /**
         * getting All products from url
         * */
        protected Room doInBackground(Room... room) {

            for(Device d : room[0].RoomDevices)
            {
                switch(d.type)
                {
                    case 1: // actuator

                        final ModelFactory model=(ModelFactory)ModelFactory.getContext();

                        List<NameValuePair> params = new ArrayList<NameValuePair>();

                        params.add(new BasicNameValuePair("zibase",model.box.login));
                        params.add(new BasicNameValuePair("token",model.box.token));
                        params.add(new BasicNameValuePair("service","get"));
                        params.add(new BasicNameValuePair("target","actuator"));
                        params.add(new BasicNameValuePair("id",String.valueOf(d.id)));
                        Log.d("Zodianet zibase : ", model.box.login);
                        Log.d("Zodianet token : ", model.box.token);
                        Log.d("Zodianet url : ", model.box.url_zodianet);
                        Log.d("Zodianet id device : ", String.valueOf(d.id));

                        // getting JSON string from URL
                        JSONObject json3 = jParser.makeHttpRequest(model.box.url_zodianet,"GET", params);

                        try{
                            if(json3.getString("head").equalsIgnoreCase("success"))
                            {
                                JSONObject jsonDevice=json3.getJSONObject("body");

                                int status=jsonDevice.getInt("status");
                                d.status=String.valueOf(status);
                                Log.d("MAJ Status : ","Succès");

                            }else{
                                Log.d("MAJ Status : ","Erreur");
                            }
                        }catch(JSONException e)
                        {
                            e.printStackTrace();
                        }

                        break;
                    default:
                }

            }

            return room[0];
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Room r) {
            // dismiss the dialog after getting all products
            try{


                if (MainActivity.mGoogleApiClient.isConnected()) {

                    String firstPageText="Équipements :";
                    JSONArray devicesjsonArray = new JSONArray();

                    for(Device device : r.RoomDevices)
                    {
                        String Newline=System.getProperty("line.separator");
                        firstPageText+=Newline;
                        firstPageText+=device.name;
                        // String reponse=model.box.Action(this.ID,device,"update_status");
                        devicesjsonArray.put(device.getJSONObject());

                    }

                    JSONObject jsonRoom = r.getJSONObject(devicesjsonArray);

                    Log.d("JSONRoom to wearable : ", jsonRoom.toString());

                    if(firstPageText.equalsIgnoreCase("Équipements :"))
                    {
                        firstPageText="Aucun équipement";
                    }

                    PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(PATH);

                    // Add data to the request

                    putDataMapRequest.getDataMap().putString(KEY_ROOM_NAME,
                            String.format(r.name));

                    putDataMapRequest.getDataMap().putString(KEY_ROOM_DEVICES,
                            String.format(jsonRoom.toString()));


                /*Bitmap background = BitmapFactory.decodeResource(c.getResources()
                        , R.drawable.bg_distance);
                Asset asset = createAssetFromBitmap(background);
                putDataMapRequest.getDataMap().putAsset(KEY_BACKGROUND, asset);*/

                    PutDataRequest request = putDataMapRequest.asPutDataRequest();

                    Wearable.DataApi.putDataItem(MainActivity.mGoogleApiClient, request)
                            .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                                @Override
                                public void onResult(DataApi.DataItemResult dataItemResult) {
                                    Log.d(TAG, "putDataItem status: "
                                            + dataItemResult.getStatus().toString());
                                }
                            });
                    Log.d("Wearable : ", "Connected");

                }else
                {
                    Log.d("Wearable : ", "Disconnected");

                }
            }catch(Exception e)
            {
                Log.d("Wearable : ", e.getMessage());

            }

        }

    }

    private static Asset createAssetFromBitmap(Bitmap bitmap) {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        return Asset.createFromBytes(byteStream.toByteArray());
    }
}
