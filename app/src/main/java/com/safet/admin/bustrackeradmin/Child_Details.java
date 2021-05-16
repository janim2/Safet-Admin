package com.safet.admin.bustrackeradmin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Random;

public class Child_Details extends AppCompatActivity {

    private TextView child_fname_textview, child_lname_textview, child_gender_textview, child_class_textview;
    private EditText child_fname_edittext, child_lname_edittext, child_class_edittext;
    private Spinner child_gender_spinner;

    private String parent_code_string,child_code_string, child_fname_string, child_lname_string, child_gender_string,
            child_class_string, school_code;
    private ImageView edit_image, delete_child;
    private Button edit_button;

    private Accessories childdetails_accessor;
    private String[] gender = {"Male", "Female"};
    private ArrayAdapter<String> gender_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child__details);
        //accessories
        childdetails_accessor = new Accessories(Child_Details.this);
        school_code = childdetails_accessor.getString("school_code");

        //retrieve the values from add child activity
        parent_code_string = childdetails_accessor.getString("parentcode_adapter");
        child_fname_string = childdetails_accessor.getString("childfname_adapter");
        child_lname_string = childdetails_accessor.getString("childlname_adapter");
        child_class_string = childdetails_accessor.getString("childclass_adapter");
        child_gender_string = childdetails_accessor.getString("childgender_adapter");
        child_code_string = childdetails_accessor.getString("childcode_adapter");

        getSupportActionBar().setTitle(child_fname_string + " | Child");

        //textviews
        child_fname_textview = findViewById(R.id.child_fname_textview);
        child_lname_textview = findViewById(R.id.child_lname_textview);
        child_gender_textview = findViewById(R.id.child_gender_textview);
        child_class_textview = findViewById(R.id.the_child_class_textview);

        //edittexts
        child_fname_edittext = findViewById(R.id.the_child_fname_edittext);
        child_lname_edittext = findViewById(R.id.the_childlname_editText);
        child_class_edittext = findViewById(R.id.the_class_editText);

        //spinner
        child_gender_spinner = findViewById(R.id.gender_spinner);

        //ImageView
        edit_image = findViewById(R.id.edit_image);
        delete_child = findViewById(R.id.delete_child);

        //button
        edit_button = findViewById(R.id.edit_button);

        //set text into tetviews
        child_fname_textview.setText(child_fname_string);
        child_lname_textview.setText(child_lname_string);
        child_gender_textview.setText(child_gender_string);
        child_class_textview.setText(child_class_string);

        //setting the adapter for the child one spinner
        child_gender_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                child_gender_string = gender[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                child_gender_string = "Male";
            }
        });
        gender_type = new ArrayAdapter<String>(Child_Details.this,android.R.layout.simple_list_item_1,gender);
        child_gender_spinner.setAdapter(gender_type);

        edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make text views invisible
                child_fname_textview.setVisibility(View.GONE);
                child_lname_textview.setVisibility(View.GONE);
                child_gender_textview.setVisibility(View.GONE);
                child_class_textview.setVisibility(View.GONE);

                //make edit texts visible
                child_fname_edittext.setVisibility(View.VISIBLE);
                child_lname_edittext.setVisibility(View.VISIBLE);
                child_gender_spinner.setVisibility(View.VISIBLE);
                child_class_edittext.setVisibility(View.VISIBLE);

                //set texts into text view
                child_fname_edittext.setText(child_fname_string);
                child_lname_edittext.setText(child_lname_string);
                child_class_edittext.setText(child_class_string);
                child_gender_textview.setText(child_gender_string);

                //make the edit_button visible
                edit_button.setVisibility(View.VISIBLE);
                edit_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        child_fname_string = child_fname_edittext.getText().toString().trim();
                        child_lname_string = child_lname_edittext.getText().toString().trim();
                        child_class_string = child_class_edittext.getText().toString().trim();

                        if(!child_fname_string.equals("")){
                            if(!child_lname_string.equals("")){
                                if(!child_class_string.equals("")){
                                    if(isNetworkAvailable()){
                                        SaveEditedChildDetails(child_fname_string, child_lname_string, child_gender_string, child_class_string);
                                    }else{
                                        Toast.makeText(Child_Details.this, "No internet connection", Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    child_class_edittext.setError("Required");
                                }
                            }else{
                                child_lname_edittext.setError("Required");
                            }
                        }else{
                            child_fname_edittext.setError("Required");
                        }
                    }
                });
            }
        });

        delete_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference frompath = FirebaseDatabase.getInstance().getReference("children")
                        .child(school_code).child(parent_code_string).child(child_code_string);

                final DatabaseReference topath = FirebaseDatabase.getInstance().getReference("deleted")
                        .child("child").child(school_code).child(parent_code_string).child(child_code_string);

                final AlertDialog.Builder logout = new AlertDialog.Builder(Child_Details.this, R.style.Myalert);
                logout.setTitle("Delete?");
                logout.setMessage("Are you sure you want to delete " + child_fname_string);
                logout.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        logout here
                        if(isNetworkAvailable()){
                            MoveChild_Information(frompath,topath,parent_code_string);
                        }else{
                            Toast.makeText(Child_Details.this,
                                    "No internet connection",Toast.LENGTH_LONG).show();
                        }
                    }
                });

                logout.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                logout.show();
            }
        });


    }

    private void SaveEditedChildDetails(final String child_fname_string, String child_lname_string, String child_gender_string, String child_class_string) {
        try {
            DatabaseReference add_child_three = FirebaseDatabase.getInstance().getReference("children")
                    .child(school_code)
                    .child(parent_code_string).child(child_code_string);

            add_child_three.child("class").setValue(child_class_string);
            add_child_three.child("firstname").setValue(child_fname_string);
            add_child_three.child("gender").setValue(child_gender_string);
            add_child_three.child("image").setValue("");
            add_child_three.child("lastname").setValue(child_lname_string);
            add_child_three.child("isAssigned_bus").setValue("No");
            add_child_three.child("assigned_bus").setValue("None").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(Child_Details.this, "Update successful", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder notifyadmin = new AlertDialog.Builder(Child_Details.this);
                    notifyadmin.setTitle("Child Updated")
                            .setMessage("You have successfully updated details of " + child_fname_string)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    notifyadmin.show();
                    addToNotifications();
                }
            });
        }catch (NullPointerException e){

        }
    }

    private void MoveChild_Information(DatabaseReference frompath, final DatabaseReference topath, final String parent_code_string) {
            frompath.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    topath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if(databaseError != null){
                            }else{
                                DeleteChild(parent_code_string);
                            }

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    private void DeleteChild(String parent_code_string) {
            try {
                //delete from parents node
                DatabaseReference delete_parents = FirebaseDatabase.getInstance().getReference("children")
                        .child(school_code).child(parent_code_string).child(child_code_string);
                delete_parents.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Child_Details.this, "Child deleted", Toast.LENGTH_LONG).show();
                        Intent reload = new Intent(Child_Details.this, Registered_Parents.class);
                        startActivity(reload);
                        Deletefrom_Notifications();
                    }
                });

            }catch (NullPointerException e){

            }
        }

    private void addToNotifications() {
        try {
            Random random = new Random();
            int a = random.nextInt(987654);
            String notificationID = "notification" + a+"";
            DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference("school_notifications").child(school_code).child(notificationID);
            mdatabase.child("image").setValue("UC");
            mdatabase.child("message").setValue("You have successfully updated details of " + child_fname_string);
            mdatabase.child("title").setValue("Child update successful");
            mdatabase.child("time").setValue(new Date().toString());
        }catch (NullPointerException e){

        }
    }

    private void Deletefrom_Notifications() {
        try {
            Random random = new Random();
            int a = random.nextInt(987654);
            String notificationID = "notification" + a+"";
            DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference("school_notifications").child(school_code).child(notificationID);
            mdatabase.child("image").setValue("DC");
            mdatabase.child("message").setValue("You have successfully deleted details of " + child_fname_string);
            mdatabase.child("title").setValue("Child deletion successful");
            mdatabase.child("time").setValue(new Date().toString());
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
