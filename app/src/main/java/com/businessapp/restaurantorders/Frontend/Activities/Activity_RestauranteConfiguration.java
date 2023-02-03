package com.businessapp.restaurantorders.Frontend.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.businessapp.restaurantorders.Backend.utils.Listeners.ListenerValue;
import com.businessapp.restaurantorders.Backend.utils.Pojos.NegocioGeneral;
import com.businessapp.restaurantorders.Backend.utils.utils.UiFunctions;
import com.businessapp.restaurantorders.Frontend.TreeView.holder.MyHolder;
import com.businessapp.restaurantorders.Frontend.TreeView.model.TreeNode;
import com.businessapp.restaurantorders.Frontend.TreeView.view.AndroidTreeView;
import com.businessapp.restaurantorders.R;

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
            "Costo fijo de envío de productos al domicilio del cliente",
            "Tiempo estimado en preparar un pedido"

    };
    private NegocioGeneral negocioGeneral;

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
                OpenRestaurantSetter(negocioObj_typeValues[0]);
            }
        });

        MyHolder.IconTreeItem childItem_restauranteTelefono = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Teléfono del restaurante");
        TreeNode child_restauranteTelefono = new TreeNode(childItem_restauranteTelefono).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_restauranteTelefono.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[5]);
            }
        });

        MyHolder.IconTreeItem childItem_restauranteEmail = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "E-mail del restaurante");
        TreeNode child_restauranteEmail = new TreeNode(childItem_restauranteEmail).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_restauranteEmail.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[6]);
            }
        });

        MyHolder.IconTreeItem childItem_restauranteAppEmail = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "E-mail de la app del restaurante");
        TreeNode child_restauranteAppEmail = new TreeNode(childItem_restauranteAppEmail).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_restauranteAppEmail.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[7]);
            }
        });

        MyHolder.IconTreeItem childItem_restauranteFacebookLink = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Link de página de Facebook");
        TreeNode child_restauranteFacebookLink = new TreeNode(childItem_restauranteFacebookLink).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_restauranteFacebookLink.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[1]);
            }
        });

        MyHolder.IconTreeItem childItem_restauranteInstagramLink = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Link de página de Instagram");
        TreeNode child_restauranteInstagramLink = new TreeNode(childItem_restauranteInstagramLink).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_restauranteInstagramLink.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[2]);
            }
        });

        MyHolder.IconTreeItem childItem_restauranteTwitterLink = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Link de página de Twitter");
        TreeNode child_restauranteTwitterLink = new TreeNode(childItem_restauranteTwitterLink).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_restauranteTwitterLink.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[3]);
            }
        });

        MyHolder.IconTreeItem childItem_restauranteSitioWebLink = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Link del sitio web del restaurante");
        TreeNode child_restauranteSitioWebLink = new TreeNode(childItem_restauranteSitioWebLink).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_restauranteSitioWebLink.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[4]);
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
        MyHolder.IconTreeItem nodeItem_serviciosyhorarios = new MyHolder.IconTreeItem(R.drawable.ic_arrow_drop_down, "Servicios y horarios");
        TreeNode parent_serviciosyhorarios = new TreeNode(nodeItem_serviciosyhorarios).setViewHolder(new MyHolder(getApplicationContext(), true, MyHolder.DEFAULT, MyHolder.DEFAULT));

        //Child
        MyHolder.IconTreeItem childItem_recojerensucursal = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Para recojer");
        TreeNode child_recojerensucursal = new TreeNode(childItem_recojerensucursal).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_recojerensucursal.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[12]);
            }
        });

        MyHolder.IconTreeItem childItem_entregaDomicilio = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Entrega a domicilio");
        TreeNode child_entregaDomicilio = new TreeNode(childItem_entregaDomicilio).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_entregaDomicilio.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[13]);
            }
        });

        MyHolder.IconTreeItem childItem_horariodelservicio = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Horario del servicio");
        TreeNode child_horariodelservicio = new TreeNode(childItem_horariodelservicio).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_horariodelservicio.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[14]);
            }
        });

        //Add sub child.


        //Add child.
        parent_serviciosyhorarios.addChildren(child_recojerensucursal);
        parent_serviciosyhorarios.addChildren(child_entregaDomicilio);
        parent_serviciosyhorarios.addChildren(child_horariodelservicio);

        root.addChild(parent_serviciosyhorarios);

        /**Pagos e impuestos*/

        //Parent
        MyHolder.IconTreeItem nodeItem_pagoseimpuestos = new MyHolder.IconTreeItem(R.drawable.ic_arrow_drop_down, "Pagos e impuestos");
        TreeNode parent_pagoseimpuestos = new TreeNode(nodeItem_pagoseimpuestos).setViewHolder(new MyHolder(getApplicationContext(), true, MyHolder.DEFAULT, MyHolder.DEFAULT));

        //Child
        MyHolder.IconTreeItem childItem_metodosdepago = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Métodos de pago");
        TreeNode child_metodosdepago = new TreeNode(childItem_metodosdepago).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_metodosdepago.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[15]);
            }
        });
        //Add sub child.


        //Add child.
        parent_pagoseimpuestos.addChildren(child_metodosdepago);

       //todo: root.addChild(parent_pagoseimpuestos);

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
                Intent i = new Intent(Activity_RestauranteConfiguration.this,ActivityRestauranteConfiguracionDelMenu.class);
                startActivity(i);
            }
        });


        MyHolder.IconTreeItem childItem_restaurante_costoDeEnvio = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Costo fijo de envío de productos al domicilio del cliente");
        TreeNode child_restaurante_costoDeEnvio = new TreeNode(childItem_restaurante_costoDeEnvio).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_restaurante_costoDeEnvio.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[16]);
            }
        });

        MyHolder.IconTreeItem childItem_restaurante_tiempoEstimadoEnPrepararUnPedido = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Tiempo estimado en preparar un pedido");
        TreeNode child_restaurante_tiempoEstimadoEnPrepararUnPedido = new TreeNode(childItem_restaurante_tiempoEstimadoEnPrepararUnPedido).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_restaurante_tiempoEstimadoEnPrepararUnPedido.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[17]);
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
                OpenRestaurantSetter(negocioObj_typeValues[11]);
            }
        });

        MyHolder.IconTreeItem childItem_restauranteAppLinkPolicyAndTerms = new MyHolder.IconTreeItem(R.drawable.ic_baseline_radio_button_unchecked_24, "Link de políticas y términos de condiciones de la app del restaurante (En caso de no disponerla se deberá solicitar a la agencia proveedora de los aplicativos)");
        TreeNode child_restauranteAppLinkPolicyAndTerms = new TreeNode(childItem_restauranteAppLinkPolicyAndTerms).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));
        child_restauranteAppLinkPolicyAndTerms.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                OpenRestaurantSetter(negocioObj_typeValues[8]);
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

    private void OpenRestaurantSetter(String valueType) {

        UiFunctions.showBottomSheetDialogPutValue(Activity_RestauranteConfiguration.this, new ListenerValue() {
            @Override
            public void onValueChanged(Object valor) {
                if (valueType != null) {

                    //nombre del restaurante.
                    if (valueType.equals(negocioObj_typeValues[0])) {
                        negocioGeneral.setNombre(valueType);
                    } else
                        //facebook link.
                        if (valueType.equals(negocioObj_typeValues[1])) {
                        negocioGeneral.setFacebook_link(valueType);
                        } else
                            //instagram link.
                            if (valueType.equals(negocioObj_typeValues[2])) {
                                negocioGeneral.setInstagram_link(valueType);

                            } else
                                //twitter link.

                                if (valueType.equals(negocioObj_typeValues[3])) {
                                    negocioGeneral.setTwitter_link(valueType);

                                } else
                                    //sitio web del restaurante.
                                    if (valueType.equals(negocioObj_typeValues[4])) {
                                        negocioGeneral.setWeb_link(valueType);

                                    } else
                                        //telefono del restaurante.
                                        if (valueType.equals(negocioObj_typeValues[5])) {
                                            negocioGeneral.setTelefono(valueType);

                                        } else
                                            //email del restaurante.

                                            if (valueType.equals(negocioObj_typeValues[6])) {
                                                negocioGeneral.setEmail(valueType);

                                            } else
                                                //app email del restaurante.

                                                if (valueType.equals(negocioObj_typeValues[7])) {
                                                    negocioGeneral.setApp_email(valueType);

                                                } else
                                                    //link de politicas de privacidad y terminos de condiciones.

                                                    if (valueType.equals(negocioObj_typeValues[8])) {
                                                        negocioGeneral.setLink_politicasyterminosdecondiciones(valueType);

                                                    } else
                                                        //tiempo estimado en preparación de pedidos.

                                                        if (valueType.equals(negocioObj_typeValues[9])) {
                                                            long tiempo_estimado_en_preparacion_de_pedido_insec = Long.parseLong(valueType);
                                                            negocioGeneral.setTiempo_estimado_en_preparacion_de_pedido_insec(tiempo_estimado_en_preparacion_de_pedido_insec);

                                                        } else
                                                            //costo de despacho fijo.

                                                            if (valueType.equals(negocioObj_typeValues[10])) {
                                                                double costoDespacho = Double.parseDouble(valueType);
                                                                negocioGeneral.setCosto_despacho(costoDespacho);

                                                            } else
                                                                //access code.
                                                                if (valueType.equals(negocioObj_typeValues[11])) {
                                                                    negocioGeneral.setAccess_code(valueType);

                                                                } else
                                                                   //servicio 'recojer en sucursal'
                                                                    if (valueType.equals(negocioObj_typeValues[12])) {

                                                                    } else
                                                                        //servicio 'entrega a domicilio'

                                                                        if (valueType.equals(negocioObj_typeValues[13])) {

                                                                        } else
                                                                            //servicio 'horarios del servicio'

                                                                            if (valueType.equals(negocioObj_typeValues[14])) {

                                                                            } else
                                                                                //metodos de pago
                                                                                if (valueType.equals(negocioObj_typeValues[15])) {

                                                                                } else
                                                                                    //costo fijo en enviar productos al domicilio de un cliente.
                                                                                    if (valueType.equals(negocioObj_typeValues[16])) {


                                                                                    } else
                                                                                        //tiempo estimado en preparar un pedido.
                                                                                        if (valueType.equals(negocioObj_typeValues[17])) {


                                                                                        }

                }
            }

        },valueType);

    }


}