package com.example.onlineshopping;

public class OrderDetails {
    private int orderdetails, OrdID,Quantity;
    private double ProID;
    public OrderDetails() {
    }



    public OrderDetails(int ordID, String proID, int quantity) {
        OrdID = ordID;
        ProID =Double.parseDouble( proID);
        Quantity = quantity;
    }

    public int getOrderdetails() {
        return orderdetails;
    }

    public void setOrderdetails(int orderdetails) {
        this.orderdetails = orderdetails;
    }

    public int getOrdID() {
        return OrdID;
    }

    public void setOrdID(int ordID) {
        OrdID = ordID;
    }

    public double getProID() {
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
}
