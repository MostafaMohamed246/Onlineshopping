package com.example.onlineshopping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Recovery_Password extends AppCompatActivity {

    EditText email;
    Button get_password;

    MyDatabase database;
    List<String> c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recoveryyour_password);
        email = (EditText) findViewById(R.id.editTextTextPersonName5);

        get_password = (Button)findViewById(R.id.button5);
        database = new MyDatabase(this);
        c= database.getEm();
        get_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().trim().equals("")){
                    Toast.makeText(Recovery_Password.this,"Enter your email",Toast.LENGTH_SHORT).show();
                }
                else {
                    recoveryyourPassword();
                }
            }
        });

    }


    protected void recoveryyourPassword() {

        String Email = email.getText().toString().trim();
        for (int i = 0; i < c.size(); i++) {
            if (Email.equals(c.get(i))) {
                Intent intent = new Intent(Recovery_Password.this, confirm_password.class);
                intent.putExtra("Email",Email);
                startActivity(intent);
                finish();
            }
            /*else{
                Toast.makeText(Recovery_Password.this,"This email is failed not found",Toast.LENGTH_SHORT).show();
            }*/
        }
    }
}
