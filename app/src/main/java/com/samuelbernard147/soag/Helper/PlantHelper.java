package com.samuelbernard147.soag.Helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.samuelbernard147.soag.MainActivity;
import com.samuelbernard147.soag.R;
import com.samuelbernard147.soag.preference.TimerPreference;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import cz.msebera.android.httpclient.Header;

import static android.app.Notification.DEFAULT_LIGHTS;
import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;

public class PlantHelper {
    private TimerPreference pref;

    public PlantHelper(Context context) {
        pref = new TimerPreference(context);
    }

    public void postStatus(final String tagName, String status) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://soag.starbhaktefa.com/public/" + tagName + "/" + status;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

    public void loadPlant(final String tagName, final String tagInterval) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://soag.starbhaktefa.com/public/" + tagName;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    if (responseObject.getInt("status") == 1) {
                        pref.setStatus(tagName, true);
                        pref.setInterval(tagInterval, responseObject.getInt("selisih"));
                    } else {
                        pref.setStatus(tagName, false);
                        pref.clearPreference(TimerPreference.CHILI_INTERVAL);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

    public String timeConverter(Long millis) {
        NumberFormat f = new DecimalFormat("00");
        long hour = (millis / 3600000) % 24;
        long min = (millis / 60000) % 60;
        long sec = (millis / 1000) % 60;
        return f.format(hour) + ":" + f.format(min) + ":" + f.format(sec);
    }

    // Memunculkan notifikasi
    public void showNotification(Context context, String title, String message, int notifId) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager notificationManagerCompat = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, String.valueOf(notifId))
                .setSmallIcon(R.drawable.ic_notifications_white)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE | DEFAULT_LIGHTS)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setContentIntent(pendingIntent);

//        Android oreo keatas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /* Create or update. */
            NotificationChannel channel = new NotificationChannel(String.valueOf(notifId),
                    title + " Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(String.valueOf(notifId));

            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }
    }
}