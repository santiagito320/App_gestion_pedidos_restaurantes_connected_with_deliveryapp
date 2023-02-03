package com.businessapp.restaurantorders.Backend.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface WS {

    //todo: probar parametro en el metodo para la api key
    @Headers({"Content-Type:application/json"})
    @POST("fcm/send")
    Call<Response> enviar_notificacion(@Body Sender body, @Header("Authorization") String key);

}
