package com.fatiha.ichsannuur.budgetalarm.Reminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.fatiha.ichsannuur.budgetalarm.R;
import com.fatiha.ichsannuur.budgetalarm.Templates.ReminderDialog;

import static android.content.Context.NOTIFICATION_SERVICE;

public class ReminderAlarmService extends BroadcastReceiver {
    private static final String TAG = ReminderAlarmService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 42;
    private static final String NOTIFICATION_CHANNEL = "alarm_channel_01";
    //This is a deep link intent, and needs the task stack
    public static PendingIntent getReminderPendingIntent(Context context, Uri uri) {
        Intent action = new Intent(context, ReminderAlarmService.class);
        action.setData(uri);
        return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Uri uri = intent.getData();

        //Grab the task description
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

        int id = 0;
        String description = "";
        int jumlah_pengeluaran = 0;
        try {
            if (cursor != null && cursor.moveToFirst()) {

                id = cursor.getInt( cursor.getColumnIndex("id") );
                description = cursor.getString( cursor.getColumnIndex("deskripsi") );
                jumlah_pengeluaran = cursor.getInt( cursor.getColumnIndex("jumlah_pengeluaran") );
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        //Display a notification to view the task details
        Intent action = new Intent(context, ReminderDialog.class);
        action.setData(uri);
        action.putExtra("id", id);
        action.putExtra("deskripsi", description);
        action.putExtra("jumlah_pengeluaran", jumlah_pengeluaran);
        PendingIntent operation = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification note = new NotificationCompat.Builder(context)
                .setContentTitle("Alarm Reminder")
                .setContentText(description)
                .setSubText("Rp. " + jumlah_pengeluaran + "")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(operation)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setAutoCancel(true)
                .setChannelId(NOTIFICATION_CHANNEL)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_CHANNEL, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(notificationChannel);
        }

        manager.notify(NOTIFICATION_ID, note);
    }

}
