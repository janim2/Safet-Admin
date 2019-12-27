package com.example.sergeherkul.bustrackeradmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sergeherkul.bustrackeradmin.Adapters.Edit_Facilities_Adapter;
import com.example.sergeherkul.bustrackeradmin.Adapters.Facilities_Adapter;
import com.example.sergeherkul.bustrackeradmin.Model.Facilities;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class Edit_Facility extends AppCompatActivity {
    private ImageView imageView;
    private Button upload_button;
    private String school_code;
    private Accessories edit_accessor;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    FirebaseStorage storage;
    StorageReference storageReference;

    //facilities items
    private ArrayList facilitiesArray = new ArrayList<Facilities>();
    private RecyclerView facilities_RecyclerView;
    private RecyclerView.Adapter facilities_Adapter;
    private String facility_id,facility_name,faciliy_name_string, facility_image;
    private TextView no_facilities, facilies_no_internet, upload_no_internet, upload_status_message;
    private EditText faciliy_name_editText;
    private ProgressBar loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__facility);

        getSupportActionBar().setTitle("Edit facilities");

        edit_accessor = new Accessories(Edit_Facility.this);

        school_code = edit_accessor.getString("school_code");

        imageView = findViewById(R.id.image);
        upload_button = findViewById(R.id.upload_button);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //        facilities initializations
        facilities_RecyclerView = findViewById(R.id.facilities_recyclerView);
        facilies_no_internet = findViewById(R.id.facilities_no_internet);
        no_facilities = findViewById(R.id.no_facilities);

        upload_no_internet = findViewById(R.id.no_internet);
        upload_status_message = findViewById(R.id.status_message);
        faciliy_name_editText = findViewById(R.id.facility_name_editText);
        loading = findViewById(R.id.loading);

        if(isNetworkAvailable()){
            Fetch_Facilities_IDS();
        }else{
            facilies_no_internet.setVisibility(View.VISIBLE);
            no_facilities.setVisibility(View.GONE);
        }

        facilities_RecyclerView.setHasFixedSize(true);
        facilities_Adapter = new Edit_Facilities_Adapter(getFacilitiesFromDatabase(),Edit_Facility.this);
        facilities_RecyclerView.setAdapter(facilities_Adapter);


        final StorageReference loadImage = storage.getReference().child("images/facilities/"+school_code+"/image1.jpg");
        loadImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(Edit_Facility.this).load(uri.toString()).into(imageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(Edit_Facility.this, "Not loading", Toast.LENGTH_LONG).show();
            }
        });

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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                faciliy_name_string = faciliy_name_editText.getText().toString().trim();
                if(!faciliy_name_string.equals("")){
                    if(isNetworkAvailable()){
                        if(filePath != null)
                        {
//                    final ProgressDialog progressDialog = new ProgressDialog(Edit_Facility.this);
//                    progressDialog.setTitle("Uploading...");
//                    progressDialog.show();

                            StorageReference ref = storageReference.child("images/facilities/"+school_code+"/"+ UUID.randomUUID().toString());
                            ref.putFile(filePath)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            if (taskSnapshot.getMetadata() != null) {
                                                if (taskSnapshot.getMetadata().getReference() != null) {
                                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            String imageUrl = uri.toString();
                                                            //createNewPost(imageUrl);
                                                            DatabaseReference addImages = FirebaseDatabase.getInstance().getReference("images").child("facilities").child(school_code).push();
                                                            addImages.child("image").setValue(imageUrl);
                                                            addImages.child("facility_name").setValue(faciliy_name_string);
                                                            loading.setVisibility(View.GONE);
                                                            upload_no_internet.setVisibility(View.GONE);
                                                            upload_status_message.setText("Upload complete");
                                                            upload_status_message.setVisibility(View.VISIBLE);
                                                            Fetch_Facilities_IDS();
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
//                                    progressDialog.dismiss();
                                            loading.setVisibility(View.GONE);
                                            upload_no_internet.setVisibility(View.GONE);
                                            upload_status_message.setText("Upload failed");
                                            upload_status_message.setVisibility(View.VISIBLE);

                                            Toast.makeText(Edit_Facility.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                                    .getTotalByteCount());
                                            loading.setVisibility(View.GONE);
                                            upload_no_internet.setVisibility(View.GONE);
                                            upload_status_message.setText(progress+""+"%"+" uploaded");
                                            upload_status_message.setVisibility(View.VISIBLE);
                                        }
                                    });
                        }
                    }else{
                        loading.setVisibility(View.GONE);
                        upload_no_internet.setVisibility(View.GONE);
                        upload_status_message.setText("No internet connection");
                        upload_status_message.setVisibility(View.VISIBLE);
                    }
                }else{
                    loading.setVisibility(View.GONE);
                    upload_no_internet.setVisibility(View.GONE);
                    upload_status_message.setText("Facility name required");
                    upload_status_message.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    imageView.setImageBitmap(bitmap);
                }catch (OutOfMemoryError e){

                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
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
                    Toast.makeText(Edit_Facility.this,"Cancelled",Toast.LENGTH_LONG).show();
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
//                            Toast.makeText(Edit_Facility.this, facility_name, Toast.LENGTH_LONG).show();
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
                Toast.makeText(Edit_Facility.this,"Cancelled",Toast.LENGTH_LONG).show();

            }
        });
    }

    public ArrayList<Facilities> getFacilitiesFromDatabase(){
        return  facilitiesArray;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
