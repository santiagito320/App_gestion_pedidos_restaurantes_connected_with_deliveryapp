package com.businessapp.restaurantorders.Backend.utils.Adapters;


import android.content.Context;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.businessapp.restaurantorders.Backend.utils.Constantes;
import com.businessapp.restaurantorders.Backend.utils.DB.Entidades.Entidad_Restaurante;
import com.businessapp.restaurantorders.Backend.utils.DB.Repositorios.Repositorio_Restaurante;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Sucursal;
import com.businessapp.restaurantorders.Backend.utils.UtilFunctions;
import com.businessapp.restaurantorders.Backend.utils.utils.UiFunctions;
import com.businessapp.restaurantorders.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecyclerView_Locales_list  extends RecyclerView.Adapter<AdapterRecyclerView_Locales_list.ViewHolder> {
    Context context;
    List<Sucursal> sucursales;
    onItemCLicked onItemCLicked;

    public AdapterRecyclerView_Locales_list(Context context, List<Sucursal> sucursals,onItemCLicked onItemCLicked) {
        this.context = context;
        this.sucursales = sucursals;
        this.onItemCLicked = onItemCLicked;
    }

    public interface onItemCLicked {
        void onItemClickedListener(Sucursal sucursal);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_sucursal_list, parent, false);

        return new AdapterRecyclerView_Locales_list.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.sucursal = sucursales.get(position);
        if(holder.sucursal != null){

            holder.txt_alias.setText(holder.sucursal.getNombre());
            if(holder.sucursal.getDireccion_absoluta() != null){
                holder.txt_direccion_absoluta.setText(holder.sucursal.getDireccion_absoluta().getCalle() + ","+holder.sucursal.getDireccion_absoluta().getColonia() +" #"+holder.sucursal.getDireccion_absoluta().getNum_exterior());

            }else{
                holder.txt_direccion_absoluta.setText("texto de ejemplo");

            }
            holder.content_local.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SelectLocal(holder.sucursal);
                    onItemCLicked.onItemClickedListener(holder.sucursal);
                }
            });

        }
    }

    private void SelectLocal(Sucursal sucursal) {
        //update restaurante seleccionado in local room database
        if(sucursal != null){
            //validate access with code.
            boolean bottom_sheet_cancelable = Constantes.MiRestaurante_Seleccionado != null;

            UiFunctions.showViewWithAccessCodeToSelectCurrentSucursal(sucursal,context,bottom_sheet_cancelable);

        }
    }

    @Override
    public int getItemCount() {
        if(sucursales!=null)
            return sucursales.size();else return 0;
    }

    public void updateValues(List<Sucursal> sucursals) {
        this.sucursales = new ArrayList<>(sucursals);
        notifyDataSetChanged();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        Sucursal sucursal;

        TextView txt_alias;
        TextView txt_direccion_absoluta;
        LinearLayout content_local;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            txt_alias = view.findViewById(R.id.txt_sucursal_alias);
            txt_direccion_absoluta = view.findViewById(R.id.txt_sucursal_direccion);
            content_local = view.findViewById(R.id.content_sucursal_direccion);
        }

    }

}
