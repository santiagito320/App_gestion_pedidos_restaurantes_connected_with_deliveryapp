package com.businessapp.restaurantorders.Backend.utils.Adapters;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.businessapp.restaurantorders.Backend.Providers.ProviderPedidos;
import com.businessapp.restaurantorders.Backend.utils.FirebaseCloudFirestore_Collections;
import com.businessapp.restaurantorders.Backend.utils.PedidoCountDownTimer;
import com.businessapp.restaurantorders.Backend.utils.PedidoUtils;
import com.businessapp.restaurantorders.Backend.utils.Pedido_CountDownTimer_SPreferences;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Destinatario;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Direccion;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Pedido;
import com.businessapp.restaurantorders.Backend.utils.UtilFunctions;
import com.businessapp.restaurantorders.Frontend.Activities.Activity_PedidoConsole;
import com.businessapp.restaurantorders.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import static android.content.Context.MODE_PRIVATE;


public class AdapterRecyclerView_Pedidos extends RecyclerView.Adapter<AdapterRecyclerView_Pedidos.ViewHolder> {

    private static final Long START_TIME_IN_MILLIS = 240000L;
    ItemTouchHelper.SimpleCallback simpleItemTouchCallback;
    private Context context;
    private List<Pedido> pedidos = new ArrayList<>();
    private boolean sound_played = false;
    private List<Pedido_CountDownTimer_SPreferences> pedidosNuevos_countDownTimer_sPreferences = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private boolean HayPedidosNuevos = false;
    private List<PedidoCountDownTimer> pedidosNuevos_countDownTimerList = new ArrayList<>();
    private RecyclerView recyclerView_parent;
    private CountDownTimer countDownTimer;
    ColorStateList oldColors;//save original colors


    public AdapterRecyclerView_Pedidos(Context context, RecyclerView recyclerView_parent) {
        this.context = context;
        this.recyclerView_parent = recyclerView_parent;
        if (context != null && mediaPlayer == null)
            mediaPlayer = MediaPlayer.create(context, R.raw.din_bell);

        setItemTouchHelper();
    }

    public AdapterRecyclerView_Pedidos(Context context, List<Pedido> pedidos) {
        this.context = context;
        this.pedidos = pedidos;
    }

