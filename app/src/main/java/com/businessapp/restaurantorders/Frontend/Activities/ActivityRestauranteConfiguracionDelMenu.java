package com.businessapp.restaurantorders.Frontend.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.businessapp.restaurantorders.Backend.utils.Constantes;
import com.businessapp.restaurantorders.Backend.utils.Pojos.NegocioGeneral;
import com.businessapp.restaurantorders.Backend.utils.Pojos.NegocioMenu;
import com.businessapp.restaurantorders.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ActivityRestauranteConfiguracionDelMenu extends AppCompatActivity {
    //views.
    private FloatingActionButton fab_addCategory;
    //data.
    private NegocioMenu negocioMenu;
    //controllers.


    //extras.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante_configuracion_del_menu);

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NegocioGeneral negocioGeneral = Constantes.negocio;
        if (negocioGeneral != null) {

            negocioMenu = negocioGeneral.getMenu();
            setDataInViews();

        }
    }

    private void initViews() {
        fab_addCategory = findViewById(R.id.fab_add_category);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setDataInViews() {
        if (negocioMenu != null) {

            fab_addCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ActivityRestauranteConfiguracionDelMenu.this, Activity_CategoriaProducto.class);
                    startActivity(i);
                }
            });

        } else {
            Toast.makeText(this, "Error al conectar con el menu. Contacte a soporte", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}