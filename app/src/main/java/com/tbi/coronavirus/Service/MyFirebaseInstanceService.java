package com.tbi.coronavirus.Service;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tbi.coronavirus.Activity.MainActivity;
import com.tbi.coronavirus.Pref.sharedprefrance;
import com.tbi.coronavirus.R;

import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseInstanceService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseIIDService";
    NotificationManager notificationManager;
    NotificationCompat.Builder notificationBuilder;

    public static final String ANDROID_CHANNEL_ID = "com.tbi.coronavirus";
    public static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL";
    Context context;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("mn13hellowowrld:", "enter");

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("LLLL_firebase_Token", "Refreshed token: " + newToken);
                sharedprefrance.setfirebasetoken(getApplicationContext(), newToken);
            }
        });
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "mn13From: " + remoteMessage.getFrom());

        context = this;

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels();
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() > 0) {
                sendNotification(remoteMessage.getData());
        }

    }
    // [END receive_message]


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels() {
        AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build();
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        CharSequence adminChannelName = ANDROID_CHANNEL_NAME;
        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ANDROID_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        adminChannel.setSound(defaultSoundUri, attributes);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(Map<String, String> messageBody) {

        try {
            Log.d("LLLL_Mess_Body", messageBody.toString() + "");
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_foreground);
            notificationBuilder = new NotificationCompat.Builder(context, ANDROID_CHANNEL_ID);

            JSONObject object = new JSONObject(messageBody);
            //   JSONObject object = new JSONObject(messageBody.toString());

            String message = object.optString("body");
            String title = object.optString("title");
            int id_notifaciton = (int) System.currentTimeMillis();
            notificationBuilder
                    .setSmallIcon(R.drawable.ic_launcher_foreground).setLargeIcon(icon)//a resource for your custom small icon
                    .setContentTitle(title)
                    .setWhen(System.currentTimeMillis())
                    .setShowWhen(true)//the "title" value you sent in your notification
                    .setContentText(message) //ditto
                    .setContentIntent(buildContentIntent(context, MainActivity.class, 0,"map"))
                    .setAutoCancel(true)  //dismisses the notification on click
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            notificationManager.notify(id_notifaciton /* ID of notification */, notificationBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static PendingIntent buildContentIntent(Context context, Class<? extends Activity> activityClass, int order_id, String type) {
        Intent intent = new Intent(context, activityClass);
        intent.putExtra("type",type);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

   /* private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.noti_icon : R.drawable.notiicon;
    }*/

}
