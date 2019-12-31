package com.safet.admin.bustrackeradmin.Model;

public class Parents {

    public String parent_first_name;
    public String parent_last_name;
    public String parent_address;
    public String parent_phone_number;
    public String parent_email;
    public String parent_code;

    public Parents(){

    }

    public Parents(String parent_first_name, String parent_last_name,
                   String parent_address, String parent_phone_number,String parent_email,
                   String parent_code) {

        this.parent_first_name = parent_first_name;
        this.parent_last_name = parent_last_name;
        this.parent_address = parent_address;
        this.parent_phone_number = parent_phone_number;
        this.parent_email = parent_email;
        this.parent_code = parent_code;
    }


    public String getParent_first_name(){return parent_first_name; }

    public String getParent_last_name(){return parent_last_name; }

    public String getParent_address(){return parent_address; }

    public String getParent_phone_number(){return parent_phone_number; }

    public String getParent_email(){return parent_email; }

    public String getParent_code(){return parent_code; }


}
