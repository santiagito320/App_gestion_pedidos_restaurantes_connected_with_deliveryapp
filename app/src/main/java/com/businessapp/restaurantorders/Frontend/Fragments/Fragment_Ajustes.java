package com.businessapp.restaurantorders.Frontend.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.businessapp.restaurantorders.Backend.utils.UtilFunctions;
import com.businessapp.restaurantorders.Backend.utils.utils.UiFunctions;
import com.businessapp.restaurantorders.Frontend.Activities.Activity_OrdersTermicPrinterConfiguration;
import com.businessapp.restaurantorders.Frontend.Activities.Activity_RestauranteConfiguration;
import com.businessapp.restaurantorders.R;


public class Fragment_Ajustes extends Fragment implements View.OnClickListener {
  private Context context;
  //Views.
    private LinearLayout linearLayout_cambiarSucursalActual;

    public Fragment_Ajustes() {
        // Required empty public constructor
    }


    public static Fragment_Ajustes newInstance(String param1, String param2) {
        Fragment_Ajustes fragment = new Fragment_Ajustes();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__ajustes, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        LinearLayout linearLayout_miRestaurante = view.findViewById(R.id.linearLayout_miRestaurante);
        linearLayout_miRestaurante.setOnClickListener(this);

        LinearLayout linearLayout_cambiarSucursalActual = view.findViewById(R.id.linearLayout_cambiarSucursalActual);
        linearLayout_cambiarSucursalActual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                UiFunctions.showSelectRestaurantBottomSheetDialog(context,true);
            }
        });

        LinearLayout linearLayout_configuracion = view.findViewById(R.id.linearLayout_configuracion);
        linearLayout_configuracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiFunctions.ShowBasicAlertDialogWithButtons(context,"Solo personal autorizado","para acceder y manipular datos en 'configuración del restaurante', es requerido conocer el código de acceso. ¿Deseas continuar?",R.drawable.ic_baseline_warning_24,true)
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            }
                        }).setPositiveButton("Ingresar código", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UiFunctions.showViewToPutNegocioGeneralAccessCode(context,true);

                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });

        LinearLayout linearLayout_impresionAutomaticaDePedidos = view.findViewById(R.id.linearLayout_impresoraTermica);
        linearLayout_impresionAutomaticaDePedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**if(context != null){
                    Intent i = new Intent(context, Activity_OrdersTermicPrinterConfiguration.class);
                    context.startActivity(i);
                }**/
                Toast.makeText(context, "Disponible para versiones posteriores", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        String tag = view.getTag().toString();
        if(tag != null) {

            if(!tag.equals("")){
                int tag_number = Integer.parseInt(tag);
                Intent intent = null;
                switch (tag_number){

                    case 0: intent = new Intent(context, Activity_RestauranteConfiguration.class);
                        break;

                    default: intent = null;

                }
                if(intent != null){
                    startActivity(intent);
                }else{
                    Toast.makeText(context, "¡Este apartado no esta disponible por el momento!", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}