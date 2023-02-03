package com.businessapp.restaurantorders.Frontend.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.businessapp.restaurantorders.R;

public class Activity_MisRestaurantes extends AppCompatActivity {
// views
    private Button btn_nuevoRestaurante;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__mis_restaurantes);

        initViews();
    }

    private void initViews() {
        btn_nuevoRestaurante = findViewById(R.id.btn_nuevoRestaurante);
        btn_nuevoRestaurante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}