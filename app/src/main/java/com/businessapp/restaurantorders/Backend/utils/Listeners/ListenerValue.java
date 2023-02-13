package com.businessapp.restaurantorders.Backend.utils.Listeners;

import android.widget.EditText;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public interface ListenerValue {

    void onValueChanged(Object valor);
    void onValueChangedAux(Object valor,boolean valor_booleano, EditText editText, BottomSheetDialog bottomSheetDialog);

}
