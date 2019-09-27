package com.example.sergeherkul.bustrackeradmin;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Driver_Profile extends AppCompatActivity {

    private TextView all_corrections_text,personal_text, school_text, bus_text,
    drivername, driverphonenumber, driveraddress, school_name, school_email, school_number,
    school_location, bus_brand, bus_model, bus_chasis, busnumber_plate,
            thedrivername, thedriverphone_number, thedriveraddress, theschoolname,
    theschool_email, theschoolnumber, theschool_location, thebus_brand, thebus_model, thebus_chasis,
    thebusnumber_plate;

    Accessories profileAccessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);
        getSupportActionBar().setTitle("Driver_Profile");

        profileAccessor = new Accessories(Driver_Profile.this);

        Typeface lovelo =Typeface.createFromAsset(getAssets(),  "fonts/lovelo.ttf");

        all_corrections_text = findViewById(R.id.all_correctins_text);
        personal_text = findViewById(R.id.personal_details_text);
        school_text = findViewById(R.id.schooldetails_text);
        bus_text = findViewById(R.id.bus_details_text);
        drivername = findViewById(R.id.dname_text);
        driverphonenumber = findViewById(R.id.dphone_number_text);
        driveraddress = findViewById(R.id.daddress_text);
        school_name = findViewById(R.id.sname_text);
        school_email = findViewById(R.id.semail_text);
        school_number = findViewById(R.id.sphonenumber_text);
        school_location = findViewById(R.id.slocation_text);
        bus_brand = findViewById(R.id.bus_brand_text);
        bus_model = findViewById(R.id.bus_model_text);
        bus_chasis = findViewById(R.id.bus_chasis_number);
        busnumber_plate = findViewById(R.id.bus_number_plate);

        //populate these from database
        thedrivername = findViewById(R.id.thedrivername);
        thedriverphone_number = findViewById(R.id.thedriver_number);
        thedriveraddress = findViewById(R.id.thedriver_address);
        theschoolname = findViewById(R.id.the_school_name);
        theschool_email = findViewById(R.id.the_school_email);
        theschool_location = findViewById(R.id.theschool_address);
        theschoolnumber = findViewById(R.id.theschool_number);
        thebus_brand  = findViewById(R.id.the_bus_brand);
        thebus_chasis  = findViewById(R.id.thebus_chasis);
        thebus_model  = findViewById(R.id.the_bus_model);
        thebusnumber_plate  = findViewById(R.id.the_bus_number_plate);

        all_corrections_text.setTypeface(lovelo);
        personal_text.setTypeface(lovelo);
        school_text.setTypeface(lovelo);
        bus_text.setTypeface(lovelo);
        drivername.setTypeface(lovelo);
        driverphonenumber.setTypeface(lovelo);
        driveraddress.setTypeface(lovelo);
        school_email.setTypeface(lovelo);
        school_number.setTypeface(lovelo);
        school_location.setTypeface(lovelo);
        bus_brand.setTypeface(lovelo);
        bus_model.setTypeface(lovelo);
        bus_chasis.setTypeface(lovelo);
        busnumber_plate.setTypeface(lovelo);

        //setting the neccessary values
//        driver
        thedrivername.setText(profileAccessor.getString("driver_fname")+" "+profileAccessor.getString("driver_lname"));
        thedriverphone_number.setText(profileAccessor.getString("driver_pnumber"));
        thedriveraddress.setText(profileAccessor.getString("driver_address"));

//        school
        theschoolname.setText(profileAccessor.getString("school_name"));
        theschoolnumber.setText(profileAccessor.getString("school_telephone"));
        theschool_location.setText(profileAccessor.getString("school_location"));
        theschool_email.setText(profileAccessor.getString("school_email"));

        //bus
        thebus_brand.setText(profileAccessor.getString("bus_brand"));
        thebus_model.setText(profileAccessor.getString("bus_chasis_no"));
        thebus_chasis.setText(profileAccessor.getString("bus_model"));
        thebusnumber_plate.setText(profileAccessor.getString("bus_number_plate"));

    }
}
