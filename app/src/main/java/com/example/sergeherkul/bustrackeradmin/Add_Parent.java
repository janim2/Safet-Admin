package com.example.sergeherkul.bustrackeradmin;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Add_Parent extends AppCompatActivity {

    private TextView parentdetails_textView,parentfname_textView,parentlname_textView, parentaddress_textView,
    parentemail_textView,parentphone_number_textView, childDetials_text, child_assignment_text,
            add_child_text, success_message;

    private EditText parent_fname_editText, parent_lname_edittext, parent_address_editText,
            parent_email_editText, parent_phone_number_editText;

    private ProgressBar loading;
    private Typeface lovelo;
    private Button add_parent;
    private int counter = 0;
    private String pfname, plname, p_location, p_email, pphone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__parent);

        lovelo = Typeface.createFromAsset(getAssets(),  "fonts/lovelo.ttf");

        getSupportActionBar().setTitle("Add Parent");
        //textviews
        parentdetails_textView = findViewById(R.id.parentdetails_text);
        parentfname_textView = findViewById(R.id.fname_text);
        parentlname_textView = findViewById(R.id.lname_text);
        parentaddress_textView = findViewById(R.id.location_text);
        parentemail_textView = findViewById(R.id.email_text);
        parentphone_number_textView = findViewById(R.id.phonenumber_text);
        childDetials_text = findViewById(R.id.childdetails_text);
        child_assignment_text = findViewById(R.id.child_assignment_text);
        add_child_text = findViewById(R.id.add_child_text);
        success_message = findViewById(R.id.success_message);

        //edittexts
        parent_fname_editText = findViewById(R.id.the_parent_fname_editText);
        parent_lname_edittext = findViewById(R.id.the_parent_lname_editText);
        parent_address_editText = findViewById(R.id.the_parent_address_editText);
        parent_email_editText = findViewById(R.id.the_email_editText);
        parent_phone_number_editText = findViewById(R.id.the_parent_number_editText);
        loading = findViewById(R.id.loading);
        add_parent = findViewById(R.id.add_button);

        //setting the font
        parentdetails_textView.setTypeface(lovelo);
        parentfname_textView .setTypeface(lovelo);
        parentlname_textView .setTypeface(lovelo);
        parentaddress_textView.setTypeface(lovelo);
        parentemail_textView .setTypeface(lovelo);
        parentphone_number_textView.setTypeface(lovelo);
        childDetials_text.setTypeface(lovelo);
        child_assignment_text.setTypeface(lovelo);
        add_child_text.setTypeface(lovelo);
        success_message.setTypeface(lovelo);
        add_parent.setTypeface(lovelo);

        add_child_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter += 1;
                addChildLayout();
            }
        });

        add_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pfname = parent_fname_editText.getText().toString().trim();
                plname = parent_lname_edittext.getText().toString().trim();
                p_location = parent_address_editText.getText().toString().trim();
                p_email = parent_email_editText.getText().toString().trim();
                pphone_number = parent_phone_number_editText.getText().toString().trim();
            }
        });

    }

    public void addChildLayout(){
        //Inflater service
        LayoutInflater layoutInfralte=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //parent layout xml refrence
        final LinearLayout linearLayout=(LinearLayout)findViewById(R.id.parent_layout);
        //Child layout xml refrence
        final View view=layoutInfralte.inflate(R.layout.child_knowables, null);

        final TextView childcount = view.findViewById(R.id.child_count);
        childcount.setText("Child " + counter+"");

        childcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.removeView(view);
                counter -= 1;
            }
        });
        //add child to parent
        linearLayout.addView(view);
    }
}
