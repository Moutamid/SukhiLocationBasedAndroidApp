package com.example.sukhilocationbasedapp.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.sukhilocationbasedapp.Model.Trip;
import com.example.sukhilocationbasedapp.R;
import com.example.sukhilocationbasedapp.adapters.RideRequestListAdapter;
import com.example.sukhilocationbasedapp.databinding.ActivityNotificationBinding;
import com.example.sukhilocationbasedapp.driver.AvailableRequestsActivity;
import com.example.sukhilocationbasedapp.driver.TripDetails;
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

public class NotificationActivity extends AppCompatActivity {

    private ActivityNotificationBinding b;
    private List<Trip> tripList;
    private DatabaseReference mRequestTrip;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b =  ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        b.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MenuItemsActivity.class));
                finish();
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        b.recyclerView.setLayoutManager(manager);
        mRequestTrip = FirebaseDatabase.getInstance().getReference().child("Requests");
        tripList = new ArrayList<>();
        getRequestList();
    }

    private void getRequestList() {
        Query query = mRequestTrip.orderByChild("status").equalTo("accepted");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Trip model = ds.getValue(Trip.class);
                        if (model.getUserId().equals(user.getUid())) {
                            tripList.add(model);
                        }
                    }
                    b.recyclerView.setVisibility(View.VISIBLE);
                    b.noDataLayout.setVisibility(View.GONE);
                    RideRequestListAdapter adapter = new RideRequestListAdapter(NotificationActivity.this,
                            tripList);
                    b.recyclerView.setAdapter(adapter);
                    adapter.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onItemClick(int position, View view) {
                            Intent intent = new Intent(NotificationActivity.this, TripDetails.class);
                            intent.putExtra("trip",tripList.get(position));
                            startActivity(intent);
                        }
                    });
                    adapter.notifyDataSetChanged();
                }else {
                    b.recyclerView.setVisibility(View.GONE);
                    b.noDataLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}