package com.businessapp.restaurantorders.Backend.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UtilFunctions {


    public static <T> List<T> ParseJsonListToAnyObjectList(String json, Type listType) {
        if (json != null) {
            List<T> list = new ArrayList<>();


            if (json.equals("") || json.equals("[]")) list = new ArrayList<>();
            else list = new Gson().fromJson(json, listType);

            return list;
        } else return new ArrayList<>();
    }

}
