package com.example.sukhilocationbasedapp.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sukhilocationbasedapp.Model.Rider;
import com.example.sukhilocationbasedapp.Model.Trip;
import com.example.sukhilocationbasedapp.Model.Vehicle;
import com.example.sukhilocationbasedapp.R;
import com.example.sukhilocationbasedapp.adapters.LocationListAdapter;
import com.example.sukhilocationbasedapp.listener.ItemClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Driver;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestRide extends AppCompatActivity {

    private TextView titleTxt,tripBtn,disabilityBtn,paymentBtn,rideBtn;
    private ImageView closeImg;
    private RelativeLayout trip_layout,disability_layout,payment_layout,ride_layout;
    private AppCompatButton nextBtn1,nextBtn2,requestBtn1,requestBtn2;
    private ImageView backBtn1,backBtn2,backBtn3,backBtn4;
    private TextView pickup_location,drop_location;
    private RelativeLayout arms_layout,backbone_layout,mental_layout,leg_layout,cash_layout,easypaisa_layout;
    private CircleImageView profileImg;
    private TextView nameTxt,carTxt,pickup,dropoff,timeTxt,distanceTxt,priceTxt;
    private TextView priceTxt1,priceTxt2,priceTxt3,priceTxt4,timeTxt1,timeTxt2,timeTxt3,timeTxt4;
    private String sourceLocation,destinationLocation = "";
    private DatabaseReference mRequestTrip;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String disability = "";
    private String price = "";
    private int time = 0;
    private String cash = "";
    private ProgressDialog pd;
    private double driverLat = 0;
    private double driverLng = 0;
    private double custLat = 0;
    private double custLng = 0;
    private double desLat = 0;
    private double desLng = 0;
    private RequestQueue requestQueue;
    private String key = "";
    private double distance = 0;
    private double estimatedDriveTimeInMinutes = 0.0;

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
        cash_layout = findViewById(R.id.cash_payment);
        easypaisa_layout = findViewById(R.id.easypaisa);
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

        priceTxt1 = findViewById(R.id.rps);
        priceTxt2 = findViewById(R.id.rps1);
        priceTxt3 = findViewById(R.id.rps2);
        priceTxt4 = findViewById(R.id.rps3);
        timeTxt1 = findViewById(R.id.time);
        timeTxt2 = findViewById(R.id.time1);
        timeTxt3 = findViewById(R.id.time2);
        timeTxt4 = findViewById(R.id.time3);
        requestQueue = Volley.newRequestQueue(RequestRide.this);

        sourceLocation = getIntent().getStringExtra("source");
        destinationLocation = getIntent().getStringExtra("destination");
        pd = new ProgressDialog(RequestRide.this);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mRequestTrip = FirebaseDatabase.getInstance().getReference().child("Requests");
        pickup_location.setText(sourceLocation);
        drop_location.setText(destinationLocation);
        getCustomerLatLng();
        getLatLng(destinationLocation);
      //  getDriverLatLng();
        arms_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disability = "arms";
                time = 10;
                price = priceTxt1.getText().toString();
                //int randomTime = new Random().nextInt(10);
                //showAlertDialogBox(randomTime);
                arms_layout.setBackgroundResource(R.drawable.selected_dividers);
                backbone_layout.setBackgroundResource(R.drawable.dividers);
                mental_layout.setBackgroundResource(R.drawable.dividers);
                leg_layout.setBackgroundResource(R.drawable.dividers);
            }
        });
        backbone_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disability = "backbone";
                time = 5;
                price = priceTxt2.getText().toString();
                //int randomTime = new Random().nextInt(10);
               // showAlertDialogBox(randomTime);
                arms_layout.setBackgroundResource(R.drawable.dividers);
                backbone_layout.setBackgroundResource(R.drawable.selected_dividers);
                mental_layout.setBackgroundResource(R.drawable.dividers);
                leg_layout.setBackgroundResource(R.drawable.dividers);
            }
        });
        mental_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disability = "mental";
                time = 5;
                price = priceTxt3.getText().toString();
                //int randomTime = new Random().nextInt(10);
                //showAlertDialogBox(randomTime);
                arms_layout.setBackgroundResource(R.drawable.dividers);
                backbone_layout.setBackgroundResource(R.drawable.dividers);
                mental_layout.setBackgroundResource(R.drawable.selected_dividers);
                leg_layout.setBackgroundResource(R.drawable.dividers);
            }
        });
        leg_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disability = "legs";
                time = 5;
                price = priceTxt4.getText().toString();
                //int randomTime = new Random().nextInt(10);
                //showAlertDialogBox(randomTime);
                arms_layout.setBackgroundResource(R.drawable.dividers);
                backbone_layout.setBackgroundResource(R.drawable.dividers);
                mental_layout.setBackgroundResource(R.drawable.dividers);
                leg_layout.setBackgroundResource(R.drawable.selected_dividers);
            }
        });

        cash_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cash = "Cash on delivery";
                cash_layout.setBackgroundResource(R.drawable.selected_dividers);
                easypaisa_layout.setBackgroundResource(R.drawable.dividers);
            }
        });
        easypaisa_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cash = "EasyPaisa";
                cash_layout.setBackgroundResource(R.drawable.dividers);
                easypaisa_layout.setBackgroundResource(R.drawable.selected_dividers);
            }
        });
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
                if (!disability.equals("")) {
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
            }
        });

        requestBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = mRequestTrip.push().getKey();
                if (!cash.equals("")){
                    Trip model = new Trip(key,user.getUid(),sourceLocation,destinationLocation,price,time,
                            "",cash,"pending", (int) distance);
                    mRequestTrip.child(key).setValue(model);
                    pd.setMessage("Searching For Driver......");
                    pd.show();
                    searchingForDriver(key);
                }
            }
        });

        requestBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestRide.this,MainScreen.class);
                intent.putExtra("key",key);
                intent.putExtra("destination",destinationLocation);
                intent.putExtra("lat",desLat);
                intent.putExtra("lng",desLng);
                intent.putExtra("driver_lat",driverLat);
                intent.putExtra("driver_lng",driverLng);
                startActivity(intent);
            }
        });

    }

    private void getLatLng(String destinationLocation) {
        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?input="+destinationLocation+"&key=AIzaSyAywE2WbCBtd5oeitbemZ4Yr3B99efVylU";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  bar.setVisibility(View.INVISIBLE);
                        try {
                            JSONObject user = new JSONObject(response);
                            JSONArray friends = user.getJSONArray("results");
                            for (int i = 0; i < friends.length(); i++) {
                                JSONObject jsonObject = friends.getJSONObject(i);
                                if (jsonObject.getString("name").equals(destinationLocation)) {
                                    JSONObject geometryObject = jsonObject.getJSONObject("geometry");
                                    desLat = geometryObject.getJSONObject("location").getDouble("lat");
                                    desLng = geometryObject.getJSONObject("location").getDouble("lng");
                                    if (custLat != 0 && custLng != 0){
                                        calculateDistance(custLat,custLng,desLat,desLng);
                                    }
                                }
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                            // progressDialog.dismiss();
                            //  bar.setVisibility(View.INVISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(stringRequest);


    }


    private void searchingForDriver(String childKey) {
        //Toast.makeText(this, ""+ childKey, Toast.LENGTH_SHORT).show();
        //Query query = mRequestTrip.child(childKey).orderByChild("status").equalTo("accepted");
        mRequestTrip.child(childKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                 //   for (DataSnapshot ds : snapshot.getChildren()){
                        Trip model = snapshot.getValue(Trip.class);
                        if(!model.getRiderId().equals("")){
                            key = model.getId();
                            getRiderLatLng(model.getRiderId());
                            pd.dismiss();
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
                            getRiderData(model);

                        }
                    }
                //}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getCustomerLatLng() {
        DatabaseReference driversOnlineDB = FirebaseDatabase.getInstance().getReference()
                .child("Customers Available").child(user.getUid());
        driversOnlineDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    custLat = (double) snapshot.child("l").child("0").getValue();
                    custLng = (double) snapshot.child("l").child("1").getValue();
                    //Toast.makeText(RequestRide.this, "" + custLat + " ," + custLng, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDriverLatLng() {
        DatabaseReference driversOnlineDB = FirebaseDatabase.getInstance().getReference()
                .child("Drivers Available");
        driversOnlineDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds: snapshot.getChildren()){
                        double lat = (double) ds.child("l").child("0").getValue();
                        double lng = (double) ds.child("l").child("1").getValue();


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void calculateDistance(double custLat, double custLng, double lat, double lng) {
        /*double theta = custLng - lng;
        double dist = Math.sin(deg2rad(custLat))
                * Math.sin(deg2rad(lat))
                + Math.cos(deg2rad(custLat))
                * Math.cos(deg2rad(lat))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        distance = (int) dist;*/

        Location location = new Location("One");
        location.setLatitude(custLat);
        location.setLongitude(custLng);
        Location location1 = new Location("Two");
        location1.setLatitude(lat);
        location1.setLongitude(lng);
        distance = location.distanceTo(location1);
     //   distance = distance/1000;
        int speedIs10MetersPerMinute = 10;
        estimatedDriveTimeInMinutes =  distance/ speedIs10MetersPerMinute;
     //   float estimatedTime = distance/location.getSpeed();
       // showAlertDialogBox((int) estimatedDriveTimeInMinutes);
//        Toast.makeText(RequestRide.this, ""+estimatedDriveTimeInMinutes, Toast.LENGTH_SHORT).show();
    }
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    private void getRiderLatLng(String riderId) {
        DatabaseReference driversOnlineDB = FirebaseDatabase.getInstance().getReference()
                .child("Drivers Available").child(riderId);
        driversOnlineDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    driverLat = (double) snapshot.child("l").child("0").getValue();
                    driverLng = (double) snapshot.child("l").child("1").getValue();
                    if(custLat != 0 && custLng != 0){
                        calculateAlertDistance(custLat,custLng,driverLat,driverLng);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getRiderData(Trip model) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Drivers").child(model.getRiderId());
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Rider user = snapshot.getValue(Rider.class);
                    nameTxt.setText(user.getFullname());
                    if (user.getImageUrl().equals("")){
                        Picasso.with(RequestRide.this)
                                .load(R.drawable.profile)
                                .into(profileImg);
                    }else {
                        Picasso.with(RequestRide.this)
                                .load(user.getImageUrl())
                                .into(profileImg);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Vehicles").child(model.getRiderId());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Vehicle vehicle = snapshot.getValue(Vehicle.class);
                    carTxt.setText(vehicle.getBrand());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        pickup.setText(model.getPickup());
        dropoff.setText(model.getDropoff());
        timeTxt.setText(model.getTime());
        priceTxt.setText(model.getPrice());
    }

    private void calculateAlertDistance(double custLat, double custLng, double lat, double lng) {
        /*double theta = custLng - lng;
        double dist = Math.sin(deg2rad(custLat))
                * Math.sin(deg2rad(lat))
                + Math.cos(deg2rad(custLat))
                * Math.cos(deg2rad(lat))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        distance = (int) dist;*/

        Location location = new Location("One");
        location.setLatitude(custLat);
        location.setLongitude(custLng);
        Location location1 = new Location("Two");
        location1.setLatitude(lat);
        location1.setLongitude(lng);
        distance = location.distanceTo(location1);
        //   distance = distance/1000;
        int speedIs10MetersPerMinute = 10;
        estimatedDriveTimeInMinutes =  distance/ speedIs10MetersPerMinute;
        //   float estimatedTime = distance/location.getSpeed();
        showAlertDialogBox((int) estimatedDriveTimeInMinutes);
//        Toast.makeText(RequestRide.this, ""+estimatedDriveTimeInMinutes, Toast.LENGTH_SHORT).show();
    }

    private void showAlertDialogBox(int time) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RequestRide.this);
        LayoutInflater inflater = getLayoutInflater();
        View add_view = inflater.inflate(R.layout.disability_alert_dialog,null);
        ImageView closeImg = add_view.findViewById(R.id.close);
        TextView textView = add_view.findViewById(R.id.timeTxt);
        builder.setView(add_view);
        textView.setText("Your driver can be arrived in " + time + " minutes.");
        AlertDialog alertDialog = builder.create();
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
      //  searchingForDriver();
    }
}