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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class Products_page extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    RecyclerView.LayoutManager layoutManager;
    RecyclerView products_recycler;
    String TAG = "Products_page";
    private FirebaseRecyclerAdapter adapter,cat_adapter;

    String category;
    Products_model model;
    ImageButton cart_btn,cat_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_page);



        products_recycler = findViewById(R.id.products_recycler);



        layoutManager = new GridLayoutManager(this, 2);
        products_recycler.setLayoutManager(layoutManager);
        cart_btn=findViewById(R.id.cart_btn);
        cat_btn=findViewById(R.id.cat_btn);

        cat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Products_page.this, v);
                popup.setOnMenuItemClickListener(Products_page.this);
                popup.inflate(R.menu.category_menu);
                popup.show();
            }
        });

        fetch();

    }

    public void fetch() {

        Query query = FirebaseDatabase.getInstance().getReference().child("Products");
        FirebaseRecyclerOptions<Products_model> options = new FirebaseRecyclerOptions.Builder<Products_model>()
                .setQuery(query, Products_model.class)
                .build();
        Log.w(TAG, "query=>" + query);
        adapter = new FirebaseRecyclerAdapter<Products_model, ViewHolder>(options) {


            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final Products_model model) {

                holder.setProduct_img(model.getImage());
                holder.setProductname(model.getProduct_name());
                holder.setProductPrice(model.getPrice());
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Products_page.this, ProductDetail.class);
                        intent.putExtra("product_id", model.getKey());
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

        public void setProductPrice(String price) {
            productPrice.setText(price);


        }

        public void setProduct_img(String img) {

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
    public void fetch_category_wise(String category) {
        try {
            model = new Products_model();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            Query query = database.child("Products").orderByChild("category").equalTo(category);

            FirebaseRecyclerOptions<Products_model> options = new FirebaseRecyclerOptions.Builder<Products_model>()
                    .setQuery(query, Products_model.class)
                    .build();
            cat_adapter = new FirebaseRecyclerAdapter<Products_model, ViewHolder>(options) {
                @NonNull
                @Override
                public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
                    return new ViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull final Products_model model) {
                    holder.setProduct_img(model.getImage());
                    Log.w(TAG,"imageURL=>"+model.getImage());
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

            products_recycler.setAdapter(cat_adapter);
            cat_adapter.startListening();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.category_menu, popup.getMenu());
        popup.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.vegetables:{
                category="Vegetables";
                fetch_category_wise(category);
                return true;}
            case R.id.fruits:{
                category="fruits";
                fetch_category_wise(category);
                return true;}
            case R.id.dairy:{
                category="Dairy";
                fetch_category_wise(category);
                return true;}

            default:
                return false;
        }
    }


}
