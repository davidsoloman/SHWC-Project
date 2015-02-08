package com.example.pyrkesa.shwc;
/*
 * Device : This class is used to represent a device. It's not the same class as the smartphone part.
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
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

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
