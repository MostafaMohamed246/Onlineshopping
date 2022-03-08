package com.example.onlineshopping;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyDatabase extends SQLiteOpenHelper{
    final static String dataName = "Mydatabase";
    SQLiteDatabase database;
    public MyDatabase(@Nullable Context context) {
        super(context, dataName, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table  customer (CustID integer primary key  autoincrement , CutName text not null, Username text not null unique," +
                "Password text not null, Gender text not null, Birthdate text , Jop text )");

        db.execSQL("create table Category (CatID integer primary key  autoincrement , CatName text not null )");

        db.execSQL("create table product (ProID integer primary key autoincrement, ProName text not null ,Proimage blob," +
                "price real not null , Quantity integer not null ,Barcodes text not null, CatID integer not null ," +
                "foreign key (CatID)references category (CatID))");

        db.execSQL("create table orders (OrdID integer primary key  autoincrement , OrdDate text ,Address text not null,CustID integer not null ," +
                "foreign key(CustID)references customer (CustID))");

        db.execSQL("create table order_details (OrdID integer , ProID integer ,Quantity integer not null ,PRIMARY KEY (OrdID, ProID)," +
                "foreign key (OrdID) references orders(OrdID),foreign key (ProID) references product(ProID))");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists customer");
        db.execSQL("drop table if exists Category");
        db.execSQL("drop table if exists product");
        db.execSQL("drop table if exists orders");
        db.execSQL("drop table if exists order_details");
        onCreate(db);

    }

    public void insertCustomer(Customer cust) {
        database = getWritableDatabase();
        ContentValues Raws = new ContentValues();
        Raws.put("CutName", cust.getCutName());
        Raws.put("Username", cust.getEmail());
        Raws.put("Password", cust.getPassword());
        Raws.put("Gender", cust.getGender());
        Raws.put("Birthdate", cust.getBirthdate());
        Raws.put("Jop", cust.getJob());
        database.insert("customer", null, Raws);
        database.close();

    }



    public Cursor userLogin(String username, String password) {
        database = getReadableDatabase();
        String[] args = {username, password};
        Cursor cursor = database.rawQuery("select CustID from customer where Username =? and Password =? ", args);

        if (cursor != null)
            cursor.moveToFirst();

        database.close();
        return cursor;

    }

    public List<String> getEm() {


        database = this.getReadableDatabase();
        List<String> stringList = new ArrayList<String>();
        Cursor cursor = database.rawQuery("SELECT Username FROM customer", null);
        if(cursor.moveToFirst()){
            do{
                stringList.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }

        return stringList;

    }

    public void insertProduct(Products product){
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("ProName",product.getProName());
        values.put("price",product.getPrice());
        values.put("Quantity",product.getQuantity());
        values.put("Barcodes",product.getBarcodes());
        values.put("CatID",product.getCatID());
        database.insert("product",null,values);
        database.close();
    }
    public Cursor getProducts(){
        database=getReadableDatabase();
        String[]fields={"ProID","ProName","Proimage ","price","Quantity","Barcodes","CatID"};
        Cursor cursor= database.query("product",fields,null,null,null,null,null);
        if (cursor!=null)
            cursor.moveToFirst();
        return cursor;
    }
    public void insertCategory(Categories categ){
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("CatName",categ.getCatName());
        database.insert("Category",null,values);
        database.close();
    }
    public void changepass(String pass,String email){
        database=getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("Password",pass);
        String[] args = {email};
        database.update("customer", values, "Username = ?", args);


    }
    public Cursor getCategory(){
        database=getReadableDatabase();
        String []fields={"CatID","CatName"};
        Cursor cursor= database.query("Category",fields,null,null,null,null,null);

        if (cursor.getCount()>0)
            cursor.moveToFirst();

        database.close();

        return cursor;
    }

    public Cursor getProduct_by_Category(String categ){
        database=getReadableDatabase();
        String []args={categ};
        Cursor cursor=  database.rawQuery("select * from product where CatID =? ",args);
        if (cursor!=null)
            cursor.moveToFirst();

        return cursor;

    }
    public Cursor getProductbyId(String id){
        database=getReadableDatabase();
        String []args={id};
        Cursor cursor=  database.rawQuery("select * from product where ProID =? ",args);
        if (cursor!=null)
            cursor.moveToFirst();
        return cursor;
    }
    public String getCatId(String name){
        database=getReadableDatabase();
        String[]args={name};
        Cursor cursor=database.rawQuery("select CatID from Category where CatName =?",args);

        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            database.close();
            return cursor.getString(0);
        }
        database.close();
        cursor.close();
        return null;

    }
    public void submetOrder (Orders orders){
        database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Address",orders.getAddress());
        contentValues.put("OrdDate",orders.getOrdDate());
        contentValues.put("CustID",orders.getCustID());

        database.insert("orders",null,contentValues);
    }

    public void insertOrderDetails (OrderDetails orderDetails){
        database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("OrdID",orderDetails.getOrdID());
        contentValues.put("ProID",orderDetails.getProID());
        contentValues.put("Quantity",orderDetails.getQuantity());

        database.insert("order_details",null,contentValues);
    }
    public int getcustIDbyemail(String username){
        database=getReadableDatabase();
        String []args={username};
        Cursor cursor=  database.rawQuery("select CustID from customer where Username =?",args);
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            database.close();
            return cursor.getInt(0);
        }
        database.close();
        cursor.close();
        return 0;
    }
  /*  public int getorderIDbyCustidandorddate(String cust_id ,String orderdate){
        database=getReadableDatabase();
        String []args={cust_id,orderdate};
        Cursor cursor=  database.rawQuery("select OrdID from orders where CustID =? and OrdDate =?",args);
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            database.close();
            return cursor.getInt(0);
        }
        database.close();
        cursor.close();
        return 0;
    }*/
  public List<Integer>  getorderID() {


      database = this.getReadableDatabase();
      List<Integer> IntegerList = new ArrayList<Integer>();
      Cursor cursor = database.rawQuery("select OrdID from orders", null);
      if(cursor.moveToFirst()){
          do{
              IntegerList.add(cursor.getInt(0));
          }while(cursor.moveToNext());
      }

      return IntegerList;

  }
}
