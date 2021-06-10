package com.example.hostelautomation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hostelautomation.Adapter.Activity;
import com.example.hostelautomation.Adapter.RoomsAdapter;
import com.example.hostelautomation.Fragments.MainFragment;
import com.jackandphantom.circularimageview.CircleImage;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.shrikanthravi.customnavigationdrawer2.data.MenuItem;
import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "userName";
    private static final int GalleryPick = 1;
    private static final String MY_PREFS_NAME="Activities";

    RecyclerView recyclerView;
    RecyclerView rc;
    ArrayList<Pair<String, Integer>> list;
    ArrayList<String> list1;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    RoomsAdapter RecyclerViewHorizontalAdapter;
    LinearLayoutManager HorizontalLayout ;
    RecyclerView.LayoutManager RecyclerViewLayoutManager1;
    Activity RecyclerViewHorizontalAdapter1;
    LinearLayoutManager HorizontalLayout1 ;
    View ChildView ;
    int RecyclerViewItemPosition ;
    CircularImageView circularImageView;
    ImageButton img;


    TextView userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = (TextView) findViewById(R.id.user_name);

        //civ=(CircleImageView)findViewById(R.id.profile_image);
       /* CircleImage circleImage = (CircleImage) findViewById(R.id.circleImage);
        circleImage.setBorderColor(Color.BLACK);
        circleImage.setBorderWidth(5);
        circleImage.setAddShadow(true);
        circleImage.setShadowRadius(20);
        circleImage.setShadowColor(Color.parseColor("#a10909"));
        circleImage.setImageResource(R.drawable.living_room);*/










        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String name = preferences.getString("user_name", "User");
       final String image=preferences.getString("user_profile","owner");

        img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
              opengallery();
            }
        });

      // img.setBackgroundResource(R.drawable.living_room);

      //  Uri imageuri=Uri.parse(image);
       // img.setImageURI(imageuri);

        //circleImage.loadHighResolutionImage(m);


        //Glide.with(MainActivity.this)
        //       .load(image)
              //  .into(img);
        userName.setText(name);

        recyclerView = (RecyclerView)findViewById(R.id.roomsRecycler);
        rc = (RecyclerView)findViewById(R.id.roomsRecycler1);
        RecyclerViewLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(RecyclerViewLayoutManager);
        RecyclerViewLayoutManager1 = new LinearLayoutManager(MainActivity.this);
        rc.setLayoutManager(RecyclerViewLayoutManager1);

        // Adding items to recycler view
        Set<String> h=prefs.getStringSet(MY_PREFS_NAME, Collections.<String>emptySet());
        list = new ArrayList<>();
        list1 = new ArrayList<>();

        for(String s:h)
        {
            list1.add(s);
        }
        System.out.println(list1);

        MainActivity.AddItemsToRecyclerView addItemsToRecyclerView = new MainActivity.AddItemsToRecyclerView();
        addItemsToRecyclerView.execute();

       // RecyclerViewHorizontalAdapter = new RoomsAdapter(list,ge);
        RecyclerViewHorizontalAdapter1 = new Activity(list1);

        HorizontalLayout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);
        HorizontalLayout1 = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rc.setLayoutManager(HorizontalLayout1);

        recyclerView.setAdapter(RecyclerViewHorizontalAdapter);
        rc.setAdapter(RecyclerViewHorizontalAdapter1);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

                ChildView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {

                    //Getting clicked value.
                    RecyclerViewItemPosition = recyclerView.getChildAdapterPosition(ChildView);

                    Intent intent = new Intent(MainActivity.this, RoomActivity.class);
                    intent.putExtra("room_name", list.get(RecyclerViewItemPosition).first);
                    System.err.println(list.get(RecyclerViewItemPosition).first);
                    startActivity(intent);
                }

                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });

    }

    private void opengallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        //startActivityForResult(galleryIntent, GalleryPick);
        startActivityForResult(galleryIntent,GalleryPick);
    }

    private class AddItemsToRecyclerView extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            list.add(new Pair<String, Integer>("livingroom", R.drawable.living_room));
            list.add(new Pair<String, Integer>("masterbedroom", R.drawable.master_bedroom));
            list.add(new Pair<String, Integer>("bedroom", R.drawable.bedroom));
            list.add(new Pair<String, Integer>("kitchen", R.drawable.kitchen));
            return null;
        }
    }


}
