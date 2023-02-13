package com.businessapp.restaurantorders.Backend.utils.Pojos;



import java.util.ArrayList;
import java.util.List;

public class NegocioGeneral {
    private String nombre = "";
    private String facebook_link = "";
    private String instagram_link = "";
    private String twitter_link = "";
    private String web_link = "";
    private String telefono = "";
    private String email = "";
    private List<Sucursal> sucursales = new ArrayList<Sucursal>();
    private NegocioMenu menu;
    private String app_email = "";
    private String link_politicasyterminosdecondiciones = "";
    private long tiempo_estimado_en_preparacion_de_pedido_insec = 0;
    private double costo_despacho = 35;
    private String access_code = "";
    private RestauranteServicios servicios_disponibles;
    private Horarios horarios;
    private double costo_fijo_de_envio_de_productos = 0;
    private MetodosDePago metodos_de_pago;


    public NegocioGeneral(String nombre, String facebook_link, String instagram_link, String telefono, String email, List<Sucursal> sucursales, NegocioMenu menu, String app_email, String link_politicasyterminosdecondiciones, long tiempo_estimado_en_preparacion_de_pedido_insec, double costo_despacho, String web_link, String twitter_link) {
        this.nombre = nombre;
        this.facebook_link = facebook_link;
        this.instagram_link = instagram_link;
        this.telefono = telefono;
        this.email = email;
        this.sucursales = sucursales;
        this.menu = menu;
        this.app_email = app_email;
        this.link_politicasyterminosdecondiciones = link_politicasyterminosdecondiciones;
        this.tiempo_estimado_en_preparacion_de_pedido_insec = tiempo_estimado_en_preparacion_de_pedido_insec;
        this.costo_despacho = costo_despacho;
        this.web_link = web_link;
        this.twitter_link = twitter_link;
    }

    public MetodosDePago getMetodos_de_pago() {
        return metodos_de_pago;
    }

    public void setMetodos_de_pago(MetodosDePago metodos_de_pago) {
        this.metodos_de_pago = metodos_de_pago;
    }

    public double getCosto_fijo_de_envio_de_productos() {
        return costo_fijo_de_envio_de_productos;
    }

    public void setCosto_fijo_de_envio_de_productos(double costo_fijo_de_envio_de_productos) {
        this.costo_fijo_de_envio_de_productos = costo_fijo_de_envio_de_productos;
    }

    public Horarios getHorarios() {
        return horarios;
    }

    public void setHorarios(Horarios horarios) {
        this.horarios = horarios;
    }

    public RestauranteServicios getServicios_disponibles() {
        return servicios_disponibles;
    }

    public void setServicios_disponibles(RestauranteServicios servicios_disponibles) {
        this.servicios_disponibles = servicios_disponibles;
    }

    public String getAccess_code() {
        return access_code;
    }

    public void setAccess_code(String access_code) {
        this.access_code = access_code;
    }

    public long getTiempo_estimado_en_preparacion_de_pedido_insec() {
        return tiempo_estimado_en_preparacion_de_pedido_insec;
    }

    public void setTiempo_estimado_en_preparacion_de_pedido_insec(long tiempo_estimado_en_preparacion_de_pedido_insec) {
        this.tiempo_estimado_en_preparacion_de_pedido_insec = tiempo_estimado_en_preparacion_de_pedido_insec;
    }

    public String getWeb_link() {
        return web_link;
    }

    public void setWeb_link(String web_link) {
        this.web_link = web_link;
    }

    public double getCosto_despacho() {
        return costo_despacho;
    }

    public void setCosto_despacho(double costo_despacho) {
        this.costo_despacho = costo_despacho;
    }

    public String getLink_politicasyterminosdecondiciones() {
        return link_politicasyterminosdecondiciones;
    }

    public void setLink_politicasyterminosdecondiciones(String link_politicasyterminosdecondiciones) {
        this.link_politicasyterminosdecondiciones = link_politicasyterminosdecondiciones;
    }

    public String getApp_email() {
        return app_email;
    }

    public void setApp_email(String app_email) {
        this.app_email = app_email;
    }

    public NegocioGeneral() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFacebook_link() {
        return facebook_link;
    }

    public void setFacebook_link(String facebook_link) {
        this.facebook_link = facebook_link;
    }

    public String getInstagram_link() {
        return instagram_link;
    }

    public void setInstagram_link(String instagram_link) {
        this.instagram_link = instagram_link;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Sucursal> getSucursales() {
        return sucursales;
    }

    public void setSucursales(List<Sucursal> sucursales) {
        this.sucursales = sucursales;
    }

    public NegocioMenu getMenu() {
        return menu;
    }

    public void setMenu(NegocioMenu menu) {
        this.menu = menu;
    }


    public String getTwitter_link() {
        return twitter_link;
    }

    public void setTwitter_link(String twitter_link) {
        this.twitter_link = twitter_link;
    }

}

