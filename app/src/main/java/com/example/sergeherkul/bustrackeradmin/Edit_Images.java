package com.example.sergeherkul.bustrackeradmin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;

public class Edit_Images extends AppCompatActivity {

    private CardView image_one_cardView, image_two_cardview, image_three_cardview;
    private ImageView imageView_one, imageView_two, imageView_three;
    private final int PICK_IMAGE_ONE_REQUEST = 71;
    private final int PICK_IMAGE_TWO_REQUEST = 81;
    private final int PICK_IMAGE_THREE_REQUEST = 91;

    private Uri filePath_one,filePath_two, filePath_three;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__images);
        getSupportActionBar().setTitle("School Images");

        image_one_cardView = findViewById(R.id.school_image_one);
        image_two_cardview = findViewById(R.id.school_image_two);
        image_three_cardview = findViewById(R.id.school_image_three);

        imageView_one = findViewById(R.id.image_one);
        imageView_two = findViewById(R.id.image_two);
        imageView_three = findViewById(R.id.image_three);

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
    }
}
