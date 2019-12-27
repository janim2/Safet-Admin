package com.example.sergeherkul.bustrackeradmin;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sergeherkul.bustrackeradmin.Model.Msg_Recepients;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Circulate_Message extends AppCompatActivity {
    private RadioButton parent_radio, driver_radio, both_radio;
    private Button send_message_button;
    private EditText subject_editText, message_editText, location_editText;
    private TextView select_date, select_time, status_message, no_internet;
    private Accessories circulate_accessor;
    private ProgressBar loading;

    private String time_string, date_string, subject_string, message_string,school_id, location_string;
    private ArrayList<String> recepients_Array = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circulate__message);

        getSupportActionBar().setTitle("Circulate message");

        circulate_accessor =  new Accessories(Circulate_Message.this);
        school_id = circulate_accessor.getString("school_code");

        parent_radio = findViewById(R.id.parent_radio_button);
        driver_radio = findViewById(R.id.driver_radio_button);
        both_radio = findViewById(R.id.both_radio_button);
        send_message_button = findViewById(R.id.circulate_button);
        subject_editText = findViewById(R.id.subject_edittext);
        message_editText = findViewById(R.id.message_edittext);
        location_editText = findViewById(R.id.location_edittext);
        select_date = findViewById(R.id.select_date);
        select_time = findViewById(R.id.select_time);
        loading = findViewById(R.id.loading);
        no_internet = findViewById(R.id.no_internet);
        status_message = findViewById(R.id.status_message);

        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView[] date_view = {select_date};
                circulate_accessor.showDatePicker(null,date_view);
            }
        });

        select_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView[] time_view = {select_time};
                circulate_accessor.showTimePicker(null,time_view);
            }
        });


        send_message_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                subject_string = subject_editText.getText().toString().trim();
                message_string = message_editText.getText().toString().trim();
                date_string = select_date.getText().toString().trim();
                time_string = select_time.getText().toString().trim();
                location_string = location_editText.getText().toString().trim();
                if (!subject_string.equals("")){
                    if (!message_string.equals("")){
                        if(isNetworkAvailable()){
                            if(parent_radio.isChecked()){
                                SendMessage_to_Parents(subject_string, message_string, location_string, date_string, time_string);
                            }

                            else if(driver_radio.isChecked()){
                                SendMessage_to_Drivers(subject_string, message_string, location_string, date_string, time_string);
                            }

                            else if(both_radio.isChecked()){
                                loading.setVisibility(View.GONE);
                                status_message.setText("Upgrade to use service");
                                Toast.makeText(Circulate_Message.this, "Upgrade to use this feature", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            loading.setVisibility(View.GONE);
                            no_internet.setVisibility(View.VISIBLE);
                            status_message.setVisibility(View.GONE);
                        }
                    }else{
                        loading.setVisibility(View.GONE);
                        no_internet.setVisibility(View.GONE);
                        status_message.setText("Message field required");
                        status_message.setVisibility(View.VISIBLE);
                    }
                }else{
                    loading.setVisibility(View.GONE);
                    no_internet.setVisibility(View.GONE);
                    status_message.setText("Subject field required");
                    status_message.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    private void SendMessage_to_Parents(final String subject_string, final String message_string, final String location_string, final String date_string, final String time_string) {
    //fetch parents ids first
        try{
            DatabaseReference get_Parents_id = FirebaseDatabase.getInstance().getReference("parents").child(school_id);

            get_Parents_id.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            Send_Message_To_Each_Parent(child.getKey(),subject_string, message_string, location_string,date_string, time_string);
                        }
                    }else{
//                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Circulate_Message.this,"Cancelled",Toast.LENGTH_LONG).show();
                }
            });
        }catch (NullPointerException e){

        }
    }

    private void Send_Message_To_Each_Parent(String key, String subject_string, String message_string,
                                             String location_string,String date_string, String time_string) {
//        Msg_Recepients obj = new Msg_Recepients(key);
//        recepients_Array.add(key);
        try{
//            for(int count=0;count < recepients_Array.size();count++){
                DatabaseReference tmp_messages_ref = FirebaseDatabase.getInstance().getReference("temp_messages").child(school_id).child(key).push();//child(recepients_Array.get(count)).push();
                tmp_messages_ref.child("subject").setValue(subject_string);
                tmp_messages_ref.child("message").setValue(message_string);
                tmp_messages_ref.child("location").setValue(location_string);
                tmp_messages_ref.child("date").setValue(date_string);
                tmp_messages_ref.child("time").setValue(time_string).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loading.setVisibility(View.GONE);
                        no_internet.setVisibility(View.GONE);
                        status_message.setText("Message Circulation Complete");
                        status_message.setVisibility(View.VISIBLE);
                    }
                });
//            }
        }catch (NullPointerException e){

        }
    }

    private void SendMessage_to_Drivers(final String subject_string, final String message_string, final String location_string, final String date_string, final String time_string) {
        //fetch drivers ids first
        try{
            DatabaseReference get_Parents_id = FirebaseDatabase.getInstance().getReference("drivers").child(school_id);

            get_Parents_id.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            Send_Message_To_Each_Driver(child.getKey(),subject_string, message_string, location_string,date_string, time_string);
                        }
                    }else{
//                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Circulate_Message.this,"Cancelled",Toast.LENGTH_LONG).show();
                }
            });
        }catch (NullPointerException e){

        }
    }

    private void Send_Message_To_Each_Driver(String key, String subject_string, String message_string, String location_string, String date_string, String time_string) {
        try{
//            for(int count=0;count < recepients_Array.size();count++){
            DatabaseReference tmp_messages_ref = FirebaseDatabase.getInstance().getReference("temp_messages").child(school_id).child(key).push();//child(recepients_Array.get(count)).push();
            tmp_messages_ref.child("subject").setValue(subject_string);
            tmp_messages_ref.child("message").setValue(message_string);
            tmp_messages_ref.child("location").setValue(location_string);
            tmp_messages_ref.child("date").setValue(date_string);
            tmp_messages_ref.child("time").setValue(time_string).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    loading.setVisibility(View.GONE);
                    no_internet.setVisibility(View.GONE);
                    status_message.setText("Message Circulation Complete");
                    status_message.setVisibility(View.VISIBLE);
                }
            });
//            }
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
