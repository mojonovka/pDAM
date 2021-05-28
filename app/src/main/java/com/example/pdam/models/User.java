package com.example.pdam.models;

public class User {

    private String uID;
    private String uName;
    private String uEmail;
    private String uTelefono;
    private String uFotoURI;

    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uName, String uEmail, String uTelefono, String uFotoURI) {
        this.uName = uName;
        this.uEmail = uEmail;
        this.uTelefono = uTelefono;
        this.uFotoURI = uFotoURI;
    }

    public User(String uID, String uName, String uEmail, String uTelefono, String uFotoURI) {
        this.uID = uID;
        this.uName = uName;
        this.uEmail = uEmail;
        this.uTelefono = uTelefono;
        this.uFotoURI = uFotoURI;
    }

    public User(User usuario){
        this.uID = usuario.uID;
        this.uName  = usuario.uName;
        this.uEmail = usuario.uEmail;
        if (usuario.uTelefono == null){
            this.uTelefono = "";
        } else {
            this.uTelefono = usuario.uTelefono;
        }
        if(usuario.uFotoURI == null){
            this.uFotoURI = "";
        } else {
            this.uFotoURI = usuario.uFotoURI;
        }
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

    public String getuTelefono() {
        return uTelefono;
    }

    public void setuTelefono(String uTelefono) {
        this.uTelefono = uTelefono;
    }

    public String getuFotoURI() {
        return uFotoURI;
    }

    public void setuTFotoURI(String uFotoURI) {
        this.uFotoURI = uFotoURI;
    }


}
