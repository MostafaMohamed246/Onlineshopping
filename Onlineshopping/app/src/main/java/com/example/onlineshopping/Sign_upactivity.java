package com.example.onlineshopping;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class Sign_upactivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    TextView add_Birthdate;
    EditText first_name;
    EditText Jop_title;
    EditText add_email;
    EditText add_password;
    Button OpenDatePicker;
    Button create_btn;
    RadioGroup radioGroup;
    RadioButton radioButton;
    MyDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_upactivity);
        first_name = (EditText)findViewById(R.id.editTextTextPersonName2);
        Jop_title = (EditText)findViewById(R.id.editTextTextPersonName3);
        add_email = (EditText)findViewById(R.id.editTextTextPersonName6);
        add_password = (EditText)findViewById(R.id.editTextTextPassword3);
        add_Birthdate = (TextView)findViewById(R.id.textView15);
        OpenDatePicker = (Button) findViewById(R.id.button6);
        create_btn = (Button) findViewById(R.id.button4);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        int count =0;
        database=new MyDatabase(this);
        List<String> c = database.getEm();
        OpenDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment datapicker = new Datapicker();
                datapicker.show(getSupportFragmentManager(),"data picker");
            }
        });


        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int k=0;
                if(!(add_email.getText().toString().contains("@") && add_email.getText().toString().contains("."))){
                    Toast.makeText(getApplicationContext(), "! Please Enter valid email, e.g:info@yahoo.com", Toast.LENGTH_SHORT).show();
                    k++;
                }
                else {

                    for(int i = 0 ; i <c.size();i++){
                        if(add_email.getText().toString().trim().equals(c.get(i))){
                            Toast.makeText(getApplicationContext(), "! This email is used, Please write the another one", Toast.LENGTH_SHORT).show();
                            k++;
                        }
                    }
                }
                if(k<=0){
                    signUp();
                }
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c =Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDatestring = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
         add_Birthdate.setText(currentDatestring);
    }
    protected void signUp() {
        String Name = first_name.getText().toString().trim();
        String Email = add_email.getText().toString().trim();
        String password = add_password.getText().toString().trim();
        int radioid=radioGroup.getCheckedRadioButtonId();
        radioButton=findViewById(radioid);
        String gender = radioButton.getText().toString();
        String birthdate = add_Birthdate.getText().toString();
        String joptitle=Jop_title.getText().toString();

        if (first_name.getText().toString().equals("") || Jop_title.getText().toString().equals("") || add_email.getText().toString().equals("") || add_password.getText().toString().equals("") || add_Birthdate.getText().toString().equals("Date"))
            Toast.makeText(this, "Some fields not entered", Toast.LENGTH_SHORT).show();
        else {
            Customer customer=new Customer(Name,Email,password,gender,birthdate,joptitle);
            database.insertCustomer(customer);
            Toast.makeText(this, "Successfully registered ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Sign_upactivity.this, login_activity.class);
            startActivity(intent);
            finish();
        }


    }
}