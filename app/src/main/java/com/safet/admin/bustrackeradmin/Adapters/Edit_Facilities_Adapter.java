package com.safet.admin.bustrackeradmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.safet.admin.bustrackeradmin.Accessories;
import com.safet.admin.bustrackeradmin.Edit_Facility;
import com.safet.admin.bustrackeradmin.Model.Facilities;
import com.safet.admin.bustrackeradmin.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Edit_Facilities_Adapter extends RecyclerView.Adapter<Edit_Facilities_Adapter.ViewHolder>{
    ArrayList<Facilities> itemList;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public Edit_Facilities_Adapter(ArrayList<Facilities> itemList, Context context){
        this.itemList  = itemList;
        this.context  = context;
    }

    @Override
    public Edit_Facilities_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_facilities_attachment,parent,false);
        ViewHolder vh = new ViewHolder(layoutView);
        return vh;
    }


    @Override
    public void onBindViewHolder(Edit_Facilities_Adapter.ViewHolder holder, final int position) {
        ImageView image = holder.view.findViewById(R.id.facility_image);
        final TextView name = holder.view.findViewById(R.id.facility_name);
        final EditText facility_name_editText = holder.view.findViewById(R.id.facility_name_editText);
        Button delete_button = holder.view.findViewById(R.id.delete_button);
        Button edit_button = holder.view.findViewById(R.id.save_button);

        Uri imageurl = Uri.parse(itemList.get(position).getImageUrl());
        Picasso.with(context).load(imageurl).into(image);
        name.setText(itemList.get(position).getFacility_name());

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setVisibility(View.GONE);
                facility_name_editText.setVisibility(View.VISIBLE);
                facility_name_editText.setText(itemList.get(position).getFacility_name());
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1020);
            }
        });

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Accessories adapter = new Accessories(context);
                String f_name = facility_name_editText.getText().toString().trim();
                DatabaseReference update_fname = FirebaseDatabase.getInstance().getReference("images").child("facilities")
                        .child(adapter.getString("school_code")).child(itemList.get(position).getId());
                update_fname.child("facility_name").setValue(f_name);
                name.setText(f_name);
                Toast.makeText(context, "Update successful", Toast.LENGTH_LONG).show();
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Accessories adapter = new Accessories(context);
                DatabaseReference remove_fname = FirebaseDatabase.getInstance().getReference("images").child("facilities")
                        .child(adapter.getString("school_code")).child(itemList.get(position).getId());
                remove_fname.removeValue();
                v.getContext().startActivity(new Intent(v.getContext(), Edit_Facility.class));

                Toast.makeText(context, "Deletion successful", Toast.LENGTH_LONG).show();

            }
        });
        }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}

