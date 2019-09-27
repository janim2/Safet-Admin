package com.example.sergeherkul.bustrackeradmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sergeherkul.bustrackeradmin.Accessories;
import com.example.sergeherkul.bustrackeradmin.Driver_Details;
import com.example.sergeherkul.bustrackeradmin.Model.Drivers;
import com.example.sergeherkul.bustrackeradmin.Model.Notify;
import com.example.sergeherkul.bustrackeradmin.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DriversAdapter extends RecyclerView.Adapter<DriversAdapter.ViewHolder>{
    ArrayList<Drivers> itemList;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public DriversAdapter(ArrayList<Drivers> itemList, Context context){
        this.itemList  = itemList;
        this.context  = context;
    }

    @Override
    public DriversAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_attachement,parent,false);
        ViewHolder vh = new ViewHolder(layoutView);
        return vh;
    }


    @Override
    public void onBindViewHolder(DriversAdapter.ViewHolder holder, final int position) {
        final TextView name = holder.view.findViewById(R.id.driver_name);
//        TextView time = holder.view.findViewById(R.id.notify_time);
        TextView phone_number = holder.view.findViewById(R.id.driver_number);
        CardView driver_card_view = holder.view.findViewById(R.id.driver_card_view);


        Typeface lovelo =Typeface.createFromAsset(context.getAssets(),  "fonts/lovelo.ttf");
        name.setTypeface(lovelo);
        phone_number.setTypeface(lovelo);

        name.setText(itemList.get(position).getDriver_first_name() + " " + itemList.get(position).getDriver_last_name());
        phone_number.setText(itemList.get(position).getDriver_phone_number());

        driver_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open_driver_details = new Intent(v.getContext(), Driver_Details.class);
                Accessories driver_accessor = new Accessories(v.getContext());
                driver_accessor.put("driver_fname_from_admin",itemList.get(position).getDriver_first_name());
                driver_accessor.put("driver_lname_from_admin",itemList.get(position).getDriver_last_name());
                driver_accessor.put("driver_address_from_admin",itemList.get(position).getDriver_address());
                driver_accessor.put("driver_phone_from_admin",itemList.get(position).getDriver_phone_number());
                driver_accessor.put("driver_code_from_admin",itemList.get(position).getDriver_code());
                v.getContext().startActivity(open_driver_details);
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
