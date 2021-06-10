package com.example.hostelautomation.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hostelautomation.Adapter.Activity;
import com.example.hostelautomation.Adapter.RoomsAdapter;
import com.example.hostelautomation.MainActivity;
import com.example.hostelautomation.R;
import com.example.hostelautomation.RoomActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class MainFragment extends Fragment {

    private static final String PREFS_NAME = "userName";
    private static final String MY_PREFS_NAME="Activities";
    private static final String PREFS_ROOM = "Room_notification";


    RecyclerView recyclerView;
    public RecyclerView rc;
    ArrayList<Pair<Pair<String, Integer>,String>> list;

    ArrayList<String> list1;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    RoomsAdapter RecyclerViewHorizontalAdapter;
    LinearLayoutManager HorizontalLayout ;
    public  RecyclerView.LayoutManager RecyclerViewLayoutManager1;
    public Activity RecyclerViewHorizontalAdapter1;
    public LinearLayoutManager HorizontalLayout1 ;
    View ChildView ;
    int RecyclerViewItemPosition ;
    CircleImageView civ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPreferences preferences4;
    TextView nb;
    int i=0;
    boolean mod;
    String one;
    int count[];
    int f=0;

    TextView userName;
     SharedPreferences.Editor editor2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        userName = (TextView) view.findViewById(R.id.user_name);
        civ=view.findViewById(R.id.profile_image);
//        getActivity().onBackPressed();



        SharedPreferences preferences = this.getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences prefs = getContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        String name = preferences.getString("user_name", "User");
        String image=preferences.getString("user_profile","user_profile");
        userName.setText(name);
        Glide.with(MainFragment.this)
               .load(image)
          .into(civ);
        //RecyclerViewHorizontalAdapter1.notifyDataSetChanged();
       // nb=view.findViewById(R.id.badgetext);
      //  nb.setText("9");

       // update();


        recyclerView = (RecyclerView) view.findViewById(R.id.roomsRecycler);
        rc = (RecyclerView) view.findViewById(R.id.roomsRecycler1);
        RecyclerViewLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(RecyclerViewLayoutManager);
        RecyclerViewLayoutManager1 = new LinearLayoutManager(getContext());
        rc.setLayoutManager(RecyclerViewLayoutManager1);

        // Adding items to recycler view
        Set<String>h=new HashSet<>(prefs.getStringSet(MY_PREFS_NAME, new HashSet<String>()));
        list = new ArrayList<>();
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
        System.out.println(list1+"ssdsdfsdfsdfsdfs");

       // Check check=new Check();
       // check.execute();
        AddItemsToRecyclerView addItemsToRecyclerView = new AddItemsToRecyclerView();
        System.out.println("GOODGOD");
        addItemsToRecyclerView.execute();






        RecyclerViewHorizontalAdapter = new RoomsAdapter(list);

       // RecyclerViewHorizontalAdapter1 = new Activity(list1);

        HorizontalLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);
       // HorizontalLayout1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
       // rc.setLayoutManager(HorizontalLayout1);

        recyclerView.setAdapter(RecyclerViewHorizontalAdapter);
       // rc.setAdapter(RecyclerViewHorizontalAdapter1);
        databaseReference=firebaseDatabase.getInstance().getReference();
      editor2 = getActivity().getSharedPreferences(PREFS_ROOM, MODE_PRIVATE).edit();
         preferences4 = getActivity().getSharedPreferences(PREFS_ROOM, MODE_PRIVATE);
        String room[]=new String[4];

         count=new int[4];
         f=0;

        databaseReference.child("jash").child("kitchen").child("kc1").addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AddItemsToRecyclerView addItemsToRecyclerView = new AddItemsToRecyclerView();
                System.out.println("GOODGOD");
                addItemsToRecyclerView.execute();



                System.out.println(f+"jjjj");
                //  databaseReference.removeEventListener(this);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

























        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

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

                    Intent intent = new Intent(getContext(), RoomActivity.class);
                    intent.putExtra("room_name", list.get(RecyclerViewItemPosition).first.first);
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


    private class AddItemsToRecyclerView extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            System.out.println("HI");
            Check check=new Check();
            check.execute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            System.out.println("why");




          //  String o=preferences4.getString("kitchen","-1");
            list.add(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("livingroom", R.drawable.living_room), "6"));
            list.add(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("masterbedroom", R.drawable.master_bedroom), "par"));
            list.add(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("bedroom", R.drawable.bedroom), "pa"));
            list.add(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("kitchen", R.drawable.kitchen), "6"));
            // list.add(new Pair<String, Integer>("masterbedroom", R.drawable.master_bedroom));
            //list.add(new Pair<String, Integer>("bedroom", R.drawable.bedroom));
            //list.add(new Pair<String, Integer>("kitchen", R.drawable.kitchen));
            // nb.setText("8");
            return null;
        }
    }

        private class Check extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {

                databaseReference=firebaseDatabase.getInstance().getReference();
                databaseReference.child("jash").child("kitchen").child("kc1").addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        mod = dataSnapshot.getValue(Boolean.class);
                        System.out.println(mod);

                        if (mod == true) {
                            f++;
                            editor2.putString("kitchen", String.valueOf(f));
                            editor2.apply();

                        }
                        System.out.println(f+"jjjj");
                        //  databaseReference.removeEventListener(this);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                    });

                    databaseReference.child("jash").child("kitchen").child("kf1").addValueEventListener(new ValueEventListener() {


                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                if (d.getValue(Boolean.class) == true) {

                                    f++;
                                    editor2.putString("kitchen",String.valueOf(f));
                                    editor2.apply();
                                    break;
                                }
                            }

                            //databaseReference.removeEventListener(this);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                databaseReference.child("jash").child("kitchen").child("kl1").addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        mod = dataSnapshot.getValue(Boolean.class);
                        System.out.println(mod);

                        if(mod==true){
                            f++;
                            editor2.putString("kitchen",String.valueOf(f));
                            editor2.apply();
                        }




                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }


                });
                databaseReference.child("jash").child("kitchen").child("kro").addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        mod = dataSnapshot.getValue(Boolean.class);
                        System.out.println(mod);

                        if(mod==true){
                            f++;
                            editor2.putString("kitchen",String.valueOf(f));
                            editor2.apply();

                        }

                        //databaseReference.removeEventListener(this);


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }


                });






                return null;
            }


        }





}
