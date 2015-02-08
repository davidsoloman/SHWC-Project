package com.example.pyrkesa.shwc;
/*
 * Device : This class is used to receive message from the smartphone and show the notification.
 * All informations about the current room are received on a json string.
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
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class OngoingNotificationListenerService extends WearableListenerService {

    public static  GoogleApiClient mGoogleApiClient;
    private String TAG="Wearable shwc :";
    private final String KEY_TITLE="PAGE_TITLE";
    private final String KEY_BACKGROUND="BACKGROUND";
    private final String KEY_ROOM_DEVICES="ROOMDEVICES";
    private final String KEY_ROOM_NAME="ROOMNAME";

    private final String PATH="/SHWC/NOTIFROOM";

    private final int NOTIFICATION_ID = 10;
    public static final String PATH_DISMISS = "/dismissnotification";
    public static final String TAG_DISMISS="TAGDISMISS";


    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        mGoogleApiClient.connect();



    }
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        final String ACTION_DEMAND = "ACTION_DEMAND";
        String EXTRA_CMD = "EXTRA_CMD";

        dataEvents.close();

        if (!mGoogleApiClient.isConnected()) {
            ConnectionResult connectionResult = mGoogleApiClient
                    .blockingConnect(30, TimeUnit.SECONDS);
            if (!connectionResult.isSuccess()) {
                Log.e(TAG, "Service failed to connect to GoogleApiClient.");
                return;
            }
        }

        for (DataEvent event : events) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if (PATH.equals(path)) {
                    // Get the data out of the event
                    DataMapItem dataMapItem =
                            DataMapItem.fromDataItem(event.getDataItem());
                    //final String title = dataMapItem.getDataMap().getString(KEY_TITLE);
                    final String room_devices = dataMapItem.getDataMap().getString(KEY_ROOM_DEVICES);
                    final String room_name = dataMapItem.getDataMap().getString(KEY_ROOM_NAME);
                  //  Asset asset = dataMapItem.getDataMap().getAsset(KEY_BACKGROUND);

                    try{
                        JSONObject roomJSON=new JSONObject(room_devices);
                        JSONArray devicesArray=roomJSON.getJSONArray("devices");
                        String firstPageText="Équipements :";

                        ArrayList<Device> devicess = new ArrayList<Device>();

                        for(int i=0;i<devicesArray.length();i++)
                        {
                            JSONObject d=devicesArray.getJSONObject(i);
                            Device device=new Device(d.getString("id"),d.getString("name"),d.getInt("type"),d.getString("status"));

                            String Newline=System.getProperty("line.separator");

                            firstPageText+=Newline;
                            firstPageText+=device.name;

                            devicess.add(device);

                        }
                        if(firstPageText.equalsIgnoreCase("Équipements :"))
                        {
                            firstPageText="Aucun équipement";
                        }

                        Bitmap background = BitmapFactory.decodeResource(this.getResources(),
                                R.drawable.bg_distance);
                        NotificationCompat.WearableExtender notifExtender= new NotificationCompat.WearableExtender();

                        for(Device d : devicess)
                        {
                            try{
                                notifExtender.addAction(d.getAction(OngoingNotificationListenerService.this));
                            }catch(Exception e)
                            {
                                Log.e("Erreur get Action :",e.getMessage());
                            }
                        }

                        NotificationCompat.Builder notificationBuilder =
                                new NotificationCompat.Builder(this)
                                        .setContentTitle("Pièce : "+roomJSON.getString("name"))
                                        .setContentText(firstPageText)
                                        .setSmallIcon(R.drawable.mini_logo)
                                        .extend(notifExtender.setBackground(background))
                                        .setOngoing(true);


                        // Build the notification and show it
                        NotificationManager notificationManager =
                                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.notify(
                                NOTIFICATION_ID, notificationBuilder.build());
                    }catch(Throwable t) {
                        Log.e("JSON_WEAR_SHWC", "Could not parse malformed JSON: "+room_devices + t.getMessage());
                    }


                } else {
                    Log.d(TAG, "Unrecognized path: " + path);
                }
            }
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(PATH_DISMISS)) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.cancel(NOTIFICATION_ID);
        }
    }
}
