package com.example.lrodabaugh14.studentattendance;

/**
 * Created by lrodabaugh on 3/7/18.
 */
 import java.io.IOException;
 import java.util.ArrayList;
 import java.util.List;
//import com.google.firebase.messaging.


public class PushNotificationHelper {
    public final static String AUTH_KEY_FCM = "your key ";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
//
//    public static void sendPushNotification(List<String> deviceTokenList) {
//        Sender sender = new Sender(AUTH_KEY_FCM);
//        Message msg = new Message.Builder().addData("message", "Message body")
//                .build();
//        try {
//            MulticastResult result = sender.send(msg, deviceTokenList, 5);
//            for (Result r : result.getResults()) {
//                if (r.getMessageId() != null)
//                    System.out.println("Push Notification Sent Successfully");
//                else
//                    System.out.println("ErrorCode " + r.getErrorCodeName());
//            }
//        } catch (IOException e) {
//            System.out.println("Error " + e.getLocalizedMessage());
//        }
//    }
}