package com.example.locals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class Login_phone extends AppCompatActivity {
    EditText phn,otp;
    Button next_login,verify;
    String phone,otp_entered;

    String TAG="LoginPhone";
    private boolean mVerificationInProgress = false;
    FirebaseAuth mAuth;
    String CodeSent;
    User_model model;
    DatabaseReference firebaseDatabase;
    DatabaseReference reference;
    private String pushKey,type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);

        Intent i=getIntent();
        type=i.getStringExtra("Type");


        phn = findViewById(R.id.phn);
        next_login = findViewById(R.id.login_next);
        otp = findViewById(R.id.otp);
        verify = findViewById(R.id.verify);

        model=new User_model();

        if(type.equals("Customer")){
            model.setUser_type("Customer");
        }
        else if(type.equals("Vendor")){
            model.setUser_type("Vendor");
        }

        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase=FirebaseDatabase.getInstance().getReference();
        reference=firebaseDatabase.child("Users");
        pushKey=reference.push().getKey();
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verifySignInCode();
            }
        });
        next_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    signInWithPhone(phone);


            }
        });
    }

    private void verifySignInCode() {
        otp_entered=otp.getText().toString();
         PhoneAuthCredential credential=PhoneAuthProvider.getCredential(CodeSent,otp_entered);
          signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhone(String phone){
        Log.w(TAG,"signInmethod");
        phone=phn.getText().toString();
        if(phone.isEmpty()){
            phn.setError("Phone Number Is Required");
            phn.requestFocus();
            return;
        }
        if(phone.length()<10){
            phn.setError("Please Enter a Valid Phone Number");
            phn.requestFocus();
            return;
        }
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);

                mVerificationInProgress = false;

                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                CodeSent=s;
            }
        };
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
        );
        model.setUser_contact(phone);

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "signInWithCredential:success");

                    FirebaseUser user = task.getResult().getUser();
                    model.setUid(user.getUid());
                    model.setKey(pushKey);


                    reference.child(model.getUid()).push().setValue(model);

                    Intent intent=new Intent(Login_phone.this,Products_page.class);
                    intent.putExtra("Type",model.getUser_type());
                    startActivity(intent);

                }else {
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                    }
                    Toast.makeText(Login_phone.this,"Failed Login",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}
