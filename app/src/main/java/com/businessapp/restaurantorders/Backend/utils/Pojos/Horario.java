package com.businessapp.restaurantorders.Backend.utils.Pojos;

public class Horario {

    private String desde = "00:00:00";
    private String hasta = "23:59:59";


    public Horario(String desde, String hasta) {
        this.desde = desde;
        this.hasta = hasta;
    }

    public Horario() {
    }

    public String getDesde() {
        return desde;
    }

    public void setDesde(String desde) {
        this.desde = desde;
    }

    public String getHasta() {
        return hasta;
    }

    public void setHasta(String hasta) {
        this.hasta = hasta;
    }
}
