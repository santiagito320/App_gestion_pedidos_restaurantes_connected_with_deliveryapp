package com.businessapp.restaurantorders.Backend.utils;

import com.businessapp.restaurantorders.Backend.utils.Pojos.Direccion;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Pedido;

import java.util.ArrayList;
import java.util.List;

public class PedidoUtils {

    public static String STATE_PROCESS_0 = "No Activo";
    public static String STATE_PROCESS_CANCELLED = "Cancelado";
    public static String STATE_PROCESS_1 = "Confirmando";
    public static String STATE_PROCESS_2 = "Preparando";
    public static String STATE_PROCESS_3 = "Esperando a ser entregado";
    public static String STATE_PROCESS_4 = "Llegó a tu domicilio";
    public static String STATE_PROCESS_5 = "Entregado";

    public static String OrderReceive_Mode1 = "Recojer en sucursal";
    public static String OrderReceive_Mode2 = "Entrega a domicilio";


    public static List<Pedido> filterPedidosByState(List<Pedido> pedidos, String state) {
        if (state.equals("All")) {
            return pedidos;
        }
        List<Pedido> pedidos_filtered = new ArrayList<>();

        if (pedidos.size() > 0) {

            int pedidos_size = pedidos.size();
            for (int i = 0; i < pedidos_size; i++) {

                Pedido pedido = pedidos.get(i);
                if (pedido != null) {

                    if (pedido.getEstado().equals(state)) {

                        pedidos_filtered.add(pedido);

                    }

                }

            }

        }

        return pedidos_filtered;
    }

    public static Direccion getDestinantarioDireccionSeleccionada(List<Direccion> direcciones) {

        if (direcciones != null) {
            if (direcciones.size() > 0) {

                Direccion direccion_seleccionada = null;
                for (int i = 0; i < direcciones.size(); i++) {
                    Direccion direccion = direcciones.get(i);
                    if (direccion.isSelected()) {
                        direccion_seleccionada = direccion;
                        break;
                    }
                }

                return direccion_seleccionada;

            } else return null;
        } else return null;

    }

    public static List<Pedido> removeNewPedidos(List<Pedido> pedidos) {
        List<Pedido> pedidosCleaned = new ArrayList<>();

        if(pedidos != null){

            final int pedidosSize = pedidos.size();
            for(int i = 0; i < pedidosSize; i++){

                Pedido pedido = pedidos.get(i);

                if(pedido != null){
                    if(!pedido.getEstado().equals(PedidoUtils.STATE_PROCESS_1)){
                        pedidosCleaned.add(pedido);
                    }
                }


            }

        }

        return pedidosCleaned;
    }

    public static boolean HayUnPedidoNuevoEnEstaLista(List<Pedido> pedidos) {
        if(pedidos != null){

            for(int i = 0; i < pedidos.size(); i++){

                Pedido pedido = pedidos.get(i);

                if(pedido != null){

                    if(pedido.getEstado().equals(PedidoUtils.STATE_PROCESS_1)){
                        return true;
                    }

                }
            }
            return  false;


        }else return false;
    }
}
