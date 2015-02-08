/*
 * ScenarioAdapter : This class extend of BaseAdapter is used to fill in the listview with personal items (icon, text)
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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pyrkesa.shwc.JSONParser;
import com.example.pyrkesa.shwc.R;
import com.example.pyrkesa.singleton.ModelFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pyrkesa on 02/02/2015.
 */
public class ScenarioAdapter extends BaseAdapter{

    private Context context1;
    private ArrayList<ScenarioItem> scenarioItems;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private Activity activity;
    private int pos;
    public ScenarioAdapter(Context context1, ArrayList<ScenarioItem> scenarioItems, Activity a){
        this.context1 = context1;
        this.scenarioItems = scenarioItems;
        activity=a;
    }

    @Override
    public int getCount() {
        return scenarioItems.size();
    }

    @Override
    public Object getItem(int position) {
        return scenarioItems.get(position);
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
            convertView = mInflater.inflate(R.layout.scenario_list_item, null);
        }
        pos=position;
        ImageView imgIcon =(ImageView) convertView.findViewById(R.id.scenario);
        final TextView txtTitle = (TextView) convertView.findViewById(R.id.scenario_name);
        ImageButton imgDelete = (ImageButton) convertView.findViewById(R.id.delete_scenario);

        imgIcon.setImageResource(scenarioItems.get(position).getIcon_Scenario());
        txtTitle.setText(scenarioItems.get(position).getName());
        imgDelete.setImageResource(scenarioItems.get(position).getIcon_Delete());
        imgDelete.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                //Toast.makeText(context1,"ggffgfg",Toast.LENGTH_LONG).show();

                ModelFactory model=(ModelFactory)ModelFactory.getContext();

                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Supprimer "+txtTitle.getText())
                            .setMessage("Voulez-vous supprimer ce scénario?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    new DeleteScenario().execute(txtTitle.getText().toString());
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
            }
        });



        return convertView;
    }


    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class DeleteScenario extends AsyncTask<String, String, Boolean> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context1);
            pDialog.setMessage("Suppression en cours ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected Boolean doInBackground(String... log) {
            String login=log[0];
            ModelFactory model=(ModelFactory)ModelFactory.getContext();

            String url=model.api_url+"Users/delete_user.php";


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("login",login));
            // getting JSON string from URL
            JSONObject json1 = jParser.makeHttpRequest(url,"GET", params);

            try{
                if(json1.getInt(TAG_SUCCESS)==1)
                {
                    scenarioItems.remove(pos);
                    return true;
                }else{
                    return false;
                }
            }catch(JSONException e)
            {
                e.printStackTrace();
            }
            return false;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(final Boolean result) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            if(result)
                notifyDataSetChanged();
            // updating UI from Background Thread
            activity.runOnUiThread(new Runnable() {

                //private ArrayList<String> AllBoxes = result;
                public void run() {

                    if (result) {
                        Toast.makeText(activity, "Scénario supprimé.", Toast.LENGTH_LONG).show();
                        Log.d("Delete scenario : ", "Success");
                    } else {
                        Log.d("Delete scenario : ", "Error");
                        Toast.makeText(activity, "Erreur lors de la suppression !", Toast.LENGTH_LONG).show();
                    }

                }


            });

        }

    }

}
