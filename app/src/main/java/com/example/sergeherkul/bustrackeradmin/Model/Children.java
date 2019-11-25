package com.example.sergeherkul.bustrackeradmin.Model;

public class Children {

    public String child_first_name;
    public String child_last_name;
    public String child_class;
    public String child_code;
    public String child_gender;

    public Children(){

    }

    public Children(String child_first_name, String child_last_name,
                    String child_class, String child_code, String gender) {

        this.child_first_name = child_first_name;
        this.child_last_name = child_last_name;
        this.child_class = child_class;
        this.child_code = child_code;
        this.child_gender = gender;
    }


    public String getChild_first_name(){return child_first_name; }

    public String getChild_last_name(){return child_last_name; }

    public String getChild_class(){return child_class; }

    public String getChild_code(){return child_code; }

    public String getChild_gender(){return child_gender; }

}
