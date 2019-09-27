package com.example.sergeherkul.bustrackeradmin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class Enter_phone_number extends AppCompatActivity {

    private Button next;
    private TextView enter_phone_numberText, verification_no_message,number_sent_success, failed_message_text;
    private EditText user_phone_number,code_one,code_two,code_three,code_four, code_five, code_six;
    private ProgressBar loading, verifying_loading;
    private ImageView back;
    private CountryCodePicker country_picker;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_phone_number);

        next = findViewById(R.id.next_button);
        enter_phone_numberText = findViewById(R.id.enter_number_text);
        verification_no_message = findViewById(R.id.verification_no_message);
        code_one = findViewById(R.id.code_number_one);
        code_two = findViewById(R.id.code_number_two);
        code_three = findViewById(R.id.code_number_three);
        code_four = findViewById(R.id.code_number_four);
        code_five = findViewById(R.id.code_number_five);
        code_six = findViewById(R.id.code_number_six);
        number_sent_success = findViewById(R.id.code_sent_message);
        loading = findViewById(R.id.loading);
        verifying_loading = findViewById(R.id.verifying_loading);
        back = findViewById(R.id.back);
        user_phone_number = findViewById(R.id.user_phone_number);
        failed_message_text = findViewById(R.id.failed_message);
        country_picker = findViewById(R.id.country_code_picker);

        mAuth = FirebaseAuth.getInstance();

        Typeface lovelo =Typeface.createFromAsset(getAssets(),  "fonts/lovelo.ttf");

        next.setTypeface(lovelo);
        enter_phone_numberText.setTypeface(lovelo);
        verification_no_message.setTypeface(lovelo);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_number = user_phone_number.getText().toString().trim();
                if(!phone_number.equals("")){
                    String countryCode = country_picker.getSelectedCountryCodeWithPlus();
                    if(isNetworkAvailable()){
                        sendVerificationMessage(countryCode,phone_number);
                    }else{
                        Toast.makeText(Enter_phone_number.this,"No internet connection",Toast.LENGTH_LONG).show();
                    }
                }else{
                    user_phone_number.setError("Required");
                }
            }
        });

        user_phone_number.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

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
                    code_five.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        code_five.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(code_five.getText().toString().length()==1)     //size as per your requirement
                {
                   code_six.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        code_six.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(code_six.getText().toString().length()==1)     //size as per your requirement
                {
//                    dothelogin();
                    next.setText("NEXT");
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String thecode = code_one.getText().toString()+code_two.getText().toString()+
                                    code_three.getText().toString()+code_four.getText().toString()+code_five.getText().toString()+
                                    code_six.getText().toString();
                            verifyPhoneNumberWithCode(mVerificationId,thecode);
                        }
                    });
//                    next.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            startActivity(new Intent(Enter_phone_number.this,Driver_Verification.class));
//                        }
//                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void verifyPhoneNumberWithCode(String verificationId,String theuserentered_code){
        verifying_loading.setVisibility(View.VISIBLE);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, theuserentered_code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            verifying_loading.setVisibility(View.GONE);
                            startActivity(new Intent(Enter_phone_number.this,Driver_Verification.class));

                        } else {
                            // Sign in failed, display a message and update the UI
                            verifying_loading.setVisibility(View.GONE);
                            failed_message_text.setVisibility(View.VISIBLE);
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid

                            }
                        }
                    }
                });
    }

    private void sendVerificationMessage(String countrycode, String number) {
        String formated_number = number.replace("(","");
        String formated_number2 = formated_number.replace(")","");
        String formated_number3 = formated_number2.replace("-","");
        String phoneNumber  = countrycode+formated_number3.replace(" ","");
//        Toast.makeText(Enter_phone_number.this,phoneNumber,Toast.LENGTH_LONG).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacksPhoneAuthActivity.java
        loading.setVisibility(View.VISIBLE);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verificaiton without
            //     user action.
//                signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            loading.setVisibility(View.GONE);


            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request

            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded

            }

            // Show a message and update the UI

        }

        @Override
        public void onCodeSent(String verificationId,
                               PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.

            loading.setVisibility(View.GONE);
            number_sent_success.setVisibility(View.VISIBLE);
            code_one.setEnabled(true);
            code_two.setEnabled(true);
            code_three.setEnabled(true);
            code_four.setEnabled(true);
            code_five.setEnabled(true);
            code_six.setEnabled(true);
            next.setText("VERIFY CODE");
            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!code_one.equals("") && !code_two.equals("") && !code_three.equals("") && !code_four.equals("") && !code_five.equals("") && !code_six.equals("")){

                    }else{
                        Toast.makeText(Enter_phone_number.this,"Code Required",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    };

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
