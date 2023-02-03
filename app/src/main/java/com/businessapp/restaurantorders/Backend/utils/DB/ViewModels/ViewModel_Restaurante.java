package com.businessapp.restaurantorders.Backend.utils.DB.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.businessapp.restaurantorders.Backend.utils.DB.Entidades.Entidad_Restaurante;
import com.businessapp.restaurantorders.Backend.utils.DB.Repositorios.Repositorio_Restaurante;

import java.util.List;

public class ViewModel_Restaurante extends AndroidViewModel{

        private Repositorio_Restaurante repositorioRestaurante;
        private LiveData<List<Entidad_Restaurante>> entidad_restaurante_livedata;


    public ViewModel_Restaurante(@NonNull Application application) {
        super(application);
        repositorioRestaurante = new Repositorio_Restaurante(application.getApplicationContext());
        entidad_restaurante_livedata = repositorioRestaurante.getRestaurante();

    }

    public LiveData<List<Entidad_Restaurante>> get_entidadrestaurante_livedata() {
            return entidad_restaurante_livedata;
        }
        public void insert_entidadRestaurante(Entidad_Restaurante entidadRestaurante){
            repositorioRestaurante.insertRestaurante(entidadRestaurante);
        }
        public void update_entidadRestaurante(Entidad_Restaurante entidadRestaurante){
            repositorioRestaurante.updateRestaurante(entidadRestaurante);
        }
        public void delete_restaurante(Entidad_Restaurante entidadRestaurante){
            repositorioRestaurante.delete_restaurante(entidadRestaurante);
        }




}
