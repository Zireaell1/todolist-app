package com.zireaell1.todolist.presentation;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.zireaell1.todolist.R;
import com.zireaell1.todolist.presentation.todoedit.ToDoEditActivity;

public class AlarmReceiver extends BroadcastReceiver {
    private final static String NOTIFICATION_CHANNEL_ID = "1";
    private final static int NOTIFICATION_ID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        int toDoId = intent.getIntExtra("toDoId", 1);
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");

        Log.d("AlarmReceiver", String.format("Triggered notification: %s", title));

        Intent resultIntent = new Intent(context, ToDoEditActivity.class);
        resultIntent.putExtra("toDoId", toDoId);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_edit_note_24)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(resultPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // request the missing permission
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
