package com.seher.shoppingregionlocator.helperClasses;

import java.io.Serializable;

public class User implements Serializable {
    private String fullname;
    private String email;
    private String phoneNo;
    private String profileImage;
    private String dob;
    private String id;
    private String password;




    public User() {
        fullname="";
        email="";
        phoneNo="";
    }

    public User(String fullname, String email, String phoneNo, String dob, String id, String password) {
        this.fullname = fullname;
        this.email = email;
        this.phoneNo = phoneNo;
        this.dob = dob;
        this.id = id;
        this.password = password;
    }

    public User(String fullname, String email, String phoneNo, String profileImage, String dob, String id, String password) {
        this.fullname = fullname;
        this.email = email;
        this.phoneNo = phoneNo;
        this.profileImage = profileImage;
        this.dob = dob;
        this.id = id;
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
