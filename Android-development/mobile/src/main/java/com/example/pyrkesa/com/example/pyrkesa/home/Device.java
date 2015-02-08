package com.example.pyrkesa.com.example.pyrkesa.home;

import org.json.JSONException;
import org.json.JSONObject;
import com.example.pyrkesa.singleton.Cmd;

/*
 * Device : This class is used to represent a device.
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

public abstract class Device {
    public String id;
    public int type; // 1: Actuator ...
    public String status;
    public String name;
    JSONObject deviceJSON=new JSONObject();

    public JSONObject getJSONObject()
    {
        try{
            deviceJSON.put("id",this.id);
            deviceJSON.put("name",this.name);
            deviceJSON.put("type",this.type);
            deviceJSON.put("status",this.status);
            return deviceJSON;
        }catch(JSONException e)
        {
            e.printStackTrace();
        }
        return deviceJSON;
    }

    public JSONObject getJSONcmd(Cmd cmd)
    {
        try{
            deviceJSON.put("id",this.id);
            deviceJSON.put("name",this.name);
            deviceJSON.put("type",this.type);
            deviceJSON.put("status",this.status);
            deviceJSON.put("cmd",cmd.getJSONObject());
            return deviceJSON;
        }catch(JSONException e)
        {
            e.printStackTrace();
        }
        return deviceJSON;
    }
}
