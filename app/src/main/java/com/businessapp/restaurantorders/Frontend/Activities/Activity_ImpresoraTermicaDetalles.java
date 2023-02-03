package com.businessapp.restaurantorders.Frontend.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.businessapp.restaurantorders.R;

public class Activity_ImpresoraTermicaDetalles extends AppCompatActivity {
   private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__impresora_termica_detalles);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)actionBar.hide();

        initViews();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar3);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_white_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}