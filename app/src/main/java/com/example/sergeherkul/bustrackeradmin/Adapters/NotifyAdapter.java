package com.example.sergeherkul.bustrackeradmin.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sergeherkul.bustrackeradmin.Model.Notify;
import com.example.sergeherkul.bustrackeradmin.R;

import java.util.ArrayList;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.ViewHolder>{
    ArrayList<Notify> itemList;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public NotifyAdapter(ArrayList<Notify> itemList, Context context){
        this.itemList  = itemList;
        this.context  = context;
    }

    @Override
    public NotifyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_attachment,parent,false);
        ViewHolder vh = new ViewHolder(layoutView);
        return vh;
    }


    @Override
    public void onBindViewHolder(NotifyAdapter.ViewHolder holder, final int position) {
        final TextView title = holder.view.findViewById(R.id.title_text);
        ImageView image = holder.view.findViewById(R.id.notify_image);
//        TextView time = holder.view.findViewById(R.id.notify_time);
        TextView message = holder.view.findViewById(R.id.notify_message);

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
            else{
                image.setImageDrawable(holder.view.getResources().getDrawable(R.drawable.logo));
            }

        Typeface lovelo =Typeface.createFromAsset(context.getAssets(),  "fonts/lovelo.ttf");
            title.setTypeface(lovelo);
            message.setTypeface(lovelo);

        title.setText(itemList.get(position).getTitle());
            message.setText(itemList.get(position).getMessage());
//            time.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",itemList.get(position).getTime()));
        }


    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
