package com.example.sergeherkul.bustrackeradmin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Random;

public class RegisterSchool extends AppCompatActivity {
    private ImageView gobackImage;
    private EditText school_code, school_name, school_address, school_phone_number, school_email,
    school_password, confirm_password;
    private Button done_button;
    private String s_code,s_name, s_address, s_phone_number,s_email,s_password,s_confirm_password;
    private ProgressBar loading;
    private TextView success_message,school_code_text,check_school_code;
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

        done_button.setTypeface(lovelo);
        success_message.setTypeface(lovelo);
        school_code_text.setTypeface(lovelo);
        check_school_code.setTypeface(lovelo);

        auth = FirebaseAuth.getInstance();

        school_name.requestFocus();

        Random random = new Random();
        Integer d = random.nextInt(99);
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

                if(s_confirm_password.equals(s_password)){
                    if(!s_code.equals("")){
                        if(!s_name.equals("") && !s_address.equals("") && !s_phone_number.equals("")
                        && !s_email.equals("") && !s_password.equals("") && !s_confirm_password.equals("")){
                            if(isNetworkAvailable()) {
                                Register_School(s_code, s_name, s_address, s_phone_number, s_email, s_password);
                            }else{
                                Toast.makeText(RegisterSchool.this,"No internet connection",Toast.LENGTH_LONG).show();
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
                                    final String school_phone_number, final String school_email, String school_password) {
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
                            mdatabase = FirebaseDatabase.getInstance().getReference("schools").child(school_code);
                            mdatabase.child("name").setValue(school_name);
                            mdatabase.child("email").setValue(school_email);
                            mdatabase.child("location").setValue(school_address);
                            mdatabase.child("telephone").setValue(school_phone_number);
                            mdatabase.child("language").setValue("");
                            mdatabase.child("range").setValue("");
                            mdatabase.child("mission").setValue("");
                            mdatabase.child("vision").setValue("");

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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
