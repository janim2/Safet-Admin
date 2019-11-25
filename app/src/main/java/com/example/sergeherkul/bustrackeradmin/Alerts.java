package com.example.sergeherkul.bustrackeradmin;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sergeherkul.bustrackeradmin.Adapters.NotifyAdapter;
import com.example.sergeherkul.bustrackeradmin.Model.Notify;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Alerts extends AppCompatActivity {
    private String school_code, alert_id, alert_title, alert_message,
            alert_time, alert_image;
    private DatabaseReference get_alerts_reference;
    private Accessories alertsAccessor;
    private FirebaseAuth mauth;
    private RecyclerView alerts_recyclerview;
    private TextView no_alerts, no_internet;
    private ArrayList alertsArray = new ArrayList<Notify>();
    private RecyclerView.Adapter alerts_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        getSupportActionBar().setTitle("Alerts");

        alertsAccessor = new Accessories(Alerts.this);

        mauth = FirebaseAuth.getInstance();

        school_code = alertsAccessor.getString("school_code");
//        driver_code = notifications_accessor.getString("driver_code");

        alerts_recyclerview = findViewById(R.id.alerts_recyclerView);
        no_alerts = findViewById(R.id.no_alerts);
        no_internet = findViewById(R.id.no_internet);

        //reviews adapter settings starts here
        if(isNetworkAvailable()){
                getAlerts_IDs();
        }else {
            no_alerts.setVisibility(View.GONE);
            no_internet.setVisibility(View.VISIBLE);
        }

        alerts_recyclerview.setHasFixedSize(true);
        alerts_Adapter = new NotifyAdapter(getAlertsFromDatabase(),Alerts.this);
        alerts_recyclerview.setAdapter(alerts_Adapter);

        no_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    getAlerts_IDs();
                }else{
                    no_alerts.setVisibility(View.GONE);
                    no_internet.setVisibility(View.VISIBLE);                }
            }
        });
    }

    private void getAlerts_IDs() {
        try{
            get_alerts_reference = FirebaseDatabase.getInstance().getReference("pending_alerts").child(school_code);
            get_alerts_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            Fetch_Alerts(child.getKey());
                            Toast.makeText(Alerts.this, child.getKey(),Toast.LENGTH_LONG).show();
                        }
                    }else{
//                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Alerts.this,"Cancelled",Toast.LENGTH_LONG).show();
                }
            });
        }catch (NullPointerException e){

        }
    }

    private void Fetch_Alerts(final String key) {
//        getDriver_details
        try{
            DatabaseReference getAlerts = FirebaseDatabase.getInstance().getReference("pending_alerts")
                    .child(school_code).child(key);
            getAlerts.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            if(child.getKey().equals("title")){
                                alert_title = child.getValue().toString();
                            }

                            if(child.getKey().equals("message")){
                                alert_message = child.getValue().toString();
                            }

                            if(child.getKey().equals("time")){
                                alert_time = child.getValue().toString();
                            }

                            if(child.getKey().equals("image")){
                                alert_image = child.getValue().toString();
                            }

                            else{
//                            Toast.makeText(getActivity(),"Couldn't fetch posts",Toast.LENGTH_LONG).show();

                            }
                        }
                        alert_id = key;
                        Notify obj = new Notify(alert_id,alert_title,alert_message,alert_time,alert_image);
                        alertsArray.add(obj);
                        alerts_recyclerview.setAdapter(alerts_Adapter);
                        alerts_Adapter.notifyDataSetChanged();
                        no_internet.setVisibility(View.GONE);
                        no_alerts.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Alerts.this,"Cancelled",Toast.LENGTH_LONG).show();

                }
            });
        }catch (NullPointerException w){

        }

    }

    public ArrayList<Notify> getAlertsFromDatabase(){
        return  alertsArray;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
