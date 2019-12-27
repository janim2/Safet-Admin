package com.example.sergeherkul.bustrackeradmin;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sergeherkul.bustrackeradmin.Adapters.Facilities_Adapter;
import com.example.sergeherkul.bustrackeradmin.Adapters.ImagesAdapter;
import com.example.sergeherkul.bustrackeradmin.Adapters.NotifyAdapter;
import com.example.sergeherkul.bustrackeradmin.Model.Facilities;
import com.example.sergeherkul.bustrackeradmin.Model.Images;
import com.example.sergeherkul.bustrackeradmin.Model.Notify;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class Admin_Profile extends AppCompatActivity {

    private Button edit_button;
    private TextView school_name_text, school_email_text, school_number_text,
    school_location_text,school_name_text_value, school_email_text_value, school_number_text_value,
            school_location_text_value;
    private EditText school_name_editText, school_email_editText, school_number_editText,
            school_location_editText, language_editText, range_editText, mission_editText, vision_editText;
    private String school_code,string_school_name,string_school_email, string_school_number,
            string_school_location, language_string, range_string, mission_string, vision_string,
            admission_string, admit_string;
    private Accessories profileaccessor;
    private TextView success_message, language_text, range_text, mission_text, vision_text, admission_status_text;
    private ProgressBar loading;
    private DatabaseReference databaseReference;
    private Dialog view_verification_dialogue;
    private Typeface lovelo;
    private Spinner admission_status_spinner;

    //facilities items
    private ArrayList facilitiesArray = new ArrayList<Facilities>();
    private RecyclerView facilities_RecyclerView;
    private RecyclerView.Adapter facilities_Adapter;
    private String facility_id,facility_name, facility_image;
    private TextView no_facilities, facilies_no_internet;

    private TextView edit_facilities;

    //images items
    private ArrayList imagesArray = new ArrayList<Images>();
    private RecyclerView images_RecyclerView;
    private RecyclerView.Adapter images_Adapter;
    private String images_id, images_image;
    private TextView no_images, images_no_internet, edit_images;

    String[] admission_state = {"OPEN","CLOSE"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        getSupportActionBar().setTitle("Admin_Profile");

        lovelo =Typeface.createFromAsset(getAssets(),  "fonts/lovelo.ttf");

        profileaccessor = new Accessories(Admin_Profile.this);
        view_verification_dialogue = new Dialog(Admin_Profile.this);

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

//        other details declaration
        admission_status_text = findViewById(R.id.admission_status_text);
        admission_status_spinner = findViewById(R.id.admission_status_spinner);
        language_text = findViewById(R.id.language_text);
        range_text = findViewById(R.id.range_text);
        mission_text = findViewById(R.id.mission_text);
        vision_text = findViewById(R.id.vision_text);

        language_editText = findViewById(R.id.language_editText);
        range_editText = findViewById(R.id.range_editText);
        mission_editText = findViewById(R.id.mission_editText);
        vision_editText = findViewById(R.id.vision_editText);

        edit_facilities = findViewById(R.id.edit_facility);

//        facilities initializations
        facilities_RecyclerView = findViewById(R.id.facilities_recyclerView);
        facilies_no_internet = findViewById(R.id.facilities_no_internet);
        no_facilities = findViewById(R.id.no_facilities);

        facilities_RecyclerView.setHasFixedSize(true);
        facilities_Adapter = new Facilities_Adapter(getFacilitiesFromDatabase(),Admin_Profile.this);
        facilities_RecyclerView.setAdapter(facilities_Adapter);


        //seeing the adapter for the admission spinner
        admission_status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                admit_string = admission_state[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                admit_string = "CLOSED";
            }
        });
        ArrayAdapter<String> the_state = new ArrayAdapter<String>(Admin_Profile.this,android.R.layout.simple_list_item_1,admission_state);
        admission_status_spinner.setAdapter(the_state);


        facilies_no_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    Fetch_Facilities_IDS();
                }else{
                    facilies_no_internet.setVisibility(View.VISIBLE);
                    no_facilities.setVisibility(View.GONE);
                }
            }
        });

