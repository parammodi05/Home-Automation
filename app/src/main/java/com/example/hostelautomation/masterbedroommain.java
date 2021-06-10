package com.example.hostelautomation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.example.hostelautomation.Adapter.Timeradapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class masterbedroommain extends AppCompatActivity {


    EditText mb;
    Button b;
    ArrayList<String> list1;

    private static final String P = "MB_IP1";
    private static final String MY_TIMERS = "Timers";
    public Timeradapter RecyclerViewHorizontalAdapter1;
    public LinearLayoutManager HorizontalLayout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_bedroom);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.timerrec);
        LinearLayout emptyView = (LinearLayout) findViewById(R.id.empty_view1);
        final SharedPreferences prefs = getApplicationContext().getSharedPreferences(MY_TIMERS, Context.MODE_PRIVATE);
        Button add = (Button) findViewById(R.id.addtimer);
        Button add1 = (Button) findViewById(R.id.addtimer1);

        Set<String> h = new HashSet<>(prefs.getStringSet(MY_TIMERS, new HashSet<String>()));
        list1 = new ArrayList<>();

        for (String s : h) {
            if (s.isEmpty()) {

            } else {
                list1.add(s);
            }

        }
        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.VERTICAL, true);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new Timeradapter(list1));
        recyclerView.addOnScrollListener(new CenterScrollListener());


        if (list1.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(masterbedroommain.this, Timer.class);

                    startActivity(i);
                    finish();


                }
            });

        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            add1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(masterbedroommain.this, Timer.class);
                    startActivity(i);
                    finish();

                }
            });
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(masterbedroommain.this,NavigationDrawer.class);
        startActivity(i);
        finish();
    }
}




