package com.businessapp.restaurantorders.Backend.utils;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.businessapp.restaurantorders.Backend.Notifications.Notification;
import com.businessapp.restaurantorders.Backend.Notifications.NotificationSender;
import com.businessapp.restaurantorders.Backend.Notifications.Response;
import com.businessapp.restaurantorders.Backend.Providers.ProviderUsuarios;
import com.businessapp.restaurantorders.Backend.utils.DB.Entidades.Entidad_Restaurante;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Coordenadas;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Destinatario;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Direccion;
import com.businessapp.restaurantorders.Backend.utils.Pojos.NegocioGeneral;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Opcion;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Pedido;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Producto;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Seleccion;
import com.businessapp.restaurantorders.Backend.utils.Pojos.Sucursal;
import com.businessapp.restaurantorders.Backend.utils.utils.UiFunctions;
import com.businessapp.restaurantorders.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;

public class UtilFunctions {

    public static <T> Object ParseJsonToObject(String json, Class<T> classOfT) {
        if (json != null) {


            Gson gson = new Gson();

            T object = gson.fromJson(json, classOfT);

            return object;
        } else return null;
    }


    public static <T> List<T> ParseJsonListToAnyObjectList(String json, Type listType) {
        if (json != null) {
            List<T> list = new ArrayList<>();


            if (json.equals("") || json.equals("[]")) list = new ArrayList<>();
            else list = new Gson().fromJson(json, listType);

            return list;
        } else return new ArrayList<>();
    }

    public static <T> String ParseObjectListToJsonList(List<T> objects) {
        if (objects != null) {
            if (objects.size() > 0) {
                String json_objects = new Gson().toJson(objects);
                return json_objects;
            } else {
                return "[]";
            }

        } else return "[]";
    }


    public static String DoubleToPrecioFormat(double precio) {
        DecimalFormat decimalFormat = new DecimalFormat("#00.00");
        return "$" + decimalFormat.format(precio);
    }

    public static String ParseObjectToJson(Object object) {
        if (object != null) {
            Gson gson = new Gson();

            String json = gson.toJson(object);

            return json;
        } else return "{}";
    }

    public static String getNegocioCloudFirestoreDocumentName(Context context) {
        String appName = context.getString(R.string.app_name);
        StringBuilder document_negocio_name_builder = new StringBuilder(appName);

        String documentName = null;

        for (int i = 0; i < appName.length(); i++) {
            char currentCHar = appName.charAt(i);
            if (currentCHar == ' ') {
                documentName = document_negocio_name_builder.substring(0, i);
            }
        }

        return documentName;
    }

    public static Entidad_Restaurante ParseSucursalTOEntidadRestaurante(Sucursal sucursal) {
        if (sucursal != null) {
            Entidad_Restaurante entidad_restaurante = new Entidad_Restaurante();
            entidad_restaurante.id = 0;
            entidad_restaurante.coordenadas_json = ParseObjectToJson(sucursal.getCoordenadas());
            entidad_restaurante.direccion_absoluta_json = ParseObjectToJson(sucursal.getDireccion_absoluta());
            entidad_restaurante.horarios_array_json = ParseObjectListToJsonList(sucursal.getHorarios());
            entidad_restaurante.imagen_demostrativa_url = sucursal.getImagen_demostrativa_url();
            entidad_restaurante.nombre = sucursal.getNombre();
            entidad_restaurante.radio_de_alcanze_de_pedidos = sucursal.getRadio_de_alcanze_de_pedidos();
            entidad_restaurante.abierto = sucursal.isAbierto();

            return entidad_restaurante;
        } else return null;

    }

