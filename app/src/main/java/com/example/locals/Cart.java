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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class Cart extends AppCompatActivity {

    RecyclerView cart_recycler;
    Button payment;
    RecyclerView.LayoutManager layoutManager;
    Cart_model model;
    String TAG = "Cart_Page",uid;
    private FirebaseRecyclerAdapter adapter;
    DatabaseReference cart_reference;
    FirebaseDatabase databaseReference;
    List<Integer> price;
    private int sum=0;
    Map<String, Object> cartobject;
    DatabaseReference ref;
    int newqty,newrate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        uid=getIntent().getStringExtra("uid");
        payment=findViewById(R.id.payment_navigate);
        cart_recycler=findViewById(R.id.cart_recycler);
        model=new Cart_model();
        price=new ArrayList<>();
         cartobject= new HashMap<>();



        databaseReference=FirebaseDatabase.getInstance();
        cart_reference=databaseReference.getReference().child("Cart").child(uid);
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

    private int subtotal(){
        cart_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    price.add(ds.child("cart_Product_price").getValue(Integer.class));
                    Log.w(TAG, "Price-=>" + price);

                }
                for (int i = 0; i < price.size(); i++) {
                    Log.w(TAG, "Price_List" + i + "=>" + price.get(i));
                    sum = sum + price.get(i);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        Log.w(TAG, "sum=>" + sum);
        return sum;
    }
    public void fetch(){
        Query query = FirebaseDatabase.getInstance().getReference().child("Cart").child(uid);
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
            protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Cart_model model) {


                holder.setProduct_img(model.getCart_Image());
                holder.setProductname(model.getCart_Product_name());
                holder.setQty(model.getCart_Product_qty());
                holder.inc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer count = Integer.parseInt(holder.product_qty.getText().toString());
                        newqty = count + 1;
                        String quantity = newqty + "";
                        holder.product_qty.setText(quantity);
                        String rate=model.getCart_Product_price();
                        newrate=Integer.parseInt(rate)*newqty;
                        cartobject.put("cart_Product_qty",newqty+"");
                        cartobject.put("total_price",newrate+"");
                        Log.w(TAG,"NEW_PRICE=>"+newrate+"NEW_QUANTITY=>"+newqty);

                        ref=databaseReference.getReference().child("Cart").child(uid).child(model.getCart_Product_ID());
                        Log.w(TAG,"KEY=>"+model.getCart_key());
                        ref.updateChildren(cartobject);
                        holder.setProductPrice(newrate+"");


                    }
                });
                holder.dec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer count = Integer.parseInt(holder.product_qty.getText().toString());
                        newqty = count + 1;
                        String quantity = newqty + "";
                        holder.product_qty.setText(quantity);
                        String rate=holder.productPrice.getText().toString();
                        newrate=Integer.parseInt(rate)*newqty;
                        cartobject.put("cart_Product_qty",newqty+"");
                        cartobject.put("total_price",newrate+"");
                        Log.w(TAG,"NEW_PRICE=>"+newrate+"NEW_QUANTITY=>"+newqty);
                        holder.setProductPrice(newrate+"");

                        ref=databaseReference.getReference().child("Cart").child(uid).child(model.getCart_Product_ID());
                        Log.w(TAG,"KEY=>"+model.getCart_key());
                        ref.updateChildren(cartobject);
                    }
                });
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Cart.this, Payments_page.class);

                        intent.putExtra("Total", subtotal());
                        Log.w(TAG, "sum=>" + subtotal());

                        startActivity(intent);
                    }
                });

            }
        };
        cart_recycler.setAdapter(adapter);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout root;
        public TextView productName, productPrice,product_qty;
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
            product_qty=itemView.findViewById(R.id.qty);


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
        public void setQty(String qty){
            product_qty.setText(qty);
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
