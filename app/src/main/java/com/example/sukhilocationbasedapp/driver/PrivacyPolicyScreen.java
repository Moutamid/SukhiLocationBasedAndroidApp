package com.example.sukhilocationbasedapp.driver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.sukhilocationbasedapp.R;
import com.example.sukhilocationbasedapp.databinding.ActivityLanguagesBinding;
import com.example.sukhilocationbasedapp.databinding.ActivityPrivacyPolicyScreenBinding;

public class PrivacyPolicyScreen extends AppCompatActivity {

    private ActivityPrivacyPolicyScreenBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityPrivacyPolicyScreenBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        b.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RiderMenuItemActivity.class));
                finish();
            }
        });
    }
}