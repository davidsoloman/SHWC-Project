package com.example.pyrkesa.singleton;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.pyrkesa.com.example.pyrkesa.home.Device;
import com.example.pyrkesa.com.example.pyrkesa.home.Room;
import com.example.pyrkesa.shwc.JSONParser;
import com.example.pyrkesa.shwc.LoginActivity;
import com.example.pyrkesa.shwc.MainActivity;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Alassane on 28/01/2015.
 */
public class Zibase extends Box {


    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_BOX = "box";
    private static final String TAG_BOX_LOGIN = "login";
    private static final String TAG_BOX_PWD = "pwd";
    private static final String TAG_BOX_URL = "url";




    @Override
    public String Authenticate() {

        new AuthenticateZibase().execute();
        return null;
    }

    @Override
    public String Action(String cmdjson) {

        try{
            JSONObject DeviceJSON = new JSONObject(cmdjson);
            String deviceID=DeviceJSON.getString("id");
            int deviceType=DeviceJSON.getInt("type");
            JSONObject cmdJSON=DeviceJSON.getJSONObject("cmd");
            String cmdJSONType=cmdJSON.getString("cmd");
            String cmdJSONValue=cmdJSON.getString("value");

            if(cmdJSONType.equalsIgnoreCase("SET"))
            {
                if(deviceType==1)
                {
                    if(cmdJSONValue.equalsIgnoreCase("1"))
                    {
                        ArrayList<String> zibaseUrlInfos=new ArrayList<String>();
                        zibaseUrlInfos.add("execute");
                        zibaseUrlInfos.add("actuator");
                        zibaseUrlInfos.add(deviceID);
                        zibaseUrlInfos.add(cmdJSONValue);

                        new SendZibaseCMD().execute(zibaseUrlInfos);

                    }else{
                        ArrayList<String> zibaseUrlInfos=new ArrayList<String>();
                        zibaseUrlInfos.add("execute");
                        zibaseUrlInfos.add("actuator");
                        zibaseUrlInfos.add(deviceID);
                        zibaseUrlInfos.add(cmdJSONValue);

                        new SendZibaseCMD().execute(zibaseUrlInfos);
                    }
                    return "XX";
                }

            }else{
                return "error";
            }

        }catch(Throwable t)
        {
            Log.e("SHWC Zibase cmd : ", "Could not parse malformed JSON: "+ cmdjson);
            return "error";

        }


        return "error";
    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class AuthenticateZibase extends AsyncTask<Void, Void, Boolean> {

        /**
         * Before starting background thread Show Progress Dialog
         * */

        /**
         * getting All products from url
         * */
        protected Boolean doInBackground(Void... args) {


            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("NAME","Zibase"));
            // getting JSON string from URL
            JSONObject json3 = jParser.makeHttpRequest(url_api,"GET", params);

            try{
                if(json3.getInt(TAG_SUCCESS)==1)
                {
                    JSONArray boxx=json3.getJSONArray(TAG_BOX);

                    JSONObject b = boxx.getJSONObject(0);
                    name=b.getString(TAG_BOX_LOGIN);
                    login=b.getString(TAG_BOX_LOGIN);
                    token=b.getString(TAG_BOX_PWD);
                    url_zodianet=b.getString(TAG_BOX_URL);
                    status=true;

                    return true;
                }else{
                    status=false;
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

                    if(result)
                    {
                        Log.d("Zibase login : ", "SUCCESS");
                    }else
                    {
                        Log.d("Zibase login : ", "FAILED");
                    }


        }

    }


    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class SendZibaseCMD extends AsyncTask<ArrayList<String>, Void, Boolean> {

        /**
         * getting All products from url
         * */
        protected Boolean doInBackground(ArrayList<String>... cmd) {

                        ArrayList<String> zibaseInfos=cmd[0];
                        String service=zibaseInfos.get(0);
                        String target=zibaseInfos.get(1);
                        String id=zibaseInfos.get(2);
                        String action=zibaseInfos.get(3);

                        final ModelFactory model=(ModelFactory)ModelFactory.getContext();

                        List<NameValuePair> params = new ArrayList<NameValuePair>();

                        params.add(new BasicNameValuePair("zibase",model.box.login));
                        params.add(new BasicNameValuePair("token",model.box.token));
                        params.add(new BasicNameValuePair("service",service));
                        params.add(new BasicNameValuePair("target",target));
                        params.add(new BasicNameValuePair("id",id));
                        params.add(new BasicNameValuePair("action",action));

                        Log.d("Zodianet zibase : ", model.box.login);
                        Log.d("Zodianet token : ", model.box.token);
                        Log.d("Zodianet url : ", model.box.url_zodianet);

                        // getting JSON string from URL
                        JSONObject json3 = jParser.makeHttpRequest(model.box.url_zodianet,"GET", params);

                        try{
                            if(json3.getString("head").equalsIgnoreCase("success"))
                            {

                                Log.d("Action "+action+" sur device "+id,"Succès");
                                model.current_room="";
                                return true;

                            }else{
                                Log.d("Action "+action+" sur device "+id,"Erreur");
                                return false;
                            }
                        }catch(JSONException e)
                        {
                            e.printStackTrace();
                            return false;
                        }

        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog after getting all products
            if(result)
            {
                Toast.makeText(ModelFactory.getContext(), "Succés", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(ModelFactory.getContext(), "Erreur", Toast.LENGTH_LONG).show();
            }

        }

    }


}
