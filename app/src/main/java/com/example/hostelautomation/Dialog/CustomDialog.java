package com.example.hostelautomation.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostelautomation.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdsmdg.harjot.crollerTest.Croller;
import com.sdsmdg.harjot.crollerTest.OnCrollerChangeListener;

import static android.content.Context.MODE_PRIVATE;

public class CustomDialog extends Dialog {

    private static final String PREFS_NAME = "FanSpeed";
    private static final String ROOM_NAME = "Room";
    private static final String SEPERATOR = "/";
    private static final String PREFSS_NAME = "userName";
    private static final String PREFS_FAN = "fan_selected";

    private static final String LIVING_ROOM_1 = "LR_IP1";
    private static final String MASTER_BEDROOM = "MB_IP1";
    private static final String BEDROOM = "B_IP1";
    private static final String KITCHEN = "K_IP1";

    public Activity c;
    public Dialog d;
    SeekBar seekBar;
    TextView ok, speedText;
    WebView webView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private Croller croller;

    String ip = "255.255.255.0";

    public CustomDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        databaseReference=FirebaseDatabase.getInstance().getReference();

       // seekBar = (SeekBar) findViewById(R.id.speedSlider);
        speedText = (TextView) findViewById(R.id.speedText);
        ok = (TextView) findViewById(R.id.OK);

        webView = (WebView) findViewById(R.id.dialogWeb);

        LoadPreviousSpeed loadPreviousSpeed = new LoadPreviousSpeed();
        loadPreviousSpeed.execute();
        croller = findViewById(R.id.croller);
        SharedPreferences preferences = getContext().getSharedPreferences(PREFSS_NAME, MODE_PRIVATE);
        final String name = preferences.getString("user_name", "User");

        croller.setIndicatorWidth(10);
//        croller.setBackCircleColor(Color.parseColor("#EDEDED"));
//        croller.setMainCircleColor(Color.WHITE);
//        croller.setMax(50);
//        croller.setStartOffset(45);
//        croller.setIsContinuous(false);
//        croller.setLabelColor(Color.BLACK);
//        croller.setProgressPrimaryColor(Color.parseColor("#0B3C49"));
//        croller.setIndicatorColor(Color.parseColor("#0B3C49"));
//        croller.setProgressSecondaryColor(Color.parseColor("#EEEEEE"));
//        croller.setProgressRadius(380);
//        croller.setBackCircleRadius(300);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences1 = c.getSharedPreferences(ROOM_NAME, MODE_PRIVATE);
                String room = preferences1.getString("room_name", "livingroom");
                String code = "";
                switch (room) {
                    case "livingroom":
                        code = "hf";

                        break;
                    case "kitchen":
                        code = "kf1";

                        break;
                    case "masterbedroom":
                        code = "r1f1";

                        break;
                    case "bedroom":
                        code = "r2f1";

                        break;
                    default:
                        break;
                }

