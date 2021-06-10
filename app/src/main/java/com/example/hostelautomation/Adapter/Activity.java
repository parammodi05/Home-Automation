package com.example.hostelautomation.Adapter;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hostelautomation.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity extends RecyclerView.Adapter<Activity.MyView> {

    private List<String> list;

    public class MyView extends RecyclerView.ViewHolder {

        public TextView textView;
        public CircleImageView imageView;

        public MyView(View view) {
            super(view);

            textView = (TextView) view.findViewById(R.id.actname);
            imageView = (CircleImageView) view.findViewById(R.id.actimg);

        }
    }

    public Activity(List<String> horizontalList) {
        this.list = horizontalList;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_act, parent, false);
        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, final int position) {
        String m=list.get(position);
        String arr[]=m.split("[?]");

        holder.textView.setText(arr[0]);
       // holder.imageView.set(list.get(position).second);
        Glide.with(holder.imageView.getContext()).load(arr[1]).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}