    public static List<Pedido> FilterPedidosBySucursalRemitente(List<Pedido> pedidos) {
        if (pedidos != null) {

            if (pedidos.size() > 0) {

                Sucursal miRestauranteSeleccionado = Constantes.MiRestaurante_Seleccionado;
                List<Pedido> mis_pedidos_filtrados = new ArrayList<>();

                if (miRestauranteSeleccionado != null) {

                    for (int i = 0; i < pedidos.size(); i++) {

                        Pedido pedido = pedidos.get(i);

                        if (pedido != null) {

                            Sucursal pedido_sucursal_remitente = pedido.getSucursal();
                            if (pedido_sucursal_remitente != null) {

                                String pedido_sucursal_nombre = pedido_sucursal_remitente.getNombre();

                                if (pedido_sucursal_nombre.equals(miRestauranteSeleccionado.getNombre())) {

                                    //add to the list.
                                    mis_pedidos_filtrados.add(pedido);

                                }


                            }

                        }

                    }
                    return mis_pedidos_filtrados;


                } else return new ArrayList<>();


            } else return pedidos;

        } else return new ArrayList<>();
    }

    public static Sucursal ParseEntidadRestauranteToPojoSucursal(Entidad_Restaurante entidad_restaurante) {

        if (entidad_restaurante != null) {

            String nombre = entidad_restaurante.nombre;
            Direccion direccion_absoluta = (Direccion) UtilFunctions.ParseJsonToObject(entidad_restaurante.direccion_absoluta_json, Direccion.class);
            List<String> horarios_array_json = UtilFunctions.ParseJsonListToAnyObjectList(entidad_restaurante.horarios_array_json, new TypeToken<ArrayList<String>>() {
            }.getType());
            Coordenadas coordenadas = (Coordenadas) UtilFunctions.ParseJsonToObject(entidad_restaurante.coordenadas_json, Coordenadas.class);
            String imagen_demostrativa = entidad_restaurante.imagen_demostrativa_url;
            int radio_de_alcanze = entidad_restaurante.radio_de_alcanze_de_pedidos;


            Sucursal sucursal = new Sucursal(

                    entidad_restaurante.nombre,
                    direccion_absoluta,
                    horarios_array_json,
                    coordenadas,
                    imagen_demostrativa,
                    radio_de_alcanze

            );
            sucursal.setAbierto(entidad_restaurante.abierto);

            return sucursal;

        } else return null;

    }


    public static String TimestampToDate(long born_timestamp) {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault());
        java.util.Date currenTimeZone = new java.util.Date(born_timestamp * 1000);

