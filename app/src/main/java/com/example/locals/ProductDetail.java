package com.example.locals;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProductDetail extends AppCompatActivity {
    String product_Id;
    DatabaseReference db,fileref,cartRef;

    ImageButton inc,dec;
    CoordinatorLayout coordinatorLayout;

    Products_model model;
    Cart_model cart_model;
    String TAG="ProductDetails";
    ImageView product_detail_image;
    String cartKey;
    TextView  product_detail_name,product_detail_desc,product_detail_price,product_detail_units,qty;
    Button addtoCart;
    int newqty=1;
   String uid,from,previousQty;
    Map<String, Object> cartobject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        try {
            product_Id = getIntent().getStringExtra("product_id");
            uid = getIntent().getStringExtra("uid");
            from=getIntent().getStringExtra("from");
            if(from.equals("cart")){
                previousQty=getIntent().getStringExtra("qty");
            }
            else previousQty=1+"";

            Log.w(TAG, "Qty  recieved=>" + previousQty);
            Log.w(TAG, "Product ID recieved=>" + product_Id);

            product_detail_desc = findViewById(R.id.product_detail_desc);
            product_detail_image = findViewById(R.id.product_detail_image);
            product_detail_name = findViewById(R.id.product_detail_name);
            product_detail_price = findViewById(R.id.product_detail_price);
            product_detail_units = findViewById(R.id.product_detail_unit);
            

            qty = findViewById(R.id.qty);
            inc = findViewById(R.id.increase_qty);
            dec = findViewById(R.id.decrease_qty);

            addtoCart = findViewById(R.id.addToCart);

            cartobject = new HashMap<>();
            db = FirebaseDatabase.getInstance().getReference();
            fileref = db.child("Products").child(product_Id);
            model = new Products_model();
            cart_model = new Cart_model();
            cartRef = db.child("Cart").child(uid);

            cartKey = cartRef.getKey();
            cart_model.setCart_key(cartKey);

            fetch();


            inc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer count = Integer.parseInt(qty.getText().toString());
                    newqty = count + 1;
                    String quantity = newqty + "";
                    qty.setText(quantity);


                }
            });

            dec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer count = Integer.parseInt(qty.getText().toString());

                    if(newqty==1){
                        dec.setEnabled(false);
                    }else if(newqty>1){
                        dec.setEnabled(true);
                        newqty = count - 1;

                    }
                    String quantity = newqty + "";
                    qty.setText(quantity);


                }
            });

            Log.w(TAG, "NEWQTY=>" + newqty);

            addtoCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer total=Integer.parseInt(model.getPrice()) * newqty;
                    cart_model.setUserId(uid);
                    cart_model.setCart_Product_name(model.getProduct_name());
                    cart_model.setCart_Product_ID(model.getKey());
                    cart_model.setCart_Product_price(model.getPrice());
                    cart_model.setTotal_price(total.toString());
                    cart_model.setCart_Product_ID(model.getKey());
                    cart_model.setCart_Product_qty(newqty+"");
                    cart_model.setCart_Image(model.getImage());
                    cart_model.setCart_measure(model.getMeasure());


                    cartRef.child(model.getKey()).setValue(cart_model);
                    Toast.makeText(ProductDetail.this,"Product Added to Cart",Toast.LENGTH_LONG).show();


                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        fetch();
    }




    @Override
    protected void onStop() {
        super.onStop();
    }

    /* private void showSnackbar() {
            Snackbar snackbar=Snackbar.make(coordinatorLayout,"View Items in Cart?",Snackbar.LENGTH_INDEFINITE)
                    .setAction("Open Cart", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                    Intent intent=new Intent(ProductDetail.this,Cart.class);
                    startActivity(intent);
                        }
                    });
            snackbar.show();
        }*/
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
                if(!previousQty.isEmpty()) {
                    qty.setText(previousQty);
                }

                Picasso.get().load(model.getImage()).into(product_detail_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });    }
}

