package com.example.hostelautomation.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hostelautomation.R;

import java.util.List;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.MyView> {

    private List<Pair<Pair<String, Integer>,String>> list;
    Context c;
    private static final String PREFS_ROOM = "Room_notification";










    public class MyView extends RecyclerView.ViewHolder {

        public TextView textView;
        public TextView textVieww;
        public ImageView imageView;
        Context c;

        public MyView(View view) {
            super(view);

            textView = (TextView) view.findViewById(R.id.roomName);
            imageView = (ImageView) view.findViewById(R.id.image);
            textVieww = (TextView) view.findViewById(R.id.badgetext);


        }
    }

    public RoomsAdapter(List<Pair<Pair<String, Integer>,String>> horizontalList) {

        this.list = horizontalList;


    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_cards, parent, false);
        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, final int position) {


        holder.textView.setText(list.get(position).first.first);
        holder.imageView.setImageResource(list.get(position).first.second);
        holder.textVieww.setText(list.get(position).second);


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

}