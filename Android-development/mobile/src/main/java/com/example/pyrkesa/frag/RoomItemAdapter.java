package com.example.pyrkesa.frag;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pyrkesa.shwc.R;

import java.util.ArrayList;

/**
 * Created by pyrkesa on 03/02/2015.
 */
public class RoomItemAdapter extends BaseAdapter {
    private Context context1;
    private ArrayList<RoomItem> roomItems;
    private ListView room_beacon_list;


    private ArrayList<BeaconItem> beaconItems=new ArrayList<BeaconItem>();
    private BeaconFoundAdapter bfAdapter;
    //private ListView beaconList;
    private String[] beaconAdress;
    private TypedArray beaconIcon;

    private ArrayList<DeviceItem> deviceItems =new ArrayList<DeviceItem>();
    private DeviceItemAdapter dAdapter;
    private ListView deviceList;
    private String[] deviceName;
    private TypedArray deviceIcon;

    public RoomItemAdapter(Context context1, ArrayList<RoomItem> roomItems){
        this.context1 = context1;
        this.roomItems= roomItems;
    }

    @Override
    public int getCount() {
        return roomItems.size();
    }

    @Override
    public Object getItem(int position){return roomItems.get(position);
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
            convertView = mInflater.inflate(R.layout.room_list_item_rooms, null);
        }


        TextView name_salle = (TextView) convertView.findViewById(R.id.salle_name);
        TextView beacon_title = (TextView) convertView.findViewById(R.id.rom_device);
        TextView device_title = (TextView) convertView.findViewById(R.id.equ);
        //ListView room_beacon_list = (ListView)convertView.findViewById(R.id.room_beacon_list);
        ListView room_equipement_found = (ListView)convertView.findViewById(R.id.room_equipement_list);

        name_salle.setText(roomItems.get(position).getRoom_name());
        beacon_title.setText(roomItems.get(position).getBeacon_title());
        device_title.setText(roomItems.get(position).getDevice_title());

        beaconIcon = convertView.getResources().obtainTypedArray(R.array.beaconimg);// load icons from
        beaconAdress = convertView.getResources().getStringArray(R.array.beaconaddr); // load
        room_beacon_list= (ListView) convertView.findViewById(R.id.room_beacon_list_design);

        for (int i = 0; i < beaconAdress.length; i++) {
            beaconItems.add(new BeaconItem(beaconIcon.getResourceId(0, 0),
                    beaconAdress[i]));
        }
        bfAdapter = new BeaconFoundAdapter(context1, beaconItems);
        room_beacon_list.setAdapter(bfAdapter);
       // imgIcon.setImageResource(deviceItems.get(position).getDevice());
        //txtTitle.setText(deviceItems.get(position).getTitle());

       /* beaconIcon = convertView.getResources().obtainTypedArray(R.array.beaconimg);// load icons from
        beaconAdress = convertView.getResources().getStringArray(R.array.beaconaddr); // load
        room_beacon_list= (ListView) convertView.findViewById(R.id.beacons_list);

        for (int i = 0; i < beaconAdress.length; i++) {
            beaconItems.add(new BeaconItem(beaconIcon.getResourceId(0, 0),
                    beaconAdress[i]));
        }
        bfAdapter = new BeaconFoundAdapter(context1, beaconItems);
        room_beacon_list.setAdapter(bfAdapter);


        deviceIcon = convertView.getResources().obtainTypedArray(R.array.deviceicon);
        deviceName = convertView.getResources().getStringArray(R.array.devicename);
        deviceList = (ListView) convertView.findViewById(R.id.equipements_list);


        deviceItems = new ArrayList<DeviceItem>();
        // adding device items

        for (int i = 0; i < deviceName.length; i++) {
            deviceItems.add(new DeviceItem(deviceIcon.getResourceId(0, 0),
                    deviceName[i],null));
        }
        dAdapter = new DeviceItemAdapter(context1, deviceItems);
        deviceList.setAdapter(dAdapter);*/

        return convertView;
    }




}



