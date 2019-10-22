package com.example.sergeherkul.bustrackeradmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sergeherkul.bustrackeradmin.Accessories;
import com.example.sergeherkul.bustrackeradmin.Driver_Details;
import com.example.sergeherkul.bustrackeradmin.Model.Drivers;
import com.example.sergeherkul.bustrackeradmin.Model.Parents;
import com.example.sergeherkul.bustrackeradmin.Parent_Details;
import com.example.sergeherkul.bustrackeradmin.R;

import java.util.ArrayList;

public class ParentsAdapter extends RecyclerView.Adapter<ParentsAdapter.ViewHolder>{
    ArrayList<Parents> itemList;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public ParentsAdapter(ArrayList<Parents> itemList, Context context){
        this.itemList  = itemList;
        this.context  = context;
    }

    @Override
    public ParentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_attachement,parent,false);
        ViewHolder vh = new ViewHolder(layoutView);
        return vh;
    }


    @Override
    public void onBindViewHolder(ParentsAdapter.ViewHolder holder, final int position) {
        final TextView name = holder.view.findViewById(R.id.parent_name);
//        TextView time = holder.view.findViewById(R.id.notify_time);
        TextView parent_number = holder.view.findViewById(R.id.parent_number);
        CardView parent_card_view = holder.view.findViewById(R.id.parent_card_view);


        Typeface lovelo =Typeface.createFromAsset(context.getAssets(),  "fonts/lovelo.ttf");
        name.setTypeface(lovelo);
        parent_number.setTypeface(lovelo);

        name.setText(itemList.get(position).getParent_first_name() + " " + itemList.get(position).getParent_last_name());
        parent_number.setText(itemList.get(position).getParent_phone_number());

        parent_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open_driver_details = new Intent(v.getContext(), Parent_Details.class);
                Accessories driver_accessor = new Accessories(v.getContext());
                driver_accessor.put("parent_fname_from_admin",itemList.get(position).getParent_first_name());
                driver_accessor.put("parent_lname_from_admin",itemList.get(position).getParent_last_name());
                driver_accessor.put("parent_address_from_admin",itemList.get(position).getParent_address());
                driver_accessor.put("parent_phone_from_admin",itemList.get(position).getParent_phone_number());
                driver_accessor.put("parent_email_from_admin",itemList.get(position).getParent_email());
                driver_accessor.put("parent_code_from_admin",itemList.get(position).getParent_code());
                v.getContext().startActivity(open_driver_details);
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
