package com.example.biblio_booking;

public class Asignacion {
    private String horaEntrada;
    private String horaSalida;
    private String cubiculo;
    private String carnet;
    private String cantidad;
    private String fecha;

    public Asignacion(String horaEntrada, String cubiculo,String carnet, String cantidad, String fecha,String horaSalida) {
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.cubiculo = cubiculo;
        this.carnet = carnet;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    public String getHora() {
        return horaEntrada;
    }

    public void setHora(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }
    public String getHoraSali() {
        return horaSalida;
    }

    public void setHoraSali(String horaSalida) {
        this.horaSalida = horaSalida;
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

