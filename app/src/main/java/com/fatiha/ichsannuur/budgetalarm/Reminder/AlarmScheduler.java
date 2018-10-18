package com.fatiha.ichsannuur.budgetalarm.Reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;


public class AlarmScheduler {
    /**
     * Schedule a reminder alarm at the specified time for the given task.
     *
     * @param context Local application or activity context
     * @param reminderTask Uri referencing the task in the content provider
     */

    public void setAlarm(Context context, long alarmTime, Uri reminderTask) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation =
                ReminderAlarmService.getReminderPendingIntent(context, reminderTask);

        if (Build.VERSION.SDK_INT >= 23) {

            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, operation);

        } else if (Build.VERSION.SDK_INT >= 19) {

            manager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, operation);

        } else {

            manager.set(AlarmManager.RTC_WAKEUP, alarmTime, operation);

        }
    }

    public void setRepeatAlarm(Context context, long alarmTime, Uri reminderTask, long RepeatTime) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(context, ReminderAlarmService.class);
        i.setData(reminderTask);
        PendingIntent operation = PendingIntent.getBroadcast(context, 0, i,0);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, RepeatTime, operation);
    }

    public void cancelAlarm(Context context, Uri reminderTask) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(context, ReminderAlarmService.class);
        i.setData(reminderTask);
        PendingIntent operation = PendingIntent.getBroadcast(context, 0, i,0);

        manager.cancel(operation);

    }
}
