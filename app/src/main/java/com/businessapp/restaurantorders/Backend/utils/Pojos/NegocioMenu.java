package com.businessapp.restaurantorders.Backend.utils.Pojos;


import java.util.ArrayList;
import java.util.List;

public class NegocioMenu {
    private List<MenuCategoria> categorias = new ArrayList<>();
    private List<Anuncio> anuncios = new ArrayList<>();


    public NegocioMenu(List<MenuCategoria> categorias, List<Anuncio> anuncios) {
        this.categorias = categorias;
        this.anuncios = anuncios;
    }

    public NegocioMenu() {
    }

    public List<MenuCategoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<MenuCategoria> categorias) {
        this.categorias = categorias;
    }

    public List<Anuncio> getAnuncios() {
        return anuncios;
    }

    public void setAnuncios(List<Anuncio> anuncios) {
        this.anuncios = anuncios;
    }
}
