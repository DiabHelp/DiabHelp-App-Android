package fr.diabhelp.diabhelp.Services;

import android.content.ComponentName;
import android.content.Intent;
import android.util.ArrayMap;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import fr.diabhelp.diabhelp.Core.AccueilFragment;
import fr.diabhelp.diabhelp.Utils.NotificationsDatasHandler;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Map<String, String> datas = remoteMessage.getData();
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            handleDatas(datas);
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    public void handleDatas(Map<String, String> datas)
    {
        NotificationsDatasHandler.handle(datas, getApplicationContext());

    }
}
