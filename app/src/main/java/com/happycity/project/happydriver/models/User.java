package com.happycity.project.happydriver.models;

/**
 * Created by Ha Truong on 1/7/2018.
 * This is a App
 * into the com.happycity.project.happydriver.models
 */

public class User {
    private String email;
    private String password;
    private String phone;
    private String name;

    public User() {
    }

    public User(String email, String password, String phone, String name) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }
}
