package com.businessapp.restaurantorders.Backend.utils.DB.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.businessapp.restaurantorders.Backend.utils.DB.Daos.Dao_Restaurante;
import com.businessapp.restaurantorders.Backend.utils.DB.Entidades.Entidad_Restaurante;

@Database(entities = {Entidad_Restaurante.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract Dao_Restaurante dao_restaurante();


    private static AppDatabase appDatabase;

    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = buildDatabaseInstance(context);
        }
        return appDatabase;
    }

    private static synchronized AppDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class,
                "appDatabase")
                .build();

    }

    public void cleanUp(){
        appDatabase = null;
    }
}
