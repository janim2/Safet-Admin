package com.example.sergeherkul.bustrackeradmin;

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

import com.example.sergeherkul.bustrackeradmin.Adapters.DriversAdapter;
import com.example.sergeherkul.bustrackeradmin.Adapters.NotifyAdapter;
import com.example.sergeherkul.bustrackeradmin.Model.Drivers;
import com.example.sergeherkul.bustrackeradmin.Model.Notify;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Registered_Drivers extends AppCompatActivity {

    private ArrayList driverArray = new ArrayList<Drivers>();
    private RecyclerView drivers_RecyclerView;
    private RecyclerView.Adapter drivers_Adapter;
    private String sdriver_code,usertype, school_id, sfirst_name, slastname, saddress, sphone_number,
    sbrand, sbus_code, schasis_no, smodel, snumber_plate;

    private Accessories registered_accessor;
    private TextView no_drivers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered__drivers);
        getSupportActionBar().setTitle("Drivers");

        registered_accessor = new Accessories(Registered_Drivers.this);

        usertype = registered_accessor.getString("user_type");
        school_id = registered_accessor.getString("school_code");

        drivers_RecyclerView = findViewById(R.id.drivers_recyclerView);
        no_drivers = findViewById(R.id.no_drivers);

        if(isNetworkAvailable()){
            get_Registered_Drivers();
        }else{
            Toast.makeText(Registered_Drivers.this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
        drivers_RecyclerView.setHasFixedSize(true);

        drivers_Adapter = new DriversAdapter(getFromDatabase(),Registered_Drivers.this);
        drivers_RecyclerView.setAdapter(drivers_Adapter);


    }

    private void get_Registered_Drivers() {
        try{
            DatabaseReference get_Driver_notifications = FirebaseDatabase.getInstance().getReference("drivers").child(school_id);

            get_Driver_notifications.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            Fetch_Driver_Info(child.getKey());
                        }
                    }else{
//                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Registered_Drivers.this,"Cancelled",Toast.LENGTH_LONG).show();
                }
            });
        }catch (NullPointerException e){

        }
    }

    private void Fetch_Driver_Info(final String key) {
        DatabaseReference getdriver_info = FirebaseDatabase.getInstance().getReference("drivers").child(school_id).child(key);
        getdriver_info.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("first_name")){
                            sfirst_name = child.getValue().toString();
                        }
                        if(child.getKey().equals("last_name")){
                            slastname = child.getValue().toString();
                        }
                        if(child.getKey().equals("address")){
                            saddress = child.getValue().toString();
                        }
                        if(child.getKey().equals("phone_number")){
                            sphone_number = child.getValue().toString();
                        }

                        sdriver_code = key;
                    }
                    Drivers obj = new Drivers(sfirst_name,slastname,saddress,sphone_number,sdriver_code);
                    driverArray.add(obj);
                    drivers_RecyclerView.setAdapter(drivers_Adapter);
                    drivers_Adapter.notifyDataSetChanged();
                    no_drivers.setVisibility(View.GONE);
                    getBusDetails(school_id,sdriver_code);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getBusDetails(String schoolcode, String drivercode) {
        DatabaseReference school_details = FirebaseDatabase.getInstance().getReference("bus_details").child(schoolcode).child(drivercode);
        school_details.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("brand")){
                            sbrand = child.getValue().toString();
                            registered_accessor.put("bus_brand",sbrand);
                        }
                        if(child.getKey().equals("bus_code")){
                            sbus_code = child.getValue().toString();
                            registered_accessor.put("bus_code",sbus_code);
                        }
                        if(child.getKey().equals("chasis_no")){
                            schasis_no = child.getValue().toString();
                            registered_accessor.put("bus_chasis_no",schasis_no);
                        }
                        if(child.getKey().equals("model")){
                            smodel = child.getValue().toString();
                            registered_accessor.put("bus_model",smodel);
                        }
                        if(child.getKey().equals("number_plate")){
                            snumber_plate = child.getValue().toString();
                            registered_accessor.put("bus_number_plate",snumber_plate);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public ArrayList<Drivers> getFromDatabase(){
        return  driverArray;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
