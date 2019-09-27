package com.example.sergeherkul.bustrackeradmin;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class Admin_Profile extends AppCompatActivity {

    private Button edit_button;
    private TextView school_name_text, school_email_text, school_number_text,
    school_location_text,school_name_text_value, school_email_text_value, school_number_text_value,
            school_location_text_value;
    private EditText school_name_editText, school_email_editText, school_number_editText,
            school_location_editText;
    private String school_code,string_school_name,string_school_email, string_school_number,
            string_school_location;
    private Accessories profileaccessor;
    private TextView success_message;
    private ProgressBar loading;
    private DatabaseReference databaseReference;
    private Dialog view_verification_dialogue;
    private Typeface lovelo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        getSupportActionBar().setTitle("Admin_Profile");

        lovelo =Typeface.createFromAsset(getAssets(),  "fonts/lovelo.ttf");

        profileaccessor = new Accessories(Admin_Profile.this);

        string_school_email = profileaccessor.getString("school_email");
        string_school_location = profileaccessor.getString("school_location");
        string_school_name = profileaccessor.getString("school_name");
        string_school_number = profileaccessor.getString("school_telephone");
        school_code = profileaccessor.getString("school_code");

        edit_button = findViewById(R.id.edit_button);
        school_name_text = findViewById(R.id.sname_text);
        school_email_text = findViewById(R.id.semail_text);
        school_number_text = findViewById(R.id.sphonenumber_text);
        school_location_text = findViewById(R.id.slocation_text);

        success_message = findViewById(R.id.success_message);
        loading = findViewById(R.id.loading);

        school_name_text_value = findViewById(R.id.the_school_name);
        school_email_text_value = findViewById(R.id.the_school_email);
        school_number_text_value = findViewById(R.id.theschool_number);
        school_location_text_value = findViewById(R.id.theschool_address);

        school_name_text_value.setText(string_school_name);
        school_email_text_value.setText(string_school_email);
        school_number_text_value.setText(string_school_number);
        school_location_text_value.setText(string_school_location);

        school_name_editText = findViewById(R.id.the_school_name_editText);
        school_email_editText = findViewById(R.id.the_school_email_editText);
        school_number_editText = findViewById(R.id.the_school_number_editText);
        school_location_editText = findViewById(R.id.the_school_address_editText);

        view_verification_dialogue = new Dialog(Admin_Profile.this);

//        setting font style here
        edit_button.setTypeface(lovelo);
        school_name_text.setTypeface(lovelo);
        school_email_text.setTypeface(lovelo);
        school_number_text.setTypeface(lovelo);
        school_location_text.setTypeface(lovelo);
        success_message.setTypeface(lovelo);

//        when edit button is clicked, it changes to save
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_button.setText("SAVE");
                school_name_text_value.setVisibility(View.GONE);
                school_email_text_value.setVisibility(View.GONE);
                school_number_text_value.setVisibility(View.GONE);
                school_location_text_value.setVisibility(View.GONE);

                school_name_editText.setVisibility(View.VISIBLE);
                school_email_editText.setVisibility(View.VISIBLE);
                school_number_editText.setVisibility(View.VISIBLE);
                school_location_editText.setVisibility(View.VISIBLE);

                school_name_editText.setText(string_school_name);
                school_email_editText.setText(string_school_email);
                school_number_editText.setText(string_school_number);
                school_location_editText.setText(string_school_location);

                edit_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        string_school_name = school_name_editText.getText().toString().trim();
                        string_school_email = school_email_editText.getText().toString().trim();
                        string_school_number = school_number_editText.getText().toString().trim();
                        string_school_location = school_location_editText.getText().toString().trim();

                        if(!string_school_name.equals("") && !string_school_email.equals("")
                        && !string_school_location.equals("") && !string_school_number.equals("")){
                            if(isNetworkAvailable()){
                                showSchoolVerifyPopup(Admin_Profile.this);
                            }else{
                                Toast.makeText(Admin_Profile.this,"No internet connection",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            loading.setVisibility(View.GONE);
                            success_message.setVisibility(View.VISIBLE);
                            success_message.setTextColor(getResources().getColor(R.color.red));
                            success_message.setText("All fields required");
                        }
                    }
                });
            }
        });
    }

    private void save_new_values(String string_school_name, String string_school_email, String string_school_number, String string_school_location) {
        loading.setVisibility(View.VISIBLE);
        success_message.setVisibility(View.GONE);

        databaseReference = FirebaseDatabase.getInstance().getReference("schools").child(school_code);
        databaseReference.child("name").setValue(string_school_name);
        databaseReference.child("email").setValue(string_school_email);
        databaseReference.child("telephone").setValue(string_school_number);
        databaseReference.child("location").setValue(string_school_location);
        loading.setVisibility(View.GONE);
        success_message.setVisibility(View.VISIBLE);
        success_message.setTextColor(getResources().getColor(R.color.green));
        success_message.setText("Update Successful");

        //updating shared preference values
        profileaccessor.put("school_email",string_school_email);
        profileaccessor.put("school_location",string_school_location);
        profileaccessor.put("school_name",string_school_name);
        profileaccessor.put("school_telephone",string_school_number);
        addToNotifications(FirebaseAuth.getInstance().getCurrentUser());
    }

    private void addToNotifications(FirebaseUser currentUser) {
        Random notifyrandom = new Random();
        int notify_no = notifyrandom.nextInt(233434);
        String notify_id = "notification" + notify_no+"";
        databaseReference = FirebaseDatabase.getInstance().getReference("notifications").child(school_code).child(currentUser.getUid()).child(notify_id);
        databaseReference.child("image").setValue("US");
        databaseReference.child("message").setValue("We have noticed an update of the details of your school, "+string_school_name
                +". If this activity wasn't you, contact customer care for rectification.");
        databaseReference.child("time").setValue(new Date());
        databaseReference.child("title").setValue("Update Successful");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showSchoolVerifyPopup(FragmentActivity activity) {
        final TextView cancelpopup,verification_message, success_message;
        final EditText code_one, code_two, code_three, code_four;
        final Button verify_button;
        view_verification_dialogue.setContentView(R.layout.custom_verification_popup);
        cancelpopup = (TextView)view_verification_dialogue.findViewById(R.id.cancel);
        verification_message = (TextView)view_verification_dialogue.findViewById(R.id.school_code_text);
        success_message = (TextView)view_verification_dialogue.findViewById(R.id.success_message);
        code_one = (EditText) view_verification_dialogue.findViewById(R.id.code_number_one);
        code_two = (EditText)view_verification_dialogue.findViewById(R.id.code_number_two);
        code_three = (EditText)view_verification_dialogue.findViewById(R.id.code_number_three);
        code_four = (EditText)view_verification_dialogue.findViewById(R.id.code_number_four);
        verify_button = (Button)view_verification_dialogue.findViewById(R.id.verify_school_button);

        cancelpopup.setTypeface(lovelo);
        verification_message.setTypeface(lovelo);
        verify_button.setTypeface(lovelo);
        success_message.setTypeface(lovelo);

        cancelpopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_verification_dialogue.dismiss();
            }
        });

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
                    verify_button.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        verify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code_one_string = code_one.getText().toString().trim();
                String code_two_string = code_two.getText().toString().trim();
                String code_three_string = code_three.getText().toString().trim();
                String code_four_string = code_four.getText().toString().trim();
                String full_code = code_one_string + code_two_string + code_three_string + code_four_string;

                if(!code_one_string.equals("") && !code_two_string.equals("") && !code_three_string.equals("")
                && !code_four_string.equals("")){
                    if(full_code.equals(school_code)){
                        view_verification_dialogue.dismiss();
                        save_new_values(string_school_name,string_school_email,string_school_number,string_school_location);
                    }else{
                        success_message.setVisibility(View.VISIBLE);
                        success_message.setTextColor(getResources().getColor(R.color.red));
                        success_message.setText("Code Verification Failed");
                    }
                }else{
                    success_message.setVisibility(View.VISIBLE);
                    success_message.setTextColor(getResources().getColor(R.color.red));
                    success_message.setText("Code required");
                }
            }
        });
        Objects.requireNonNull(view_verification_dialogue.getWindow()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        view_verification_dialogue.show();
    }
}
