package com.safet.admin.bustrackeradmin.Model;

public class Images {


    public String id;
    public String imageUrl;

    public Images(){

    }

    public Images(String id, String imageUrl) {

        this.id = id;
        this.imageUrl = imageUrl;
    }

    public String getId(){return  id;}

    public String getImageUrl(){return imageUrl; }

}
