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

