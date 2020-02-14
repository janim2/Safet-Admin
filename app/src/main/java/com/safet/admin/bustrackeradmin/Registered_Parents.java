package com.safet.admin.bustrackeradmin;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.safet.admin.bustrackeradmin.Adapters.ParentsAdapter;
import com.safet.admin.bustrackeradmin.Model.Parents;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Registered_Parents extends AppCompatActivity {

    private ArrayList parentsArray = new ArrayList<Parents>();
    private RecyclerView parents_RecyclerView;
    private RecyclerView.Adapter parents_Adapter;

    private Accessories registered_accessor;
    private TextView no_parents;
    private ImageView add_parent;

    private String school_id,sfirst_name,slastname,saddress,sphone_number,semail,sparent_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered__parents);

        getSupportActionBar().setTitle("Registered Parents");

        registered_accessor = new Accessories(Registered_Parents.this);

        school_id = registered_accessor.getString("school_code");

        parents_RecyclerView = findViewById(R.id.parents_recyclerView);
        no_parents = findViewById(R.id.no_parents);
        add_parent = findViewById(R.id.add_parent);

        if(isNetworkAvailable()){
            get_Registered_Parents();
        }else{
            Toast.makeText(Registered_Parents.this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
        parents_RecyclerView.setHasFixedSize(true);
        parents_RecyclerView.addItemDecoration(new DividerItemDecoration(Registered_Parents.this,
                DividerItemDecoration.VERTICAL));

        parents_Adapter = new ParentsAdapter(getFromDatabase(),Registered_Parents.this);
        parents_RecyclerView.setAdapter(parents_Adapter);

        //setting an onclick for the add driver button
        add_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registered_Parents.this, Add_Parent.class));
            }
        });
    }

    private void get_Registered_Parents() {
        try{
            DatabaseReference get_Parents_notifications = FirebaseDatabase.getInstance().getReference("parents")
                    .child(school_id);

            get_Parents_notifications.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            Fetch_Parent_Info(child.getKey());
                        }
                    }else{
//                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Registered_Parents.this,"Cancelled",Toast.LENGTH_LONG).show();
                }
            });
        }catch (NullPointerException e){

        }
    }

    private void Fetch_Parent_Info(final String key) {
        DatabaseReference getdriver_info = FirebaseDatabase.getInstance().getReference("parents").child(school_id).child(key);
        getdriver_info.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("firstname")){
                            sfirst_name = child.getValue().toString();
                        }
                        if(child.getKey().equals("lastname")){
                            slastname = child.getValue().toString();
                        }
                        if(child.getKey().equals("location")){
                            saddress = child.getValue().toString();
                        }
                        if(child.getKey().equals("email")){
                            semail = child.getValue().toString();
                        }
                        if(child.getKey().equals("phone_number")){
                            sphone_number = child.getValue().toString();
                        }

                        sparent_code = key;
                    }
                    Parents obj = new Parents(sfirst_name,slastname,saddress,sphone_number,semail,sparent_code);
                    parentsArray.add(obj);
                    parents_RecyclerView.setAdapter(parents_Adapter);
                    parents_Adapter.notifyDataSetChanged();
                    no_parents.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public ArrayList<Parents> getFromDatabase() {
        return parentsArray;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
