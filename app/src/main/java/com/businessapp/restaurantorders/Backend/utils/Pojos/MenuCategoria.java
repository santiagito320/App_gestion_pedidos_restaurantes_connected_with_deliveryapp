package com.businessapp.restaurantorders.Backend.utils.Pojos;


import java.util.List;

public class MenuCategoria {
    private String alias;
    private List<Producto> productos;

    public MenuCategoria(String alias, List<Producto> productos) {
        this.alias = alias;
        this.productos = productos;
    }

    public MenuCategoria() {
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
}
