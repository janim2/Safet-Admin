package com.safet.admin.bustrackeradmin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
import java.util.Random;

public class RegisterSchool extends AppCompatActivity {
    private ImageView gobackImage;
    private EditText school_code, school_name, school_address, school_phone_number, school_email,
    school_password, confirm_password, coupon_code_editText;
    private Button done_button;
    private String s_code,s_name, s_address, s_phone_number,s_email,
            s_password,s_confirm_password, coupon_code;
    private ProgressBar loading;
    private TextView success_message,school_code_text,check_school_code, request_coupon_code;
    private String[] capital_letters = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O",
    "P","Q","R","s","T","U","V","W","X","Y","Z"};
    private String[] small_letters = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p",
    "q","r","s","t","u","v","w","x","y","z"};
    private FirebaseAuth auth;
    private DatabaseReference mdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_school);

        Typeface lovelo =Typeface.createFromAsset(getAssets(),  "fonts/lovelo.ttf");

        gobackImage = findViewById(R.id.back);
        school_code = findViewById(R.id.school_code);
        school_name = findViewById(R.id.school_name);
        school_address = findViewById(R.id.school_address);
        school_phone_number = findViewById(R.id.school_phone_number);
        school_email = findViewById(R.id.school_email);
        school_password = findViewById(R.id.passcode);
        confirm_password = findViewById(R.id.confirm_passcode);
        done_button = findViewById(R.id.done_button);
        loading = findViewById(R.id.loading);
        success_message = findViewById(R.id.success_message);
        school_code_text = findViewById(R.id.school_details_text);
        check_school_code = findViewById(R.id.school_code_check);
        coupon_code_editText = findViewById(R.id.coupon_code_editText);
        request_coupon_code = findViewById(R.id.request_coupon_code);

        done_button.setTypeface(lovelo);
        success_message.setTypeface(lovelo);
        school_code_text.setTypeface(lovelo);
        check_school_code.setTypeface(lovelo);

        auth = FirebaseAuth.getInstance();

        school_name.requestFocus();

        Random random = new Random();
        Integer d = random.nextInt(89) + 10;
        Integer capital_letterfinder = random.nextInt(26);
        Integer small_letterfinder = random.nextInt(26);
        s_code = capital_letters[capital_letterfinder] + d+"" + small_letters[small_letterfinder];

        school_code.setText(s_code);

        gobackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        request_coupon_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                if (isNetworkAvailable()){
                    s_email = school_email.getText().toString().trim();
                    if(!s_email.equals("")){
                        new Sending_mail("https://iamjesse75.000webhostapp.com/Send_emails_only.php",
                                "iamjesse75@gmail.com","Request for Coupon code",
                                s_email+" has asked for a coupon code. Waiting to hear from you shortly","safet").execute();
                    }else{
                        loading.setVisibility(View.GONE);
                        success_message.setVisibility(View.VISIBLE);
                        success_message.setTextColor(getResources().getColor(R.color.red));
                        success_message.setText("Email required for code request");
                    }
                }else{
                    loading.setVisibility(View.GONE);
                    success_message.setVisibility(View.VISIBLE);
                    success_message.setTextColor(getResources().getColor(R.color.red));
                    success_message.setText("No internet connection");
                }

            }
        });
        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_code = school_code.getText().toString().trim();
                s_name = school_name.getText().toString().trim();
                s_address = school_address.getText().toString().trim();
                s_phone_number = school_phone_number.getText().toString().trim();
                s_email = school_email.getText().toString().trim();
                s_password = school_password.getText().toString().trim();
                s_confirm_password = confirm_password.getText().toString().trim();
                coupon_code = coupon_code_editText.getText().toString().trim();
                if(s_confirm_password.equals(s_password)){
                    if(!s_code.equals("")){
                        if(!s_name.equals("") && !s_address.equals("") && !s_phone_number.equals("")
                        && !s_email.equals("") && !s_password.equals("") && !s_confirm_password.equals("")){
                            if(s_password.length() > 6){
                                if(s_email.indexOf("@") > 1 || s_email.lastIndexOf(".") - s_email.indexOf("@") > 2){
                                    if(isNetworkAvailable()) {
                                        Register_School(s_code, s_name, s_address, s_phone_number, s_email, s_password, coupon_code);
                                    }else{
                                        Toast.makeText(RegisterSchool.this,"No internet connection",Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    success_message.setVisibility(View.VISIBLE);
                                    success_message.setTextColor(getResources().getColor(R.color.red));
                                    success_message.setText("Enter valid email");
                                }

                            }else{
                                success_message.setVisibility(View.VISIBLE);
                                success_message.setTextColor(getResources().getColor(R.color.red));
                                success_message.setText("Password must be greater than 6");
                            }
                        }else{
                            success_message.setVisibility(View.VISIBLE);
                            success_message.setTextColor(getResources().getColor(R.color.red));
                            success_message.setText("All fields required");
                        }
                    }else{
                        Random random = new Random();
                        Integer d = random.nextInt(99);
                        s_code = d+"";
                        school_code.setText(s_code);
                    }
                }else{
                    success_message.setVisibility(View.VISIBLE);
                    success_message.setTextColor(getResources().getColor(R.color.red));
                    success_message.setText("Password Mismatch");
                }
            }
        });
    }

    private void Register_School(final String school_code, final String school_name, final String school_address,
                                 final String school_phone_number, final String school_email, String school_password, final String coupon_code) {
        loading.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(school_email, school_password)
                .addOnCompleteListener(RegisterSchool.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
//                                Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
//                            Toast.makeText(RegisterSchool.this, "Authentication failed." + task.getException(),
//                                    Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                            success_message.setVisibility(View.VISIBLE);
                            success_message.setTextColor(getResources().getColor(R.color.red));
                            success_message.setText("Registration failed");
                        } else {
                            if(!coupon_code.equals("")){
                                Query cross_checkCoupon = FirebaseDatabase.getInstance().getReference("coupon_code")
                                       .orderByChild("code").equalTo(coupon_code);
                                cross_checkCoupon.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                                String key = ds.getKey();
                                                mdatabase = FirebaseDatabase.getInstance().getReference("schools").child(school_code);
                                                mdatabase.child("name").setValue(school_name);
                                                mdatabase.child("email").setValue(school_email);
                                                mdatabase.child("location").setValue(school_address);
                                                mdatabase.child("telephone").setValue(school_phone_number);
                                                mdatabase.child("language").setValue("");
                                                mdatabase.child("range").setValue("");
                                                mdatabase.child("mission").setValue("");
                                                mdatabase.child("vision").setValue("");
                                                mdatabase.child("admission_status").setValue("");
                                                mdatabase.child("mechanic_number").setValue("");

                                                //removing coupon code from database
                                                FirebaseDatabase.getInstance().getReference("coupon_code").child(key).removeValue();

                                                //send email
                                                new Sending_mail("https://iamjesse75.000webhostapp.com/Send_emails_only.php",
                                                        school_email,"Welcome to the Safet Family",
                                                        s_email+" has been added to our system as a school. We hope you are as happy as we are to get started" +
                                                                "on the journey of keeping children safe. \nSchool codes help you tremendously in using the system" +
                                                                "so please keep yours safe. Your school code is " + school_code+" .Feel free to contact us when " +
                                                                "you have any unanswered questions.","safet").execute();

                                                addToNotifications();
                                                loading.setVisibility(View.GONE);
                                                success_message.setVisibility(View.VISIBLE);
                                                success_message.setTextColor(getResources().getColor(R.color.green));
                                                success_message.setText("Registration successful");
                                                FirebaseAuth.getInstance().signOut();
                                                Intent goToLogin  = new Intent(RegisterSchool.this, Admin_Login.class);
                                                goToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(goToLogin);
                                            }
                                        }else{
                                            loading.setVisibility(View.GONE);
                                            success_message.setVisibility(View.VISIBLE);
                                            success_message.setTextColor(getResources().getColor(R.color.red));
                                            success_message.setText("Coupon code invalid");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }else{
                                loading.setVisibility(View.GONE);
                                success_message.setVisibility(View.VISIBLE);
                                success_message.setTextColor(getResources().getColor(R.color.red));
                                success_message.setText("Coupon code required");
                            }
                        }
                    }
                });
    }

    private void addToNotifications() {
        try {
            Random random = new Random();
            int a = random.nextInt(987654);
            String notificationID = "notification" + a+"";
            mdatabase = FirebaseDatabase.getInstance().getReference("school_notifications").child(s_code).child(notificationID);
            mdatabase.child("image").setValue("WM");
            mdatabase.child("message").setValue("Welcome to Safet. The most secure bus tracker system for your school. Your kids safety is our number one concern");
            mdatabase.child("title").setValue("Welcome to Safet");
            mdatabase.child("time").setValue(new Date().toString());
        }catch (NullPointerException e){

        }
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
            if (s.equals("Done")){
                loading.setVisibility(View.GONE);
                success_message.setVisibility(View.VISIBLE);
                success_message.setTextColor(getResources().getColor(R.color.red));
                success_message.setText("Request sent");
            }else{
                loading.setVisibility(View.GONE);
                success_message.setVisibility(View.VISIBLE);
                success_message.setTextColor(getResources().getColor(R.color.red));
                success_message.setText("Sending failed. Try Again Later");
            }
        }

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
