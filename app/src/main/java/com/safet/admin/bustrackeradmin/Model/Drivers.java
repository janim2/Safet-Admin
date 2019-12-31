package com.safet.admin.bustrackeradmin.Model;

public class Drivers {

    public String driver_first_name;
    public String driver_last_name;
    public String driver_address;
    public String driver_phone_number;
    public String driver_code;

    public Drivers(){

    }

    public Drivers(String driver_first_name, String driver_last_name,
                   String driver_address, String driver_phone_number,String driver_code) {

        this.driver_first_name = driver_first_name;
        this.driver_last_name = driver_last_name;
        this.driver_address = driver_address;
        this.driver_phone_number = driver_phone_number;
        this.driver_code = driver_code;
    }


    public String getDriver_first_name(){return driver_first_name; }

    public String getDriver_last_name(){return driver_last_name; }

    public String getDriver_address(){return driver_address; }

    public String getDriver_phone_number(){return driver_phone_number; }

    public String getDriver_code(){return driver_code; }


}
