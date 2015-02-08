/*
 * User_Fragment : This class is used to display all user list with the adapter class
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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.pyrkesa.shwc.JSONParser;
import com.example.pyrkesa.shwc.MainActivity;
import com.example.pyrkesa.shwc.NavDrawerItem;
import com.example.pyrkesa.shwc.NavDrawerListAdapter;
import com.example.pyrkesa.shwc.R;
import com.example.pyrkesa.shwc.User;
import com.example.pyrkesa.singleton.ModelFactory;
import com.google.android.gms.tagmanager.Container;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pyrkesa on 29/01/2015.
 */
public class User_Fragment extends Fragment {

    // Progress Dialog
    //private ProgressDialog pDialogLoadUser;
   // private ProgressDialog pDialogAddUser;

    private ArrayList<UserItem> userItems;
    private UserAdapter adapter;
    private ListView userList;
    private String[] dialogTypes;
    private TypedArray userIcons;
    private TypedArray userIcons1;
    private Layout userLayout;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    public static Dialog d;

    View rootView;
    public User_Fragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setRetainInstance(true);
        rootView = inflater.inflate(R.layout.user_fragment, container, false);
        userList=(ListView)rootView.findViewById(R.id.lt_user);

        // custom dialog
        final Dialog dialog = new Dialog(rootView.getContext());
        d=dialog;
        userIcons  = getResources().obtainTypedArray(R.array.userIcons);// load icons from
        userIcons1 = getResources().obtainTypedArray(R.array.userIcons1);

