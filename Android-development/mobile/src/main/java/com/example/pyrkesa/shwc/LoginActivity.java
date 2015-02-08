package com.example.pyrkesa.shwc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

import com.example.pyrkesa.com.example.pyrkesa.home.Room;
import com.example.pyrkesa.singleton.ModelFactory;
import com.example.pyrkesa.zeroconf.*;

import android.provider.Settings.Secure;

public class LoginActivity extends Activity {
    ImageButton LogInButton=null;

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<String> boxList=new ArrayList<String>();

    public static String shwcserverip="";
    // url to get all products list
    public static String url_all_box = "";
    public static String url_authenticate = "";
    public static String url_authenticateByDevice = "";
    public static Boolean UserAuthenticate=false;
    public static Boolean shwcserverFound=false;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_BOX = "box";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_LOGIN = "login";
    private static final String TAG_USER_INFO = "user_info";
    private static final String TAG_USER_TYPE = "type";
    private static final String TAG_USER_ID = "user_id";
    private static final String TAG_USER_ID1 = "id";

    public static FindServicesNSD x;

    // products JSONArray
    JSONArray boxes = null;

    public static void setShwcserverip(String ip)
    {
        LoginActivity.shwcserverip=ip;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_log);

        final ModelFactory model=(ModelFactory)ModelFactory.getContext();
        UserAuthenticate=model.UserAuthenticate;
        //For settings option-- put it in the first acticity on the onCreate function
        if( getIntent().getBooleanExtra("Exit me", false)){

            finish();
        }


