package com.example.locals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class Products_page extends AppCompatActivity {
    RecyclerView.LayoutManager layoutManager;
    RecyclerView products_recycler;
    String TAG="Products_page";
    private FirebaseRecyclerAdapter adapter;
    FloatingActionButton add_product;
    String type;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_page);

        Intent i=getIntent();
        type=i.getStringExtra("Type");

        add_product=findViewById(R.id.floatingActionButton);
        products_recycler=findViewById(R.id.products_recycler);

        if(type.equals("Vendor")){
            add_product.setVisibility(View.VISIBLE);
        }
        else if(type.equals("Customer")){
            add_product.setVisibility(View.GONE);
        }

        layoutManager=new GridLayoutManager(this,2);
        products_recycler.setLayoutManager(layoutManager);

        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Products_page.this,Add_product.class);
                startActivity(intent);
            }
        });
        fetch();

    }

    public void fetch(){

        Query query = FirebaseDatabase.getInstance().getReference().child("Products");
        FirebaseRecyclerOptions<Products_model> options = new FirebaseRecyclerOptions.Builder<Products_model>()
                .setQuery(query, Products_model.class)
                .build();
        Log.w(TAG,"query=>"+query);
        adapter=new FirebaseRecyclerAdapter<Products_model, ViewHolder>(options){


            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
                return new ViewHolder(view);            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final Products_model model) {

                holder.setProduct_img(model.getImage());
                holder.setProductname(model.getProduct_name());
                holder.setProductPrice(model.getPrice());
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Products_page.this,ProductDetail.class);
                        intent.putExtra("product_id",model.getKey());
                        startActivity(intent);
                    }
                });

            }
        };
        products_recycler.setAdapter(adapter);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout root;
        public TextView productName, productPrice;


        public ImageView product_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.product_price);
            product_img = itemView.findViewById(R.id.productImage);


        }

        public LinearLayout getRoot() {
            return root;
        }

        public void setRoot(LinearLayout root) {
            this.root = root;
        }



        public void setProductname(String string) {
            productName.setText(string);
        }

        public void setProductPrice(Integer price) {
            productPrice.setText(price);


        }
            public void setProduct_img(String img){

            Picasso.get().load(img).into(product_img);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
