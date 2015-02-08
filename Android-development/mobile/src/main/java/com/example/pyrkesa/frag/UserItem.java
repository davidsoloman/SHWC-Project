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

