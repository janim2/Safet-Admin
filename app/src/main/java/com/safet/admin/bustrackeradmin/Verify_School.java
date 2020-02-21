package com.safet.admin.bustrackeradmin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Verify_School extends AppCompatActivity {

    private ImageView goback;
    private TextView final_step_text, school_code_text, code_verified_text, school_v_text, change_email,
            code_verified_error;
    private EditText code_one, code_two, code_three, code_four;
    private Button next_button;
    private ProgressBar loading;
    private Accessories verify_school_accessor;
    private String sschoolname, sschoolemail, ssechoollocation, sschoolphone, temp_school_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify__school);

        Typeface lovelo = Typeface.createFromAsset(getAssets(), "fonts/lovelo.ttf");

        verify_school_accessor = new Accessories(Verify_School.this);
        temp_school_email = verify_school_accessor.getString("school_email_temp");

        goback = findViewById(R.id.back);
        final_step_text = findViewById(R.id.final_step_text);
        school_code_text = findViewById(R.id.enter_school_code_text);

        code_one = findViewById(R.id.code_number_one);
        code_two = findViewById(R.id.code_number_two);
        code_three = findViewById(R.id.code_number_three);
        code_four = findViewById(R.id.code_number_four);
        next_button = findViewById(R.id.next_button);
        code_verified_text = findViewById(R.id.code_verified);
        school_v_text = findViewById(R.id.school_v_text);
        change_email = findViewById(R.id.change_email);
        loading = findViewById(R.id.loading);
        code_verified_error = findViewById(R.id.code_verified_error);

        final_step_text.setTypeface(lovelo);
        school_code_text.setTypeface(lovelo);
        next_button.setTypeface(lovelo);
        code_verified_text.setTypeface(lovelo);
        school_v_text.setTypeface(lovelo);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    finish();
                }

            }
        });

        change_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotBack_to_login = new Intent(Verify_School.this, Admin_Login.class);
                gotBack_to_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(gotBack_to_login);
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Verify_School.this, "Code required", Toast.LENGTH_LONG).show();
            }
        });

        code_one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (code_one.getText().toString().length() == 1)     //size as per your requirement
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
                if (code_two.getText().toString().length() == 1)     //size as per your requirement
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
                if (code_three.getText().toString().length() == 1)     //size as per your requirement
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
                if (code_four.getText().toString().length() == 1)     //size as per your requirement
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
                            if (!scode_one.equals("") && !scode_two.equals("") && !scode_three.equals("")
                                    && !scode_four.equals("")) {
                                String full_code = scode_one + scode_two + scode_three + scode_four;
                                if (isNetworkAvailable()) {
                                    loading.setVisibility(View.VISIBLE);
                                    fetchSchoolCodes(full_code);
                                } else {
                                    Toast.makeText(Verify_School.this, "No internet connection", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(Verify_School.this, "Code Required", Toast.LENGTH_LONG).show();
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
        //finding parent node from code value
        Query getDriver_code_query = FirebaseDatabase.getInstance().getReference("schools").orderByChild("email").equalTo(temp_school_email);
        getDriver_code_query.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange (@NonNull DataSnapshot dataSnapshot){
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                String key = ds.getKey();
                String school_code = key;
                if(school_code.equals(thecode)){
                    verify_school_accessor.put("school_code",school_code);
                    loading.setVisibility(View.GONE);
                    code_verified_error.setVisibility(View.GONE);
                    code_verified_text.setVisibility(View.VISIBLE);
                    Intent goto_main = new Intent(Verify_School.this, Admin_MainActivity.class);
                    goto_main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(goto_main);
                    verify_school_accessor.put("isverified",true);
                    getSchoolinformation(school_code);
                }else{
                    loading.setVisibility(View.GONE);
                    code_verified_text.setVisibility(View.GONE);
                    code_verified_error.setVisibility(View.VISIBLE);
                }

            }
        }

        @Override
        public void onCancelled (@NonNull DatabaseError databaseError){

        }
    });
}

//    private void fetchSchoolCodes(final String thecode) {
//        try{
//            DatabaseReference schoolcodes = FirebaseDatabase.getInstance().getReference("schools");
//
//            schoolcodes.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if(dataSnapshot.exists()){
//                        for(DataSnapshot child : dataSnapshot.getChildren()){
////                            FetchParts(child.getKey(), which_item);
//                            if(thecode.equals(child.getKey())){
//                                verify_school_accessor.put("school_code",child.getKey());
//                                loading.setVisibility(View.GONE);
//                                code_verified_error.setVisibility(View.GONE);
//                                code_verified_text.setVisibility(View.VISIBLE);
//                                Intent goto_main = new Intent(Verify_School.this, Admin_MainActivity.class);
//                                goto_main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(goto_main);
//                                verify_school_accessor.put("isverified",true);
//                                getSchoolinformation(child.getKey());
//                            }else{
//                                loading.setVisibility(View.GONE);
//                                code_verified_text.setVisibility(View.GONE);
//                                code_verified_error.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    }else{
////                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Toast.makeText(Verify_School.this,"Cancelled",Toast.LENGTH_LONG).show();
//                }
//            });
//        }catch (NullPointerException e){
//
//        }
//    }

    private void getSchoolinformation(String key) {
        DatabaseReference school_details = FirebaseDatabase.getInstance().getReference("schools").child(key);
        school_details.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("email")){
                            sschoolemail = child.getValue().toString();
                            verify_school_accessor.put("school_email",sschoolemail);
                        }
                        if(child.getKey().equals("location")){
                            ssechoollocation = child.getValue().toString();
                            verify_school_accessor.put("school_location",ssechoollocation);
                        }
                        if(child.getKey().equals("name")){
                            sschoolname = child.getValue().toString();
                            verify_school_accessor.put("school_name",sschoolname);
                        }
                        if(child.getKey().equals("telephone")){
                            sschoolphone = child.getValue().toString();
                            verify_school_accessor.put("school_telephone",sschoolphone);
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
