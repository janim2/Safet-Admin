package com.safet.admin.bustrackeradmin;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Random;

public class Add_Parent extends AppCompatActivity {

    private TextView parentdetails_textView,parentfname_textView,parentlname_textView, parentaddress_textView,
    parentemail_textView,parentphone_number_textView, childDetials_text, child_assignment_text,
            add_child_text, success_message;
    private TextView first_child_count, second_child_count, third_child_count;

    private EditText parent_fname_editText, parent_lname_edittext, parent_address_editText,
            parent_email_editText, parent_phone_number_editText;

    //child details editText and spinners and strings
    private EditText fchild_fname_editText, fchild_lname_editText, fchild_class, sechild_fname_editText,
    sechild_lname_editText, sechild_class, tchild_fname_editText, tchild_lname_editText, tchild_class;
    private Spinner childone_gender, childtwo_gender, childthree_gender;
    private String fchild_fname_string, fchild_lname_string, fchild_gender_string, fchild_class_string,
    sechild_fname_string, sechild_lname_string, sechild_gender_string, sechild_class_string,
    tchild_fname_string, tchild_lname_string, tchild_gender_string, tchild_class_string, fgender_string,
    segender_string, thgender_string;

    private ProgressBar loading;
    private Typeface lovelo;
    private Button add_parent;
    private int counter = 0;
    private String school_code, parent_code,pfname, plname, p_location, p_email, pphone_number;
    private LinearLayout childone_layout, child_two_layout, child_three_layout;
    private String[] gender = {"Male", "Female"};
    private Accessories addparentAccessor;

    ArrayAdapter<String> gender_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__parent);

        lovelo = Typeface.createFromAsset(getAssets(),  "fonts/lovelo.ttf");

        getSupportActionBar().setTitle("Add Parent");
        addparentAccessor = new Accessories(Add_Parent.this);

//        LinearLayouts
        childone_layout = findViewById(R.id.child_one_layout);
        child_two_layout = findViewById(R.id.child_two_layout);
        child_three_layout = findViewById(R.id.child_three_layout);

        //textviews

        parentdetails_textView = findViewById(R.id.parentdetails_text);
        parentfname_textView = findViewById(R.id.fname_text);
        parentlname_textView = findViewById(R.id.lname_text);
        parentaddress_textView = findViewById(R.id.location_text);
        parentemail_textView = findViewById(R.id.email_text);
        parentphone_number_textView = findViewById(R.id.phonenumber_text);
        childDetials_text = findViewById(R.id.childdetails_text);
        child_assignment_text = findViewById(R.id.child_assignment_text);
        add_child_text = findViewById(R.id.add_child_text);
        success_message = findViewById(R.id.success_message);

        //child details editText
        fchild_fname_editText = findViewById(R.id.the_child_first_name);
        fchild_lname_editText = findViewById(R.id.the_childlast_name_editText);
        childone_gender = findViewById(R.id.gender_spinner);
        fchild_class = findViewById(R.id.the_class_editText);

        sechild_fname_editText = findViewById(R.id.the_second_child_first_name);
        sechild_lname_editText = findViewById(R.id.the_second_childlast_name_editText);
        childtwo_gender = findViewById(R.id.the_second_gender_spinner);
        sechild_class = findViewById(R.id.the_second_class_editText);

        tchild_fname_editText = findViewById(R.id.the_third_child_first_name);
        tchild_lname_editText = findViewById(R.id.the_third_childlast_name_editText);
        childthree_gender = findViewById(R.id.third_gender_spinner);
        tchild_class = findViewById(R.id.the_third_class_editText);

        //child counters
        first_child_count = findViewById(R.id.child_count);
        second_child_count = findViewById(R.id.second_child_count);
        third_child_count = findViewById(R.id.third_child_count);

        //edittexts
        parent_fname_editText = findViewById(R.id.the_parent_fname_editText);
        parent_lname_edittext = findViewById(R.id.the_parent_lname_editText);
        parent_address_editText = findViewById(R.id.the_parent_address_editText);
        parent_email_editText = findViewById(R.id.the_email_editText);
        parent_phone_number_editText = findViewById(R.id.the_parent_number_editText);
        loading = findViewById(R.id.loading);
        add_parent = findViewById(R.id.add_button);

        school_code = addparentAccessor.getString("school_code");

        //setting the font
        parentdetails_textView.setTypeface(lovelo);
        parentfname_textView .setTypeface(lovelo);
        parentlname_textView .setTypeface(lovelo);
        parentaddress_textView.setTypeface(lovelo);
        parentemail_textView .setTypeface(lovelo);
        parentphone_number_textView.setTypeface(lovelo);
        childDetials_text.setTypeface(lovelo);
        child_assignment_text.setTypeface(lovelo);
        add_child_text.setTypeface(lovelo);
