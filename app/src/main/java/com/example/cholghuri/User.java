package com.example.cholghuri;

public class User {
    private String userID;
    private String email;

    public User() {
    }

    public User(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public String getEmail() {
        return email;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}


