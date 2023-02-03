package com.businessapp.restaurantorders.Backend.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.businessapp.restaurantorders.Backend.Notifications.Notification;
import com.businessapp.restaurantorders.Backend.Providers.ProviderPedidos;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Destinatario;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Direccion;
import com.businessapp.restaurantorders.Backend.utils.Pojos.NegocioGeneral;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Opcion;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Pedido;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Producto;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Seleccion;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Sucursal;
import com.businessapp.restaurantorders.Backend.utils.utils.UiFunctions;
import com.businessapp.restaurantorders.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.security.AllPermission;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

                    //los estados: 'esperando a ser entregado' & 'llego a tu domicilio' van en una seccion juntos.
                    if(state.equals(PedidoUtils.STATE_PROCESS_3)){
                        if(pedido.getEstado().equals(state) || pedido.getEstado().equals(STATE_PROCESS_4)){
                            pedidos_filtered.add(pedido);

                        }
                    }else
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

    public static synchronized String crearNumeroDePedido(String pre) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYMMddHHmmss");
        String orderNum = pre + dateFormat.format(new Date());

        return orderNum;
    }


    public static void GenerarPedidoDePrueba(Context context) {
        ProviderPedidos providerPedidos = new ProviderPedidos(context);

        //productos
        List<Producto> pedido_prueba_productos = new ArrayList<>();
        List<Seleccion> seleccions = new ArrayList<>();
        List<Opcion> opcions = new ArrayList<>();
        Opcion opcion = new Opcion("Pequeña",true,0);
        Opcion opcion1 = new Opcion("Mediana",false,0);
        Opcion opcion2 = new Opcion("Grande",false,0);

        opcions.add(0,opcion);
        opcions.add(1,opcion1);
        opcions.add(2,opcion2);

        Seleccion seleccion = new Seleccion("Tamaño",true,opcions,1);

        List<Opcion> opcions1 = new ArrayList<>();
        Opcion opcion1_1 = new Opcion("Queso Mozarella",true,15);

        opcions1.add(0,opcion1_1);

        Seleccion seleccion1 = new Seleccion("Adiciones",true,opcions1,1);


        seleccions.add(0,seleccion);
        seleccions.add(1,seleccion1);

        Producto producto = new Producto("Especialidades","Pizza Prosciutto","¡Pizza prosciutto tamaño familiar!",100,"sin champiñones, porfavor!",2,200,seleccions,"https://fabeveryday.com/wp-content/uploads/2021/04/prosciutto-mushroom-truffle-pizza-6-720x720.jpg");
        pedido_prueba_productos.add(0,producto);

        //Sucurs4l
        Sucursal sucursal = Constantes.MiRestaurante_Seleccionado;
        if(sucursal == null){
            sucursal = new Sucursal();
        }
        //destinatario
        List<Direccion> direcciones = new ArrayList<>();
        Direccion direccion_seleccionada = new Direccion("La luz","194","","Dirección de ejemplo","37260","","0123456789","00"," referencias de ejemplo");
        direccion_seleccionada.setCiudad("León");
        direccion_seleccionada.setCodigo_pais("MX");
        direccion_seleccionada.setEstado("Gto");
        direccion_seleccionada.setLatitud(String.valueOf(21.185968511144807));
        direccion_seleccionada.setLongitud(String.valueOf(-101.64616272900957));
        direccion_seleccionada.setSelected(true);
        direccion_seleccionada.setColonia("La luz");
        direcciones.add(direccion_seleccionada);
        Destinatario destinatario = new Destinatario("John","Doe","0123456789",1,"john.doe@email.com", direcciones);

        //Pedido.
        Pedido pedido_prueba = new Pedido(crearNumeroDePedido(""),PedidoUtils.STATE_PROCESS_1,pedido_prueba_productos,200,"",sucursal,destinatario,PedidoUtils.OrderReceive_Mode1,"Efectivo","Encontrarse en la puerta");
        pedido_prueba.setOwner_firebase_uid(crearNumeroDePedido(""));
        long current_timestamp = System.currentTimeMillis()/1000;
        pedido_prueba.setBorn_timestamp(current_timestamp);

        providerPedidos.insert_new_pedido(pedido_prueba,pedido_prueba.getOwner_firebase_uid()+"-"+pedido_prueba.getNumero_pedido()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Pedido de prueba generado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                UiFunctions.ShowBasicAlertDialog(context,"Fallo al generar pedido de prueba","Fallo al generar pedido de prueba, verifica tu conexión, o intenta de nuevo mas tarde.", R.drawable.ic_baseline_warning_24,true);
                Toast.makeText(context, "Fallo al generar pedido de prueba, verifica tu conexión, o intenta de nuevo mas tarde.", Toast.LENGTH_LONG).show();

            }
        });
    }

    public static void BorrarTodosLosPedidos(List<Pedido> AllPedidos,Context context) {

        if(AllPedidos != null){

            if(AllPedidos.size() > 0){

               List<Pedido> pedidos_entregados_y_cancelados = PedidoUtils.filtrarPedidosEntregadosYCancelados(AllPedidos);
                if(pedidos_entregados_y_cancelados.size() > 0){

                    UiFunctions.ShowBasicAlertDialogWithButtons(context,"¿Está seguro?","Esto borrará los pedidos entregados y cancelados, excepto los pedidos pendientes.",-1,true)
                    .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).setPositiveButton("SI, ELIMINAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        //delete pedidos entregados y cancelados.
                         dialogInterface.dismiss();

                         ProviderPedidos providerPedidos = new ProviderPedidos(context);
                         providerPedidos.deleteListOfPedidos(pedidos_entregados_y_cancelados);

                        }
                    }).show();

                }else{
                    UiFunctions.ShowBasicAlertDialog(context,"Borrar pedidos", "Tu lista de pedidos (entregados y cancelados) ya está vacia.",-1,true);
                }
            }else{
                UiFunctions.ShowBasicAlertDialog(context,"Borrar pedidos", "Tu lista de pedidos (entregados y cancelados) ya está vacia.",-1,true);
            }

        }


    }

    private static List<Pedido> filtrarPedidosEntregadosYCancelados(List<Pedido> allPedidos) {

        List<Pedido> pedidos_filtered = new ArrayList<>();

        if(allPedidos != null){

            if(allPedidos.size() > 0){

                for(int i = 0; i < allPedidos.size(); i ++){

                    Pedido pedido = allPedidos.get(i);
                    if(pedido != null){

                        if(pedido.getEstado().equals(PedidoUtils.STATE_PROCESS_CANCELLED) || pedido.getEstado().equals(PedidoUtils.STATE_PROCESS_5)){

                            pedidos_filtered.add(pedido);

                        }

                    }

                }

            }

        }

        return pedidos_filtered;
    }

    public static Notification createNotificationFromPedido(Pedido pedido) {
        if(pedido != null){


            String negocioNombre = "";
            NegocioGeneral negocioGeneral = Constantes.negocio;
            if(negocioGeneral != null){
                negocioNombre = negocioGeneral.getNombre();
            }
            String notificationTitle = !negocioNombre.equals("") ? "Pedido en "+negocioNombre : "Pedido actualizado";
            String notificationMessage = "Tu pedido ha cambiado de status, ¡Hecha un vistazo!";

            if(pedido.getEstado().equals(PedidoUtils.STATE_PROCESS_CANCELLED)){
                notificationMessage = "Tu pedido fue cancelado "+(!negocioNombre.equals("") ? ("por "+negocioNombre) : "");
            }else if(pedido.getEstado().equals(PedidoUtils.STATE_PROCESS_2)){
                notificationMessage = "Tu pedido fue aceptado y se esta preparando";
            }else if(pedido.getEstado().equals(STATE_PROCESS_3)){

                if(pedido.getTipo_entrega().equals(PedidoUtils.OrderReceive_Mode1)){

                    notificationMessage = "Tu pedido esta listo, ¡Es hora de recojerlo en el local "+pedido.getSucursal().getNombre()+"!";

                }else if(pedido.getTipo_entrega().equals(PedidoUtils.OrderReceive_Mode2)){

                    notificationMessage = "Tu pedido esta en camino, ¡Estate atent@!";

                }

            }else if(pedido.getEstado().equals(PedidoUtils.STATE_PROCESS_4)){
                notificationMessage = "El repartidor ha llegado al domicilio, ¡asegurate de que el pedido ha sido entregado en las condiciones deseadas!";
            }


            return new Notification(notificationTitle,notificationMessage);

        }else return null;
    }
}
