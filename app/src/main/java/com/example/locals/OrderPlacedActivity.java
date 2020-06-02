package com.example.locals;

import androidx.annotation.NonNull;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

public class OrderPlacedActivity extends AppCompatActivity {
    TextView textname, textadd1, textadd2, textadd3,textOrderId,textTotal,textTime;
    Button orderList;
    RecyclerView.LayoutManager layoutManager;

    String Name, uid, orderId;
    String TAG = "PlacedOrder";
    DatabaseReference orderRef;
    FirebaseRecyclerAdapter adapter;
    RecyclerView placedObejct_Recycler;
    SharedPreferences mypref;
    Order_model model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);

        orderId = getIntent().getStringExtra("orderId");

        Log.w(TAG,"Order ID=>"+orderId);
        mypref = getSharedPreferences("Phone_Preference", MODE_PRIVATE);
        uid = mypref.getString("uid", "");

        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(uid).child(orderId);

        textTime=findViewById(R.id.placed_time);
        textOrderId=findViewById(R.id.placed_OrderId);
        textTotal=findViewById(R.id.placed_deliverytotal);
        textname=findViewById(R.id.placed_deliveryName);
        textadd1=findViewById(R.id.placed_deliveryAdd1);
        textadd2=findViewById(R.id.placed_deliveryAdd2);
        textadd3=findViewById(R.id.placed_deliveryAdd3);
        placedObejct_Recycler=findViewById(R.id.placedOrderRecycler);
        orderList=findViewById(R.id.allOrders);

        layoutManager = new LinearLayoutManager(this);
        placedObejct_Recycler.setLayoutManager(layoutManager);

        textadd1.setText(mypref.getString("add1", ""));
        textadd2.setText(mypref.getString("add2", ""));
        textadd3.setText(mypref.getString("add3", ""));
        textOrderId.setText(orderId);
        textname.setText(mypref.getString("name",""));
        Name = mypref.getString("name", "");

        fetch();

        orderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderPlacedActivity.this,OrderPage.class);
                startActivity(intent);

            }
        });
    }


    public void fetch() {

        textOrderId.setText("Order ID : "+orderId);

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //model=dataSnapshot.getValue(Order_model.class);
                Log.w(TAG,"Datasnapshot=>"+dataSnapshot);

                textTime.setText(String.valueOf(dataSnapshot.child("order_time").getValue()));
                textTotal.setText("Total : "+dataSnapshot.child("sum").getValue(String.class));
                textadd1.setText(String.valueOf(dataSnapshot.child("add1").getValue()));
                textadd2.setText(String.valueOf(dataSnapshot.child("add2").getValue()));
                textadd3.setText(String.valueOf(dataSnapshot.child("add3").getValue()));
                textOrderId.setText(orderId);
                textname.setText(String.valueOf(dataSnapshot.child("username").getValue()));



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query query = FirebaseDatabase.getInstance().getReference().child("Orders").child(uid).child(orderId).child("OrderList");
        FirebaseRecyclerOptions<Cart_model> options = new FirebaseRecyclerOptions.Builder<Cart_model>()
                .setQuery(query, Cart_model.class)
                .build();
        Log.w(TAG, "query=>" + query);
        adapter = new FirebaseRecyclerAdapter<Cart_model, ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderplaced_view, parent, false);
                Log.w(TAG, "viewHolder=>");
                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Cart_model model) {

                Log.w(TAG, "OnbindViewHolder=>");

              holder.setProductname(model.getCart_Product_name());
                holder.setProductPrice(model.getCart_Product_price());
                holder.setQty(model.getCart_Product_qty(),model.getCart_measure());

                holder.setProduct_img(model.getCart_Image());


            }
        };
        placedObejct_Recycler.setAdapter(adapter);
        adapter.startListening();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView productName, productPrice, product_qty;
                ImageView product_img;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            productName = itemView.findViewById(R.id.placed_objectName);
            productPrice = itemView.findViewById(R.id.placed_objectPrice);
            product_qty = itemView.findViewById(R.id.placed_objectQty);
            product_img=itemView.findViewById(R.id.placed_objectImage);
            Log.w(TAG, "viewHolderClass=>");


        }

        public void setProductname(String string) {
            productName.setText(string);
        }

        public void setProductPrice(String price) {
            productPrice.setText("Rs "+price);
        }

        public void setQty(String qty,String units) {
            product_qty.setText("Quantity :"+qty+units);
        }
        public void setProduct_img(String img){
            Picasso.get().load(img).into(product_img);
        }

    }


        }
