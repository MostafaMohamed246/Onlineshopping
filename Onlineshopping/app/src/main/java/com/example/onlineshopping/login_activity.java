package com.example.onlineshopping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class login_activity extends AppCompatActivity {

    TextView create_acount;
    TextView forget_password;
    EditText add_username;
    EditText add_password;
    Button continue_btn;
    CheckBox Remember_me;
    MyDatabase database;
    SharedPreferences sharedPreferences;

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PASSWORD = "password";

    boolean login ; //tell me i am login or not
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        create_acount =(TextView) findViewById(R.id.textView6);
        forget_password = (TextView) findViewById(R.id.textView10);
        add_username = (EditText)findViewById(R.id.editTextTextPersonName);
        add_password = (EditText)findViewById(R.id.editTextTextPassword);
        continue_btn = (Button)findViewById(R.id.button3);
        Remember_me = (CheckBox) findViewById(R.id.Remember_me);
        database = new MyDatabase(this);
        sharedPreferences=getSharedPreferences("remember file",MODE_PRIVATE);

        checkLogin();

        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login_activity.this,Recovery_Password.class);
                startActivity(i);
            }
        });

        create_acount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login_activity.this,Sign_upactivity.class);
                startActivity(i);
            }
        });

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(add_username.getText().toString().equals("") && add_password.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter your email and password", Toast.LENGTH_SHORT).show();
                }
                else if(add_username.getText().toString().equals("") ) {
                    Toast.makeText(getApplicationContext(), "Enter your email", Toast.LENGTH_SHORT).show();
                }
                else if(add_password.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Enter your password", Toast.LENGTH_SHORT).show();

                }
                else if(add_username.getText().toString().equals("admin")){
                    login();
                }
                else if(!(add_username.getText().toString().contains("@") && add_username.getText().toString().contains("."))){
                    Toast.makeText(getApplicationContext(), "! Please Enter valid email, e.g:info@yahoo.com", Toast.LENGTH_SHORT).show();
                }
                else{
                    login();
                }

            }
        });


    }


    protected void login() {

        String name = add_username.getText().toString().trim();
        String Password = add_password.getText().toString();
        Cursor cursor = database.userLogin(name, Password);

        if (name.equals("admin") && Password.equals("admin")) {
            Intent intent = new Intent(login_activity.this, UploadProduct.class);
            startActivity(intent);
            finish();
        } else {


            if (cursor.getCount() <= 0)
                Toast.makeText(this, "Please Check username and password", Toast.LENGTH_SHORT).show();

            else {

                if (Remember_me.isChecked()) {

                    getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                            .edit()
                            .putString(PREF_USERNAME, (add_username).getText().toString())
                            .putString(PREF_PASSWORD, (add_password).getText().toString())
                            .commit();

                }
                int id = database.getcustIDbyemail(name);
                Toast.makeText(this, "Successfully login", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(login_activity.this, HomeActivity.class);
                intent.putExtra("customer_id",id);
                startActivity(intent);
                finish();


            }
        }
    }
    protected void checkLogin(){

            String username = sharedPreferences.getString(PREF_USERNAME, null);
            String password = sharedPreferences.getString(PREF_PASSWORD, null);

            if (username != null || password != null) {
                //Prompt for username and password
                Intent intent=new Intent(login_activity.this,Sign_upactivity.class);
                startActivity(intent);
                finish();
            }
        }
    }