                room=room.toLowerCase();
                if(room.equals("livingroomnnjn")) {
                    SharedPreferences preferences = c.getSharedPreferences(PREFS_FAN, MODE_PRIVATE);
                    String name = preferences.getString("fan_selected", "Fan 1");

                    if(name.equals("Fan 1")) {
                        SharedPreferences preferences20 = c.getSharedPreferences(LIVING_ROOM_1, MODE_PRIVATE);
                        ip = preferences20.getString("lr_ip1", "255.255.0");

                        SharedPreferences preferences22 = c.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                        String progress = preferences22.getString("fan_speed" + code + "1", "0");
                        webView.loadUrl("http://" + ip + SEPERATOR + code + progress);
                        //Toast.makeText(getContext(), "http://" + ip + SEPERATOR + code + progress, Toast.LENGTH_LONG).show();
                        dismiss();
                    }
                    else {

                        SharedPreferences preferences21 = c.getSharedPreferences(LIVING_ROOM_1, MODE_PRIVATE);
                        ip = preferences21.getString("lr_ip2", "255.255.0");

                        SharedPreferences preferences23 = c.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                        String progress = preferences23.getString("fan_speed" + code + "2", "0");
                        webView.loadUrl("http://" + ip + SEPERATOR + code + progress);
                        //Toast.makeText(getContext(), "http://" + ip + SEPERATOR + code + progress, Toast.LENGTH_LONG).show();
                        dismiss();
                    }
                } else
                    {

                    SharedPreferences preferences = c.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    String progress = preferences.getString("fan_speed" + code, "0");
                    System.out.println(progress);
                    if(progress.equals("0"))
                    {
                        databaseReference.child(name).child(room).child(code).child("s0").setValue(false);
                        databaseReference.child(name).child(room).child(code).child("s1").setValue(false);
                        databaseReference.child(name).child(room).child(code).child("s2").setValue(false);
                        databaseReference.child(name).child(room).child(code).child("s3").setValue(false);
                        databaseReference.child(name).child(room).child(code).child("s4").setValue(false);
                    }

                    else
                        {

                        databaseReference.child(name).child(room).child(code).child("s0").setValue(false);
                        databaseReference.child(name).child(room).child(code).child("s1").setValue(false);
                        databaseReference.child(name).child(room).child(code).child("s2").setValue(false);
                        databaseReference.child(name).child(room).child(code).child("s3").setValue(false);
                        databaseReference.child(name).child(room).child(code).child("s4").setValue(false);
                        databaseReference.child(name).child(room).child(code).child("s"+progress).setValue(true);

                    }


                    webView.loadUrl("http://" + ip + SEPERATOR + code + progress);
                    //Toast.makeText(getContext(), "http://" + ip + SEPERATOR + code + progress, Toast.LENGTH_LONG).show();
                    dismiss();
                }

            }
        });
        croller.setOnCrollerChangeListener(new OnCrollerChangeListener() {
            @Override
            public void onProgressChanged(Croller croller, int progress) {
                speedText.setText(String.valueOf(progress));
                System.out.println(progress);
                SharedPreferences preferences1 = c.getSharedPreferences(ROOM_NAME, MODE_PRIVATE);
                String room = preferences1.getString("room_name", "Living Room");
                String code = "";
                switch (room) {
                    case "livingroom":
                        code = "hf";
                        break;
                    case "kitchen":
                        code = "kf1";
                        break;
                    case "masterbedroom":
                        code = "r1f1";
                        break;
                    case "bedroom":
                        code = "r2f1";
                        break;
                    default:
                        break;
                }
                if(room.equals("livingroomhuhu")) {
                    SharedPreferences preferences = c.getSharedPreferences(PREFS_FAN, MODE_PRIVATE);
                    String name = preferences.getString("fan_selected", "Fan 1");
                    //Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
                    if(name.equals("Fan 1")) {
                        code += "1";
                    }
                    else {
                        code += "2";
                    }
                }
                SharedPreferences.Editor editor = c.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("fan_speed" + code, String.valueOf(progress));

                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(Croller croller) {

            }

            @Override
            public void onStopTrackingTouch(Croller croller) {

            }
        });


        /*seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedText.setText(String.valueOf(progress));
                System.out.println(progress);

                SharedPreferences preferences1 = c.getSharedPreferences(ROOM_NAME, MODE_PRIVATE);
                String room = preferences1.getString("room_name", "Living Room");
                String code = "";
                switch (room) {
                    case "Living Room":
                        code = "hfs";
                        break;
                    case "Kitchen":
                        code = "kfs";
                        break;
                    case "Master Bedroom":
                        code = "r1fs";
                        break;
                    case "Bedroom":
                        code = "r2fs";
                        break;
                    default:
                        break;
                }
                if(room.equals("Living Room")) {
                    SharedPreferences preferences = c.getSharedPreferences(PREFS_FAN, MODE_PRIVATE);
                    String name = preferences.getString("fan_selected", "Fan 1");
                    //Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
                    if(name.equals("Fan 1")) {
                        code += "1";
                    }
                    else {
                        code += "2";
                    }
                }
                SharedPreferences.Editor editor = c.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("fan_speed" + code, String.valueOf(progress));
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/
    }

    private class LoadPreviousSpeed extends AsyncTask<Void,Void,Void> {

        String progress;

        @Override
        protected Void doInBackground(Void... voids) {

            SharedPreferences preferences10 = c.getSharedPreferences(ROOM_NAME, MODE_PRIVATE);
            String room = preferences10.getString("room_name", "Living Room");
            String code = "";

            switch (room) {
                case "livingroom":
                    code = "hf";
                    break;
                case "kitchen":
                    code = "kf1";
                    break;
                case "masterbedroom":
                    code = "r1f1";
                    break;
                case "bedroom":
                    code = "r2f1";
                    break;
                default:
                    break;
            }

            if(room.equals("livingroomdede")) {
                SharedPreferences preferences = c.getSharedPreferences(PREFS_FAN, MODE_PRIVATE);
                String name = preferences.getString("fan_selected", "Fan 1");
                //Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
                if(name.equals("Fan 1")) {
                    code += "1";
                }
                else {
                    code += "2";
                }
            }

            SharedPreferences preferences11 = c.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            progress = preferences11.getString("fan_speed" + code, "0");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //seekBar.setProgress(Integer.valueOf(progress));
            speedText.setText(progress);
            croller.setProgress(Integer.valueOf(progress));
        }
    }
}
