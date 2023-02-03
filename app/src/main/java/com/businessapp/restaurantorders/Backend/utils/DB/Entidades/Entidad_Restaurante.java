package com.businessapp.restaurantorders.Backend.utils.DB.Entidades;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.businessapp.restaurantorders.Backend.utils.Pojos.Coordenadas;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Direccion;

import java.util.ArrayList;
import java.util.List;

@Entity

public class Entidad_Restaurante {

    @PrimaryKey(autoGenerate = false)
    public int id = 0;

    public String nombre = "";
    public String direccion_absoluta_json;
    public String horarios_array_json;
    public String coordenadas_json;
    public String imagen_demostrativa_url = "";
    public int radio_de_alcanze_de_pedidos = 0;



}
