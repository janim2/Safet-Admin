package com.example.sergeherkul.bustrackeradmin.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sergeherkul.bustrackeradmin.Model.Images;
import com.example.sergeherkul.bustrackeradmin.Model.Notify;
import com.example.sergeherkul.bustrackeradmin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder>{
    ArrayList<Images> itemList;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public ImagesAdapter(ArrayList<Images> itemList, Context context){
        this.itemList  = itemList;
        this.context  = context;
    }

    @Override
    public ImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.school_images_attachment,parent,false);
        ViewHolder vh = new ViewHolder(layoutView);
        return vh;
    }


    @Override
    public void onBindViewHolder(ImagesAdapter.ViewHolder holder, final int position) {
        ImageView image = holder.view.findViewById(R.id.school_imageView);

        Uri imageurl = Uri.parse(itemList.get(position).getImageUrl());
        Picasso.with(context).load(imageurl).centerCrop().into(image);
        }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}

