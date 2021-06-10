package com.example.hostelautomation.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hostelautomation.R;

import java.util.List;


public class Timeradapter extends RecyclerView.Adapter<Timeradapter.MyView> {

    private List<String> list;

    public class MyView extends RecyclerView.ViewHolder {

        public TextView textView1,textView2,textView3;
        public ImageView imageView;


        public MyView(View view) {
            super(view);

            textView1 = (TextView) view.findViewById(R.id.timername);
            textView2 = (TextView) view.findViewById(R.id.timerdays);
            textView3 = (TextView) view.findViewById(R.id.timertime);
            imageView = (ImageView) view.findViewById(R.id.timerimage);

        }
    }

    public Timeradapter(List<String> horizontalList) {
        this.list = horizontalList;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_abouttimer, parent, false);
        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, final int position) {
        String m=list.get(position);
        String arr[]=m.split("[?]");
        for(int i=0;i<arr.length;i++)
        {
            arr[i]=arr[i].trim();
        }


        holder.textView1.setText(arr[0]);
        String days="";
        if(1<arr.length&&!arr[1].isEmpty())
        {
            days=arr[1];
        }
        String present="";
        if(!days.isEmpty())
        {
            if (days.contains("monday")) {
                present += ("monday" + " ");
            }
            if (days.contains("tuesday")) {
                present += ("tuesday" + " ");
            }
            if (days.contains("wednesday")) {
                present += ("wednesday" + " ");
            }
            if (days.contains("thursday")) {
                present += ("thursday" + " ");
            }
            if (days.contains("friday")) {
                present += ("friday" + " ");
            }
            if (days.contains("saturday")) {
                present += ("saturday" + " ");
            }
            if (days.contains("sunday")) {
                present += ("sunday" + " ");
            }
        }
        holder.textView2.setText(present);

        String time="";
        if(2<arr.length&&!arr[2].isEmpty())
        {
            time=arr[2];
        }
        String tm="";
        if(!time.isEmpty())
        {
            String str[]=time.split(":");
            int hour=Integer.valueOf(str[0]);
            int min=Integer.valueOf(str[1]);
            if(hour>6&&hour<12)
            {
                //iv.setBackgroundResource(R.drawable.morning);
                holder.imageView.setBackgroundResource(R.drawable.morning);
                //time.setText(hr+":"+min+" "+"AM");
                holder.textView3.setText(hour+":"+min+" "+"AM");

            }
            else if(hour==12)
            {
                holder.imageView.setBackgroundResource(R.drawable.afternoon);
                //time.setText(hr+":"+min+" "+"AM");
                holder.textView3.setText(hour+":"+min+" "+"PM");
            }
            else if(hour>=12&&hour<=18)
            {
               // iv.setBackgroundResource(R.drawable.afternoon);
                holder.imageView.setBackgroundResource(R.drawable.afternoon);
                String phr=String.valueOf(hour-12);
                holder.textView3.setText(phr+":"+min+" "+"PM");



            }
            else
            {
               // iv.setBackgroundResource(R.drawable.night);
                holder.imageView.setBackgroundResource(R.drawable.night);
                if(hour>18&&hour<=23)
                {
                    String phr=String.valueOf(hour-12);
                    holder.textView3.setText(phr+":"+min+" "+"PM");
                }
                else
                {
                    holder.textView3.setText(hour+":"+min+" "+"AM");
                }

            }
        }




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}