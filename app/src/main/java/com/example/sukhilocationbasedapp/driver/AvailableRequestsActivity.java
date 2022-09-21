package com.example.sukhilocationbasedapp.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sukhilocationbasedapp.Model.Trip;
import com.example.sukhilocationbasedapp.R;
import com.example.sukhilocationbasedapp.adapters.RideRequestListAdapter;
import com.example.sukhilocationbasedapp.listener.ItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AvailableRequestsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView notifyTxt;
    private LinearLayoutManager manager;
    private ImageView menuImg;
    private LinearLayout dataLayout,noDataLayout;
    private DatabaseReference mRequestTrip;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private List<Trip> tripList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_requests);
        menuImg = findViewById(R.id.menu);
        recyclerView = findViewById(R.id.recyclerView);
        manager = new LinearLayoutManager(AvailableRequestsActivity.this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        notifyTxt = findViewById(R.id.count);
        dataLayout = findViewById(R.id.data_layout);
        noDataLayout = findViewById(R.id.no_data_layout);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mRequestTrip = FirebaseDatabase.getInstance().getReference().child("Requests");
        tripList = new ArrayList<>();
        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AvailableRequestsActivity.this,RiderMenuItemActivity.class));
            }
        });

        getRequestList();
    }

    private void getRequestList() {

        Query query = mRequestTrip.orderByChild("status").equalTo("pending");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Trip model = ds.getValue(Trip.class);
                        tripList.add(model);
                    }
                    noDataLayout.setVisibility(View.GONE);
                    dataLayout.setVisibility(View.VISIBLE);
                    notifyTxt.setVisibility(View.VISIBLE);
                    notifyTxt.setText("You have " + snapshot.getChildrenCount() + " new requests.");
                    RideRequestListAdapter adapter = new RideRequestListAdapter(AvailableRequestsActivity.this,
                            tripList);
                    recyclerView.setAdapter(adapter);
                    adapter.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onItemClick(int position, View view) {
                            Intent intent = new Intent(AvailableRequestsActivity.this,TripDetails.class);
                            intent.putExtra("trip",tripList.get(position));
                            startActivity(intent);
                        }
                    });
                    adapter.notifyDataSetChanged();
                }
                else {
                    noDataLayout.setVisibility(View.VISIBLE);
                    dataLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}