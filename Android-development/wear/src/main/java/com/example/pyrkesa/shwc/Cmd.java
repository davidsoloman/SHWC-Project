package com.example.pyrkesa.shwc;
/*
 * Cmd : This class is used is used to create a cmd for the domotic box. For example for the Zibase Box for an actuator you can
 * create a object like this new CMD("SET","0").
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

import org.json.JSONException;
import org.json.JSONObject;

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
