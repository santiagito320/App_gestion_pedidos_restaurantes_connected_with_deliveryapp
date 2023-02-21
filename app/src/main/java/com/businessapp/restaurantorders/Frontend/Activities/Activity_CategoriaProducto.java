package com.businessapp.restaurantorders.Frontend.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.businessapp.restaurantorders.Backend.utils.Constantes;
import com.businessapp.restaurantorders.Backend.utils.Listeners.ListenerProducto;
import com.businessapp.restaurantorders.Backend.utils.Pojos.MenuCategoria;
import com.businessapp.restaurantorders.Backend.utils.Pojos.NegocioGeneral;
import com.businessapp.restaurantorders.Backend.utils.Pojos.NegocioMenu;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Producto;
import com.businessapp.restaurantorders.Backend.utils.utils.UiFunctions;
import com.businessapp.restaurantorders.R;
import com.google.gson.Gson;

public class Activity_CategoriaProducto extends AppCompatActivity {
    //views.
    private Toolbar toolbar;
    private EditText ettxt_categoria_alias;
    private Button btn_categoria_add_product;
    private RecyclerView recyclerView_categoria_productos;
    //data.
    private MenuCategoria menuCategoria;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__categoria_producto);

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setDataInViews();
    }

    private void setDataInViews() {
        if(menuCategoria == null) menuCategoria = new MenuCategoria();

        String toolbar_title = menuCategoria.getAlias() == null ? "Nueva categoría" : menuCategoria.getAlias();
        toolbar.setTitle(toolbar_title);

    }

    private void setData() {
        NegocioGeneral negocioGeneral = Constantes.negocio;
        if(negocioGeneral != null){
            NegocioMenu negocioMenu = negocioGeneral.getMenu();
            if(negocioMenu != null){

                //get categoria
                Bundle extras = getIntent().getExtras();
                if(extras != null){
                    String json_categoria = extras.getString("MenuCategoria");
                    menuCategoria = new Gson().fromJson(json_categoria,MenuCategoria.class);

                    if(menuCategoria == null){
                        // create new category.
                        menuCategoria = new MenuCategoria();
                    }
                }

            }

        }


    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar4);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Activity_CategoriaProducto.this).setTitle("¿Salir sin guardar cambios?").setMessage("Al salir sin guardar los cambios se perdera toda la información previamente registrada. ¿Deseas continuar? ").setIcon(R.drawable.ic_baseline_warning_24)
                        .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).setPositiveButton("Guardar los cambios", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        GuardarCambios();
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });

        btn_categoria_add_product = findViewById(R.id.button_add_product);
        btn_categoria_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiFunctions.showBottomSheetDialog_getProduct(Activity_CategoriaProducto.this,null, new ListenerProducto() {
                    @Override
                    public void onValueChanged(Producto producto) {

                    }
                });
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void GuardarCambios() {

    }
}