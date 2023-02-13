package com.businessapp.restaurantorders.Backend.utils.Pojos;

public class MetodosDePago {

    private boolean efectivo = true;
    private boolean tarjeta_terminal = false;

    public MetodosDePago(boolean efectivo, boolean tarjeta_terminal) {
        this.efectivo = efectivo;
        this.tarjeta_terminal = tarjeta_terminal;
    }

    public MetodosDePago() {
    }

    public boolean isEfectivo() {
        return efectivo;
    }

    public void setEfectivo(boolean efectivo) {
        this.efectivo = efectivo;
    }

    public boolean isTarjeta_terminal() {
        return tarjeta_terminal;
    }

    public void setTarjeta_terminal(boolean tarjeta_terminal) {
        this.tarjeta_terminal = tarjeta_terminal;
    }
}
