package com.businessapp.restaurantorders.Backend.utils.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.businessapp.restaurantorders.Backend.Providers.ProviderPedidos;
import com.businessapp.restaurantorders.Backend.utils.Adapters.AdapterRecyclerView_Locales_list;
import com.businessapp.restaurantorders.Backend.utils.Constantes;
import com.businessapp.restaurantorders.Backend.utils.DB.Entidades.Entidad_Restaurante;
import com.businessapp.restaurantorders.Backend.utils.DB.Repositorios.Repositorio_Restaurante;
import com.businessapp.restaurantorders.Backend.utils.Listeners.ListenerProducto;
import com.businessapp.restaurantorders.Backend.utils.Listeners.ListenerValue;
import com.businessapp.restaurantorders.Backend.utils.PedidoUtils;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Destinatario;
import com.businessapp.restaurantorders.Backend.utils.Pojos.NegocioGeneral;
import com.businessapp.restaurantorders.Backend.utils.Pojos.NegocioMenu;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Pedido;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Producto;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Seleccion;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Sucursal;
import com.businessapp.restaurantorders.Backend.utils.UtilFunctions;
import com.businessapp.restaurantorders.Frontend.Activities.Activity_AppRestaurante_Configuracion;
import com.businessapp.restaurantorders.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class UiFunctions {


    public static void showSelectRestaurantBottomSheetDialog(Context context, boolean cancelable) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_selectlocal);
        bottomSheetDialog.setCancelable(cancelable);

        SearchView searchView_locales = bottomSheetDialog.findViewById(R.id.searchview_locales);
        RecyclerView recyclerView_locales = bottomSheetDialog.findViewById(R.id.recyclerView_locales);

        recyclerView_locales.setLayoutManager(new LinearLayoutManager(context));
        recyclerView_locales.setHasFixedSize(true);

        NegocioGeneral negocioGeneral = Constantes.negocio;
        if (negocioGeneral != null) {
            List<Sucursal> sucursals = Constantes.negocio.getSucursales();
            AdapterRecyclerView_Locales_list adapter = new AdapterRecyclerView_Locales_list(context, sucursals, new AdapterRecyclerView_Locales_list.onItemCLicked() {
                @Override
                public void onItemClickedListener(Sucursal sucursal) {
                    if (bottomSheetDialog != null && bottomSheetDialog.isShowing())
                        bottomSheetDialog.dismiss();
                }
            });
            recyclerView_locales.setAdapter(adapter);

            View view_hide = bottomSheetDialog.findViewById(R.id.view_hider);
            view_hide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            bottomSheetDialog.show();
        }


    }

    public static void showViewWithAccessCodeToSelectCurrentSucursal(Sucursal sucursal, Context context, boolean cancelable) {

        if (sucursal != null && context != null) {

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_access_code_to_select_sucursal);
            bottomSheetDialog.setCancelable(cancelable);

            //Views.
            EditText etxt_accessCode = bottomSheetDialog.findViewById(R.id.etxt_input_access_code);
            Button btn_acceder = bottomSheetDialog.findViewById(R.id.btn_acceder);

            if (etxt_accessCode != null) {
                etxt_accessCode.setAllCaps(true);
                if (btn_acceder != null) {
                    btn_acceder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String accessCode = etxt_accessCode.getText().toString();

                            if (accessCode.equals("")) {
                                etxt_accessCode.setError("??Ingresa el c??digo!");
                            } else {

                                //validate.

                                if (accessCode.equals(sucursal.getAccessCode())) {

                                    //insert current sucursal in local db to remind it.
                                    Repositorio_Restaurante repositorio_restaurante = new Repositorio_Restaurante(context);

                                    Entidad_Restaurante entidad_restaurante = UtilFunctions.ParseSucursalTOEntidadRestaurante(sucursal);
                                    if (entidad_restaurante != null) {

                                        repositorio_restaurante.insertRestaurante(entidad_restaurante);

                                        bottomSheetDialog.dismiss();

                                        UiFunctions.ShowBasicAlertDialog(context, "Local " + entidad_restaurante.nombre, "Ahora recibir??s pedidos que sean solicitados para la sucursal '" + entidad_restaurante.nombre + "'", R.drawable.ic_baseline_store_24_colornormal, false);

                                        Toast toast = Toast.makeText(context, "Ahora recibir??s pedidos que sean solicitados para la sucursal '" + entidad_restaurante.nombre, Toast.LENGTH_LONG);
                                        toast.show();

                                    }

                                } else {
                                    etxt_accessCode.setError("C??digo incorrecto");

                                    Toast toast = Toast.makeText(context, "C??digo incorrecto, intenta con otro", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }

                            }


                        }
                    });

                }

            }

            bottomSheetDialog.show();
        } else {
            Toast.makeText(context, "??No se pudo mostrar la ventana con el c??digo de acceso para la sucursal!, contacte al soporte", Toast.LENGTH_SHORT).show();
        }


    }

    public static void ShowBasicAlertDialog(Context context, String title, String message, int icon, boolean cancelable) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        if (icon != -1) {
            dialog.setIcon(icon);
        }

        dialog.show();

    }

    public static androidx.appcompat.app.AlertDialog.Builder ShowBasicAlertDialogWithButtons(Context context, String title, String message, int icon, boolean cancelable) {

        androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(context);
        dialog.setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable);
        if (icon != -1) {
            dialog.setIcon(icon);
        }


        return dialog;
    }

    public static void showCancelPedidoFinalOptions(Activity context, Pedido pedido) {
        if (context != null) {

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);

            if (bottomSheetDialog != null) {

                bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_cancelpedido);
                bottomSheetDialog.setCancelable(true);

                //views.
                ImageButton imgbtn_back = bottomSheetDialog.findViewById(R.id.imgbtn_back);
                Button btn_llamarCliente = bottomSheetDialog.findViewById(R.id.btn_llamar_cliente);
                Button btn_cancelarPedido = bottomSheetDialog.findViewById(R.id.btn_rechazarPedido);

                if (imgbtn_back != null)
                    imgbtn_back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            bottomSheetDialog.dismiss();

                        }
                    });


                Destinatario destinatario = pedido.getDestinatario();
                if (destinatario != null && btn_llamarCliente != null) {

                    String telefono = destinatario.getTelefono();

                    btn_llamarCliente.setText(telefono);
                    btn_llamarCliente.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Destinatario destinatario = pedido.getDestinatario();
                            if (destinatario != null) {

                                openPhoneContact(context, true, telefono);

                            }

                        }
                    });

                }


                bottomSheetDialog.show();

                if (btn_cancelarPedido != null) {
                    btn_cancelarPedido.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            showBottomSheetDialog_cancelPedido_motivos(context, pedido, new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    bottomSheetDialog.dismiss();
                                }
                            });

                        }
                    });
                }

            }


        }


    }

    public static BottomSheetDialog openPhoneContact(Activity context, boolean cancelable, String telefono) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.bottomsheetdialog_negocio_phone);
        bottomSheetDialog.setCancelable(cancelable);

        LinearLayout linearLayout_content_call = bottomSheetDialog.findViewById(R.id.linear_content_call);
        if (linearLayout_content_call != null) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context, new String[]{android.Manifest.permission.CALL_PHONE}, 100);
            }

            linearLayout_content_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (telefono != null) {
                        if (!telefono.equals("")) {

                            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(context, new String[]{android.Manifest.permission.CALL_PHONE}, 100);
                            } else {

                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telefono));
                                context.startActivity(intent);

                            }

                        }
                    }

                }
            });

        }

        bottomSheetDialog.show();

        return bottomSheetDialog;
    }

    public static void showBottomSheetDialog_cancelPedido_motivos(Context context, Pedido pedido, DialogInterface.OnDismissListener onDismissListener) {
        if (context != null) {

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_cancelpedido_motivos);
            bottomSheetDialog.setCancelable(true);

            //views.
            ImageButton imgbtn_back = bottomSheetDialog.findViewById(R.id.imgBtn_back);
            TextView txtEnviar = bottomSheetDialog.findViewById(R.id.txtbtn_enviar);
            RadioGroup radioGroup_motivos = bottomSheetDialog.findViewById(R.id.radio_group_motivos);
            EditText etxt_mensajePersonalizado = bottomSheetDialog.findViewById(R.id.etxt_mensajePersonalizado);

            if (imgbtn_back != null) {

                imgbtn_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        bottomSheetDialog.dismiss();

                    }
                });

            }
            if (txtEnviar != null) {

                txtEnviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (radioGroup_motivos != null) {

                            int rb_checked_motivo_id = radioGroup_motivos.getCheckedRadioButtonId();
                            RadioButton radioButton_motivo_checked = radioGroup_motivos.findViewById(rb_checked_motivo_id);

                            if (radioButton_motivo_checked != null) {

                                String pedidoCancelled_motivo = "Ning??n motivo especifico";
                                if (rb_checked_motivo_id == R.id.rb_mensajePersonalizado) {
                                    if (etxt_mensajePersonalizado != null) {
                                        pedidoCancelled_motivo = etxt_mensajePersonalizado.getText().toString();
                                    }
                                } else
                                    pedidoCancelled_motivo = radioButton_motivo_checked.getText().toString();

                                System.out.println("pedidoCancelled_motivo = " + pedidoCancelled_motivo);

                                if (!pedidoCancelled_motivo.equals("")) {

                                    ProviderPedidos providerPedidos = new ProviderPedidos(context);
                                    String pedidoFirebaseDocID = pedido.getOwner_firebase_uid() + "-" + pedido.getNumero_pedido();

                                    pedido.setEstado(PedidoUtils.STATE_PROCESS_CANCELLED);
                                    pedido.setPedidoCancelledMotivo(pedidoCancelled_motivo);

                                    providerPedidos.updatePedido(pedido).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "Pedido #" + pedido.getNumero_pedido() + " cancelado", Toast.LENGTH_SHORT).show();
                                            onDismissListener.onDismiss(bottomSheetDialog);
                                            bottomSheetDialog.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Pedido #" + pedido.getNumero_pedido() + " no se pudo cancelar", Toast.LENGTH_SHORT).show();

                                        }
                                    });


                                } else {
                                    if (etxt_mensajePersonalizado != null)
                                        etxt_mensajePersonalizado.setError("Porfavor, escribe el mensaje!");
                                }

                            }

                        }
                    }
                });


            }
            if (radioGroup_motivos != null) {
                radioGroup_motivos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        if (etxt_mensajePersonalizado != null) {
                            if (i == R.id.rb_mensajePersonalizado) {
                                //mensaje personalizado.
                                etxt_mensajePersonalizado.setVisibility(View.VISIBLE);

                            } else etxt_mensajePersonalizado.setVisibility(View.GONE);
                        }

                    }
                });
            }


            bottomSheetDialog.show();
        }
    }

    public static void showSetPedidoHoraDeEntrega(Context context, Pedido pedido) {

        if (context != null) {

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_set_pedido_hora_de_entrega);
            bottomSheetDialog.setCancelable(true);

            //views.
            ImageButton imgbtn_back = bottomSheetDialog.findViewById(R.id.imgBtn_back);
            EditText etxt_horaEntregaEstimada = bottomSheetDialog.findViewById(R.id.etxt_tiempoEntrega);
            Button btn_aceptar = bottomSheetDialog.findViewById(R.id.btn_aceptar);

            if (imgbtn_back != null) {
                imgbtn_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();

                    }
                });
            }
            if (btn_aceptar != null) {

                btn_aceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (etxt_horaEntregaEstimada != null) {

                            String tiempo_de_entrega = etxt_horaEntregaEstimada.getText().toString();

                            if (!tiempo_de_entrega.equals("")) {

                                int tiempo_de_entrega_number = Integer.parseInt(tiempo_de_entrega);
                                int tiempo_de_entrega_plus_tenMinutes = tiempo_de_entrega_number + 10;

                                String tiempo_de_entrega_estimada = tiempo_de_entrega_number + "-" + tiempo_de_entrega_plus_tenMinutes + " min";

                                ProviderPedidos providerPedidos = new ProviderPedidos(context);

                                pedido.setEstado(PedidoUtils.STATE_PROCESS_2);
                                pedido.setHora_entrega_estimada(tiempo_de_entrega_estimada);

                                providerPedidos.updatePedido(pedido).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        bottomSheetDialog.dismiss();
                                        Toast.makeText(context, "Pedido #" + pedido.getNumero_pedido() + " indexado en la seccion de pedidos en preparaci??n", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Pedido #" + pedido.getNumero_pedido() + " fallo al ser indexado en la seccion de pedidos en preparaci??n", Toast.LENGTH_LONG).show();
                                    }
                                });

                            } else
                                etxt_horaEntregaEstimada.setError("Ingresa la hora de entrega estimada.");

                        }


                    }
                });

            }


            bottomSheetDialog.show();
        }


    }

    public static void showViewToPutNegocioGeneralAccessCode(Context context, boolean cancelable) {
        NegocioGeneral negocioGeneral = Constantes.negocio;
        if (context != null && negocioGeneral != null) {

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_access_code_to_select_sucursal);
            bottomSheetDialog.setCancelable(cancelable);

            //Views.
            EditText etxt_accessCode = bottomSheetDialog.findViewById(R.id.etxt_input_access_code);
            Button btn_acceder = bottomSheetDialog.findViewById(R.id.btn_acceder);

            if (etxt_accessCode != null) {
                etxt_accessCode.setAllCaps(true);
                if (btn_acceder != null) {
                    btn_acceder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String accessCode = etxt_accessCode.getText().toString();

                            if (accessCode.equals("")) {
                                etxt_accessCode.setError("??Ingresa el c??digo!");
                            } else {

                                //validate.
                                if (accessCode.equals(negocioGeneral.getAccess_code())) {

                                    //give access to restaurante configuration page.
                                    Toast.makeText(context, "Acceso concedido.", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(context, Activity_AppRestaurante_Configuracion.class);

                                    context.startActivity(i);

                                    bottomSheetDialog.dismiss();

                                } else {
                                    etxt_accessCode.setError("C??digo incorrecto");

                                    Toast toast = Toast.makeText(context, "C??digo incorrecto, intenta con otro", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }

                            }


                        }
                    });

                }

            }

            bottomSheetDialog.show();
        } else {
            Toast.makeText(context, "??No se pudo mostrar la ventana con el c??digo de acceso para la sucursal!, contacte al soporte", Toast.LENGTH_SHORT).show();
        }


    }

    public static BottomSheetDialog showBottomSheetDialogPutValue(Context context, int editText_inputType, ListenerValue listenerValue, String typeValue, String valor_anterior) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.bottomsheetdialog_put_value);

        TextView textView_title = bottomSheetDialog.findViewById(R.id.textView_title);
        textView_title.setText("Actualizar");

        TextView textView_valueHeader = bottomSheetDialog.findViewById(R.id.textView_valueHeader);

        if (textView_valueHeader != null) {
            if (typeValue.equals("Tel??fono")) {

                textView_valueHeader.setText(typeValue + " (Insertar prefijo '[+] C??DIGO DE PA??S')");

            } else
                textView_valueHeader.setText(typeValue);
        }


        //Vista que oculta la ventana.
        View view_hide = bottomSheetDialog.findViewById(R.id.view_hider);
        view_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        //Campo para el telefono.
        EditText editText_value = bottomSheetDialog.findViewById(R.id.etxt_valor);
        if (editText_value != null) {
            editText_value.setInputType(editText_inputType);
            editText_value.setText(valor_anterior);

        }
        //Switch.
        Switch switch1 = (Switch) bottomSheetDialog.findViewById(R.id.switch1);
        if (valor_anterior.equals("YES") || valor_anterior.equals("NO")) {
            switch1.setVisibility(View.VISIBLE);
            editText_value.setVisibility(View.GONE);
            if (switch1 != null) {
                switch1.setChecked(valor_anterior.equals("YES"));
                switch1.setTextOff("NO");
                switch1.setTextOn("SI");
                switch1.setText(valor_anterior.equals("YES") ? "SI" : "NO");
                switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        switch1.setText(b ? "SI" : "NO");
                    }
                });
            }
        } else {
            switch1.setVisibility(View.GONE);
            editText_value.setVisibility(View.VISIBLE);
        }


        //Boton para guardar el nuevo telefono.
        Button btn_guardar = bottomSheetDialog.findViewById(R.id.btn_guardar);
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String valor = editText_value.getText().toString();
                boolean valor_booleano = switch1.isChecked();

                if (!valor.equals("")) {

                    listenerValue.onValueChangedAux(valor, valor_booleano, editText_value, bottomSheetDialog);

                } else editText_value.setError("??Rellena este campo!");


            }
        });

        bottomSheetDialog.show();

        return bottomSheetDialog;
    }

    public static void showBottomSheetDialog_getProduct(Context context, Producto producto, ListenerProducto listenerProducto) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dialog_get_product, null);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(true);

        //Views.
        EditText etxtNombre = bottomSheetDialog.findViewById(R.id.etxt_valor_nombre);
        EditText etxt_descripcion = bottomSheetDialog.findViewById(R.id.etxt_valor_descripcion);
        EditText etxt_precio = bottomSheetDialog.findViewById(R.id.etxt_valor_precio);
        TextView txt_precio = bottomSheetDialog.findViewById(R.id.textView_valueHeader_precio);
        Button btn_opciones_y_complementos = bottomSheetDialog.findViewById(R.id.button_opciones_y_complementos);
        Button btnGuardar = bottomSheetDialog.findViewById(R.id.btn_guardar);

        if (producto == null) producto = new Producto();

        if (etxtNombre != null) etxtNombre.setText(producto.getTitulo());
        if (etxt_descripcion != null) etxt_descripcion.setText(producto.getDescripcion());

        String divisa = context.getString(R.string.divisa);
        if (etxt_precio != null) {
            etxt_precio.setText(String.valueOf(producto.getPrecio_unitario()));
        }
        if (txt_precio != null) txt_precio.setText(String.format("Precio (%s)", divisa));

        if (btn_opciones_y_complementos != null) {
            btn_opciones_y_complementos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder dialog_list_of_opcionesYComplementos = new AlertDialog.Builder(context);
                    dialog_list_of_opcionesYComplementos.setTitle("Opciones y complementos");

                    View listOfOpcionesYComplementosView = LayoutInflater.from(context).inflate(R.layout.list_of_opciones_y_complementos, null);

                    dialog_list_of_opcionesYComplementos.setCancelable(true);

                    NegocioMenu negocioMenu = UtilFunctions.getMenu();
                    if (negocioMenu != null) {
                        List<Seleccion> menu_opcionesYcomplementos = negocioMenu.getOpciones_y_complementos();

                        if(menu_opcionesYcomplementos.size() == 0) dialog_list_of_opcionesYComplementos.setMessage("No hay complementos disponibles, empieza por crear una nueva opci??n u complemento.");
                        else dialog_list_of_opcionesYComplementos.setMessage("");

                        final CharSequence[] menu_opcionesYcomplementos_chsq = new CharSequence[menu_opcionesYcomplementos.size()];

                        for (int i = 0; i < menu_opcionesYcomplementos.size(); i++) {

                            Seleccion opcionYComplemento = menu_opcionesYcomplementos.get(i);
                            if (opcionYComplemento != null) {

                                menu_opcionesYcomplementos_chsq[i] = opcionYComplemento.getTitulo();

                            }

                        }

                        dialog_list_of_opcionesYComplementos.setItems(menu_opcionesYcomplementos_chsq, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.dismiss();
                            }
                        });
                        dialog_list_of_opcionesYComplementos.setPositiveButton("Nuevo complemento", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        dialog_list_of_opcionesYComplementos.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        dialog_list_of_opcionesYComplementos.show();

                    }

                }
            });
        }

        if (btnGuardar != null) {
            btnGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }

        //expand bottom sheet dialog.
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        bottomSheetDialog.show();
    }
}
