package com.palanim.botcontroller;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        }

    public void wifiset(View v){
        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
    }
    public void arenaSet(View v){
        Intent intent = new Intent(this, arenaAdv.class);

        startActivity(intent);
    }


    }
/*
  android:layout_gravity="center_vertical|center_horizontal"
            android:gravity="center"
            android:orientation="horizontal"

*/
