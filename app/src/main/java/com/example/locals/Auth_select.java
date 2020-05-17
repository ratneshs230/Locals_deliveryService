package com.example.locals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Auth_select extends AppCompatActivity {
        Button cust,vend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_select);

        cust=findViewById(R.id.customer);
        vend=findViewById(R.id.vendor);

        cust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Auth_select.this,Login_phone.class);
                intent.putExtra("Type","Customer");
                startActivity(intent);
            }
        });
        vend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Auth_select.this,Login_phone.class);
                intent.putExtra("Type","Vendor");
                startActivity(intent);

            }
        });
    }
}
