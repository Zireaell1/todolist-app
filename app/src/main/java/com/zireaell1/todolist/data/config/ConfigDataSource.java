package com.zireaell1.todolist.data.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.zireaell1.todolist.R;
import com.zireaell1.todolist.domain.repositories.ConfigRepository;

public class ConfigDataSource implements ConfigRepository {
    private static final String NOTIFICATIONS_REMINDER_TIME_KEY = "notificationsReminderTime";
    private final SharedPreferences sharedPreferences;
    private final Context context;

    public ConfigDataSource(Context context, String configName) {
        sharedPreferences = context.getSharedPreferences(configName, Context.MODE_PRIVATE);
        this.context = context;
    }

    @Override
    public int getNotificationsReminderTime() {
        int defaultValue = context.getResources().getInteger(R.integer.notifications_reminder_time_default);
        return sharedPreferences.getInt(NOTIFICATIONS_REMINDER_TIME_KEY, defaultValue);
    }

    @Override
    public void saveNotificationsReminderTime(int minutes) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(NOTIFICATIONS_REMINDER_TIME_KEY, minutes);
        editor.apply();
    }
}
