package com.example.hostelautomation.Adapter;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.hostelautomation.Act;
import com.example.hostelautomation.R;

import java.util.List;

public class Actadapter extends RecyclerView.Adapter<Actadapter.MyView> {

    private List<Act> list;

    public class MyView extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageView imageView;
        public CheckBox cb;
        public Switch sw;

        public MyView(View view) {
            super(view);

            textView = (TextView) view.findViewById(R.id.stateName);
            imageView = (ImageView) view.findViewById(R.id.stateIcon);
            cb=(CheckBox)view.findViewById(R.id.checkbox);
            sw=(Switch)view.findViewById(R.id.switch1);



        }


    }

    public Actadapter(List<Act> horizontalList) {
        this.list = horizontalList;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_state, parent, false);
        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, final int position) {
        final Act act = list.get(position);

        holder.textView.setText(act.getApptext());
        holder.imageView.setImageResource(act.getAppimage());

        holder.cb.setOnCheckedChangeListener(null);
       // holder.sw.setOnCheckedChangeListener(null);
       holder.cb.setChecked(act.isChecked1());
        holder.sw.setChecked(act.isChecked2());




        //if true, your checkbox will be selected, else unselected

        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //set your object's last status

                if(isChecked==true)
                {
                    act.setChecked1(true);
                    holder.sw.setVisibility(View.VISIBLE);
                }
                else
                {
                    act.setChecked1(false);
                    holder.sw.setVisibility(View.INVISIBLE);

                }
                //notifyDataSetChanged();

            }
        });

        holder.sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true)
                {
                    act.setChecked2(true);
                }
                else
                {
                    act.setChecked2(false);

                }
              //  notifyDataSetChanged();


            }
        });






    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}