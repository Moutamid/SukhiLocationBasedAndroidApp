package com.example.sukhilocationbasedapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sukhilocationbasedapp.Model.Rider;
import com.example.sukhilocationbasedapp.Model.User;
import com.example.sukhilocationbasedapp.customer.MainScreen;
import com.example.sukhilocationbasedapp.customer.MenuItemsActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {

    private EditText fnameTxt,emailInput,passInput,cpassTxt;
    private Button signUpBtn;
    private String fname,email,password,cpassword;
    private CircleImageView profileImg;
    private ProgressDialog pd;
    FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference userdb,driverdb;
    private String image = "";
    private static final int PICK_IMAGE_REQUEST = 1;
    Uri uri;
    private String phoneNumber = "";
    StorageReference mStorage;
    private Bitmap bitmap;
    private ImageView backImg;
    private static final int STORAGE_PERMISSION_CODE = 101;
   // private SharedPreferencesManager manager;
    private String utype = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        fnameTxt = findViewById(R.id.fname);
        emailInput = findViewById(R.id.email);
        passInput = findViewById(R.id.pass);
        cpassTxt = findViewById(R.id.cpass);
        profileImg = findViewById(R.id.imageView);
        signUpBtn = findViewById(R.id.signUp);
        backImg = findViewById(R.id.back);
        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        //manager = new SharedPreferencesManager(this);
        //utype = manager.retrieveString("utype","");
        utype = getIntent().getStringExtra("utype");
        phoneNumber = getIntent().getStringExtra("phone");
        userdb = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        driverdb = FirebaseDatabase.getInstance().getReference("Drivers").child(user.getUid());
        if(utype.equals("user")){
            getUser();
        }else {
            getDriver();
        }

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(utype.equals("user")){
                    startActivity(new Intent(EditProfile.this,MainScreen.class));
                    finish();
                }else {
                    startActivity(new Intent(EditProfile.this, com.example.sukhilocationbasedapp.driver.MainScreen.class));
                    finish();
                }
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validInfo()){
                    pd = new ProgressDialog(EditProfile.this);
                    pd.setMessage("Creating Account....");
                    pd.show();
                    if(utype.equals("user")){
                        storeUser();
                    }else {
                        storeDriver();
                    }
                }
            }
        });
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
    }

    private void getDriver() {
        driverdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Rider rider = snapshot.getValue(Rider.class);
                    fname = rider.getFullname();
                    email = rider.getEmail();
                    password = rider.getPassword();
                    fnameTxt.setText(fname);
                    emailInput.setText(email);
                    passInput.setText(password);
                    image = rider.getImageUrl();
                    cpassTxt.setText(password);
                    if (rider.getImageUrl().equals("")){
                        Picasso.with(EditProfile.this)
                                .load(R.drawable.profile)
                                .into(profileImg);
                    }else {
                        Picasso.with(EditProfile.this)
                                .load(image)
                                .into(profileImg);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUser() {
        userdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    User rider = snapshot.getValue(User.class);
                    fname = rider.getFullname();
                    email = rider.getEmail();
                    password = rider.getPassword();
                    fnameTxt.setText(fname);
                    emailInput.setText(email);
                    passInput.setText(password);
                    image = rider.getImageUrl();
                    cpassTxt.setText(password);
                    if (rider.getImageUrl().equals("")){
                        Picasso.with(EditProfile.this)
                                .load(R.drawable.profile)
                                .into(profileImg);
                    }else {
                        Picasso.with(EditProfile.this)
                                .load(image)
                                .into(profileImg);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void checkPermission()
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(EditProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(EditProfile.this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, STORAGE_PERMISSION_CODE);
        }
        else {
            openGallery();
            Toast.makeText(EditProfile.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
                Toast.makeText(EditProfile.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(EditProfile.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"SELECT IMAGE"),PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            uri = data.getData();
            profileImg.setImageURI(uri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                saveInformation();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void saveInformation() {
        ProgressDialog dialog = new ProgressDialog(EditProfile.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("Uploading your profile....");
        dialog.show();
        if (uri != null) {
            profileImg.setDrawingCacheEnabled(true);
            profileImg.buildDrawingCache();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
            byte[] thumb_byte_data = byteArrayOutputStream.toByteArray();

            final StorageReference reference = mStorage.child("Profile Images").child(System.currentTimeMillis() + ".jpg");
            final UploadTask uploadTask = reference.putBytes(thumb_byte_data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return reference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                image = downloadUri.toString();
                                dialog.dismiss();
                            }
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }else {
            Toast.makeText(getApplicationContext(), "Please Select Image ", Toast.LENGTH_LONG).show();

        }
    }

    private void storeUser(){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        HashMap<String,Object> user = new HashMap<>();
        user.put("fullname",fname);
        user.put("email",email);
        user.put("password",password);
        user.put("imageUrl",image);
        userdb.child(firebaseUser.getUid()).setValue(user);
        pd.dismiss();
        Toast.makeText(EditProfile.this,"Registered Successfully",Toast.LENGTH_LONG).show();

        Intent intent = new Intent(EditProfile.this, MainScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void storeDriver(){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        HashMap<String,Object> user = new HashMap<>();
        user.put("fullname",fname);
        user.put("email",email);
        user.put("password",password);
        user.put("imageUrl",image);
        driverdb.child(firebaseUser.getUid()).setValue(user);
        pd.dismiss();
        Toast.makeText(EditProfile.this,"Registered Successfully",Toast.LENGTH_LONG).show();

        Intent intent = new Intent(EditProfile.this, com.example.sukhilocationbasedapp.driver.MainScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    //Validate Input Fields
    public boolean validInfo() {
        fname = fnameTxt.getText().toString();
        email = emailInput.getText().toString();
        password = passInput.getText().toString();
        cpassword = cpassTxt.getText().toString();

        if(fname.isEmpty()){
            fnameTxt.setText("Input Fullname");
            fnameTxt.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            emailInput.setError("Input email!");
            emailInput.requestFocus();
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Please input valid email!");
            emailInput.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            passInput.setError("Input password!");
            passInput.requestFocus();
            return false;
        }

        if(cpassword.isEmpty()){
            cpassTxt.setText("Input Confirm Password");
            cpassTxt.requestFocus();
            return false;
        }


        return true;
    }

}