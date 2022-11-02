package com.businessapp.restaurantorders.Backend.utils.Pojos;

import java.util.ArrayList;
import java.util.List;

public class Sucursal {
    private String nombre = "";
    private Direccion direccion_absoluta = new Direccion();
    private List<String> horarios = new ArrayList<>();
    private Coordenadas coordenadas = new Coordenadas();
    private String imagen_demostrativa_url = "";
    private int radio_de_alcanze_de_pedidos = 3500;


    public Sucursal(String nombre, Direccion direccion_absoluta, List<String> horarios, Coordenadas coordenadas,String imagen_demostrativa_url,int radio_de_alcanze_de_pedidos) {
        this.nombre = nombre;
        this.direccion_absoluta = direccion_absoluta;
        this.horarios = horarios;
        this.coordenadas = coordenadas;
        this.imagen_demostrativa_url = imagen_demostrativa_url;
        this.radio_de_alcanze_de_pedidos = radio_de_alcanze_de_pedidos;
    }

    public int getRadio_de_alcanze_de_pedidos() {
        return radio_de_alcanze_de_pedidos;
    }

    public void setRadio_de_alcanze_de_pedidos(int radio_de_alcanze_de_pedidos) {
        this.radio_de_alcanze_de_pedidos = radio_de_alcanze_de_pedidos;
    }

    public void setDireccion_absoluta(Direccion direccion_absoluta) {
        this.direccion_absoluta = direccion_absoluta;
    }

    public Direccion getDireccion_absoluta() {
        return direccion_absoluta;
    }

    public String getImagen_demostrativa_url() {
        return imagen_demostrativa_url;
    }

    public void setImagen_demostrativa_url(String imagen_demostrativa_url) {
        this.imagen_demostrativa_url = imagen_demostrativa_url;
    }

    public Sucursal() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public List<String> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<String> horarios) {
        this.horarios = horarios;
    }

    public Coordenadas getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(Coordenadas coordenadas) {
        this.coordenadas = coordenadas;
    }


}
