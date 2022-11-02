package com.businessapp.restaurantorders.Backend.utils.Pojos;

import java.util.List;

public class Seleccion {
    private String titulo;
    private boolean obligatorio;
    private List<Opcion> opciones;
    private int max_options_selected = -1;

    public Seleccion(String titulo, boolean obligatorio, List<Opcion> opciones,int max_options_selected) {
        this.titulo = titulo;
        this.obligatorio = obligatorio;
        this.opciones = opciones;
        this.max_options_selected = max_options_selected;
    }

    public Seleccion() {
    }

    public int getMax_options_selected() {
        return max_options_selected;
    }

    public void setMax_options_selected(int max_options_selected) {
        this.max_options_selected = max_options_selected;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    public List<Opcion> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<Opcion> opciones) {
        this.opciones = opciones;
    }
}
