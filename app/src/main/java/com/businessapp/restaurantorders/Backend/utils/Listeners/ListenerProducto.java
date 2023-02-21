package com.businessapp.restaurantorders.Backend.utils.Listeners;

import android.widget.EditText;

import com.businessapp.restaurantorders.Backend.utils.Pojos.Producto;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public interface ListenerProducto {

    void onValueChanged(Producto producto);

}
