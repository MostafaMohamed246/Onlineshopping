package com.example.onlineshopping;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UploadProduct extends AppCompatActivity {
    EditText Product_Name,Product_Price,Product_Quantity,Barcode;
    ImageView proimage;
    Spinner Category;
    ArrayAdapter adapter;
    Button upload;
    MyDatabase database;
    final static int GALLERY_REQUEST_CODE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);
        Product_Name=findViewById(R.id.editTextTextPersonName4);
        Product_Price=findViewById(R.id.editTextTextPersonName8);
        Product_Quantity=findViewById(R.id.editTextTextPersonName7);
        Category =findViewById(R.id.category);
        proimage=findViewById(R.id.imageView9);
        Barcode=findViewById(R.id.editTextTextPersonName12);
        upload=findViewById(R.id.button7);
        database=new MyDatabase(this);

        //addCategory();
        getAllcategory();

        proimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadProduct();

            }
        });

    }
    protected void addCategory(){
        database.insertCategory(new Categories("Clothes"));
        database.insertCategory(new Categories("Cars"));
        database.insertCategory(new Categories("Sports"));
        database.insertCategory(new Categories("Electrical devices"));


    }

    protected void chooseImage() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                proimage.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    protected static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
    protected void getAllcategory(){

        List<String> catego=new ArrayList<>();
        Cursor cursor=database.getCategory();
        if (cursor!=null){
            while (!cursor.isAfterLast()){
                catego.add(cursor.getString(1));
                cursor.moveToNext();
            }
            adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,catego);
            Category.setAdapter(adapter);

        }
    }
    protected void uploadProduct(){

        String proname=Product_Name.getText().toString().trim();
        String proprice=Product_Price.getText().toString().trim();
        String proquantity=Product_Quantity.getText().toString().trim();
        String Barcodes = Barcode.getText().toString().trim();
        int categoryid=Integer.parseInt(database.getCatId(Category.getSelectedItem().toString()));
        byte [] image=imageViewToByte(proimage);

        if(!proname.equals("")||!proprice.equals("")||!proquantity.equals(""))
        {
            Products product = new Products(Integer.parseInt(proquantity), categoryid,proname,Double.parseDouble(proprice),image,Barcodes);
            database.insertProduct(product);


            proimage.setImageResource(android.R.drawable.ic_menu_gallery);
            Product_Name.setText("");
            Product_Price.setText("");
            Product_Quantity.setText("");
            Barcode.setText("");


            Toast.makeText(this, "product added", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Check data again", Toast.LENGTH_SHORT).show();
        }
    }
}