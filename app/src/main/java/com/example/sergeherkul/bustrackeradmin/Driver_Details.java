package com.safet.admin.bustrackeradmin;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Driver;
import java.util.Objects;

public class Driver_Details extends AppCompatActivity {

    private Accessories driver_details_accessor;
    private String driver_fname, driver_lname, driver_address, driver_phone_, driver_code, school_code,
    sbus_brand, sbus_code, sbus_chasis_number, sbus_model, sbus_number,
    driver_name_from_etext,driver_phone_from_etext,driver_address_from_etext,busbrand_fro_etext
            ,bus_model_from_etext,bus_chasis_from_etext,bus_number_plage_etext;

    private TextView driverDetails_text, school_details_text, driver_name_textview, driver_phone_number_textview,
    driver_address_textview, bus_brand_textview, bus_model_textview, bus_chasis_textview, bus_number_plate_textview,
    the_drivername, the_driver_phone_number, the_driver_address, the_bus_brand, the_bus_model, the_bus_chasis_number,
    the_bus_numberplate, success_message;

    private EditText driver_name_editText, driver_phone_number_editText,driver_address_editText,
    busbrand_editText, bus_model_editText, bus_chasis_number_editText, bus_number_plate_editText;

    private Button next_button;
    private ProgressBar loading;

    private Dialog view_verification_dialogue;
    private Typeface lovelo;

    private DatabaseReference mdatabasereference,mdatabasereference_bus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver__details);

        driver_details_accessor = new Accessories(Driver_Details.this);
        getSupportActionBar().setTitle("Driver Details");
        lovelo =Typeface.createFromAsset(getAssets(),  "fonts/lovelo.ttf");
        school_code = driver_details_accessor.getString("school_code");

        driver_fname = driver_details_accessor.getString("driver_fname_from_admin");
        driver_lname = driver_details_accessor.getString("driver_lname_from_admin");
        driver_address = driver_details_accessor.getString("driver_address_from_admin");
        driver_phone_ = driver_details_accessor.getString("driver_phone_from_admin");
        driver_code = driver_details_accessor.getString("driver_code_from_admin");

        sbus_brand = driver_details_accessor.getString("bus_brand");
        sbus_code = driver_details_accessor.getString("bus_code");
        sbus_chasis_number = driver_details_accessor.getString("bus_chasis_no");
        sbus_model = driver_details_accessor.getString("bus_model");
        sbus_number = driver_details_accessor.getString("bus_number_plate");

        driverDetails_text = findViewById(R.id.driverdetails_text);
        school_details_text = findViewById(R.id.busdetails_text);
        driver_name_textview = findViewById(R.id.dname_text);
        driver_phone_number_textview  = findViewById(R.id.dphonenumber_text);
        driver_address_textview = findViewById(R.id.dlocation_text);
        bus_brand_textview = findViewById(R.id.bus_brand_text);
        bus_model_textview = findViewById(R.id.bus_model_text);
        bus_chasis_textview = findViewById(R.id.bus_chasis_number);
        bus_number_plate_textview = findViewById(R.id.bus_number_plate);
        the_drivername = findViewById(R.id.the_driver_name);
        the_driver_phone_number = findViewById(R.id.thedriver_number);
        the_driver_address = findViewById(R.id.thedriver_address);
        the_bus_brand = findViewById(R.id.the_bus_brand);
        the_bus_model = findViewById(R.id.the_bus_model);
        the_bus_chasis_number = findViewById(R.id.thebus_chasis);
        the_bus_numberplate = findViewById(R.id.the_bus_number_plate);

        next_button = findViewById(R.id.edit_button);
        success_message = findViewById(R.id.success_message);
        loading = findViewById(R.id.loading);

