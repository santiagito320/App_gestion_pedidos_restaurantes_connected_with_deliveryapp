package com.businessapp.restaurantorders.Frontend.Activities;

import android.bluetooth.BluetoothDevice;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.businessapp.restaurantorders.Backend.utils.Bluetooth.BluetoothPrint;
import com.businessapp.restaurantorders.Backend.utils.Constantes;
import com.businessapp.restaurantorders.R;
import com.google.gson.Gson;

public class Activity_ImpresoraTermicaDetalles extends AppCompatActivity {
    //Data
    private BluetoothDevice bluetoothDevice;

    //Views.
    private Toolbar toolbar;
    private TextView txt_nombre;
    private TextView txt_macCode;
    private Button btn_control_bluetoothConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__impresora_termica_detalles);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        getData();
        initViews();
    }

    private void getData() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String bluetoothDevice_Json = extras.getString("BluetoothDevice");
            this.bluetoothDevice = new Gson().fromJson(bluetoothDevice_Json, BluetoothDevice.class);

        }
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

        txt_nombre = findViewById(R.id.txt_dispositivoNombre);
        txt_macCode = findViewById(R.id.txt_dispositivoMacCode);
        btn_control_bluetoothConnection = findViewById(R.id.button_ConectarDispositivo);

    }

    @Override
    protected void onStart() {
        super.onStart();
        setDataInViews();
    }

    private void setDataInViews() {

        if(bluetoothDevice != null){
            txt_nombre.setText(bluetoothDevice.getName());
            txt_macCode.setText(bluetoothDevice.getAddress());

            ChangeButtonAttributes();
            btn_control_bluetoothConnection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Constantes.bluetoothPrint == null){
                        Constantes.bluetoothPrint = new BluetoothPrint(Activity_ImpresoraTermicaDetalles.this,getResources());
                    }

                    ChangeButtonAttributes();

                    boolean isConnected = Constantes.bluetoothPrint.checkConnection();
                    if(isConnected){
                        Constantes.bluetoothPrint.disconnectBT();
                    }
                    else{
                        Constantes.bluetoothPrint.openBluetoothPrinter(bluetoothDevice.getUuids()[0].getUuid());
                    }

                }
            });
        }

    }

    private void ChangeButtonAttributes() {
        if(Constantes.bluetoothPrint != null){
            boolean isConnected = Constantes.bluetoothPrint.checkConnection();

            int button_drawableStart = !isConnected ? R.drawable.ic_baseline_bluetooth_searching_24 : R.drawable.ic_baseline_bluetooth_disabled_24;
            String button_text = !isConnected ? "Conectar" : "Desconectar";

            Drawable buttonDrawableStart = getDrawable(button_drawableStart);
            btn_control_bluetoothConnection.setCompoundDrawablesRelative(buttonDrawableStart,null,null,null);
            btn_control_bluetoothConnection.setText(button_text);
        }else{
            Drawable buttonDrawableStart = getDrawable(R.drawable.ic_baseline_bluetooth_searching_24);

            btn_control_bluetoothConnection.setCompoundDrawablesRelative(buttonDrawableStart,null,null,null);
            btn_control_bluetoothConnection.setText("Conectar");
        }

    }
}