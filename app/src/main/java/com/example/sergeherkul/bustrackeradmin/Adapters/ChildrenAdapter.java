package com.safet.admin.bustrackeradmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.safet.admin.bustrackeradmin.Accessories;
import com.safet.admin.bustrackeradmin.Driver_Details;
import com.safet.admin.bustrackeradmin.Model.Children;
import com.safet.admin.bustrackeradmin.Model.Drivers;
import com.safet.admin.bustrackeradmin.R;

import java.util.ArrayList;

public class ChildrenAdapter extends RecyclerView.Adapter<ChildrenAdapter.ViewHolder>{
    ArrayList<Children> itemList;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public ChildrenAdapter(ArrayList<Children> itemList, Context context){
        this.itemList  = itemList;
        this.context  = context;
    }

    @Override
    public ChildrenAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_attachment,parent,false);
        ViewHolder vh = new ViewHolder(layoutView);
        return vh;
    }


    @Override
    public void onBindViewHolder(ChildrenAdapter.ViewHolder holder, final int position) {
        final TextView child_name = holder.view.findViewById(R.id.child_name);
        TextView child_class = holder.view.findViewById(R.id.child_class);
        TextView child_gender = holder.view.findViewById(R.id.child_gender);
        CardView child_card_view = holder.view.findViewById(R.id.child_card_view);


        Typeface lovelo =Typeface.createFromAsset(context.getAssets(),  "fonts/lovelo.ttf");
        child_name.setTypeface(lovelo);
        child_class.setTypeface(lovelo);

        child_name.setText(itemList.get(position).getChild_first_name() + " " + itemList.get(position).getChild_last_name());
        child_class.setText(itemList.get(position).getChild_class());
        child_gender.setText(itemList.get(position).getChild_gender());

        child_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent open_driver_details = new Intent(v.getContext(), Driver_Details.class);
//                Accessories driver_accessor = new Accessories(v.getContext());
//                driver_accessor.put("driver_fname_from_admin",itemList.get(position).getDriver_first_name());
//                driver_accessor.put("driver_lname_from_admin",itemList.get(position).getDriver_last_name());
//                driver_accessor.put("driver_address_from_admin",itemList.get(position).getDriver_address());
//                driver_accessor.put("driver_phone_from_admin",itemList.get(position).getDriver_phone_number());
//                driver_accessor.put("driver_code_from_admin",itemList.get(position).getDriver_code());
//                v.getContext().startActivity(open_driver_details);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
