package com.example.hostelautomation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.example.hostelautomation.Fragments.MainFragment;
import com.example.hostelautomation.Fragments.MasterBedroomFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

import net.steamcrafted.lineartimepicker.dialog.LinearTimePickerDialog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class Timer extends AppCompatActivity {
    Button b1,b2,b3,b4,b5,b6,b7;
    boolean f1,f2,f3,f4,f5,f6,f7;
    HashSet<String>h=new HashSet<String>();
    EditText schedulename;
    TextView time,appname;
    String hr="12";
    String min="00";
    String x="Once";
    String y="kitchen";
    String k="Light";
    String m="Light1";
    String b="Light";
    String l="TV";
    boolean switchstate=false;
    Button save;
    ImageView iv;
    Switch sw;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    boolean first=true;
    String timeofsch=hr+":"+min;
    private static final String MY_TIMERS = "Timers";
    private static final String MY_DEMO = "DEMO";
    private static final String PREFSS_NAME = "userName";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

      schedulename=(EditText)findViewById(R.id.schedulename);
      CardView c=(CardView)findViewById(R.id.cardview1);
     final RelativeLayout er=(RelativeLayout)findViewById(R.id.recempt);
      c.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              schedulename.setVisibility(View.VISIBLE);
              er.setVisibility(View.INVISIBLE);
          }
      });

      time=(TextView)findViewById(R.id.time);
      sw=(Switch)findViewById(R.id.switchonoff);
      appname=(TextView)findViewById(R.id.appliancename);
      save=(Button)findViewById(R.id.save);
      iv=(ImageView)findViewById(R.id.timeimage);
      final LinearLayout l1=findViewById(R.id.linear_layout3);
      final MaterialSpinner spinner3 = (MaterialSpinner) findViewById(R.id.spinnerappliance);
      final SharedPreferences prefs = getApplicationContext().getSharedPreferences(MY_TIMERS, Context.MODE_PRIVATE);
      final SharedPreferences prefs1 = getApplicationContext().getSharedPreferences(MY_DEMO, Context.MODE_PRIVATE);
      final SharedPreferences.Editor editor1 = getApplicationContext().getSharedPreferences(MY_TIMERS, Context.MODE_PRIVATE).edit();
      final SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(MY_DEMO, Context.MODE_PRIVATE).edit();
      final SharedPreferences prefs2 = getApplicationContext().getSharedPreferences(PREFSS_NAME, Context.MODE_PRIVATE);
      final boolean demo=prefs1.getBoolean("demo",true);
      final String name = prefs2.getString("user_name", "User");
      databaseReference=firebaseDatabase.getInstance().getReference();


      time.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
                int black= Color.BLACK;
        int blue=Color.BLUE;
        LinearTimePickerDialog dialog = LinearTimePickerDialog.Builder.with(Timer.this)
                .setDialogBackgroundColor(R.color.bluegray)
                .setPickerBackgroundColor(R.color.bluegray)
                .setLineColor(Color.WHITE)
                .setTextColor(Color.WHITE)
                .setShowTutorial(demo)
                .setTextBackgroundColor(Color.TRANSPARENT)
                .setButtonColor(Color.WHITE)






       .setButtonCallback(new LinearTimePickerDialog.ButtonCallback() {
            @Override
            public void onPositive(DialogInterface dialog, int hour, int minutes) {
                if(hour==24)
                {
                    hr="00";
                    min="00";
                }
                else
                    {
                    hr = String.valueOf(hour);
                    min = String.valueOf(minutes);
                }

                timeofsch=hr+":"+min;
                if(hour>6&&hour<12)
                {
                    iv.setBackgroundResource(R.drawable.morning);
                    time.setText(hr+":"+min+" "+"AM");

                }
                else if(hour==12)
                {
                    iv.setBackgroundResource(R.drawable.afternoon);
                    time.setText(hr+":"+min+" "+"PM");

                }
                else if(hour==24)
                {

                    iv.setBackgroundResource(R.drawable.night);
                    time.setText(hr+":"+min+" "+"AM");
                }
                else if(hour>12&&hour<=18)
                {
                    iv.setBackgroundResource(R.drawable.afternoon);
                    String phr=String.valueOf(hour-12);
                    time.setText(phr+":"+min+" "+"PM");



                }
                else
                {
                    iv.setBackgroundResource(R.drawable.night);
                    if(hour>18&&hour<=23)
                    {
                        String phr=String.valueOf(hour-12);
                        time.setText(phr+":"+min+" "+"PM");
                    }
                    else
                    {
                        time.setText(hr+":"+min+" "+"AM");
                    }

                }
                System.out.println(hr);
                System.out.println(min);
                System.out.println(timeofsch);




            }

            @Override
            public void onNegative(DialogInterface dialog) {
                 // time.setText(hr+":"+min);
            }
        })
         .build();



          dialog.show();
          editor.putBoolean("demo",false);
          editor.apply();
          }

      });
       sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true)
                {
                   switchstate=true;
                  sw.setTextOn("ON");
                  sw.setTextColor(Color.GRAY);

                }
                else
                {
                    switchstate=false;
                    sw.setTextOn("OFF");
                    sw.setTextColor(Color.GRAY);

                }
                //  notifyDataSetChanged();


            }
        });

        final HashMap<String,String>kitchen=new HashMap<>();
        kitchen.put("RO","kro");
        kitchen.put("Light","kl1");
        kitchen.put("Fan","kf1");
        kitchen.put("Charging Plug","kc1");

        final HashMap<String,String>livingroom=new HashMap<>();
        livingroom.put("TV","htv");
        livingroom.put("Light","hl");
        livingroom.put("Chandelier","hc");
        livingroom.put("Fan 1","hf");
        livingroom.put("LED 2","hl2");
        livingroom.put("LED 3","hl3");
        livingroom.put("LED 4","hl4");
        livingroom.put("LED 1","hl1");

        final HashMap<String,String>masterbedroom=new HashMap<>();
        masterbedroom.put("Fan","r1f1");
        masterbedroom.put("Light 1","r1l1");
        masterbedroom.put("Light 2","r1l2");
        masterbedroom.put("Charging Plug","r1c1");

        final HashMap<String,String>bedroom=new HashMap<>();
        bedroom.put("Fan","r2f1");
        bedroom.put("Light","r2l1");
        bedroom.put("Bathroom Light","r2l2");
        bedroom.put("Charging Plug","r2c1");












        spinner3.setItems("Fan", "Light", "Charging Plug", "RO");
        spinner3.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                k=item;
            }
        });
        spinner3.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override public void onNothingSelected(MaterialSpinner spinner) {

            }
        });
        MaterialSpinner spinner2 = (MaterialSpinner) findViewById(R.id.spinnerroom);
        spinner2.setItems("kitchen", "livingroom", "bedroom", "masterbedroom");
        spinner2.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                y=item;
                if(y.equals("kitchen"))
                {


                    spinner3.setItems("Light", "Fan", "Charging Plug", "RO");
                    spinner3.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                        @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                            k=item;
                        }
                    });
                    spinner3.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

                        @Override public void onNothingSelected(MaterialSpinner spinner) {

                        }
                    });


                }
                else if(y.equals("livingroom"))
                {


                    spinner3.setItems("TV", "Light", "Chandelier", "Fan 1","LED1","LED2","LED3","LED4");
                    spinner3.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                        @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                            l=item;
                        }
                    });
                    spinner3.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

                        @Override public void onNothingSelected(MaterialSpinner spinner) {

                        }
                    });

                }
                else if(y.equals("bedroom"))
                {

                    spinner3.setItems("Light", "Fan", "Charging Plug", "Bathroom Light");
                    spinner3.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                        @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                            b=item;
                        }
                    });
                    spinner3.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

                        @Override public void onNothingSelected(MaterialSpinner spinner) {

                        }
                    });


                }
                else if(y.equals("masterbedroom"))
                {


                    spinner3.setItems("Light1", "Fan", "Charging Plug", "Light2");
                    spinner3.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                        @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                            m=item;
                        }
                    });
                    spinner3.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

                        @Override public void onNothingSelected(MaterialSpinner spinner) {

                        }
                    });
                }


            }
        });
        spinner2.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override public void onNothingSelected(MaterialSpinner spinner) {

            }
        });


        MaterialSpinner spinner1 = (MaterialSpinner) findViewById(R.id.selection);
        spinner1.setItems("Once", "Weekly");
        spinner1.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                x=item;
                if(x.equals("Weekly"))
                {

                    l1.setVisibility(View.VISIBLE);
                }
                else
                {
                    l1.setVisibility(View.INVISIBLE);
                }

            }
        });
        spinner1.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override public void onNothingSelected(MaterialSpinner spinner) {

            }
        });
        System.out.println(x);






        b1=(Button)findViewById(R.id.b1);
        b2=(Button)findViewById(R.id.b2);
        b3=(Button)findViewById(R.id.b3);
        b4=(Button)findViewById(R.id.b4);
        b5=(Button)findViewById(R.id.b5);
        b6=(Button)findViewById(R.id.b6);
        b7=(Button)findViewById(R.id.b7);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(f1==true)
                {

                    f1=false;
                    h.remove("monday");
                    b1.setBackgroundResource(R.drawable.badge);



                }
                else
                {
                    b1.setBackgroundResource(R.drawable.badge2);
                    f1=true;
                    h.add("monday");
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(f2==true)
                {

                    f2=false;
                    h.remove("tuesday");
                    b2.setBackgroundResource(R.drawable.badge);



                }
                else
                {
                    b2.setBackgroundResource(R.drawable.badge2);
                    f2=true;
                    h.add("tuesday");
                }
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(f3==true)
                {

                    f3=false;
                    h.remove("wednesday");
                    b3.setBackgroundResource(R.drawable.badge);



                }
                else
                {
                    b3.setBackgroundResource(R.drawable.badge2);
                    f3=true;
                    h.add("wednesday");
                }
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(f4==true)
                {

                    f4=false;
                    h.remove("thursday");
                    b4.setBackgroundResource(R.drawable.badge);



                }
                else
                {
                    b4.setBackgroundResource(R.drawable.badge2);
                    f4=true;
                    h.add("thursday");
                }
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(f5==true)
                {

                    f5=false;
                    h.remove("friday");
                    b5.setBackgroundResource(R.drawable.badge);



                }
                else
                {
                    b5.setBackgroundResource(R.drawable.badge2);
                    f5=true;
                    h.add("friday");
                }
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(f6==true)
                {

                    f6=false;
                    h.remove("saturday");
                    b6.setBackgroundResource(R.drawable.badge);



                }
                else
                {
                    b6.setBackgroundResource(R.drawable.badge2);
                    f6=true;
                    h.add("saturday");
                }
            }
        });b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(f7==true)
                {

                    f7=false;
                    h.remove("sunday");
                    b7.setBackgroundResource(R.drawable.badge);



                }
                else
                {
                    b7.setBackgroundResource(R.drawable.badge2);
                    f7=true;
                    h.add("sunday");
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String days="";
                if(h.size()>0) {
                    for (String s : h) {
                        days += s;
                    }
                }
                final String schname=schedulename.getText().toString();
                if(schname.isEmpty())
                {
                    er.setVisibility(View.VISIBLE);
                    schedulename.setVisibility(View.INVISIBLE);
                }
                else {
                    String abouttimer = schname + "?" + days + "?" + timeofsch;
                    Set<String> timerset = new HashSet<>(prefs.getStringSet("Timers", new HashSet<String>()));
                    timerset.add(abouttimer);
                    editor1.putStringSet("Timers", timerset);
                    editor1.apply();
                    String stringindatabase = "";
                    if (x.equals("Once")) {
                        stringindatabase += "0";
                    } else {
                        stringindatabase += "1";
                    }
                    if(switchstate==true)
                    {
                        stringindatabase += "1";
                    }
                    else
                    {
                        stringindatabase += "0";
                    }
                    HashMap<String, Integer> hmap = new HashMap<>();
                    hmap.put("monday", 0);
                    hmap.put("tuesday", 1);
                    hmap.put("wednesday", 2);
                    hmap.put("thursday", 3);
                    hmap.put("friday", 4);
                    hmap.put("saturday", 5);
                    hmap.put("sunday", 6);
                    char arr[] = new char[7];
                    Arrays.fill(arr, '0');
                    for (String s : h) {
                        int pos = hmap.get(s);
                        arr[pos] = '1';
                    }
                    String week = String.valueOf(arr);
                    stringindatabase += week;

                    System.out.println(abouttimer);
                    System.out.println(stringindatabase);

                   String code="";
                   if(y.equals("kitchen"))
                   {
                       code=kitchen.get(k);
                   }
                   else if(y.equals("bedroom"))
                   {
                       code=bedroom.get(b);
                   }
                   else if(y.equals("masterbedroom"))
                   {
                       code=masterbedroom.get(m);
                   }
                   else
                   {
                       code=livingroom.get(l);
                   }

                    databaseReference.child(name).child(y).child(code).child("flag").setValue(1);
                    databaseReference.child(name).child(y).child(code).child("str").setValue(stringindatabase);
                    int totaltime=Integer.valueOf(hr)*60+Integer.valueOf(min);
                    databaseReference.child(name).child(y).child(code).child("time").setValue(totaltime);
                    System.out.println(abouttimer);
                    Intent i=new Intent(Timer.this,masterbedroommain.class);
                    startActivity(i);
                    finish();

                }

            }
        });





    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(Timer.this,masterbedroommain.class);
        startActivity(i);
        finish();
    }
}
