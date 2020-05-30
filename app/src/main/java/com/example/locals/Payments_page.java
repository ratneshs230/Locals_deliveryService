package com.example.locals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class

Payments_page extends AppCompatActivity {
    String uid;
    ImageButton edit,cartItemsEdit;
    TextView Upi,Cod,deliveryName,deliveryAdd1,deliveryAdd2,deliveryAdd3;
    TextView cartTotal,cartItems;
    LinearLayout upiLayout,CodLayout;
    String TAG = "Payment_page";
    Button upipayment,codpayment;
    final int UPI_PAYMENT = 0;
    Integer sum;
    String Name,key,address;
    Order_model model;
    DatabaseReference orderRef,cartRef;
    Map<String,String> productIds;
    Cart_model cart_model;
    RecyclerView cartItemRecycler;
    private FirebaseRecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    LinearLayout headings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payments_page);
        model=new Order_model();
        productIds=new HashMap<>();

        cartItemsEdit=findViewById(R.id.cartItemsEdit);
        cartItemRecycler=findViewById(R.id.cartItemRecycler);
        cartItems=findViewById(R.id.cartItems);
        cartTotal=findViewById(R.id.cartotal);
        Upi=findViewById(R.id.upi);
        Cod=findViewById(R.id.Cod);
        deliveryName=findViewById(R.id.deliveryName);
        deliveryAdd1=findViewById(R.id.deliveryAdd1);
        deliveryAdd2=findViewById(R.id.deliveryAdd2);
        deliveryAdd3=findViewById(R.id.deliveryAdd3);
        edit=findViewById(R.id.addressEdit);
        upipayment=findViewById(R.id.upi_payment);
        codpayment=findViewById(R.id.cash_payment);
        upiLayout=findViewById(R.id.upilayout);
        CodLayout=findViewById(R.id.CodLayout);
       headings=findViewById(R.id.head);


        layoutManager = new LinearLayoutManager(this);
        cartItemRecycler.setLayoutManager(layoutManager);

        SharedPreferences mypref=getSharedPreferences("Phone_Preference",MODE_PRIVATE);
        uid=mypref.getString("uid","");
        sum=mypref.getInt("cartSum",0);
        Name=mypref.getString("name","");
        address=mypref.getString("address","");
        deliveryName.setText(Name);
        deliveryAdd1.setText(mypref.getString("add1",""));
        deliveryAdd2.setText(mypref.getString("add2",""));
        deliveryAdd3.setText(mypref.getString("add3",""));

        cartTotal.setText(sum+"");

        cartRef=FirebaseDatabase.getInstance().getReference().child("Cart").child(uid);
        orderRef= FirebaseDatabase.getInstance().getReference().child("Orders").child(uid);

        key=orderRef.push().getKey();

        model.setOrderkey(key);
        model.setSum(sum+"");
        model.setAddress(address);
        model.setUid(mypref.getString("uid",""));
        model.setOrder_time(
                Calendar.getInstance().getTime().toString());
        Log.w(TAG,Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+"/"+Calendar.getInstance().get(Calendar.MONTH)+"/"+Calendar.YEAR);
        model.setOrder_date(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+"/"+Calendar.getInstance().get(Calendar.MONTH)+"/"+Calendar.YEAR);


        model.setStatus(false);


        getProductdetails();


