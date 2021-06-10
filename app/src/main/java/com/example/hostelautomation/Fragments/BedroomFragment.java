package com.example.hostelautomation.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.widget.Toast;

import com.example.hostelautomation.Act;
import com.example.hostelautomation.Adapter.Actadapter;
import com.example.hostelautomation.Adapter.ApplianceAdapter;
import com.example.hostelautomation.Livingroommain;
import com.example.hostelautomation.NavigationDrawer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

import com.example.hostelautomation.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class BedroomFragment extends Fragment {

    EditText btext;
    Button b;
    private static final String MY_PREFS_NAME = "Activities";

    RecyclerView recyclerView;
    RecyclerView rc;
    ArrayList<Act> list;
    ArrayList<Pair<Pair<Pair<String, Integer>, String>, Integer>> list1;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    RecyclerView.LayoutManager RecyclerViewLayoutManager1;
    Actadapter RecyclerViewHorizontalAdapter;
    ApplianceAdapter RecyclerViewHorizontalAdapter1;
    LinearLayoutManager HorizontalLayout ;
    LinearLayoutManager HorizontalLayout1 ;
    View ChildView ;
    String image;
    int RecyclerViewItemPosition ;
    CircleImageView civ1;
    private static final Integer TV = 1;
    private static final Integer FAN = 2;
    private static final Integer LIGHT = 3;
    private static final Integer CHANDELIER = 4;
    private static final Integer SOCKET = 5;
    private static final int GalleryPick = 1;
    String state[];
    String x="kitchen";


    private static final String P = "B_IP1";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_bedroom, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        recyclerView = (RecyclerView) view.findViewById(R.id.fragrec);
        state= new String[4];
        state[0]="livingroom";
        state[1]="kitchen";
        state[2]="bedroom";
        state[3]="masterbedroom";

        final EditText et=(EditText)view.findViewById(R.id.textfrag);

        civ1=view.findViewById(R.id.pi1);
        Button add=(Button)view.findViewById(R.id.add);
        civ1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opengallery();

            }
        });


        MaterialSpinner spinner = (MaterialSpinner) view.findViewById(R.id.spinner);
        spinner.setItems("kitchen", "livingroom", "bedroom", "masterbedroom");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                x=state[position];
            }
        });


          RecyclerViewLayoutManager = new LinearLayoutManager(getContext());
      //  RecyclerViewLayoutManager1 = new LinearLayoutManager(getContext());
        final SharedPreferences prefs = getContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);

        final SharedPreferences.Editor editor1 = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();

        final Button save=(Button)view.findViewById(R.id.buttonfrag);







        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save.setVisibility(View.VISIBLE);
                if(!x.isEmpty()) {
                    list = new ArrayList<>();
                    LoadAppliances loadAppliances = new LoadAppliances();
                    loadAppliances.execute(x);
                    for(Act a:list)
                    {
                        System.out.println(a.isChecked1());
                    }



                }
            }
        });
        System.out.println("NONONONONONO");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zz=et.getText().toString();
                final SharedPreferences.Editor editor = getActivity().getSharedPreferences(zz, Context.MODE_PRIVATE).edit();
                for(int i=0;i<list.size();i++)
                {
                    System.err.println(list.size());

                    String name = list.get(i).getApptext();

                    String alpha=list.get(i).getCodeon()+"modi";
                    boolean m=list.get(i).isChecked1();
                    boolean n=list.get(i).isChecked2();
                    //if(!m)continue;

                    StringBuffer sb=new StringBuffer();
                    if(n)
                    {
                        sb.append("true"+"this");
                    }
                    else
                        {
                            sb.append("false"+"it");
                        }

                        String beta=sb.toString();
                        String z=alpha+"?"+beta;
                        System.out.println(z);
                      //  editor.putString(name,z);
                       // editor.apply();
                    }



                System.err.println("sdjcos");
                Set<String> h=new HashSet<>(prefs.getStringSet("Activities",new HashSet<String>()));
                h.add(zz+"?"+image);
                System.out.println(list.size()+"can");

                editor1.putStringSet("Activities",h);
                editor1.apply();
                Intent intent = new Intent(getContext(), Livingroommain.class);

                //getActivity().finish();
                getContext().startActivity(intent);





            }

        });








    }
    private class LoadAppliances extends AsyncTask<String,Void,Void>  {
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
                    addSocket("Charging Plug", "hc");
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

        @Override
        protected void onPostExecute(Void aVoid) {
            RecyclerViewHorizontalAdapter = new Actadapter(list);
            HorizontalLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(HorizontalLayout);
            recyclerView.setAdapter(RecyclerViewHorizontalAdapter);
            RecyclerViewHorizontalAdapter.notifyDataSetChanged();
        }
        /* LoadAppliances(String x){

            switch (x) {
                case "livingroom":
                    addTV("TV", "htv");
                    addChandelier("Chandelier", "hc");
                    addLight("Light", "hl");
                    addFan("Fan 1", "hf");
                    addLight("LED 1", "hl1");
                    addLight("LED 2", "hl2");
                    addLight("LED 3", "hl3");
                    addLight("LED 4", "hl4");
                    addSocket("Charging Plug", "hc");
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
        }*/




        }



    public void addTV(String name, String code) {
        list.add(new Act(R.drawable.tv_off,name,false,false,code,""));

        //list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.tv_on), code),TV));
        //else
        //   list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.tv_off), code), TV));
    }

    public void addFan(String name, String code) {
        list.add(new Act(R.drawable.fan_off,name,false,false,code,""));
        //   SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        //if(preferences.getBoolean(code, false))
       // list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.fan_on), code), FAN));
        //else
        //  list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.fan_off), code), FAN));
    }

    public void addLight(String name, String code) {
        list.add(new Act(R.drawable.light_bulb_off,name,false,false,code,""));
        //  SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        //  if(preferences.getBoolean(code, false))
       // list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.light_bulb_on), code), LIGHT));
        // else
        //   list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.light_bulb_off), code), LIGHT));
    }

    public void addChandelier(String name, String code) {
        list.add(new Act(R.drawable.chandelier_off,name,false,false,code,""));
        //SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        // if(preferences.getBoolean(code, false))
       // list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.chandelier_on), code), CHANDELIER));
        //else
        //  list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.chandelier_off), code), CHANDELIER));
    }

    public void addSocket(String name, String code) {
        list.add(new Act(R.drawable.socket_off,name,false,false,code,""));
        //SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        //if(preferences.getBoolean(code, false))
        //list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.socket_on), code), SOCKET));
        //else
        //  list.add(new Pair<Pair<Pair<String, Integer>, String>, Integer>(new Pair<Pair<String, Integer>, String>(new Pair<String, Integer>(name, R.drawable.socket_off), code), SOCKET));
    }
    private void opengallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        //startActivityForResult(galleryIntent, GalleryPick);
        startActivityForResult(galleryIntent,GalleryPick);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK&&requestCode ==GalleryPick
                && data != null
        ) {

            // Get the Uri of data
            Uri ImageUri = data.getData();
            image=ImageUri.toString();

            civ1.setImageURI(ImageUri);

            System.out.println(ImageUri);
        }
        else{
            System.out.println(2);
        }
    }


    }

