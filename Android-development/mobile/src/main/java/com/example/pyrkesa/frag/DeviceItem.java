package com.example.pyrkesa.frag;

import android.os.AsyncTask;

/**
 * Created by pyrkesa on 02/02/2015.
 */
public class DeviceItem {
    private String title;
    private int device;
    private String id_room;






    public DeviceItem(int device, String title,String id) {
        this.title = title;
        this.device = device;
        this.id_room=id;

    }

    public DeviceItem(String title) {
        this.title = title;
    }

    public String GetidRoom(String idRoom){





        return this.id_room;
    }

    public String getTitle() {
        return this.title;
    }

    public int getDevice() {
        return this.device;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDevice(int Device) {
        this.device = device;
    }

}