        userItems = new ArrayList<UserItem>();
        adapter = new UserAdapter(getActivity(),userItems,getActivity());
        userList.setAdapter(adapter);
        ImageButton addButton= (ImageButton) rootView.findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                ModelFactory model=(ModelFactory)ModelFactory.getContext();
                if(model.user.type==1)
                {


                    dialog.setContentView(R.layout.adduser_dialog);
                    dialog.setTitle("Ajouter un utilisateur ");
                    dialogTypes = getResources().getStringArray(R.array.user_type_dialog); // load
                    Button dialogButtonOk = (Button) dialog.findViewById(R.id.button_ok);
                    Button dialogButtonCancel = (Button) dialog.findViewById(R.id.button_cancel);
                    final EditText Newlogin = (EditText) dialog.findViewById(R.id.dialogLogin);
                    final EditText NewPW = (EditText) dialog.findViewById(R.id.dialogPW);
                    final Spinner NewType = (Spinner) dialog.findViewById(R.id.dialogType);

                    ArrayList<String> types=new ArrayList<String>();

                    for(String t: dialogTypes)
                    {
                        types.add(t);
                    }

                    NewType.setAdapter(new ArrayAdapter<String>(rootView.getContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            types));
                    dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialogButtonOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ArrayList<String> temp=new ArrayList<String>();
                            String login=Newlogin.getText().toString();
                            String password=NewPW.getText().toString();
                            String type=NewType.getSelectedItem().toString();

                            if(login.equalsIgnoreCase("") && password.equalsIgnoreCase("") && type!=null)
                            {
                                new AlertDialog.Builder(v.getContext())
                                        .setTitle("Attention")
                                        .setMessage("Veuillez remplir toutes les informations.").show();

                            }else{
                                temp.add(login);
                                temp.add(password);

                                if(type.equalsIgnoreCase("admin"))
                                {
                                    temp.add("1");
                                }else if(type.equalsIgnoreCase("utilisateur"))
                                {
                                    temp.add("0");
                                }else
                                {
                                    temp.add("0");
                                }

                            }

                            new addUser().execute(temp);
                        }
                    });
                    dialog.show();

                }else{
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Attention")
                            .setMessage("Opération interdite : Droits d'administration requis.").show();
                }




            }
        });
        // adding user items

        new LoadAllUser().execute();

        return rootView;

    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllUser extends AsyncTask<Void, Void, ArrayList<User>> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        ProgressDialog pDialogLoadUser = new ProgressDialog(rootView.getContext());
        @Override
        protected void onPreExecute() {
            pDialogLoadUser.setMessage("Chargement ...");
            pDialogLoadUser.setCancelable(true);
            pDialogLoadUser.show();
        }

        /**
         * getting All products from url
         * */
        protected ArrayList<User> doInBackground(Void... args) {
            ModelFactory model=(ModelFactory)ModelFactory.getContext();

            String url=model.api_url+"Users/get_all_users.php";


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            // getting JSON string from URL
            JSONObject json1 = jParser.makeHttpRequest(url,"POST", params);

            try{
                if(json1.getInt(TAG_SUCCESS)==1)
                {
                    JSONArray users_info=json1.getJSONArray("users_info");
                    ArrayList<User> allusers = new ArrayList<User>();

                    for(int i=0;i<users_info.length();i++)
                    {
                        JSONObject u=users_info.getJSONObject(i);
                        User user=new User();
                        user.login=u.getString("login");
                        user.id=Integer.parseInt(u.getString("id"));
                        user.type=Integer.parseInt(u.getString("type"));
                        allusers.add(user);
                    }

                    return allusers;
                }else{
                    return null;
                }
            }catch(JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(final ArrayList<User> users) {
            // dismiss the dialog after getting all products
            pDialogLoadUser.dismiss();
            if(users!=null)
            {
                for(User u:users)
                {
                    userItems.add(new UserItem(u.login,
                            userIcons.getResourceId(0,0),
                            userIcons1.getResourceId(0,0)));
                }

                adapter.notifyDataSetChanged();
            }

            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {

                //private ArrayList<String> AllBoxes = result;
                public void run() {

                    if (users!=null) {

                        Log.d("LoadAlluser : ", "Success");
                    } else {
                        Log.d("Authentification : ", "Error");
                        Toast.makeText(getActivity(), "Erreur lors du chargement !", Toast.LENGTH_LONG).show();
                    }

                }


            });

        }

    }
    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class addUser extends AsyncTask<ArrayList<String>, String, User> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        private ProgressDialog pDialogAddUser=new ProgressDialog(rootView.getContext());
        @Override
        protected void onPreExecute() {
            pDialogAddUser.setMessage("Ajout en cours ...");
            pDialogAddUser.setCancelable(true);
            pDialogAddUser.show();
        }

        /**
         * getting All products from url
         * */
        protected User doInBackground(ArrayList<String>... log) {
            ArrayList<String> authenticateInfos=log[0];
            String logg=authenticateInfos.get(0);
            String pw=authenticateInfos.get(1);
            String ty=authenticateInfos.get(2);

            ModelFactory model=(ModelFactory)ModelFactory.getContext();

            String url=model.api_url+"Users/create_user.php";


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("LOGIN",logg));
            params.add(new BasicNameValuePair("PWD",pw));
            params.add(new BasicNameValuePair("TYPE",ty));

            // getting JSON string from URL
            JSONObject json1 = jParser.makeHttpRequest(url,"POST", params);

            try{
                if(json1.getInt(TAG_SUCCESS)==1)
                {
                    User d=new User();
                    d.login=logg;
                    d.type=Integer.parseInt(ty);
                    return d;
                }else{
                    return null;
                }
            }catch(JSONException e)
            {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(final User result) {
            // dismiss the dialog after getting all products
            pDialogAddUser.dismiss();
            userIcons  = getResources().obtainTypedArray(R.array.userIcons);// load icons from
            userIcons1 = getResources().obtainTypedArray(R.array.userIcons1);
            userItems.add(new UserItem(result.login,
                    userIcons.getResourceId(0,0),
                    userIcons1.getResourceId(0,0)));
            adapter.notifyDataSetChanged();
            d.dismiss();
            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {

                //private ArrayList<String> AllBoxes = result;
                public void run() {

                    if (result!=null) {

                        Toast.makeText(getActivity(), "Utilisateur ajouté.", Toast.LENGTH_LONG).show();
                        Log.d("Delete user : ", "Success");
                    } else {
                        Log.d("Delete user : ", "Error");
                        Toast.makeText(getActivity(), "Erreur lors de la suppression !", Toast.LENGTH_LONG).show();
                    }

                }


            });

        }

    }
}
