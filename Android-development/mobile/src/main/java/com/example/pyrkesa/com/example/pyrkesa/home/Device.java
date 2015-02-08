package com.example.pyrkesa.com.example.pyrkesa.home;

import org.json.JSONException;
import org.json.JSONObject;
import com.example.pyrkesa.singleton.Cmd;

/**
 * Created by Alassane on 28/01/2015.
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
