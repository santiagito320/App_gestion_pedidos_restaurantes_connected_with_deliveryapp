package com.businessapp.restaurantorders.Frontend.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.businessapp.restaurantorders.Backend.Notifications.NotificationSender;
import com.businessapp.restaurantorders.Backend.utils.Constantes;
import com.businessapp.restaurantorders.Backend.utils.DB.Entidades.Entidad_Restaurante;
import com.businessapp.restaurantorders.Backend.utils.DB.ViewModels.ViewModel_Restaurante;
import com.businessapp.restaurantorders.Backend.utils.FirebaseCloudFirestore_Collections;
import com.businessapp.restaurantorders.Backend.utils.Pojos.NegocioGeneral;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Sucursal;
import com.businessapp.restaurantorders.Backend.utils.UtilFunctions;
import com.businessapp.restaurantorders.Backend.utils.utils.UiFunctions;
import com.businessapp.restaurantorders.Frontend.Fragments.Fragment_Ajustes;
import com.businessapp.restaurantorders.Frontend.Fragments.Fragment_Pedidos;
import com.businessapp.restaurantorders.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        SetWifiConnectionListener();
        getRestaurantData();
    }

    @Override
    protected void onStop() {


        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("on key down");
        //notificar que el usuario necesita reingresar a la app.
        NotificationSender notificationSender = new NotificationSender();
        notificationSender.sendLocalNotification(MainActivity.this,getString(R.string.app_name),"¡Abre de nuevo la app para seguir tomando pedidos!",R.drawable.ic_baseline_warning_24);

        return super.onKeyDown(keyCode, event);

    }



    protected void onDestroy() {
        super.onDestroy();

    }

    private AlertDialog alertDialog_noConnection;
    private void SetWifiConnectionListener() {
    }
    BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = manager.getActiveNetworkInfo();

            if (ni != null) {

                if (ni.getState() == NetworkInfo.State.CONNECTED) {
                    //CONNECTED.
                    if(alertDialog_noConnection != null){
                        if(alertDialog_noConnection.isShowing()){
                            alertDialog_noConnection.dismiss();
                        }
                    }

                }else{
                    //DISCONNECTED.
                    if(alertDialog_noConnection == null){
                        AlertDialog.Builder dialogBuilder;
                        dialogBuilder = UiFunctions.ShowBasicAlertDialogWithButtons(context,"¡Sin conexión a internet!","Se requiere de una conexión estable a la red para seguir recibiendo pedidos, conectate de nuevo lo mas rápido posible.",R.drawable.ic_baseline_wifi_off_24,false);

                        alertDialog_noConnection = dialogBuilder.create();
                    }
                    alertDialog_noConnection.show();
                }

            }else{
                //DISCONNECTED.
                if(alertDialog_noConnection == null){
                    AlertDialog.Builder dialogBuilder;
                    dialogBuilder = UiFunctions.ShowBasicAlertDialogWithButtons(context,"¡Sin conexión a internet!","Se requiere de una conexión estable a la red para seguir recibiendo pedidos, conectate de nuevo lo mas rápido posible.",R.drawable.ic_baseline_wifi_off_24,false);

                    alertDialog_noConnection = dialogBuilder.create();
                }
                alertDialog_noConnection.show();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }



    @Override
    protected void onPause() {
        super.onPause();


        unregisterReceiver(networkStateReceiver);
    }

    private void initVIews() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                switch (itemId) {

                    case R.id.pedidos:
                        GoToFragment(new Fragment_Pedidos());
                        break;

                    case R.id.configuration:
                        GoToFragment(new Fragment_Ajustes());
                        break;

                    default:
                        GoToFragment(new Fragment_Pedidos());
                        break;

                }

                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.pedidos);
    }

    private void GoToFragment(Fragment fragment) {

        if (fragment != null) {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (getSupportFragmentManager().getFragments().size() > 0)
                fragmentTransaction.replace(R.id.fragment_container, fragment);
            else fragmentTransaction.add(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

        }

    }


    private void getRestaurantData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String document_negocio_name = UtilFunctions.getNegocioCloudFirestoreDocumentName(MainActivity.this);
        if (document_negocio_name != null) {
            db.collection(FirebaseCloudFirestore_Collections.NEGOCIO).document(document_negocio_name).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot != null) {
                        NegocioGeneral negocioGeneral = documentSnapshot.toObject(NegocioGeneral.class);
                        if (negocioGeneral != null) {

                            Constantes.negocio = negocioGeneral;
                            getRestauranteSeleccionado();

                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });
        }


    }

    private void getRestauranteSeleccionado() {
        ViewModel_Restaurante viewModel_restaurante = new ViewModelProvider(this).get(ViewModel_Restaurante.class);
        viewModel_restaurante.get_entidadrestaurante_livedata().observe(MainActivity.this, new Observer<List<Entidad_Restaurante>>() {
            @Override
            public void onChanged(List<Entidad_Restaurante> entidad_restaurantes) {
                if (entidad_restaurantes != null) {
                    if (entidad_restaurantes.size() > 0) {
                        //un restaurante ha sido seleccionado anteriormente.
                        Entidad_Restaurante entidad_restaurante = entidad_restaurantes.get(0);
                        if (entidad_restaurante != null) {

                            Sucursal sucursal = UtilFunctions.ParseEntidadRestauranteToPojoSucursal(entidad_restaurante);
                            if(sucursal != null)
                            Constantes.MiRestaurante_Seleccionado = sucursal;



                        }
                    } else {
                        //no hay un restaurante seleccionado aún
                        SelectRestaurant();
                    }
                } else {
                    //no hay un restaurante seleccionado aún
                    SelectRestaurant();
                }


                initVIews();

            }
        });

    }

    private void SelectRestaurant() {
        Toast.makeText(this, "Selecciona el restaurante en el que te encuentras...", Toast.LENGTH_LONG).show();

        UiFunctions.showSelectRestaurantBottomSheetDialog(MainActivity.this,false);

    }
}