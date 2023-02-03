package com.businessapp.restaurantorders.Backend.Providers;

import android.content.Context;
import android.widget.Toast;

import com.businessapp.restaurantorders.Backend.Notifications.Notification;
import com.businessapp.restaurantorders.Backend.utils.PedidoUtils;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Pedido;
import com.businessapp.restaurantorders.Backend.utils.UtilFunctions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProviderPedidos {

    private FirebaseFirestore db;
    private String collectionName = "pedidos";
    private Context context;

    public ProviderPedidos(Context context) {
        this.db = FirebaseFirestore.getInstance();
        this.context = context;
    }

    public Task<Void> insert_new_pedido(Pedido pedido, String id) {

        return db.collection(collectionName).document(id).set(pedido);

    }

    public void deleteListOfPedidos(List<Pedido> pedidos_entregados_y_cancelados) {
        if (pedidos_entregados_y_cancelados != null) {

            if (pedidos_entregados_y_cancelados.size() > 0) {

                //delete all pedidos in list
                for(int i = 0; i < pedidos_entregados_y_cancelados.size(); i++){

                    //delete individually pedido iterated in list.
                    Pedido pedido = pedidos_entregados_y_cancelados.get(i);
                    if(pedido != null){
                        int finalI = i;
                        deletePedido(pedido).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if(finalI == pedidos_entregados_y_cancelados.size() -1){
                                    Toast.makeText(context, "Pedidos entregados y cancelados eliminados correctamente.", Toast.LENGTH_SHORT).show();
                                }
                                System.out.println("("+ finalI +")"+" "+pedido.getOwner_firebase_uid()+"-"+pedido.getNumero_pedido() + " eliminado correctamente");
                            }
                        });
                    }
                }

            }

        }
    }

    private Task<Void> deletePedido(Pedido pedido) {
       return db.collection(collectionName).document(pedido.getOwner_firebase_uid()+"-"+pedido.getNumero_pedido()).delete();
    }

    public Task<Void> updatePedidoState(String pedidoFirebaseDocID, String pedidoState) {

        if(pedidoState.equals(PedidoUtils.STATE_PROCESS_CANCELLED)
                || pedidoState.equals(PedidoUtils.STATE_PROCESS_2)
                || pedidoState.equals(PedidoUtils.STATE_PROCESS_3)
                        || pedidoState.equals(PedidoUtils.STATE_PROCESS_4)){

            /** SEND NOTIFICATION **/
            //GET CLIENT FIREBASE ID .
            if(context != null){
                ProviderPedidos providerPedidos = new ProviderPedidos(context);
                providerPedidos.getPedido(pedidoFirebaseDocID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot != null){
                            Pedido pedido = documentSnapshot.toObject(Pedido.class);
                            if(pedido != null){
                                String client_firebaseID = pedido.getOwner_firebase_uid();
                                //SEND NOTIFICATION TO CLIENT
                                pedido.setEstado(pedidoState);
                                Notification notification = PedidoUtils.createNotificationFromPedido(pedido);
                                UtilFunctions.sendNotificationToFirebaseClient(context,client_firebaseID,notification);
                            }
                        }
                    }
                });
            }

        }



        return db.collection(collectionName).document(pedidoFirebaseDocID).update("estado",pedidoState);
    }

    public Task<Void> updatePedido(Pedido pedido) {

        //send notification to client.
        Notification notification = PedidoUtils.createNotificationFromPedido(pedido);
        UtilFunctions.sendNotificationToFirebaseClient(context,pedido.getOwner_firebase_uid(),notification);

        String id = pedido.getOwner_firebase_uid() + "-" + pedido.getNumero_pedido();
        return db.collection(collectionName).document(id).set(pedido);
    }


    public DocumentReference getPedido(String id) {
       return db.collection(collectionName).document(id);
    }
}
