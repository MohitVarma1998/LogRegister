package com.example.logregsiterapp.model;

public class User {

    private int id;
    private String mName;
    private String mEmail;
    private String mPassword;
    private String mDOB;

    public User(String mName, String mEmail, String mPassword, String mDOB) {
        this.mName = mName;
        this.mEmail = mEmail;
        this.mPassword = mPassword;
        this.mDOB = mDOB;
    }

    public User(int id, String mName, String mEmail, String mPassword, String mDOB) {
        this.id = id;
        this.mName = mName;
        this.mEmail = mEmail;
        this.mPassword = mPassword;
        this.mDOB = mDOB;
    }

    public User(String mName, String mEmail, String mDOB) {
        this.mName = mName;
        this.mEmail = mEmail;
        this.mDOB = mDOB;
    }

    public String getmName() {
        return mName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public String getmPassword() {
        return mPassword;
    }

    public String getmDOB() {
        return mDOB;
    }
}
