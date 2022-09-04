package com.example.sukhilocationbasedapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuItemsActivity extends AppCompatActivity {

    private ImageView closeImg;
    private CircleImageView profileImg;
    private TextView nameTxt,editTxt,tripBtn,notificationBtn,paymentBtn,promosBtn,helpBtn,settingsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_items);

        profileImg = findViewById(R.id.profile);
        closeImg = findViewById(R.id.close);
        nameTxt = findViewById(R.id.name);
        editTxt = findViewById(R.id.edit);
        profileImg = findViewById(R.id.profile);
        tripBtn = findViewById(R.id.trips);
        notificationBtn = findViewById(R.id.notification);
        paymentBtn = findViewById(R.id.payment);
        promosBtn = findViewById(R.id.promos);
        helpBtn = findViewById(R.id.help);
        settingsBtn = findViewById(R.id.settings);

        tripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuItemsActivity.this,RequestRide.class));
                finish();
            }
        });

        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuItemsActivity.this,MainScreen.class);
                startActivity(intent);
            }
        });
    }
}