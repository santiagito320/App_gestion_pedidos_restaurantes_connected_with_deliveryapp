package com.businessapp.restaurantorders.Backend.utils.Pojos;

public class Horarios {

    private Horario lunes;
    private Horario martes;
    private Horario miercoles;
    private Horario jueves;
    private Horario viernes;
    private Horario sabado;
    private Horario domingo;


    public Horarios(Horario lunes, Horario martes, Horario miercoles, Horario jueves, Horario viernes, Horario sabado, Horario domingo) {
        this.lunes = lunes;
        this.martes = martes;
        this.miercoles = miercoles;
        this.jueves = jueves;
        this.viernes = viernes;
        this.sabado = sabado;
        this.domingo = domingo;
    }

    public Horarios() {
    }

    public Horario getLunes() {
        return lunes;
    }

    public void setLunes(Horario lunes) {
        this.lunes = lunes;
    }

    public Horario getMartes() {
        return martes;
    }

    public void setMartes(Horario martes) {
        this.martes = martes;
    }

    public Horario getMiercoles() {
        return miercoles;
    }

    public void setMiercoles(Horario miercoles) {
        this.miercoles = miercoles;
    }

    public Horario getJueves() {
        return jueves;
    }

    public void setJueves(Horario jueves) {
        this.jueves = jueves;
    }

    public Horario getViernes() {
        return viernes;
    }

    public void setViernes(Horario viernes) {
        this.viernes = viernes;
    }

    public Horario getSabado() {
        return sabado;
    }

    public void setSabado(Horario sabado) {
        this.sabado = sabado;
    }

    public Horario getDomingo() {
        return domingo;
    }

    public void setDomingo(Horario domingo) {
        this.domingo = domingo;
    }
}