/*        Subtotal=getIntent().getStringExtra("Total");
        Log.w(TAG,"Subtotal=>"+Subtotal);
    }*/
        Log.w(TAG,"Name"+Name+"sum"+sum+"note"+key);

        upipayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setModeOfPayment("Online");
                payUsingUpi(Name, "ratneshs230-1@okhdfcbank", key, sum.toString());

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Payments_page.this,User_details.class);
                startActivity(intent);
            }
        });
        codpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                model.setModeOfPayment("COD");
                Toast.makeText(Payments_page.this,"Order placed. Mode of payment : Cash on Delivery ",Toast.LENGTH_LONG).show();
                orderRef.child(key).setValue(model);
                Intent intent=new Intent(Payments_page.this,OrderPlacedActivity.class);
                intent.putExtra("orderId",key);
                Log.w(TAG,"Order ID=>"+key);
                moveRecord(cartRef,orderRef.child(key).child("OrderList"));
                startActivity(intent);
            }
        });
        cartItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartItemRecycler.getVisibility()==View.GONE){
                    cartItemRecycler.setVisibility(View.VISIBLE);
                    headings.setVisibility(View.VISIBLE);

                    fetch();
                    Log.w(TAG,"VISIBLE");
                }else {
                    cartItemRecycler.setVisibility(View.GONE);
                    headings.setVisibility(View.GONE);
                }
                Log.w(TAG,"INVISIBLE");
            }
        });
        Upi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CodLayout.setVisibility(View.GONE);

                if(upiLayout.getVisibility()==View.GONE){
                    upiLayout.setVisibility(View.VISIBLE);
                }
                else
                    upiLayout.setVisibility(View.GONE);
            }
        });

        Cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upiLayout.setVisibility(View.GONE);
                if(CodLayout.getVisibility()==View.GONE){
                    CodLayout.setVisibility(View.VISIBLE);
                }
                else
                    CodLayout.setVisibility(View.GONE);

            }
        });
    }

    private void moveRecord(DatabaseReference fromPath,final DatabaseReference toPAth){
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toPAth.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete()){
                            Log.w(TAG,"Successfully moved");
                        }else{
                            Log.w(TAG,"Moved Failed");
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };fromPath.addListenerForSingleValueEvent(valueEventListener);
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartitemobjectview, parent, false);
                return new ViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Cart_model model) {


                holder.setProductname(model.getCart_Product_name());
                holder.setQty(model.getCart_Product_qty());
                holder.setProductPrice(model.getTotal_price());


            }
        };
        cartItemRecycler.setAdapter(adapter);
        adapter.startListening();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView productName, productPrice,product_qty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.cartItemName);
            productPrice = itemView.findViewById(R.id.cartItemPrice);
            product_qty=itemView.findViewById(R.id.cartItemQty);

        }
        public void setProductname(String string) {
            productName.setText(string);
        }
        public void setProductPrice(String price) {
            productPrice.setText(price);
        }
        public void setQty(String qty){
            product_qty.setText(qty);
        }

    }
    public void getProductdetails(){
        cart_model=new Cart_model();
        FirebaseDatabase.getInstance().getReference().child("Cart").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                    productIds.put(ds.child("cart_Product_ID").getValue(String.class),ds.child("cart_Product_qty").getValue(String.class));
                    Log.w(TAG,"Products And Qty List=>"+productIds);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    void payUsingUpi(String name, String upiId, String note, String amount) {
        Log.e("main ", "name " + name + "--up--" + upiId + "--" + note + "--" + amount);
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                //.appendQueryParameter("mc", "")
                //.appendQueryParameter("tid", "02125412")
                .appendQueryParameter("tr", "83183655")
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", 1+"")
                .appendQueryParameter("cu", "INR")
                //.appendQueryParameter("refUrl", "blueapp")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(Payments_page.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response " + resultCode);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(Payments_page.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(Payments_page.this, "Transaction successful.", Toast.LENGTH_SHORT).show();

                orderRef.child(key).setValue(model);

                Intent intent=new Intent(Payments_page.this,OrderPlacedActivity.class);
                intent.putExtra("orderId",key);
                moveRecord(cartRef,orderRef.child(key).child("OrderList"));
                startActivity(intent);
                Log.e("UPI", "payment successfull: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(Payments_page.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: "+approvalRefNo);

            }
            else {
                Toast.makeText(Payments_page.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: "+approvalRefNo);

            }
        } else {
            Log.e("UPI", "Internet issue: ");

            Toast.makeText(Payments_page.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}