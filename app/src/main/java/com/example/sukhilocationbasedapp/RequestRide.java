package com.example.sukhilocationbasedapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RequestRide extends AppCompatActivity {

    private TextView titleTxt,tripBtn,disabilityBtn,paymentBtn,rideBtn;
    private ImageView closeImg;
    private RelativeLayout trip_layout,disability_layout,payment_layout,ride_layout;
    private AppCompatButton nextBtn1,nextBtn2,requestBtn1,requestBtn2;
    private ImageView backBtn1,backBtn2,backBtn3,backBtn4;
    private TextView pickup_location,drop_location;
    private RelativeLayout arms_layout,backbone_layout,mental_layout,leg_layout,cash_layout,easypaisa_layout;
    private ImageView profileImg;
    private TextView nameTxt,carTxt,pickup,dropoff,timeTxt,distanceTxt,priceTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_ride);
        titleTxt = findViewById(R.id.trip);
        tripBtn = findViewById(R.id.btn1);
        disabilityBtn = findViewById(R.id.btn2);
        paymentBtn = findViewById(R.id.btn3);
        rideBtn = findViewById(R.id.btn4);
        closeImg = findViewById(R.id.close);
        trip_layout = findViewById(R.id.trip_detail);
        disability_layout = findViewById(R.id.disability_detail);
        payment_layout = findViewById(R.id.payment_detail);
        ride_layout = findViewById(R.id.ride_detail);
        nextBtn1 = findViewById(R.id.continueBtn);
        nextBtn2 = findViewById(R.id.continueBtns);
        requestBtn1 = findViewById(R.id.request_trip);
        requestBtn2 = findViewById(R.id.confirm);
        backBtn1 = findViewById(R.id.back);
        backBtn2 = findViewById(R.id.backBtn);
        backBtn3 = findViewById(R.id.back3);
        backBtn4 = findViewById(R.id.back4);
        pickup_location = findViewById(R.id.plocation);
        drop_location = findViewById(R.id.dlocation);
        arms_layout = findViewById(R.id.arms);
        backbone_layout = findViewById(R.id.backbone);
        mental_layout = findViewById(R.id.mental);
        leg_layout = findViewById(R.id.legs);
        profileImg = findViewById(R.id.profile);
        nameTxt = findViewById(R.id.name);
        carTxt = findViewById(R.id.car);
        pickup = findViewById(R.id.pick_up);
        dropoff = findViewById(R.id.drop_off);
        timeTxt = findViewById(R.id.estimated_time);
        distanceTxt = findViewById(R.id.estimated_distance);
        priceTxt = findViewById(R.id.rupees);

        backBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tripBtn.setBackgroundResource(R.drawable.selected_text);
                disabilityBtn.setBackgroundResource(R.drawable.unselected_text);
                paymentBtn.setBackgroundResource(R.drawable.unselected_text);
                rideBtn.setBackgroundResource(R.drawable.unselected_text);
                trip_layout.setVisibility(View.VISIBLE);
                disability_layout.setVisibility(View.GONE);
                payment_layout.setVisibility(View.GONE);
                ride_layout.setVisibility(View.GONE);
                tripBtn.setTextColor(Color.BLACK);
                disabilityBtn.setTextColor(Color.WHITE);
                paymentBtn.setTextColor(Color.WHITE);
                rideBtn.setTextColor(Color.WHITE);
                titleTxt.setText("Create a Trip");
            }
        });

        backBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tripBtn.setBackgroundResource(R.drawable.unselected_text);
                disabilityBtn.setBackgroundResource(R.drawable.selected_text);
                paymentBtn.setBackgroundResource(R.drawable.unselected_text);
                rideBtn.setBackgroundResource(R.drawable.unselected_text);
                trip_layout.setVisibility(View.GONE);
                disability_layout.setVisibility(View.VISIBLE);
                payment_layout.setVisibility(View.GONE);
                ride_layout.setVisibility(View.GONE);
                tripBtn.setTextColor(Color.WHITE);
                disabilityBtn.setTextColor(Color.BLACK);
                paymentBtn.setTextColor(Color.WHITE);
                rideBtn.setTextColor(Color.WHITE);
                titleTxt.setText("Choose a disability");
            }
        });

        nextBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tripBtn.setBackgroundResource(R.drawable.unselected_text);
                disabilityBtn.setBackgroundResource(R.drawable.selected_text);
                paymentBtn.setBackgroundResource(R.drawable.unselected_text);
                rideBtn.setBackgroundResource(R.drawable.unselected_text);
                trip_layout.setVisibility(View.GONE);
                disability_layout.setVisibility(View.VISIBLE);
                payment_layout.setVisibility(View.GONE);
                ride_layout.setVisibility(View.GONE);
                tripBtn.setTextColor(Color.WHITE);
                disabilityBtn.setTextColor(Color.BLACK);
                paymentBtn.setTextColor(Color.WHITE);
                rideBtn.setTextColor(Color.WHITE);
                titleTxt.setText("Choose a disability");
            }
        });

        nextBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tripBtn.setBackgroundResource(R.drawable.unselected_text);
                disabilityBtn.setBackgroundResource(R.drawable.unselected_text);
                paymentBtn.setBackgroundResource(R.drawable.selected_text);
                rideBtn.setBackgroundResource(R.drawable.unselected_text);
                trip_layout.setVisibility(View.GONE);
                disability_layout.setVisibility(View.GONE);
                payment_layout.setVisibility(View.VISIBLE);
                ride_layout.setVisibility(View.GONE);
                tripBtn.setTextColor(Color.WHITE);
                disabilityBtn.setTextColor(Color.WHITE);
                paymentBtn.setTextColor(Color.BLACK);
                rideBtn.setTextColor(Color.WHITE);
                titleTxt.setText("Payment Method");
            }
        });

        requestBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tripBtn.setBackgroundResource(R.drawable.unselected_text);
                disabilityBtn.setBackgroundResource(R.drawable.unselected_text);
                paymentBtn.setBackgroundResource(R.drawable.unselected_text);
                rideBtn.setBackgroundResource(R.drawable.selected_text);
                trip_layout.setVisibility(View.GONE);
                disability_layout.setVisibility(View.GONE);
                payment_layout.setVisibility(View.GONE);
                ride_layout.setVisibility(View.VISIBLE);
                tripBtn.setTextColor(Color.WHITE);
                disabilityBtn.setTextColor(Color.WHITE);
                paymentBtn.setTextColor(Color.WHITE);
                rideBtn.setTextColor(Color.BLACK);
                titleTxt.setText("Ride Confirmed");
            }
        });

    }
}