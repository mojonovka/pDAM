package com.example.pdam.models;

public class User {

    private String uID;
    private String uName;
    private String uEmail;

    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uName, String uEmail) {
        this.uName = uName;
        this.uEmail = uEmail;
    }

    public User(String uID, String uName, String uEmail) {
        this.uID = uID;
        this.uName = uName;
        this.uEmail = uEmail;
    }

    public User(User usuario){
        this.uID = usuario.uID;
        this.uName  = usuario.uName;
        this.uEmail = usuario.uEmail;
    }


    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

}
