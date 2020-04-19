package com.happyhomes.technician;

public class User {
    public String fullname, address, dob,mobile,mail,pass,imagename,status,category;

    public User(){

    }

    public User(String fullname, String address, String dob, String mobile, String mail, String pass,String imagename,String status,String category) {
        this.fullname = fullname;
        this.address = address;
        this.dob = dob;
        this.mobile = mobile;
        this.mail = mail;
        this.pass = pass;
        this.imagename=imagename;
        this.status=status;
        this.category=category;
    }
}
