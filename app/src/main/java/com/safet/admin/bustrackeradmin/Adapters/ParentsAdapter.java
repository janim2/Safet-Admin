package com.safet.admin.bustrackeradmin.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.safet.admin.bustrackeradmin.Accessories;
import com.safet.admin.bustrackeradmin.Admin_MainActivity;
import com.safet.admin.bustrackeradmin.Login_Selector;
import com.safet.admin.bustrackeradmin.Model.Parents;
import com.safet.admin.bustrackeradmin.Parent_Details;
import com.safet.admin.bustrackeradmin.R;
import com.safet.admin.bustrackeradmin.Registered_Parents;

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
        ImageView call_image = holder.view.findViewById(R.id.call_parent);
//        TextView time = holder.view.findViewById(R.id.notify_time);
        TextView parent_number = holder.view.findViewById(R.id.parent_number);
        CardView parent_card_view = holder.view.findViewById(R.id.parent_card_view);


        Typeface lovelo =Typeface.createFromAsset(context.getAssets(),  "fonts/lovelo.ttf");
        name.setTypeface(lovelo);
        parent_number.setTypeface(lovelo);

        name.setText(itemList.get(position).getParent_first_name() + " " + itemList.get(position).getParent_last_name());
        parent_number.setText(itemList.get(position).getParent_phone_number());

        call_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialer(v,itemList.get(position).getParent_phone_number());
            }
        });

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

    private void openDialer(View v, String call_number){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + call_number));
        v.getContext().startActivity(intent);
    }

}
