package com.businessapp.restaurantorders.Backend.utils.Pojos;

public class Coordenadas {
    private double latitud = (long)21.120865123714527;
    private double longitud = (long)-101.69038851779938;


    public Coordenadas(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Coordenadas() {
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}