        return sdf.format(currenTimeZone);
    }

    public static Direccion getDireccionSeleccionada(List<Direccion> direcciones) {
        if (direcciones != null) {
            if (direcciones.size() > 0) {

                Direccion direccion_seleccionada = null;
                for (int i = 0; i < direcciones.size(); i++) {
                    Direccion direccion = direcciones.get(i);
                    if (direccion.isSelected()) {
                        direccion_seleccionada = direccion;
                        break;
                    }
                }

                return direccion_seleccionada;

            } else return null;
        } else return null;
    }

    public static void openGoogleMapsWithLocation(Context context, String latitud, String longitud, String title) {

        if (context != null) {

            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%s,%s (%s)", Float.valueOf(latitud), Float.valueOf(longitud), title);
            Uri myUri = Uri.parse("geo:0,0?q=" + latitud + "," + longitud + "(my city)");

            Intent intent = new Intent(Intent.ACTION_VIEW, myUri);
            intent.setPackage("com.google.android.apps.maps");
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                try {
                    Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    context.startActivity(unrestrictedIntent);
                } catch (ActivityNotFoundException innerEx) {
                    UiFunctions.ShowBasicAlertDialog(context, "¡No hay aplicación de mapas instalado!", "Porfavor instala una aplicación de mapas para poder visualizar la dirección del cliente.", R.drawable.ic_baseline_warning_24, true);
                    Toast.makeText(context, "¡No hay aplicación de mapas instalado!", Toast.LENGTH_LONG).show();
                }
            }


        }

    }

    public static Opcion getProductoSeleccionOpcionSeleccionada(List<Opcion> opciones) {

        Opcion opcionSeleccionada = null;

        if (opciones != null) {

            if (opciones.size() > 0) {

                int opcionesSeleccionadasSize = opciones.size();
                for (int i = 0; i < opcionesSeleccionadasSize; i++) {

                    Opcion opcion = opciones.get(i);
                    if (opcion != null) {

                        if (opcion.isSeleccionado()) {
                            opcionSeleccionada = opcion;
                            break;
                        }

                    }

                }

            }

        }
        return opcionSeleccionada;
    }

    public static double sumAllProductosPrices(List<Producto> productos) {
        if (productos != null) {
            if (productos.size() > 0) {

                double precio_box = 0;
                for (int i = 0; i < productos.size(); i++) {
                    Producto producto = productos.get(i);
                    if (producto != null) {
                        precio_box += producto.getPrecio_total();
                    }
                }
                return precio_box;

            } else return 0;
        } else return 0;
    }

    public static void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawablesRelative()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textView.getContext(), color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    public static void copyPedidoNoteToClipboard(Pedido pedido,Context context) {

        if (pedido != null && context != null) {
            NegocioGeneral negocioGeneral = Constantes.negocio;
            double costo_de_envio = -1;
            if (negocioGeneral != null) {
                costo_de_envio = Constantes.negocio.getCosto_despacho();

            }
            Destinatario destinatario = pedido.getDestinatario();
            String cliente_direccion = "no especificado*";
            if (destinatario != null) {
                Direccion direccionSeleccionada = UtilFunctions.getDireccionSeleccionada(destinatario.getDirecciones());

                if (direccionSeleccionada != null) {

                    cliente_direccion = UtilFunctions.createReadeableDireccion(direccionSeleccionada);

                }
            }
            String productos = UtilFunctions.getProductosToText(pedido.getProductos());
            if (productos.equals("")) productos = "No se seleccionaron productos *";

            String pedido_note =
                    "=== Nuevo pedido ===\n\n" +
                            "Número de pedido: " + pedido.getNumero_pedido() + "\n" +
                            "Solicitado el: " + UtilFunctions.TimestampToDate(pedido.getBorn_timestamp()) +
                            (pedido.getHora_entrega_estimada().equals("") ? "" :  "\n" + "Entrega en: " + pedido.getHora_entrega_estimada()) + "\n\n" +

                            "Tipo de entrega: " + pedido.getTipo_entrega() + "\n" +
                            "Forma de pago: " + pedido.getMedio_de_pago() + "\n\n" +

                            "SubTotales: " + UtilFunctions.DoubleToPrecioFormat(UtilFunctions.sumAllProductosPrices(pedido.getProductos())) + "\n" +
                            (pedido.getTipo_entrega().equals(PedidoUtils.OrderReceive_Mode2) && costo_de_envio != -1 ?
                                    "Costo de envío: " + UtilFunctions.DoubleToPrecioFormat(costo_de_envio) + "\n" : "") +
                            "Total: " + UtilFunctions.DoubleToPrecioFormat(pedido.getPago_total()) + (destinatario != null ? "\n\n" +

                            "=== Información del cliente ===\n\n" +

                            "Nombre: " + destinatario.getNombre() + " " +destinatario.getApellido() + "\n" +
                            "Correo electrónico: " + destinatario.getEmail() + "\n" +
                            "Teléfono: " + destinatario.getTelefono() + "\n" +
                            "Dirección: " + cliente_direccion + "\n"


                            : "") + "\n\n" +

                            "=== Artículos ===\n\n" +

                            productos
                            + "=== Fin del pedido ===";


            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Nota del pedido #"+pedido.getNumero_pedido(), pedido_note);
            clipboard.setPrimaryClip(clip);

        }
    }

    private static String getProductosToText(List<Producto> productos) {

        if (productos != null) {

            if (productos.size() > 0) {

                String productosText = "";
                StringBuilder productosTextBuilder = new StringBuilder(productosText);

                for (int i = 0; i < productos.size(); i++) {

                    String producto_text = "";
                    StringBuilder producto_text_builder = new StringBuilder(producto_text);

                    Producto producto = productos.get(i);

                    if (producto != null) {

                        producto_text_builder.append(producto.getCantidad()).append(" x ").append(producto.getTitulo()).append(" ").append(UtilFunctions.DoubleToPrecioFormat(producto.getPrecio_total()));
                        producto_text_builder.append(UtilFunctions.productoSeleccionesToText(producto.getSelecciones())).append("\n");
                        producto_text_builder.append(producto.getInstruccion_especial()).append("\n-------------------------\n\n ");

                        producto_text = producto_text_builder.toString();
                        productosTextBuilder.append(producto_text);
                    }


                }
                productosText = productosTextBuilder.toString();

                return productosText;

            } else return "";

        } else return "";


    }

    private static String productoSeleccionesToText(List<Seleccion> selecciones) {

        if (selecciones != null) {

            if (selecciones.size() > 0) {

                String seleccionesText = "";
                StringBuilder seleccionesTextBuilder = new StringBuilder(seleccionesText);

                for (int i = 0; i < selecciones.size(); i++) {

                    String seleccionText = "";

                    Seleccion seleccion = selecciones.get(i);
                    if (seleccion != null) {
                        Opcion opcionSeleccionada = UtilFunctions.getProductoSeleccionOpcionSeleccionada(seleccion.getOpciones());
                        if (opcionSeleccionada != null) {
                            seleccionText = seleccion.getTitulo() + ": " + opcionSeleccionada.getNombre() + (opcionSeleccionada.getCosto_extra() > 0 ? " " + UtilFunctions.DoubleToPrecioFormat(opcionSeleccionada.getCosto_extra()) : "");

                            seleccionesTextBuilder.append("\n").append(seleccionText);
                        }

                    }

                }
                seleccionesText = seleccionesTextBuilder.toString();

                return seleccionesText;

            } else return "";

        } else return "";

    }

    private static String createReadeableDireccion(Direccion direccion) {
        if (direccion != null) {

            return direccion.getCalle() + " #" + direccion.getNum_exterior() + (direccion.getNum_interior().equals("") ? "" :  " (°num interior: " + direccion.getNum_interior() + ")") + ", " + direccion.getColonia() + " CP." + direccion.getCodigo_postal() + ", " + direccion.getCiudad();

        } else return "";
    }

    public static void sendNotificationToFirebaseClient(Context context, String client_firebase_uid,Notification notification) {
        /** SEND NOTIFICATION TO CLIENT **/
        //GET TOKEN
        if(client_firebase_uid != null && context != null){
            if(!client_firebase_uid.equals("")){

                ProviderUsuarios providerUsuarios = new ProviderUsuarios(context);
                providerUsuarios.getUsuario(client_firebase_uid).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot != null){
                            Destinatario destinatario = documentSnapshot.toObject(Destinatario.class);

                            if(destinatario != null){

                                String token = destinatario.getToken();

                                //SEND NOTIFICATION TO CLIENT (TOKEN)
                                NotificationSender notificationSender = new NotificationSender();


                                notificationSender.sendNotification(context,token,notification).enqueue(new Callback<Response>() {
                                    @Override
                                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                        Response response_body = response.body();
                                        if(response_body != null){
                                            if(response_body.getSuccess() == 1){
                                                Toast.makeText(context, "El cliente ha sido notificado sobre el cambio de estado de su pedido", Toast.LENGTH_LONG).show();
                                            }else if (response_body.getFailure() == 1){
                                                System.err.println("Error al enviar notificación al cliente, ex = "+response.errorBody().toString());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Response> call, Throwable t) {
                                        t.printStackTrace();
                                        System.err.println("Error al enviar notificacion al cliente, ex = "+t.getMessage());

                                    }
                                });

                            }

                        }
                    }
                });
            }
        }

    }
}
