package com.example.sukhilocationbasedapp.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.sukhilocationbasedapp.Model.Vehicle;
import com.example.sukhilocationbasedapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class VehicleManagement extends AppCompatActivity {

    private EditText brandTxt,modelTxt,yearTxt,licenseTxt,colorTxt;
    private AppCompatButton saveBtn;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference db;
    private ImageView backImg;
    private String brand,model,year,license,color;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_management);

        brandTxt = findViewById(R.id.brand);
        modelTxt = findViewById(R.id.model);
        yearTxt = findViewById(R.id.year);
        licenseTxt = findViewById(R.id.plate);
        colorTxt = findViewById(R.id.color);
        saveBtn = findViewById(R.id.saveBtn);
        backImg = findViewById(R.id.back);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance().getReference().child("Vehicles");
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validInfo()){
                    pd = new ProgressDialog(VehicleManagement.this);
                    pd.setMessage("Adding Vehicle Information....");
                    pd.show();
                    addVehicles();
                }
            }
        });
        checkVehicles();
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VehicleManagement.this,MainScreen.class));
                finish();
            }
        });
    }

    private void checkVehicles() {
        db.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    saveBtn.setVisibility(View.GONE);
                    brandTxt.setEnabled(false);
                    modelTxt.setEnabled(false);
                    yearTxt.setEnabled(false);
                    licenseTxt.setEnabled(false);
                    colorTxt.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addVehicles() {
        String key = db.push().getKey();
        Vehicle vehicle = new Vehicle(key,user.getUid(),brand,model,year,license,color);
        db.child(user.getUid()).setValue(vehicle);
        pd.dismiss();
        Intent intent = new Intent(VehicleManagement.this, MainScreen.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    public boolean validInfo() {
        brand = brandTxt.getText().toString();
        model = modelTxt.getText().toString();
        year = yearTxt.getText().toString();
        license = licenseTxt.getText().toString();
        color = colorTxt.getText().toString();

        if(brand.isEmpty()){
            brandTxt.setText("Input Brand");
            brandTxt.requestFocus();
            return false;
        }

        if(model.isEmpty()){
            modelTxt.setText("Input Model No");
            modelTxt.requestFocus();
            return false;
        }
        if(year.isEmpty()){
            yearTxt.setText("Input Year");
            yearTxt.requestFocus();
            return false;
        }
        if(license.isEmpty()){
            licenseTxt.setText("Input License No");
            licenseTxt.requestFocus();
            return false;
        }
        if(color.isEmpty()){
            colorTxt.setText("Input Color");
            colorTxt.requestFocus();
            return false;
        }
        return true;
    }
}