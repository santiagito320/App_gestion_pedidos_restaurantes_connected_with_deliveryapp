package com.businessapp.restaurantorders.Frontend.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.businessapp.restaurantorders.Backend.Providers.ProviderNegocioGeneral;
import com.businessapp.restaurantorders.Backend.utils.Adapters.AdapterRecyclerView_Pedidos;
import com.businessapp.restaurantorders.Backend.utils.Constantes;
import com.businessapp.restaurantorders.Backend.utils.FirebaseCloudFirestore_Collections;
import com.businessapp.restaurantorders.Backend.utils.PedidoCountDownTimer;
import com.businessapp.restaurantorders.Backend.utils.PedidoUtils;
import com.businessapp.restaurantorders.Backend.utils.Pedido_CountDownTimer_SPreferences;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Pedido;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Sucursal;
import com.businessapp.restaurantorders.Backend.utils.UtilFunctions;
import com.businessapp.restaurantorders.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_Pedidos extends Fragment {
    private Activity activity_parent;
    private Context context;
    //Views.
    private TabLayout tabLayout_pedidos;
    private RecyclerView recyclerView_pedidos;
    private RecyclerView recyclerView_pedidosNuevos;
    private ConstraintLayout constraintLayout_pedidosGone;
    private LinearLayout linearLayout_contentPedidos;
    private FloatingActionButton fab_controlstore;

    //Data.
    private List<Pedido> pedidos = new ArrayList<>();

    public Fragment_Pedidos() {
        // Required empty public constructor
    }

    public static Fragment_Pedidos newInstance(String param1, String param2) {
        Fragment_Pedidos fragment = new Fragment_Pedidos();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetListenerToPedidos();
    }

    private void SetListenerToPedidos() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(FirebaseCloudFirestore_Collections.PEDIDOS).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (value != null) {
                    List<DocumentSnapshot> documentSnapshots = value.getDocuments();

                    if (documentSnapshots.size() > 0) {

                        List<Pedido> pedidos = new ArrayList<>();
                        for (int i = 0; i < documentSnapshots.size(); i++) {

                            DocumentSnapshot documentSnapshot = documentSnapshots.get(i);
                            Pedido pedido = documentSnapshot.toObject(Pedido.class);

                            pedidos.add(pedido);

                        }
                        pedidos = UtilFunctions.FilterPedidosBySucursalRemitente(pedidos);

                        Fragment_Pedidos.this.pedidos = new ArrayList<>(pedidos);

                        String state_tab_selected = "All";
                        int tab_selected_pos = tabLayout_pedidos.getSelectedTabPosition();
                        switch (tab_selected_pos) {

                            case 0:
                                state_tab_selected = "All";
                                break;

                            case 1:
                                state_tab_selected = PedidoUtils.STATE_PROCESS_1;
                                break;

                            case 2:
                                state_tab_selected = PedidoUtils.STATE_PROCESS_2;
                                break;

                            case 3:
                                state_tab_selected = PedidoUtils.STATE_PROCESS_3;
                                break;

                            case 4:
                                state_tab_selected = PedidoUtils.STATE_PROCESS_5;


                        }

                        if (pedidos.size() == 0) {
                            SHowGonePedidosView(true);
                        } else SHowGonePedidosView(false);

                        ShowPedidosByState(state_tab_selected);
                    } else {
                        pedidos = new ArrayList<>();
                        SHowGonePedidosView(true);
                        ShowPedidosByState("All");
                    }

                }


            }
        });


    }

    private void SHowGonePedidosView(boolean show) {

        if (constraintLayout_pedidosGone != null) {

            int visibility_pedidosGoneLayout = show ? View.VISIBLE : View.GONE;
            int visibility_linearContent_pedidos = !show ? View.VISIBLE : View.GONE;
            //constraint pedidos gone.
            constraintLayout_pedidosGone.setVisibility(visibility_pedidosGoneLayout);
            LottieAnimationView lottieAnimationView = constraintLayout_pedidosGone.findViewById(R.id.lottie_no_orders);
            lottieAnimationView.playAnimation();
            //linear content pedidos.
            if (linearLayout_contentPedidos != null)
                linearLayout_contentPedidos.setVisibility(visibility_linearContent_pedidos);
        }

    }

    private void ShowPedidosNuevos() {
        recyclerView_pedidosNuevos.setVisibility(View.VISIBLE);

        AdapterRecyclerView_Pedidos adapterRecyclerView_pedidos = (AdapterRecyclerView_Pedidos) recyclerView_pedidosNuevos.getAdapter();
        if (adapterRecyclerView_pedidos != null) adapterRecyclerView_pedidos.notifyDataSetChanged();

        AdapterRecyclerView_Pedidos adapterRecyclerView_pedidosNuevos = (AdapterRecyclerView_Pedidos) recyclerView_pedidosNuevos.getAdapter();
        if (adapterRecyclerView_pedidosNuevos != null) {

            List<Pedido> pedidosNuevos_filtered = PedidoUtils.filterPedidosByState(pedidos, PedidoUtils.STATE_PROCESS_1);
            if (pedidosNuevos_filtered != null) {

                adapterRecyclerView_pedidosNuevos.updateMValues(pedidosNuevos_filtered);
                setNuevosPedidosDownTimers();

            }

        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity_parent = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragmentt__pedidos, container, false);

        initVIews(view);

        return view;
    }

    private void initVIews(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        Sucursal sucursal_seleccionado = Constantes.MiRestaurante_Seleccionado;
        if (sucursal_seleccionado != null) {
            String sucursal_nombre = sucursal_seleccionado.getNombre();
            if (sucursal_nombre.equals("")) toolbar.setSubtitle("Local ( No especificado )");

            toolbar.setSubtitle("Local ( " + sucursal_seleccionado.getNombre() + " )");
        }
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                int item_id = menuItem.getItemId();

                switch (item_id) {

                    case R.id.action_borrarpedidos:
                        PedidoUtils.BorrarTodosLosPedidos(pedidos, context);
                        break;

                    case R.id.action_pedido_de_prueba:
                        PedidoUtils.GenerarPedidoDePrueba(context);
                        break;

                    default:
                        return false;
                }

                return true;
            }
        });

        tabLayout_pedidos = view.findViewById(R.id.tabLayout_pedidos);
        tabLayout_pedidos.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String pedidos_state = "All";

                switch (tab.getPosition()) {

                    case 0:
                        pedidos_state = "All";
                        break;

                    case 1:
                        pedidos_state = PedidoUtils.STATE_PROCESS_1;
                        break;

                    case 2:
                        pedidos_state = PedidoUtils.STATE_PROCESS_2;
                        break;

                    case 3:
                        pedidos_state = PedidoUtils.STATE_PROCESS_3;
                        break;

                    case 4:
                        pedidos_state = PedidoUtils.STATE_PROCESS_5;


                }

                ShowPedidosByState(pedidos_state);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        recyclerView_pedidos = view.findViewById(R.id.recyclerView_pedidos);
        recyclerView_pedidos.setLayoutManager(new LinearLayoutManager(context));
        recyclerView_pedidos.setHasFixedSize(true);
        AdapterRecyclerView_Pedidos adapterRecyclerView_pedidos = new AdapterRecyclerView_Pedidos(context, recyclerView_pedidos);
        recyclerView_pedidos.setAdapter(adapterRecyclerView_pedidos);

        //recycler view pedidos nuevos.
        recyclerView_pedidosNuevos = view.findViewById(R.id.recyclerView_pedidosNuevos);
        recyclerView_pedidosNuevos.setLayoutManager(new LinearLayoutManager(context));
        recyclerView_pedidosNuevos.setHasFixedSize(true);
        AdapterRecyclerView_Pedidos adapterRecyclerViewPedidosNuevos = new AdapterRecyclerView_Pedidos(context, recyclerView_pedidos);
        recyclerView_pedidosNuevos.setAdapter(adapterRecyclerViewPedidosNuevos);
        //constraint gone pedidos.
        constraintLayout_pedidosGone = view.findViewById(R.id.constraint_content_pedidosGone);
        //linear content pedidos.
        linearLayout_contentPedidos = view.findViewById(R.id.linearContent_pedidos);

        fab_controlstore = view.findViewById(R.id.fab_controlStore);
        fab_controlstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constantes.MiRestaurante_Seleccionado != null) {
                    boolean isOpen = Constantes.MiRestaurante_Seleccionado.isAbierto();
                    String sucursalNombre = Constantes.MiRestaurante_Seleccionado.getNombre();

                    String title = isOpen ? "Cerrar sucursal (" + sucursalNombre + ")" : "Abrir sucursal (" + sucursalNombre + ")";
                    String message = isOpen ? "Al cerrar servicio los clientes ya no podrán emitir pedidos a la consola. ¿Deseas continuar?" : "Al abrir servicio los clientes podrán emitir pedidos a la consola. ¿Deseas continuar?";
                    int fabRes = !isOpen ? R.drawable.ic_baseline_lock_24_white : R.drawable.ic_baseline_storefront_24_white;
                    int dialog_icon = isOpen ? R.drawable.ic_baseline_lock_24 : R.drawable.ic_baseline_storefront_24;


                    new AlertDialog.Builder(context).setTitle(title).setMessage(message).setIcon(dialog_icon).setPositiveButton(title, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //set block icon.
                            //action close store.
                            ProviderNegocioGeneral providerNegocioGeneral = new ProviderNegocioGeneral(context);
                            providerNegocioGeneral.ControlSucursalStatus(Constantes.MiRestaurante_Seleccionado.getNombre(), !isOpen);

                            fab_controlstore.setImageResource(fabRes);

                            dialogInterface.dismiss();
                        }
                    }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
                }

            }
        });

        if (Constantes.MiRestaurante_Seleccionado != null) {
            ConfigureControlStoreFab();
        }

    }

    private void ConfigureControlStoreFab() {
        if(Constantes.MiRestaurante_Seleccionado != null){
            boolean isOpen = Constantes.MiRestaurante_Seleccionado.isAbierto();
            int fabRes = isOpen ? R.drawable.ic_baseline_lock_24_white : R.drawable.ic_baseline_storefront_24_white;
            fab_controlstore.setImageResource(fabRes);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constantes.MiRestaurante_Seleccionado != null) {
            ConfigureControlStoreFab();
        }
    }

    private void ShowPedidosByState(String state) {

        if (state.equals(PedidoUtils.STATE_PROCESS_1)) {
            recyclerView_pedidos.setVisibility(View.GONE);

            ShowPedidosNuevos();
        } else {
            recyclerView_pedidos.setVisibility(View.VISIBLE);
            if (state.equals("All")) ShowPedidosNuevos();
            else HidePedidosNuevos();
            AdapterRecyclerView_Pedidos adapterRecyclerView_pedidos = (AdapterRecyclerView_Pedidos) recyclerView_pedidos.getAdapter();
            if (adapterRecyclerView_pedidos != null) {

                List<Pedido> pedidos_filtered = PedidoUtils.filterPedidosByState(pedidos, state);
                if (pedidos_filtered != null) {
                    //remove pedidos nuevos with state 'Confirmando'
                    pedidos_filtered = PedidoUtils.removeNewPedidos(pedidos_filtered);


                    adapterRecyclerView_pedidos.updateMValues(pedidos_filtered);
                    adapterRecyclerView_pedidos.notifyDataSetChanged();

                }

            }
        }

    }

    private void HidePedidosNuevos() {
        recyclerView_pedidosNuevos.setVisibility(View.GONE);
        AdapterRecyclerView_Pedidos adapterRecyclerView_pedidos = (AdapterRecyclerView_Pedidos) recyclerView_pedidosNuevos.getAdapter();
        if (adapterRecyclerView_pedidos != null) {
            adapterRecyclerView_pedidos.StopAlertSound();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        SaveNuevosPedidosDownTimersStates();
    }

    private void SaveNuevosPedidosDownTimersStates() {
        //save timers of new orders in shared preferences
        AdapterRecyclerView_Pedidos adapterRecyclerView_pedidosNuevos = (AdapterRecyclerView_Pedidos) recyclerView_pedidosNuevos.getAdapter();
        if (adapterRecyclerView_pedidosNuevos != null) {

            List<Pedido_CountDownTimer_SPreferences> pedido_countDownTimer_sPreferences = adapterRecyclerView_pedidosNuevos.getPedidosTimers();
            if (pedido_countDownTimer_sPreferences != null) {


                if (pedido_countDownTimer_sPreferences.size() > 0) {
                    //set end time.
                    for (int i = 0; i < pedido_countDownTimer_sPreferences.size(); i++) {

                        Pedido_CountDownTimer_SPreferences pedido_countDownTimer = pedido_countDownTimer_sPreferences.get(i);
                        pedido_countDownTimer.setmEndTime(System.currentTimeMillis());
                        pedido_countDownTimer_sPreferences.set(i, pedido_countDownTimer);

                    }

                    //save in json.
                    String json_pedidosNuevosTimers = new Gson().toJson(pedido_countDownTimer_sPreferences);

                    SharedPreferences prefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putString(Pedido_CountDownTimer_SPreferences.SharedPreferenceJsonTimersName, json_pedidosNuevosTimers);
                    editor.apply();

                }
            }

            //Cancel pedidos nuevos CountDownTimers.
            List<PedidoCountDownTimer> pedidosNuevos_countDownTimers = adapterRecyclerView_pedidosNuevos.getPedidosNuevos_countDownTimerList();
            if (pedidosNuevos_countDownTimers != null) {
                if (pedidosNuevos_countDownTimers.size() > 0) {

                    for (int i = 0; i < pedidosNuevos_countDownTimers.size(); i++) {

                        CountDownTimer countDownTimer = pedidosNuevos_countDownTimers.get(i).getCountDownTimer();
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }

                    }

                }
            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //ShowPedidosNuevos();

    }

    private void ResumePedidosNuevosCountDownTimers() {
        //Cancel pedidos nuevos CountDownTimers.
        AdapterRecyclerView_Pedidos adapterRecyclerView_pedidosNuevos = (AdapterRecyclerView_Pedidos) recyclerView_pedidosNuevos.getAdapter();
        if (adapterRecyclerView_pedidosNuevos != null) {

            List<PedidoCountDownTimer> pedidosNuevos_countDownTimers = adapterRecyclerView_pedidosNuevos.getPedidosNuevos_countDownTimerList();
            if (pedidosNuevos_countDownTimers != null) {
                if (pedidosNuevos_countDownTimers.size() > 0) {

                    for (int i = 0; i < pedidosNuevos_countDownTimers.size(); i++) {

                        CountDownTimer countDownTimer = pedidosNuevos_countDownTimers.get(i).getCountDownTimer();
                        if (countDownTimer != null) {
                            countDownTimer.start();
                        }

                    }

                }
            }

        }

    }

    private void setNuevosPedidosDownTimers() {
        AdapterRecyclerView_Pedidos adapterRecyclerView_pedidosNuevos = (AdapterRecyclerView_Pedidos) recyclerView_pedidosNuevos.getAdapter();
        if (adapterRecyclerView_pedidosNuevos != null) {

            SharedPreferences prefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
            if (prefs != null) {

                String json_nuevosPedidosTimersStates = prefs.getString(Pedido_CountDownTimer_SPreferences.SharedPreferenceJsonTimersName, "[]");
                List<Pedido_CountDownTimer_SPreferences> pedidos_countDownTimer_sPreferences = UtilFunctions.ParseJsonListToAnyObjectList(json_nuevosPedidosTimersStates, new TypeToken<ArrayList<Pedido_CountDownTimer_SPreferences>>() {
                }.getType());

                if (pedidos_countDownTimer_sPreferences != null) {

                    //update in adapter.
                    adapterRecyclerView_pedidosNuevos.setPedidosDownTimers(pedidos_countDownTimer_sPreferences);

                }
            }


        }
    }
}