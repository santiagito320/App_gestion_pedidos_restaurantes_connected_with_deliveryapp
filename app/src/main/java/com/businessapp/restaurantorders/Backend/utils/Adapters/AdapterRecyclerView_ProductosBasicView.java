package com.businessapp.restaurantorders.Backend.utils.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.businessapp.restaurantorders.Backend.utils.Pojos.Opcion;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Producto;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Seleccion;
import com.businessapp.restaurantorders.Backend.utils.UtilFunctions;
import com.businessapp.restaurantorders.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecyclerView_ProductosBasicView extends RecyclerView.Adapter<AdapterRecyclerView_ProductosBasicView.ViewHolder> {
    //Utils.
    private Context context;
    //data.
    private List<Producto> productos;


    public AdapterRecyclerView_ProductosBasicView(Context context, List<Producto> productos) {
        this.context = context;
        this.productos = new ArrayList<>(productos);
    }

    public AdapterRecyclerView_ProductosBasicView() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.producto_item_basic, parent, false);

        return new AdapterRecyclerView_ProductosBasicView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mProducto = productos.get(position);
        if (holder.mProducto != null) {

            holder.imgbtn_arrow_dropDown.setTag("Closed");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String state = holder.imgbtn_arrow_dropDown.getTag().toString();
                    int content_visibility = View.VISIBLE;

                    int imgbtn_arrow_res = R.drawable.ic_arrow_drop_up;

                    if (state.equals("Closed")) {
                        imgbtn_arrow_res = R.drawable.ic_arrow_drop_up;
                        content_visibility = View.VISIBLE;
                        state = "Open";
                    } else if (state.equals("Open")) {
                        imgbtn_arrow_res = R.drawable.ic_arrow_drop_down;
                        content_visibility = View.GONE;
                        state = "Closed";
                    }
                    holder.linearLayout_content_producto_details.setVisibility(content_visibility);
                    holder.imgbtn_arrow_dropDown.setImageResource(imgbtn_arrow_res);
                    holder.imgbtn_arrow_dropDown.setTag(state);


                }
            });

            String cantidad_str = holder.mProducto.getCantidad() + " X";
            holder.txt_producto_cantidad.setText(cantidad_str);
            holder.txt_producto_nombre.setText(holder.mProducto.getTitulo());
            holder.txt_producto_precioTotal.setText(UtilFunctions.DoubleToPrecioFormat(holder.mProducto.getPrecio_total()));

            String producto_instruccion_especial = holder.mProducto.getInstruccion_especial();
            if(producto_instruccion_especial.equals(""))
                holder.txt_producto_cliente_nota.setVisibility(View.GONE);
            else
                holder.txt_producto_cliente_nota.setVisibility(View.VISIBLE);

            holder.txt_producto_cliente_nota.setText(holder.mProducto.getInstruccion_especial());

            //iterate producto_selecciones for this product.
            List<Seleccion> producto_selecciones = holder.mProducto.getSelecciones();
            holder.linearLayout_content_producto_selecciones.removeAllViews();
            if (producto_selecciones != null) {
                if (producto_selecciones.size() > 0) {

                    int seleccionesSize = producto_selecciones.size();

                    for (int i = 0; i < seleccionesSize; i++) {

                        Seleccion seleccion = producto_selecciones.get(i);

                        if (seleccion != null) {

                            LinearLayout linearLayout_productoSelecion = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.producto_seleccion_item, null, false);
                            if (linearLayout_productoSelecion != null) {

                                TextView txt_producto_seleccion_nameAndValue = linearLayout_productoSelecion.findViewById(R.id.txt_producto_seleccion_nameAndValue);
                                TextView txt_producto_seleccion_extraCost = linearLayout_productoSelecion.findViewById(R.id.txt_producto_seleccion_extraCost);

                                Opcion opcion_seleccionada = UtilFunctions.getProductoSeleccionOpcionSeleccionada(seleccion.getOpciones());
                                String opcionNombre = "no especificado*";
                                double opcionSeleccionada_costoExtra = 0;

                                if (opcion_seleccionada != null) {
                                    opcionNombre = opcion_seleccionada.getNombre();
                                    opcionSeleccionada_costoExtra = opcion_seleccionada.getCosto_extra();
                                }

                                String seleccion_nameAndValue_txt = seleccion.getTitulo() + ": " + opcionNombre;
                                txt_producto_seleccion_nameAndValue.setText(seleccion_nameAndValue_txt);


                                String opcionSeleccionda_costo_extra_text = "+ " + UtilFunctions.DoubleToPrecioFormat(opcionSeleccionada_costoExtra);
                                txt_producto_seleccion_extraCost.setText(opcionSeleccionda_costo_extra_text);

                                if (opcionSeleccionada_costoExtra == 0)
                                    txt_producto_seleccion_extraCost.setVisibility(View.GONE);
                                else txt_producto_seleccion_extraCost.setVisibility(View.VISIBLE);


                                holder.linearLayout_content_producto_selecciones.addView(linearLayout_productoSelecion);
                            }
                        }

                    }

                }
            }


        } else holder.itemView.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        if (productos != null) return productos.size();
        else return 0;
    }

    public void update_mValues(List<Producto> productos) {
        this.productos = new ArrayList<>(productos);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //data.
        Producto mProducto;
        //Views.
        TextView txt_producto_cantidad, txt_producto_nombre, txt_producto_precioTotal,
                txt_producto_cliente_nota;
        LinearLayout linearLayout_content_producto_details, linearLayout_content_producto_selecciones;
        ImageButton imgbtn_arrow_dropDown;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_producto_cantidad = itemView.findViewById(R.id.txt_producto_cantidad);
            txt_producto_nombre = itemView.findViewById(R.id.txt_producto_nombre);
            txt_producto_cliente_nota = itemView.findViewById(R.id.txt_producto_nota);
            txt_producto_precioTotal = itemView.findViewById(R.id.txt_producto_costo);
            linearLayout_content_producto_details = itemView.findViewById(R.id.linearLayout_content_producto_details);
            linearLayout_content_producto_selecciones = itemView.findViewById(R.id.linearLayout_producto_selecciones);
            imgbtn_arrow_dropDown = itemView.findViewById(R.id.imgbtn_arrowDown);
        }
    }
}
