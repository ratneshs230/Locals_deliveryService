package com.example.locals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class OrderPage extends AppCompatActivity {
    RecyclerView orderRecycler;

    DatabaseReference order_reference;
    LinearLayoutManager layoutManager;
    FirebaseDatabase databaseReference;
    String uid;
    String TAG="OrderPage";
    private FirebaseRecyclerAdapter adapter,cat_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);
        SharedPreferences pref=getSharedPreferences("Phone_Preference",MODE_PRIVATE);
        uid=pref.getString("uid","");

        databaseReference= FirebaseDatabase.getInstance();
        order_reference=databaseReference.getReference().child("Orders").child(uid);
        layoutManager = new LinearLayoutManager(this);
        orderRecycler.setLayoutManager(layoutManager);



    }


    public void fetch() {
        Query query = FirebaseDatabase.getInstance().getReference().child("Products");
        FirebaseRecyclerOptions<Order_model> options = new FirebaseRecyclerOptions.Builder<Order_model>()
                .setQuery(query, Order_model.class)
                .build();
        Log.w(TAG, "query=>" + query);
        adapter = new FirebaseRecyclerAdapter<Order_model, ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_objectview, parent, false);
                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Order_model model) {

                holder.setOrderId(model.getOrderkey());
                if(model.getStatus()){
                    holder.setOrderStatus("Delivered on "+model.getDelivery_date()+" , "+model.getDelivery_time());
                    holder.setpaymentStatus("Payment Status : Paid");
                }else{
                    if(model.getModeOfPayment().equals("Online")){
                        holder.setpaymentStatus("Payment Status : Paid");
                    }else {
                        holder.setpaymentStatus("Payment Status : Pending");

                    }
                    holder.setOrderStatus("Awaiting Delivery");
                }

                holder.setOrderTotal("Total : Rs"+model.getSum());
                holder.setOrderAddress(model.getAddress());
                holder.setOrderDate(model.getOrder_date()+" "+model.getDelivery_time());

               /* holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(OrderPage.this, OrderDetails.class);
                        intent.putExtra("uid",uid);
                        intent.putExtra("product_id", model.getKey());
                        intent.putExtra("from","home");

                        startActivity(intent);
                    }
                });*/
            }
        };

        orderRecycler.setAdapter(adapter);
        adapter.startListening();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout root;
        public TextView OrderId, OrderTotal,OrderStatus,OrderDate,OrderAddress,PaymentStatus;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            OrderId = itemView.findViewById(R.id.orderId);
            OrderStatus = itemView.findViewById(R.id.orderDeliveryStatus);
            OrderDate = itemView.findViewById(R.id.orderDate);
            OrderTotal=itemView.findViewById(R.id.orderTotal);
            OrderAddress=itemView.findViewById(R.id.orderDeliveryAddress);
            PaymentStatus=itemView.findViewById(R.id.paymentStatus);

        }
        public LinearLayout getRoot() {
            return root;
        }
        public void setRoot(LinearLayout root) {
            this.root = root;
        }
        public void setOrderId(String string) {
            OrderId.setText(string);
        }
        public void setOrderTotal(String price) {
            OrderTotal.setText(price);
        }
        public void setOrderStatus(String status) {
            OrderStatus.setText(status);
        }
        public void setOrderDate(String date) {
            OrderDate.setText(date);
        }
        public void setOrderAddress(String address) {
            OrderAddress.setText(address);
        }
        public void setpaymentStatus(String status) {
            OrderStatus.setText(status);
        }

    }
}
