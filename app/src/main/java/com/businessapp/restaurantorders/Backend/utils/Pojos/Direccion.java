package com.businessapp.restaurantorders.Backend.utils.Pojos;

public class Direccion {
    private String calle = "";
    private String num_exterior = "";
    private String num_interior = "";
    private String aliasdireccion = "";
    private String codigo_postal = "";
    private String colonia = "";
    private String telefono = "";
    private String extension = "";
    private String referencias = "";
    private boolean selected = false;
    private String pais = "";
    private String ciudad = "";
    private String estado = "";
    private String codigo_pais = "";
    private String latitud = "";
    private String longitud= "";


    public Direccion(String calle, String num_exterior, String num_interior, String aliasdireccion, String codigo_postal, String colonia, String telefono, String extension, String referencias) {
        this.calle = calle;
        this.num_exterior = num_exterior;
        this.num_interior = num_interior;
        this.aliasdireccion = aliasdireccion;
        this.codigo_postal = codigo_postal;
        this.colonia = colonia;
        this.telefono = telefono;
        this.extension = extension;
        this.referencias = referencias;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCodigo_pais() {
        return codigo_pais;
    }

    public void setCodigo_pais(String codigo_pais) {
        this.codigo_pais = codigo_pais;
    }

    public Direccion() {
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNum_exterior() {
        return num_exterior;
    }

    public void setNum_exterior(String num_exterior) {
        this.num_exterior = num_exterior;
    }

    public String getNum_interior() {
        return num_interior;
    }

    public void setNum_interior(String num_interior) {
        this.num_interior = num_interior;
    }

    public String getAliasdireccion() {
        return aliasdireccion;
    }

    public void setAliasdireccion(String aliasdireccion) {
        this.aliasdireccion = aliasdireccion;
    }

    public String getCodigo_postal() {
        return codigo_postal;
    }

    public void setCodigo_postal(String codigo_postal) {
        this.codigo_postal = codigo_postal;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getReferencias() {
        return referencias;
    }

    public void setReferencias(String referencias) {
        this.referencias = referencias;
    }
}

