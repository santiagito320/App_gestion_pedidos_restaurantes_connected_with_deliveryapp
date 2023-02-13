package com.businessapp.restaurantorders.Backend.utils.Adapters;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.businessapp.restaurantorders.Backend.utils.Bluetooth.BluetoothPrint;
import com.businessapp.restaurantorders.Frontend.Activities.Activity_ImpresoraTermicaDetalles;
import com.businessapp.restaurantorders.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AdapterListBluetoothDevices extends RecyclerView.Adapter<AdapterListBluetoothDevices.ViewHolder> {

    private Context context;
    private List<BluetoothDevice> bluetoothDevices;

    public AdapterListBluetoothDevices(Context context, List<BluetoothDevice> bluetoothDevices) {
        this.context = context;
        this.bluetoothDevices = bluetoothDevices;
    }

    public List<BluetoothDevice> getBluetoothDevices() {
        return bluetoothDevices;
    }

    public void setBluetoothDevices(List<BluetoothDevice> bluetoothDevices) {
        this.bluetoothDevices = new ArrayList<>(bluetoothDevices);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bluetooth_device,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bluetoothDevice = bluetoothDevices.get(position);
        if(holder.bluetoothDevice != null){
            holder.txt_deviceName.setText(holder.bluetoothDevice.getName());
            holder.txt_connectionStatus.setText(holder.bluetoothDevice.getAddress());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(context != null){
                        Toast.makeText(context, "Conectando a "+holder.bluetoothDevice.getName()+"...", Toast.LENGTH_LONG).show();

                        //validar si el dispositivo es una impresora
                        BluetoothPrint bluetoothPrint = new BluetoothPrint(context,context.getResources());
                        BluetoothClass bluetoothClass = holder.bluetoothDevice.getBluetoothClass();
                        if(bluetoothClass.getDeviceClass() == BluetoothClass.Device.COMPUTER_HANDHELD_PC_PDA || bluetoothClass.getDeviceClass() == BluetoothClass.Device.COMPUTER_PALM_SIZE_PC_PDA) {
                            // validar que es una impresora normal o térmica.
                            bluetoothPrint.openBluetoothPrinter(holder.bluetoothDevice.getUuids()[0].getUuid());

                            Intent i = new Intent(context, Activity_ImpresoraTermicaDetalles.class);
                            String bluetoothDevide_json = new Gson().toJson(holder.bluetoothDevice);
                            i.putExtra(bluetoothDevide_json,"BluetoothDevice");
                            context.startActivity(i);
                        }

                    }
                }
            });

        }else {
            holder.itemView.setAlpha(0.5f);
            holder.itemView.setEnabled(false);
        };

    }

    @Override
    public int getItemCount() {
        if(bluetoothDevices.size() > 0) return bluetoothDevices.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       BluetoothDevice bluetoothDevice;

       TextView txt_deviceName;
       TextView txt_connectionStatus;
       ImageView img_deviceType;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_deviceName = itemView.findViewById(R.id.txt_bluetoothName);
            txt_connectionStatus = itemView.findViewById(R.id.txt_bluetoothIsConnected);
            img_deviceType = itemView.findViewById(R.id.imgView_deviceBluetoothType);
        }
    }
}
