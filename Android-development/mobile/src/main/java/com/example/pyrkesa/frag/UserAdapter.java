package com.example.pyrkesa.frag;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.pyrkesa.shwc.MainActivity;
import com.example.pyrkesa.shwc.R;
import com.example.pyrkesa.shwc.User;
import com.example.pyrkesa.singleton.ModelFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pyrkesa on 02/02/2015.
 */
public class UserAdapter extends BaseAdapter{

    private Context context1;
    private ArrayList<UserItem> userItems;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private Activity activity;
    private int pos;

    // Progress Dialog
    private ProgressDialog pDialog;

    public UserAdapter(Context context1, ArrayList<UserItem> userItems,Activity a){
        this.context1 = context1;
        this.userItems = userItems;
        activity=a;
    }

    @Override
    public int getCount() {
        return userItems.size();
    }

    @Override
    public Object getItem(int position) {
        return userItems.get(position);
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
            convertView = mInflater.inflate(R.layout.user_list_item, null);
        }
        pos=position;
        ImageView imgIcon =(ImageView) convertView.findViewById(R.id.contact);
        final TextView txtTitle = (TextView) convertView.findViewById(R.id.user_login);
        ImageButton imgDelete = (ImageButton) convertView.findViewById(R.id.delete);


        imgIcon.setImageResource(userItems.get(position).getIcon());
        txtTitle.setText(userItems.get(position).getTitle());
        imgDelete.setImageResource(userItems.get(position).getDelete());

        imgDelete.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                //Toast.makeText(context1,"ggffgfg",Toast.LENGTH_LONG).show();

                ModelFactory model=(ModelFactory)ModelFactory.getContext();
                if(model.user.type==1)
                {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Supprimer "+txtTitle.getText())
                            .setMessage("Voulez-vous supprimer cet utilisateur?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    new DeleteUser().execute(txtTitle.getText().toString());
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }else{
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Attention")
                            .setMessage("Opération interdite : Droits d'administration requis.").show();
                }




            }
        });



        return convertView;
    }


    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class DeleteUser extends AsyncTask<String, Void, Boolean> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        private ProgressDialog pDialogDelete=new ProgressDialog(context1);
        @Override
        protected void onPreExecute() {
            pDialogDelete.setMessage("Chargement ...");
            pDialogDelete.setCancelable(true);
            pDialogDelete.show();
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
                    userItems.remove(pos);
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
            pDialogDelete.dismiss();

            if(result)
                notifyDataSetChanged();
            // updating UI from Background Thread
            activity.runOnUiThread(new Runnable() {

                //private ArrayList<String> AllBoxes = result;
                public void run() {

                    if (result) {
                        Toast.makeText(activity, "Utilisateur supprimé.", Toast.LENGTH_LONG).show();
                        Log.d("Delete user : ", "Success");
                    } else {
                        Log.d("Delete user : ", "Error");
                        Toast.makeText(activity, "Erreur lors de la suppression !", Toast.LENGTH_LONG).show();
                    }

                }


            });

        }

    }

}
