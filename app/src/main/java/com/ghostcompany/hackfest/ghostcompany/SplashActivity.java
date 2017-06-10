package com.ghostcompany.hackfest.ghostcompany;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseLongArray;


import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.i("SPLASH", "Abriu splash screen");
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {

                Log.i("SPLASH", "Run() timer splash screen");

            }
        }, 2000);

        Intent intent = new Intent(SplashActivity.this, MapsActivity.class);
        startActivity(intent);

        finish();

    }
}