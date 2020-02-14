package com.safet.admin.bustrackeradmin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.safet.admin.bustrackeradmin.Adapters.ChildrenAdapter;
import com.safet.admin.bustrackeradmin.Adapters.ParentsAdapter;
import com.safet.admin.bustrackeradmin.Model.Children;
import com.safet.admin.bustrackeradmin.Model.Parents;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Parent_Details extends AppCompatActivity {

    private Accessories parent_details_accessor;
    private String parent_fname, parent_lname, parent_address,parent_phone, parent_email, parent_code,
            school_code, schild_code, child_first_name, child_last_name, child_class, child_gender;

    private TextView parentfname_textview, parentlname_textview, parentnumber_textview, parentaddress_textview, parentemail_textview;
    private EditText parentfname_edittext, parentlname_edittext, parentnumber_edittext, parentaddress_edittext, parentemail_edittext;
    private Button editbutton;
    private ImageView editImage, call_parent, message_parent, delete_parent, add_child;

    //fetching child information from database
    private ArrayList childrenArray = new ArrayList<Children>();
    private RecyclerView children_RecyclerView;
    private RecyclerView.Adapter children_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent__details);
        parent_details_accessor = new Accessories(Parent_Details.this);

        parent_fname = parent_details_accessor.getString("parent_fname_from_admin");
        parent_lname = parent_details_accessor.getString("parent_lname_from_admin");
        parent_address = parent_details_accessor.getString("parent_address_from_admin");
        parent_phone = parent_details_accessor.getString("parent_phone_from_admin");
        parent_email = parent_details_accessor.getString("parent_email_from_admin");
        parent_code = parent_details_accessor.getString("parent_code_from_admin");
        school_code = parent_details_accessor.getString("school_code");

        getSupportActionBar().setTitle(parent_fname + " | Parent");

        //textviews
        parentfname_textview = findViewById(R.id.the_p_fname);
        parentlname_textview = findViewById(R.id.the_p_lname);
        parentnumber_textview = findViewById(R.id.theparent_number);
        parentaddress_textview = findViewById(R.id.theparent_address);
        parentemail_textview = findViewById(R.id.the_parent_email);

        //edittexts
        parentfname_edittext = findViewById(R.id.the_parent_fname_editText);
        parentlname_edittext = findViewById(R.id.the_parent_lname_editText);
        parentnumber_edittext = findViewById(R.id.the_parent_number_editText);
        parentaddress_edittext = findViewById(R.id.the_parent_address_editText);
        parentemail_edittext = findViewById(R.id.the_parent_email_editText);

        //buttons
        editbutton = findViewById(R.id.edit_button);

        //imageViews
        editImage = findViewById(R.id.edit_image);
        call_parent = findViewById(R.id.call_parent);
        message_parent = findViewById(R.id.message_parent);
        delete_parent = findViewById(R.id.delete_parent);
        add_child = findViewById(R.id.add_child);

        //recyclerview
        children_RecyclerView = findViewById(R.id.child_recyclerView);


        parentfname_textview.setText(parent_fname);
        parentlname_textview.setText(parent_lname);
        parentnumber_textview.setText(parent_phone);
        parentaddress_textview.setText(parent_address);
        parentemail_textview.setText(parent_email);


        if(isNetworkAvailable()){
            getParent_children();
        }else{
            Toast.makeText(Parent_Details.this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }

        children_RecyclerView.setHasFixedSize(true);
        children_RecyclerView.addItemDecoration(new DividerItemDecoration(Parent_Details.this,
                DividerItemDecoration.VERTICAL));

        children_Adapter = new ChildrenAdapter(getFromDatabase(),Parent_Details.this);
        children_RecyclerView.setAdapter(children_Adapter);


        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //make textviews invisible
                parentfname_textview.setVisibility(View.GONE);
                parentlname_textview.setVisibility(View.GONE);
                parentnumber_textview.setVisibility(View.GONE);
                parentaddress_textview.setVisibility(View.GONE);
                parentemail_textview.setVisibility(View.GONE);

                //make edittexts visible
                parentfname_edittext.setVisibility(View.VISIBLE);
                parentlname_edittext.setVisibility(View.VISIBLE);
                parentnumber_edittext.setVisibility(View.VISIBLE);
                parentaddress_edittext.setVisibility(View.VISIBLE);
                parentemail_edittext.setVisibility(View.VISIBLE);
                editbutton.setVisibility(View.VISIBLE);

                //set text into edittexts
                parentfname_edittext.setText(parent_fname);
                parentlname_edittext.setText(parent_lname);
                parentnumber_edittext.setText(parent_phone);
                parentaddress_edittext.setText(parent_address);
                parentemail_edittext.setText(parent_email);
            }
        });

        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent_fname = parentfname_edittext.getText().toString().trim();
                parent_lname = parentlname_edittext.getText().toString().trim();
                parent_address = parentaddress_edittext.getText().toString().trim();
                parent_phone = parentnumber_edittext.getText().toString().trim();
                parent_email = parentemail_edittext.getText().toString().trim();
                    if(!parent_fname.equals("")){
                        if(!parent_lname.equals("")){
                            if(!parent_phone.equals("")){
                                if (isNetworkAvailable()){
                                    SaveNewParentDetails(parent_fname, parent_lname, parent_address, parent_phone, parent_email);
                                }else{
                                    Toast.makeText(Parent_Details.this, "No internet connection",Toast.LENGTH_LONG).show();
                                }
                            }else{
                               parentnumber_edittext.setError("Required");
                            }
                        }else{
                            parentlname_edittext.setError("Required");
                        }
                    }else{
                        parentfname_edittext.setError("Required");
                    }

            }
        });

        call_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialer(v,parent_phone);
            }
        });

        delete_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference frompath = FirebaseDatabase.getInstance().getReference("parents")
                        .child(school_code).child(parent_code);

                final DatabaseReference topath = FirebaseDatabase.getInstance().getReference("deleted")
                        .child("parent").child(school_code).child(parent_code);

                final AlertDialog.Builder logout = new AlertDialog.Builder(Parent_Details.this, R.style.Myalert);
                logout.setTitle("Delete?");
                logout.setMessage("Are you sure you want to delete " + parent_fname
                        + " from the system?");
                logout.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        logout here
                        if(isNetworkAvailable()){
                            MoveParent_Information(frompath,topath,parent_code);
                        }else{
                            Toast.makeText(Parent_Details.this,
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

        message_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Parent_Details.this,
                        "Will be added soon",Toast.LENGTH_LONG).show();
            }
        });

        add_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addchild = new Intent(Parent_Details.this, Add_child.class);
                parent_details_accessor.put("pfname", parent_fname);
                parent_details_accessor.put("plname", parent_lname);
                parent_details_accessor.put("pid", parent_code);
                startActivity(addchild);
            }
        });
    }

    private void getParent_children() {
        try {
            DatabaseReference children = FirebaseDatabase.getInstance().getReference("children")
                    .child(school_code).child(parent_code);
            children.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            Fetch_Child_info(child.getKey());
                        }
                    }else{
//                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (NullPointerException e){

        }
    }

    private void Fetch_Child_info(final String key) {
        try{
            DatabaseReference childinfo = FirebaseDatabase.getInstance().getReference("children")
                    .child(school_code).child(parent_code).child(key);
            childinfo.addListenerForSingleValueEvent(new ValueEventListener() {
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

                        }
                        schild_code = key;
                        Children obj = new Children(parent_code,child_first_name,child_last_name,child_class,
                                schild_code,child_gender);
                        childrenArray.add(obj);
                        children_RecyclerView.setAdapter(children_Adapter);
                        children_Adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (NullPointerException e){

        }

    }

    private void SaveNewParentDetails(final String parent_fname, String parent_lname, String parent_address,
                                      String parent_phone, String parent_email) {
        try {
            DatabaseReference addParent_  = FirebaseDatabase.getInstance().getReference("parents")
                    .child(school_code).child(parent_code);
            addParent_.child("firstname").setValue(parent_fname);
            addParent_.child("lastname").setValue(parent_lname);
            addParent_.child("location").setValue(parent_address);
            addParent_.child("email").setValue(parent_email);
            addParent_.child("phone_number").setValue(parent_phone).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    addToNotifications(parent_fname);
                }
            });
        }catch (NullPointerException e){

        }
    }

    private void MoveParent_Information(DatabaseReference frompath, final DatabaseReference topath, final String parent_code) {
        frompath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                topath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if(databaseError != null){
                        }else{
                            DeleteParent_(parent_code);
                        }

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void DeleteParent_(String parent_code) {
        try {
            //delete from parents node
            DatabaseReference delete_parents = FirebaseDatabase.getInstance().getReference("parents")
                    .child(school_code).child(parent_code);
            delete_parents.removeValue();

            //delete from isRegistered node
            DatabaseReference delete_isRegistered = FirebaseDatabase.getInstance().getReference("isRegistered")
                    .child(school_code).child(parent_code);
            delete_isRegistered.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(Parent_Details.this, "Parent deleted", Toast.LENGTH_LONG).show();
                    Intent reload = new Intent(Parent_Details.this, Registered_Parents.class);
                    startActivity(reload);
                    Deletefrom_Notifications();
                }
            });

        }catch (NullPointerException e){

        }
    }


    private ArrayList<Children> getFromDatabase() {
        return childrenArray;
    }

    private void addToNotifications(String parent_firstname) {
        try {
            Random random = new Random();
            int a = random.nextInt(987654);
            String notificationID = "notification" + a+"";
            DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference("school_notifications").child(school_code).child(notificationID);
            mdatabase.child("image").setValue("AP");
            mdatabase.child("message").setValue("You have successfully updated the details of " + parent_firstname);
            mdatabase.child("title").setValue("Parent update successful");
            mdatabase.child("time").setValue(new Date().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    AlertDialog.Builder updateComplete = new AlertDialog.Builder(Parent_Details.this);
                    updateComplete.setTitle("Update successful")
                            .setMessage("Details of " + parent_fname + " has been successfully updated")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    updateComplete.show();
                }
            });
        }catch (NullPointerException e){

        }
    }

    private void Deletefrom_Notifications() {
        try {
            Random random = new Random();
            int a = random.nextInt(987654);
            String notificationID = "notification" + a+"";
            DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference("school_notifications").child(school_code).child(notificationID);
            mdatabase.child("image").setValue("DP");
            mdatabase.child("message").setValue("You have successfully deleted details of " + parent_fname);
            mdatabase.child("title").setValue("Parent deletion successful");
            mdatabase.child("time").setValue(new Date().toString());
        }catch (NullPointerException e){

        }
    }


    private void openDialer(View v, String call_number){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + call_number));
        v.getContext().startActivity(intent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
