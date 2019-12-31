package com.safet.admin.bustrackeradmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

public class Edit_Images extends AppCompatActivity {

    private CardView school_logo_cardView,image_one_cardView, image_two_cardview, image_three_cardview;
    private ImageView logo_imageView, imageView_one, imageView_two, imageView_three;
    private final int PICK_IMAGE_ONE_REQUEST = 71;
    private final int PICK_IMAGE_TWO_REQUEST = 81;
    private final int PICK_IMAGE_THREE_REQUEST = 91;
    private final int PICK_IMAGE_LOGO_REQUEST = 61;

    private Uri filePath_one,filePath_two, filePath_three, filePath_logo;
    private Button save_button;
    private String school_code;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Accessories edit_images_accessor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__images);
        getSupportActionBar().setTitle("School Images");

        edit_images_accessor = new Accessories(Edit_Images.this);

        school_logo_cardView = findViewById(R.id.school_logo);
        image_one_cardView = findViewById(R.id.school_image_one);
        image_two_cardview = findViewById(R.id.school_image_two);
        image_three_cardview = findViewById(R.id.school_image_three);

        school_code = edit_images_accessor.getString("school_code");

        logo_imageView = findViewById(R.id.logo_image);
        imageView_one = findViewById(R.id.image_one);
        imageView_two = findViewById(R.id.image_two);
        imageView_three = findViewById(R.id.image_three);

        save_button = findViewById(R.id.save_button);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Fetch_Image_One();

        school_logo_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_LOGO_REQUEST);
            }
        });

        image_one_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_ONE_REQUEST);
            }
        });

        image_two_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_TWO_REQUEST);
            }
        });

        image_three_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_THREE_REQUEST);
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filePath_one != null)
                {
//                    final ProgressDialog progressDialog = new ProgressDialog(Edit_Images.this);
//                    progressDialog.setTitle("Uploading...");
//                    progressDialog.show();

                    StorageReference ref_1 = storageReference.child("images/school_images/"+school_code+"/"+ UUID.randomUUID().toString());
                    ref_1.putFile(filePath_one)
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
                                                    DatabaseReference addImages_one = FirebaseDatabase.getInstance().getReference("images").child("school_images").child(school_code).child("image_one");//.push();
                                                    addImages_one.child("image").setValue(imageUrl);
                                                    Toast.makeText(Edit_Images.this, "Image one uploaded ", Toast.LENGTH_SHORT).show();

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
                                    Toast.makeText(Edit_Images.this, "Image one upload failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                            .getTotalByteCount());
                                    Toast.makeText(Edit_Images.this,"Image one: " + progress+""+"%",Toast.LENGTH_LONG).show();
//                                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                                }
                            });
                }else{
                    Toast.makeText(Edit_Images.this, "Image one not selected", Toast.LENGTH_LONG).show();

                }

                    //image two
                    if(filePath_two != null){
                        StorageReference ref_2 = storageReference.child("images/school_images/"+school_code+"/"+ UUID.randomUUID().toString());
                        ref_2.putFile(filePath_two)
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
                                                        DatabaseReference add_imagetwo = FirebaseDatabase.getInstance().getReference("images").child("school_images").child(school_code).child("image_two");//.push();
                                                        add_imagetwo.child("image").setValue(imageUrl);
                                                        Toast.makeText(Edit_Images.this, "Image two uploaded ", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(Edit_Images.this, "Image two upload failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                            .getTotalByteCount());
                                    Toast.makeText(Edit_Images.this,"Image two: " + progress+""+"%",Toast.LENGTH_LONG).show();
//                                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                                    }
                                });
                    }else{
                        Toast.makeText(Edit_Images.this, "Image two not selected", Toast.LENGTH_LONG).show();
                    }


                    //image three
                    if(filePath_three != null){
                        StorageReference ref_3 = storageReference.child("images/school_images/"+school_code+"/"+ UUID.randomUUID().toString());
                        ref_3.putFile(filePath_three)
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
                                                        DatabaseReference add_imagethree = FirebaseDatabase.getInstance().getReference("images").child("school_images").child(school_code).child("image_three");//.push();
                                                        add_imagethree.child("image").setValue(imageUrl);
                                                        Toast.makeText(Edit_Images.this, "Image three uploaded ", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
//                                        progressDialog.dismiss();
                                        Toast.makeText(Edit_Images.this, "Image three upload failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                            .getTotalByteCount());
                                    Toast.makeText(Edit_Images.this,"Image three: " + progress+""+"%",Toast.LENGTH_LONG).show();
//                                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                                    }
                                });
                    }else{
                        Toast.makeText(Edit_Images.this, "Image three not selected", Toast.LENGTH_LONG).show();

                    }

                    //logo image
                    if(filePath_logo != null){
                        StorageReference logo_ = storageReference.child("images/school_images/"+school_code+"/"+ UUID.randomUUID().toString());
                        logo_.putFile(filePath_logo)
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
                                                        DatabaseReference add_logo = FirebaseDatabase.getInstance().getReference("images").child("school_images").child(school_code).child("logo");//.push();
                                                        add_logo.child("image").setValue(imageUrl);
                                                        Toast.makeText(Edit_Images.this, "logo uploaded ", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
//                                        progressDialog.dismiss();
                                        Toast.makeText(Edit_Images.this, "Logo upload failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                                .getTotalByteCount());
                                        Toast.makeText(Edit_Images.this,"Logo: " + progress+""+"%",Toast.LENGTH_LONG).show();
//                                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                                    }
                                });
                    }else{
                        Toast.makeText(Edit_Images.this, "Logo not selected", Toast.LENGTH_LONG).show();

                    }
            }
        });
    }

    private void Fetch_Image_One() {
        try {
            DatabaseReference image_one = FirebaseDatabase.getInstance().getReference("images").child("school_images").child(school_code).child("image_one");
            image_one.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            if(child.getKey().equals("image")){
                                String image_one_image = child.getValue().toString();
                                if(image_one_image.equals("")){
                                }else{
                                    Picasso.with(Edit_Images.this).load(Uri.parse(image_one_image)).into(imageView_one);
                                }
                            }

                            else{
//                            Toast.makeText(getActivity(),"Couldn't fetch posts",Toast.LENGTH_LONG).show();

                            }
                        }
                        Fetch_Image_Two();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Edit_Images.this,"Cancelled",Toast.LENGTH_LONG).show();

                }
            });
        }catch (NullPointerException e){

        }
    }

    private void Fetch_Image_Two() {
        try {
            DatabaseReference image_two = FirebaseDatabase.getInstance().getReference("images").child("school_images").child(school_code).child("image_two");
            image_two.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            if(child.getKey().equals("image")){
                                String image_two_image = child.getValue().toString();
                                if(image_two_image.equals("")){
                                }else{
                                    Picasso.with(Edit_Images.this).load(Uri.parse(image_two_image)).into(imageView_two);
                                }
                            }

                            else{
//                            Toast.makeText(getActivity(),"Couldn't fetch posts",Toast.LENGTH_LONG).show();

                            }
                        }
                        Fetch_Image_Three();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Edit_Images.this,"Cancelled",Toast.LENGTH_LONG).show();

                }
            });
        }catch (NullPointerException e){

        }
    }

    private void Fetch_Image_Three() {
        try {
            DatabaseReference image_three = FirebaseDatabase.getInstance().getReference("images").child("school_images").child(school_code).child("image_three");
            image_three.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            if(child.getKey().equals("image")){
                                String image_three_image = child.getValue().toString();
                                if(image_three_image.equals("")){
                                }else{
                                    Picasso.with(Edit_Images.this).load(Uri.parse(image_three_image)).into(imageView_three);
                                }
                            }

                            else{
//                            Toast.makeText(getActivity(),"Couldn't fetch posts",Toast.LENGTH_LONG).show();

                            }
                        }
                        Fetch_school_logo();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Edit_Images.this,"Cancelled",Toast.LENGTH_LONG).show();

                }
            });
        }catch (NullPointerException e){

        }
    }

    private void Fetch_school_logo() {
        try {
            DatabaseReference logo = FirebaseDatabase.getInstance().getReference("images").child("school_images").child(school_code).child("logo");
            logo.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            if(child.getKey().equals("image")){
                                String logo_image = child.getValue().toString();
                                if(logo_image.equals("")){
                                }else{
                                    Picasso.with(Edit_Images.this).load(Uri.parse(logo_image)).into(logo_imageView);
                                }
                            }

                            else{
//                            Toast.makeText(getActivity(),"Couldn't fetch posts",Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Edit_Images.this,"Cancelled",Toast.LENGTH_LONG).show();

                }
            });
        }catch (NullPointerException e){

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_ONE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath_one = data.getData();
            try {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath_one);
                    imageView_one.setImageBitmap(bitmap);
                }catch (OutOfMemoryError e){

                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else if(requestCode == PICK_IMAGE_TWO_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            filePath_two = data.getData();
            try {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath_two);
                    imageView_two.setImageBitmap(bitmap);
                }catch (OutOfMemoryError e){

                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else if(requestCode == PICK_IMAGE_THREE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            filePath_three = data.getData();
            try {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath_three);
                    imageView_three.setImageBitmap(bitmap);
                }catch (OutOfMemoryError e){

                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        else if(requestCode == PICK_IMAGE_LOGO_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            filePath_logo = data.getData();
            try {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath_logo);
                    logo_imageView.setImageBitmap(bitmap);
                }catch (OutOfMemoryError e){

                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
