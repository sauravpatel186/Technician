package com.happyhomes.technician;

public class feedback {
    String pid;
    String tech_id;
    String pname;

    public String getPdesc() {
        return pdesc;
    }

    public void setPdesc(String pdesc) {
        this.pdesc = pdesc;
    }

    String pdesc;
    String tech_name;
    String uid;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    String rating;
    String comment;
    String c_name;
public feedback(){

}
    @Override
    public String toString() {
        return "feedback{" +
                "pid='" + pid + '\'' +
                ", tech_id='" + tech_id + '\'' +
                ", pname='" + pname + '\'' +
                ", pdesc='" + pdesc + '\'' +
                ", tech_name='" + tech_name + '\'' +
                ", uid='" + uid + '\'' +
                ", rating='" + rating + '\'' +
                ", comment='" + comment + '\'' +
                ", c_name='" + c_name + '\'' +
                '}';
    }



    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTech_id() {
        return tech_id;
    }

    public void setTech_id(String tech_id) {
        this.tech_id = tech_id;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }


    public String getTech_name() {
        return tech_name;
    }

    public void setTech_name(String tech_name) {
        this.tech_name = tech_name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }


}
