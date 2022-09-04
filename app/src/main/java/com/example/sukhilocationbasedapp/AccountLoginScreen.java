package com.example.sukhilocationbasedapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class AccountLoginScreen extends AppCompatActivity {

    private EditText phoneTxt;
    private Button nextBtn;
    private ImageView googleBtn;
    private ImageView facebookBtn;
    private static final int RC_SIGN_IN = 234;
    GoogleApiClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    FirebaseUser currrentUser;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_login_screen);
        phoneTxt = findViewById(R.id.phone);
        nextBtn = findViewById(R.id.next);
        googleBtn = findViewById(R.id.google);
        facebookBtn = findViewById(R.id.facebook);

        mAuth = FirebaseAuth.getInstance();
        currrentUser = mAuth.getCurrentUser();
        dialog = new ProgressDialog(AccountLoginScreen.this);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = phoneTxt.getText().toString();
                if (!phone.isEmpty()){
                    Intent intent = new Intent(AccountLoginScreen.this,VerficationCode.class);
                    intent.putExtra("phone",phone);
                    startActivity(intent);
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();



        mGoogleSignInClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(AccountLoginScreen.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(
                mGoogleSignInClient
        );
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dialog.setTitle("Logging into your Account");
        dialog.setMessage("Please wait, while logging your Account.....");
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        if (requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            try {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } catch (Exception e) {
                Toast.makeText(AccountLoginScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    sendUserToMainScreen();
                    dialog.dismiss();
                    // Toast.makeText(Login.this, "User Signed In", Toast.LENGTH_SHORT).show();
                } else {

                    dialog.dismiss();
                    Toast.makeText(AccountLoginScreen.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void sendUserToMainScreen() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(AccountLoginScreen.this, MainScreen.class);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(AccountLoginScreen.this,"Login First",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendUserToMainScreen();
    }
}