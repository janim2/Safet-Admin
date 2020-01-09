package com.safet.admin.bustrackeradmin;

import android.content.Context;
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
import android.widget.TextClock;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_password extends AppCompatActivity {
    private TextView success_message, no_internet,cancel_textView;
    private EditText email_editText;
    private Button done_button;
    private ProgressBar loading;
    private ImageView goBack;
    private String email_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        goBack = findViewById(R.id.go_back);
        success_message = findViewById(R.id.success_message);
        cancel_textView = findViewById(R.id.cancel);
        email_editText = findViewById(R.id.school_email);
        done_button = findViewById(R.id.request_button);
        loading = findViewById(R.id.loading);
        no_internet = findViewById(R.id.no_internet);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cancel_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_string = email_editText.getText().toString().trim();
                if(!email_string.equals("")){
                    if(isNetworkAvailable()){
                        loading.setVisibility(View.VISIBLE);
                        success_message.setVisibility(View.GONE);
                        no_internet.setVisibility(View.GONE);
                        FirebaseAuth.getInstance().sendPasswordResetEmail(email_string)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            loading.setVisibility(View.GONE);
                                            no_internet.setVisibility(View.GONE);
                                            success_message.setText("Done.\nCheck email for password reset instructions");
                                            success_message.setVisibility(View.VISIBLE);
                                        }else{
                                            loading.setVisibility(View.GONE);
                                            no_internet.setVisibility(View.GONE);
                                            success_message.setText("Email isn't registered with safet");
                                            success_message.setVisibility(View.VISIBLE);
                                        }
                                    }
                                });
                    }else{
                        loading.setVisibility(View.GONE);
                        no_internet.setVisibility(View.VISIBLE);
                        success_message.setVisibility(View.GONE);
                    }
                }else{
                    loading.setVisibility(View.GONE);
                    no_internet.setVisibility(View.GONE);
                    success_message.setText("Email required");
                    success_message.setVisibility(View.VISIBLE);
                }
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
