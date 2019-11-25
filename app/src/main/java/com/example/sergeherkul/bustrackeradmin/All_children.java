package com.example.sergeherkul.bustrackeradmin;

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

import com.example.sergeherkul.bustrackeradmin.Adapters.ChildrenAdapter;
import com.example.sergeherkul.bustrackeradmin.Adapters.DriversAdapter;
import com.example.sergeherkul.bustrackeradmin.Model.Children;
import com.example.sergeherkul.bustrackeradmin.Model.Drivers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class All_children extends AppCompatActivity {
    private ArrayList child_Array = new ArrayList<Children>();
    private RecyclerView child_RecyclerView;
    private RecyclerView.Adapter child_Adapter;
    private String schild_code, parent_code, school_id,
            child_first_name, child_last_name, child_class, child_gender;

    private Accessories child_accessor;
    private TextView no_children, no_internet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_children);
        getSupportActionBar().setTitle("Drivers");

        getSupportActionBar().setTitle("Children");
        child_accessor = new Accessories(All_children.this);

        school_id = child_accessor.getString("school_code");

        child_RecyclerView = findViewById(R.id.children_recyclerView);
        no_children = findViewById(R.id.no_children);
        no_internet = findViewById(R.id.no_internet);

        if(isNetworkAvailable()){
            get_Parent_IDs();
        }else{
            no_children.setVisibility(View.GONE);
            no_internet.setVisibility(View.VISIBLE);
        }

        //onclick for the no internet textView
        no_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    get_Parent_IDs();
                }else{
                    no_children.setVisibility(View.GONE);
                    no_internet.setVisibility(View.VISIBLE);
                }
            }
        });

        // recycler view initializations starts here
        child_RecyclerView.setHasFixedSize(true);
        child_RecyclerView.addItemDecoration(new DividerItemDecoration(All_children.this,
                DividerItemDecoration.VERTICAL));

        child_Adapter = new ChildrenAdapter(getFromDatabase(),All_children.this);
        child_RecyclerView.setAdapter(child_Adapter);
    }

    private void get_Parent_IDs() {
        try{
            DatabaseReference get_parent_id = FirebaseDatabase.getInstance().getReference("children")
                    .child(school_id);//.child(pone_no).child(child_one_id);
            get_parent_id.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            Fetch_Child_IDs(child.getKey());
                        }
                    }else{
//                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(All_children.this,"Cancelled",Toast.LENGTH_LONG).show();
                }
            });
        }catch (NullPointerException e){

        }
    }

    private void Fetch_Child_IDs(final String key) {
        parent_code = key;
        try{
            DatabaseReference get_child_id = FirebaseDatabase.getInstance().getReference("children")
                    .child(school_id).child(key);//.child(child_one_id);
            get_child_id.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            Fetch_Child_Info(child.getKey());
                        }
                    }else{
//                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(All_children.this,"Cancelled",Toast.LENGTH_LONG).show();
                }
            });
        }catch (NullPointerException e){

        }
    }

    private void Fetch_Child_Info(final String key) {
        DatabaseReference get_child_id = FirebaseDatabase.getInstance().getReference("children")
                .child(school_id).child(parent_code).child(key);
        get_child_id.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("firstname")){
                            child_first_name = child.getValue().toString();
                        }
                        if(child.getKey().equals("lastname")){
                            child_last_name = child.getValue().toString();
                        }
                        if(child.getKey().equals("class")){
                            child_class = child.getValue().toString();
                        }
                        if(child.getKey().equals("gender")){
                            child_gender = child.getValue().toString();
                        }

                        schild_code = key;
                    }
                    Children obj = new Children(child_first_name,child_last_name,child_class,
                            schild_code,child_gender);
                    child_Array.add(obj);
                    child_RecyclerView.setAdapter(child_Adapter);
                    child_Adapter.notifyDataSetChanged();
                    no_children.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public ArrayList<Children> getFromDatabase(){
        return  child_Array;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
