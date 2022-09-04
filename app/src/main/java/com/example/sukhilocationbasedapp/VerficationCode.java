package com.example.sukhilocationbasedapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class VerficationCode extends AppCompatActivity {

    private String phoneNumber;
    private String verificationId;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mFirebaseDatabase;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private EditText mEtCode;
    private Button mBtNext;
    private TextView  mTvResend, mTvTime,numberTxt;
    private TextView mToolbarTitle;
    String code;
    ProgressDialog dialog;
    private String userId;
    private CountDownTimer timer;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verfication_code);
        mEtCode = findViewById(R.id.code);
        mBtNext = findViewById(R.id.buttonContinue);
        mTvResend = findViewById(R.id.resendCode);
        mTvTime = findViewById(R.id.tv_countdown_sms);
        numberTxt = findViewById(R.id.number);
        phoneNumber = getIntent().getStringExtra("phone");
        dialog = new ProgressDialog(VerficationCode.this);
        numberTxt.setText(phoneNumber);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("Customer")
                .child(mFirebaseAuth.getCurrentUser().getUid());
        sendVerificationCode(phoneNumber);

        mBtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = mEtCode.getText().toString();

                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(VerficationCode.this, "Please Enter the code", Toast.LENGTH_LONG).show();
                } else {
                    verifyCode(code);
                }
            }
        });
        mTvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeResend(phoneNumber);
            }
        });

    }


    private void setUpVerificationCallbacks() {
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                resendToken = forceResendingToken;
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                code = phoneAuthCredential.getSmsCode();
                if (code != null) {
                    mEtCode.setText(code);
                    verifyCode(code);
                }
                signInWithCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Log.d("TAG", "Invalid credential: "
                            + e.getLocalizedMessage());
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // SMS quota exceeded
                    Log.d("TAG", "SMS Quota exceeded.");
                }
            }
        };
    }

    private void codeResend(String phone) {
        //textView.setText("00:00");

        setUpVerificationCallbacks();
        resetTimer();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mFirebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setForceResendingToken(resendToken)
                .setActivity(this).setCallbacks(mCallback).build();

        PhoneAuthProvider.verifyPhoneNumber(options);
        //timeStamp = false;
        //startStop();
    }

    private void resetTimer() {
        timer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long sec = (millisUntilFinished / 1000) % 60;
                mTvTime.setText("00" + ":" + f.format(sec));
            }
            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                mTvTime.setText("02:00");
                timer.cancel();
                disableButton();
            }
        }.start();
    }

    private void disableButton() {

        mTvResend.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mTvResend.setEnabled(true);
            }
        },60000*2);
    }


    private void verifyCode(String code) {
        dialog.setMessage("Verifying....");
        dialog.show();
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
        } catch (Exception e) {
            Toast.makeText(this, "Verification code wrong", Toast.LENGTH_SHORT).show();
        }

    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("phone",phoneNumber);
                            mFirebaseDatabase.updateChildren(hashMap);
                            dialog.dismiss();

                            Intent intent = new Intent(VerficationCode.this, MainScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(VerficationCode.this, "Cannot verify : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void sendVerificationCode(String number) {
        setUpVerificationCallbacks();

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mFirebaseAuth)
                .setPhoneNumber(number)
                .setTimeout(60L,TimeUnit.SECONDS)
                .setActivity(this).setCallbacks(mCallback).build();

        PhoneAuthProvider.verifyPhoneNumber(options);

    }



}