package com.example.pyrkesa.frag;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pyrkesa.com.example.pyrkesa.home.Scenario;
import com.example.pyrkesa.shwc.JSONParser;
import com.example.pyrkesa.shwc.R;
import com.example.pyrkesa.shwc.User;
import com.example.pyrkesa.singleton.ModelFactory;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pyrkesa on 29/01/2015.
 */
public class ScenarioDetail extends Fragment {


    private ArrayList<ScenarioItem> scenarioItems;
    private ScenarioAdapter adapter;
    private ListView scenarioList;

    private TypedArray scenarioIcon;
    private TypedArray scenarioIconDel;
    private Layout scenarioLayout;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    public ScenarioDetail() {
    }

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.scenario_detail, container, false);
        //userLayout = (Layout)getView().findViewById(R.id.user_layout);
     /*   scenarioList=(ListView)rootView.findViewById(R.id.lt_scenario);

        scenarioName = getResources().getStringArray(R.array.user_list_item); // load
        scenarioIcon  = getResources().obtainTypedArray(R.array.scenarioIcons);// load icons from
        scenarioIconDel = getResources().obtainTypedArray(R.array.userIcons1);



        scenarioItems = new ArrayList<ScenarioItem>();

        // adding user items

        new LoadAllScenario().execute();



*/



        return rootView;

    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllScenario extends AsyncTask<Void, String, ArrayList<Scenario>> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Chargement des utilisateurs ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected ArrayList<Scenario> doInBackground(Void... args) {
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
                    ArrayList<Scenario> allscenario = new ArrayList<Scenario>();

                    for(int i=0;i<users_info.length();i++)
                    {
                        JSONObject u=users_info.getJSONObject(i);
                        Scenario scenario=new Scenario();
                        scenario.id=Integer.parseInt(u.getString("id"));
                        scenario.userId=Integer.parseInt(u.getString("user_id"));
                        scenario.roomId=Integer.parseInt(u.getString("room_id"));
                        allscenario.add(scenario);
                    }


                    return allscenario;
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
            pDialog.dismiss();
            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {

                //private ArrayList<String> AllBoxes = result;
                public void run() {

                    if (users!=null) {
                        for(User u:users)
                        {
                           /* userItems.add(new UserItem(u.login,
                                    userIcons.getResourceId(0,0),
                                    userIcons1.getResourceId(0,0)));*/
                        }
                        /*adapter = new UserAdapter(getActivity(),userItems,getActivity());
                        userList.setAdapter(adapter);
                        Log.d("LoadAlluser : ", "Success");*/
                    } else {
                        Log.d("Authentification : ", "Error");
                        Toast.makeText(getActivity(), "Erreur lors du chargement !", Toast.LENGTH_LONG).show();
                    }

                    /*Spinner mySpinner = (Spinner) findViewById(R.id.box_choice);
                    // Spinner adapter
                    mySpinner
                            .setAdapter(new ArrayAdapter<String>(LoginActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    AllBoxes));*/
                }


            });

        }

    }
}
