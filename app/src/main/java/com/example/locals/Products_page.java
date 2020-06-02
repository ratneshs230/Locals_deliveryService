package com.example.locals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Products_page extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    RecyclerView.LayoutManager layoutManager;
    RecyclerView products_recycler;
    String TAG = "Products_page";
    private FirebaseRecyclerAdapter adapter,cat_adapter;
    Button addtoBag,btn;
    String category;
    LinearLayout setqty;
    Products_model model;
    Map<String, Object> product_object;
    DatabaseReference ref;
    ImageButton cart_btn,cat_btn,profile_link,order_btn;
    String no;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uid;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_page);

        Intent i=getIntent();
        no=i.getStringExtra("no");

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();

        if(user!=null){
            uid=user.getUid();
        }

        SharedPreferences pref=getSharedPreferences("Phone_Preference",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();

        editor.putString("uid",uid);
        editor.apply();

        order_btn=findViewById(R.id.order_btn);
        products_recycler = findViewById(R.id.products_recycler);
        profile_link=findViewById(R.id.prof_btn);
        title=findViewById(R.id.page_title);
        btn=findViewById(R.id.add_btn);
        product_object=new HashMap<>();
        layoutManager = new GridLayoutManager(this, 3);
        products_recycler.setLayoutManager(layoutManager);
        cart_btn=findViewById(R.id.cart_btn);
        cat_btn=findViewById(R.id.cat_btn);

        if(no!=null && no.equals("+911111222233")){
            btn.setVisibility(View.VISIBLE);
        }else{
            btn.setVisibility(View.GONE);
        }
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Products_page.this,OrderPage.class);
                startActivity(intent);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Products_page.this,Add_product.class);
                startActivity(intent);
            }
        });

        profile_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Products_page.this,Profile_page.class);
                startActivity(intent);
            }
        });

        cat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Products_page.this, v);
                popup.setOnMenuItemClickListener(Products_page.this);
                popup.inflate(R.menu.category_menu);
                popup.show();
            }
        });

        cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Products_page.this,Cart.class);


                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });

        fetch();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fetch();
    }

    public void fetch() {
        title.setText("Products");
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
            protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Products_model model) {

                holder.setProduct_img(model.getImage());
                holder.setProductname(model.getProduct_name());
                holder.setProductUnits(model.getMeasure());

                holder.setProductPrice(model.getPrice());

                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Products_page.this, ProductDetail.class);
                        intent.putExtra("uid",uid);
                        intent.putExtra("product_id", model.getKey());
                        intent.putExtra("from","home");

                        startActivity(intent);
                    }
                });
            }
        };

        products_recycler.setAdapter(adapter);
        adapter.startListening();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ConstraintLayout root;
        public TextView productName, productPrice,productUnit;
        public ImageView product_img;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.product_price);
            product_img = itemView.findViewById(R.id.productImage);
            productUnit=itemView.findViewById(R.id.units);
        }
        public ConstraintLayout getRoot() {
            return root;
        }
        public void setRoot(ConstraintLayout root) {
            this.root = root;
        }
        public void setProductname(String string) {
            productName.setText(string);
        }
        public void setProductPrice(String price) {
        productPrice.setText(price);
        }
        public void setProductUnits(String unit) {
            productPrice.setText(unit);
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
            title.setText(category);
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
                    holder.setProductUnits(model.getMeasure());
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
                category="Fruits";
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
