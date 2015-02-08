package com.example.pyrkesa.zeroconf;

/*
 * FindServicesNSD : This class is used to found automatically the SHWCServer on a local network.
 * The zeroconf protocol by Apple is used for this job. You have to activate this option on the MainActivity
 * file since in a network with a master DNS this function can't work.
 *
 * Copyright (c) 2015 Pierre-Yves Rancien, Alassane Diagne, Axel Francart, Clément Casasreales, Andreas Roche
 *
 * Copyright (c) 2013 Estimote, Inc.
 *
 * Copyright (c) 2013 By Simon Lewis. All Rights Reserved.
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


import android.net.nsd.NsdManager;
import android.net.nsd.NsdManager.DiscoveryListener;
import android.net.nsd.NsdManager.ResolveListener;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.pyrkesa.shwc.LoginActivity;

public class FindServicesNSD
        implements
        DiscoveryListener,
        ResolveListener
{
    // DiscoveryListener
    private Handler handler;



    @Override
    public void onDiscoveryStarted(String theServiceType)
    {
        Log.d(TAG, "onDiscoveryStarted");
    }

    @Override
    public void onStartDiscoveryFailed(String theServiceType, int theErrorCode)
    {
        Log.d(TAG, "onStartDiscoveryFailed(" + theServiceType + ", " + theErrorCode);
    }

    @Override
    public void onDiscoveryStopped(String serviceType)
    {
        Log.d(TAG, "onDiscoveryStopped");
    }

    @Override
    public void onStopDiscoveryFailed(String theServiceType, int theErrorCode)
    {
        Log.d(TAG, "onStartDiscoveryFailed(" + theServiceType + ", " + theErrorCode);
    }

    @Override
    public void onServiceFound(NsdServiceInfo theServiceInfo)
    {
        Log.d(TAG, "onServiceFound(" + theServiceInfo + ")");
        Log.d(TAG, "name == " + theServiceInfo.getServiceName());
        Log.d(TAG, "type == " + theServiceInfo.getServiceType());
        serviceFound(theServiceInfo);
    }

    @Override
    public void onServiceLost(NsdServiceInfo theServiceInfo)
    {
        Log.d(TAG, "onServiceLost(" + theServiceInfo + ")");
    }

    // Resolve Listener

    @Override
    public void onServiceResolved(NsdServiceInfo theServiceInfo)
    {
        String ip=theServiceInfo.getHost().getHostAddress();
        Log.d(TAG, "onServiceResolved(" + theServiceInfo + ")");
        Log.d(TAG, "name == " + theServiceInfo.getServiceName());
        Log.d(TAG, "type == " + theServiceInfo.getServiceType());
        Log.d(TAG, "host == " + theServiceInfo.getHost());
        Log.d(TAG, "port == " + theServiceInfo.getPort());
        if(theServiceInfo.getServiceName().equalsIgnoreCase("shwcserver"))
        {
            Log.d("IP = ", theServiceInfo.getHost().getHostAddress());
            LoginActivity.setShwcserverip(theServiceInfo.getHost().getHostAddress());
            Message msgObj = handler.obtainMessage();
            Bundle b = new Bundle();
            b.putString("ip", theServiceInfo.getHost().getHostAddress());
            msgObj.setData(b);
            handler.sendMessage(msgObj);

        }
    }

    @Override
    public void onResolveFailed(NsdServiceInfo theServiceInfo, int theErrorCode)
    {
        Log.d(TAG, "onResolveFailed(" + theServiceInfo + ", " + theErrorCode);
    }

    //

    public FindServicesNSD(NsdManager theManager, String theServiceType,Handler handfromlogactivity)
    {
        manager     = theManager;
        serviceType = theServiceType;
        handler=handfromlogactivity;
    }

    public void run()
    {
        manager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, this);
    }

    private void serviceFound(NsdServiceInfo theServiceInfo)
    {
        manager.resolveService(theServiceInfo, this);
    }

    public void tearDown() {
        manager.stopServiceDiscovery(this);
    }
    //

    public NsdManager   manager;
    public String       serviceType;

    //

    private static final String TAG = "FindServicesNSD";
}