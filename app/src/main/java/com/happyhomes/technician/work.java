package com.happyhomes.technician;

public class work {
    String fname;
    String pname;
    String pdesc;
    String address;
    String category;
    String status;
    String mobile;
    String id;
    String Date_Time;
    String pid;
    public work() {
    }
    @Override
    public String toString() {
        return "work{" +
                "fname='" + fname + '\'' +
                ", pname='" + pname + '\'' +
                ", pdesc='" + pdesc + '\'' +
                ", address='" + address + '\'' +
                ", category='" + category + '\'' +
                ", status='" + status + '\'' +
                ", mobile='" + mobile + '\'' +
                ", id='" + id + '\'' +
                ", Date_Time='" + Date_Time + '\'' +
                ", pid='" + pid + '\'' +
                '}';
    }



    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }


    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPdesc() {
        return pdesc;
    }

    public void setPdesc(String pdesc) {
        this.pdesc = pdesc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDate_Time() {
        return Date_Time;
    }

    public void setDate_Time(String date_Time) {
        Date_Time = date_Time;
    }


}
