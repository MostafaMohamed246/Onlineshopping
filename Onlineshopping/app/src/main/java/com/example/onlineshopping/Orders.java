package com.example.onlineshopping;

public class Orders {
    private int OrdID,CustID;
    private String Address;
    private String OrdDate;

    public Orders() {
    }

    public Orders( int custID, String address, String ordDate) {

        CustID = custID;
        Address = address;
        OrdDate = ordDate;
    }

    public int getOrdID() {
        return OrdID;
    }

    public void setOrdID(int ordID) {
        OrdID = ordID;
    }

    public int getCustID() {
        return CustID;
    }

    public void setCustID(int custID) {
        CustID = custID;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getOrdDate() {
        return OrdDate;
    }

    public void setOrdDate(String ordDate) {
        OrdDate = ordDate;
    }
}
