/*
 * UserItem : This class is used to store and get details about the listview UserItem
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

/**
 * Created by pyrkesa on 02/02/2015.
 */
public class UserItem {
    private String title;
    private int icon;
    private int delete;

    public UserItem() {
    }

    public UserItem(String title, int icon, int delete) {
        this.title = title;
        this.icon = icon;
        this.delete= delete;
    }

    public UserItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public int getIcon() {
        return this.icon;
    }

    public int getDelete(){return this.delete;}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIcon(int icon, int delete) {
        this.icon = icon; this.delete=delete;
    }

}

