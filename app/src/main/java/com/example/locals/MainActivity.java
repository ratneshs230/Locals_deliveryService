package com.example.locals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Intent Authintent=new Intent(MainActivity.this,Login_phone.class);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(Authintent);

                finish();

            }
        }, 1000);



    }
}
