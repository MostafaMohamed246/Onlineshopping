package com.example.onlineshopping;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Cart_Adapter extends BaseAdapter {
    private ArrayList<Products> data;
    private Context context;

    private double total_cost = 0;
    private ArrayList<Integer> list_id = new ArrayList<>();

    SharedPreferences sharedPreferences;

    public Cart_Adapter(Context context, ArrayList<Products> data) {
        this.data = data;
        this.context = context;

        getProductsids();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.shop_cart, parent, false);

        }

        // get current item to be displayed
        final Products currentItem = (Products) getItem(position);

        // get the TextView for item name and item description
        ImageView proimage = convertView.findViewById(R.id.imageView);
        TextView proname = convertView.findViewById(R.id.textView20);
        TextView proprice = convertView.findViewById(R.id.textView21);
        Button Remove = convertView.findViewById(R.id.button9);
        ImageView increase_quantity = convertView.findViewById(R.id.imageView14);
        ImageView decrase_quantity = convertView.findViewById(R.id.imageView12);
        final TextView quantity = convertView.findViewById(R.id.textView19);
        total_cost +=  currentItem.getPrice();
//String ItemName = tv.getText().toString();
        String qty = quantity.getText().toString();
        String t_cost = String.valueOf(total_cost);
        Intent intent = new Intent("custom-message");
        //   intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
        intent.putExtra("quantity",qty);
        intent.putExtra("total_cost",t_cost);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);



        Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data.remove(position);
                notifyDataSetChanged();

                list_id.remove(Double.valueOf(currentItem.getProID()));

                SharedPreferences.Editor editor;
                Gson gson = new Gson();
                String json = gson.toJson(list_id);
                editor = sharedPreferences.edit();
                editor.putString("lastorder", json);

                editor.apply();
                int quan = Integer.parseInt(quantity.getText().toString());
                double res_before = quan * currentItem.getPrice();
                total_cost = total_cost-res_before;
                String t_cost = String.valueOf(total_cost);
                Intent intent = new Intent("custom-message");
                intent.putExtra("total_cost",t_cost);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });

        increase_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quan = Integer.parseInt(quantity.getText().toString());

                if(quan<currentItem.getQuantity()) {


                    //double res_after = quan * currentItem.getPrice();
                    //total_cost += res_after;
                    total_cost +=  currentItem.getPrice();
                    quan++;
                    quantity.setText(String.valueOf(quan));
                    String t_cost = String.valueOf(total_cost);
                    String qty = quantity.getText().toString();
                    Intent intent = new Intent("custom-message");
                    //   intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                    intent.putExtra("quantity",qty);
                    intent.putExtra("total_cost",t_cost);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                }
            }
        });

        decrase_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quan = Integer.parseInt(quantity.getText().toString());
                //double res_before = quan * currentItem.getPrice();
                //total_cost = total_cost - res_before;


                if (quan > 1) {

                    quan--;


                    quantity.setText(String.valueOf(quan));

//                double res_after = quan * currentItem.getPrice();
                    total_cost -= currentItem.getPrice();
////String ItemName = tv.getText().toString();
                    String qty = quantity.getText().toString();
                    String t_cost = String.valueOf(total_cost);
                    Intent intent = new Intent("custom-message");
                    //   intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                    intent.putExtra("quantity", qty);
                    intent.putExtra("total_cost", t_cost);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }
        });


        if (currentItem.getProImage() != null) {
            byte[] image_byte = data.get(position).getProImage();
            Bitmap bmp = BitmapFactory.decodeByteArray(image_byte, 0, image_byte.length);
            proimage.setImageBitmap(bmp);
        }

        proname.setText(currentItem.getProName());
        proprice.setText(currentItem.getPrice() + " $");


        // returns the view for the current row
        return convertView;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }

    public double getTotal_cost() {
        return total_cost;
    }


    private void getProductsids() {
        sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        String ids = sharedPreferences.getString("lastorder", null);
        if (ids != null) {
            Gson gson = new Gson();
            list_id = gson.fromJson(ids, ArrayList.class);

        }
    }

}