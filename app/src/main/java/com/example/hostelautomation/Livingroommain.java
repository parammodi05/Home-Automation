package com.example.hostelautomation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.hostelautomation.Adapter.Activity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Livingroommain extends AppCompatActivity {
    EditText lr1, lr2;
    Button b;
    ArrayList<String> l=new ArrayList<>();
    ArrayList<String> list1;
    public  RecyclerView.LayoutManager RecyclerViewLayoutManager1;
    public Activity RecyclerViewHorizontalAdapter1;
    public LinearLayoutManager HorizontalLayout1 ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    View ChildView ;
    int RecyclerViewItemPosition ;
    private static final String PREFSS_NAME = "userName";

    private static final String P1 = "LR_IP1";
    private static final String P2 = "LR_IP2";
    private static final String MY_PREFS_NAME="Activities";
    protected void onDestroy() {
        super.onDestroy();
        finish();
        // databaseReference.removeEventListener(mf);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_living_room);
        RecyclerView recyclerView  = (RecyclerView) findViewById(R.id.actrec);
        LinearLayout emptyView = (LinearLayout) findViewById(R.id.empty_view);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(PREFSS_NAME, Context.MODE_PRIVATE);
        final String name = preferences.getString("user_name", "User");

        final SharedPreferences prefs = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        databaseReference=firebaseDatabase.getInstance().getReference();
        Set<String> h=new HashSet<>(prefs.getStringSet(MY_PREFS_NAME, new HashSet<String>()));
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
        RecyclerViewHorizontalAdapter1 = new Activity(list1);
        HorizontalLayout1 = new AutoFitGridLayoutManager(getApplicationContext(), 500);
        recyclerView.setLayoutManager(HorizontalLayout1);
        recyclerView.setAdapter(RecyclerViewHorizontalAdapter1);
        // notify();




        /*lr1 = view.findViewById(R.id.lr_ip1);
        lr2 = view.findViewById(R.id.lr_ip2);
        b = view.findViewById(R.id.lr_set);

        SharedPreferences preferences = getActivity().getSharedPreferences(P1, Context.MODE_PRIVATE);
        String ip = preferences.getString("lr_ip1", "255.255.0");
        if(!ip.equals("") && !ip.equals("255.255.0")) {
            lr1.setText(ip);
        }

        SharedPreferences preferences1 = getActivity().getSharedPreferences(P1, Context.MODE_PRIVATE);
        String ip2 = preferences1.getString("lr_ip2", "255.255.0");
        if(!ip2.equals("") && !ip2.equals("255.255.0")) {
            lr2.setText(ip2);
        }


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip1 = lr1.getText().toString();
                String ip2 = lr2.getText().toString();

                SharedPreferences.Editor p1 = getActivity().getSharedPreferences(P1, Context.MODE_PRIVATE).edit();
                p1.putString("lr_ip1", ip1);
                p1.apply();

                SharedPreferences.Editor p2 = getActivity().getSharedPreferences(P1, Context.MODE_PRIVATE).edit();
                p2.putString("lr_ip2", ip2);
                p2.apply();

                Toast.makeText(getContext(), "IP changed", Toast.LENGTH_SHORT).show();
            }
        });*/


        Button add=(Button)findViewById(R.id.addactivity);
        Button add1=(Button) findViewById(R.id.addactivity1);
        if (list1.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            // frameLayout.setVisibility(View.INVISIBLE);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   /* frameLayout.setVisibility(View.VISIBLE);
                    Fragment fragment = new BedroomFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame1, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();*/
                    Intent i=new Intent(getApplicationContext(), BedroomMain.class);

                    startActivity(i);
                    finish();


                }
            });

        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            add1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(getApplicationContext(),BedroomMain.class);

                    startActivity(i);
                    finish();

                }
            });
        }

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

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
                    String z=list1.get(RecyclerViewItemPosition);
                    String arr[]=z.split("[?]");
                    String activityname=arr[0];
                    String roomname=arr[2];





                    final SharedPreferences preferences = getApplicationContext().getSharedPreferences(activityname, Context.MODE_PRIVATE);
                    if(roomname.equals("kitchen"))
                    {
                        String app1=preferences.getString("RO","-1");

                        if(app1.equals("-1"))
                        {

                        }
                        else
                        {
                            String array[]=app1.split("[?]");
                            String code=array[0];
                            String appstate=array[1];
                            boolean b=appstate.equals("true")?true:false;
                            databaseReference.child(name).child(roomname).child(code).child("value").setValue(b);



                        }
                        String app2=preferences.getString("Light","-1");

                        if(app2.equals("-1"))
                        {

                        }
                        else
                        {
                            String array[]=app2.split("[?]");
                            String code=array[0];
                            String appstate=array[1];
                            boolean b=appstate.equals("true")?true:false;
                            databaseReference.child(name).child(roomname).child(code).child("value").setValue(b);


                        }
                        String app3=preferences.getString("Charging Plug","-1");
                        if(app3.equals("-1"))
                        {

                        }
                        else
                        {
                            String array[]=app3.split("[?]");
                            String code=array[0];
                            String appstate=array[1];
                            boolean b=appstate.equals("true")?true:false;
                            databaseReference.child(name).child(roomname).child(code).child("value").setValue(b);


                        }
                        String app4=preferences.getString("Fan","-1");
                        if(app4.equals("-1"))
                        {

                        }
                        else
                        {
                            String array[]=app4.split("[?]");
                            String code=array[0];
                            String appstate=array[1];
                            boolean b=appstate.equals("true")?true:false;
                            if(b)
                            {
                                databaseReference.child(name).child(roomname).child(code).child("s0").setValue(false);
                                databaseReference.child(name).child(roomname).child(code).child("s1").setValue(false);
                                databaseReference.child(name).child(roomname).child(code).child("s2").setValue(false);
                                databaseReference.child(name).child(roomname).child(code).child("s3").setValue(false);
                                databaseReference.child(name).child(roomname).child(code).child("s4").setValue(b);

                            }
                            else
                            {
                                databaseReference.child(name).child(roomname).child(code).child("s0").setValue(b);
                                databaseReference.child(name).child(roomname).child(code).child("s1").setValue(b);
                                databaseReference.child(name).child(roomname).child(code).child("s2").setValue(b);
                                databaseReference.child(name).child(roomname).child(code).child("s3").setValue(b);
                                databaseReference.child(name).child(roomname).child(code).child("s4").setValue(b);

                            }
                        }


                    }
                    else if(roomname.equals("bedroom"))
                    {
                        String app1=preferences.getString("Bathroom Light","-1");

                        if(app1.equals("-1"))
                        {

                        }
                        else
                        {
                            String array[]=app1.split("[?]");
                            String code=array[0];
                            String appstate=array[1];
                            boolean b=appstate.equals("true")?true:false;
                            databaseReference.child(name).child(roomname).child(code).child("value").setValue(b);



                        }
                        String app2=preferences.getString("Light","-1");

                        if(app2.equals("-1"))
                        {

                        }
                        else
                        {
                            String array[]=app2.split("[?]");
                            String code=array[0];
                            String appstate=array[1];
                            boolean b=appstate.equals("true")?true:false;
                            databaseReference.child(name).child(roomname).child(code).child("value").setValue(b);


                        }
                        String app3=preferences.getString("Charging Plug","-1");
                        if(app3.equals("-1"))
                        {

                        }
                        else
                        {
                            String array[]=app3.split("[?]");
                            String code=array[0];
                            String appstate=array[1];
                            boolean b=appstate.equals("true")?true:false;
                            databaseReference.child(name).child(roomname).child(code).child("value").setValue(b);


                        }
                        String app4=preferences.getString("Fan","-1");
                        if(app4.equals("-1"))
                        {

                        }
                        else
                        {
                            String array[]=app4.split("[?]");
                            String code=array[0];
                            String appstate=array[1];
                            boolean b=appstate.equals("true")?true:false;
                            if(b)
                            {
                                databaseReference.child(name).child(roomname).child(code).child("s0").setValue(false);
                                databaseReference.child(name).child(roomname).child(code).child("s1").setValue(false);
                                databaseReference.child(name).child(roomname).child(code).child("s2").setValue(false);
                                databaseReference.child(name).child(roomname).child(code).child("s3").setValue(false);
                                databaseReference.child(name).child(roomname).child(code).child("s4").setValue(b);

                            }
                            else
                            {
                                databaseReference.child(name).child(roomname).child(code).child("s0").setValue(b);
                                databaseReference.child(name).child(roomname).child(code).child("s1").setValue(b);
                                databaseReference.child(name).child(roomname).child(code).child("s2").setValue(b);
                                databaseReference.child(name).child(roomname).child(code).child("s3").setValue(b);
                                databaseReference.child(name).child(roomname).child(code).child("s4").setValue(b);

                            }
                        }
                    }
                    else if(roomname.equals("masterbedroom"))
                    {
                        String app1=preferences.getString("Light1","-1");

                        if(app1.equals("-1"))
                        {

                        }
                        else
                        {
                            String array[]=app1.split("[?]");
                            String code=array[0];
                            String appstate=array[1];
                            boolean b=appstate.equals("true")?true:false;
                            databaseReference.child(name).child(roomname).child(code).child("value").setValue(b);



                        }
                        String app2=preferences.getString("Light2","-1");

                        if(app2.equals("-1"))
                        {

                        }
                        else
                        {
                            String array[]=app2.split("[?]");
                            String code=array[0];
                            String appstate=array[1];
                            boolean b=appstate.equals("true")?true:false;
                            databaseReference.child(name).child(roomname).child(code).child("value").setValue(b);


                        }
                        String app3=preferences.getString("Charging Plug","-1");
                        if(app3.equals("-1"))
                        {

                        }
                        else
                        {
                            String array[]=app3.split("[?]");
                            String code=array[0];
                            String appstate=array[1];
                            boolean b=appstate.equals("true")?true:false;
                            databaseReference.child(name).child(roomname).child(code).child("value").setValue(b);


                        }
                        String app4=preferences.getString("Fan","-1");
                        if(app4.equals("-1"))
                        {

                        }
                        else
                        {
                            String array[]=app4.split("[?]");
                            String code=array[0];
                            String appstate=array[1];
                            boolean b=appstate.equals("true")?true:false;
                            if(b)
                            {
                                databaseReference.child(name).child(roomname).child(code).child("s0").setValue(false);
                                databaseReference.child(name).child(roomname).child(code).child("s1").setValue(false);
                                databaseReference.child(name).child(roomname).child(code).child("s2").setValue(false);
                                databaseReference.child(name).child(roomname).child(code).child("s3").setValue(false);
                                databaseReference.child(name).child(roomname).child(code).child("s4").setValue(b);

                            }
                            else
                            {
                                databaseReference.child(name).child(roomname).child(code).child("s0").setValue(b);
                                databaseReference.child(name).child(roomname).child(code).child("s1").setValue(b);
                                databaseReference.child(name).child(roomname).child(code).child("s2").setValue(b);
                                databaseReference.child(name).child(roomname).child(code).child("s3").setValue(b);
                                databaseReference.child(name).child(roomname).child(code).child("s4").setValue(b);

                            }
                        }
                    }
                    else
                    {
                        String app1=preferences.getString("TV","-1");

                        if(app1.equals("-1"))
                        {

                        }
                        else
                        {
                            String array[]=app1.split("[?]");
                            String code=array[0];
                            String appstate=array[1];
                            boolean b=appstate.equals("true")?true:false;
                            databaseReference.child(name).child(roomname).child(code).child("value").setValue(b);



                        }
                        String app2=preferences.getString("Light","-1");

                        if(app2.equals("-1"))
                        {

                        }
                        else
                        {
                            String array[]=app2.split("[?]");
                            String code=array[0];
                            String appstate=array[1];
                            boolean b=appstate.equals("true")?true:false;
                            databaseReference.child(name).child(roomname).child(code).child("value").setValue(b);


                        }
                        String app3=preferences.getString("LED 1","-1");
                        if(app3.equals("-1"))
                        {

                        }
                        else
                        {
                            String array[]=app3.split("[?]");
                            String code=array[0];
                            String appstate=array[1];
                            boolean b=appstate.equals("true")?true:false;
                            databaseReference.child(name).child(roomname).child(code).child("value").setValue(b);


                        }
                        String app4=preferences.getString("Fan 1","-1");
                        if(app4.equals("-1"))
                        {

                        }
                        else
                        {
                            String array[]=app4.split("[?]");
                            String code=array[0];
                            String appstate=array[1];
                            boolean b=appstate.equals("true")?true:false;
                            if(b)
                            {
                                databaseReference.child(name).child(roomname).child(code).child("s0").setValue(false);
                                databaseReference.child(name).child(roomname).child(code).child("s1").setValue(false);
                                databaseReference.child(name).child(roomname).child(code).child("s2").setValue(false);
                                databaseReference.child(name).child(roomname).child(code).child("s3").setValue(false);
                                databaseReference.child(name).child(roomname).child(code).child("s4").setValue(b);

                            }
                            else
                            {
                                databaseReference.child(name).child(roomname).child(code).child("s0").setValue(b);
                                databaseReference.child(name).child(roomname).child(code).child("s1").setValue(b);
                                databaseReference.child(name).child(roomname).child(code).child("s2").setValue(b);
                                databaseReference.child(name).child(roomname).child(code).child("s3").setValue(b);
                                databaseReference.child(name).child(roomname).child(code).child("s4").setValue(b);

                            }
                        }
                        String app5=preferences.getString("LED 2","-1");
                        if(app5.equals("-1"))
                        {

                        }
                        else
                        {
                            String array[]=app5.split("[?]");
                            String code=array[0];
                            String appstate=array[1];
                            boolean b=appstate.equals("true")?true:false;
                            databaseReference.child(name).child(roomname).child(code).child("value").setValue(b);


                        }
                        String app6=preferences.getString("LED 3","-1");
                        if(app6.equals("-1"))
                        {

                        }
                        else
                        {
                            String array[]=app6.split("[?]");
                            String code=array[0];
                            String appstate=array[1];
                            boolean b=appstate.equals("true")?true:false;
                            databaseReference.child(name).child(roomname).child(code).child("value").setValue(b);


                        }
                        String app7=preferences.getString("LED 4","-1");
                        if(app7.equals("-1"))
                        {

                        }
                        else
                        {
                            String array[]=app7.split("[?]");
                            String code=array[0];
                            String appstate=array[1];
                            boolean b=appstate.equals("true")?true:false;
                            databaseReference.child(name).child(roomname).child(code).child("value").setValue(b);


                        }
                        String app8=preferences.getString("Chandelier","-1");
                        if(app3.equals("-1"))
                        {

                        }
                        else
                        {
                            String array[]=app8.split("[?]");
                            String code=array[0];
                            String appstate=array[1];
                            boolean b=appstate.equals("true")?true:false;
                            databaseReference.child(name).child(roomname).child(code).child("value").setValue(b);


                        }
                    }






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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(Livingroommain.this,NavigationDrawer.class);
        finish();
        startActivity(i);

    }

}

