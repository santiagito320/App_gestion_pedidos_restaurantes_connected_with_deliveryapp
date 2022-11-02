package com.businessapp.restaurantorders.Backend.utils.Pojos;

import java.util.ArrayList;
import java.util.List;

public class Producto {
    private String categoria = "";
    private String titulo = "";
    private String descripcion = "";
    private double precio_unitario = 0;
    private String instruccion_especial = "";
    private int cantidad = 1;
    private double precio_total = 0;
    private List<Seleccion> selecciones = new ArrayList<>();
    private String imagen_url = "";

    public Producto(String categoria, String titulo, String descripcion, double precio_unitario, String instruccion_especial, int cantidad, double precio_total, List<Seleccion> selecciones,String imagen_url) {
        this.categoria = categoria;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio_unitario = precio_unitario;
        this.instruccion_especial = instruccion_especial;
        this.cantidad = cantidad;
        this.precio_total = precio_total;
        this.selecciones = selecciones;
        this.imagen_url = imagen_url;
    }

    public Producto() {
    }

    public String getImagen_url() {
        return imagen_url;
    }

    public void setImagen_url(String imagen_url) {
        this.imagen_url = imagen_url;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public String getInstruccion_especial() {
        return instruccion_especial;
    }

    public void setInstruccion_especial(String instruccion_especial) {
        this.instruccion_especial = instruccion_especial;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio_total() {
        return precio_total;
    }

    public void setPrecio_total(double precio_total) {
        this.precio_total = precio_total;
    }

    public List<Seleccion> getSelecciones() {
        return selecciones;
    }

    public void setSelecciones(List<Seleccion> selecciones) {
        this.selecciones = selecciones;
    }
}
