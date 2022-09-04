package com.example.sukhilocationbasedapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.sukhilocationbasedapp.walkthrough.WalkthroughScreens;

public class SplashScreen extends AppCompatActivity {

    private static int SSPLASH_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(getApplicationContext(), WalkthroughScreens.class));
                finish();
            }
        },SSPLASH_TIME);

    }
}