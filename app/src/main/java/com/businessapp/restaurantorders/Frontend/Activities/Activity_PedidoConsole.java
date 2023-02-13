package com.businessapp.restaurantorders.Frontend.Activities;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.businessapp.restaurantorders.Backend.Providers.ProviderPedidos;
import com.businessapp.restaurantorders.Backend.utils.Adapters.AdapterRecyclerView_ProductosBasicView;
import com.businessapp.restaurantorders.Backend.utils.Bluetooth.BluetoothPrint;
import com.businessapp.restaurantorders.Backend.utils.Constantes;
import com.businessapp.restaurantorders.Backend.utils.PedidoUtils;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Destinatario;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Direccion;
import com.businessapp.restaurantorders.Backend.utils.Pojos.NegocioGeneral;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Pedido;
import com.businessapp.restaurantorders.Backend.utils.UtilFunctions;
import com.businessapp.restaurantorders.Backend.utils.utils.UiFunctions;
import com.businessapp.restaurantorders.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Activity_PedidoConsole extends AppCompatActivity {
    //data.
    private Pedido pedido;
    private ProviderPedidos providerPedidos;
    //Views.
    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView txtBornTime, txt_pedido_id, txt_tipoEntrega, txt_modo_de_pago, txt_cliente_nombre, txt_clienteTelefono, txt_cliente_email, txt_clienteDireccion;
    private ImageButton imgbtn_arrowDown;
    private CardView cardContent_clienteDireccion, cardContent_cliente,cardView_change_basic_pedidoOptions;
    private RecyclerView recyclerView_productos;
    private TextView txt_subTotal, txt_costo_de_envio, txt_total;
    private View view_pedidoState_background;
    private TextView txt_pedidoState;
    private ImageButton imgbtn_cancelPedido;
    private Button btn_acceptPedido;
    private Toolbar toolbar_pedidoOptions;
    private View contentView;
    //Utils.
    private boolean views_initialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__pedido_console);

        getData();
        initViews();
    }

    private void initViews() {
        contentView = findViewById(android.R.id.content);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);

        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //txt pedido id.
        txt_pedido_id = findViewById(R.id.txt_numero_pedido);

        //txt pedido borntime.
        txtBornTime = findViewById(R.id.txt_bornTime);

        // txt tipo de entrega.
        txt_tipoEntrega = findViewById(R.id.txt_tipo_de_entrega);
        //txt modo de pago.
        txt_modo_de_pago = findViewById(R.id.txt_modo_de_pago);
        // img btn arrow down.
        imgbtn_arrowDown = findViewById(R.id.imgbtn_arrowDown);
        imgbtn_arrowDown.setTag("Closed");
        //txt cliente nombre.
        txt_cliente_nombre = findViewById(R.id.txt_cliente_nombre);
        //txt cliente telefono.
        txt_clienteTelefono = findViewById(R.id.txt_cliente_telefono);
        //txt cliente email
        txt_cliente_email = findViewById(R.id.txt_cliente_email);
        //txt cliente direccion.
        txt_clienteDireccion = findViewById(R.id.txt_clienteDireccion);

        //card content cliente direccion
        cardContent_clienteDireccion = findViewById(R.id.card_content_cliente_direccion);
        //Card content cliente.
        cardContent_cliente = findViewById(R.id.cardContent_cliente);
        cardContent_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String state = imgbtn_arrowDown.getTag().toString();
                LinearLayout linearLayoutContent_cliente_details = findViewById(R.id.linearLayoutContent_cliente_details);
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
                linearLayoutContent_cliente_details.setVisibility(content_visibility);
                imgbtn_arrowDown.setImageResource(imgbtn_arrow_res);
                imgbtn_arrowDown.setTag(state);


            }
        });
        //recycler view productos.
        recyclerView_productos = findViewById(R.id.recyclerView_productos);
        recyclerView_productos.setLayoutManager(new LinearLayoutManager(Activity_PedidoConsole.this));
        recyclerView_productos.setHasFixedSize(true);
        AdapterRecyclerView_ProductosBasicView adapterRecyclerView_productosBasicView = new AdapterRecyclerView_ProductosBasicView(Activity_PedidoConsole.this, new ArrayList<>());
        recyclerView_productos.setAdapter(adapterRecyclerView_productosBasicView);

        //txt subttotal.
        txt_subTotal = findViewById(R.id.txt_subTotal);
        //txt costo total.
        txt_total = findViewById(R.id.txt_costo_total);
        // txt costo de envio.
        txt_costo_de_envio = findViewById(R.id.txt_costo_de_envio);
        // view pedido background.
        view_pedidoState_background = findViewById(R.id.view_pedidoState_background);
        //txt pedido state.
        txt_pedidoState = findViewById(R.id.txt_pedidoState);
        //img btn cancel pedido.
        imgbtn_cancelPedido = findViewById(R.id.imageButton_cancelPedido);
        //button accept pedido.
        btn_acceptPedido = findViewById(R.id.button_acepptPedido);

        views_initialized = true;
        //toolbar pedido options.
        toolbar_pedidoOptions = findViewById(R.id.toolbar_pedidoOptions);

        //card view change pedido basic states.
        cardView_change_basic_pedidoOptions = findViewById(R.id.cardView_change_basic_pedidoStates);
    }

    private void getData() {
        /**get intent data.**/
        Intent i = getIntent();
        if (i != null) {
            Bundle extras = i.getExtras();
            if (extras != null) {

                String pedidoJson = extras.getString("PedidoJson");
                if (pedidoJson != null) {
                    this.pedido = new Gson().fromJson(pedidoJson, Pedido.class);


                }

            }
        }
        providerPedidos = new ProviderPedidos(Activity_PedidoConsole.this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        setPedidoListener();
        setDataInViews();
    }

    private void setPedidoListener() {

        if(pedido != null){

            String id = pedido.getOwner_firebase_uid() + "-" + pedido.getNumero_pedido();
            providerPedidos.getPedido(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                    if(value != null){

                        Pedido pedido = value.toObject(Pedido.class);

                        if(pedido != null){

                            Activity_PedidoConsole.this.pedido = pedido;
                            setDataInViews();

                        }

                    }

                }
            });

        }

    }

    private void setDataInViews() {
        if (pedido != null && views_initialized) {

            actionBar.setTitle(UtilFunctions.DoubleToPrecioFormat(pedido.getPago_total()));

            txt_pedido_id.setText(pedido.getNumero_pedido());
            txtBornTime.setText(UtilFunctions.TimestampToDate(pedido.getBorn_timestamp()));

            txt_tipoEntrega.setText(pedido.getTipo_entrega());
            txt_modo_de_pago.setText(pedido.getMedio_de_pago());

            Destinatario destinatario = pedido.getDestinatario();
            if (destinatario != null) {

                txt_cliente_nombre.setText(destinatario.getNombre());
                txt_clienteTelefono.setText(destinatario.getTelefono());
                txt_cliente_email.setText(destinatario.getEmail());

                Direccion direccion_seleccionada = UtilFunctions.getDireccionSeleccionada(destinatario.getDirecciones());
                if (direccion_seleccionada != null) {

                    String numInterior = direccion_seleccionada.getNum_interior();
                    txt_clienteDireccion.setText(direccion_seleccionada.getCalle() + " #" + direccion_seleccionada.getNum_exterior() + ", " + (!numInterior.equals("") ? " °num interior: " + numInterior : "") + direccion_seleccionada.getColonia() + ", CP." + direccion_seleccionada.getCodigo_postal() + ", " + direccion_seleccionada.getCiudad());

                    cardContent_clienteDireccion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            UtilFunctions.openGoogleMapsWithLocation(Activity_PedidoConsole.this, direccion_seleccionada.getLatitud(), direccion_seleccionada.getLongitud(), direccion_seleccionada.getAliasdireccion());

                        }
                    });

                }
            }

            /** Print productos in recyclerView **/
            AdapterRecyclerView_ProductosBasicView adapterRecyclerView_productosBasicView = (AdapterRecyclerView_ProductosBasicView) recyclerView_productos.getAdapter();
            if (adapterRecyclerView_productosBasicView != null) {

                adapterRecyclerView_productosBasicView.update_mValues(pedido.getProductos());

            }

            double productos_subTotal = UtilFunctions.sumAllProductosPrices(pedido.getProductos());
            txt_subTotal.setText(UtilFunctions.DoubleToPrecioFormat(productos_subTotal));
            txt_total.setText(UtilFunctions.DoubleToPrecioFormat(pedido.getPago_total()));


            int pedidoState_colorResource = R.color.colorGrisOscuro;
            int pedidoState_textColor = R.color.colorWhite;
            int pedidoState_drawableRes = R.drawable.ic_baseline_access_time_24;
            String state_text = pedido.getEstado();

            if(pedido.getEstado().equals(PedidoUtils.STATE_PROCESS_1)){

                pedidoState_colorResource = R.color.colorGrisOscuro;
                pedidoState_textColor = R.color.colorWhite;

                pedidoState_drawableRes = R.drawable.ic_baseline_access_time_24;

            }else  if(pedido.getEstado().equals(PedidoUtils.STATE_PROCESS_2)){

                pedidoState_colorResource = R.color.colorRed;
                pedidoState_textColor = R.color.colorRed;

                pedidoState_drawableRes = R.drawable.ic_baseline_restaurant_24;

            }else if(pedido.getEstado().equals(PedidoUtils.STATE_PROCESS_3)){

                pedidoState_colorResource = R.color.colorOrange;
                pedidoState_textColor = R.color.colorOrange;

                pedidoState_drawableRes = R.drawable.ic_baseline_call_received_24;

            }else if(pedido.getEstado().equals(PedidoUtils.STATE_PROCESS_4)){

                pedidoState_colorResource = R.color.colorGreenLight;
                pedidoState_textColor = R.color.colorGreenLight;

                pedidoState_drawableRes = R.drawable.ic_baseline_location_on_24;

            }else if(pedido.getEstado().equals(PedidoUtils.STATE_PROCESS_5)){

                pedidoState_colorResource = R.color.colorGreen;
                pedidoState_textColor = R.color.colorGreen;

                pedidoState_drawableRes = R.drawable.ic_baseline_check_24;

            }else if(pedido.getEstado().equals(PedidoUtils.STATE_PROCESS_CANCELLED)) {

                pedidoState_colorResource = R.color.colorRed;
                pedidoState_textColor = R.color.colorRed;

                pedidoState_drawableRes = R.drawable.ic_baseline_close_24;

            }else if(pedido.getEstado().equals(PedidoUtils.STATE_PROCESS_0)){

                pedidoState_colorResource = R.color.colorRed;
                pedidoState_textColor = R.color.colorRed;

                pedidoState_drawableRes = R.drawable.ic_baseline_warning_24;
            }

            Resources resources = getResources();
            view_pedidoState_background.setBackgroundColor(resources.getColor(pedidoState_colorResource));
            txt_pedidoState.setText(state_text);
            txt_pedidoState.setTextColor(resources.getColor(pedidoState_textColor));

            txt_pedidoState.setCompoundDrawablesRelativeWithIntrinsicBounds(pedidoState_drawableRes,0,0,0);


            UtilFunctions.setTextViewDrawableColor(txt_pedidoState,pedidoState_textColor);

            imgbtn_cancelPedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    UiFunctions.showCancelPedidoFinalOptions(Activity_PedidoConsole.this,pedido);

                }
            });

            btn_acceptPedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    UiFunctions.showSetPedidoHoraDeEntrega(Activity_PedidoConsole.this, pedido);

                }
            });

            LinearLayout linearLayout_content_costo_de_envio = findViewById(R.id.linearLayout_content_costo_de_envio);

            if(linearLayout_content_costo_de_envio != null){

                NegocioGeneral negocioGeneral = Constantes.negocio;
                if (negocioGeneral != null) {

                    if(pedido.getTipo_entrega().equals(PedidoUtils.OrderReceive_Mode2)){

                        linearLayout_content_costo_de_envio.setVisibility(View.VISIBLE);
                        txt_costo_de_envio.setText(UtilFunctions.DoubleToPrecioFormat(negocioGeneral.getCosto_despacho()));

                    }else{

                        linearLayout_content_costo_de_envio.setVisibility(View.GONE);
                        txt_costo_de_envio.setText(UtilFunctions.DoubleToPrecioFormat(0));

                    }


                }


            }

            /**Bottom toolbar**/
            Menu toolbar_pedidoOptions_menu = toolbar_pedidoOptions.getMenu();
            if(toolbar_pedidoOptions_menu != null){

                MenuItem menuItem_cambiar_estado_del_pedido = toolbar_pedidoOptions_menu.getItem(0);
                MenuItem menuItem_imprimir = toolbar_pedidoOptions_menu.getItem(1);
                MenuItem menuItem_copiarAlPortapapeles = toolbar_pedidoOptions_menu.getItem(2);

                if(pedido.getEstado().equals(PedidoUtils.STATE_PROCESS_CANCELLED) || pedido.getEstado().equals(PedidoUtils.STATE_PROCESS_5)){

                    menuItem_cambiar_estado_del_pedido.setVisible(false);
                    menuItem_imprimir.setVisible(true);
                    menuItem_copiarAlPortapapeles.setVisible(true);
                }else{

                    if(menuItem_cambiar_estado_del_pedido != null){

                        menuItem_cambiar_estado_del_pedido.setVisible(true);

                        SubMenu subMenu_cambiarEstadoDelPedido = menuItem_cambiar_estado_del_pedido.getSubMenu();
                        if(subMenu_cambiarEstadoDelPedido != null){
                            MenuItem menuItem_cancelarPedido = subMenu_cambiarEstadoDelPedido.getItem(0);
                            MenuItem menuItem_preparandoPedido = subMenu_cambiarEstadoDelPedido.getItem(1);
                            MenuItem menuItem_esperandoASerEntregado = subMenu_cambiarEstadoDelPedido.getItem(2);
                            MenuItem menuItem_llego_al_domicilio = subMenu_cambiarEstadoDelPedido.getItem(3);
                            MenuItem menuItem_pedidoEntregado = subMenu_cambiarEstadoDelPedido.getItem(4);

                            String pedido_firebaseID = pedido.getOwner_firebase_uid()+"-"+pedido.getNumero_pedido();
                            menuItem_cancelarPedido.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {

                                    UiFunctions.showCancelPedidoFinalOptions(Activity_PedidoConsole.this,pedido);

                                    return true;
                                }
                            });
                            menuItem_preparandoPedido.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {

                                    providerPedidos.updatePedidoState(pedido_firebaseID,PedidoUtils.STATE_PROCESS_2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Snackbar.make(contentView,"Pedido #"+pedido.getNumero_pedido()+" en preparación",Snackbar.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Snackbar.make(contentView,"Falló al actualizar estado del (Pedido #"+pedido.getNumero_pedido()+"), verifica tu conexión a internet e intente de nuevo.",Snackbar.LENGTH_LONG).show();

                                        }
                                    });
                                    return true;
                                }
                            });
                            menuItem_esperandoASerEntregado.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {

                                    providerPedidos.updatePedidoState(pedido_firebaseID,PedidoUtils.STATE_PROCESS_3).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Snackbar.make(contentView,"Pedido #"+pedido.getNumero_pedido()+" esperando a ser entregado",Snackbar.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Snackbar.make(contentView,"Falló al actualizar estado del (Pedido #"+pedido.getNumero_pedido()+"), verifica tu conexión a internet e intente de nuevo.",Snackbar.LENGTH_LONG).show();

                                        }
                                    });
                                    return true;
                                }
                            });
                            menuItem_llego_al_domicilio.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {

                                    providerPedidos.updatePedidoState(pedido_firebaseID,PedidoUtils.STATE_PROCESS_4).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Snackbar.make(contentView,"Pedido #"+pedido.getNumero_pedido()+" llegó al domicilio",Snackbar.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Snackbar.make(contentView,"Falló al actualizar estado del (Pedido #"+pedido.getNumero_pedido()+"), verifica tu conexión a internet e intente de nuevo.",Snackbar.LENGTH_LONG).show();

                                        }
                                    });
                                    return true;
                                }
                            });
                            menuItem_pedidoEntregado.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {

                                    providerPedidos.updatePedidoState(pedido_firebaseID,PedidoUtils.STATE_PROCESS_5).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Snackbar.make(contentView,"Pedido #"+pedido.getNumero_pedido()+" entregado",Snackbar.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Snackbar.make(contentView,"Falló al actualizar estado del (Pedido #"+pedido.getNumero_pedido()+"), verifica tu conexión a internet e intente de nuevo.",Snackbar.LENGTH_SHORT).show();

                                        }
                                    });
                                    return true;
                                }
                            });


                            if(pedido.getTipo_entrega().equals(PedidoUtils.OrderReceive_Mode1)){
                                menuItem_llego_al_domicilio.setVisible(false);
                            }else if(pedido.getTipo_entrega().equals(PedidoUtils.OrderReceive_Mode2)){
                                menuItem_llego_al_domicilio.setVisible(true);
                            }

                            subMenu_cambiarEstadoDelPedido.setGroupVisible(0,true);

                        }
                    }

                }

                menuItem_imprimir.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                      /**  if(Constantes.bluetoothPrint != null){
                            if(Constantes.bluetoothPrint.checkConnection()){
                             //Print.

                                 Constantes.bluetoothPrint.printData(pedido);

                                ProgressDialog progressDialog = ProgressDialog.show(Activity_PedidoConsole.this, "Impresora térmica",
                                        "Generando recibo de compra...", true);



                                    new Handler(getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                        }
                                    },5000);


                                }else{
                                //Connnect
                                Intent i = new Intent(Activity_PedidoConsole.this,Activity_ImpresoraTermicaDetalles.class);
                                i.putExtra(new Gson().toJson(Constantes.bluetoothPrint),"BluetoothDevice");
                                startActivity(i);
                            }
                        }else{
                            //go to select bluetooth printers.
                            Intent i = new Intent(Activity_PedidoConsole.this, Activity_OrdersTermicPrinterConfiguration.class);
                            startActivity(i);
                        }**/
                        Toast.makeText(Activity_PedidoConsole.this, "Disponible para versiones posteriores", Toast.LENGTH_SHORT).show();

                        return true;
                    }
                });
                menuItem_copiarAlPortapapeles.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        UtilFunctions.copyPedidoNoteToClipboard(pedido,Activity_PedidoConsole.this);

                        Snackbar.make(contentView,"Copiado al portapapeles",Snackbar.LENGTH_SHORT).show();

                        return true;
                    }
                });

            }

            if(pedido.getEstado().equals(PedidoUtils.STATE_PROCESS_1)){

                toolbar_pedidoOptions.setVisibility(View.GONE);
                cardView_change_basic_pedidoOptions.setVisibility(View.VISIBLE);

            }else{

                toolbar_pedidoOptions.setVisibility(View.VISIBLE);

                cardView_change_basic_pedidoOptions.setVisibility(View.GONE);

            }


        }
    }
}