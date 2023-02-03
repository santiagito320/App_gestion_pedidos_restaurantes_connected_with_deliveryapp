package com.businessapp.restaurantorders.Frontend.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.businessapp.restaurantorders.R;

public class Activity_AppRestaurante_Configuracion extends AppCompatActivity {
    //Views.
    private LinearLayout linearLayout_miRestaurante;
    private LinearLayout linearLayout_anuncios;
    private ImageButton imageButton_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__apprestaurante_configuracion);


        initViews();
    }



    private void initViews() {
        linearLayout_miRestaurante = findViewById(R.id.linearLayout_miRestaurante);
        linearLayout_anuncios = findViewById(R.id.linearLayout_anuncios);
        imageButton_back = findViewById(R.id.imgbtn_back);

        linearLayout_miRestaurante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Activity_AppRestaurante_Configuracion.this,Activity_RestauranteConfiguration.class);
                startActivity(i);

            }
        });
        linearLayout_anuncios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Activity_AppRestaurante_Configuracion.this,Activity_set_AppClientAnuncios.class);
                startActivity(i);
            }
        });
        imageButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}