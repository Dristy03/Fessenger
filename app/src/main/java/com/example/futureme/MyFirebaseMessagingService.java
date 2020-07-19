package com.example.futureme;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingServ";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);
        String title,body;
        Log.d(TAG, "onMessageReceived: " + remoteMessage);
        if(remoteMessage.getNotification()!=null){
           title = remoteMessage.getNotification().getTitle();
           body = remoteMessage.getNotification().getBody();
        }
        else {
            title = remoteMessage.getData().get("title");
            body =remoteMessage.getData().get("body");
        }
        putNotification(title,body);
    }
    public void putNotification(String title, String body){

        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.mainicon1)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(false)
                .setSound(notificationSoundUri);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        //creating a channel for upper build versions to receive notification

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("1","email received",NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Device to device notification");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(1,notificationBuilder.build());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)FirebaseMessaging.getInstance().subscribeToTopic((FirebaseAuth.getInstance().getCurrentUser().getEmail()).replace('@','_'));
    }
}
