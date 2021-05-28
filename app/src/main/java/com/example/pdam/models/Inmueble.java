package com.example.pdam.models;

import com.google.android.gms.maps.model.LatLng;

public class Inmueble {

    private String inmbID;
    private String inmbPropID;
    private String inmbNombreDesc;
    private String inmbDescCompleta;
    private String inmbTipo;
    private String inmbPeriodo;
    private String inmbPrecio;
    private String inmbProvincia;
    private String inmbMunicipio;
    private String inmbDireccion;
    private String inmbCP;
    private double inmbGEOLat;
    private double inmbGEOLng;
    private String inmbFotoURI;
    private Boolean inmbDisp;
    private long inmbTimeStamp;

    public Inmueble (){
        // Default constructor required for calls to DataSnapshot.getValue(Inmueble.class)
    }

    public Inmueble(
            String inmbID,
            String inmbPropID,
            String inmbNombreDesc,
            String inmbDescCompleta,
            String inmbTipo,
            String inmbPeriodo,
            String inmbPrecio,
            String inmbProvincia,
            String inmbMunicipio,
            String inmbDireccion,
            String inmbCP,
            double inmbGEOLat,
            double inmbGEOLng,
            String inmbFotoURI,
            Boolean inmbDisp,
            long inmbTimeStamp) {

        this.inmbID = inmbID;
        this.inmbPropID = inmbPropID;
        this.inmbNombreDesc = inmbNombreDesc;
        this.inmbDescCompleta = inmbDescCompleta;
        this.inmbTipo = inmbTipo;
        this.inmbPeriodo = inmbPeriodo;
        this.inmbPrecio = inmbPrecio;
        this.inmbProvincia = inmbProvincia;
        this.inmbMunicipio = inmbMunicipio;
        this.inmbDireccion = inmbDireccion;
        this.inmbCP = inmbCP;
        this.inmbGEOLat = inmbGEOLat;
        this.inmbGEOLng = inmbGEOLng;
        this.inmbFotoURI = inmbFotoURI;
        this.inmbDisp = inmbDisp;
        this.inmbTimeStamp = inmbTimeStamp;
    }

    public Inmueble (Inmueble inmb){

        this.inmbID = inmb.inmbID;
        this.inmbPropID = inmb.inmbPropID;
        this.inmbNombreDesc = inmb.inmbNombreDesc;
        this.inmbDescCompleta = inmb.inmbDescCompleta;
        this.inmbTipo = inmb.inmbTipo;
        this.inmbPeriodo = inmb.inmbPeriodo;
        this.inmbPrecio = inmb.inmbPrecio;
        this.inmbProvincia = inmb.inmbProvincia;
        this.inmbMunicipio = inmb.inmbMunicipio;
        this.inmbDireccion = inmb.inmbDireccion;
        this.inmbCP = inmb.inmbCP;
        this.inmbGEOLat = inmb.inmbGEOLat;
        this.inmbGEOLng = inmb.inmbGEOLng;
        this.inmbFotoURI = inmb.inmbFotoURI;
        this.inmbDisp = inmb.inmbDisp;
        this.inmbTimeStamp = inmb.inmbTimeStamp;

    }

    public String getInmbID() {
        return inmbID;
    }

    public void setInmbID(String inmbID) {
        this.inmbID = inmbID;
    }

    public String getInmbPropID() {
        return inmbPropID;
    }

    public void setInmbPropID(String inmbPropID) {
        this.inmbPropID = inmbPropID;
    }

    public String getInmbNombreDesc() {
        return inmbNombreDesc;
    }

    public void setInmbNombreDesc(String inmbNombreDesc) {
        this.inmbNombreDesc = inmbNombreDesc;
    }

    public String getInmbDescCompleta() {
        return inmbDescCompleta;
    }

    public void setInmbDescCompleta(String inmbDescCompleta) {
        this.inmbDescCompleta = inmbDescCompleta;
    }

    public String getInmbTipo() {
        return inmbTipo;
    }

    public void setInmbTipo(String inmbTipo) {
        this.inmbTipo = inmbTipo;
    }

    public String getInmbPeriodo() {
        return inmbPeriodo;
    }

    public void setInmbPeriodo(String inmbPeriodo) {
        this.inmbPeriodo = inmbPeriodo;
    }

    public String getInmbPrecio() {
        return inmbPrecio;
    }

    public void setInmbPrecio(String inmbPrecio) {
        this.inmbPrecio = inmbPrecio;
    }

    public String getInmbProvincia() {
        return inmbProvincia;
    }

    public void setInmbProvincia(String inmbProvincia) {
        this.inmbProvincia = inmbProvincia;
    }

    public String getInmbMunicipio() {
        return inmbMunicipio;
    }

    public void setInmbMunicipio(String inmbMunicipio) {
        this.inmbMunicipio = inmbMunicipio;
    }

    public String getInmbDireccion() {
        return inmbDireccion;
    }

    public void setInmbDireccion(String inmbDireccion) {
        this.inmbDireccion = inmbDireccion;
    }

    public String getInmbCP() {
        return inmbCP;
    }

    public void setInmbCP(String inmbCP) {
        this.inmbCP = inmbCP;
    }

    public double getInmbGEOLat() {
        return inmbGEOLat;
    }

    public void setInmbGEOLat(double inmbGEOLat) {
        this.inmbGEOLat = inmbGEOLat;
    }

    public double getInmbGEOLng() {
        return inmbGEOLng;
    }

    public void setInmbGEOLng(double inmbGEOLng) {
        this.inmbGEOLng = inmbGEOLng;
    }

    public String getInmbFotoURI() {
        return inmbFotoURI;
    }

    public void setInmbFotoURI(String inmbFotoURI) {
        this.inmbFotoURI = inmbFotoURI;
    }

    public Boolean getInmbDisp() {
        return inmbDisp;
    }

    public void setinmbDisp(Boolean inmbDisp) {
        this.inmbDisp = inmbDisp;
    }

    public long getInmbTimeStamp() {
        return inmbTimeStamp;
    }

    public void setInmbTimeStamp(long inmbTimeStamp) {
        this.inmbTimeStamp = inmbTimeStamp;
    }

}
