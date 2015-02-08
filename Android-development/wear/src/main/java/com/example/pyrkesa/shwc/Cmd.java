package com.example.pyrkesa.shwc;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alassane on 01/02/2015.
 */
public class Cmd {
    public String cmd;
    public String value;
    JSONObject cmdJSON=new JSONObject();

    public Cmd(String CMD, String VALUE)
    {
        this.cmd=CMD;
        this.value=VALUE;
    }



    public JSONObject getJSONObject()
    {
        try{
            cmdJSON.put("cmd",this.cmd);
            cmdJSON.put("value",this.value);
        }catch(JSONException e)
        {
            e.printStackTrace();
        }
        return cmdJSON;
    }
}
