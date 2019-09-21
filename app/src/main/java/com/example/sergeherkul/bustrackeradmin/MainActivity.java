package com.example.sergeherkul.bustrackeradmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mauth;
    Accessories mainaccessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mauth = FirebaseAuth.getInstance();
        mainaccessor = new Accessories(MainActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mauth.getCurrentUser() != null){
            if(mainaccessor.getBoolean("isverified")){

            }else{
                Intent verify_school = new Intent(MainActivity.this, Verify_School.class);
                verify_school.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(verify_school);
            }
        }else{
            Intent gotoLogin = new Intent(MainActivity.this, Login.class);
            gotoLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(gotoLogin);
        }
    }
}
