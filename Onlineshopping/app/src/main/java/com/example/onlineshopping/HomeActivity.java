package com.example.onlineshopping;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Products> products = new ArrayList<>();
    private Adabter_product adabter;
    private MyDatabase database;
    private EditText search;
    private Spinner category;
    ArrayAdapter adapter_category;
    private Toolbar toolbar;
    int voiceCode = 1;
    TextView reset;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int cust_id;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listView = findViewById(R.id.mylist);
        search=findViewById(R.id.editTextTextPersonName9);
        category=findViewById(R.id.spinner);
        reset=findViewById(R.id.textView22);
        bundle = getIntent().getExtras();
        cust_id =  bundle.getInt("customer_id");
        sharedPreferences=getSharedPreferences("remember file",MODE_PRIVATE);
        database = new MyDatabase(this);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        adabter = new Adabter_product(this, 0, products);


        getAllcategory();
        getAllProduct();

        if (adabter==null) {
            adabter = new Adabter_product(this, 0,products);
            listView.setAdapter(adabter);
        }

reset.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        search.setText("");
        category.setSelection(0);
    }
});
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(category.getSelectedItem().toString().equals("All")) {

                    getAllProduct();
                    adabter=new Adabter_product(HomeActivity.this,0,products);
                    listView.setAdapter(adabter);
                }
                else

                    search_By_Category(category.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(""))
                    filter(s.toString());

                else
                {
                    getAllProduct();
                    adabter=new Adabter_product(HomeActivity.this,0,products);
                    listView.setAdapter(adabter);
                }


            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.logout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout) {
            editor = sharedPreferences.edit();
            editor.putString("username", null);
            editor.putString("password", null);
            editor.putBoolean("login", false);
            editor.apply();

            Intent intent = new Intent(HomeActivity.this, login_activity.class);
            startActivity(intent);
            finish();


            return true;
        }
        if (item.getItemId() ==R.id.cart){

                Intent i = new Intent(HomeActivity.this,OrderCartActivity.class);
                i.putExtra("cust_id",cust_id);
                startActivity(i);
                finish();
            return true;
        }
        if(item.getItemId()==R.id.barcode){

                scanCode();
            return true;
        }
        if(item.getItemId()==R.id.voice){

            Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            startActivityForResult(i,voiceCode);
            return true;
        }
        return super.onOptionsItemSelected(item);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == voiceCode && resultCode == HomeActivity.RESULT_OK) {
            ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            search.setText(text.get(0));
            listView.setAdapter(adabter);
            filter(text.get(0));
        }
        else{
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
            if(result!=null){
                if(result.getContents()!=null ){
                    search.setText(result.getContents());
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(result.getContents());
                    builder.setTitle("Scanning Result");
                    builder.setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            scanCode();
                        }
                    }).setNegativeButton("finish", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else{
                    Toast.makeText(this,"No Results",Toast.LENGTH_LONG).show();
                }
            }else{
                super.onActivityResult(requestCode,resultCode,data);
            }
        }
    }

    private void getAllProduct() {
        Cursor cursor = database.getProducts();
        products.clear();
        if (cursor != null) {
            while (!cursor.isAfterLast()) {
                Products product=new Products(Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(6)),
                        cursor.getString(1),
                        Double.parseDouble(cursor.getString(3)),cursor.getBlob(2),cursor.getString(5));
                product.setProID(Integer.parseInt(cursor.getString(0)));
                products.add(product);
                cursor.moveToNext();
            }
        }
    }
    private void getAllcategory(){

        List<String> cat=new ArrayList<>();
        cat.add("All");
        Cursor cursor=database.getCategory();
        if (cursor!=null){
            while (!cursor.isAfterLast()){
                cat.add(cursor.getString(1));
                cursor.moveToNext();
            }
            adapter_category=new ArrayAdapter(HomeActivity.this,android.R.layout.simple_list_item_1,cat);
            category.setAdapter(adapter_category);
        }
    }
    private void search_By_Category(String name) {

        ArrayList<Products>filterlist=new ArrayList<>();


        String cat_id = database.getCatId(name);
        Cursor cursor = database.getProduct_by_Category(cat_id);
        if (cursor != null) {
            while (!cursor.isAfterLast()) {

                Products productModel=new Products(Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(6)),
                        cursor.getString(1),
                        Double.parseDouble(cursor.getString(3)), cursor.getBlob(2),cursor.getString(5));
                productModel.setProID(Integer.parseInt(cursor.getString(0)));
                filterlist.add(productModel);

                cursor.moveToNext();
            }


            adabter.filter(filterlist);

            if (filterlist.size()==0)
                Toast.makeText(HomeActivity.this, "No products to show", Toast.LENGTH_SHORT).show();
        }



    }
    private void filter(String text) {
        ArrayList<Products> filteredList = new ArrayList<>();

        for (Products item : products) {
            if (item.getProName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
            else if(item.getBarcodes().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }

        adabter.filter(filteredList);
    }

    private void scanCode(){
        IntentIntegrator integrator=new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }
}