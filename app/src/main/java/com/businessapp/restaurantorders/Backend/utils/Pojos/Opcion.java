package com.businessapp.restaurantorders.Backend.utils.Pojos;

public class Opcion {
    private String nombre;
    private boolean seleccionado;
    private double costo_extra;

    public Opcion(String nombre, boolean seleccionado, double costo_extra) {
        this.nombre = nombre;
        this.seleccionado = seleccionado;
        this.costo_extra = costo_extra;
    }

    public Opcion() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public double getCosto_extra() {
        return costo_extra;
    }

    public void setCosto_extra(double costo_extra) {
        this.costo_extra = costo_extra;
    }
}

