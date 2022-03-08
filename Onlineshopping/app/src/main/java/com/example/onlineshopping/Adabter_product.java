package com.example.onlineshopping;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class Adabter_product extends ArrayAdapter {
    private ArrayList<Products> data;
    private LinkedHashSet<Integer> selected_items=new LinkedHashSet<>();//to set my items

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public Adabter_product(@NonNull Context context, int resource, @NonNull ArrayList<Products> objects) {
        super(context, resource, objects);
        data=objects;
        sharedPreferences=getContext().getSharedPreferences("cart",Context.MODE_PRIVATE);

       // getSelectedProducts();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView==null)
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_products, parent, false);


            ImageView proimage = convertView.findViewById(R.id.imageView4);
            TextView proname = convertView.findViewById(R.id.textView17);
            TextView proprice = convertView.findViewById(R.id.textView18);
            Button add_to_cart = convertView.findViewById(R.id.button8);


            if (data.get(position).getProImage() != null) {
                byte[] image_byte = data.get(position).getProImage();
                Bitmap bmp = BitmapFactory.decodeByteArray(image_byte, 0, image_byte.length);
                proimage.setImageBitmap(bmp);
            }

            proname.setText(data.get(position).getProName());
            proprice.setText(data.get(position).getPrice() + " $");


            add_to_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected_items.add(data.get(position).getProID());
                    Gson gson = new Gson();//convert all list to string
                    String json = gson.toJson(selected_items);
                    editor = sharedPreferences.edit();
                    editor.putString("lastorder", json);
                    editor.apply();

                    Toast.makeText(getContext(), "product added", Toast.LENGTH_SHORT).show();
            }
        });


        return convertView;


    }
    public void filter(ArrayList<Products>filterlist){

        data.clear();
        data.addAll(filterlist);
        notifyDataSetChanged();

    }





    private void getSelectedProducts() {

        String ids = sharedPreferences.getString("lastorder", null);
        selected_items.clear();
        if (ids != null) {
            Gson gson = new Gson();
            selected_items = gson.fromJson(ids, LinkedHashSet.class);
        }
    }
}
