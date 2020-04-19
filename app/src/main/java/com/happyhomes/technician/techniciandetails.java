package com.happyhomes.technician;

public class techniciandetails {
    private String fullname;
    String address;
    String status;
    String mobile;
    String mail;
    String pass;
    String category;
    String dob;

    public techniciandetails()
    {

    }

    public techniciandetails(String fullname, String address, String status, String mobile, String mail, String pass,  String category,String dob) {
        this.fullname = fullname;
        this.address = address;
        this.status = status;
        this.mobile = mobile;
        this.mail = mail;
        this.pass = pass;
        this.category = category;
        this.dob=dob;
    }

    public String getFullname() {
        return fullname;
    }

    public String getAddress() {
        return address;
    }

    public String getStatus() {
        return status;
    }

    public String getMobile() {
        return mobile;
    }

    public String getMail() {
        return mail;
    }

    public String getPass() {
        return pass;
    }

    public String getCategory() {
        return category;
    }

    public String getDob() {
        return dob;
    }
}
