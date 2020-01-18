package com.safet.admin.bustrackeradmin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Driver_Verification extends AppCompatActivity {

    private TextView final_step_text,code_verified,login_text,login_text_details, driver_code_verified,
    another_code_verified,change_number_textView, code_verified_error;
    private EditText code_one,code_two,code_three,code_four, driver_code_one, driver_code_two,
    driver_code_three,driver_code_four, driver_code_five;
    private ProgressBar loading;
    private Button next_button;
    private ImageView back;
    private String sschool_code,sdriver_code,sfirst_name,slastname,saddress,sphone_number,sschoolname,sschoolemail,
    ssechoollocation, sschoolphone,sbrand, sbus_code, schasis_no, smodel, snumber_plate;
    private Accessories driver_verification_accessor;
    private LinearLayout driver_verified_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver__verification);

        Typeface lovelo =Typeface.createFromAsset(getAssets(),  "fonts/lovelo.ttf");

        final_step_text =  findViewById(R.id.final_step_text);
        code_one = findViewById(R.id.code_number_one);
        code_two = findViewById(R.id.code_number_two);
        code_three = findViewById(R.id.code_number_three);
        code_four = findViewById(R.id.code_number_four);
        driver_code_one = findViewById(R.id.driver_code_number_one);
        driver_code_two = findViewById(R.id.driver_code_number_two);
        driver_code_three = findViewById(R.id.driver_code_number_three);
        driver_code_four = findViewById(R.id.driver_code_number_four);
        driver_code_five = findViewById(R.id.driver_code_number_five);
        loading = findViewById(R.id.loading);
        code_verified = findViewById(R.id.code_verified);
        login_text = findViewById(R.id.login_text);
        login_text_details = findViewById(R.id.login_text_details);
        next_button = findViewById(R.id.next_button);
        back = findViewById(R.id.back);
        driver_code_verified = findViewById(R.id.driver_code_verified);
        driver_verified_layout = findViewById(R.id.driver_verified_layout);
        another_code_verified = findViewById(R.id.another_code_verified);
        change_number_textView = findViewById(R.id.change_number);
        code_verified_error = findViewById(R.id.driver_code_verified_error);

        final_step_text.setTypeface(lovelo);
        code_verified.setTypeface(lovelo);
        login_text.setTypeface(lovelo);
        login_text_details.setTypeface(lovelo);
        next_button.setTypeface(lovelo);

        driver_verification_accessor = new Accessories(Driver_Verification.this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        change_number_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    FirebaseAuth.getInstance().signOut();
                    Intent enter_number = new Intent(Driver_Verification.this, Phone_number_Verification.class);
                    enter_number.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(enter_number);
                }else{
                    loading.setVisibility(View.GONE);
                    driver_code_verified.setVisibility(View.GONE);
                    code_verified_error.setVisibility(View.VISIBLE);
                    code_verified_error.setTextColor(getResources().getColor(R.color.red));
                    code_verified_error.setText("No internet connection");
                }
            }
        });

        //school code verification
        code_one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(code_one.getText().toString().length()==1)     //size as per your requirement
                {
                    code_two.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        code_two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(code_two.getText().toString().length()==1)     //size as per your requirement
                {
                    code_three.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        code_three.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(code_three.getText().toString().length()==1)     //size as per your requirement
                {
                    code_four.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        code_four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(code_four.getText().toString().length()==1)     //size as per your requirement
                {
//                    dothelogin();
//                    next_button.setText("VERIFY");
                    next_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String scode_one = code_one.getText().toString();
                            String scode_two = code_two.getText().toString();
                            String scode_three = code_three.getText().toString();
                            String scode_four = code_four.getText().toString();
                            if(!scode_one.equals("") && !scode_two.equals("") && !scode_three.equals("")
                                    && !scode_four.equals("")){
                                String full_code = scode_one + scode_two + scode_three + scode_four;
                                if(isNetworkAvailable()){
                                    loading.setVisibility(View.VISIBLE);
                                    fetchSchoolCodes(full_code);
                                }else{
                                    Toast.makeText(Driver_Verification.this,"No internet connection",Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(Driver_Verification.this,"Code Required",Toast.LENGTH_LONG).show();
                            }
//                            startActivity(new Intent(Driver_Verification.this,MapsActivity.class));
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //driver code verification
        driver_code_one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(driver_code_one.getText().toString().length()==1)     //size as per your requirement
                {
                    driver_code_two.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        driver_code_two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(driver_code_two.getText().toString().length()==1)     //size as per your requirement
                {
                    driver_code_three.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        driver_code_three.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(driver_code_three.getText().toString().length()==1)     //size as per your requirement
                {
                    driver_code_four.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        driver_code_four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(driver_code_four.getText().toString().length()==1)     //size as per your requirement
                {
                    driver_code_five.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        driver_code_five.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(driver_code_five.getText().toString().length()==1)     //size as per your requirement
                {
//                    dothelogin();
//                    next_button.setText("VERIFY");
                    next_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String scode_one = driver_code_one.getText().toString();
                            String scode_two = driver_code_two.getText().toString();
                            String scode_three = driver_code_three.getText().toString();
                            String scode_four = driver_code_four.getText().toString();
                            String scode_five = driver_code_five.getText().toString();
                            if(!scode_one.equals("") && !scode_two.equals("") && !scode_three.equals("")
                                    && !scode_four.equals("")){
                                final String full_code = scode_one + scode_two + scode_three + scode_four + scode_five;
                                if(isNetworkAvailable()){
                                    code_verified_error.setVisibility(View.GONE);
                                    loading.setVisibility(View.VISIBLE);
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("drivers").child(sschool_code).child(full_code);
                                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                driver_code_verified.setVisibility(View.VISIBLE);
                                                driver_code_verified.setTextColor(getResources().getColor(R.color.green));
                                                driver_verification_accessor.put("isverified",true);
                                                Intent gotodriver_maps = new Intent(Driver_Verification.this,MapsActivity.class);
                                                gotodriver_maps.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(gotodriver_maps);
                                                getdriverinformation(sschool_code, full_code);
                                                getBusInformation(driver_verification_accessor.getString("school_code"),driver_verification_accessor.getString("driver_code"));
                                            }else{
                                                loading.setVisibility(View.GONE);
                                                driver_code_verified.setVisibility(View.GONE);
                                                code_verified_error.setVisibility(View.VISIBLE);
                                                code_verified_error.setTextColor(getResources().getColor(R.color.red));
                                                code_verified_error.setText("Code Verification Failed");                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
//
                                }else{
                                    Toast.makeText(Driver_Verification.this,"No internet connection",Toast.LENGTH_LONG).show();

                                }
                            }else{
                                Toast.makeText(Driver_Verification.this,"Code Required",Toast.LENGTH_LONG).show();
                            }
//                            startActivity(new Intent(Driver_Verification.this,MapsActivity.class));
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void fetchSchoolCodes(final String thecode) {
        try{
            DatabaseReference schoolcodes = FirebaseDatabase.getInstance().getReference("drivers");

            schoolcodes.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
//                            FetchParts(child.getKey(), which_item);
                            if(thecode.equals(child.getKey())){
                                sschool_code = child.getKey();
                                driver_verification_accessor.put("school_code",child.getKey());
                                loading.setVisibility(View.GONE);
//                                code_verified.setVisibility(View.GONE);
//                                driver_code_verified.setVisibility(View.GONE);
                                another_code_verified.setText("Code verification complete");
                                another_code_verified.setTextColor(getResources().getColor(R.color.green));
                                another_code_verified.setVisibility(View.VISIBLE);
                                 next_button.setText("Verify Me");
                                driver_verified_layout.setVisibility(View.VISIBLE);

//                                getdriverinformation(child.getKey());
                                getSchoolinformation(child.getKey());

                                next_button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(!driver_code_one.equals("") && !driver_code_two.equals("") &&
                                        !driver_code_three.equals("") && !driver_code_four.equals("") &&
                                        !driver_code_five.equals("")){

                                        }else{
                                            Toast.makeText(Driver_Verification.this,"Driver code required", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                return;
                            }else{
                                loading.setVisibility(View.GONE);
                                code_verified.setVisibility(View.GONE);
//                                code_verified.setTextColor(getResources().getColor(R.color.red));
//                                code_verified.setText("Code Verification Failed");

                                another_code_verified.setText("Code verification failed");
                                another_code_verified.setTextColor(getResources().getColor(R.color.red));
                                another_code_verified.setVisibility(View.VISIBLE);

                            }
                        }
                    }else{
//                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Driver_Verification.this,"Cancelled",Toast.LENGTH_LONG).show();
                }
            });
        }catch (NullPointerException e){

        }
    }

    private void getBusInformation(String schoolcode, String drivercode) {
        DatabaseReference school_details = FirebaseDatabase.getInstance().getReference("bus_details").child(schoolcode).child(drivercode);
        school_details.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("brand")){
                            sbrand = child.getValue().toString();
                            driver_verification_accessor.put("bus_brand",sbrand);
                        }
                        if(child.getKey().equals("bus_code")){
                            sbus_code = child.getValue().toString();
                            driver_verification_accessor.put("bus_code",sbus_code);
                        }
                        if(child.getKey().equals("chasis_no")){
                            schasis_no = child.getValue().toString();
                            driver_verification_accessor.put("bus_chasis_no",schasis_no);
                        }
                        if(child.getKey().equals("model")){
                            smodel = child.getValue().toString();
                            driver_verification_accessor.put("bus_model",smodel);
                        }
                        if(child.getKey().equals("number_plate")){
                            snumber_plate = child.getValue().toString();
                            driver_verification_accessor.put("bus_number_plate",snumber_plate);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getSchoolinformation(String key) {
        DatabaseReference school_details = FirebaseDatabase.getInstance().getReference("schools").child(key);
        school_details.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("email")){
                            sschoolemail = child.getValue().toString();
                            driver_verification_accessor.put("school_email",sschoolemail);
                        }
                        if(child.getKey().equals("location")){
                            ssechoollocation = child.getValue().toString();
                            driver_verification_accessor.put("school_location",ssechoollocation);
                        }
                        if(child.getKey().equals("name")){
                            sschoolname = child.getValue().toString();
                            driver_verification_accessor.put("school_name",sschoolname);
                        }
                        if(child.getKey().equals("telephone")){
                            sschoolphone = child.getValue().toString();
                            driver_verification_accessor.put("school_telephone",sschoolphone);
                        }
                }
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getdriverinformation(final String the_school_code, final String the_driver_code) {
        driver_verification_accessor.put("driver_code", the_driver_code);
        DatabaseReference getindividual_info = FirebaseDatabase.getInstance().getReference("drivers").child(the_school_code).child(the_driver_code);
        getindividual_info.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals("first_name")) {
                            sfirst_name = child.getValue().toString();
                            driver_verification_accessor.put("driver_fname", sfirst_name);
                        }
                        if (child.getKey().equals("last_name")) {
                            slastname = child.getValue().toString();
                            driver_verification_accessor.put("driver_lname", slastname);
                        }
                        if (child.getKey().equals("address")) {
                            saddress = child.getValue().toString();
                            driver_verification_accessor.put("driver_address", saddress);
                        }
                        if (child.getKey().equals("phone_number")) {
                            sphone_number = child.getValue().toString();
                            driver_verification_accessor.put("driver_pnumber", sphone_number);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
