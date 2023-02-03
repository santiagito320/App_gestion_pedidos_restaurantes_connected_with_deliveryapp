package com.businessapp.restaurantorders.Backend.Providers;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProviderUsuarios {

    private String collectionName = "usuarios";
    private Context context;
    private FirebaseFirestore db;


    public ProviderUsuarios(Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }


    public Task<DocumentSnapshot> getUsuario(String firebase_uid){
        return db.collection(collectionName).document(firebase_uid).get();
    }

}
