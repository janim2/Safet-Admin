package com.example.sergeherkul.bustrackeradmin;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Random;

public class Alerts extends AppCompatActivity {
    private String school_code, alert_id, alert_title, alert_message,
            alert_time, alert_image;
    private DatabaseReference add_todatabaseReference, remove_fromReference;
    private Accessories alertsAccessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        getSupportActionBar().setTitle("Alerts");

        alertsAccessor = new Accessories(Alerts.this);

        //intents
        alert_id = getIntent().getStringExtra("alert_id");
        alert_title = getIntent().getStringExtra("alert_title");
        alert_message = getIntent().getStringExtra("alert_message");
        alert_time = getIntent().getStringExtra("alert_time");
        alert_image = getIntent().getStringExtra("alert_image");
        school_code = alertsAccessor.getString("school_code");

        String clicked_check = getIntent().getStringExtra("alertID");
        Toast.makeText(Alerts.this, "bb"+clicked_check, Toast.LENGTH_LONG).show();

        if(clicked_check != null){
            //removing notification from alerts node to pending alerts node
            if (isNetworkAvailable()){
                MoveFrom_main_to_pending(alert_id,alert_title,alert_message,alert_image,alert_time);
            }else{
                Toast.makeText(Alerts.this, "No internet connection", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(Alerts.this, "null", Toast.LENGTH_LONG).show();
        }

    }

    private void MoveFrom_main_to_pending(final String alert_id, String alert_title, String alert_message,
                                          String alert_image, String alert_time) {
        try {
            add_todatabaseReference = FirebaseDatabase.getInstance().getReference("pending_alerts").child(school_code).child(alert_id);
            add_todatabaseReference.child("image").setValue(alert_image);
            add_todatabaseReference.child("message").setValue(alert_message);
            add_todatabaseReference.child("title").setValue(alert_title);
            add_todatabaseReference.child("time").setValue(alert_time).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Remove_alerts_from_alerts(alert_id);
                }
            });
        }catch (NullPointerException e){

        }
    }

    private void Remove_alerts_from_alerts(String alert_id) {
        try {
            remove_fromReference = FirebaseDatabase.getInstance().getReference("alerts").child(school_code).child(alert_id);
            remove_fromReference.removeValue();
        }catch (NullPointerException e){

        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
