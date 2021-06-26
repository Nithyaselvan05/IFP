package com.palanim.botcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void CallRobot(View v){
        Intent intent = new Intent(this, robotCaller.class);

        startActivity(intent);

    }
    public void Settings(View v){
        Intent intent = new Intent(this, settings.class);

        startActivity(intent);

    }

    public void setupArena(View v){
        Intent intent = new Intent(this, setupArena.class);

        startActivity(intent);

    }
    public void status(View v){
        TextView statusText = findViewById( R.id.status);

        if( statusText.getText().toString().equals("Robot Status:         Not Found")){

            Toast.makeText(getApplicationContext(),"Connect to Robot's WiFi. Check WiFi Settings!",Toast.LENGTH_LONG).show();
        }
        else if( statusText.getText().toString().equals("Robot Status:         Connected")) {
            Toast.makeText(getApplicationContext(),"Connected to Robot",Toast.LENGTH_LONG).show();
        }


    }
}