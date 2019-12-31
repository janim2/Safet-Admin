package com.safet.admin.bustrackeradmin;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.safet.admin.bustrackeradmin.Adapters.AlertsAdapter;
import com.safet.admin.bustrackeradmin.Model.Notify;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Alerts extends AppCompatActivity {
    private String school_code, distress_id, seurity_id, distress_title, security_title, distress_message,
            security_message, distress_time, securiy_time, distress_image, security_image;
    private DatabaseReference get_distress_reference,get_security_reference;
    private Accessories alertsAccessor;
    private FirebaseAuth mauth;
    private RecyclerView distress_recyclerview, security_recyclerView;
    private TextView no_distress_, no_distress_internet, no_security, no_security_internet;
    private ArrayList distressArray = new ArrayList<Notify>();
    private ArrayList securityArray = new ArrayList<Notify>();
    private RecyclerView.Adapter distress_Adapter, security_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        getSupportActionBar().setTitle("Alerts");

        alertsAccessor = new Accessories(Alerts.this);

        mauth = FirebaseAuth.getInstance();

        school_code = alertsAccessor.getString("school_code");
//        driver_code = notifications_accessor.getString("driver_code");

        distress_recyclerview = findViewById(R.id.distress_recyclerView);
        security_recyclerView = findViewById(R.id.security_recyclerView);
        no_distress_ = findViewById(R.id.no_distress);
        no_distress_internet = findViewById(R.id.no_distress_internet);

        no_security = findViewById(R.id.no_security);
        no_security_internet = findViewById(R.id.no_security_internet);

        //reviews adapter settings starts here
        if(isNetworkAvailable()){
                getDistress_IDs();
                getSecurity_IDs();
        }else {
            no_distress_.setVisibility(View.GONE);
            no_distress_internet.setVisibility(View.VISIBLE);

            no_security.setVisibility(View.GONE);
            no_security_internet.setVisibility(View.VISIBLE);
        }

        //for distress
        distress_recyclerview.setHasFixedSize(true);
        distress_Adapter = new AlertsAdapter(getDistressFromDatabase(),Alerts.this);
        distress_recyclerview.setAdapter(distress_Adapter);

        //for security
        security_recyclerView.setHasFixedSize(true);
        security_Adapter = new AlertsAdapter(getSecurityFromDatabase(),Alerts.this);
        security_recyclerView.setAdapter(security_Adapter);

        no_distress_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    getDistress_IDs();
                    getSecurity_IDs();
                }else{
                    no_distress_.setVisibility(View.GONE);
                    no_distress_internet.setVisibility(View.VISIBLE);

                    no_security.setVisibility(View.GONE);
                    no_security_internet.setVisibility(View.VISIBLE);
                }
            }
        });

        no_security_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    getDistress_IDs();
                    getSecurity_IDs();
                }else{
                    no_distress_.setVisibility(View.GONE);
                    no_distress_internet.setVisibility(View.VISIBLE);

                    no_security.setVisibility(View.GONE);
                    no_security_internet.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getDistress_IDs() {
        try{
            get_distress_reference = FirebaseDatabase.getInstance().getReference("pending_distress").child(school_code);
            get_distress_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            Fetch_Distresses(child.getKey());
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

    private void Fetch_Distresses(final String key) {
//        getDriver_details
        try{
            DatabaseReference getAlerts = FirebaseDatabase.getInstance().getReference("pending_distress")
                    .child(school_code).child(key);
            getAlerts.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            if(child.getKey().equals("title")){
                                distress_title = child.getValue().toString();
                            }

                            if(child.getKey().equals("message")){
                                distress_message = child.getValue().toString();
                            }

                            if(child.getKey().equals("time")){
                                distress_time = child.getValue().toString();
                            }

                            if(child.getKey().equals("image")){
                                distress_image = child.getValue().toString();
                            }

                            else{
//                            Toast.makeText(getActivity(),"Couldn't fetch posts",Toast.LENGTH_LONG).show();

                            }
                        }
                        distress_id = key;
                        Notify obj = new Notify(distress_id,distress_title,distress_message,
                                distress_time,distress_image);
                        distressArray.add(obj);
                        distress_recyclerview.setAdapter(distress_Adapter);
                        distress_Adapter.notifyDataSetChanged();
                        no_distress_.setVisibility(View.GONE);
                        no_distress_internet.setVisibility(View.GONE);
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

    // security alerts starts here
    private void getSecurity_IDs() {
        try{
            get_security_reference = FirebaseDatabase.getInstance().getReference("pending_security").child(school_code);
            get_security_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            Fetch_Securities(child.getKey());
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

    private void Fetch_Securities(final String key) {
//        getDriver_details
        try{
            DatabaseReference getsecurity_Alerts = FirebaseDatabase.getInstance().getReference("pending_security")
                    .child(school_code).child(key);
            getsecurity_Alerts.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            if(child.getKey().equals("title")){
                                security_title = child.getValue().toString();
                            }

                            if(child.getKey().equals("message")){
                                security_message = child.getValue().toString();
                            }

                            if(child.getKey().equals("time")){
                                securiy_time = child.getValue().toString();
                            }

                            if(child.getKey().equals("image")){
                                security_image = child.getValue().toString();
                            }

                            else{
//                            Toast.makeText(getActivity(),"Couldn't fetch posts",Toast.LENGTH_LONG).show();

                            }
                        }
                        seurity_id = key;
                        Notify obj = new Notify(seurity_id,security_title,security_message,
                                securiy_time,security_image);
                        securityArray.add(obj);
                        security_recyclerView.setAdapter(security_Adapter);
                        security_Adapter.notifyDataSetChanged();
                        no_security.setVisibility(View.GONE);
                        no_security_internet.setVisibility(View.GONE);
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

    public ArrayList<Notify> getDistressFromDatabase(){
        return distressArray;
    }

    public ArrayList<Notify> getSecurityFromDatabase(){
        return securityArray;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
