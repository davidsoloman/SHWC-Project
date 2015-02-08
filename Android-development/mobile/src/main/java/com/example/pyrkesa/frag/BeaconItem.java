package com.example.pyrkesa.frag;

/**
 * Created by pyrkesa on 02/02/2015.
 */
public class BeaconItem {
    private String title;
    private int beacon;
    String type="beacon";


    public BeaconItem() {
    }

    public BeaconItem(int beacon, String title) {
        this.title = title;
        this.beacon = beacon;

    }
public String getType()
{
    return type;
}

    public BeaconItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public int getBeacon() {
        return this.beacon;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setBeacon(int beacon) {
        this.beacon = beacon;
    }

}

