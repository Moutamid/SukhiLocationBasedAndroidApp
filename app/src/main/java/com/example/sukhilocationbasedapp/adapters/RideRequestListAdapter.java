package com.example.sukhilocationbasedapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sukhilocationbasedapp.Model.Trip;
import com.example.sukhilocationbasedapp.Model.User;
import com.example.sukhilocationbasedapp.Model.Vehicle;
import com.example.sukhilocationbasedapp.R;
import com.example.sukhilocationbasedapp.listener.ItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RideRequestListAdapter extends RecyclerView.Adapter<RideRequestListAdapter.RideRequestViewHolder>{

    private Context mContext;
    private List<Trip> tripList;
    private ItemClickListener itemClickListener;

    public RideRequestListAdapter(Context mContext, List<Trip> tripList) {
        this.mContext = mContext;
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public RideRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.request_custom_layout,parent,false);
        return new RideRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RideRequestViewHolder holder, int position) {

        Trip model = tripList.get(position);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(model.getUserId());
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    holder.nameTxt.setText(user.getFullname());
                    if (user.getImageUrl().equals("")){
                        Picasso.with(mContext)
                                .load(R.drawable.profile)
                                .into(holder.profileImg);
                    }else {
                        Picasso.with(mContext)
                                .load(user.getImageUrl())
                                .into(holder.profileImg);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.pickupTxt.setText(model.getPickup());
        holder.dropoffTxt.setText(model.getDropoff());
        holder.priceTxt.setText(model.getPrice());
        holder.distanceTxt.setText(model.getDistance() + " km\n" + model.getTime() + " mins");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Vehicles").
                child(mAuth.getCurrentUser().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Vehicle vehicle = snapshot.getValue(Vehicle.class);
                    holder.disabilityTxt.setText(vehicle.getDisability());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public class RideRequestViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView profileImg;
        private TextView nameTxt,priceTxt,distanceTxt,pickupTxt,dropoffTxt,disabilityTxt;

        public RideRequestViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImg = itemView.findViewById(R.id.profile);
            nameTxt = itemView.findViewById(R.id.name);
            priceTxt = itemView.findViewById(R.id.price);
            distanceTxt = itemView.findViewById(R.id.distance);
            pickupTxt = itemView.findViewById(R.id.pickup);
            dropoffTxt = itemView.findViewById(R.id.dropoff);
            disabilityTxt = itemView.findViewById(R.id.disability);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null){
                        itemClickListener.onItemClick(getAdapterPosition(),view);
                    }
                }
            });
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
