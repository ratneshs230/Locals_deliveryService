package com.example.locals;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Profile_page extends AppCompatActivity {
    ImageView imageView;
    ImageButton imageButton;
    TextView nameText,add1Text,add2Text,add3Text,phoneText,signOutText,textCart,textOrder;
    String uid,phone;
    User_model user_model;
    DatabaseReference ref;
    Uri imageUri;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        SharedPreferences mypref=getSharedPreferences("Phone_Preference",MODE_PRIVATE);
        uid=mypref.getString("uid","");
        phone=mypref.getString("Phone","");

        textCart=findViewById(R.id.profileCart);
        nameText=findViewById(R.id.profileName);
        add1Text=findViewById(R.id.add1);
        add2Text=findViewById(R.id.add2);
        add3Text=findViewById(R.id.add3);
        phoneText=findViewById(R.id.profilePhone);
        signOutText=findViewById(R.id.signout);
        textOrder=findViewById(R.id.profileOrder);
        signOutText=findViewById(R.id.signout);

        ref= FirebaseDatabase.getInstance().getReference().child("User").child(phone);
        storageReference= FirebaseStorage.getInstance().getReference().child("UsersProfile");

        textCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile_page.this,Cart.class);
                startActivity(intent);
            }
        });
        textOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile_page.this,OrderPage.class);
                startActivity(intent);
            }
        });
        signOutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                SharedPreferences pref=getSharedPreferences("Phone_Preference",MODE_PRIVATE);
                SharedPreferences.Editor editor=pref.edit();

                editor.putString("Phone","");
                editor.putString("uid","");


                editor.apply();
                Intent intent=new Intent(Profile_page.this,Login_phone.class);
                startActivity(intent);            }
        });


        fetch();


    }

    private void fetch() {

        ref.child("Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           user_model=dataSnapshot.getValue(User_model.class);
            nameText.setText(user_model.getName());
                add1Text.setText(user_model.getAdd1());
                add2Text.setText(user_model.getAdd2());
                add3Text.setText(user_model.getAdd3());
                phoneText.setText(user_model.getPhn());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
