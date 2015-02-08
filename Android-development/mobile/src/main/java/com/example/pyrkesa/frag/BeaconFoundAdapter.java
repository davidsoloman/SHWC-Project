/*
 * BeaconFoundAdapter : This class extend of BaseAdapter is used to fill in the listview with personal items (icon, text)
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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pyrkesa.shwc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pyrkesa on 03/02/2015.
 */
public class BeaconFoundAdapter extends BaseAdapter {

    private Context context1;
    private ArrayList<BeaconItem> beaconItems;

    public BeaconFoundAdapter(Context context1, ArrayList<BeaconItem> beaconItems){
        this.context1 = context1;
        this.beaconItems = beaconItems;
    }

    @Override
    public int getCount() {
        return beaconItems.size();
    }

    @Override
    public Object getItem(int position) {
        return beaconItems.get(position);
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
            convertView = mInflater.inflate(R.layout.room_list_item_beaconfound, null);
        }

        ImageView imgIcon =(ImageView) convertView.findViewById(R.id.beacon_foto);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.beacons_found);
/*        ImageButton imgDelete = (ImageButton) convertView.findViewById(R.id.delete);
        imgDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Toast.makeText(context1,"ggffgfg",Toast.LENGTH_LONG).show();

               final DialogFragment dialog = new DialogFragment(){
                    @Override
                    public Dialog onCreateDialog(Bundle savedInstanceState) {

                        AlertDialog.Builder builder = new  AlertDialog.Builder(getActivity());
                        builder.setTitle("Are you sure you want to delete user")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // do something

                                    }
                                })
                                .setNegativeButton("cancelling",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dismiss();
                                        // do something
                                    }
                                }).show();

                        return builder.create();

                    }

                };
                dialog.setCancelable(true);

            }
        });*/
        imgIcon.setImageResource(beaconItems.get(position).getBeacon());
        txtTitle.setText(beaconItems.get(position).getTitle());

        return convertView;
    }

}

