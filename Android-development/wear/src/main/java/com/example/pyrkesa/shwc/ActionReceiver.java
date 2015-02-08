package com.example.pyrkesa.shwc;

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

/**
 * Created by Alassane on 01/02/2015.
 */
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
