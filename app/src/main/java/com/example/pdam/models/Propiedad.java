package com.example.pdam.models;

public class Propiedad {

    private String pID;
    private String id_usuario;
    private String nombreDescriptivo;
    private String tipo;
    private String precio;
    private String periodo;
    private String provincia;
    private String municipio;
    private String direccion;
    private String cp;
    //private String id_chat;

    public Propiedad() {
        // Default constructor required for calls to DataSnapshot.getValue(Propiedad.class)
    }

    public Propiedad(String id_usuario, String nombreDescriptivo, String tipo, String periodo, String precio, String provincia, String cp, String municipio, String direccion) {
        this.id_usuario = id_usuario;
        this.nombreDescriptivo = nombreDescriptivo;
        this.tipo = tipo;
        this.periodo = periodo;
        this.precio = precio;
        this.provincia = provincia;
        this.cp = cp;
        this.municipio = municipio;
        this.direccion = direccion;
    }

    public Propiedad(Propiedad propiedad) {
        this.pID = propiedad.pID;
        this.id_usuario = propiedad.id_usuario;
        this.nombreDescriptivo = propiedad.nombreDescriptivo;
        this.tipo = propiedad.tipo;
        this.precio = propiedad.precio;
        this.periodo = propiedad.periodo;
        this.provincia = propiedad.provincia;
        this.municipio = propiedad.municipio;
        this.direccion = propiedad.direccion;
        this.cp = propiedad.cp;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getnombreDescriptivo() {
        return nombreDescriptivo;
    }

    public void setnombreDescriptivo(String nombreDescriptivo) {
        this.nombreDescriptivo = nombreDescriptivo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }
}
