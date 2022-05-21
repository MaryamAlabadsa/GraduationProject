package com.example.graduationproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.graduationproject.activities.MainActivity;
import com.example.graduationproject.activities.SplashActivity;
import com.example.graduationproject.retrofit.Creator;
import com.example.graduationproject.retrofit.ServiceApi;
import com.example.graduationproject.retrofit.categories.AllCategories;
import com.example.graduationproject.retrofit.post.AllPosts;
import com.example.graduationproject.utils.AppSharedPreferences;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.NotificationParams;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String TAG = "MyFirebaseMessagingService";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.e("onNewToken", token);
        AppSharedPreferences sharedPreferences = new AppSharedPreferences(getApplicationContext());
        sharedPreferences.writeString(AppSharedPreferences.DEVICE_TOKEN, token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message_data_payload: " + remoteMessage.getData());
            Map<String, String> params = remoteMessage.getData();
            int post_id=0;
//            JSONArray object = null;
//            try {
//                object = new JSONArray(params);
//                for (int i = 0; i < object.length(); i++) {
//                    JSONObject notificationDetail = object.getJSONObject(i);
//
//                    post_id=notificationDetail.getInt("post_id");
//                }
//                Toast.makeText(this, post_id+"", Toast.LENGTH_SHORT).show();
//
//                Log.e("JSON_ARRAY", object.toString());
//                Log.e("JSON_ARRAY_post_id", post_id+"");
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            JSONObject object = new JSONObject(params);
            Log.e("JSON OBJECT", object.toString());
            try {
                if (object.has("post_id")) {
                    int postId = object.getInt("post_id");
                    Log.d("post_iddd", postId + "");
                    if (remoteMessage.getNotification() != null)
                        sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), postId);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),0);
    }

    //This method is only generating push notification
    private void sendNotification(String messageTitle, String messageBody, int type) {

        PendingIntent contentIntent = null;
        Intent intent = null;

        intent = new Intent(this, SplashActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        contentIntent = PendingIntent.getActivity(this, 0 /* request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        CharSequence tickerText = messageBody;

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "M_CH_ID");

        notificationBuilder.setAutoCancel(true)
                .setSmallIcon(R.drawable.logo)
                .setPriority(Notification.PRIORITY_MAX) // this is deprecated in API 26 but you can still use for below 26. check below update for 26 API
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setContentIntent(contentIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(tickerText));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            String channelId = getString(R.string.app_name);
            NotificationChannel channel = new NotificationChannel(channelId, messageTitle, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(messageBody);
            channel.enableVibration(true);
            channel.setSound(sound, audioAttributes);
            notificationManager.createNotificationChannel(channel);
            notificationBuilder.setChannelId(channelId);
        }

        notificationManager.notify(0, notificationBuilder.build());
//        count++;
    }

}