//        success_message.setTypeface(lovelo);
        add_parent.setTypeface(lovelo);

        //setting the adapter for the child one spinner
        childone_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  fgender_string = gender[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fgender_string = "Male";
            }
        });
        gender_type = new ArrayAdapter<String>(Add_Parent.this,android.R.layout.simple_list_item_1,gender);
        childone_gender.setAdapter(gender_type);

        //setting the adapter for the child two spinner
        childtwo_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  segender_string = gender[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                segender_string = "Male";
            }
        });
        gender_type = new ArrayAdapter<String>(Add_Parent.this,android.R.layout.simple_list_item_1,gender);
        childtwo_gender.setAdapter(gender_type);


        //setting the adapter for the child two spinner
        childthree_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  thgender_string = gender[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                thgender_string = "Male";
            }
        });
        gender_type = new ArrayAdapter<String>(Add_Parent.this,android.R.layout.simple_list_item_1,gender);
        childthree_gender.setAdapter(gender_type);



        add_child_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter == 0){
                    counter += 1;
                    childone_layout.setVisibility(View.VISIBLE);
                    childone_gender.setAdapter(gender_type);
                }
                else if(counter == 1){
                    counter += 1;
                    childone_layout.setVisibility(View.VISIBLE);
                    child_two_layout.setVisibility(View.VISIBLE);
                    childtwo_gender.setAdapter(gender_type);
                }
                else if(counter == 2){
                    counter += 1;
                    childone_layout.setVisibility(View.VISIBLE);
                    child_two_layout.setVisibility(View.VISIBLE);
                    child_three_layout.setVisibility(View.VISIBLE);
                    childthree_gender.setAdapter(gender_type);

                }else{
                    Toast.makeText(Add_Parent.this, "maximum child limit reached", Toast.LENGTH_LONG).show();
                }
            }
        });

        first_child_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter -= 1;
                childone_layout.setVisibility(View.GONE);
                child_two_layout.setVisibility(View.GONE);
                child_three_layout.setVisibility(View.GONE);
            }
        });

        second_child_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter -= 1;
                child_two_layout.setVisibility(View.GONE);
                child_three_layout.setVisibility(View.GONE);
            }
        });

        third_child_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter -= 1;
                child_three_layout.setVisibility(View.GONE);
            }
        });


        add_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pfname = parent_fname_editText.getText().toString().trim();
                plname = parent_lname_edittext.getText().toString().trim();
                p_location = parent_address_editText.getText().toString().trim();
                p_email = parent_email_editText.getText().toString().trim();
                pphone_number = parent_phone_number_editText.getText().toString().trim();

                if(!pfname.equals("") && !plname.equals("") && !p_location.equals("")
                        && !p_email.equals("") && !pphone_number.equals("")){
                    if(counter != 0){
                        if(isNetworkAvailable()){
                            if(!fchild_fname_editText.getText().toString().trim().equals("")){
                                addParent_Details_to_store(pfname, plname, p_location, p_email, pphone_number);
                            }else {
                                Toast.makeText(Add_Parent.this, "Child details required", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(Add_Parent.this, "No internet connection", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(Add_Parent.this, "Child details required", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(Add_Parent.this, "All fields required",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void addParent_Details_to_store(String pfname, String plname, String p_location, String p_email, String pphone_number) {
        try {
            DatabaseReference addParent_  = FirebaseDatabase.getInstance().getReference("parents").child(school_code).child(pphone_number);
            addParent_.child("firstname").setValue(pfname);
            addParent_.child("lastname").setValue(plname);
            addParent_.child("location").setValue(p_location);
            addParent_.child("email").setValue(p_email);
            addParent_.child("phone_number").setValue(pphone_number);
            if(counter == 1){
                add_OneChild(pphone_number);
            }else if(counter == 2){
                add_two_Children(pphone_number);
            }else if(counter == 3){
                addthree_Children(pphone_number);
            }
        }catch (NullPointerException e){

        }

    }

    private void add_OneChild(String pone_no) {
        fchild_fname_string = fchild_fname_editText.getText().toString().trim();
        fchild_lname_string = fchild_lname_editText.getText().toString().trim();
        fchild_gender_string = fgender_string;
        fchild_class_string = fchild_class.getText().toString().trim();

        if(!fchild_fname_string.equals("") && !fchild_lname_string.equals("") && !fchild_class_string.equals("")){
            try {
                Random random = new Random();
                int a = random.nextInt(9864);
                String child_one_id = "child" + a + "";
                DatabaseReference add_child_one = FirebaseDatabase.getInstance().getReference("children").child(school_code).child(pone_no).child(child_one_id);

                add_child_one.child("class").setValue(fchild_class_string);
                add_child_one.child("firstname").setValue(fchild_fname_string);
                add_child_one.child("gender").setValue(fchild_gender_string);
                add_child_one.child("image").setValue("");
                add_child_one.child("lastname").setValue(fchild_lname_string);
                add_child_one.child("isAssigned_bus").setValue("No");
                add_child_one.child("assigned_bus").setValue("None");
                Toast.makeText(Add_Parent.this, "Addition successful", Toast.LENGTH_LONG).show();
                finish();
                addToNotifications();
            }catch (NullPointerException e){

            }
        }else{
            Toast.makeText(Add_Parent.this, "Child details required", Toast.LENGTH_LONG).show();
        }
    }

    private void add_two_Children(String pone_no) {
        //first child
        fchild_fname_string = fchild_fname_editText.getText().toString().trim();
        fchild_lname_string = fchild_lname_editText.getText().toString().trim();
        fchild_gender_string = fgender_string;
        fchild_class_string = fchild_class.getText().toString().trim();

        //second child
        sechild_fname_string = sechild_fname_editText.getText().toString().trim();
        sechild_lname_string = sechild_lname_editText.getText().toString().trim();
        sechild_gender_string = segender_string;
        sechild_class_string = sechild_class.getText().toString().trim();

        if(!fchild_fname_string.equals("") && !fchild_lname_string.equals("") && !fchild_class_string.equals("")){
            if(!sechild_fname_string.equals("") && !sechild_lname_string.equals("") && !sechild_class_string.equals("")){
            try {
                Random random = new Random();
                int a = random.nextInt(9864);
                String child_one_id = "child" + a + "";
                DatabaseReference add_child_one = FirebaseDatabase.getInstance().getReference("children").child(school_code).child(pone_no).child(child_one_id);

                add_child_one.child("class").setValue(sechild_class_string);
                add_child_one.child("firstname").setValue(fchild_fname_string);
                add_child_one.child("gender").setValue(fchild_gender_string);
                add_child_one.child("image").setValue("");
                add_child_one.child("lastname").setValue(fchild_lname_string);
                add_child_one.child("isAssigned_bus").setValue("No");
                add_child_one.child("assigned_bus").setValue("None");

                Random c_tworandom = new Random();
                int ab = c_tworandom.nextInt(9864);
                String child_two_id = "child" + ab + "";
                DatabaseReference add_child_two = FirebaseDatabase.getInstance().getReference("children").child(school_code).child(pone_no).child(child_two_id);

                add_child_two.child("class").setValue(sechild_class_string);
                add_child_two.child("firstname").setValue(sechild_fname_string);
                add_child_two.child("gender").setValue(sechild_gender_string);
                add_child_two.child("image").setValue("");
                add_child_two.child("lastname").setValue(sechild_lname_string);
                add_child_one.child("isAssigned_bus").setValue("No");
                add_child_one.child("assigned_bus").setValue("None");
                Toast.makeText(Add_Parent.this, "Addition successful", Toast.LENGTH_LONG).show();
                finish();
                addToNotifications();
            }catch (NullPointerException e){

            }
        }
    }

//    public void addChildLayout(){
//        //Inflater service
//        LayoutInflater layoutInfralte=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        //parent layout xml refrence
//        final LinearLayout linearLayout=(LinearLayout)findViewById(R.id.parent_layout);
//        //Child layout xml refrence
//        view=layoutInfralte.inflate(R.layout.child_knowables, null);
//        view.setId(3999);
//        TextView childcount = view.findViewById(R.id.child_count);
//        childfname = view.findViewById(R.id.the_child_first_name);
//        childlastname = view.findViewById(R.id.the_childlast_name_editText);
//        childclass = view.findViewById(R.id.the_class_editText);
//        childcount.setText("Child " + counter+"");
//
//        childcount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                linearLayout.removeView(view);
//                counter -= 1;
//            }
//        });
//        //add child to parent
//        linearLayout.addView(view);
//    }
    }

    private void addthree_Children(String pone_no) {
        //first child
        fchild_fname_string = fchild_fname_editText.getText().toString().trim();
        fchild_lname_string = fchild_lname_editText.getText().toString().trim();
        fchild_gender_string = fgender_string;
        fchild_class_string = fchild_class.getText().toString().trim();

        //second child
        sechild_fname_string = sechild_fname_editText.getText().toString().trim();
        sechild_lname_string = sechild_lname_editText.getText().toString().trim();
        sechild_gender_string = segender_string;
        sechild_class_string = sechild_class.getText().toString().trim();

        //third child
        tchild_fname_string = tchild_fname_editText.getText().toString().trim();
        tchild_lname_string = tchild_lname_editText.getText().toString().trim();
        tchild_gender_string = thgender_string;
        tchild_class_string = tchild_class.getText().toString().trim();


        if(!fchild_fname_string.equals("") && !fchild_lname_string.equals("") && !fchild_class_string.equals("")){
            if(!sechild_fname_string.equals("") && !sechild_lname_string.equals("") && !sechild_class_string.equals("")){
                if(!tchild_fname_string.equals("") && !tchild_lname_string.equals("") && !tchild_class_string.equals("")){
                    try {
                        Random random = new Random();
                        int a = random.nextInt(9864);
                        String child_one_id = "child" + a + "";
                        DatabaseReference add_child_one = FirebaseDatabase.getInstance().getReference("children").child(school_code).child(pone_no).child(child_one_id);

                        add_child_one.child("class").setValue(sechild_class_string);
                        add_child_one.child("firstname").setValue(fchild_fname_string);
                        add_child_one.child("gender").setValue(fchild_gender_string);
                        add_child_one.child("image").setValue("");
                        add_child_one.child("lastname").setValue(fchild_lname_string);
                        add_child_one.child("isAssigned_bus").setValue("No");
                        add_child_one.child("assigned_bus").setValue("None");

                        //child two
                        Random c_tworandom = new Random();
                        int ab = c_tworandom.nextInt(9864);
                        String child_two_id = "child" + ab + "";
                        DatabaseReference add_child_two = FirebaseDatabase.getInstance().getReference("children").child(school_code).child(pone_no).child(child_two_id);

                        add_child_two.child("class").setValue(sechild_class_string);
                        add_child_two.child("firstname").setValue(sechild_fname_string);
                        add_child_two.child("gender").setValue(sechild_gender_string);
                        add_child_two.child("image").setValue("");
                        add_child_two.child("lastname").setValue(sechild_lname_string);
                        add_child_one.child("isAssigned_bus").setValue("No");
                        add_child_one.child("assigned_bus").setValue("None");

                        //child three
                        Random c_threerandom = new Random();
                        int abc = c_threerandom.nextInt(9864);
                        String child_three_id = "child" + abc + "";
                        DatabaseReference add_child_three = FirebaseDatabase.getInstance().getReference("children").child(school_code).child(pone_no).child(child_three_id);

                        add_child_three.child("class").setValue(tchild_class_string);
                        add_child_three.child("firstname").setValue(tchild_fname_string);
                        add_child_three.child("gender").setValue(tchild_gender_string);
                        add_child_three.child("image").setValue("");
                        add_child_three.child("lastname").setValue(tchild_lname_string);
                        add_child_one.child("isAssigned_bus").setValue("No");
                        add_child_one.child("assigned_bus").setValue("None");
                        Toast.makeText(Add_Parent.this, "Addition successful", Toast.LENGTH_LONG).show();
                        finish();
                        addToNotifications();
                    }catch (NullPointerException e){

                    }
            }else{
                    Toast.makeText(Add_Parent.this, "child three details required", Toast.LENGTH_LONG).show();
                }
            } else{
                Toast.makeText(Add_Parent.this, "child two details required", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(Add_Parent.this, "child one details required", Toast.LENGTH_LONG).show();
        }

    }

    private void addToNotifications() {
        try {
            Random random = new Random();
            int a = random.nextInt(987654);
            String notificationID = "notification" + a+"";
            DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference("school_notifications").child(school_code).child(notificationID);
            mdatabase.child("image").setValue("AP");
            mdatabase.child("message").setValue("You have successfully added " + pfname + " to school as a parent");
            mdatabase.child("title").setValue("Parent addition successful");
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
