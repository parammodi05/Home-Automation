package com.example.hostelautomation.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.example.hostelautomation.Adapter.Activity;
import com.example.hostelautomation.Adapter.Timeradapter;
import com.example.hostelautomation.AutoFitGridLayoutManager;
import com.example.hostelautomation.BedroomMain;
import com.example.hostelautomation.R;
import com.example.hostelautomation.Timer;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MasterBedroomFragment extends Fragment {

    EditText mb;
    Button b;
    ArrayList<String> list1;

    private static final String P = "MB_IP1";
    private static final String MY_TIMERS = "Timers";
    public Timeradapter RecyclerViewHorizontalAdapter1;
    public LinearLayoutManager HorizontalLayout1 ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_master_bedroom, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

      /*  mb = view.findViewById(R.id.mb_ip1);
        b = view.findViewById(R.id.mb_set);

        SharedPreferences preferences = getActivity().getSharedPreferences(P, Context.MODE_PRIVATE);
        String ip = preferences.getString("mb_ip1", "255.255.0");
        if(!ip.equals("") && !ip.equals("255.255.0")) {
            mb.setText(ip);
        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip1 = mb.getText().toString();

                SharedPreferences.Editor p1 = getActivity().getSharedPreferences(P, Context.MODE_PRIVATE).edit();
                p1.putString("mb_ip1", ip1);
                p1.apply();

                Toast.makeText(getContext(), "IP changed", Toast.LENGTH_SHORT).show();
            }
        });*/
        RecyclerView recyclerView  = (RecyclerView) view.findViewById(R.id.timerrec);
        LinearLayout emptyView = (LinearLayout) view.findViewById(R.id.empty_view1);
        final SharedPreferences prefs = getContext().getSharedPreferences(MY_TIMERS, Context.MODE_PRIVATE);
        Button add=(Button)view.findViewById(R.id.addtimer);
        Button add1=view.findViewById(R.id.addtimer1);

        Set<String> h=new HashSet<>(prefs.getStringSet(MY_TIMERS, new HashSet<String>()));
        list1 = new ArrayList<>();

        for(String s:h)
        {
            if(s.isEmpty()){

            }
            else
            {
                list1.add(s);
            }

        }
        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.VERTICAL, true);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new Timeradapter(list1));
        recyclerView.addOnScrollListener(new CenterScrollListener());
      //  RecyclerViewHorizontalAdapter1 = new Timeradapter(list1);
       // HorizontalLayout1 = new AutoFitGridLayoutManager(getContext(), 500);
        //recyclerView.setLayoutManager(HorizontalLayout1);


        //recyclerView.setAdapter(RecyclerViewHorizontalAdapter1);

        if (list1.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            // frameLayout.setVisibility(View.INVISIBLE);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i=new Intent(getContext(), Timer.class);
                    getActivity().finish();
                    startActivity(i);


                }
            });

        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            add1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(getContext(), Timer.class);
                    getActivity().finish();
                    startActivity(i);

                }
            });
        }




    }

}
