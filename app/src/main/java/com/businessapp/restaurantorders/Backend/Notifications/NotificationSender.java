package com.businessapp.restaurantorders.Backend.Notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.businessapp.restaurantorders.Frontend.Activities.MainActivity;
import com.businessapp.restaurantorders.R;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationSender {


    public Call<Response> sendNotification(Context context,String mobile_token, Notification notification){

        if(context != null && notification != null && mobile_token != null){

            String api_key = context.getString(R.string.firebase_cloud_messaging_apiKey);

            Sender notificationSender = new Sender(mobile_token,notification);
            WS ws = new Retrofit.Builder().baseUrl("https://fcm.googleapis.com/").addConverterFactory(GsonConverterFactory.create()).build().create(WS.class);


            return ws.enviar_notificacion(notificationSender,"key="+api_key);
        }else return null;

    }
    public void sendLocalNotification(Context context,String title, String message, int smallIcon){

        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(context,"");
        notificationCompat
                .setSmallIcon(smallIcon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSound(notificationSound)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
        ;

        NotificationManager notificationManager = (NotificationManager)  context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationCompat.build());

    }

}
