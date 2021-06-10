package com.example.hostelautomation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.hostelautomation.Adapter.ApplianceAdapter;
import com.example.hostelautomation.Dialog.CustomDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RoomActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "AppliancesStates";
    private static final String ROOM_NAME = "Room";
    private static final String PREFSS_NAME = "userName";
    private static final String SEPERATOR = "/";
    private static final String PREFS_FAN = "fan_selected";
    private static final String PREFS_ROOM = "Room_notification";

    public static FragmentManager fm;

    private static final String LIVING_ROOM_1 = "LR_IP1";
    private static final String MASTER_BEDROOM = "MB_IP1";
    private static final String BEDROOM = "B_IP1";
    private static final String KITCHEN = "K_IP1";

    private static final Integer TV = 1;
    private static final Integer FAN = 2;
    private static final Integer LIGHT = 3;
    private static final Integer CHANDELIER = 4;
    private static final Integer SOCKET = 5;

    private float mDownX;
    private float mDownY;
    private final float SCROLL_THRESHOLD = 10;
    private boolean isOnClick;
    private int count1=0;
    private int count2=0;
    private int count3=0;
    private int count4=0;


    RecyclerView recyclerView;
    ArrayList<Pair<Pair<Pair<String, Integer>, String>, Integer>> list;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    ApplianceAdapter RecyclerViewHorizontalAdapter;
    LinearLayoutManager HorizontalLayout ;
    View ChildView ;
    int RecyclerViewItemPosition ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String ip = "255.255.255.0";

    ImageView back;
    RelativeLayout roomRelativeLayout;
    TextView roomName;
    String room;
    boolean mod;
    boolean click;
    ValueEventListener mf;

    WebView webView;

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // databaseReference.removeEventListener(mf);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);



        recyclerView = (RecyclerView) findViewById(R.id.roomAppliancesRecycler);
        back = (ImageView) findViewById(R.id.back_arrow);
        roomRelativeLayout = (RelativeLayout) findViewById(R.id.roomRelativeLayout);
        roomName = (TextView) findViewById(R.id.room);
        webView = (WebView) findViewById(R.id.webView);
        databaseReference=firebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        room = intent.getStringExtra("room_name");
        roomName.setText(room);


       /* switch (room) {
            case "livingroom":
                SharedPreferences preferences1 = getSharedPreferences(LIVING_ROOM_1, MODE_PRIVATE);
               // ip = preferences1.getString("lr_ip2", "255.255.0");
                break;
            case "masterbedroom":
                SharedPreferences preferences3 = getSharedPreferences(MASTER_BEDROOM, MODE_PRIVATE);
               // ip = preferences3.getString("mb_ip1", "255.255.0");
                break;
            case "bedroom":
                SharedPreferences preferences4 = getSharedPreferences(BEDROOM, MODE_PRIVATE);
               // ip = preferences4.getString("b_ip1", "255.255.0");
                break;
            case "kitchen":
                SharedPreferences preferences5 = getSharedPreferences(KITCHEN, MODE_PRIVATE);
               // ip = preferences5.getString("k_ip1", "255.255.0");
                break;
            default:
                break;
        }
        */
        setBackground(room);



       // System.err.println(room);

        final SharedPreferences.Editor editor = getSharedPreferences(ROOM_NAME, MODE_PRIVATE).edit();
        editor.putString("room_name", room);
        editor.apply();

        SharedPreferences preferences = getSharedPreferences(PREFSS_NAME, MODE_PRIVATE);
        final String name = preferences.getString("user_name", "User");
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(RecyclerViewLayoutManager);

        // Adding items to recycler view
        list = new ArrayList<>();
        LoadAppliances loadAppliances = new LoadAppliances();
        loadAppliances.execute(room);

        RecyclerViewHorizontalAdapter = new ApplianceAdapter(list);

        HorizontalLayout = new ScaleLayoutManager(RoomActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);




        recyclerView.setAdapter(RecyclerViewHorizontalAdapter);

        room=room.toLowerCase();

        click=false;



        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(RoomActivity.this, new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }


                @Override  public void onLongPress(MotionEvent e) {
                    return;
                }

                @Override
                public boolean onDoubleTap(MotionEvent ee) {
                    return false;
                }
            });


            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

                ChildView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
              //  System.err.println(ChildView);

                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {


                    //Getting clicked value.
                    RecyclerViewItemPosition = recyclerView.getChildAdapterPosition(ChildView);
                     String codee = list.get(RecyclerViewItemPosition).first.second;
                   final String code=codee.toLowerCase();

                    click=true;


                    final SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                    final SharedPreferences.Editor editor2 = getSharedPreferences(PREFS_ROOM, MODE_PRIVATE).edit();
                    SharedPreferences preferences4 = getSharedPreferences(PREFS_ROOM, MODE_PRIVATE);


                    SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

                    if(code.equals("kf1")||code.equals("hf")||code.equals("r1f1")||code.equals("r2f1"))
                    {
                        databaseReference.child(name).child(room).child(code).addValueEventListener(new ValueEventListener() {
                            boolean b=false;
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("s0").getValue(Boolean.class)||dataSnapshot.child("s1").getValue(Boolean.class)
                                        ||dataSnapshot.child("s2").getValue(Boolean.class)
                                        ||dataSnapshot.child("s3").getValue(Boolean.class)
                                        ||dataSnapshot.child("s4").getValue(Boolean.class))
                                {
                                    editor.putBoolean(code,true);
                                    editor.apply();


                                }
                                else{
                                    editor.putBoolean(code,false);
                                    editor.apply();
                                }
                                databaseReference.removeEventListener(this);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                        else
                            {

                    databaseReference.child(name).child(room).child(code).child("value").addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                mod = dataSnapshot.getValue(Boolean.class);

                                if(mod==true){
                                    editor.putBoolean(code,true);
                                    editor.apply();
                                }
                                else{
                                    editor.putBoolean(code,false);
                                    editor.apply();
                                }
                                databaseReference.removeEventListener(this);


                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }


                        });
                    }




                    if(preferences.getBoolean(code,false))
                    {
                        String appl_name = list.get(RecyclerViewItemPosition).first.first.first;
                        String appl_code = list.get(RecyclerViewItemPosition).first.second;

                        if(appl_name.equals("Fan"))
                        {
                            databaseReference.child(name).child(room).child(code).child("s4").setValue(false);
                            databaseReference.child(name).child(room).child(code).child("s3").setValue(false);
                            databaseReference.child(name).child(room).child(code).child("s2").setValue(false);
                            databaseReference.child(name).child(room).child(code).child("s1").setValue(false);
                            databaseReference.child(name).child(room).child(code).child("s0").setValue(false);

                        }

                        else
                            {
                             databaseReference.child(name).child(room).child(code).child("value").setValue(false);
                             System.out.println(code);
                            }



                        // Appliance is ON
                    //    SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean(code, false);
                        editor.apply();

                       /* if(room.equals("Living Room")) {
                           // System.err.println(1);
                            String name = list.get(RecyclerViewItemPosition).first.first.first;
                            if(name.equals("TV") || name.equals("Chandelier") || name.equals("Light") || name.equals("Fan 1")) {
                                SharedPreferences preferences20 = getSharedPreferences(LIVING_ROOM_1, MODE_PRIVATE);
                                ip = preferences20.getString("lr_ip1", "255.255.0");
                            }
                            else {
                                System.err.println(name);
                                SharedPreferences preferences21 = getSharedPreferences(LIVING_ROOM_1, MODE_PRIVATE);
                                ip = preferences21.getString("lr_ip2", "255.255.0");
                            }
                        }*/

                       // webView.loadUrl("http://" + ip + SEPERATOR + code + "0");
                        //Toast.makeText(getApplicationContext(), ip, Toast.LENGTH_LONG).show();



                        switch (list.get(RecyclerViewItemPosition).second) {
                            case 1:
                                list.add(RecyclerViewItemPosition, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(appl_name, R.drawable.tv_off), appl_code), TV));
                                break;
                            case 2:
                                list.add(RecyclerViewItemPosition, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(appl_name, R.drawable.fan_off), appl_code), FAN));
                                break;
                            case 3:
                                list.add(RecyclerViewItemPosition, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(appl_name, R.drawable.light_bulb_off), appl_code), LIGHT));
                                break;
                            case 4:
                                list.add(RecyclerViewItemPosition, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(appl_name, R.drawable.chandelier_off), appl_code), CHANDELIER));
                                break;
                            case 5:
                                list.add(RecyclerViewItemPosition, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(appl_name, R.drawable.socket_off), appl_code), SOCKET));
                                break;
                            default:
                                break;
                        }

                        list.remove(RecyclerViewItemPosition+1);
                       // System.out.println(list);
                       // RecyclerViewHorizontalAdapter = new ApplianceAdapter(list);
                        RecyclerViewHorizontalAdapter.notifyDataSetChanged();
                    }
                    else {
                        // Appliance is OFF

                        String appl_name = list.get(RecyclerViewItemPosition).first.first.first;
                        String appl_code = list.get(RecyclerViewItemPosition).first.second;
                      //  System.out.println(appl_name);
                        if(appl_name.equals("Fan"))
                        {
                            databaseReference.child(name).child(room).child(code).child("s4").setValue(true);
                            databaseReference.child(name).child(room).child(code).child("s0").setValue(false);
                        }
                        else
                            {

                            databaseReference.child(name).child(room).child(code).child("value").setValue(true);
                        }


                        //SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean(code, true);
                        editor.apply();

                       /* if(room.equals("Living Room")) {
                            String name = list.get(RecyclerViewItemPosition).first.first.first;
                            if(name.equals("TV") || name.equals("Chandelier") || name.equals("Light") || name.equals("Fan 1")) {
                                SharedPreferences preferences20 = getSharedPreferences(LIVING_ROOM_1, MODE_PRIVATE);
                                ip = preferences20.getString("lr_ip1", "255.255.0");
                            }
                            else {
                                SharedPreferences preferences21 = getSharedPreferences(LIVING_ROOM_1, MODE_PRIVATE);
                                ip = preferences21.getString("lr_ip2", "255.255.0");
                            }
                        }*/

                        //webView.loadUrl("http://" + ip + SEPERATOR + code + "1");
                        //Toast.makeText(getApplicationContext(), ip, Toast.LENGTH_LONG).show();



                        switch (list.get(RecyclerViewItemPosition).second) {
                            case 1:
                                list.add(RecyclerViewItemPosition, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(appl_name, R.drawable.tv_on), appl_code), TV));
                                break;
                            case 2:
                                list.add(RecyclerViewItemPosition, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(appl_name, R.drawable.fan_on), appl_code), FAN));
                                break;
                            case 3:
                                list.add(RecyclerViewItemPosition, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(appl_name, R.drawable.light_bulb_on), appl_code), LIGHT));
                                break;
                            case 4:
                                list.add(RecyclerViewItemPosition, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(appl_name, R.drawable.chandelier_on), appl_code), CHANDELIER));
                                break;
                            case 5:
                                list.add(RecyclerViewItemPosition, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(appl_name, R.drawable.socket_on), appl_code), SOCKET));
                                break;
                            default:
                                break;
                        }

                        list.remove(RecyclerViewItemPosition+1);
                      //  System.out.println(list);
                      //  RecyclerViewHorizontalAdapter = new ApplianceAdapter(list);
                        RecyclerViewHorizontalAdapter.notifyDataSetChanged();
                    }
                }
                else {

                    switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                           mDownX = motionEvent.getX();
                            mDownY = motionEvent.getY();


                            isOnClick = true;


                            break;
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP:
                            if (isOnClick) {

                                RecyclerViewItemPosition = recyclerView.getChildAdapterPosition(ChildView);
                                if(RecyclerViewItemPosition==-1){

                                }
                                else
                                    {
                                        Integer type = list.get(RecyclerViewItemPosition).second;
                                        if (type == 2) {


                                            if (room.equals("livingroom")) {
                                                SharedPreferences.Editor editor1 = getSharedPreferences(PREFS_FAN, MODE_PRIVATE).edit();
                                                editor1.putString("fan_selected", list.get(RecyclerViewItemPosition).first.first.first);
                                                editor1.apply();
                                            }
                                            CustomDialog customDialog = new CustomDialog(RoomActivity.this);
                                            customDialog.show();
                                            customDialog.setCanceledOnTouchOutside(false);

                                        }

                                }

                            }

                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (isOnClick && (Math.abs(mDownX - motionEvent.getX()) > SCROLL_THRESHOLD || Math.abs(mDownY - motionEvent.getY()) > SCROLL_THRESHOLD)) {
                                isOnClick = false;
                            }
                            break;
                        default:
                            break;
                    }
                }

                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent event) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(room.equals("kitchen")) {

            databaseReference.child(name).child(room).child("kl1").child("value").addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    boolean b = dataSnapshot.getValue(Boolean.class);
                    int p = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).first.second.equals("kl1")) {
                            p = i;
                            break;
                        }
                    }
                    if (b == true) {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Light", R.drawable.light_bulb_on), "kl1"), LIGHT));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("kl1", true);
                        editor.apply();

                    } else {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Light", R.drawable.light_bulb_off), "kl1"), LIGHT));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("kl1", false);
                        editor.apply();

                    }
                    list.remove(p + 1);
                    RecyclerViewHorizontalAdapter.notifyDataSetChanged();
                    databaseReference.removeEventListener(this);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            databaseReference.child(name).child(room).child("kf1").addValueEventListener(new ValueEventListener() {
                boolean b=false;
                String speed="";
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   //dataSnapshot.child("s0").getValue(Boolean.class);

                  //  System.out.println(dataSnapshot);
                  //  System.out.println(b);
                    // boolean b = dataSnapshot.getValue(Boolean.class);
                    int p = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).first.second.equals("kf1")) {
                            p = i;
                            break;
                        }
                    }
                    if (dataSnapshot.child("s0").getValue(Boolean.class)||dataSnapshot.child("s1").getValue(Boolean.class)
                    ||dataSnapshot.child("s2").getValue(Boolean.class)
                    ||dataSnapshot.child("s3").getValue(Boolean.class)
                    ||dataSnapshot.child("s4").getValue(Boolean.class))
                    {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Fan", R.drawable.fan_on), "kf1"), FAN));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("kf1", true);
                        editor.apply();

                    }
                    else
                        {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Fan", R.drawable.fan_off), "kf1"), FAN));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("kf1", false);
                        editor.apply();

                    }
                    list.remove(p + 1);
                   // RecyclerViewHorizontalAdapter = new ApplianceAdapter(list);
                    RecyclerViewHorizontalAdapter.notifyDataSetChanged();
                    databaseReference.removeEventListener(this);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            databaseReference.child(name).child(room).child("kc1").child("value").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // System.out.println(dataSnapshot);
                    boolean b = dataSnapshot.getValue(Boolean.class);
                    int p = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).first.second.equals("kc1")) {
                            p = i;
                            break;
                        }
                    }
                    if (b == true) {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Charging Plug", R.drawable.socket_on), "kc1"), SOCKET));
                         SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                         editor.putBoolean("kc1", true);
                         editor.apply();

                    } else {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Charging Plug", R.drawable.socket_off), "kc1"), SOCKET));
                         SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("kc1", false);
                        editor.apply();

                    }
                    list.remove(p + 1);
                    RecyclerViewHorizontalAdapter.notifyDataSetChanged();
                    databaseReference.removeEventListener(this);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            databaseReference.child(name).child(room).child("kro").child("value").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   // System.out.println(dataSnapshot);
                    boolean b = dataSnapshot.getValue(Boolean.class);
                    int p = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).first.second.equals("kro")) {
                            p = i;
                            break;
                        }
                    }
                    if (b == true) {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("RO", R.drawable.socket_on), "kro"), SOCKET));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("kro", true);
                        editor.apply();

                    } else {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("RO", R.drawable.socket_off), "kro"), SOCKET));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("kro", false);
                        editor.apply();

                    }
                    list.remove(p + 1);
                   // RecyclerViewHorizontalAdapter = new ApplianceAdapter(list);
                    RecyclerViewHorizontalAdapter.notifyDataSetChanged();
                    databaseReference.removeEventListener(this);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        if(room.equals("bedroom")) {

            databaseReference.child(name).child(room).child("r2l1").child("value").addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //  System.out.println(dataSnapshot);
                    boolean b = dataSnapshot.getValue(Boolean.class);
                    int p = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).first.second.equals("r2l1")) {
                            p = i;
                            break;
                        }
                    }
                    if (b == true) {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Light", R.drawable.light_bulb_on), "r2l1"), LIGHT));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("r2l1", true);
                        editor.apply();

                    } else {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Light", R.drawable.light_bulb_off), "r2l1"), LIGHT));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("r2l1", false);
                        editor.apply();

                    }
                    list.remove(p + 1);
                    RecyclerViewHorizontalAdapter.notifyDataSetChanged();
                    databaseReference.removeEventListener(this);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            databaseReference.child(name).child(room).child("r2f1").addValueEventListener(new ValueEventListener() {
                boolean b=false;
                String speed="";
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //dataSnapshot.child("s0").getValue(Boolean.class);

                    //  System.out.println(dataSnapshot);
                    //  System.out.println(b);
                    // boolean b = dataSnapshot.getValue(Boolean.class);
                    int p = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).first.second.equals("r2f1")) {
                            p = i;
                            break;
                        }
                    }
                    if (dataSnapshot.child("s0").getValue(Boolean.class)||dataSnapshot.child("s1").getValue(Boolean.class)
                            ||dataSnapshot.child("s2").getValue(Boolean.class)
                            ||dataSnapshot.child("s3").getValue(Boolean.class)
                            ||dataSnapshot.child("s4").getValue(Boolean.class))
                    {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Fan", R.drawable.fan_on), "r2f1"), FAN));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("r2f1", true);
                        editor.apply();

                    }
                    else
                    {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Fan", R.drawable.fan_off), "r2f1"), FAN));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("r2f1", false);
                        editor.apply();

                    }
                    list.remove(p + 1);
                    // RecyclerViewHorizontalAdapter = new ApplianceAdapter(list);
                    RecyclerViewHorizontalAdapter.notifyDataSetChanged();
                    databaseReference.removeEventListener(this);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            databaseReference.child(name).child(room).child("r2c1").child("value").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    boolean b = dataSnapshot.getValue(Boolean.class);
                    int p = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).first.second.equals("r2c1")) {
                            p = i;
                            break;
                        }
                    }
                    if (b == true) {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Charging Plug", R.drawable.socket_on), "r2c1"), SOCKET));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("r2c1", true);
                        editor.apply();

                    } else {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Charging Plug", R.drawable.socket_off), "r2c1"), SOCKET));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("r2c1", false);
                        editor.apply();

                    }
                    list.remove(p + 1);
                    RecyclerViewHorizontalAdapter.notifyDataSetChanged();
                    databaseReference.removeEventListener(this);

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            databaseReference.child(name).child(room).child("r2l2").child("value").addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //  System.out.println(dataSnapshot);
                    boolean b = dataSnapshot.getValue(Boolean.class);
                    int p = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).first.second.equals("r2l2")) {
                            p = i;
                            break;
                        }
                    }
                    if (b == true) {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Light", R.drawable.light_bulb_on), "r2l2"), LIGHT));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("r2l2", true);
                        editor.apply();

                    } else {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Light", R.drawable.light_bulb_off), "r2l2"), LIGHT));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("r2l2", false);
                        editor.apply();

                    }
                    list.remove(p + 1);
                    RecyclerViewHorizontalAdapter.notifyDataSetChanged();
                    databaseReference.removeEventListener(this);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        if(room.equals("masterbedroom")) {

            databaseReference.child(name).child(room).child("r1l1").child("value").addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //  System.out.println(dataSnapshot);
                    boolean b = dataSnapshot.getValue(Boolean.class);
                    int p = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).first.second.equals("r1l1")) {
                            p = i;
                            break;
                        }
                    }
                    if (b == true) {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Light", R.drawable.light_bulb_on), "r1l1"), LIGHT));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("r1l1", true);
                        editor.apply();

                    } else {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Light", R.drawable.light_bulb_off), "r1l1"), LIGHT));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("r1l1", false);
                        editor.apply();

                    }
                    list.remove(p + 1);
                    RecyclerViewHorizontalAdapter.notifyDataSetChanged();
                    databaseReference.removeEventListener(this);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            databaseReference.child(name).child(room).child("r1f1").addValueEventListener(new ValueEventListener() {
                boolean b=false;
                String speed="";
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //dataSnapshot.child("s0").getValue(Boolean.class);

                    //  System.out.println(dataSnapshot);
                    //  System.out.println(b);
                    // boolean b = dataSnapshot.getValue(Boolean.class);
                    int p = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).first.second.equals("r1f1")) {
                            p = i;
                            break;
                        }
                    }
                    if (dataSnapshot.child("s0").getValue(Boolean.class)||dataSnapshot.child("s1").getValue(Boolean.class)
                            ||dataSnapshot.child("s2").getValue(Boolean.class)
                            ||dataSnapshot.child("s3").getValue(Boolean.class)
                            ||dataSnapshot.child("s4").getValue(Boolean.class))
                    {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Fan", R.drawable.fan_on), "r1f1"), FAN));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("r1f1", true);
                        editor.apply();

                    }
                    else
                    {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Fan", R.drawable.fan_off), "r1f1"), FAN));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("r1f1", false);
                        editor.apply();

                    }
                    list.remove(p + 1);
                    // RecyclerViewHorizontalAdapter = new ApplianceAdapter(list);
                    RecyclerViewHorizontalAdapter.notifyDataSetChanged();
                    databaseReference.removeEventListener(this);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            databaseReference.child(name).child(room).child("r1c1").child("value").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // System.out.println(dataSnapshot);
                    boolean b = dataSnapshot.getValue(Boolean.class);
                    int p = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).first.second.equals("r1c1")) {
                            p = i;
                            break;
                        }
                    }
                    if (b == true) {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Charging Plug", R.drawable.socket_on), "r1c1"), SOCKET));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("r1c1", true);
                        editor.apply();

                    } else {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Charging Plug", R.drawable.socket_off), "r1c1"), SOCKET));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("r1c1", false);
                        editor.apply();

                    }
                    list.remove(p + 1);
                    RecyclerViewHorizontalAdapter.notifyDataSetChanged();
                    databaseReference.removeEventListener(this);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            databaseReference.child(name).child(room).child("r1l2").child("value").addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //  System.out.println(dataSnapshot);
                    boolean b = dataSnapshot.getValue(Boolean.class);
                    int p = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).first.second.equals("r1l2")) {
                            p = i;
                            break;
                        }
                    }
                    if (b == true) {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Light", R.drawable.light_bulb_on), "r1l2"), LIGHT));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("r1l2", true);
                        editor.apply();

                    } else {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Light", R.drawable.light_bulb_off), "r1l2"), LIGHT));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("r1l2", false);
                        editor.apply();

                    }
                    list.remove(p + 1);
                    RecyclerViewHorizontalAdapter.notifyDataSetChanged();
                    databaseReference.removeEventListener(this);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        if(room.equals("livingroom")) {

            databaseReference.child(name).child(room).child("hl1").child("value").addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //  System.out.println(dataSnapshot);
                    boolean b = dataSnapshot.getValue(Boolean.class);
                    int p = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).first.second.equals("hl1")) {
                            p = i;
                            break;
                        }
                    }
                    if (b == true) {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Light", R.drawable.light_bulb_on), "hl1"), LIGHT));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("hl1", true);
                        editor.apply();

                    } else {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Light", R.drawable.light_bulb_off), "hl1"), LIGHT));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("hl1", false);
                        editor.apply();

                    }
                    list.remove(p + 1);
                    RecyclerViewHorizontalAdapter.notifyDataSetChanged();
                    databaseReference.removeEventListener(this);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            databaseReference.child(name).child(room).child("hf").addValueEventListener(new ValueEventListener() {
                boolean b=false;
                String speed="";
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //dataSnapshot.child("s0").getValue(Boolean.class);

                    //  System.out.println(dataSnapshot);
                    //  System.out.println(b);
                    // boolean b = dataSnapshot.getValue(Boolean.class);
                    int p = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).first.second.equals("hf")) {
                            p = i;
                            break;
                        }
                    }
                    if (dataSnapshot.child("s0").getValue(Boolean.class)||dataSnapshot.child("s1").getValue(Boolean.class)
                            ||dataSnapshot.child("s2").getValue(Boolean.class)
                            ||dataSnapshot.child("s3").getValue(Boolean.class)
                            ||dataSnapshot.child("s4").getValue(Boolean.class))
                    {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Fan", R.drawable.fan_on), "hf"), FAN));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("hf", true);
                        editor.apply();

                    }
                    else
                    {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Fan", R.drawable.fan_off), "hf"), FAN));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("hf", false);
                        editor.apply();

                    }
                    list.remove(p + 1);
                    // RecyclerViewHorizontalAdapter = new ApplianceAdapter(list);
                    RecyclerViewHorizontalAdapter.notifyDataSetChanged();
                    databaseReference.removeEventListener(this);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            databaseReference.child(name).child(room).child("hc").child("value").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // System.out.println(dataSnapshot);
                    boolean b = dataSnapshot.getValue(Boolean.class);
                    int p = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).first.second.equals("hc")) {
                            p = i;
                            break;
                        }
                    }
                    if (b == true) {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Chandelier", R.drawable.chandelier_on), "hc"), CHANDELIER));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("hc", true);
                        editor.apply();

                    } else {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Chandelier", R.drawable.chandelier_off), "hc"), CHANDELIER));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("hc", false);
                        editor.apply();

                    }
                    list.remove(p + 1);
                    RecyclerViewHorizontalAdapter.notifyDataSetChanged();
                    databaseReference.removeEventListener(this);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            databaseReference.child(name).child(room).child("hl").child("value").addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //  System.out.println(dataSnapshot);
                    boolean b = dataSnapshot.getValue(Boolean.class);
                    int p = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).first.second.equals("hl")) {
                            p = i;
                            break;
                        }
                    }
                    if (b == true) {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Light", R.drawable.light_bulb_on), "hl"), LIGHT));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("hl", true);
                        editor.apply();

                    } else {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Light", R.drawable.light_bulb_off), "hl"), LIGHT));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("hl", false);
                        editor.apply();

                    }
                    list.remove(p + 1);
                    RecyclerViewHorizontalAdapter.notifyDataSetChanged();
                    databaseReference.removeEventListener(this);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            databaseReference.child(name).child(room).child("hl4").child("value").addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //  System.out.println(dataSnapshot);
                    boolean b = dataSnapshot.getValue(Boolean.class);
                    int p = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).first.second.equals("hl4")) {
                            p = i;
                            break;
                        }
                    }
                    if (b == true) {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Light", R.drawable.light_bulb_on), "hl4"), LIGHT));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("hl4", true);
                        editor.apply();

                    } else {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Light", R.drawable.light_bulb_off), "hl4"), LIGHT));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("hl4", false);
                        editor.apply();

                    }
                    list.remove(p + 1);
                    RecyclerViewHorizontalAdapter.notifyDataSetChanged();
                    databaseReference.removeEventListener(this);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            databaseReference.child(name).child(room).child("hl3").child("value").addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //  System.out.println(dataSnapshot);
                    boolean b = dataSnapshot.getValue(Boolean.class);
                    int p = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).first.second.equals("hl3")) {
                            p = i;
                            break;
                        }
                    }
                    if (b == true) {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Light", R.drawable.light_bulb_on), "hl3"), LIGHT));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("hl3", true);
                        editor.apply();

                    } else {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Light", R.drawable.light_bulb_off), "hl3"), LIGHT));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("hl3", false);
                        editor.apply();

                    }
                    list.remove(p + 1);
                    RecyclerViewHorizontalAdapter.notifyDataSetChanged();
                    databaseReference.removeEventListener(this);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            databaseReference.child(name).child(room).child("hl2").child("value").addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //  System.out.println(dataSnapshot);
                    boolean b = dataSnapshot.getValue(Boolean.class);
                    int p = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).first.second.equals("hl2")) {
                            p = i;
                            break;
                        }
                    }
                    if (b == true) {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Light", R.drawable.light_bulb_on), "hl2"), LIGHT));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("hl2", true);
                        editor.apply();

                    } else {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("Light", R.drawable.light_bulb_off), "hl2"), LIGHT));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("hl2", false);
                        editor.apply();

                    }
                    list.remove(p + 1);
                    RecyclerViewHorizontalAdapter.notifyDataSetChanged();
                    databaseReference.removeEventListener(this);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            databaseReference.child(name).child(room).child("htv").child("value").addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //  System.out.println(dataSnapshot);
                    boolean b = dataSnapshot.getValue(Boolean.class);
                    int p = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).first.second.equals("htv")) {
                            p = i;
                            break;
                        }
                    }
                    if (b == true) {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("TV", R.drawable.tv_on), "htv"), TV));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("htv", true);
                        editor.apply();

                    } else {
                        list.add(p, new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>("TV", R.drawable.tv_off), "htv"), TV));
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("htv", false);
                        editor.apply();

                    }
                    list.remove(p + 1);
                    RecyclerViewHorizontalAdapter.notifyDataSetChanged();
                    databaseReference.removeEventListener(this);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }

    }


    private class LoadAppliances extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... strings) {

            switch (strings[0]) {
                case "livingroom":
                    addTV("TV", "htv");
                    addChandelier("Chandelier", "hc");
                    addLight("Light", "hl");
                    addFan("Fan 1", "hf");
                    addLight("LED 1", "hl1");
                    addLight("LED 2", "hl2");
                    addLight("LED 3", "hl3");
                    addLight("LED 4", "hl4");
                   // addSocket("Charging Plug", "hc"); SOCKET CODE CHANGE
                    break;
                case "kitchen":
                    addSocket("RO", "kro");
                    addLight("Light", "kl1");
                    addFan("Fan", "kf1");
                    addSocket("Charging Plug", "kc1");
                    break;
                case "masterbedroom":
                    addFan("Fan", "r1f1");
                    addLight("Light 1", "r1l1");
                    addLight("Light 2", "r1l2");
                    addSocket("Charging Plug", "r1c1");
                    break;
                case "bedroom":
                    addFan("Fan", "r2f1");
                    addLight("Light", "r2l1");
                    addLight("Bathroom Light", "r2l2");
                    addSocket("Charging Plug", "r2c1");
                    break;
                default:
                    addTV("TV", "htv");
                    addFan("Fan", "hf1");
                    addLight("Light", "hl1");
                    addSocket("Socket", "hc1");
                    break;
            }

            return null;
        }
        protected void onPostExecute(Long result)
        {


        }
    }

    public void addTV(String name, String code) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(preferences.getBoolean(code, false))
            list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.tv_on), code),TV));
        else
            list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.tv_off), code), TV));
    }

    public void addFan(String name, String code) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        if(preferences.getBoolean(code, false))
            list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.fan_on), code), FAN));
        else
            list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.fan_off), code), FAN));
    }

    public void addLight(String name, String code) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(preferences.getBoolean(code, false))
            list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.light_bulb_on), code), LIGHT));
        else
            list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.light_bulb_off), code), LIGHT));
    }

    public void addChandelier(String name, String code) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(preferences.getBoolean(code, false))
            list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.chandelier_on), code), CHANDELIER));
        else
            list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.chandelier_off), code), CHANDELIER));
    }

    public void addSocket(String name, String code) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(preferences.getBoolean(code, false))
            list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.socket_on), code), SOCKET));
        else
            list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.socket_off), code), SOCKET));
    }

    private void setBackground(String roomname) {
        switch(roomname) {
            case "livingroom":
                Glide.with(this).load(R.drawable.living_room_portrait).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            roomRelativeLayout.setBackground(resource);
                        }
                    }
                });
                break;
            case "masterbedroom":
                Glide.with(this).load(R.drawable.master_bedroom_portrait).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            roomRelativeLayout.setBackground(resource);
                        }
                    }
                });
                break;
            case "bedroom":
                Glide.with(this).load(R.drawable.bedroom_portrait).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            roomRelativeLayout.setBackground(resource);
                        }
                    }
                });
                break;
            case "kitchen":
                Glide.with(this).load(R.drawable.kitchen_portrait).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            roomRelativeLayout.setBackground(resource);
                        }
                    }
                });
                break;
            default:
                break;
        }
    }
}
