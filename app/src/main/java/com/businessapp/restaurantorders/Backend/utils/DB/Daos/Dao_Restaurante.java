package com.businessapp.restaurantorders.Backend.utils.DB.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.businessapp.restaurantorders.Backend.utils.DB.Entidades.Entidad_Restaurante;
import com.google.firebase.firestore.auth.User;

import java.util.List;

@Dao
public interface Dao_Restaurante {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert_selected_restaurante(Entidad_Restaurante entidad_restaurante);

    @Query("SELECT * FROM Entidad_Restaurante")
    LiveData<List<Entidad_Restaurante>> getAll();

    @Update
    void updateRestaurante(Entidad_Restaurante entidadRestaurante);

    @Delete
    void deleteRestaurante(Entidad_Restaurante entidadRestaurante);
}
