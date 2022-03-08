package com.example.onlineshopping;

public class Customer {
    private int CustID;
    private String cutName, Email, Password ,Gender ,Birthdate ,Job;
    public Customer(){

    }

    public Customer(String cutName, String email, String password, String gender, String birthdate, String job) {
        this.cutName = cutName;
        this.Email = email;
        this.Password = password;
        this.Gender = gender;
        this.Birthdate = birthdate;
        this.Job = job;
    }

    public int getCustID() {
        return CustID;
    }

    public void setCustID(int custID) {
        CustID = custID;
    }

    public String getCutName() {
        return cutName;
    }

    public void setCutName(String cutName) {
        this.cutName = cutName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getBirthdate() {
        return Birthdate;
    }

    public void setBirthdate(String birthdate) {
        Birthdate = birthdate;
    }

    public String getJob() {
        return Job;
    }

    public void setJob(String job) {
        Job = job;
    }
}
