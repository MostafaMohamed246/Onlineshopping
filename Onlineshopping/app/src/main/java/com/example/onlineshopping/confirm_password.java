package com.example.onlineshopping;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class confirm_password extends AppCompatActivity {

    EditText pass , repeatpass;
    Button confirm;
    MyDatabase database;
    String email;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_password);
        pass = (EditText) findViewById(R.id.editTextTextPersonName11);
        repeatpass = (EditText) findViewById(R.id.editTextTextPersonName13);
        confirm = (Button) findViewById(R.id.button11);

        database = new MyDatabase(this);
        confirm.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View v) {
                if(pass.getText().toString().trim().equals(repeatpass.getText().toString().trim())){
                    bundle = getIntent().getExtras();
                    email =  bundle.getString("Email");
                    database.changepass(pass.getText().toString().trim(),email);
                    Toast.makeText(confirm_password.this,"Change successful",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(confirm_password.this, login_activity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(confirm_password.this,"The Password and Repeat Password donot match ",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}