//        images initializarions
        images_RecyclerView = findViewById(R.id.images_recyclerView);
        images_no_internet = findViewById(R.id.images_no_internet);
        no_images = findViewById(R.id.no_images);
        edit_images = findViewById(R.id.edit_Images);

        images_RecyclerView.setHasFixedSize(true);
        images_Adapter = new ImagesAdapter(getImagesFromDatabase(),Admin_Profile.this);
        images_RecyclerView.setAdapter(images_Adapter);

        if (isNetworkAvailable()){
            Fetch_Remaining_school_info();
        }else{
            Toast.makeText(Admin_Profile.this, "No internet connection", Toast.LENGTH_LONG).show();
            images_no_internet.setVisibility(View.VISIBLE);
            facilies_no_internet.setVisibility(View.VISIBLE);

            no_images.setVisibility(View.GONE);
            no_facilities.setVisibility(View.GONE);
        }

        final Handler thehandler;

        thehandler = new Handler(Looper.getMainLooper());
        final int delay = 15000;

        thehandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isNetworkAvailable()){
                    Fetch_Remaining_school_info();
                }else{
                    Toast.makeText(Admin_Profile.this, "No internet connection", Toast.LENGTH_LONG).show();
                    images_no_internet.setVisibility(View.VISIBLE);
                    facilies_no_internet.setVisibility(View.VISIBLE);
                }
                thehandler.postDelayed(this,delay);
            }
        },delay);

        images_no_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                        Fetch_Images_ID();
                }else{
                    images_no_internet.setVisibility(View.VISIBLE);
                    no_images.setVisibility(View.GONE);
                }
            }
        });

//        setting font style here
        edit_button.setTypeface(lovelo);
        school_name_text.setTypeface(lovelo);
        school_email_text.setTypeface(lovelo);
        school_number_text.setTypeface(lovelo);
        school_location_text.setTypeface(lovelo);
        success_message.setTypeface(lovelo);

        edit_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin_Profile.this, Edit_Images.class));
            }
        });

        edit_facilities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin_Profile.this, Edit_Facility.class));
            }
        });

