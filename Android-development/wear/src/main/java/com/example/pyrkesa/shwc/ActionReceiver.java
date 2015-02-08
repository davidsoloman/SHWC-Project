package com.example.pyrkesa.shwc;

/*
 * ActionReceiver : This class is used to receive actions from the MainActivity. The actions are sent by the notification.
 * When the user push to an action an intent is sent to this receiver and we send the cmd received to the smartphone.
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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

public class ActionReceiver extends BroadcastReceiver implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    public String EXTRA_CMD = "EXTRA_CMD";
    public static final String PATH_CMD_DEVICE = "/cmd/device";
    public  GoogleApiClient mGoogleApiClient;
    public String cmdToSend="null";


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null) {
            /** wearable part **/
            mGoogleApiClient=OngoingNotificationListenerService.mGoogleApiClient;

            /*mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(Wearable.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();*/

            if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
            }
            /*******************/
            Log.d("WEAR INTENT", "INTENT RECU");
            this.cmdToSend = intent.getStringExtra(EXTRA_CMD);
            sendCMD();
        }
    }

    private void sendCMD() {
        if (mGoogleApiClient.isConnected()) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    NodeApi.GetConnectedNodesResult nodes =
                            Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
                    for (Node node : nodes.getNodes()) {
                        MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                                mGoogleApiClient, node.getId(), PATH_CMD_DEVICE, cmdToSend.getBytes()).await();
                        if (!result.getStatus().isSuccess()) {
                            Log.e(PATH_CMD_DEVICE, "ERROR: failed to send Message: " + result.getStatus());
                        }
                    }

                    return null;
                }
            }.execute();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
