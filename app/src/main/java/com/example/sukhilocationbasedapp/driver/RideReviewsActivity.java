package com.example.sukhilocationbasedapp.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sukhilocationbasedapp.Model.Reviews;
import com.example.sukhilocationbasedapp.R;
import com.example.sukhilocationbasedapp.adapters.ReviewListAdapter;
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

public class RideReviewsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private ImageView menuImg;
    private DatabaseReference mReviewsDB;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private List<Reviews> reviewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_reviews);
        menuImg = findViewById(R.id.menu);
        recyclerView = findViewById(R.id.recyclerView);
        manager = new LinearLayoutManager(RideReviewsActivity.this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mReviewsDB = FirebaseDatabase.getInstance().getReference().child("Reviews");
        reviewsList = new ArrayList<>();
        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RideReviewsActivity.this,RiderMenuItemActivity.class));
            }
        });

        getReviewsList();
    }

    private void getReviewsList() {
        Query query = mReviewsDB.orderByChild("riderId").equalTo(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Reviews model = ds.getValue(Reviews.class);
                        reviewsList.add(model);
                    }
                    ReviewListAdapter adapter = new ReviewListAdapter(RideReviewsActivity.this,
                            reviewsList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}