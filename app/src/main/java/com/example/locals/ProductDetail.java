package com.example.locals;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class ProductDetail extends AppCompatActivity {
    String product_Id;
    DatabaseReference db,fileref,cartRef;

    Products_model model;
    Cart_model cart_model;
    String TAG="ProductDetails";
    ImageView product_detail_image;
    String cartKey;
    TextView  product_detail_name,product_detail_desc,product_detail_price,product_detail_units;
    Button addtoCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Intent i=getIntent();
        product_Id=i.getStringExtra("product_id");
        Log.w(TAG,"Product ID recieved=>"+product_Id);

        product_detail_desc=findViewById(R.id.product_detail_desc);
        product_detail_image=findViewById(R.id.product_detail_image);
        product_detail_name=findViewById(R.id.product_detail_name);
        product_detail_price=findViewById(R.id.product_detail_price);
        product_detail_units=findViewById(R.id.product_detail_unit);

        addtoCart=findViewById(R.id.addToCart);


        db= FirebaseDatabase.getInstance().getReference();
        fileref=db.child("Products").child(product_Id);
        model=new Products_model();
        cart_model=new Cart_model();
        cartRef=db.child("Cart");
        fetch();

        addtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cart_model.setCart_Product_name(model.getProduct_name());
                cart_model.setCart_Product_ID(model.getKey());
                cartKey=cartRef.push().getKey();
                cart_model.setCart_key(cartKey);


                cartRef.child(cartKey).setValue(cart_model);



            }
        });


    }

    public void fetch(){

        fileref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model=dataSnapshot.getValue(Products_model.class);
                Log.w(TAG,"model=>"+model);
                Log.w(TAG,"datasnapShot=>"+dataSnapshot);
                product_detail_name.setText(model.getProduct_name());
                product_detail_desc.setText(model.getProduct_desc());
                product_detail_price.setText(model.getPrice());
                product_detail_units.setText(model.getMeasure());

                Picasso.get().load(model.getImage()).into(product_detail_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });    }
}

