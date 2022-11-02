package com.businessapp.restaurantorders.Frontend.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.businessapp.restaurantorders.Frontend.Fragment_Ajustes;
import com.businessapp.restaurantorders.Frontend.Fragment_Pedidos;
import com.businessapp.restaurantorders.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
   private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getSupportActionBar() != null) getSupportActionBar().hide();


       initVIews();
    }

    private void initVIews() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                switch(itemId){

                    case R.id.pedidos: GoToFragment(new Fragment_Pedidos());
                    break;

                    case R.id.configuration: GoToFragment(new Fragment_Ajustes());
                    break;

                    default: GoToFragment(new Fragment_Pedidos());
                    break;

                }

                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.pedidos);
    }

    private void GoToFragment(Fragment fragment){

        if(fragment != null){

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if(getSupportFragmentManager().getFragments().size() > 0) fragmentTransaction.replace(R.id.fragment_container,fragment); else fragmentTransaction.add(R.id.fragment_container,fragment);
            fragmentTransaction.commit();

        }

    }

}