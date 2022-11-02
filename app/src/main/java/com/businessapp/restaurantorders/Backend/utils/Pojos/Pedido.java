package com.businessapp.restaurantorders.Backend.utils.Pojos;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private String numero_pedido;
    private String estado = "InActivo";
    private List<Producto> productos = new ArrayList<>();
    private double pago_total = 0;
    private String hora_entrega_estimada = "";
    private Sucursal sucursal = new Sucursal();//sucursal remitente.
    private Destinatario destinatario = new Destinatario();
    private String tipo_entrega = "Recojer en sucursal";
    private String medio_de_pago = "Efectivo";
    private String modo_de_entrega = "Dejar el paquete en conserjeria o en la puerta si es una casa";
    private String owner_firebase_uid = "";
    private long born_timestamp;


    public Pedido(String numero_pedido, String estado, List<Producto> productos, double pago_total, String hora_entrega_estimada, Sucursal sucursal, Destinatario destinatario, String tipo_entrega, String medio_de_pago, String modo_de_entrega) {
        this.numero_pedido = numero_pedido;
        this.estado = estado;
        this.productos = productos;
        this.pago_total = pago_total;
        this.hora_entrega_estimada = hora_entrega_estimada;
        this.sucursal = sucursal;
        this.destinatario = destinatario;
        this.tipo_entrega = tipo_entrega;
        this.medio_de_pago = medio_de_pago;
        this.modo_de_entrega = modo_de_entrega;
    }

    public String getModo_de_entrega() {
        return modo_de_entrega;
    }

    public void setModo_de_entrega(String modo_de_entrega) {
        this.modo_de_entrega = modo_de_entrega;
    }

    public Pedido() {
    }

    public String getMedio_de_pago() {
        return medio_de_pago;
    }

    public void setMedio_de_pago(String medio_de_pago) {
        this.medio_de_pago = medio_de_pago;
    }

    public String getNumero_pedido() {
        return numero_pedido;
    }

    public void setNumero_pedido(String numero_pedido) {
        this.numero_pedido = numero_pedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public double getPago_total() {
        return pago_total;
    }

    public void setPago_total(double pago_total) {
        this.pago_total = pago_total;
    }

    public String getHora_entrega_estimada() {
        return hora_entrega_estimada;
    }

    public void setHora_entrega_estimada(String hora_entrega_estimada) {
        this.hora_entrega_estimada = hora_entrega_estimada;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Destinatario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Destinatario destinatario) {
        this.destinatario = destinatario;
    }

    public String getTipo_entrega() {
        return tipo_entrega;
    }

    public void setTipo_entrega(String tipo_entrega) {
        this.tipo_entrega = tipo_entrega;
    }

    public String getOwner_firebase_uid() {
        return owner_firebase_uid;
    }

    public void setOwner_firebase_uid(String owner_firebase_uid) {
        this.owner_firebase_uid = owner_firebase_uid;
    }

    public long getBorn_timestamp() {
        return born_timestamp;
    }

    public void setBorn_timestamp(long born_timestamp) {
        this.born_timestamp = born_timestamp;
    }
}
