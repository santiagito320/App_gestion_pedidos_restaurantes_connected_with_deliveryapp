package com.businessapp.restaurantorders.Backend.utils.Adapters;

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

import com.businessapp.restaurantorders.Frontend.Activities.Activity_ImpresoraTermicaDetalles;
import com.businessapp.restaurantorders.R;

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
            holder.txt_connectionStatus.setText(holder.bluetoothDevice.getBondState() == 1 ? "Conectado" : "Desconectado");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(context != null){
                        Toast.makeText(context, "Conectando a "+holder.bluetoothDevice.getName()+"...", Toast.LENGTH_LONG).show();

                        Intent i = new Intent(context, Activity_ImpresoraTermicaDetalles.class);
                        context.startActivity(i);
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
