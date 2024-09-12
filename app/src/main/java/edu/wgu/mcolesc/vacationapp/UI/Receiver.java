package edu.wgu.mcolesc.vacationapp.UI;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import edu.wgu.mcolesc.vacationapp.R;

public class Receiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "notification";
    String channel_id = "test";
    static int notificationID;

    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        createNotificationChannel(context, CHANNEL_ID);
        NotificationCompat.Builder mBuilder = null;

        if (intent.hasExtra("key1")) {
            mBuilder = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentText(intent.getStringExtra("vacation_title") + ": " + intent.getStringExtra("key1"));
        }
        else if (intent.hasExtra("key2")) {
            mBuilder = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentText(intent.getStringExtra("vacation_title") + ": " + intent.getStringExtra("key2"));

        }
        else if (intent.hasExtra("key3")) {
            mBuilder = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentText(intent.getStringExtra("excursion_title") + ": " + intent.getStringExtra("key3"));

        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert mBuilder != null;
        notificationManager.notify(notificationID++, mBuilder.build());
    }

    private void createNotificationChannel (Context context, String CHANNEL_ID) {
        CharSequence name = "mychannelname";
        String description = "mychanneldescription";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID, name, importance);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.setDescription(description);
        }

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }


    }
}