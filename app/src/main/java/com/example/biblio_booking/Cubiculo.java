package com.example.biblio_booking;

public class Cubiculo {
    private String capacidad;
    private String idCubiculo;
    private String idEstadoC;
    private String nombre;
    private String servicioE;
    private String tiempoMaxUso;
    private String ubicacion;




    public Cubiculo(String capacidad, String idCubiculo, String idEstadoC, String nombre, String servicioE, String tiempoMaxUso, String ubicacion) {
        this.capacidad = capacidad;
        this.idCubiculo = idCubiculo;
        this.idEstadoC = idEstadoC;
        this.nombre = nombre;
        this.servicioE = servicioE;
        this.tiempoMaxUso = tiempoMaxUso;
        this.ubicacion = ubicacion;

    }
    public String getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(String capacidad) {
        this.capacidad = capacidad;
    }

    public String getIdEstadoC() {
        return idEstadoC;
    }

    public void setIdEstadoC(String idEstadoC) {
        this.idEstadoC = idEstadoC;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getServicioE() {
        return servicioE;
    }

    public void setServicioE(String servicioE) {
        this.servicioE = servicioE;
    }

    public String getTiempoMaxUso() {
        return tiempoMaxUso;
    }

    public void setTiempoMaxUso(String tiempoMaxUso) {
        this.tiempoMaxUso = tiempoMaxUso;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
    public String getIdCubiculo() {
        return idCubiculo;
    }

    public void setIdCubiculo(String idCubiculo) {
        this.idCubiculo = idCubiculo;
    }

}
