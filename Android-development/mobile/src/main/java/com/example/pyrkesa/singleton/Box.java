package com.example.pyrkesa.singleton;

import android.content.Context;

import com.example.pyrkesa.com.example.pyrkesa.home.Device;

/*
 * Box : This abstract class is used to have a common base for all the different domotic boxes.
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
