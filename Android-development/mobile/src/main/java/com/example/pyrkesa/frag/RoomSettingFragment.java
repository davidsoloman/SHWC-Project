/*
 * RoomSettingFragment : This class is used to display all room list and features with the adapter class
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

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


import com.example.pyrkesa.com.example.pyrkesa.home.Device;
import com.example.pyrkesa.com.example.pyrkesa.home.Actuator;
import com.example.pyrkesa.shwc.JSONParser;
import com.example.pyrkesa.shwc.R;
import com.example.pyrkesa.singleton.ModelFactory;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pyrkesa on 29/01/2015.
 */
public class RoomSettingFragment extends Fragment {

    private ArrayList<BeaconItem> beaconItems;
    private BeaconFoundAdapter bfAdapter;
    private ListView beaconList;
    private String[] beaconAdress;
    private TypedArray beaconIcon;


    private ArrayList<DeviceItem> deviceItems;
    private DeviceItemAdapter dAdapter;
    private ListView deviceList;
    private String[] deviceName;
    private TypedArray deviceIcon;


    private ArrayList<RoomItem> roomsItems;
    private RoomItemAdapter rAdapter;
    private ListView roomList;
    private String[] room_name;
    private String beacon_title;
    private String device_title;
    private ListView DeviceListRoom;
    private ListView BeaconListRoom;
//    private TypedArray deviceIcon;


    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();


    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_DEVICE_ID = "id";
    private static final String TAG_TYPE = "type";
    private static final String TAG_DEVICE_NAME = "name";
    private static final String TAG_DEVICES = "devices";

    public RoomSettingFragment() {
    }

    /* List<Item> items1, items2, items3;
     ListView listView1, listView2, listView3;
     ItemsListAdapter myItemsListAdapter1, myItemsListAdapter2, myItemsListAdapter3;
     RoomItem area1, area2, area3;
     TextView prompt;
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.room_setting_fragment, container, false);


        beaconList = (ListView) rootView.findViewById(R.id.beacons_list);
        deviceList = (ListView) rootView.findViewById(R.id.equipements_list);
        roomList = (ListView)rootView.findViewById(R.id.salles_list);

        beaconIcon = getResources().obtainTypedArray(R.array.beaconimg);// load icons from
        beaconAdress = getResources().getStringArray(R.array.beaconaddr); // load

        deviceIcon = getResources().obtainTypedArray(R.array.deviceicon);
        deviceName = getResources().getStringArray(R.array.devicename);
        room_name=getResources().getStringArray(R.array.roomname);



        beaconItems = new ArrayList<BeaconItem>();
        deviceItems = new ArrayList<DeviceItem>();
        roomsItems = new ArrayList<RoomItem>();
        // adding user items

       /* for (int i = 0; i < beaconAdress.length; i++) {
            beaconItems.add(new BeaconItem(beaconIcon.getResourceId(0, 0),
                    beaconAdress[i]));
        }
        bfAdapter = new BeaconFoundAdapter(this.getActivity(), beaconItems);
        beaconList.setAdapter(bfAdapter);
*/
        for (int i = 0; i < room_name.length; i++) {
            roomsItems.add(new RoomItem(room_name[i],beacon_title,device_title,BeaconListRoom,BeaconListRoom));
        }
        rAdapter = new RoomItemAdapter(this.getActivity(), roomsItems);
        roomList.setAdapter(rAdapter);


        new LoadAllORDevices().execute();






