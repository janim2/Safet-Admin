package com.example.sergeherkul.bustrackeradmin;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Driver_Details extends AppCompatActivity {

    private Accessories driver_details_accessor;
    private String driver_fname, driver_lname, driver_address, driver_phone_, driver_code, school_code,
    sbus_brand, sbus_code, sbus_chasis_number, sbus_model, sbus_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver__details);

        driver_details_accessor = new Accessories(Driver_Details.this);

        school_code = driver_details_accessor.getString("school_code");

        driver_fname = driver_details_accessor.getString("driver_fname_from_admin");
        driver_lname = driver_details_accessor.getString("driver_lname_from_admin");
        driver_address = driver_details_accessor.getString("driver_address_from_admin");
        driver_phone_ = driver_details_accessor.getString("driver_phone_from_admin");
        driver_code = driver_details_accessor.getString("driver_code_from_admin");

        driver_code = driver_details_accessor.getString("driver_code_from_admin");
        driver_code = driver_details_accessor.getString("driver_code_from_admin");
        driver_code = driver_details_accessor.getString("driver_code_from_admin");
        driver_code = driver_details_accessor.getString("driver_code_from_admin");
        driver_code = driver_details_accessor.getString("driver_code_from_admin");

        sbus_brand = driver_details_accessor.getString("bus_brand");
        sbus_code = driver_details_accessor.getString("bus_code");
        sbus_chasis_number = driver_details_accessor.getString("bus_chasis_no");
        sbus_model = driver_details_accessor.getString("bus_model");
        sbus_number = driver_details_accessor.getString("bus_number_plate");

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
