package com.zireaell1.todolist.domain.entities;

import androidx.annotation.Nullable;

public class Config {
    private final int notificationsReminderTime;

    public Config(int notificationsReminderTime) {
        this.notificationsReminderTime = notificationsReminderTime;
    }

    public int getNotificationsReminderTime() {
        return notificationsReminderTime;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        Config config = (Config) obj;
        return notificationsReminderTime == config.notificationsReminderTime;
    }
}
