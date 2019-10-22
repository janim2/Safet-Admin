package com.example.sergeherkul.bustrackeradmin.Model;

import android.widget.ImageView;

public class ItemObjects {

    public String name;
    public int photo;

    public ItemObjects(){

    }

    public ItemObjects(String name, int photo) {

        this.name = name;
        this.photo = photo;
    }


    public String getName(){return name; }

    public int getPhoto(){return photo; }
}
