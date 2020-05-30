package com.example.locals;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User_details extends AppCompatActivity {

        EditText name,phn,add1,add2,add3;
        Button save;
        String uid,phoneNumber;
        String key;
        DatabaseReference ref;
        User_model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        uid=getIntent().getStringExtra("uid");

        SharedPreferences no_pref=getSharedPreferences("Phone_Preference",MODE_PRIVATE);
        phoneNumber=no_pref.getString("Phone","");

        model=new User_model();
        name=findViewById(R.id.name);
        phn=findViewById(R.id.phn);
        add1=findViewById(R.id.address1);
        add2=findViewById(R.id.address2);
        add3=findViewById(R.id.address3);
        save=findViewById(R.id.save);

        phn.setText(phoneNumber);

        name.setText(no_pref.getString("name",""));
        add1.setText(no_pref.getString("add1",""));
        add2.setText(no_pref.getString("add2",""));
        add3.setText(no_pref.getString("add3",""));

        ref= FirebaseDatabase.getInstance().getReference().child("User").child(phoneNumber);
        key=ref.push().getKey();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    if (TextUtils.isEmpty(name.getText().toString().trim())) {
                        name.setError("Name is Required");
                        name.requestFocus();
                    }
                     else if (TextUtils.isEmpty(add1.getText().toString().trim())) {
                        name.setError("House number/Apartment Number is Required");
                        name.requestFocus();
                    } else if (TextUtils.isEmpty(add2.getText().toString().trim())) {
                        name.setError("Area/Block is Required");
                        name.requestFocus();
                    } else if (TextUtils.isEmpty(add3.getText().toString().trim())) {

                        Toast.makeText(User_details.this, "Please fill in the City", Toast.LENGTH_SHORT).show();
                    }else{
                        saveDetail();
                        Intent intent=new Intent(User_details.this,Payments_page.class);
                        startActivity(intent);
                    }

            }
        });


    }

    private void saveDetail() {

        SharedPreferences pref=getSharedPreferences("Phone_Preference",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();




        model.setUid(uid);
        model.setPhn(phn.getText().toString());
        model.setName(name.getText().toString());
        model.setAdd1(add1.getText().toString());
        model.setAdd2(add2.getText().toString());
        model.setAdd3(add3.getText().toString());

        editor.putString("name",model.getName());
        editor.putString("address",model.getAdd1()+","+model.getAdd2()+","+model.getAdd3());
        editor.putString("add1",model.getAdd1());
        editor.putString("add2",model.getAdd2());
        editor.putString("add3",model.getAdd3());

        editor.apply();
        ref.child(key).setValue(model);
    }
}
