package com.example.sergeherkul.bustrackeradmin;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sergeherkul.bustrackeradmin.Adapters.NotifyAdapter;
import com.example.sergeherkul.bustrackeradmin.Model.Notify;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Notifications extends AppCompatActivity {

    private ArrayList notificationsArray = new ArrayList<Notify>();
    private RecyclerView notifications_RecyclerView;
    private RecyclerView.Adapter notifications_Adapter;
    private String usertype,school_id, driver_code, notification_title, notifications_message, notifications_time,
            notificationImage;
    private FirebaseAuth mauth;
    private TextView no_notifications, no_internet;

    private Accessories notifications_accessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        getSupportActionBar().setTitle("Notifications");

        notifications_accessor = new Accessories(Notifications.this);

        mauth = FirebaseAuth.getInstance();

        usertype = notifications_accessor.getString("user_type");
        school_id = notifications_accessor.getString("school_code");
        driver_code = notifications_accessor.getString("driver_code");

        notifications_RecyclerView = findViewById(R.id.notifications_recyclerView);
        no_notifications = findViewById(R.id.no_notifications);
        no_internet = findViewById(R.id.no_internet);

        //reviews adapter settings starts here
        if(isNetworkAvailable()){
            if(usertype.equals("Driver")){
                getDriverNotifications_ID();
            }else{
                getAdminNotifications_ID();
            }
        }else{
            no_internet.setVisibility(View.VISIBLE);
            no_notifications.setVisibility(View.GONE);
        }
        notifications_RecyclerView.setHasFixedSize(true);
        notifications_Adapter = new NotifyAdapter(getNotificationsFromDatabase(),Notifications.this);
        notifications_RecyclerView.setAdapter(notifications_Adapter);

        no_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    if(usertype.equals("Driver")){
                        getDriverNotifications_ID();
                    }else{
                        getAdminNotifications_ID();
                    }
                }else{
                    no_internet.setVisibility(View.VISIBLE);
                    no_notifications.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getDriverNotifications_ID() {
        try{
            DatabaseReference get_Driver_notifications = FirebaseDatabase.getInstance().getReference("notifications").child(school_id).child(driver_code);

            get_Driver_notifications.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            Fetch_Driver_Notifications(child.getKey());
                        }
                    }else{
//                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Notifications.this,"Cancelled",Toast.LENGTH_LONG).show();
                }
            });
        }catch (NullPointerException e){

        }
    }

    private void getAdminNotifications_ID() {
        try{
            DatabaseReference get_admin_notifications = FirebaseDatabase.getInstance().getReference("school_notifications").child(school_id);//.child(mauth.getCurrentUser().getUid());
            get_admin_notifications.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            Fetch_Admin_Notifications(child.getKey());
                        }
                    }else{
//                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Notifications.this,"Cancelled",Toast.LENGTH_LONG).show();
                }
            });
        }catch (NullPointerException e){

        }
    }

    private void Fetch_Driver_Notifications(String key) {
        DatabaseReference getNotifications = FirebaseDatabase.getInstance().getReference("notifications").child(school_id).child(driver_code).child(key);
        getNotifications.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("title")){
                            notification_title = child.getValue().toString();
                        }

                        if(child.getKey().equals("message")){
                            notifications_message = child.getValue().toString();
                        }

                        if(child.getKey().equals("time")){
                            notifications_time = child.getValue().toString();
                        }

                        if(child.getKey().equals("image")){
                            notificationImage = child.getValue().toString();
                        }

                        else{
//                            Toast.makeText(getActivity(),"Couldn't fetch posts",Toast.LENGTH_LONG).show();

                        }
                    }

                    Notify obj = new Notify(notification_title,notifications_message,notifications_time,notificationImage);
                    notificationsArray.add(obj);
                    notifications_RecyclerView.setAdapter(notifications_Adapter);
                    notifications_Adapter.notifyDataSetChanged();
                    no_notifications.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Notifications.this,"Cancelled",Toast.LENGTH_LONG).show();

            }
        });
    }

    private void Fetch_Admin_Notifications(String key) {
        DatabaseReference get_admin_Notifications = FirebaseDatabase.getInstance().getReference("school_notifications").child(school_id).child(key);
        get_admin_Notifications.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("title")){
                            notification_title = child.getValue().toString();
                        }

                        if(child.getKey().equals("message")){
                            notifications_message = child.getValue().toString();
                        }

                        if(child.getKey().equals("time")){
                            notifications_time = child.getValue().toString();
                        }

                        if(child.getKey().equals("image")){
                            notificationImage = child.getValue().toString();
                        }

                        else{
//                            Toast.makeText(getActivity(),"Couldn't fetch posts",Toast.LENGTH_LONG).show();

                        }
                    }

                    Notify obj = new Notify(notification_title,notifications_message,notifications_time,notificationImage);
                    notificationsArray.add(obj);
                    notifications_RecyclerView.setAdapter(notifications_Adapter);
                    notifications_Adapter.notifyDataSetChanged();
                    no_notifications.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Notifications.this,"Cancelled",Toast.LENGTH_LONG).show();

            }
        });

    }

    public ArrayList<Notify> getNotificationsFromDatabase(){
        return  notificationsArray;
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
