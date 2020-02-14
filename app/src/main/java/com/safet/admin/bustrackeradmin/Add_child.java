package com.safet.admin.bustrackeradmin;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Random;

public class Add_child extends AppCompatActivity {

    private EditText child_fname_editText, child_lname_editText, child_class;
    private Spinner childone_gender;
    private ProgressBar loading;
    private Button add_child;
    private String school_code, parent_fname, parent_lname, parent_code,childfname_string, child_lname_string,
    childgender_string, childclass_string;
    private Accessories addchildaccesssor;
    private String[] gender = {"Male", "Female"};
    private ArrayAdapter<String> gender_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        addchildaccesssor = new Accessories(Add_child.this);

        getSupportActionBar().setTitle("Add child");

        child_fname_editText = findViewById(R.id.the_child_first_name);
        child_lname_editText = findViewById(R.id.the_childlast_name_editText);
        childone_gender = findViewById(R.id.gender_spinner);
        child_class = findViewById(R.id.the_class_editText);
        loading = findViewById(R.id.loading);
        add_child = findViewById(R.id.add_button);

        school_code = addchildaccesssor.getString("school_code");
        parent_fname = addchildaccesssor.getString("pfname");
        parent_lname = addchildaccesssor.getString("plname");
        parent_code = addchildaccesssor.getString("pid");

        //setting the adapter for the child one spinner
        childone_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                childgender_string = gender[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                childgender_string = "Male";
            }
        });
        gender_type = new ArrayAdapter<String>(Add_child.this,android.R.layout.simple_list_item_1,gender);
        childone_gender.setAdapter(gender_type);

        add_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childfname_string = child_fname_editText.getText().toString().trim();
                child_lname_string = child_lname_editText.getText().toString().trim();
                childclass_string = child_class.getText().toString().trim();

                if(!childfname_string.equals("")){
                    if(!child_lname_string.equals("")){
                        if(!childclass_string.equals("")){
                            if(isNetworkAvailable()){
                                AddChildToDatabase(childfname_string, child_lname_string, childclass_string);
                            }else{
                                Toast.makeText(Add_child.this, "No internet connection", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            child_class.setError("Required");
                        }
                    }else{
                        child_lname_editText.setError("Required");
                    }
                }else{
                    child_fname_editText.setError("Required");
                }
            }
        });


    }

    private void AddChildToDatabase(final String childfname_string, String child_lname_string, String childclass_string) {
        try {
            Random c_threerandom = new Random();
            int abc = c_threerandom.nextInt(9864);
            String child_three_id = "child" + abc + "";
            DatabaseReference add_child_three = FirebaseDatabase.getInstance().getReference("children").child(school_code)
                    .child(parent_code).child(child_three_id);

            add_child_three.child("class").setValue(childclass_string);
            add_child_three.child("firstname").setValue(childfname_string);
            add_child_three.child("gender").setValue(childgender_string);
            add_child_three.child("image").setValue("");
            add_child_three.child("lastname").setValue(child_lname_string);
            add_child_three.child("isAssigned_bus").setValue("No");
            add_child_three.child("assigned_bus").setValue("None").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(Add_child.this, "Addition successful", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder notifyadmin = new AlertDialog.Builder(Add_child.this);
                    notifyadmin.setTitle("Child Added")
                            .setMessage("You have successfully added " + childfname_string  + " as a child of " + parent_fname + " "+ parent_lname)
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

    private void addToNotifications() {
        try {
            Random random = new Random();
            int a = random.nextInt(987654);
            String notificationID = "notification" + a+"";
            DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference("school_notifications").child(school_code).child(notificationID);
            mdatabase.child("image").setValue("AC");
            mdatabase.child("message").setValue("You have successfully added " + childfname_string  + " as a child of " +
                    parent_fname + " " + parent_lname);
            mdatabase.child("title").setValue("Child addition successful");
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
