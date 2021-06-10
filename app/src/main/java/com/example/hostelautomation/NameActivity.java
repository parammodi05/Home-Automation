package com.example.hostelautomation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class NameActivity extends AppCompatActivity {

    private static final String PREF_NAME = "userName";
    EditText userName;
    Button go;
    ImageButton ib;
    CircleImageView civ;

    String image;
    private static final int GalleryPick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        userName = (EditText) findViewById(R.id.user_name);


        go = (Button) findViewById(R.id.go);
        civ=findViewById(R.id.pi);


        civ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opengallery();

            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = userName.getText().toString();
                name=name.toLowerCase();


                SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();

                if(image==null||name.isEmpty()||image.isEmpty()||(!name.equals("jash")&&!name.equals("bunny")))
                {
                    Toast.makeText(NameActivity.this,"Please enter the Details",Toast.LENGTH_LONG);
                }
                else
                    {
                    Intent intent = new Intent(NameActivity.this, NavigationDrawer.class);
                    startActivity(intent);
                    editor.putString("user_name", name);
                    editor.putString("user_profile",image);
                    editor.apply();
                    finish();
                }


            }
        });

        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        if(preferences.contains("user_name")) {
            Intent intent = new Intent(NameActivity.this, NavigationDrawer.class);
            startActivity(intent);
            finish();
        }

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK&&requestCode ==GalleryPick
                && data != null
        ) {

            // Get the Uri of data
            Uri ImageUri = data.getData();
            image=ImageUri.toString();

            civ.setImageURI(ImageUri);

            System.out.println(ImageUri);
        }
        else{
            System.out.println(2);
        }
    }
}
