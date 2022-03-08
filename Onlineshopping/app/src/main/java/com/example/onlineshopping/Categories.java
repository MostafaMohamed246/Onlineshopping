package com.example.onlineshopping;

public class Categories {
    private int CatID;
    private String CatName;

    public Categories() {
    }

    public Categories( String catName) {
        CatName = catName;
    }

    public int getCatID() {
        return CatID;
    }

    public void setCatID(int catID) {
        CatID = catID;
    }

    public String getCatName() {
        return CatName;
    }

    public void setCatName(String catName) {
        CatName = catName;
    }
}
