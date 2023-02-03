package com.businessapp.restaurantorders.Backend.utils.Pojos;


public class Anuncio {
    private String titulo;
    private String descripcion;
    private String imagen_url;


    public Anuncio(String titulo, String descripcion, String imagen_url) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagen_url = imagen_url;
    }

    public Anuncio() {
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

    public String getImagen_url() {
        return imagen_url;
    }

    public void setImagen_url(String imagen_url) {
        this.imagen_url = imagen_url;
    }
}
