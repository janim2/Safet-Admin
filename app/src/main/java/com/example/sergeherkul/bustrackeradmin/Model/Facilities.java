package com.example.sergeherkul.bustrackeradmin.Model;

public class Facilities {


    public String id;
    public String facility_name;
    public String imageUrl;

    public Facilities(){

    }

    public Facilities(String id, String facility_name, String imageUrl) {

        this.id = id;
        facility_name = facility_name;
        this.imageUrl = imageUrl;
    }

    public String getId(){return  id;}

    public String getFacility_name(){return facility_name; }

    public String getImageUrl(){return imageUrl; }

}
