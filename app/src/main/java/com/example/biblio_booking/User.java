package com.example.biblio_booking;
import java.io.Serializable;

public class User implements Serializable {
    private String nombre;
    private String apellido;
    private String apellido2;
    private String carnet;
    private String fechaNac;
    private String correo;
    private String contraseña;

    private String idTipo;

    private String idEstado;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String nombre, String apellido, String apellido2, String carnet, String fechaNac, String correo, String contraseña, String idTipo, String idEstado) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.apellido2 = apellido2;
        this.carnet = carnet;
        this.fechaNac = fechaNac;
        this.correo = correo;
        this.contraseña = contraseña;
        this.idTipo = idTipo;
        this.idEstado = idEstado;
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

    public String getIdTipo() {
        return idTipo;
    }

    public String getIdEstado() {
        return idEstado;
    }
}

