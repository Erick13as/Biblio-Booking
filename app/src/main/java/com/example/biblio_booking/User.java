package com.example.biblio_booking;

public class User {
    private String nombre;
    private String apellido;
    private String apellido2;
    private String carnet;
    private String fechaNac;
    private String correo;
    private String contraseña;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String nombre, String apellido, String apellido2, String carnet, String fechaNac, String correo, String contraseña) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.apellido2 = apellido2;
        this.carnet = carnet;
        this.fechaNac = fechaNac;
        this.correo = correo;
        this.contraseña = contraseña;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getApellido2() {
        return apellido2;
    }

    public String getCarnet() {
        return carnet;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public String getCorreo() {
        return correo;
    }

    public String getContraseña() {
        return contraseña;
    }
}

