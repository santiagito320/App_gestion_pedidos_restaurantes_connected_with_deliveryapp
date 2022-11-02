package com.businessapp.restaurantorders.Frontend;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.businessapp.restaurantorders.Backend.utils.Adapters.AdapterRecyclerView_Pedidos;
import com.businessapp.restaurantorders.Backend.utils.FirebaseCloudFirestore_Collections;
import com.businessapp.restaurantorders.Backend.utils.PedidoCountDownTimer;
import com.businessapp.restaurantorders.Backend.utils.PedidoUtils;
import com.businessapp.restaurantorders.Backend.utils.Pedido_CountDownTimer_SPreferences;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Pedido;
import com.businessapp.restaurantorders.Backend.utils.UtilFunctions;
import com.businessapp.restaurantorders.R;
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
                List<DocumentSnapshot> documentSnapshots = value.getDocuments();
                List<Pedido> pedidos = new ArrayList<>();
                for(int i = 0; i< documentSnapshots.size(); i++){

                    DocumentSnapshot documentSnapshot = documentSnapshots.get(i);
                    Pedido pedido = documentSnapshot.toObject(Pedido.class);

                    pedidos.add(pedido);

                }
                Fragment_Pedidos.this.pedidos = new ArrayList<>(pedidos);
                ShowPedidosByState("All");
                ShowPedidosNuevos();
            }
        });


    }

    private void ShowPedidosNuevos() {
        AdapterRecyclerView_Pedidos adapterRecyclerView_pedidosNuevos = (AdapterRecyclerView_Pedidos) recyclerView_pedidosNuevos.getAdapter();
        if(adapterRecyclerView_pedidosNuevos != null){

            List<Pedido> pedidosNuevos_filtered = PedidoUtils.filterPedidosByState(pedidos,PedidoUtils.STATE_PROCESS_1);
            if(pedidosNuevos_filtered != null){

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
                        pedidos_state = PedidoUtils.STATE_PROCESS_5;
                        break;


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
        AdapterRecyclerView_Pedidos adapterRecyclerView_pedidos = new AdapterRecyclerView_Pedidos(context);
        recyclerView_pedidos.setAdapter(adapterRecyclerView_pedidos);

        //recycler view pedidos nuevos.
        recyclerView_pedidosNuevos = view.findViewById(R.id.recyclerView_pedidosNuevos);
        recyclerView_pedidosNuevos.setLayoutManager(new LinearLayoutManager(context));
        recyclerView_pedidosNuevos.setHasFixedSize(true);
        AdapterRecyclerView_Pedidos adapterRecyclerViewPedidosNuevos = new AdapterRecyclerView_Pedidos(context);
        recyclerView_pedidosNuevos.setAdapter(adapterRecyclerViewPedidosNuevos);
    }

    private void ShowPedidosByState(String state) {

        AdapterRecyclerView_Pedidos adapterRecyclerView_pedidos = (AdapterRecyclerView_Pedidos) recyclerView_pedidos.getAdapter();
        if(adapterRecyclerView_pedidos != null){

            List<Pedido> pedidos_filtered = PedidoUtils.filterPedidosByState(pedidos,state);
            if(pedidos_filtered != null){
                //remove pedidos nuevos with state 'Confirmando'
                pedidos_filtered = PedidoUtils.removeNewPedidos(pedidos_filtered);

                adapterRecyclerView_pedidos.updateMValues(pedidos_filtered);

            }

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
        if(adapterRecyclerView_pedidosNuevos != null){

            List<Pedido_CountDownTimer_SPreferences> pedido_countDownTimer_sPreferences = adapterRecyclerView_pedidosNuevos.getPedidosTimers();
            if(pedido_countDownTimer_sPreferences != null){


                if(pedido_countDownTimer_sPreferences.size() > 0){
                    //set end time.
                    for(int i = 0 ; i < pedido_countDownTimer_sPreferences.size(); i++){

                        Pedido_CountDownTimer_SPreferences pedido_countDownTimer = pedido_countDownTimer_sPreferences.get(i);
                        pedido_countDownTimer.setmEndTime(System.currentTimeMillis());
                        pedido_countDownTimer_sPreferences.set(i,pedido_countDownTimer);

                    }

                    //save in json.
                    String json_pedidosNuevosTimers = new Gson().toJson(pedido_countDownTimer_sPreferences);

                    SharedPreferences prefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putString(Pedido_CountDownTimer_SPreferences.SharedPreferenceJsonTimersName,json_pedidosNuevosTimers);
                    editor.apply();

                }
            }

            //Cancel pedidos nuevos CountDownTimers.
            List<PedidoCountDownTimer> pedidosNuevos_countDownTimers = adapterRecyclerView_pedidosNuevos.getPedidosNuevos_countDownTimerList();
            if(pedidosNuevos_countDownTimers != null){
                if(pedidosNuevos_countDownTimers.size() > 0){

                    for(int i = 0; i < pedidosNuevos_countDownTimers.size(); i++){

                        CountDownTimer countDownTimer = pedidosNuevos_countDownTimers.get(i).getCountDownTimer();
                        if(countDownTimer != null){
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
        ShowPedidosNuevos();

    }

    private void ResumePedidosNuevosCountDownTimers() {
        //Cancel pedidos nuevos CountDownTimers.
        AdapterRecyclerView_Pedidos adapterRecyclerView_pedidosNuevos = (AdapterRecyclerView_Pedidos) recyclerView_pedidosNuevos.getAdapter();
        if(adapterRecyclerView_pedidosNuevos != null){

            List<PedidoCountDownTimer> pedidosNuevos_countDownTimers = adapterRecyclerView_pedidosNuevos.getPedidosNuevos_countDownTimerList();
            if(pedidosNuevos_countDownTimers != null){
                if(pedidosNuevos_countDownTimers.size() > 0){

                    for(int i = 0; i < pedidosNuevos_countDownTimers.size(); i++){

                        CountDownTimer countDownTimer = pedidosNuevos_countDownTimers.get(i).getCountDownTimer();
                        if(countDownTimer != null){
                            countDownTimer.start();
                        }

                    }

                }
            }

        }

    }

    private void setNuevosPedidosDownTimers() {
        AdapterRecyclerView_Pedidos adapterRecyclerView_pedidosNuevos = (AdapterRecyclerView_Pedidos) recyclerView_pedidosNuevos.getAdapter();
        if(adapterRecyclerView_pedidosNuevos != null){

            SharedPreferences prefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
            if(prefs != null){

                String json_nuevosPedidosTimersStates = prefs.getString(Pedido_CountDownTimer_SPreferences.SharedPreferenceJsonTimersName,"[]");
                List<Pedido_CountDownTimer_SPreferences> pedidos_countDownTimer_sPreferences = UtilFunctions.ParseJsonListToAnyObjectList(json_nuevosPedidosTimersStates,new TypeToken<ArrayList<Pedido_CountDownTimer_SPreferences>>() {
                }.getType());

                if(pedidos_countDownTimer_sPreferences != null){


                    //update in adapter.
                    adapterRecyclerView_pedidosNuevos.setPedidosDownTimers(pedidos_countDownTimer_sPreferences);

                }
            }


        }
    }
}