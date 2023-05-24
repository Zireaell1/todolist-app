package com.zireaell1.todolist.domain.repositories;

public interface ConfigRepository {
    int getNotificationsReminderTime();

    void saveNotificationsReminderTime(int minutes);
}
