package com.example.sergeherkul.bustrackeradmin;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Phone_number_Verification extends AppCompatActivity {

    private TextView logo_name, enter_message;
    private LinearLayout number_layout;
    private ImageView goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number__verification);

        logo_name = findViewById(R.id.logo_name);
        enter_message = findViewById(R.id.enter_message);
        number_layout = findViewById(R.id.number_layout);
        goBack = findViewById(R.id.go_back);

        Typeface lovelo =Typeface.createFromAsset(getAssets(),  "fonts/lovelo.ttf");

        logo_name.setTypeface(lovelo);
        enter_message.setTypeface(lovelo);

        number_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Phone_number_Verification.this,Enter_phone_number.class));
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
