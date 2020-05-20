package com.example.locals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import static java.lang.Integer.parseInt;

public class Cart extends AppCompatActivity {
    RecyclerView cart_recycler;
    Button payment;
    RecyclerView.LayoutManager layoutManager;
    Cart_model model;
    String TAG = "Cart_Page";
    private FirebaseRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        payment=findViewById(R.id.payment_navigate);
        cart_recycler=findViewById(R.id.cart_recycler);
        model=new Cart_model();

        layoutManager = new LinearLayoutManager(this);
        cart_recycler.setLayoutManager(layoutManager);

        fetch();
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent=new Intent(Cart.this,Payments_page.class);
            startActivity(intent);
            }
        });

    }

    public void fetch(){
        Query query = FirebaseDatabase.getInstance().getReference().child("Cart");
        FirebaseRecyclerOptions<Cart_model> options = new FirebaseRecyclerOptions.Builder<Cart_model>()
                .setQuery(query, Cart_model.class)
                .build();
        Log.w(TAG, "query=>" + query);
        adapter = new FirebaseRecyclerAdapter<Cart_model, ViewHolder>(options) {


            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_objectviw, parent, false);
                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final Cart_model model) {

                holder.setProduct_img(model.getCart_Image());
                holder.setProductname(model.getCart_Product_name());
                holder.setProductPrice(model.getCart_Product_price());
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Cart.this, Payments_page.class);
                        intent.putExtra("Total", 20);
                        startActivity(intent);
                    }
                });

            }
        };
        cart_recycler.setAdapter(adapter);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout root;
        public TextView productName, productPrice,qty;
        public ImageView product_img;
        ImageButton inc,dec;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            productName = itemView.findViewById(R.id.cart_objectName);
            productPrice = itemView.findViewById(R.id.cart_objectPrice);
            product_img = itemView.findViewById(R.id.cart_objectImage);
            inc=itemView.findViewById(R.id.increase_qty);
            dec=itemView.findViewById(R.id.decrease_qty);
            qty=itemView.findViewById(R.id.qty);

            inc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer quantity=parseInt(qty.getText().toString());
                    qty.setText(quantity+1);
                }
            });
            dec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer quantity=parseInt(qty.getText().toString());
                    qty.setText(quantity-1);

                }
            });
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
}