        if(LoginActivity.UserAuthenticate)
        {
            Intent homepage = new Intent(getApplicationContext(),MainActivity.class);
            homepage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homepage);
            finish();
        }

        final Handler searchipHandler = new Handler() {
            @Override
            public void handleMessage (Message msg) {
                String shwcipadress = msg.getData().getString("ip");
                if(shwcipadress!=null)
                {
                    if(LoginActivity.UserAuthenticate==false)
                    {
                        LoginActivity.shwcserverFound=true;
                        LoginActivity.url_all_box="http://"+shwcipadress+"/SHWCDataManagement/Box/get_all_box.php";
                        LoginActivity.url_authenticate="http://"+shwcipadress+"/SHWCDataManagement/Users/authenticate.php";
                        LoginActivity.url_authenticateByDevice="http://"+shwcipadress+"/SHWCDataManagement/Users/authenticateByDevice.php";

                        Log.d("SHWCServer","IP="+shwcipadress);


                        String android_id = Secure.getString(getContentResolver(),
                                Secure.ANDROID_ID);

                        Log.d("Android id :", android_id);

                        ArrayList<String> Logs1=new ArrayList<String>();
                        Logs1.add(url_authenticateByDevice);
                        Logs1.add(android_id);
                        Logs1.add(url_all_box);
                        new AuthenticateUserByDevice().execute(Logs1);
                    }
                }

            }
        };

        // Run the ServiceNSD to find the shwcserver
        //final FindServicesNSD zeroconf=new FindServicesNSD((NsdManager)getSystemService(NSD_SERVICE), "_http._tcp",searchipHandler);
        //zeroconf.run();
       // LoginActivity.x=zeroconf;

        // simulation découverte serveur shwc
        LoginActivity.shwcserverFound=true;
        model.api_url="http://"+"10.0.1.5"+"/SHWCDataManagement/";
        LoginActivity.url_all_box=model.api_url+"Box/get_all_box.php";
        LoginActivity.url_authenticate=model.api_url+"Users/authenticate.php";
        LoginActivity.url_authenticateByDevice=model.api_url+"Users/authenticateByDevice.php";

        String android_id = Secure.getString(getContentResolver(),
                Secure.ANDROID_ID);
        Log.d("Android id :", android_id);

        ArrayList<String> Logs1=new ArrayList<String>();
        Logs1.add(url_authenticateByDevice);
        Logs1.add(android_id);
        Logs1.add(url_all_box);

        if(getIntent().hasExtra("logout"))
        {
            new LoadAllBoxes().execute(url_all_box);
        }else
        {
            new AuthenticateUserByDevice().execute(Logs1);
        }



        /// end simulation

        LogInButton = ((ImageButton) this.findViewById(R.id.LogInButton));
        // Get login and password from EditText
        final EditText login = ((EditText) this.findViewById(R.id.login));
        final EditText password = ((EditText) this.findViewById(R.id.password));
        final Spinner box_choice = ((Spinner) this.findViewById(R.id.box_choice));

        LogInButton.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {

                if(LoginActivity.shwcserverFound)
                {
                    String pass = password.getText().toString();
                    String log = login.getText().toString();
                    String box= box_choice.getSelectedItem().toString();

                    String android_id1 = Secure.getString(getContentResolver(),
                            Secure.ANDROID_ID);
                    ArrayList<String> Logs=new ArrayList<String>();
                    Logs.add(log);
                    Logs.add(pass);
                    Logs.add(url_authenticate);
                    Logs.add(android_id1);
                    Logs.add(box);
                    new AuthenticateUser().execute(Logs);
                }else
                {
                    Toast.makeText(LoginActivity.this,"Système SHWC indisponible !", Toast.LENGTH_LONG).show();
                }



            }
        });
        // on seleting single product
        // launching Edit Product Screen




    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class AuthenticateUserByDevice extends AsyncTask<ArrayList<String>, String, Boolean> {

        /**
         * Before starting background thread Show Progress Dialog
         * */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Connexion ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected Boolean doInBackground(ArrayList<String>... logs) {
            ArrayList<String> passed = logs[0];
            String url_authenticate_byDevice=passed.get(0);
            String android_id=passed.get(1);
            String url_all_box=passed.get(2);

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("DEVICE_ID",android_id));
            // getting JSON string from URL
            JSONObject json2 = jParser.makeHttpRequest(url_authenticate_byDevice,"POST", params);
                ModelFactory model=(ModelFactory)ModelFactory.getContext();
            try{
                if(json2.getInt(TAG_SUCCESS)==1)
                {
                    LoginActivity.UserAuthenticate=true;
                    model.UserAuthenticate=true;
                    String typebox=null;

                    JSONArray jsonUser=json2.getJSONArray(TAG_USER_INFO);

                    // looping through All Products
                    for (int i = 0; i < jsonUser.length(); i++) {

                        JSONObject u = jsonUser.getJSONObject(i);
                        model.user.login=u.getString(TAG_LOGIN);
                        model.user.type=Integer.parseInt(u.getString(TAG_USER_TYPE));
                        model.user.id=Integer.parseInt(u.getString(TAG_USER_ID));
                        // Storing each json item in variable

                        JSONArray jsonBox=u.getJSONArray(TAG_BOX);

                        // looping through All Products
                        for (int x = 0; x < jsonBox.length(); x++) {

                            JSONObject b = jsonBox.getJSONObject(x);

                            // Storing each json item in variable
                            typebox=b.getString(TAG_NAME);

                        }

                    }

                    model.setBox(typebox);
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
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                private Boolean success=result;
                //private ArrayList<String> AllBoxes = result;
                public void run() {

                    if(success)
                    {
                        Intent homepage = new Intent(getApplicationContext(),MainActivity.class);
                        homepage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homepage);
                        finish();
                    }else
                    {
                        // Loading boxes in Background Thread
                        new LoadAllBoxes().execute(url_all_box);
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

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllBoxes extends AsyncTask<String, String, ArrayList<String>> {

        /**
         * Before starting background thread Show Progress Dialog
         * */

         @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Loading boxes... Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        /**
         * getting All products from url
         * */
        protected ArrayList<String> doInBackground(String... url) {


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url[0], "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All boxes: ", json.toString());


            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    boxes = json.getJSONArray(TAG_BOX);

                    // looping through All Products
                    for (int i = 0; i < boxes.length(); i++) {
                        JSONObject c = boxes.getJSONObject(i);

                        // Storing each json item in variable
                        String name = c.getString(TAG_NAME);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        /*map.put(TAG_PID, id);
                        map.put(TAG_NAME, name);*/

                        // adding HashList to ArrayList
                        boxList.add(name);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final ArrayList<String> boxess=boxList;

            return boxess;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(final ArrayList<String> result) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                private ArrayList<String> AllBoxes = result;
                public void run() {

                    Spinner mySpinner = (Spinner) findViewById(R.id.box_choice);
                    // Spinner adapter
                    mySpinner
                            .setAdapter(new ArrayAdapter<String>(LoginActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    AllBoxes));
                }
            });

        }

    }


    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class AuthenticateUser extends AsyncTask<ArrayList<String>, String, Boolean> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Connexion en cours ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected Boolean doInBackground(ArrayList<String>... logs) {
            ArrayList<String> passed = logs[0];
            String log=passed.get(0);
            String pass=passed.get(1);
            String url=passed.get(2);
            String android_id1=passed.get(3);
            String box=passed.get(4);
            box.trim();
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("LOGIN",log));
            params.add(new BasicNameValuePair("PWD",pass));
            params.add(new BasicNameValuePair("ANDROID_ID",android_id1));
            params.add(new BasicNameValuePair("BOX",box));
            // getting JSON string from URL
            JSONObject json1 = jParser.makeHttpRequest(url,"POST", params);

            try{
                if(json1.getInt(TAG_SUCCESS)==1)
                {

                    ModelFactory model=(ModelFactory)ModelFactory.getContext();
                    model.UserAuthenticate=true;
                    LoginActivity.UserAuthenticate=true;
                    JSONArray jsonUser=json1.getJSONArray(TAG_USER_INFO);

                    for (int i = 0; i < jsonUser.length(); i++) {

                        JSONObject u = jsonUser.getJSONObject(i);
                        model.user.login = u.getString(TAG_LOGIN);
                        model.user.type = Integer.parseInt(u.getString(TAG_USER_TYPE));
                        model.user.id = Integer.parseInt(u.getString(TAG_USER_ID1));
                    }
                    model.setBox(box);
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
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                private Boolean success=result;
                //private ArrayList<String> AllBoxes = result;
                public void run() {

                    if(success)
                    {
                        Log.d("Authentification : ", "Success = 1");
                        Intent homepage = new Intent(getApplicationContext(),MainActivity.class);
                        homepage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homepage);
                        finish();
                    }else
                    {
                        Log.d("Authentification : ", "Success = 0");
                        Toast.makeText(LoginActivity.this,"Erreur d'authentification !", Toast.LENGTH_LONG).show();
                    }

                }


            });

        }

    }
}
