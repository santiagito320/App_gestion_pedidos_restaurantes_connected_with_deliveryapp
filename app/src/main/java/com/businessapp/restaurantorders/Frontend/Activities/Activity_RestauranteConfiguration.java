package com.businessapp.restaurantorders.Frontend.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.businessapp.restaurantorders.Backend.Providers.ProviderNegocioGeneral;
import com.businessapp.restaurantorders.Backend.utils.Listeners.ListenerValue;
import com.businessapp.restaurantorders.Backend.utils.Pojos.MetodosDePago;
import com.businessapp.restaurantorders.Backend.utils.Pojos.NegocioGeneral;
import com.businessapp.restaurantorders.Backend.utils.Pojos.RestauranteServicios;
import com.businessapp.restaurantorders.Backend.utils.utils.UiFunctions;
import com.businessapp.restaurantorders.Frontend.TreeView.holder.MyHolder;
import com.businessapp.restaurantorders.Frontend.TreeView.model.TreeNode;
import com.businessapp.restaurantorders.Frontend.TreeView.view.AndroidTreeView;
import com.businessapp.restaurantorders.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.gson.Gson;

import java.util.regex.Pattern;

public class Activity_RestauranteConfiguration extends AppCompatActivity {

    private final String[] negocioObj_typeValues = {
            "Nombre",
            "Link de facebook",
            "Link de instagram",
            "Link de twitter",
            "Link del sitio web",
            "Teléfono",
            "Email",
            "Email de la app del restaurante",
            "Link de políticas y términos de condiciones",
            "Tiempo estimado en preparación de pedido en segundos",
            "Costo de despacho fijo",
            "Código de acceso",
            "Para recojer",
            "Entrega a domicilio",
            "Horario del servicio",
            "Métodos de pago",
            "(Usar divisa local) Costo fijo de envío de productos al domicilio del cliente",
            "(Minutos) Tiempo estimado en preparar un pedido",
            "Pago Efectivo",
            "Pago Tarjeta Terminal"

    };
    //data.
    private NegocioGeneral negocioGeneral;
    //extra.
    private ListenerRegistration listenerRegistration_providerNegocioGeneral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__restaurante_configuration);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            getSupportActionBar().hide();


        initViews();
        setUpCustomSelectionsTreeView();
    }

    private void getData() {
        //get restaurante data.
        if (listenerRegistration_providerNegocioGeneral == null) {
            ProviderNegocioGeneral providerNegocioGeneral = new ProviderNegocioGeneral(Activity_RestauranteConfiguration.this);
            listenerRegistration_providerNegocioGeneral = providerNegocioGeneral.getMyNegocioGeneral().addSnapshotListener(Activity_RestauranteConfiguration.this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value != null) {

                        NegocioGeneral negocioGeneral1 = value.toObject(NegocioGeneral.class);

                        if (negocioGeneral1 != null) {
                            Activity_RestauranteConfiguration.this.negocioGeneral = negocioGeneral1;
                        }

                    }
                    if (error != null) {
                        System.err.println("Error en evento listener de negocio general = " + error.getMessage());
                        error.printStackTrace();
                    }
                }
            });

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (listenerRegistration_providerNegocioGeneral != null) {
            listenerRegistration_providerNegocioGeneral.remove();
            listenerRegistration_providerNegocioGeneral = null;
        }
    }

    private void initViews() {

        ImageButton imgbtn_back = findViewById(R.id.imgbtn_back);
        imgbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void setUpCustomSelectionsTreeView() {

        //Root
        TreeNode root = TreeNode.root();

        /**Informacion basica (tree).**/

        //Parent
        MyHolder.IconTreeItem nodeItem = new MyHolder.IconTreeItem(R.drawable.ic_arrow_drop_down, "Información basica");
        TreeNode parent = new TreeNode(nodeItem).setViewHolder(new MyHolder(getApplicationContext(), true, MyHolder.DEFAULT, MyHolder.DEFAULT));

        //Child
        MyHolder.IconTreeItem childItem_restauranteNombre = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Nombre del restaurante");
        TreeNode child_restauranteNOmbre = new TreeNode(childItem_restauranteNombre).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_restauranteNOmbre.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[0], negocioGeneral.getNombre(), InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            }
        });

        MyHolder.IconTreeItem childItem_restauranteTelefono = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Teléfono del restaurante");
        TreeNode child_restauranteTelefono = new TreeNode(childItem_restauranteTelefono).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_restauranteTelefono.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[5], negocioGeneral.getTelefono(), InputType.TYPE_CLASS_PHONE);
            }
        });

        MyHolder.IconTreeItem childItem_restauranteEmail = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "E-mail del restaurante");
        TreeNode child_restauranteEmail = new TreeNode(childItem_restauranteEmail).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_restauranteEmail.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[6], negocioGeneral.getEmail(), InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            }
        });

        MyHolder.IconTreeItem childItem_restauranteAppEmail = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "E-mail de la app del restaurante");
        TreeNode child_restauranteAppEmail = new TreeNode(childItem_restauranteAppEmail).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_restauranteAppEmail.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[7], negocioGeneral.getApp_email(), InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            }
        });

        MyHolder.IconTreeItem childItem_restauranteFacebookLink = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Link de página de Facebook");
        TreeNode child_restauranteFacebookLink = new TreeNode(childItem_restauranteFacebookLink).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_restauranteFacebookLink.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[1], negocioGeneral.getFacebook_link(), InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
            }
        });

        MyHolder.IconTreeItem childItem_restauranteInstagramLink = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Link de página de Instagram");
        TreeNode child_restauranteInstagramLink = new TreeNode(childItem_restauranteInstagramLink).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_restauranteInstagramLink.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[2], negocioGeneral.getInstagram_link(), InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
            }
        });

        MyHolder.IconTreeItem childItem_restauranteTwitterLink = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Link de página de Twitter");
        TreeNode child_restauranteTwitterLink = new TreeNode(childItem_restauranteTwitterLink).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_restauranteTwitterLink.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[3], negocioGeneral.getTwitter_link(), InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
            }
        });

        MyHolder.IconTreeItem childItem_restauranteSitioWebLink = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Link del sitio web del restaurante");
        TreeNode child_restauranteSitioWebLink = new TreeNode(childItem_restauranteSitioWebLink).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_restauranteSitioWebLink.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[4], negocioGeneral.getWeb_link(), InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
            }
        });

        //Add sub child.


        //Add child.
        parent.addChildren(child_restauranteNOmbre);
        parent.addChildren(child_restauranteTelefono);
        parent.addChildren(child_restauranteEmail);
        parent.addChildren(child_restauranteAppEmail);
        parent.addChildren(child_restauranteFacebookLink);
        parent.addChildren(child_restauranteInstagramLink);
        parent.addChildren(child_restauranteTwitterLink);
        parent.addChildren(child_restauranteSitioWebLink);

        root.addChild(parent);

        //Add AndroidTreeView into view.

        /**Servicios y horarios*/

        //Parent
        MyHolder.IconTreeItem nodeItem_serviciosyhorarios = new MyHolder.IconTreeItem(R.drawable.ic_arrow_drop_down, "Servicios");
        TreeNode parent_serviciosyhorarios = new TreeNode(nodeItem_serviciosyhorarios).setViewHolder(new MyHolder(getApplicationContext(), true, MyHolder.DEFAULT, MyHolder.DEFAULT));

        //Child
        MyHolder.IconTreeItem childItem_recojerensucursal = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Para recojer");
        TreeNode child_recojerensucursal = new TreeNode(childItem_recojerensucursal).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_recojerensucursal.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[12], negocioGeneral.getServicios_disponibles().isRecojer_en_sucursal() ? "YES" : "NO", InputType.TYPE_CLASS_TEXT);
            }
        });

        MyHolder.IconTreeItem childItem_entregaDomicilio = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Entrega a domicilio");
        TreeNode child_entregaDomicilio = new TreeNode(childItem_entregaDomicilio).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_entregaDomicilio.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[13], negocioGeneral.getServicios_disponibles().isEntrega_a_domicilio() ? "YES" : "NO", InputType.TYPE_CLASS_TEXT);
            }
        });

        MyHolder.IconTreeItem childItem_horariodelservicio = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Horario del servicio");
        TreeNode child_horariodelservicio = new TreeNode(childItem_horariodelservicio).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_horariodelservicio.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                //todo:OpenRestaurantSetter(negocioObj_typeValues[14],negocioGeneral.getHorarios());
            }
        });

        //Add sub child.


        //Add child.
        parent_serviciosyhorarios.addChildren(child_recojerensucursal);
        parent_serviciosyhorarios.addChildren(child_entregaDomicilio);
        //todo: parent_serviciosyhorarios.addChildren(child_horariodelservicio);

        root.addChild(parent_serviciosyhorarios);

        /**Pagos e impuestos*/

        //Parent
        MyHolder.IconTreeItem nodeItem_pagoseimpuestos = new MyHolder.IconTreeItem(R.drawable.ic_arrow_drop_down, "Pagos e impuestos");
        TreeNode parent_pagoseimpuestos = new TreeNode(nodeItem_pagoseimpuestos).setViewHolder(new MyHolder(getApplicationContext(), true, MyHolder.DEFAULT, MyHolder.DEFAULT));

        //Child
        MyHolder.IconTreeItem childItem_metodosdepago = new MyHolder.IconTreeItem(R.drawable.ic_baseline_arrow_drop_down_24_gray, "Métodos de pago");
        TreeNode child_metodosdepago = new TreeNode(childItem_metodosdepago).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        //Add sub child.
        MyHolder.IconTreeItem childItem_metodosdepago_efectivo = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Efectivo");
        TreeNode child_metodosdepago_efectivo = new TreeNode(childItem_metodosdepago_efectivo).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_metodosdepago_efectivo.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                MetodosDePago metodosDePago = negocioGeneral.getMetodos_de_pago();
                if (metodosDePago != null) {
                    OpenRestaurantSetter(negocioObj_typeValues[18], metodosDePago.isEfectivo() ? "YES" : "NO", InputType.TYPE_CLASS_TEXT);
                }
            }
        });
        child_metodosdepago_efectivo.setSelectable(true);

        MyHolder.IconTreeItem childItem_metodosdepago_tarjetaTerminal = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Tarjeta terminal");
        TreeNode child_metodosdepago_tarjetaTerminal = new TreeNode(childItem_metodosdepago_tarjetaTerminal).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_metodosdepago_tarjetaTerminal.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                MetodosDePago metodosDePago = negocioGeneral.getMetodos_de_pago();
                if (metodosDePago != null) {
                    OpenRestaurantSetter(negocioObj_typeValues[19], metodosDePago.isTarjeta_terminal() ? "YES" : "NO", InputType.TYPE_CLASS_TEXT);
                }
            }
        });
        child_metodosdepago_tarjetaTerminal.setSelectable(true);

        child_metodosdepago.addChildren(child_metodosdepago_efectivo);
        child_metodosdepago.addChildren(child_metodosdepago_tarjetaTerminal);

        //Add child.
        parent_pagoseimpuestos.addChildren(child_metodosdepago);

        root.addChild(parent_pagoseimpuestos);

        /**Menu y pedidos*/

        //Parent
        MyHolder.IconTreeItem nodeItem_menuYPedidos = new MyHolder.IconTreeItem(R.drawable.ic_arrow_drop_down, "Menú y pedidos");
        TreeNode parent_menuYPedidos = new TreeNode(nodeItem_menuYPedidos).setViewHolder(new MyHolder(getApplicationContext(), true, MyHolder.DEFAULT, MyHolder.DEFAULT));

        //Child
        MyHolder.IconTreeItem childItem_configuracionDelMenu = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Configuración del menú");
        TreeNode child_configuracionDelMenu = new TreeNode(childItem_configuracionDelMenu).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_configuracionDelMenu.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                Intent i = new Intent(Activity_RestauranteConfiguration.this, ActivityRestauranteConfiguracionDelMenu.class);
                String menu_json = new Gson().toJson(negocioGeneral.getMenu());
                i.putExtra(menu_json, "NegocioMenu_Json");
                startActivity(i);
            }
        });


        MyHolder.IconTreeItem childItem_restaurante_costoDeEnvio = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Costo fijo de envío de productos al domicilio del cliente");
        TreeNode child_restaurante_costoDeEnvio = new TreeNode(childItem_restaurante_costoDeEnvio).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_restaurante_costoDeEnvio.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[16], String.valueOf((int) negocioGeneral.getCosto_fijo_de_envio_de_productos()), InputType.TYPE_CLASS_NUMBER);
            }
        });

        MyHolder.IconTreeItem childItem_restaurante_tiempoEstimadoEnPrepararUnPedido = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Tiempo estimado en preparar un pedido");
        TreeNode child_restaurante_tiempoEstimadoEnPrepararUnPedido = new TreeNode(childItem_restaurante_tiempoEstimadoEnPrepararUnPedido).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_restaurante_tiempoEstimadoEnPrepararUnPedido.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                String minutos_estimados_en_preparacion_de_pedido = String.valueOf( negocioGeneral.getTiempo_estimado_en_preparacion_de_pedido_insec() / 60 );
                OpenRestaurantSetter(negocioObj_typeValues[17],minutos_estimados_en_preparacion_de_pedido,InputType.TYPE_CLASS_NUMBER);
            }
        });

        //Add sub child.


        //Add child.
        parent_menuYPedidos.addChildren(child_configuracionDelMenu);
        parent_menuYPedidos.addChildren(child_restaurante_tiempoEstimadoEnPrepararUnPedido);
        parent_menuYPedidos.addChildren(child_restaurante_costoDeEnvio);

        root.addChild(parent_menuYPedidos);


        /**Otros*/

        //Parent
        MyHolder.IconTreeItem nodeItem_otros = new MyHolder.IconTreeItem(R.drawable.ic_arrow_drop_down, "Otros");
        TreeNode parent_otros = new TreeNode(nodeItem_otros).setViewHolder(new MyHolder(getApplicationContext(), true, MyHolder.DEFAULT, MyHolder.DEFAULT));

        //Child
        MyHolder.IconTreeItem childItem_codigoDeAcceso = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Código de acceso a la configuración del restaurante a nivel de aplicativo");
        TreeNode child_codigoDeAcceso = new TreeNode(childItem_codigoDeAcceso).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_codigoDeAcceso.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[11], negocioGeneral.getAccess_code(), InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        MyHolder.IconTreeItem childItem_restauranteAppLinkPolicyAndTerms = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Link de políticas y términos de condiciones de la app del restaurante (En caso de no disponerla se deberá solicitar a la agencia proveedora de los aplicativos)");
        TreeNode child_restauranteAppLinkPolicyAndTerms = new TreeNode(childItem_restauranteAppLinkPolicyAndTerms).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_restauranteAppLinkPolicyAndTerms.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[8], negocioGeneral.getLink_politicasyterminosdecondiciones(), InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
            }
        });

        //Add sub child.


        //Add child.
        parent_otros.addChildren(child_codigoDeAcceso);
        parent_otros.addChildren(child_restauranteAppLinkPolicyAndTerms);


        root.addChild(parent_otros);


        //Add AndroidTreeView into view.
        AndroidTreeView tView = new AndroidTreeView(getApplicationContext(), root);

        ((LinearLayout) findViewById(R.id.linearLayout_treeViewParent)).addView(tView.getView());


        /***
         (Example tree setup code)


         //Root
         TreeNode root = TreeNode.root();


         //Parent
         MyHolder.IconTreeItem nodeItem_pagoseimpuestos = new MyHolder.IconTreeItem(R.drawable.ic_arrow_drop_down, "Parent");
         TreeNode parent_pagoseimpuestos = new TreeNode(nodeItem_pagoseimpuestos).setViewHolder(new MyHolder(getApplicationContext(), true, MyHolder.DEFAULT, MyHolder.DEFAULT));

         //Child
         MyHolder.IconTreeItem childItem = new MyHolder.IconTreeItem(R.drawable.ic_folder, "Child");
         TreeNode child = new TreeNode(childItem).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));

         //Sub Child
         MyHolder.IconTreeItem subChildItem = new MyHolder.IconTreeItem(R.drawable.ic_folder, "Sub Child");
         TreeNode subChild = new TreeNode(subChildItem).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 50));

         //Add sub child.
         child.addChild(subChild);


         //Add child.
         parent_pagoseimpuestos.addChildren(child);
         root.addChild(parent_pagoseimpuestos);

         //Add AndroidTreeView into view.
         AndroidTreeView tView = new AndroidTreeView(getApplicationContext(), root);
         ((LinearLayout) findViewById(R.id.linearLayout_treeViewParent)).addView(tView.getView());
         **/


    }

    private void OpenRestaurantSetter(String valueType, String valor_anterior, int inputType) {


        UiFunctions.showBottomSheetDialogPutValue(Activity_RestauranteConfiguration.this, inputType, new ListenerValue() {
            @Override
            public void onValueChanged(Object valor) {

            }

            @Override
            public void onValueChangedAux(Object valor, boolean valor_booleano, EditText editText, BottomSheetDialog bottomSheetDialog) {
                if (valor != null) {

                    //nombre del restaurante.
                    if (valueType.equals(negocioObj_typeValues[0])) {
                        negocioGeneral.setNombre(valor.toString());
                        bottomSheetDialog.dismiss();
                        Toast.makeText(Activity_RestauranteConfiguration.this, "Nombre del restaurante = " + valor.toString(), Toast.LENGTH_SHORT).show();

                    } else
                        //facebook link.
                        if (valueType.equals(negocioObj_typeValues[1])) {
                            //validar url.
                            if (Patterns.WEB_URL.matcher(valor.toString()).matches()) {

                                negocioGeneral.setFacebook_link(valor.toString());
                                bottomSheetDialog.dismiss();
                                Toast.makeText(Activity_RestauranteConfiguration.this, "Link de Facebook = " + valor.toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                editText.setError("URL Invalida");
                                Toast.makeText(Activity_RestauranteConfiguration.this, "URL Invalida", Toast.LENGTH_SHORT).show();
                            }

                        } else
                            //instagram link.
                            if (valueType.equals(negocioObj_typeValues[2])) {

                                if (Patterns.WEB_URL.matcher(valor.toString()).matches()) {

                                    negocioGeneral.setInstagram_link(valor.toString());
                                    bottomSheetDialog.dismiss();
                                    Toast.makeText(Activity_RestauranteConfiguration.this, "Link de Instagram = " + valor.toString(), Toast.LENGTH_SHORT).show();
                                } else {
                                    editText.setError("URL Invalida");
                                    Toast.makeText(Activity_RestauranteConfiguration.this, "URL Invalida", Toast.LENGTH_SHORT).show();
                                }

                                negocioGeneral.setInstagram_link(valor.toString());

                            } else
                                //twitter link.

                                if (valueType.equals(negocioObj_typeValues[3])) {

                                    if (Patterns.WEB_URL.matcher(valor.toString()).matches()) {

                                        negocioGeneral.setTwitter_link(valor.toString());
                                        bottomSheetDialog.dismiss();
                                        Toast.makeText(Activity_RestauranteConfiguration.this, "Link de Twitter = " + valor.toString(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        editText.setError("URL Invalida");
                                        Toast.makeText(Activity_RestauranteConfiguration.this, "URL Invalida", Toast.LENGTH_SHORT).show();
                                    }


                                } else
                                    //sitio web del restaurante.
                                    if (valueType.equals(negocioObj_typeValues[4])) {

                                        if (Patterns.WEB_URL.matcher(valor.toString()).matches()) {

                                            negocioGeneral.setWeb_link(valor.toString());
                                            bottomSheetDialog.dismiss();
                                            Toast.makeText(Activity_RestauranteConfiguration.this, "Link de Sitio Web = " + valor.toString(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            editText.setError("URL Invalida");
                                            Toast.makeText(Activity_RestauranteConfiguration.this, "URL Invalida", Toast.LENGTH_SHORT).show();
                                        }

                                    } else
                                        //telefono del restaurante.
                                        if (valueType.equals(negocioObj_typeValues[5])) {
                                            String allCountryRegex = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$";

                                            Pattern pattern = Pattern.compile(allCountryRegex);

                                            if (pattern.matcher(valor.toString()).matches()) {

                                                negocioGeneral.setTelefono(valor.toString());
                                                bottomSheetDialog.dismiss();
                                                Toast.makeText(Activity_RestauranteConfiguration.this, "Teléfono = " + valor.toString(), Toast.LENGTH_SHORT).show();
                                            } else {
                                                editText.setError("Formato de teléfono incorrecto.");
                                                Toast.makeText(Activity_RestauranteConfiguration.this, "Formato de teléfono incorrecto.", Toast.LENGTH_SHORT).show();
                                            }

                                        } else
                                            //email del restaurante.

                                            if (valueType.equals(negocioObj_typeValues[6])) {
                                                Pattern pattern = Patterns.EMAIL_ADDRESS;
                                                String email = valor.toString();
                                                if (pattern.matcher(email).matches()) {
                                                    negocioGeneral.setEmail(email);
                                                    bottomSheetDialog.dismiss();
                                                    Toast.makeText(Activity_RestauranteConfiguration.this, "Email = " + email, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    editText.setError("Formato de email incorrecto.");
                                                    Toast.makeText(Activity_RestauranteConfiguration.this, "Formato de email incorrecto.", Toast.LENGTH_SHORT).show();
                                                }
                                            } else
                                                //app email del restaurante.

                                                if (valueType.equals(negocioObj_typeValues[7])) {

                                                    Pattern pattern = Patterns.EMAIL_ADDRESS;
                                                    String email = valor.toString();
                                                    if (pattern.matcher(email).matches()) {
                                                        negocioGeneral.setApp_email(valor.toString());
                                                        bottomSheetDialog.dismiss();
                                                        Toast.makeText(Activity_RestauranteConfiguration.this, "App Email = " + email, Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        editText.setError("Formato de App Email incorrecto.");
                                                        Toast.makeText(Activity_RestauranteConfiguration.this, "Formato de App Email incorrecto.", Toast.LENGTH_SHORT).show();
                                                    }

                                                } else
                                                    //link de politicas de privacidad y terminos de condiciones.

                                                    if (valueType.equals(negocioObj_typeValues[8])) {

                                                        if (Patterns.WEB_URL.matcher(valor.toString()).matches()) {

                                                            negocioGeneral.setLink_politicasyterminosdecondiciones(valor.toString());
                                                            bottomSheetDialog.dismiss();
                                                            Toast.makeText(Activity_RestauranteConfiguration.this, "Link de Politicas de privacidad y Términos de condiciones = " + valor.toString(), Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            editText.setError("URL Invalida");
                                                            Toast.makeText(Activity_RestauranteConfiguration.this, "URL Invalida", Toast.LENGTH_SHORT).show();
                                                        }

                                                    } else
                                                        //tiempo estimado en preparación de pedidos.

                                                        if (valueType.equals(negocioObj_typeValues[9])) {
                                                            long tiempo_estimado_en_preparacion_de_pedido_insec = Long.parseLong(valor.toString());
                                                            negocioGeneral.setTiempo_estimado_en_preparacion_de_pedido_insec(tiempo_estimado_en_preparacion_de_pedido_insec);

                                                        } else
                                                            //costo de despacho fijo.

                                                            if (valueType.equals(negocioObj_typeValues[10])) {
                                                                double costoDespacho = Double.parseDouble(valor.toString());
                                                                negocioGeneral.setCosto_despacho(costoDespacho);

                                                            } else
                                                                //access code.
                                                                if (valueType.equals(negocioObj_typeValues[11])) {
                                                                    String password = valor.toString();
                                                                    if (password.length() == 5) {
                                                                        negocioGeneral.setAccess_code(password);
                                                                        bottomSheetDialog.dismiss();
                                                                        Toast.makeText(Activity_RestauranteConfiguration.this, "Código de acceso = " + password, Toast.LENGTH_SHORT).show();
                                                                    } else {
                                                                        editText.setError("El código estrictamente debe tener 5 caracteres.");
                                                                    }

                                                                } else
                                                                    //servicio 'recojer en sucursal'
                                                                    if (valueType.equals(negocioObj_typeValues[12])) {
                                                                        RestauranteServicios restauranteServicios = negocioGeneral.getServicios_disponibles();
                                                                        if (restauranteServicios != null) {

                                                                            restauranteServicios.setRecojer_en_sucursal(valor_booleano);
                                                                            negocioGeneral.setServicios_disponibles(restauranteServicios);
                                                                            bottomSheetDialog.dismiss();
                                                                            Toast.makeText(Activity_RestauranteConfiguration.this, "(Servicio) Recojer en local = " + valor_booleano, Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    } else
                                                                        //servicio 'entrega a domicilio'

                                                                        if (valueType.equals(negocioObj_typeValues[13])) {

                                                                            RestauranteServicios restauranteServicios = negocioGeneral.getServicios_disponibles();
                                                                            if (restauranteServicios != null) {

                                                                                restauranteServicios.setEntrega_a_domicilio(valor_booleano);
                                                                                negocioGeneral.setServicios_disponibles(restauranteServicios);
                                                                                bottomSheetDialog.dismiss();
                                                                                Toast.makeText(Activity_RestauranteConfiguration.this, "(Servicio) Entrega a domicilio = " + valor_booleano, Toast.LENGTH_SHORT).show();
                                                                            }

                                                                        } else
                                                                            //servicio 'horarios del servicio'

                                                                            if (valueType.equals(negocioObj_typeValues[14])) {

                                                                            } else
                                                                                //metodos de pago
                                                                                if (valueType.equals(negocioObj_typeValues[15])) {

                                                                                } else
                                                                                    //costo fijo en enviar productos al domicilio de un cliente.
                                                                                    if (valueType.equals(negocioObj_typeValues[16])) {

                                                                                        double costo_fijo_de_envio_de_productos = Double.parseDouble((String) valor);
                                                                                        negocioGeneral.setCosto_fijo_de_envio_de_productos(costo_fijo_de_envio_de_productos);
                                                                                        bottomSheetDialog.dismiss();
                                                                                        Toast.makeText(Activity_RestauranteConfiguration.this, "Costo fijo de envío de productos = " + costo_fijo_de_envio_de_productos, Toast.LENGTH_SHORT).show();

                                                                                    } else
                                                                                        //tiempo estimado en preparar un pedido.
                                                                                        if (valueType.equals(negocioObj_typeValues[17])) {

                                                                                            long tiempo_preparacion_minutos = (Long.parseLong( valor.toString() ));
                                                                                            long tiempo_preparacion_seg =  tiempo_preparacion_minutos * 60;
                                                                                            negocioGeneral.setTiempo_estimado_en_preparacion_de_pedido_insec(tiempo_preparacion_seg);
                                                                                            bottomSheetDialog.dismiss();

                                                                                            Toast.makeText(Activity_RestauranteConfiguration.this, "Tiempo en preparar un pedido = "+tiempo_preparacion_minutos, Toast.LENGTH_SHORT).show();
                                                                                        } else
                                                                                            //Pago en efectivo
                                                                                            if (valueType.equals(negocioObj_typeValues[18])) {

                                                                                                MetodosDePago metodosDePago = negocioGeneral.getMetodos_de_pago();
                                                                                                if (metodosDePago == null)
                                                                                                    metodosDePago = new MetodosDePago(true, false);
                                                                                                metodosDePago.setEfectivo(valor_booleano);
                                                                                                negocioGeneral.setMetodos_de_pago(metodosDePago);

                                                                                                bottomSheetDialog.dismiss();
                                                                                                Toast.makeText(Activity_RestauranteConfiguration.this, "Pago en Efectivo = " + valor_booleano, Toast.LENGTH_SHORT).show();
                                                                                            } else
                                                                                                //Pago en tarjeta con terminal
                                                                                                if (valueType.equals(negocioObj_typeValues[19])) {

                                                                                                    MetodosDePago metodosDePago = negocioGeneral.getMetodos_de_pago();
                                                                                                    if (metodosDePago == null)
                                                                                                        metodosDePago = new MetodosDePago(true, false);
                                                                                                    metodosDePago.setTarjeta_terminal(valor_booleano);
                                                                                                    negocioGeneral.setMetodos_de_pago(metodosDePago);

                                                                                                    bottomSheetDialog.dismiss();
                                                                                                    Toast.makeText(Activity_RestauranteConfiguration.this, "Pago en Tarjeta terminal = " + valor_booleano, Toast.LENGTH_SHORT).show();


                                                                                                }

                }

            }
        }, valueType, valor_anterior);


    }


}