package com.businessapp.restaurantorders.Frontend.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.businessapp.restaurantorders.Backend.utils.Adapters.AdapterListBluetoothDevices;
import com.businessapp.restaurantorders.Backend.utils.Bluetooth.BluetoothPrint;
import com.businessapp.restaurantorders.Backend.utils.Constantes;
import com.businessapp.restaurantorders.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Activity_OrdersTermicPrinterConfiguration extends AppCompatActivity {
   private Toolbar toolbar;
   private FloatingActionButton fab_search_bluetooths;
   private TextView txt_bluetooths_availables;
   private RecyclerView recyclerView_bluetooths_availables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__orders_termic_printer_configuration);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)actionBar.hide();

        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SearchBluetoothDevices();

        if(Constantes.bluetoothPrint != null){
            Intent i = new Intent(this,Activity_ImpresoraTermicaDetalles.class);
            startActivity(i);
        }

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar2);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_white_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        fab_search_bluetooths = findViewById(R.id.fab_search_bluetooths);
        fab_search_bluetooths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchBluetoothDevices();
            }
        });

        recyclerView_bluetooths_availables = findViewById(R.id.recyclerView_bluetooths_availables);
        recyclerView_bluetooths_availables.setLayoutManager(new LinearLayoutManager(Activity_OrdersTermicPrinterConfiguration.this));
        recyclerView_bluetooths_availables.setHasFixedSize(true);

        AdapterListBluetoothDevices adapterListBluetoothDevices = new AdapterListBluetoothDevices(Activity_OrdersTermicPrinterConfiguration.this,new ArrayList<>());
        recyclerView_bluetooths_availables.setAdapter(adapterListBluetoothDevices);

        txt_bluetooths_availables = findViewById(R.id.txt_bluetooths_availables);
    }

    private void SearchBluetoothDevices() {
        ProgressDialog progressDialog = ProgressDialog.show(this, "Bluetooth ",
                "Buscando dispositivos...", true);

        Toast.makeText(this, "Buscando dispositivos...", Toast.LENGTH_LONG).show();

        Resources resources = getResources();
        if(resources != null){

            BluetoothPrint bluetoothPrint = new BluetoothPrint(Activity_OrdersTermicPrinterConfiguration.this,getResources());
            List<BluetoothDevice> bluetoothDevices = bluetoothPrint.FindBluetoothDevices();



            new Handler(getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    setBluetoothDevicesInList(bluetoothDevices);
                }
            },2000);

        }

    }

    private void setBluetoothDevicesInList(List<BluetoothDevice> bluetoothDevices) {

        BluetoothDevice bluetoothDeviceHelper = null;
        //bluetoothDevices.add(bluetoothDeviceHelper);

        txt_bluetooths_availables.setText( "("+ bluetoothDevices.size() +") Dispositivos Bluetooth disponibles");

        if(bluetoothDevices.size() > 0){

            new AlertDialog.Builder(Activity_OrdersTermicPrinterConfiguration.this).setTitle("¡Advertencia!").setIcon(R.drawable.ic_baseline_warning_24).setMessage("Si tu impresora térmica no aparece en esta lista, deberas buscarla desde: Ajustes > Bluetooth > Buscar dispositivos y conectarla directamente.").setCancelable(true).setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();

            recyclerView_bluetooths_availables.setVisibility(View.VISIBLE);

            AdapterListBluetoothDevices adapter = (AdapterListBluetoothDevices) recyclerView_bluetooths_availables.getAdapter();

            if(adapter != null){

                adapter.setBluetoothDevices(bluetoothDevices);

            }else{
                adapter = new AdapterListBluetoothDevices(Activity_OrdersTermicPrinterConfiguration.this,bluetoothDevices);
                adapter.notifyDataSetChanged();
            }

            recyclerView_bluetooths_availables.setAdapter(adapter);

            Toast.makeText(this, "Se encontraron "+ bluetoothDevices.size() + " dispositivos bluetooth disponibles", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(this, "¡No hay impresoras térmicas disponibles!", Toast.LENGTH_SHORT).show();
            recyclerView_bluetooths_availables.setVisibility(View.GONE);
        }

    }

}