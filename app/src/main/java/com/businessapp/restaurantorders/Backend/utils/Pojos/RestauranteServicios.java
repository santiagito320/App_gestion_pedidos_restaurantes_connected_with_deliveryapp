package com.businessapp.restaurantorders.Backend.utils.Pojos;

public class RestauranteServicios {
    private boolean recojer_en_sucursal = true;
    private boolean entrega_a_domicilio = false;


    public RestauranteServicios(boolean recojer_en_sucursal, boolean entrega_a_domicilio) {
        this.recojer_en_sucursal = recojer_en_sucursal;
        this.entrega_a_domicilio = entrega_a_domicilio;
    }

    public RestauranteServicios() {
    }

    public boolean isRecojer_en_sucursal() {
        return recojer_en_sucursal;
    }

    public void setRecojer_en_sucursal(boolean recojer_en_sucursal) {
        this.recojer_en_sucursal = recojer_en_sucursal;
    }

    public boolean isEntrega_a_domicilio() {
        return entrega_a_domicilio;
    }

    public void setEntrega_a_domicilio(boolean entrega_a_domicilio) {
        this.entrega_a_domicilio = entrega_a_domicilio;
    }
}
