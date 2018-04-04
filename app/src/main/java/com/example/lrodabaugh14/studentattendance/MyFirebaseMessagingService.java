package com.example.lrodabaugh14.studentattendance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.internal.FirebaseAppHelper;
import com.google.firebase.iid.*;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM Service";
    private static final String SENDER_ID = "1";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }
//    public void SendMessage(){
//        FirebaseMessaging.getInstance().send(
//                new RemoteMessage.Builder(SENDER_ID + "@gcm.googleapis.com")
//                        .setMessageId(String.valueOf(1))
//                        .addData("key", "value")
//                        .build());
//        );
//    }

}


