package com.businessapp.restaurantorders.Backend.utils.DB.Repositorios;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.businessapp.restaurantorders.Backend.utils.DB.DB.AppDatabase;
import com.businessapp.restaurantorders.Backend.utils.DB.Daos.Dao_Restaurante;
import com.businessapp.restaurantorders.Backend.utils.DB.Entidades.Entidad_Restaurante;

import java.util.List;

public class Repositorio_Restaurante {
    private Context context;

    private Dao_Restaurante daoRestaurante;
    private LiveData<List<Entidad_Restaurante>> restaurante_livedata;

    public Repositorio_Restaurante(Context context) {
        this.context = context;
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        daoRestaurante = appDatabase.dao_restaurante();

        //parse entity to pojo.
        restaurante_livedata = daoRestaurante.getAll();
    }

    public LiveData<List<Entidad_Restaurante>> getRestaurante() {
        return  restaurante_livedata;
    }

    public void insertRestaurante(Entidad_Restaurante entidadRestaurante) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                daoRestaurante.insert_selected_restaurante(entidadRestaurante);
            }
        }).start();
    }

    public void updateRestaurante(Entidad_Restaurante entidadRestaurante) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                daoRestaurante.updateRestaurante(entidadRestaurante);
            }
        }).start();
    }

    public void delete_restaurante(Entidad_Restaurante entidadRestaurante) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                daoRestaurante.deleteRestaurante(entidadRestaurante);
            }
        }).start();
    }
}
