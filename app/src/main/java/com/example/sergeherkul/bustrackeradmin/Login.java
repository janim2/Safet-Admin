package com.safet.admin.bustrackeradmin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private TextView logoname, login_text, forgot_password_text, register_school_text;
    private ImageView forward;
    private EditText school_email, password;
    private FirebaseAuth mauth;
    private TextView success_message;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Typeface lovelo =Typeface.createFromAsset(getAssets(),  "fonts/lovelo.ttf");

        mauth = FirebaseAuth.getInstance();

        logoname = findViewById(R.id.logo_name);
        login_text = findViewById(R.id.login_text);
        forgot_password_text = findViewById(R.id.forgot_password);
        register_school_text = findViewById(R.id.register_text);
        forward = findViewById(R.id.forward);
        school_email = findViewById(R.id.school_email);
        password = findViewById(R.id.password);
        success_message = findViewById(R.id.success_message);
        loading = findViewById(R.id.loading);

        logoname.setTypeface(lovelo);
        login_text.setTypeface(lovelo);
        forgot_password_text.setTypeface(lovelo);
        register_school_text.setTypeface(lovelo);

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sschool_email = school_email.getText().toString().trim();
                String spassword = password.getText().toString().trim();

                if(!sschool_email.equals("")){
                    if(!spassword.equals("")){
                        if(isNetworkAvailable()){
                            loading.setVisibility(View.VISIBLE);
                            success_message.setVisibility(View.GONE);
                            mauth.signInWithEmailAndPassword(sschool_email,spassword).addOnCompleteListener(Login.this,new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful()){
                                        loading.setVisibility(View.GONE);
                                        success_message.setVisibility(View.VISIBLE);
                                        success_message.setTextColor(getResources().getColor(R.color.red));
                                        success_message.setText("Login failed");
                                    }else{
                                        loading.setVisibility(View.GONE);
                                        success_message.setVisibility(View.VISIBLE);
                                        success_message.setTextColor(getResources().getColor(R.color.green));
                                        success_message.setText("Login successful");
                                        Intent gotoVerification = new Intent(Login.this,Verify_School.class);
//                                        gotoVerification.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(gotoVerification);
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(Login.this,"No internet connection",Toast.LENGTH_LONG).show();
                        }
                    }else{
                            password.setError("Required");
                        }
                }else{
                    school_email.setError("Required");
                }
            }
        });

        register_school_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,RegisterSchool.class));
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
