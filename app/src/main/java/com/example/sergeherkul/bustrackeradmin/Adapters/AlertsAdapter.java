package com.example.sergeherkul.bustrackeradmin.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sergeherkul.bustrackeradmin.Accessories;
import com.example.sergeherkul.bustrackeradmin.Model.Notify;
import com.example.sergeherkul.bustrackeradmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AlertsAdapter extends RecyclerView.Adapter<AlertsAdapter.ViewHolder>{
    ArrayList<Notify> itemList;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public AlertsAdapter(ArrayList<Notify> itemList, Context context){
        this.itemList  = itemList;
        this.context  = context;
    }

    @Override
    public AlertsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_attachment,parent,false);
        ViewHolder vh = new ViewHolder(layoutView);
        return vh;
    }


    @Override
    public void onBindViewHolder(AlertsAdapter.ViewHolder holder, final int position) {
        final TextView title = holder.view.findViewById(R.id.title_text);
        ImageView image = holder.view.findViewById(R.id.notify_image);
//        TextView time = holder.view.findViewById(R.id.notify_time);
        TextView message = holder.view.findViewById(R.id.notify_message);
        Button call_driver = holder.view.findViewById(R.id.call_driver);
        Button call_police_or_mechanic = holder.view.findViewById(R.id.other_call);
        Button dismiss = holder.view.findViewById(R.id.dismiss);

        String the_image = itemList.get(position).getImageType();
            if(the_image.equals("WM")){//stands for welcome Notification
                image.setImageDrawable(holder.view.getResources().getDrawable(R.drawable.logo));
            }
            else if(the_image.equals("US")){
                image.setImageDrawable(holder.view.getResources().getDrawable(R.drawable.save));
            }
            else if(the_image.equals("AD")){
                image.setImageDrawable(holder.view.getResources().getDrawable(R.drawable.driver));
            }
            else if(the_image.equals("AP")){
                image.setImageDrawable(holder.view.getResources().getDrawable(R.drawable.parents));
            }
            else if(the_image.equals("SN")){
                image.setImageDrawable(holder.view.getResources().getDrawable(R.drawable.security));
                call_police_or_mechanic.setText("Call police");
            }
            else if(the_image.equals("DN")){
                image.setImageDrawable(holder.view.getResources().getDrawable(R.drawable.distress));
                call_police_or_mechanic.setText("Call mechanic");
            }
            else{
                image.setImageDrawable(holder.view.getResources().getDrawable(R.drawable.logo));
            }

        Typeface lovelo =Typeface.createFromAsset(context.getAssets(),  "fonts/lovelo.ttf");
            title.setTypeface(lovelo);
            message.setTypeface(lovelo);

        title.setText(itemList.get(position).getTitle());
            message.setText(itemList.get(position).getMessage());
//            time.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",itemList.get(position).getTime()));

//      setting onclick for each button
        call_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Accessories alertsAccesor = new Accessories(context);
                Query find_driver = FirebaseDatabase.getInstance().getReference("drivers")
                        .child(alertsAccesor.getString("school_code")).child(itemList.get(position).getId());
                find_driver.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot child : dataSnapshot.getChildren()){
                                if(child.getKey().equals("phone_number")){
                                    String driver_number = child.getValue().toString();
                                    if(!driver_number.equals("")){
                                        openDialer(v,driver_number);
                                    }else{
                                        Toast.makeText(context, "number not added", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                openDialer(v,"0268977129");
            }
        });

        call_police_or_mechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(itemList.get(position).getImageType().equals("SN")){
                    //call 911
                    openDialer(v,"911");
                }else{
                    //call mechanic
                    Accessories alertsAccessor = new Accessories(context);
                    String school_code = alertsAccessor.getString("school_code");
                    final DatabaseReference mechanic_number = FirebaseDatabase.getInstance().getReference("schools")
                            .child(school_code);
                    mechanic_number.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                for(DataSnapshot child : dataSnapshot.getChildren()){
                                    if(child.getKey().equals("mechanic_number")){
                                        String mechanic_number_string = child.getValue().toString();
                                        if(!mechanic_number_string.equals("")){
                                            openDialer(v,mechanic_number_string);
                                        }else{
                                            Toast.makeText(context, "number not added", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });

//        dismiss.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                final Accessories notify_adapter_accessor = new Accessories(v.getContext());
//                final AlertDialog.Builder confirm_dismiss = new AlertDialog.Builder(v.getContext(), R.style.Myalert);
//                confirm_dismiss.setTitle("Confirm dismiss?");
//                confirm_dismiss.setMessage("Leaving us? Please reconsider.");
//                confirm_dismiss.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        logout here
//                            FirebaseDatabase.getInstance().getReference("pending_alerts")
//                                    .child(notify_adapter_accessor.getString("school_code")).child(itemList.get(position).getId()).removeValue();
//                            Toast.makeText(v.getContext(),"Alert Dismissed",Toast.LENGTH_LONG).show();
//                    }
//                });
//
//                confirm_dismiss.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                confirm_dismiss.show();
//            }
//        });

        }

        private void openDialer(View v, String call_number){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + call_number));
            v.getContext().startActivity(intent);
        }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}

