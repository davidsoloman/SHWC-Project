/*
 * DeviceItemAdapter : This class extend of BaseAdapter is used to fill in the listview with personal items (icon, text...)
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

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pyrkesa.shwc.R;

import java.util.ArrayList;

/**
 * Created by pyrkesa on 03/02/2015.
 */
public class DeviceItemAdapter extends BaseAdapter {
    private Context context1;
    private ArrayList<DeviceItem> deviceItems;

    public DeviceItemAdapter(Context context1, ArrayList<DeviceItem> deviceItems){
        this.context1 = context1;
        this.deviceItems = deviceItems;
    }

    @Override
    public int getCount() {
        return deviceItems.size();
    }

    @Override
    public Object getItem(int position){return deviceItems.get(position);
    }

    @Override
    public long getItemId(int position) {


        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context1.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.room_device_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.device_icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.device_name);
        imgIcon.setImageResource(deviceItems.get(position).getDevice());
        txtTitle.setText(deviceItems.get(position).getTitle());

        return convertView;
    }
}
