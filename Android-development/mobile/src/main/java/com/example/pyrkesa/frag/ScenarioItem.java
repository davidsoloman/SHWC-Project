/*
 * ScenarioItem : This class is used to store and get details about the listview ScenarioItem
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
public class ScenarioItem {
    private String name;
    private int icon_scenario;
    private int icon_delete;

    public ScenarioItem() {
    }

    public ScenarioItem(String name, int icon_scenario, int icon_delete) {
        this.name = name;
        this.icon_scenario = icon_scenario;
        this.icon_delete= icon_delete;
    }

    public ScenarioItem(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getIcon_Scenario() {
        return this.icon_scenario;
    }

    public int getIcon_Delete(){return this.icon_delete;}

    public void setTitle(String name) {
        this.name= name;
    }

    public void setIcon(int icon_scenario, int icon_delete) {
        this.icon_scenario = icon_scenario; this.icon_delete=icon_delete;
    }

}

