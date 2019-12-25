package com.example.sergeherkul.bustrackeradmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sergeherkul.bustrackeradmin.Alerts;
import com.example.sergeherkul.bustrackeradmin.AllBuses;
import com.example.sergeherkul.bustrackeradmin.All_children;
import com.example.sergeherkul.bustrackeradmin.Circulate_Message;
import com.example.sergeherkul.bustrackeradmin.R;
import com.example.sergeherkul.bustrackeradmin.Registered_Drivers;

public class SolventViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView countryName;
        public ImageView countryPhoto;

        public SolventViewHolders(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            countryName = (TextView) itemView.findViewById(R.id.country_name);
            countryPhoto = (ImageView) itemView.findViewById(R.id.country_photo);
        }

        @Override
        public void onClick(View view) {
            switch (getPosition()){
                case 0:
                    Intent gotoAlerts = new Intent(itemView.getContext(), Alerts.class);
                    itemView.getContext().startActivity(gotoAlerts);
                    break;

                case 1:
                    Intent gotoBuses = new Intent(itemView.getContext(),AllBuses.class);
                    itemView.getContext().startActivity(gotoBuses);
                    break;

                case 2:
                    Intent gotoChildren = new Intent(itemView.getContext(), All_children.class);
                    itemView.getContext().startActivity(gotoChildren);
                    break;

                case 3:
                    Intent dispatch_messages = new Intent(itemView.getContext(), Circulate_Message.class);
                    itemView.getContext().startActivity(dispatch_messages);
                    break;
            }
        }
}