    private void setItemTouchHelper() {
        simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return true;
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getAdapterPosition();

                if (pedidos != null) {
                    if (pedidos.size() > 0) {

                        Pedido pedidoSwiped = pedidos.get(position);
                        if (pedidoSwiped != null) {

                            if (context != null) {
                                ProviderPedidos providerPedidos = new ProviderPedidos(context);

                                String pedido_firebaseID = pedidoSwiped.getOwner_firebase_uid() + "-" + pedidoSwiped.getNumero_pedido();

                                String pedidoState = pedidoSwiped.getEstado();
                                String pedidoNextState = pedidoState;


                                if (pedidoState.equals(PedidoUtils.STATE_PROCESS_2)) {

                                    pedidoNextState = PedidoUtils.STATE_PROCESS_3;

                                } else if (pedidoState.equals(PedidoUtils.STATE_PROCESS_3)) {

                                    if (pedidoSwiped.getTipo_entrega().equals(PedidoUtils.OrderReceive_Mode1)) {

                                        pedidoNextState = PedidoUtils.STATE_PROCESS_5;

                                    } else if (pedidoSwiped.getTipo_entrega().equals(PedidoUtils.OrderReceive_Mode2)) {

                                        pedidoNextState = PedidoUtils.STATE_PROCESS_4;

                                    }


                                } else if (pedidoState.equals(PedidoUtils.STATE_PROCESS_4)) {

                                    pedidoNextState = PedidoUtils.STATE_PROCESS_5;


                                }

                                providerPedidos.updatePedidoState(pedido_firebaseID, pedidoNextState).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "Estado del Pedido #"+pedidoSwiped.getNumero_pedido() + " actualizado.", Toast.LENGTH_LONG).show();
                                    }else{
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "Error al actualizar el estado del Pedido #"+pedidoSwiped.getNumero_pedido()+", verifica tu conexión a internet e intente de nuevo.", Toast.LENGTH_LONG).show();
                                    }
                                    }
                                });
                            }

                        }

                    }
                }
            }

            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                if (pedidos.size() > 0) {

                    Pedido pedido_deslizado = pedidos.get(viewHolder.getAdapterPosition());
                    if (pedido_deslizado != null) {
                        String pedidoEstado = pedido_deslizado.getEstado();

                        if (pedidoEstado.equals(PedidoUtils.STATE_PROCESS_CANCELLED) || pedidoEstado.equals(PedidoUtils.STATE_PROCESS_5) || pedidoEstado.equals(PedidoUtils.STATE_PROCESS_1)) {
                            return 0;
                        } else {
                            return super.getSwipeDirs(recyclerView, viewHolder);
                        }
                    } else return 0;

                } else return 0;

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    // Get RecyclerView item from the ViewHolder
                    View itemView = viewHolder.itemView;

                    /**  Atributos del slide  **/
                    Pedido pedido_deslizado = pedidos.get(viewHolder.getAdapterPosition());
                    if (pedido_deslizado != null) {
                        String pedidoState = pedido_deslizado.getEstado();
                        String pedidoNextState = "Cambiar estado a: ";

                        int background_color = R.color.colorGreen;
                        int icon_resource = R.drawable.ic_baseline_check_24;

                        if (pedidoState.equals(PedidoUtils.STATE_PROCESS_2)) {

                            background_color = R.color.colorOrange;
                            icon_resource = R.drawable.ic_baseline_call_received_24;
                            pedidoNextState += PedidoUtils.STATE_PROCESS_3;

                        } else if (pedidoState.equals(PedidoUtils.STATE_PROCESS_3)) {

                            if (pedido_deslizado.getTipo_entrega().equals(PedidoUtils.OrderReceive_Mode1)) {

                                background_color = R.color.colorGreen;
                                icon_resource = R.drawable.ic_baseline_check_24;
                                pedidoNextState += PedidoUtils.STATE_PROCESS_5;

                            } else if (pedido_deslizado.getTipo_entrega().equals(PedidoUtils.OrderReceive_Mode2)) {

                                background_color = R.color.colorGreenLight;
                                icon_resource = R.drawable.ic_baseline_location_on_24;
                                pedidoNextState += PedidoUtils.STATE_PROCESS_4;

                            }


                        } else if (pedidoState.equals(PedidoUtils.STATE_PROCESS_4)) {

                            background_color = R.color.colorGreen;
                            icon_resource = R.drawable.ic_baseline_check_24;
                            pedidoNextState += PedidoUtils.STATE_PROCESS_5;


                        }

                        new RecyclerViewSwipeDecorator.Builder(context, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                                .addBackgroundColor(ContextCompat.getColor(context, background_color))
                                .addActionIcon(icon_resource)

                                .addSwipeRightLabel(pedidoNextState)
                                .create()
                                .decorate();

                    }


                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                }
            }

            private int convertDpToPx(int dp) {
                return Math.round(dp * (context.getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView_parent);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_pedido, parent, false);

        return new AdapterRecyclerView_Pedidos.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.pedido = pedidos.get(position);
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
                if (stateColor != -1) {
                    Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, drawableResStart);
                    if (unwrappedDrawable != null) {
                        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                        DrawableCompat.setTint(wrappedDrawable, stateColor);
                    }

                    holder.txt_pedidoState.setTextColor(stateColor);
                } else {
                    if(oldColors != null)
                    holder.txt_pedidoState.setTextColor(oldColors);

                }

                holder.txt_pedidoState.setCompoundDrawablesRelativeWithIntrinsicBounds(drawableResStart, 0, 0, 0);
                holder.txt_pedidoState.setText(estado_text);
                holder.txt_precioTotal.setText(UtilFunctions.DoubleToPrecioFormat(holder.pedido.getPago_total()));

                if (pedido_estado.equals(PedidoUtils.STATE_PROCESS_1)) {
                    Animation shake;
                    shake = AnimationUtils.loadAnimation(context, R.anim.shake);

                    if (position == 0) {
                        if (countDownTimer == null) {
                            countDownTimer = new CountDownTimer(240000, 7000) {
                                @Override
                                public void onTick(long l) {

                                    mediaPlayer.start();
                                    holder.itemView.startAnimation(shake);
                                }

                                @Override
                                public void onFinish() {

                                }
                            };
                            countDownTimer.start();
                        }


                    }

                    /** //Start down counter
                     holder.txt_precioTotal.setVisibility(View.GONE);
                     /**holder.txt_expiration_counter.setVisibility(View.VISIBLE);


                     long remainingTimeInMillis = 65000;
                     long countDownInterval = 1000;

                     //update timer with saherd preferences item list values.
                     if (pedidosNuevos_countDownTimer_sPreferences.size() > 0) {
                     int best_position_for_pref =  -1;
                     // get best position.
                     String holderPedido_id = holder.pedido.getNumero_pedido();
                     for(int i = 0; i < pedidosNuevos_countDownTimer_sPreferences.size(); i++){
                     Pedido_CountDownTimer_SPreferences pedidoCountDOwnTimePref = pedidosNuevos_countDownTimer_sPreferences.get(i);
                     if(pedidoCountDOwnTimePref != null){

                     String pedidoPref_id = pedidoCountDOwnTimePref.getNumeroPedido();

                     if(holderPedido_id.equals(pedidoPref_id)){

                     best_position_for_pref = i;
                     break;
                     }

                     }
                     }

                     Pedido_CountDownTimer_SPreferences pedido_countDownTimer_sPreferences = pedidosNuevos_countDownTimer_sPreferences.get(best_position_for_pref);
                     if (pedido_countDownTimer_sPreferences.getmEndTime() == 0L) {

                     remainingTimeInMillis = (pedido_countDownTimer_sPreferences.getmTimeLeftInMillis());

                     CancelarPedido(holder.pedido, best_position_for_pref);

                     } else {
                     if (pedido_countDownTimer_sPreferences.getmEndTime() == -1) {
                     remainingTimeInMillis = pedido_countDownTimer_sPreferences.getmTimeLeftInMillis();
                     } else {

                     Long timeDiff = (pedido_countDownTimer_sPreferences.getmEndTime() - System.currentTimeMillis());
                     //to convert into positive number
                     timeDiff = Math.abs(timeDiff);

                     long timeDiffInSeconds = (timeDiff / 1000) % 60;
                     long timeDiffInMillis = timeDiffInSeconds * 1000;
                     Long timeDiffInMillisPlusTimerRemaining = remainingTimeInMillis = pedido_countDownTimer_sPreferences.getmTimeLeftInMillis() - timeDiffInMillis;

                     if (timeDiffInMillisPlusTimerRemaining < 0) {
                     timeDiffInMillisPlusTimerRemaining = Math.abs(timeDiffInMillisPlusTimerRemaining);
                     remainingTimeInMillis = START_TIME_IN_MILLIS - timeDiffInMillisPlusTimerRemaining;
                     CancelarPedido(holder.pedido, best_position_for_pref);
                     }
                     }

                     }

                     long finalRemainingTimeInMillis = remainingTimeInMillis;
                     if (pedidosNuevos_countDownTimerList.size() < pedidos.size()) {
                     int finalBest_position1 = best_position_for_pref;
                     CountDownTimer countDownTimer = new CountDownTimer(finalRemainingTimeInMillis, countDownInterval) {
                    @Override public void onTick(long l) {

                    OnCountTicked(l, holder, finalBest_position1, finalRemainingTimeInMillis, shake);

                    }


                    @Override public void onFinish() {
                    //Cancel pedido.
                    OnCountFinished(holder, finalBest_position1);
                    }
                    };
                     todo: countDownTimer.start();
                     PedidoCountDownTimer pedidoCountDownTimer = new PedidoCountDownTimer(holder.pedido.getNumero_pedido(), countDownTimer);
                     todo:pedidosNuevos_countDownTimerList.add(pedidoCountDownTimer);
                     } else {
                     int finalBest_position = best_position_for_pref;
                     CountDownTimer countDownTimer = new CountDownTimer(finalRemainingTimeInMillis, countDownInterval) {
                    @Override public void onTick(long l) {
                    OnCountTicked(l, holder, finalBest_position, finalRemainingTimeInMillis, shake);
                    }


                    @Override public void onFinish() {
                    //Cancel pedido.
                    OnCountFinished(holder, finalBest_position);
                    }
                    };
                     todo:countDownTimer.start();

                     PedidoCountDownTimer pedidoCountDownTimer = new PedidoCountDownTimer(holder.pedido.getNumero_pedido(), countDownTimer);
                     todo:getPedidosNuevos_countDownTimerList().set(finalBest_position, pedidoCountDownTimer);

                     }

                     }
                     **/

                } else {
                    holder.txt_precioTotal.setVisibility(View.VISIBLE);
                    holder.txt_expiration_counter.setVisibility(View.GONE);

                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GoToPedidoConsole(holder.pedido);
                    }
                });

            }

        }
    }

    private void GoToPedidoConsole(Pedido pedido) {
        if (pedido != null && context != null) {

            Intent i = new Intent(context, Activity_PedidoConsole.class);
            String pedidoJson = new Gson().toJson(pedido);
            i.putExtra("PedidoJson", pedidoJson);
            context.startActivity(i);

        }
    }

    private void OnCountFinished(ViewHolder holder, int position) {
        holder.txt_expiration_counter.setText("0 sec");
        sound_played = false;

        /***find position to remove.
         for(int i = 0; i< pedidosNuevos_countDownTimer_sPreferences.size(); i++){
         String pedidoID_InCOountDownTimerList = pedidosNuevos_countDownTimer_sPreferences.get(i).getNumeroPedido();
         for(int j = 0; j < pedidos.size() ; j++) {

         String HolderPedido_id = holder.pedido.getNumero_pedido();

         if(HolderPedido_id.equals(pedidoID_InCOountDownTimerList)){
         //remove this item.
         pedidosNuevos_countDownTimer_sPreferences.remove(j);

         break;
         }

         }

         }***/

        CancelarPedido(holder.pedido, position);
    }

    private void OnCountTicked(long l, ViewHolder holder, int position, long finalRemainingTimeInMillis, Animation shake) {

        sound_played = true;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(l);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(l);

        String minutesAndSecondsFormat = "" + String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(l),
                TimeUnit.MILLISECONDS.toSeconds(l) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));

        if (minutes == 0) {
            holder.txt_expiration_counter.setText(seconds + " sec");
        } else {
            holder.txt_expiration_counter.setText(minutesAndSecondsFormat);

        }

        //get the best position
        int best_position_for_pref = position;
        // get best position.
        String holderPedido_id = holder.pedido.getNumero_pedido();
        for (int i = 0; i < pedidosNuevos_countDownTimer_sPreferences.size(); i++) {
            Pedido_CountDownTimer_SPreferences pedidoCountDOwnTimePref = pedidosNuevos_countDownTimer_sPreferences.get(i);
            if (pedidoCountDOwnTimePref != null) {

                String pedidoPref_id = pedidoCountDOwnTimePref.getNumeroPedido();

                if (holderPedido_id.equals(pedidoPref_id)) {

                    best_position_for_pref = i;
                    break;
                }

            }
        }

        if (best_position_for_pref == 0) {
            if (l == finalRemainingTimeInMillis) {
                mediaPlayer.start();
                holder.itemView.startAnimation(shake);
            } else if (seconds % 7 == 0) {
                mediaPlayer.start();
                holder.itemView.startAnimation(shake);
            } else if (seconds <= 10) {
                if (seconds % 2 == 0) {
                    mediaPlayer.start();
                    holder.itemView.startAnimation(shake);
                }
            }
        }


        if (pedidosNuevos_countDownTimer_sPreferences != null && pedidosNuevos_countDownTimer_sPreferences.size() > 0) {


            Pedido_CountDownTimer_SPreferences pedido_countDownTimer_sPreferences = pedidosNuevos_countDownTimer_sPreferences.get(best_position_for_pref);
            pedido_countDownTimer_sPreferences.setmTimeLeftInMillis(l);
            pedido_countDownTimer_sPreferences.setRemainingTimeInMillis(l);

            pedidosNuevos_countDownTimer_sPreferences.set(best_position_for_pref, pedido_countDownTimer_sPreferences);
        }


    }

    public List<PedidoCountDownTimer> getPedidosNuevos_countDownTimerList() {
        return pedidosNuevos_countDownTimerList;
    }


    private void CancelarPedido(Pedido pedido, int position) {
        if (pedido != null) {

            //cancelar en cloud firestore.
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(FirebaseCloudFirestore_Collections.PEDIDOS).document(pedido.getOwner_firebase_uid() + "-" + pedido.getNumero_pedido()).update("estado", PedidoUtils.STATE_PROCESS_CANCELLED).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toast.makeText(context, "Pedido #" + pedido.getNumero_pedido() + " CANCELADO AUTOMATICAMENTE", Toast.LENGTH_LONG).show();

                }
            });
            //eliminar del shared preferences.
            //save in json.
            List<Pedido_CountDownTimer_SPreferences> pedidos_countDownTimer_sPreferences = new ArrayList<>(pedidosNuevos_countDownTimer_sPreferences);

            //find pedido to delete.
            int size = pedidos_countDownTimer_sPreferences.size();
            for (int i = 0; i < size; i++) {

                Pedido_CountDownTimer_SPreferences pedido_countDownTimer_sPreferences = pedidos_countDownTimer_sPreferences.get(i);
                if (pedido_countDownTimer_sPreferences != null) {
                    if (pedido_countDownTimer_sPreferences.getNumeroPedido().equals(pedido.getNumero_pedido())) {
                        pedidos_countDownTimer_sPreferences.remove(i);
                        break;
                    }
                }

            }

            String json_pedidosNuevosTimers = new Gson().toJson(pedidos_countDownTimer_sPreferences);

            SharedPreferences prefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putString(Pedido_CountDownTimer_SPreferences.SharedPreferenceJsonTimersName, json_pedidosNuevosTimers);
            editor.apply();
            //eliminar count down timer del pedido.
            if (pedidosNuevos_countDownTimerList.size() > 0) {
                for (int i = 0; i < pedidosNuevos_countDownTimerList.size(); i++) {
                    String pedidoCOuntDownTImer_id = pedidosNuevos_countDownTimerList.get(i).getNumeroPedido();
                    String HolderPedido_id = pedido.getNumero_pedido();

                    if (pedidoCOuntDownTImer_id.equals(HolderPedido_id)) {
                        CountDownTimer countDownTimer = pedidosNuevos_countDownTimerList.get(i).getCountDownTimer();
                        countDownTimer.cancel();
                        pedidosNuevos_countDownTimerList.remove(i);
                        break;
                    }

                }


            }

        }

    }

    @Override
    public int getItemCount() {
        if (pedidos != null) return pedidos.size();
        else {
            return 0;
        }
    }

    public void updateMValues(List<Pedido> mvalues) {
        this.pedidos = new ArrayList<>(mvalues);

        //todo: resume after this code.
        /** this.HayPedidosNuevos = PedidoUtils.HayUnPedidoNuevoEnEstaLista(pedidos);
         if (HayPedidosNuevos) UpdatePedidosNuevos_CountDownTimerPrefs();
         if (pedidos.size() <= 0) {
         if (pedidosNuevos_countDownTimerList.size() > 0) {
         for (int i = 0; i < pedidosNuevos_countDownTimerList.size(); i++) {
         CountDownTimer countDownTimer = pedidosNuevos_countDownTimerList.get(i).getCountDownTimer();
         countDownTimer.cancel();
         countDownTimer = null;

         }
         pedidosNuevos_countDownTimerList = new ArrayList<>();
         }
         }**/

        if (pedidos.size() == 0) {

            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer = null;
            }

        }

        notifyDataSetChanged();
    }

    private void UpdatePedidosNuevos_CountDownTimerPrefs() {

        pedidosNuevos_countDownTimer_sPreferences = new ArrayList<>();

        for (int i = 0; i < pedidos.size(); i++) {

            Pedido_CountDownTimer_SPreferences pedido_countDownTimer_sPreferences = new Pedido_CountDownTimer_SPreferences();
            pedido_countDownTimer_sPreferences.setNumeroPedido(pedidos.get(i).getNumero_pedido());

            int pedidoNuevos_countDownTimer_size = pedidosNuevos_countDownTimer_sPreferences.size();
            for (int j = 0; j < pedidoNuevos_countDownTimer_size; j++) {

                if (!pedidosNuevos_countDownTimer_sPreferences.get(j).getNumeroPedido().equals(pedido_countDownTimer_sPreferences.getNumeroPedido())) {
                    pedidosNuevos_countDownTimer_sPreferences.add(pedido_countDownTimer_sPreferences);
                }


            }
        }
        if (pedidosNuevos_countDownTimer_sPreferences != null) {


            if (pedidosNuevos_countDownTimer_sPreferences.size() > 0) {
                /***set end time.
                 for (int i = 0; i < pedidosNuevos_countDownTimer_sPreferences.size(); i++) {

                 Pedido_CountDownTimer_SPreferences pedido_countDownTimer = pedidosNuevos_countDownTimer_sPreferences.get(i);
                 pedido_countDownTimer.setmEndTime(System.currentTimeMillis());
                 pedidosNuevos_countDownTimer_sPreferences.set(i, pedido_countDownTimer);

                 }**/

                //save in json.
                String json_pedidosNuevosTimers = new Gson().toJson(pedidosNuevos_countDownTimer_sPreferences);

                SharedPreferences prefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                editor.putString(Pedido_CountDownTimer_SPreferences.SharedPreferenceJsonTimersName, json_pedidosNuevosTimers);
                editor.apply();

            }
        }

    }

    private void setPedidosNuevosCountDownTimers() {

        pedidosNuevos_countDownTimerList = new ArrayList<>();
        if (pedidos != null) {
            for (int i = 0; i < pedidos.size(); i++) {
                Pedido pedido = pedidos.get(i);


            }
        }


    }


    private void FillPedidosTimers_Prefs() {
        pedidosNuevos_countDownTimer_sPreferences = new ArrayList<>();

        for (int i = 0; i < pedidos.size(); i++) {

            Pedido_CountDownTimer_SPreferences pedido_countDownTimer_sPreferences = new Pedido_CountDownTimer_SPreferences();
            pedido_countDownTimer_sPreferences.setNumeroPedido(pedidos.get(i).getNumero_pedido());
            pedidosNuevos_countDownTimer_sPreferences.add(pedido_countDownTimer_sPreferences);
        }
    }

    public List<Pedido_CountDownTimer_SPreferences> getPedidosTimers() {
        return pedidosNuevos_countDownTimer_sPreferences;
    }

    public void setPedidosDownTimers(List<Pedido_CountDownTimer_SPreferences> pedidos_countDownTimer_sPreferences) {
        if (pedidos_countDownTimer_sPreferences.size() > 0) {
            this.pedidosNuevos_countDownTimer_sPreferences = pedidos_countDownTimer_sPreferences;
        } else {
            FillPedidosTimers_Prefs();
        }

        notifyDataSetChanged();
    }

    public void StopAlertSound() {
        if (countDownTimer != null) {

            countDownTimer.cancel();
            countDownTimer = null;
        }
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
            oldColors = txt_pedidoState.getTextColors();

        }
    }
}

