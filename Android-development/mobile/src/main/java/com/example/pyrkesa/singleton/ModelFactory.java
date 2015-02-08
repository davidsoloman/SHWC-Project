package com.example.pyrkesa.singleton;
/*
 * ModelFactory : This class is used for all globals variables like the current domotic box, the current room,
 * the current user etc... The box object is a singleton and is instantiate at the after log in the app.
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

import android.app.Application;
import android.content.Context;

import com.example.pyrkesa.com.example.pyrkesa.home.Room;
import com.example.pyrkesa.com.example.pyrkesa.home.Device;
import com.example.pyrkesa.shwc.User;

import java.util.ArrayList;


public class ModelFactory extends Application{

    public Box box = null;
    public String login=null;
    public ArrayList<Room> rooms=new ArrayList<Room>();
    public String current_room=". . .";
    public String api_url = "";
    public User user=new User();
    public Boolean LoadRoomsSuccess=false;
    public int current_page;
    private static Context mContext;
    public Boolean UserAuthenticate=false;
    public ArrayList<Device> or_devices=new ArrayList<Device>();

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public void setBox(String typebox)
    {
        if(box==null)
        {
            if(typebox.equalsIgnoreCase("Zibase"))
            {

                box = new Zibase();
                box.url_api=api_url+"Box/get_box_details.php";
                box.Authenticate();

            }else{
                    box=null;
            }
        }
    }


    public static Context getContext()
    {
        return mContext;
    }

    public Box getBox()
    {
        return box;
    }


}
