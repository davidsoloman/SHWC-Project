package com.example.pyrkesa.singleton;

import android.app.Application;
import android.content.Context;

import com.example.pyrkesa.com.example.pyrkesa.home.Room;
import com.example.pyrkesa.com.example.pyrkesa.home.Device;
import com.example.pyrkesa.shwc.User;

import java.util.ArrayList;

/**
 * Created by Alassane on 28/01/2015.
 */
public class ModelFactory extends Application{

    public Box box = null;
    public String login=null;
    public ArrayList<Room> rooms=new ArrayList<Room>();
    public String current_room=". . .";
    public String api_url = "";
    public User user=new User();
    public Boolean LoadRoomsSuccess=false;
    public int current_page;
    private static Context mContext;
    public Boolean UserAuthenticate=false;
    public ArrayList<Device> or_devices=new ArrayList<Device>();

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public void setBox(String typebox)
    {
        if(box==null)
        {
            if(typebox.equalsIgnoreCase("Zibase"))
            {

                box = new Zibase();
                box.url_api=api_url+"Box/get_box_details.php";
                box.Authenticate();

            }else{
                    box=null;
            }
        }
    }


    public static Context getContext()
    {
        return mContext;
    }

    public Box getBox()
    {
        return box;
    }


}
