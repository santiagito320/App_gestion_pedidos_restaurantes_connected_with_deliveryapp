package com.businessapp.restaurantorders.Backend.Providers;

import android.content.Context;
import android.widget.Toast;

import com.businessapp.restaurantorders.Backend.utils.Constantes;
import com.businessapp.restaurantorders.Backend.utils.DB.Entidades.Entidad_Restaurante;
import com.businessapp.restaurantorders.Backend.utils.DB.Repositorios.Repositorio_Restaurante;
import com.businessapp.restaurantorders.Backend.utils.Pojos.NegocioGeneral;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Sucursal;
import com.businessapp.restaurantorders.Backend.utils.UtilFunctions;
import com.businessapp.restaurantorders.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProviderNegocioGeneral {
    private Context context;
    private FirebaseFirestore db;
    private String collectionName;
    private String documentName;

    public ProviderNegocioGeneral(Context context) {

        this.db = FirebaseFirestore.getInstance();
        this.collectionName = "negocio";
        documentName = context.getString(R.string.app_delivery_name);
        this.context = context;
    }



    public void ControlSucursalStatus(String sucursalNombre, boolean isAbierto) {
        List<Sucursal> sucursales = Constantes.negocio.getSucursales();

        if(sucursales != null){

            //find sucursal in list.
            boolean finded = false;

            int sucursalesSize = sucursales.size();
            for(int i = 0; i < sucursalesSize; i++){
                Sucursal sucursal_iterated = sucursales.get(i);
                if(sucursalNombre.equals(sucursal_iterated.getNombre())){
                    sucursal_iterated.setAbierto(isAbierto);
                    sucursales.set(i,sucursal_iterated);
                    finded = true;
                    break;
                }
            }
            if(finded ){
                NegocioGeneral negocioGeneral = Constantes.negocio;
                negocioGeneral.setSucursales(sucursales);
                db.collection(collectionName).document(documentName).set(negocioGeneral).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Abierto = " + isAbierto, Toast.LENGTH_SHORT).show();
                        Constantes.MiRestaurante_Seleccionado.setAbierto(isAbierto);
                        //update current sucursal in local db to remind it.
                        Repositorio_Restaurante repositorio_restaurante = new Repositorio_Restaurante(context);

                        Entidad_Restaurante entidad_restaurante = UtilFunctions.ParseSucursalTOEntidadRestaurante(Constantes.MiRestaurante_Seleccionado);
                        if (entidad_restaurante != null) {

                            repositorio_restaurante.insertRestaurante(entidad_restaurante);

                        }
                    }
                });
            }
        }

    }

    public DocumentReference getMyNegocioGeneral() {
        return db.collection(collectionName).document(documentName);
    }
}
