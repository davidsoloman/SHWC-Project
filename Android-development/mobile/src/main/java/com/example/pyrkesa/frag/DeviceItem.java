/*
 * DeviceItem : This class is used to store and get details about the listview DeviceItem
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

