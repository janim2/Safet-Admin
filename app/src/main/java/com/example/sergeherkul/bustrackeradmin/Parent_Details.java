package com.example.sergeherkul.bustrackeradmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Parent_Details extends AppCompatActivity {

    private Accessories parent_details_accessor;
    private String parent_fname, parent_lname, parent_address,parent_phone, parent_email, parent_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent__details);

        parent_details_accessor = new Accessories(Parent_Details.this);

        parent_fname = parent_details_accessor.getString("parent_fname_from_admin");
        parent_lname = parent_details_accessor.getString("parent_lname_from_admin");
        parent_address = parent_details_accessor.getString("parent_address_from_admin");
        parent_phone = parent_details_accessor.getString("parent_phone_from_admin");
        parent_email = parent_details_accessor.getString("parent_email_from_admin");
        parent_code = parent_details_accessor.getString("parent_code_from_admin");
}
}
