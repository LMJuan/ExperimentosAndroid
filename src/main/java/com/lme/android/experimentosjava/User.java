package com.lme.android.experimentosjava;


import android.graphics.Bitmap;

public class User {

    private String name;
    private String lastName;
    private String email;
    private Bitmap picture;

    public User(String name, String lastName, String email, Bitmap picture) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }
}
