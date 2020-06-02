package com.example.locals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Login_phone extends AppCompatActivity {
    EditText phn;
    Button next_login;
    String phone;

    String TAG="LoginPhone";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);

        SharedPreferences mypref=getSharedPreferences("Phone_Preference",MODE_PRIVATE);
        String uid=mypref.getString("uid","");
        Log.w(TAG,"UID"+uid);




        phn = findViewById(R.id.phn);
        next_login = findViewById(R.id.login_next);



        next_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = phn.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    phn.setError("Valid number is required");
                    phn.requestFocus();
                    return;
                }

                String phonenumber = "+" + "91" + number;

                Intent intent = new Intent(Login_phone.this, OtpActivity.class);
                intent.putExtra("phonenumber", phonenumber);
                startActivity(intent);

            }
        });
    }


    }


