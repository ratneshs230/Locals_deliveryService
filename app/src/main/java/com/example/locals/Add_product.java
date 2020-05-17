 package com.example.locals;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

 public class Add_product extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
     ImageView addPhoto;
     TextView product_name, product_desc, product_price;
     Spinner measure_spinner;
    Button addBtn;
     private String[] unit = new String[]{"kg", "grams"};
     Uri imageUri;
     Products_model model;
     String TAG = "Add products";
     DatabaseReference db, reference;
     StorageReference storageReference;
     String pushKey, measure;

     @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         try {
             if (requestCode == 1000) {
                 if (resultCode == Activity.RESULT_OK) {
                     imageUri = data.getData();
                     addPhoto.setImageURI(imageUri);

                 }
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_add_product);
         addPhoto = findViewById(R.id.productAddpic);
         product_name = findViewById(R.id.productAddname);
         product_desc = findViewById(R.id.productAddDesc);
         product_price = findViewById(R.id.productAddprice);
         measure_spinner = findViewById(R.id.measure);
         addBtn=findViewById(R.id.add_product_btn);

         reference = FirebaseDatabase.getInstance().getReference();
         db = reference.child("Products");
         storageReference = FirebaseStorage.getInstance().getReference().child("Products");


         model = new Products_model();
         ArrayAdapter<String> adapter = new ArrayAdapter(this,
                 android.R.layout.simple_spinner_item, unit);
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

         measure_spinner.setAdapter(adapter);
         measure_spinner.setOnItemSelectedListener(this);

         addBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 store_data();
             }
         });

         addPhoto.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View view) {
                 Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                 startActivityForResult(openGallery, 1000);
             }
         });

     }

     public void store_data() {
         final Intent intent = new Intent(Add_product.this, ProductDetail.class);

         final String store_title = product_name.getText().toString();
         final String store_desc = product_desc.getText().toString();
         final Integer store_price =parseInt(product_price.getText().toString());
         final String[] path = new String[1];
         if (!store_title.isEmpty() && store_price!=null) {


             try {


                 storageReference.child(store_title).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                     @Override
                     public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                         Log.w(TAG, "image Uploaded Successfully");
                         Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();

                         downloadUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                             @Override
                             public void onSuccess(Uri uri) {
                                 path[0] = uri.toString();

                                 Map<String, Object> imageObject = new HashMap<>();
                                 imageObject.put("story_image", path[0]);


                                 model.setImage(path[0]);

                                 Log.w(TAG, "URI=====>>>" + uri);
                                 Log.w(TAG, "Path=====>>>" + path[0]);

                                 intent.putExtra("image", path[0]);


                                 reference.updateChildren(imageObject);

                             }
                         }).addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                 Log.w(TAG, "Image linking failed");
                                 Toast.makeText(Add_product.this, "Error Uploading File", Toast.LENGTH_LONG).show();
                             }
                         });
                     }
                 });


                 model.setProduct_name(store_title);
                 model.setProduct_desc(store_desc);
                 model.setPrice(store_price);
                 model.setMeasure(measure);
                pushKey=reference.push().getKey();
                 Log.w(TAG, "Testing||referenceKey" + pushKey);

                 model.setKey(pushKey);
                 reference.child(pushKey).setValue(model);


                 Toast.makeText(Add_product.this, "Event Hosted Successfully", Toast.LENGTH_LONG).show();

                 intent.putExtra("product_Id", pushKey);

                 startActivity(intent);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }

     }






     @Override
     public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         switch (position) {
             case 0: {
                 measure = "Kg";
                 break;
             }
             case 1: {
                 measure = "Grams";
                 break;
             }

             default: {
                 measure = "Kg";
             }
         }
     }

     @Override
     public void onNothingSelected(AdapterView<?> parent) {

     }
 }