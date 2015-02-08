package com.example.pyrkesa.shwc;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alassane on 28/01/2015.
 */
public class Device {
    public String id;
    public int type; // 1: Actuator ...
    public String status;
    public String name;
    public Cmd cmd;
    String ACTION_DEMAND = "ACTION_";
    public String EXTRA_CMD = "EXTRA_CMD";

    JSONObject deviceJSON=new JSONObject();

    public Device(String ID, String NAME, int TYPE,String STATUS)
    {
        this.type=TYPE;
        this.name=NAME;
        this.id=ID;
        this.status=STATUS;

        switch (type)
        {
            case 1:
                if(status.equalsIgnoreCase("1"))
                {
                    this.cmd = new Cmd("SET","0");

                }else{
                    this.cmd = new Cmd("SET","1");
                }
                break;
            default:
        }

    }

    public JSONObject getJSONObject()
    {
        try{
            deviceJSON.put("id",this.id);
            deviceJSON.put("name",this.name);
            deviceJSON.put("type",this.type);
            deviceJSON.put("status",this.status);
            deviceJSON.put("cmd",this.cmd.getJSONObject());
            return deviceJSON;
        }catch(JSONException e)
        {
            e.printStackTrace();
        }
        return deviceJSON;
    }

    public NotificationCompat.Action getAction(Context c)
    {
        ACTION_DEMAND+=(this.id+cmd.cmd+cmd.value);
        Intent demandIntent = new Intent(c, ActionReceiver.class)
                .putExtra(EXTRA_CMD, this.getJSONObject().toString())
                        .setAction(ACTION_DEMAND);

        PendingIntent demandPendingIntent =
                PendingIntent.getBroadcast(c, 0, demandIntent, 0);

        NotificationCompat.Action Action=null;


        if(type==1)
        {

            if(cmd.value.equalsIgnoreCase("0"))
            {
                Action = new NotificationCompat.Action.Builder(R.drawable.eteindre,
                                this.name, demandPendingIntent)
                                .build();

            }else
            {
                Action = new NotificationCompat.Action.Builder(R.drawable.allumer,
                        this.name, demandPendingIntent)
                        .build();


            }
        }else
        {
            Action = new NotificationCompat.Action.Builder(R.drawable.go_to_phone_00157,
                    "problème", demandPendingIntent)
                    .build();

        }

        if(Action!=null)
        {

            return Action;
        }else
        {
            Action = new NotificationCompat.Action.Builder(R.drawable.ic_full_cancel,
                    "problème", demandPendingIntent)
                    .build();

            return Action;
        }


    }
}
