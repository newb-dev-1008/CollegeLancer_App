package com.college.freelancestartup;

import java.util.Date;

public class User {
    public String name, email, phoneNo;
    public Date birthDate;

    public User(){

    }

    public User(String name, String email, String phoneNo) {
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
    }
}
