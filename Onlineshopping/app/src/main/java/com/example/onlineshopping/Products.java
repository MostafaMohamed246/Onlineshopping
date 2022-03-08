package com.example.onlineshopping;

public class Products {
    private int ProID,Quantity,CatID;
    private String ProName;
    private double Price;
    private byte[] proImage;
    private String Barcodes;

    public byte [] getProImage() {
        return proImage;
    }

    public void setProImage(byte [] proImage) {
        this.proImage = proImage;
    }

    public Products() {

    }

    public String getBarcodes() {
        return Barcodes;
    }

    public void setBarcodes(String barcodes) {
        Barcodes = barcodes;
    }

    public Products(int quantity, int catID, String proName, double price, byte[] proImage, String barcodes) {
        Quantity = quantity;
        CatID = catID;
        ProName = proName;
        Price = price;
        this.proImage = proImage;
        Barcodes = barcodes;
    }



    public int getProID() {
        return ProID;
    }

    public void setProID(int proID) {
        ProID = proID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getCatID() {
        return CatID;
    }

    public void setCatID(int catID) {
        CatID = catID;
    }

    public String getProName() {
        return ProName;
    }

    public void setProName(String proName) {
        ProName = proName;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }
}
