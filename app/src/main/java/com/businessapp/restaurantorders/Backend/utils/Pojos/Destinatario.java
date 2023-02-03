package com.businessapp.restaurantorders.Backend.utils.Pojos;


import java.util.ArrayList;
import java.util.List;

public class Destinatario {
    private String nombre = "";
    private String apellido = "";
    private String telefono = "";
    private int numero_pedidos = 0;
    private String email = "";
    private List<Direccion> direcciones = new ArrayList<>();
    private String token = "";


    public Destinatario(String nombre, String apellido, String telefono, int numero_pedidos, String email, List<Direccion> direcciones) {
        this.nombre = nombre;
        this.apellido = apellido;

        this.telefono = telefono;
        this.numero_pedidos = numero_pedidos;
        this.email = email;
        this.direcciones = direcciones;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Destinatario() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getNumero_pedidos() {
        return numero_pedidos;
    }

    public void setNumero_pedidos(int numero_pedidos) {
        this.numero_pedidos = numero_pedidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Direccion> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<Direccion> direcciones) {
        this.direcciones = direcciones;
    }
}