      /*  getActivity().setContentView(R.layout.room_setting_fragment);
        listView1 = (ListView)rootView.findViewById(R.id.beacons);
        listView2 = (ListView)rootView.findViewById(R.id.salles);
        //listView3 = (ListView)findViewById(R.id.listview3);

        area1 = (RoomItem)rootView.findViewById(R.id.list_beacon_trouve);
        area2 = (RoomItem)rootView.findViewById(R.id.list_salles_trouve);
        area3 = (RoomItem)rootView.findViewById(R.id.beacons);
        area1.setOnDragListener(myOnDragListener);
        area2.setOnDragListener(myOnDragListener);
        area3.setOnDragListener(myOnDragListener);
        area1.setListView(listView1);
        area2.setListView(listView2);
        area3.setListView(listView3);

        initItems();
        myItemsListAdapter1 = new ItemsListAdapter(getActivity().getApplication(), items1);
        myItemsListAdapter2 = new ItemsListAdapter(getActivity().getApplication(), items2);
        myItemsListAdapter3 = new ItemsListAdapter(getActivity().getApplication(), items3);
        listView1.setAdapter(myItemsListAdapter1);
        listView2.setAdapter(myItemsListAdapter2);
        listView3.setAdapter(myItemsListAdapter3);

        //Auto scroll to end of ListView
        listView1.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView2.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView3.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        listView1.setOnItemClickListener(listOnItemClickListener);
        listView2.setOnItemClickListener(listOnItemClickListener);
        listView3.setOnItemClickListener(listOnItemClickListener);

        listView1.setOnItemLongClickListener(myOnItemLongClickListener);
        listView2.setOnItemLongClickListener(myOnItemLongClickListener);
        listView3.setOnItemLongClickListener(myOnItemLongClickListener);

        prompt = (TextView) rootView.findViewById(R.id.txtLabel);
        // make TextView scrollable
        prompt.setMovementMethod(new ScrollingMovementMethod());
        //clear prompt area if LongClick
        prompt.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View v) {
                prompt.setText("");
                return true;
            }});




    return rootView;
    }

    //items stored in ListView
    public class Item {
        Drawable ItemDrawable;
        String ItemString;
        Item(Drawable drawable, String t){
            ItemDrawable = drawable;
            ItemString = t;
        }
    }

    //objects passed in Drag and Drop operation
    class PassObject{
        View view;
        Item item;
        List<Item> srcList;

        PassObject(View v, Item i, List<Item> s){
            view = v;
            item = i;
            srcList = s;
        }
    }

    static class ViewHolder {
        ImageView icon;
        TextView text;
    }

   public class ItemsListAdapter extends BaseAdapter {

        private Context context;
        private List<Item> list;

        ItemsListAdapter(Context c, List<Item> l){
            context = c;
            list = l;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.room_list_item_beaconfound, null);

                ViewHolder viewHolder = new ViewHolder();
                viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.rowTextView);
                rowView.setTag(viewHolder);
            }

            ViewHolder holder = (ViewHolder) rowView.getTag();
            holder.icon.setImageDrawable(list.get(position).ItemDrawable);
            holder.text.setText(list.get(position).ItemString);

            return rowView;
        }

        public List<Item> getList(){
            return list;
        }
    }





    AdapterView.OnItemLongClickListener myOnItemLongClickListener = new AdapterView.OnItemLongClickListener(){

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                       int position, long id) {
            Item selectedItem = (Item)(parent.getItemAtPosition(position));

            ItemsListAdapter associatedAdapter = (ItemsListAdapter)(parent.getAdapter());
            List<Item> associatedList = associatedAdapter.getList();

            PassObject passObj = new PassObject(view, selectedItem, associatedList);


            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, passObj, 0);

            return true;
        }

    };

    View.OnDragListener myOnDragListener = new View.OnDragListener() {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            String area;
            if(v == area1){
                area = "area1";
            }else if(v == area2){
                area = "area2";
            }else if(v == area3){
                area = "area3";
            }else{
                area = "unknown";
            }

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    prompt.append("ACTION_DRAG_STARTED: " + area  + "\n");
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    prompt.append("ACTION_DRAG_ENTERED: " + area  + "\n");
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    prompt.append("ACTION_DRAG_EXITED: " + area  + "\n");
                    break;
                case DragEvent.ACTION_DROP:
                    prompt.append("ACTION_DROP: " + area  + "\n");

                    PassObject passObj = (PassObject)event.getLocalState();
                    View view = passObj.view;
                    Item passedItem = passObj.item;
                    List<Item> srcList = passObj.srcList;
                    ListView oldParent = (ListView)view.getParent();
                    ItemsListAdapter srcAdapter = (ItemsListAdapter)(oldParent.getAdapter());

                    RoomItem newParent = (RoomItem)v;
                    ItemsListAdapter destAdapter = (ItemsListAdapter)(newParent.listView.getAdapter());
                    List<Item> destList = destAdapter.getList();

                    if(removeItemToList(srcList, passedItem)){
                        addItemToList(destList, passedItem);
                    }

                    srcAdapter.notifyDataSetChanged();
                    destAdapter.notifyDataSetChanged();

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    prompt.append("ACTION_DRAG_ENDED: " + area  + "\n");
                default:
                    break;
            }

            return true;
        }

    };

    AdapterView.OnItemClickListener listOnItemClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Toast.makeText(getActivity().getApplication(),
                    ((Item) (parent.getItemAtPosition(position))).ItemString,
                    Toast.LENGTH_SHORT).show();
        }

    };

    private void initItems(){
        items1 = new ArrayList<Item>();
        items2 = new ArrayList<Item>();
        items3 = new ArrayList<Item>();

        TypedArray arrayDrawable = getResources().obtainTypedArray(R.array.resicon);
        TypedArray arrayText = getResources().obtainTypedArray(R.array.restext);

        for(int i=0; i<arrayDrawable.length(); i++){
            Drawable d = arrayDrawable.getDrawable(i);
            String s = arrayText.getString(i);
            Item item = new Item(d, s);
            items1.add(item);
        }

        arrayDrawable.recycle();
        arrayText.recycle();
    }

    private boolean removeItemToList(List<Item> l, Item it){
        boolean result = l.remove(it);
        return result;
    }

    private boolean addItemToList(List<Item> l, Item it){
        boolean result = l.add(it);
        return result;
    }*/
        return rootView;
    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     */
    class LoadAllORDevices extends AsyncTask<Void, String, Boolean> {

        /**
         * Before starting background thread Show Progress Dialog
         */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Chargement...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        /**
         * getting All products from url
         */
        protected Boolean doInBackground(Void... args) {
            ModelFactory model = (ModelFactory)ModelFactory.getContext();

            String url=model.api_url+"Devices/get_all_or_devices.php";
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All devices: ", json.toString());


            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);


                if (success == 1) {

                    // products found
                    // Getting Array of Products

                        JSONArray devices = json.getJSONArray(TAG_DEVICES);
                        ArrayList<Device> allDevices = new ArrayList<Device>();

                        for (int z = 0; z < devices.length(); z++) {
                            JSONObject u = devices.getJSONObject(z);
                            deviceItems.add(new DeviceItem(deviceIcon.getResourceId(0, 0),
                                    u.getString(TAG_DEVICE_NAME),null));
                            Actuator a=new Actuator();
                            a.id=null;
                            a.name=u.getString(TAG_DEVICE_NAME);
                            a.status="0";
                            a.type=Integer.parseInt(u.getString(TAG_TYPE));
                            allDevices.add(a);

                        }
                        model.or_devices=allDevices;
                        // adding HashList to ArrayList

                    }

                    return true;


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            if(result)
            {
                dAdapter = new DeviceItemAdapter(getActivity(),deviceItems);
                deviceList.setAdapter(dAdapter);
            }

        }

    }
}