//        editexts
        driver_name_editText = findViewById(R.id.the_driver_name_editText);
        driver_address_editText = findViewById(R.id.the_driver_address_editText);
        driver_phone_number_editText = findViewById(R.id.the_driver_number_editText);

        busbrand_editText = findViewById(R.id.the_bus_brand_editText);
        bus_model_editText = findViewById(R.id.the_bus_model_editText);
        bus_chasis_number_editText = findViewById(R.id.the_chasis_no_editText);
        bus_number_plate_editText = findViewById(R.id.the_number_plate_editText);

        view_verification_dialogue = new Dialog(Driver_Details.this);

        //setting fonts
        driverDetails_text.setTypeface(lovelo);
        school_details_text.setTypeface(lovelo);
        driver_name_textview.setTypeface(lovelo);
        driver_phone_number_textview.setTypeface(lovelo);
        driver_address_textview.setTypeface(lovelo);
        bus_brand_textview.setTypeface(lovelo);
        bus_model_textview.setTypeface(lovelo);
        bus_chasis_textview.setTypeface(lovelo);
        bus_number_plate_textview.setTypeface(lovelo);

        //setting text
        the_drivername.setText(driver_fname +" "+ driver_lname);
        the_driver_phone_number.setText(driver_phone_);
        the_driver_address.setText(driver_address);
        the_bus_brand.setText(sbus_brand);
        the_bus_model.setText(sbus_model);
        the_bus_chasis_number.setText(sbus_chasis_number);
        the_bus_numberplate.setText(sbus_number);

        //setting onclick for edit button
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next_button.setText("SAVE");
                //making the textviews invisible
                the_drivername.setVisibility(View.GONE);
                the_driver_phone_number.setVisibility(View.GONE);
                the_driver_address.setVisibility(View.GONE);
                the_bus_brand.setVisibility(View.GONE);
                the_bus_model.setVisibility(View.GONE);
                the_bus_chasis_number.setVisibility(View.GONE);
                the_bus_numberplate.setVisibility(View.GONE);

                //making the editText visible
                driver_name_editText.setVisibility(View.VISIBLE);
                driver_phone_number_editText.setVisibility(View.VISIBLE);
                driver_address_editText.setVisibility(View.VISIBLE);
                busbrand_editText.setVisibility(View.VISIBLE);
                bus_model_editText.setVisibility(View.VISIBLE);
                bus_chasis_number_editText.setVisibility(View.VISIBLE);
                bus_number_plate_editText.setVisibility(View.VISIBLE);

                //setting text into the editText
                driver_name_editText.setText(driver_fname+ " " + driver_lname);
                driver_phone_number_editText.setText(driver_phone_);
                driver_address_editText.setText(driver_address);
                busbrand_editText.setText(sbus_brand);
                bus_model_editText.setText(sbus_model);
                bus_chasis_number_editText.setText(sbus_chasis_number);
                bus_number_plate_editText.setText(sbus_number);

                next_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        driver_name_from_etext = driver_name_editText.getText().toString().trim();
                        driver_phone_from_etext = driver_phone_number_editText.getText().toString().trim();
                        driver_address_from_etext = driver_address_editText.getText().toString().trim();
                        busbrand_fro_etext = busbrand_editText.getText().toString().trim();
                        bus_model_from_etext = bus_model_editText.getText().toString().trim();
                        bus_chasis_from_etext = bus_chasis_number_editText.getText().toString().trim();
                        bus_number_plage_etext = bus_number_plate_editText.getText().toString().trim();

                        if(!driver_name_from_etext.equals("") && !driver_phone_from_etext.equals("")
                                && !driver_address_from_etext.equals("") && !busbrand_fro_etext.equals("")
                        && !bus_model_from_etext.equals("") && !bus_chasis_from_etext.equals("") &&
                                !bus_number_plage_etext.equals("")){

                            if(isNetworkAvailable()) {
                                ShowVerificationDialogue(Driver_Details.this);
                             }else{
                                Toast.makeText(Driver_Details.this, "No internet connection",Toast.LENGTH_LONG).show();
                            }
                            }else{
                                loading.setVisibility(View.GONE);
                                success_message.setTextColor(getResources().getColor(R.color.red));
                                success_message.setText("Fields Required");
                                success_message.setVisibility(View.VISIBLE);
                            }
                    }
                });
            }
        });

    }

    private void ShowVerificationDialogue(FragmentActivity activity) {
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
                        updateDriver_bus_Values(driver_name_from_etext, driver_phone_from_etext, driver_address_from_etext,
                                busbrand_fro_etext, bus_model_from_etext, bus_chasis_from_etext, bus_number_plage_etext);
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

    private void updateDriver_bus_Values(String driver_name_from_etext, String driver_phone_from_etext,
                                         String driver_address_from_etext, String busbrand_fro_etext,
                                         String bus_model_from_etext, String bus_chasis_from_etext,
                                         String bus_number_plage_etext) {

        loading.setVisibility(View.VISIBLE);
        success_message.setVisibility(View.GONE);
        //update driver_details
        //spliting the name into first and last name
        String[] name_splited = driver_name_from_etext.split(" ");
        if (name_splited.length != 2)
            throw new IllegalArgumentException("String not in correct format");

        mdatabasereference = FirebaseDatabase.getInstance().getReference("drivers").child(school_code).child(driver_code);
        mdatabasereference.child("address").setValue(driver_address_from_etext);
        mdatabasereference.child("first_name").setValue(name_splited[0]);
        mdatabasereference.child("last_name").setValue(name_splited[1]);
        mdatabasereference.child("phone_number").setValue(driver_phone_from_etext);

        //update bus details
        mdatabasereference_bus = FirebaseDatabase.getInstance().getReference("bus_details").child(school_code).child(driver_code);
        mdatabasereference_bus.child("brand").setValue(busbrand_fro_etext);
        mdatabasereference_bus.child("chasis_no").setValue(bus_chasis_from_etext);
        mdatabasereference_bus.child("model").setValue(bus_model_from_etext);
        mdatabasereference_bus.child("number_plate").setValue(bus_number_plage_etext);
        loading.setVisibility(View.GONE);
        success_message.setVisibility(View.VISIBLE);
        success_message.setTextColor(getResources().getColor(R.color.green));
        success_message.setText("Update Complete");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
