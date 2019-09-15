package com.example.sergeherkul.bustrackeradmin;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    private TextView logoname, login_text, forgot_password_text, register_school_text;
    private ImageView forward;
    private EditText school_id, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Typeface lovelo =Typeface.createFromAsset(getAssets(),  "fonts/lovelo.ttf");

        logoname = findViewById(R.id.logo_name);
        login_text = findViewById(R.id.login_text);
        forgot_password_text = findViewById(R.id.forgot_password);
        register_school_text = findViewById(R.id.register_text);
        forward = findViewById(R.id.forward);
        school_id = findViewById(R.id.school_id);
        password = findViewById(R.id.password);

        logoname.setTypeface(lovelo);
        login_text.setTypeface(lovelo);
        forgot_password_text.setTypeface(lovelo);
        register_school_text.setTypeface(lovelo);

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sschool_id = school_id.getText().toString().trim();
                String spassword = password.getText().toString().trim();

                if(!sschool_id.equals("")){
                    if(!spassword.equals("")){

                    }else{
                        password.setError("Required");
                    }
                }else{
                    school_id.setError("Required");
                }
            }
        });

    }
}
