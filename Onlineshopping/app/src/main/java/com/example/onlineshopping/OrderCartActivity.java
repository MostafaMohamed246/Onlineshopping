package com.example.onlineshopping;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrderCartActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener  {
    private ListView cart_products;
    private Cart_Adapter adapter;
    private Button map_btn;
    private ArrayList<Products> data = new ArrayList<>();
    private ArrayList<Integer> pro_id = new ArrayList<>();

    private MyDatabase database;
    private SharedPreferences sharedPreferences;
    double cost = 0;
    String loc;
    Bundle bundle;
    EditText location;
    Button OpenDatePicker,submet,confirm;
    EditText orderdate;
   int c_id;
    String qty;
    String totalcost;
    TextView total_cost;
    List<Integer> orderid;
    int cust_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_cart);
        map_btn = (Button) findViewById(R.id.button10);
        database = new MyDatabase(this);
        location = findViewById(R.id.editTextTextPersonName14);
        cart_products = findViewById(R.id.list);
        orderdate = findViewById(R.id.editTextTextPersonName15);
        total_cost = findViewById(R.id.total_cost);
        submet = findViewById(R.id.button13);
        confirm= findViewById(R.id.button14);
        bundle = getIntent().getExtras();
        cust_id =  bundle.getInt("cust_id");
        int id=cust_id;

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

        OpenDatePicker = (Button) findViewById(R.id.button15);
        OpenDatePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DialogFragment datapicker = new Datapicker();
                datapicker.show(getSupportFragmentManager(),"data picker");
            }

        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle = getIntent().getExtras();
                loc =  bundle.getString("Address");
                c_id=bundle.getInt("c_id");
                location.setText(String.valueOf(loc));
            }
        });

        submet.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

              String address = location.getText().toString().trim();
              String Orderdate = orderdate.getText().toString().trim();



              if(!address.equals("")&&!Orderdate.equals("")) {
                    Orders orders = new Orders(c_id,address,Orderdate);

                  database.submetOrder(orders);
                  //int ord_id = database.getorderIDbyCustidandorddate(String.valueOf(c_id),Orderdate);
                  orderid = database.getorderID();
                  int ord_id=orderid.get(orderid.size()-1);
                  for(int  i = 0;i<pro_id.size();i++){
                      OrderDetails orderDetails = new OrderDetails(ord_id,String.valueOf(pro_id.get(i)),Integer.parseInt(qty));
                      database.insertOrderDetails (orderDetails);


                  }



                    location.setText("Your location");
                    orderdate.setText("Data");
                    total_cost.setText("price");
                    cart_products.setAdapter(null);
                    Toast.makeText(getApplicationContext(), "Submit Successfully", Toast.LENGTH_SHORT).show();
                   //sendemail();
              }
              else {
                    Toast.makeText(getApplicationContext(), "Check data again", Toast.LENGTH_SHORT).show();
              }
            }
        });
        getProductsids();


        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(OrderCartActivity.this, MapsActivity.class);
                i.putExtra("customerid",id);
                startActivity(i);

            }
        });




    }


    private void getProductsids() {
        sharedPreferences = this.getSharedPreferences("cart", Context.MODE_PRIVATE);
        String ids = sharedPreferences.getString("lastorder", null);
        if (ids != null) {
            Gson gson = new Gson();
            ArrayList id = gson.fromJson(ids, ArrayList.class);
            pro_id = gson.fromJson(ids, ArrayList.class);
            getCartProduct(id);


            adapter = new Cart_Adapter(this, data);
            cart_products.setAdapter(adapter);
           /* adapter.setTotal_cost(cost);




          //  String.valueOf(adapter.getTotal_cost()) + " $"
            total_cost.setText(String.valueOf(adapter.getTotal_cost()) + " $");*/
        }
    }

    private void getCartProduct(ArrayList<Integer> ids) {

        data.clear();
        for (int i = 0; i < ids.size(); i++) {
            Cursor cursor = database.getProductbyId(String.valueOf(ids.get(i)));
            if (cursor != null) {
                Products product = new Products(Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(6)),
                        cursor.getString(1),
                        Double.parseDouble(cursor.getString(3)), cursor.getBlob(2),cursor.getString(5));
                product.setProID(Integer.parseInt(cursor.getString(0)));
                data.add(product);
                cost += Double.parseDouble(cursor.getString(3));
            }
        }


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c =Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDatestring = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        orderdate.setText(currentDatestring);
    }
    public void sendemail(){
        try {
            String[] to = {"YourEmail@gmail.com"};
            Intent SendEmail = new Intent(Intent.ACTION_SEND);
            SendEmail.putExtra(Intent.EXTRA_EMAIL, to);
            SendEmail.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            SendEmail.putExtra(Intent.EXTRA_TEXT, "successful order:");
            SendEmail.setType("message/rfc822");
            Intent.createChooser(SendEmail, "Send Email");
            startActivity(SendEmail);

        }catch (Exception e){
            Toast.makeText(this, "عفوا لا يوجد تطبيق مراسلة في جهازك", Toast.LENGTH_SHORT).show();

        }
    }
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

             qty = intent.getStringExtra("quantity");
             totalcost=intent.getStringExtra("total_cost");

            //  String.valueOf(adapter.getTotal_cost()) + " $"
            total_cost.setText(totalcost+ " $");
          // Toast.makeText(OrderCartActivity.this,totalcost ,Toast.LENGTH_SHORT).show();
        }
    };
}