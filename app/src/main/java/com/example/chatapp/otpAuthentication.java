package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class otpAuthentication extends AppCompatActivity {
    TextView mchangenumber;
    EditText mgetotp;
    android.widget.Button mverifyotp;
    String enteredotp;

    FirebaseAuth firebaseAuth;
    ProgressBar mprogressBarOtpAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_authentication);
        mchangenumber = findViewById(R.id.changeNumber);
        mgetotp = findViewById(R.id.getOtp);
        mverifyotp = findViewById(R.id.verifyOtp);
        mprogressBarOtpAuth = findViewById(R.id.progressBarOtpAuth);

        firebaseAuth = FirebaseAuth.getInstance();
        mchangenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(otpAuthentication.this, MainActivity.class);
                startActivity(intent);
            }
        });
        mverifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredotp = mgetotp.getText().toString();
                if (enteredotp.isEmpty()){
                    Toast.makeText(otpAuthentication.this, "Enter Your OTP first", Toast.LENGTH_SHORT).show();
                }
                else {
                    mprogressBarOtpAuth.setVisibility(View.VISIBLE);
                    String coderecieved=getIntent().getStringExtra("otp");
                    PhoneAuthCredential credential= PhoneAuthProvider.getCredential(coderecieved,enteredotp);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    mprogressBarOtpAuth.setVisibility(View.INVISIBLE);
                    Toast.makeText(otpAuthentication.this, "Login Success", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(otpAuthentication.this,setProfile.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        mprogressBarOtpAuth.setVisibility(View.INVISIBLE);
                        Toast.makeText(otpAuthentication.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}