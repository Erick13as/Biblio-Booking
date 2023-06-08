package com.example.biblio_booking;

public class Asignacion {
    private String hora;
    private String cubiculo;
    private String carnet;
    private String cantidad;
    private String fecha;

    public Asignacion(String hora, String cubiculo, String carnet, String cantidad, String fecha) {
        this.hora = hora;
        this.cubiculo = cubiculo;
        this.carnet = carnet;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getCubiculo() {
        return cubiculo;
    }

    public void setCubiculo(String cubiculo) {
        this.cubiculo = cubiculo;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}

