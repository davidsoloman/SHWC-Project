package com.example.pyrkesa.singleton;

import android.content.Context;

import com.example.pyrkesa.com.example.pyrkesa.home.Device;

/**
 * Created by Alassane on 27/01/2015.
 */
public abstract class Box {

    public String name;
    public String token;
    public String login;
    public static String url_api;
    public static String url_zodianet;
    public Boolean status=false;
    abstract public String Authenticate();
    abstract public String Action(String cmd);


}
