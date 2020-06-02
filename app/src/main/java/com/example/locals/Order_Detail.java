package com.example.locals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Order_Detail extends AppCompatActivity {
    TextView status,orderId,sum,mOp,add,toCust;
    RecyclerView productRecycler;
    String oId,uid;
    Order_model orderModel;
    Cart_model cartModel;
    FirebaseRecyclerAdapter adapter;
    String TAG="Order_Detail";
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__detail);

        oId=getIntent().getStringExtra("orderId");
        uid=getIntent().getStringExtra("uid");

        orderModel=new Order_model();
        cartModel=new Cart_model();

        status=findViewById(R.id.orderStatus);
        orderId=findViewById(R.id.orderId);
        sum=findViewById(R.id.sum);
        mOp=findViewById(R.id.mOp);
        toCust=findViewById(R.id.to_Cust);
        add=findViewById(R.id.addCust);
        productRecycler=findViewById(R.id.prodRecycler);

        layoutManager = new LinearLayoutManager(this);
        productRecycler.setLayoutManager(layoutManager);


        getUserDetails(oId);
        fetchProducts(oId);//different threads
        }

    private void getUserDetails(String oId) {
        FirebaseDatabase.getInstance().getReference().child("Orders").child(uid).child(oId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderModel=dataSnapshot.getValue(Order_model.class);
                    if(orderModel.getStatus())
                status.setText("Order Delivered Successfully");
                    else
                        status.setText("Order Awaiting Delivery");
                    orderId.setText(orderModel.getOrderkey());
                    sum.setText("Rs : "+orderModel.getSum());
                    if(orderModel.getModeOfPayment().equals("Online"))
                            mOp.setText("Online Upi Payment");
                    else
                        mOp.setText("Cash On Delivery");

                    toCust.setText("To "+orderModel.getUsername());
                    add.setText("At "+orderModel.getAddress());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG,databaseError+"");
            }
        });
    }
    public  void fetchProducts(String oid){
        Query query=FirebaseDatabase.getInstance().getReference().child("Orders").child(uid).child(oId).child("OrderList");
        FirebaseRecyclerOptions<Cart_model> options = new FirebaseRecyclerOptions.Builder<Cart_model>()
                .setQuery(query, Cart_model.class)
                .build();
        Log.w(TAG, "query=>" + query);
        adapter = new FirebaseRecyclerAdapter<Cart_model, ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderplaced_view, parent, false);
                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Cart_model model) {

                Log.w(TAG, "OnbindViewHolder=>");
                holder.setProductname(model.getCart_Product_name());
                holder.setProductPrice(model.getTotal_price());
                holder.setQty(model.getCart_Product_qty(),model.getCart_measure());
                holder.setProduct_img(model.getCart_Image());
            }
        };

        productRecycler.setAdapter(adapter);
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

