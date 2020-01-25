package com.safet.admin.bustrackeradmin;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class Add_driver extends AppCompatActivity {

    private TextView driverDetails_text, school_details_text, driver_fname_textview,driver_lname_textview,
            driver_phone_number_textview,driver_email_textview, driver_address_textview, bus_assignment_text,bus_brand_textview,
            bus_model_textview, bus_chasis_textview, bus_number_plate_textview,
            success_message;

    private EditText driver_fname_editText,driver_lname_editText,driver_phone_number_editText,
            driver_email_editText,driver_address_editText,
            busbrand_editText, bus_model_editText, bus_chasis_number_editText, bus_number_plate_editText,
            passenger_capacity, bus_route;

    private Button done_button;
    private ProgressBar loading;
    private Typeface lovelo;

    private String school_code,string_school_name,
            driver_lname_from_etext,driver_fname_from_etext,driver_phone_from_etext,
            driver_email_from_etext,driver_address_from_etext,
            busbrand_from_etext,bus_model_from_etext,bus_chasis_from_etext,bus_number_plate_etext,
            passenger_capacity_from_etext, bus_route_from_etext, school_email;

    private Dialog view_verification_dialogue;
    private DatabaseReference add_driver_databaseReference, add_bus_databaseReference,add_notification_databaseReference;
    private Accessories add_driver_accessor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver);
        getSupportActionBar().setTitle("Add Driver");

        lovelo =Typeface.createFromAsset(getAssets(),  "fonts/lovelo.ttf");

        view_verification_dialogue = new Dialog(Add_driver.this);
        add_driver_accessor = new Accessories(Add_driver.this);

        school_email = add_driver_accessor.getString("school_email");

        driverDetails_text = findViewById(R.id.driverdetails_text);
        school_details_text = findViewById(R.id.busdetails_text);
        driver_fname_textview = findViewById(R.id.dname_text);
        driver_lname_textview = findViewById(R.id.lname_text);
        driver_phone_number_textview  = findViewById(R.id.dphonenumber_text);
        driver_email_textview  = findViewById(R.id.demail_text);
        driver_address_textview = findViewById(R.id.dlocation_text);
        bus_assignment_text = findViewById(R.id.bus_assignment_text);
        bus_brand_textview = findViewById(R.id.bus_brand_text);
        bus_model_textview = findViewById(R.id.bus_model_text);
        bus_chasis_textview = findViewById(R.id.bus_chasis_number);
        bus_number_plate_textview = findViewById(R.id.bus_number_plate);

        done_button = findViewById(R.id.add_button);
        success_message = findViewById(R.id.success_message);
        loading = findViewById(R.id.loading);

