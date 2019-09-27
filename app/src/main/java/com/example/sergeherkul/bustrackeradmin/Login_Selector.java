package com.example.sergeherkul.bustrackeradmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class Login_Selector extends AppCompatActivity {

    private TextView user_selection_text;
    private RadioButton driver_radio_button, school_admin_radio_button;
    private Button next_button;
    private String isSelected;
    Accessories loginselector_accessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__selector);

        loginselector_accessor = new Accessories(Login_Selector.this);

        user_selection_text = findViewById(R.id.user_selection_text);
        driver_radio_button = findViewById(R.id.driver_radio_button);
        school_admin_radio_button = findViewById(R.id.school_admin_radio_button);

        next_button = findViewById(R.id.next_button);

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(driver_radio_button.isChecked() || school_admin_radio_button.isChecked()){
                    if(driver_radio_button.isChecked()){
                        isSelected = "Driver";
                        loginselector_accessor.put("user_type", isSelected);
                        startActivity(new Intent(Login_Selector.this, Phone_number_Verification.class));
                    }else{
                        isSelected = "Admin";
                        loginselector_accessor.put("user_type", isSelected);
                        startActivity(new Intent(Login_Selector.this, Admin_Login.class));
                    }
                }else{
                    Toast.makeText(Login_Selector.this,"User selection Required",Toast.LENGTH_LONG).show();

                }
            }
        });

    }
}
