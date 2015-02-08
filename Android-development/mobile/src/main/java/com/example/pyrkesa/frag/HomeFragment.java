package com.example.pyrkesa.frag;

import android.app.Fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pyrkesa.shwc.R;



/**
 * Created by pyrkesa on 29/01/2015.
 */
public class HomeFragment extends Fragment {

    public static final String PATH_DISMISS = "/dismissnotification";
    public static final String TAG_DISMISS="TAGDISMISS";

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);


        return rootView;
    }


}
