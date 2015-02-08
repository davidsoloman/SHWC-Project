/*
 * RoomItem : This class is used to store and get details about the listview RoomItem
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
package com.example.pyrkesa.frag;


import android.widget.ListView;

/**
 * Created by pyrkesa on 03/02/2015.
 */
public class RoomItem {
    private String room_name;
    private String device_title;
    private String beacon_title;
    private ListView beaconItem;
    private ListView deviceItem;

public RoomItem(String room_name,String device_title,String beacon_title,ListView beaconItem,ListView deviceItem) {
        this.room_name=room_name;
        this.device_title=device_title;
        this.beacon_title=beacon_title;
        this.beaconItem=beaconItem;
        this.deviceItem=deviceItem;

    }

    //getter
    public String getRoom_name (){

        return this.room_name;
    }
    public String getDevice_title (){

        return this.device_title;
    }
    public String getBeacon_title (){

        return this.beacon_title;
    }
    public ListView getBeaconItem (){



        return this.beaconItem;
    }
    public ListView getDeviceItem(){
        return this.deviceItem;
    }


    //setter
    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }
    public void setDevice_title(String device_title) {
        this.device_title= device_title;
    }

    public void setBeacon_title(String beacon_title) {
        this.beacon_title = beacon_title;
    }
    public void setBeaconItem(ListView beaconItem){
        this.beaconItem=beaconItem;
    }
    public void setDeviceItem(ListView deviceItem){

        this.deviceItem=deviceItem;
    }

}

/*    ListView listView;

    public RoomItem(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public RoomItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public RoomItem(Context context, AttributeSet attrs,
                                int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
    }

    public void setListView(ListView lv){

        listView = lv;
    }

}*/
