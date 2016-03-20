package com.example.activity.safezoneparent;

/**
 * Created by Tien on 08-Jan-16.
 */
public class ParentAccount {
    String name;
    String username;
    String pass;
    String phone;
    String email;

    public ParentAccount(String name, String username, String pass, String phone, String email) {
        this.name = name;
        this.username = username;
        this.pass = pass;
        this.phone = phone;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