//        when edit button is clicked, it changes to save
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_button.setText("SAVE");
                school_name_text_value.setVisibility(View.GONE);
                school_email_text_value.setVisibility(View.GONE);
                school_number_text_value.setVisibility(View.GONE);
                school_location_text_value.setVisibility(View.GONE);

                //other details items
                admission_status_text.setVisibility(View.GONE);
                language_text.setVisibility(View.GONE);
                range_text.setVisibility(View.GONE);
                mission_text.setVisibility(View.GONE);
                vision_text.setVisibility(View.GONE);

                school_name_editText.setVisibility(View.VISIBLE);
                school_email_editText.setVisibility(View.VISIBLE);
                school_number_editText.setVisibility(View.VISIBLE);
                school_location_editText.setVisibility(View.VISIBLE);

                //other details items
                admission_status_spinner.setVisibility(View.VISIBLE);
                language_editText.setVisibility(View.VISIBLE);
                range_editText.setVisibility(View.VISIBLE);
                mission_editText.setVisibility(View.VISIBLE);
                vision_editText.setVisibility(View.VISIBLE);

                school_name_editText.setText(string_school_name);
                school_email_editText.setText(string_school_email);
                school_number_editText.setText(string_school_number);
                school_location_editText.setText(string_school_location);

                //other details items
                language_editText.setText(language_string);
                range_editText.setText(range_string);
                mission_editText.setText(mission_string);
                vision_editText.setText(vision_string);

                edit_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        string_school_name = school_name_editText.getText().toString().trim();
                        string_school_email = school_email_editText.getText().toString().trim();
                        string_school_number = school_number_editText.getText().toString().trim();
                        string_school_location = school_location_editText.getText().toString().trim();

                        language_string = language_editText.getText().toString().trim();
                        range_string = range_editText.getText().toString().trim();
                        mission_string = mission_editText.getText().toString().trim();
                        vision_string = vision_editText.getText().toString().trim();

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

    private void save_new_values(String string_school_name, String string_school_email,
                                 String string_school_number, String string_school_location,
                                 String string_language, String string_range,
                                 String string_mission, String string_vision, String admittion) {
        loading.setVisibility(View.VISIBLE);
        success_message.setVisibility(View.GONE);

        databaseReference = FirebaseDatabase.getInstance().getReference("schools").child(school_code);
        databaseReference.child("name").setValue(string_school_name);
        databaseReference.child("email").setValue(string_school_email);
        databaseReference.child("telephone").setValue(string_school_number);
        databaseReference.child("location").setValue(string_school_location);
        databaseReference.child("language").setValue(string_language);
        databaseReference.child("range").setValue(string_range);
        databaseReference.child("mission").setValue(string_mission);
        databaseReference.child("vision").setValue(string_vision);
        databaseReference.child("admission_status").setValue(admittion);
        loading.setVisibility(View.GONE);
//        success_message.setVisibility(View.VISIBLE);
//        success_message.setTextColor(getResources().getColor(R.color.green));
//        success_message.setText("Update Successful");
        Toast.makeText(Admin_Profile.this, "Update Successful", Toast.LENGTH_LONG).show();

        //updating shared preference values
        profileaccessor.put("school_email",string_school_email);
        profileaccessor.put("school_location",string_school_location);
        profileaccessor.put("school_name",string_school_name);
        profileaccessor.put("school_telephone",string_school_number);
        addToNotifications(FirebaseAuth.getInstance().getCurrentUser());
        startActivity(new Intent(Admin_Profile.this, Admin_Profile.class));
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
                        save_new_values(string_school_name,string_school_email,string_school_number,
                                string_school_location,language_string, range_string, mission_string, vision_string, admit_string);
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

    private void Fetch_Remaining_school_info() {
        try {
            DatabaseReference get_more_details = FirebaseDatabase.getInstance().getReference("schools").child(school_code);
            get_more_details.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            if(child.getKey().equals("language")){
                                language_string = child.getValue().toString();
                                if(language_string.equals("")){
                                    language_text.setText("None");
                                }else{
                                    language_text.setText(language_string);
                                }
                            }

                            if(child.getKey().equals("range")){
                                range_string = child.getValue().toString();
                                if(range_string.equals("")){
                                    range_text.setText("None");
                                }else{
                                    range_text.setText(range_string);
                                }
                            }

                            if(child.getKey().equals("mission")){
                                mission_string = child.getValue().toString();
                                if(mission_string.equals("")){
                                    mission_text.setText("None");
                                }else{
                                    mission_text.setText(mission_string);
                                }
                            }

                            if(child.getKey().equals("vision")){
                                vision_string = child.getValue().toString();
                                if(vision_string.equals("")){
                                    vision_text.setText("None");
                                }else{
                                    vision_text.setText(vision_string);
                                }
                            }

                            if(child.getKey().equals("admission_status")){
                                admission_string = child.getValue().toString();
                                if(admission_string.equals("")){
                                    admission_status_text.setText("CLOSED");
                                }else{
                                    admission_status_text.setText(admission_string);
                                }
                            }

                            else{
//                            Toast.makeText(getActivity(),"Couldn't fetch posts",Toast.LENGTH_LONG).show();

                            }
                        }
                        Fetch_Images_ID();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Admin_Profile.this,"Cancelled",Toast.LENGTH_LONG).show();

                }
            });
        }catch (NullPointerException e){

        }
    }

    public ArrayList<Images> getImagesFromDatabase(){
        return  imagesArray;
    }

    public ArrayList<Facilities> getFacilitiesFromDatabase(){
        return  facilitiesArray;
    }

    private void Fetch_Images_ID() {
        images_no_internet.setVisibility(View.GONE);
        try{
            DatabaseReference get_Image_urlID = FirebaseDatabase.getInstance().getReference("images").child("school_images").child(school_code);//.child(mauth.getCurrentUser().getUid());
            get_Image_urlID.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            Fetch_Images_Url(child.getKey());
                        }
                    }else{
//                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Admin_Profile.this,"Cancelled",Toast.LENGTH_LONG).show();
                }
            });
        }catch (NullPointerException e){

        }
    }

    private void Fetch_Images_Url(final String key) {
        DatabaseReference getImageUrls = FirebaseDatabase.getInstance().getReference("images").child("school_images").child(school_code).child(key);
        getImageUrls.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("image")){
                            images_image = child.getValue().toString();
                        }
                        else{
//                            Toast.makeText(getActivity(),"Couldn't fetch posts",Toast.LENGTH_LONG).show();

                        }
                    }

                    Images obj = new Images(key,images_image);
                    imagesArray.add(obj);
                    images_RecyclerView.setAdapter(images_Adapter);
                    images_Adapter.notifyDataSetChanged();
                    no_images.setVisibility(View.GONE);
                    Fetch_Facilities_IDS();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Admin_Profile.this,"Cancelled",Toast.LENGTH_LONG).show();

            }
        });
    }

    private void Fetch_Facilities_IDS() {
        facilies_no_internet.setVisibility(View.GONE);
        try{
            DatabaseReference get_faciility_urlID = FirebaseDatabase.getInstance().getReference("images").child("facilities").child(school_code);//.child(mauth.getCurrentUser().getUid());
            get_faciility_urlID.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            Fetch_Facility_Url(child.getKey());
                        }
                    }else{
//                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Admin_Profile.this,"Cancelled",Toast.LENGTH_LONG).show();
                }
            });
        }catch (NullPointerException e){

        }
    }

    private void Fetch_Facility_Url(final String key) {
        DatabaseReference getFacilityUrls = FirebaseDatabase.getInstance().getReference("images").child("facilities").child(school_code).child(key);
        getFacilityUrls.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("image")){
                            facility_image = child.getValue().toString();
                        }
                        if(child.getKey().equals("facility_name")){
                            facility_name = child.getValue().toString();
                            Toast.makeText(Admin_Profile.this, facility_name, Toast.LENGTH_LONG).show();
                        }
                        else{
//                            Toast.makeText(getActivity(),"Couldn't fetch posts",Toast.LENGTH_LONG).show();

                        }
                    }

                    Facilities obj = new Facilities(key,facility_name, facility_image);
                    facilitiesArray.add(obj);
                    facilities_RecyclerView.setAdapter(facilities_Adapter);
                    facilities_Adapter.notifyDataSetChanged();
                    no_facilities.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Admin_Profile.this,"Cancelled",Toast.LENGTH_LONG).show();

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
