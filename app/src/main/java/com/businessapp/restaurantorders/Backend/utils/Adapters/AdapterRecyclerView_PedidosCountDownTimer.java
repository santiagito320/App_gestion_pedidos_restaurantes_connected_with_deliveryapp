package com.businessapp.restaurantorders.Backend.utils.Adapters;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.businessapp.restaurantorders.Backend.utils.FirebaseCloudFirestore_Collections;
import com.businessapp.restaurantorders.Backend.utils.PedidoCountDownTimer;
import com.businessapp.restaurantorders.Backend.utils.PedidoUtils;
import com.businessapp.restaurantorders.Backend.utils.Pedido_CountDownTimer_SPreferences;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Destinatario;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Direccion;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Pedido;
import com.businessapp.restaurantorders.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;


public class AdapterRecyclerView_PedidosCountDownTimer extends RecyclerView.Adapter<AdapterRecyclerView_PedidosCountDownTimer.ViewHolder> {

    private static final Long START_TIME_IN_MILLIS = 240000L;
    private Context context;
    private List<PedidoCountDownTimer> pedidos = new ArrayList<>();
    private MediaPlayer mediaPlayer;

    public AdapterRecyclerView_PedidosCountDownTimer(Context context) {
        this.context = context;
        if (context != null && mediaPlayer == null)
            mediaPlayer = MediaPlayer.create(context, R.raw.din_bell);
    }

    public AdapterRecyclerView_PedidosCountDownTimer(Context context, List<PedidoCountDownTimer> pedidos) {
        this.context = context;
        this.pedidos = pedidos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_pedido, parent, false);

        return new AdapterRecyclerView_PedidosCountDownTimer.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (pedidos != null) return pedidos.size();
        else return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     //   holder.pedido = pedidos.get(position);
        if (context != null && pedidos != null) {

            if (holder.pedido != null) {

                int entregaMode = holder.pedido.getTipo_entrega().equals(PedidoUtils.OrderReceive_Mode2) ? 2 : 1;

                holder.roundedImageView.setImageResource(entregaMode == 2 ? R.drawable.ic_baseline_motorcycle_24 : R.drawable.ic_baseline_shopping_basket_24);
                Destinatario destinatario = holder.pedido.getDestinatario();
                if (destinatario != null) {
                    if (entregaMode == 2) {
                        List<Direccion> direccions = destinatario.getDirecciones();
                        Direccion direccion_despacho = PedidoUtils.getDestinantarioDireccionSeleccionada(direccions);
                        if (direccion_despacho != null) {

                            holder.txt_direccionDespacho.setText(String.format("%s %s, %s, %s", direccion_despacho.getCalle(), direccion_despacho.getNum_exterior(), direccion_despacho.getCodigo_postal(), direccion_despacho.getCiudad()));


                        } else {
                            holder.txt_direccionDespacho.setText(destinatario.getNombre());

                        }
                    } else {
                        holder.txt_direccionDespacho.setText(destinatario.getNombre());
                    }

                }

                String pedido_estado = holder.pedido.getEstado();

                String estado_text = "Pendiente...";
                Resources resources = context.getResources();

                int drawableResStart = R.drawable.ic_baseline_warning_24;
                int stateColor = -1;
                if (pedido_estado.equals(PedidoUtils.STATE_PROCESS_CANCELLED)) {
                    drawableResStart = R.drawable.ic_baseline_close_24;
                    estado_text = "Cancelado";
                    stateColor = Color.RED;
                } else if (pedido_estado.equals(PedidoUtils.STATE_PROCESS_1)) {
                    drawableResStart = R.drawable.ic_baseline_new_releases_24;
                    stateColor = -1;

                    estado_text = "¡Nuevo!";
                } else if (pedido_estado.equals(PedidoUtils.STATE_PROCESS_2)) {
                    drawableResStart = R.drawable.ic_baseline_restaurant_24;
                    stateColor = -1;


                    estado_text = "Preparando...";
                } else if (pedido_estado.equals(PedidoUtils.STATE_PROCESS_3)) {
                    drawableResStart = R.drawable.ic_baseline_call_received_24;
                    stateColor = -1;

                    estado_text = "Esperando a ser entregado...";

                } else if (pedido_estado.equals(PedidoUtils.STATE_PROCESS_4)) {
                    drawableResStart = R.drawable.ic_baseline_my_location_24;
                    stateColor = -1;

                    estado_text = "Llegó al domicilio";
                } else if (pedido_estado.equals(PedidoUtils.STATE_PROCESS_5)) {
                    drawableResStart = R.drawable.ic_baseline_check_24;
                    stateColor = Color.GREEN;

                    estado_text = "Entregada";
                }




                // Pedidos Pendientes
                if (stateColor != -1) {
                    Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, drawableResStart);
                    if (unwrappedDrawable != null) {
                        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                        DrawableCompat.setTint(wrappedDrawable, stateColor);
                    }

                    holder.txt_pedidoState.setTextColor(stateColor);
                }


                holder.txt_pedidoState.setCompoundDrawablesRelativeWithIntrinsicBounds(drawableResStart, 0, 0, 0);
                holder.txt_pedidoState.setText(estado_text);

                if (pedido_estado.equals(PedidoUtils.STATE_PROCESS_1)) {
                    //Start down counter
                    holder.txt_precioTotal.setVisibility(View.GONE);
                    holder.txt_expiration_counter.setVisibility(View.VISIBLE);
                    Animation shake;
                    shake = AnimationUtils.loadAnimation(context, R.anim.shake);


                } else {
                    holder.txt_precioTotal.setVisibility(View.VISIBLE);
                    holder.txt_expiration_counter.setVisibility(View.GONE);

                }
            }

        }
    }


    public void updateMValues(List<Pedido> mvalues) {
       // this.pedidos = new ArrayList<>(mvalues);

        notifyDataSetChanged();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        Pedido pedido;

        View itemView;
        RoundedImageView roundedImageView;
        TextView txt_direccionDespacho;
        TextView txt_pedidoState;
        TextView txt_expiration_counter;
        TextView txt_precioTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            roundedImageView = itemView.findViewById(R.id.roundedImageView_tipoentrega);
            txt_direccionDespacho = itemView.findViewById(R.id.txt_ubicacion);
            txt_pedidoState = itemView.findViewById(R.id.txt_state);
            txt_expiration_counter = itemView.findViewById(R.id.txt_expirationCounter);
            txt_precioTotal = itemView.findViewById(R.id.txt_pagoTotal);
        }
    }
}