//        editexts
        driver_fname_editText = findViewById(R.id.the_driver_fname_editText);
        driver_lname_editText = findViewById(R.id.the_driver_lname_editText);
        driver_address_editText = findViewById(R.id.the_driver_address_editText);
        driver_phone_number_editText = findViewById(R.id.the_driver_number_editText);
        driver_email_editText = findViewById(R.id.the_email_editText);

        busbrand_editText = findViewById(R.id.the_bus_brand_editText);
        bus_model_editText = findViewById(R.id.the_bus_model_editText);
        bus_chasis_number_editText = findViewById(R.id.the_chasis_no_editText);
        bus_number_plate_editText = findViewById(R.id.the_number_plate_editText);
        passenger_capacity = findViewById(R.id.passenger_capacity);
        bus_route = findViewById(R.id.bus_route);

        //setting fonts
        driverDetails_text.setTypeface(lovelo);
        school_details_text.setTypeface(lovelo);
        driver_fname_textview.setTypeface(lovelo);
        driver_lname_textview.setTypeface(lovelo);
        driver_phone_number_textview.setTypeface(lovelo);
        driver_email_textview.setTypeface(lovelo);
        driver_address_textview.setTypeface(lovelo);
        bus_assignment_text.setTypeface(lovelo);
        bus_brand_textview.setTypeface(lovelo);
        bus_model_textview.setTypeface(lovelo);
        bus_chasis_textview.setTypeface(lovelo);
        bus_number_plate_textview.setTypeface(lovelo);

        school_code = add_driver_accessor.getString("school_code");
        string_school_name = add_driver_accessor.getString("school_name");

        //setting an onclick for the done button
        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driver_fname_from_etext = driver_fname_editText.getText().toString().trim();
                driver_lname_from_etext = driver_lname_editText.getText().toString().trim();
                driver_phone_from_etext = driver_phone_number_editText.getText().toString().trim();
                driver_address_from_etext = driver_address_editText.getText().toString().trim();
                driver_email_from_etext = driver_email_editText.getText().toString().trim();
                busbrand_from_etext = busbrand_editText.getText().toString().trim();
                bus_model_from_etext = bus_model_editText.getText().toString().trim();
                bus_chasis_from_etext = bus_chasis_number_editText.getText().toString().trim();
                bus_number_plate_etext = bus_number_plate_editText.getText().toString().trim();
                passenger_capacity_from_etext = passenger_capacity.getText().toString().trim();
                bus_route_from_etext = bus_route.getText().toString().trim();
                if(!driver_fname_from_etext.equals("") && !driver_lname_from_etext.equals("") &&
                        !driver_phone_from_etext.equals("") && !driver_address_from_etext.equals("") &&
                        /*!driver_email_from_etext.equals("") &&*/ !busbrand_from_etext.equals("") && !bus_model_from_etext.equals("") &&
                        !bus_chasis_from_etext.equals("") && !bus_number_plate_etext.equals("") && !passenger_capacity_from_etext.equals("") && !bus_route_from_etext.equals("")){
                    if(isNetworkAvailable()){
                        showSchoolVerifyPopup(Add_driver.this);
                    }else{
                        Toast.makeText(Add_driver.this,"No internet connection",Toast.LENGTH_LONG).show();
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
                        addDriverToDatabase(driver_fname_from_etext,driver_lname_from_etext,driver_email_from_etext,driver_phone_from_etext,
                                driver_address_from_etext,busbrand_from_etext, bus_model_from_etext, bus_chasis_from_etext,
                                bus_number_plate_etext,passenger_capacity_from_etext,bus_route_from_etext);
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

    private void addDriverToDatabase(String driver_fname_from_etext, String driver_lname_from_etext,
                                     String driver_email,String driver_phone_from_etext,String driver_address_from_etext,
                                     String busbrand_from_etext, String bus_model_from_etext,
                                     String bus_chasis_from_etext, String bus_number_plate_etext,
                                     String passenger_capacity,String bus_route) {
        loading.setVisibility(View.VISIBLE);
        success_message.setVisibility(View.GONE);

        Random add_d_ = new Random();
        int aa = add_d_.nextInt(999);
        String driver_id = "dR" + aa+"";
        String bus_code = "Bus" + aa+"";
        String assigned_code = "assign"+aa+"";

        //adding driver to database
        add_driver_databaseReference = FirebaseDatabase.getInstance().getReference("drivers").child(school_code).child(driver_id);
        add_driver_databaseReference.child("first_name").setValue(driver_fname_from_etext);
        add_driver_databaseReference.child("last_name").setValue(driver_lname_from_etext);
        add_driver_databaseReference.child("email").setValue(driver_email);
        add_driver_databaseReference.child("address").setValue(driver_address_from_etext);
        add_driver_databaseReference.child("phone_number").setValue(driver_phone_from_etext);

        //adding bus to driver
        add_bus_databaseReference = FirebaseDatabase.getInstance().getReference("bus_details").child(school_code).child(driver_id);
        add_bus_databaseReference.child("brand").setValue(busbrand_from_etext);
        add_bus_databaseReference.child("bus_code").setValue(bus_code);
        add_bus_databaseReference.child("chasis_no").setValue(bus_chasis_from_etext);
        add_bus_databaseReference.child("model").setValue(bus_model_from_etext);
        add_bus_databaseReference.child("number_plate").setValue(bus_number_plate_etext);
        add_bus_databaseReference.child("bus_capacity").setValue(passenger_capacity);
        add_bus_databaseReference.child("assigned_no_left").setValue(passenger_capacity);
        add_bus_databaseReference.child("bus_route").setValue(bus_route);

//        DatabaseReference bus_assigned_children = FirebaseDatabase.getInstance().getReference("bus_assignment").child(school_code).child(driver_id).child(assigned_code);
//        bus_assigned_children.child("number_remaining").setValue(passenger_capacity);

        addToNotifications(FirebaseAuth.getInstance().getCurrentUser());

        loading.setVisibility(View.GONE);
        success_message.setVisibility(View.VISIBLE);
        success_message.setTextColor(getResources().getColor(R.color.green));
        success_message.setText("Driver addition successful");
        new Sending_mail("https://iamjesse75.000webhostapp.com/Send_emails_only.php"
                ,school_email,"Driver Addition Successful","Thank you for chosing Safet as your school bus safety monitoring service." +
                " We have noticed the addition of a driver by name " + driver_fname_from_etext + " " + driver_lname_from_etext +
                " He has been added successfully to our system and would thus be identified by the code " + driver_id + " in relation to your school.\n" +
                "Please provide this code to Mr." + driver_fname_from_etext + " as he would need it to operate. Thank you","safet").execute();
        Toast.makeText(Add_driver.this, "Driver addition successful", Toast.LENGTH_LONG).show();

    }

    private void addToNotifications(FirebaseUser currentUser) {
        Random notifyrandom = new Random();
        int notify_no = notifyrandom.nextInt(233434);
        String notify_id = "notification" + notify_no+"";
        add_notification_databaseReference = FirebaseDatabase.getInstance().getReference("school_notifications").child(school_code).child(notify_id);
        add_notification_databaseReference.child("image").setValue("AD");
        add_notification_databaseReference.child("message").setValue("We have noticed an addition of a driver to your school, "+string_school_name
                +". If this activity wasn't you, contact customer care for rectification.");
        add_notification_databaseReference.child("time").setValue(new Date());
        add_notification_databaseReference.child("title").setValue("Driver Added");
    }


    class Sending_mail extends AsyncTask<Void, Void, String> {

        String url_location,to,subject,message,header_begin ;

        public Sending_mail(String url_location,String to, String subject, String message, String header_begin) {
            this.url_location = url_location;
            this.to = to;
            this.subject = subject;
            this.message = message;
            this.header_begin = header_begin;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            try {
                URL url = new URL(url_location);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String data = URLEncoder.encode("to", "UTF-8") + "=" + URLEncoder.encode(to, "UTF-8") + "&" +
                        URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subject, "UTF-8") + "&" +
                        URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8") + "&" +
                        URLEncoder.encode("header_begin", "UTF-8") + "=" + URLEncoder.encode(header_begin, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String fetch;
                while ((fetch = bufferedReader.readLine()) != null) {
                    stringBuffer.append(fetch);
                }
                String string = stringBuffer.toString();
                inputStream.close();
                return string;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            return "please check internet connection";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
