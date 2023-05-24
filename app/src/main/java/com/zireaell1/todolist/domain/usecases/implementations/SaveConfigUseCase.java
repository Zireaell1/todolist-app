package com.zireaell1.todolist.domain.usecases.implementations;

import com.zireaell1.todolist.domain.entities.Config;
import com.zireaell1.todolist.domain.repositories.ConfigRepository;
import com.zireaell1.todolist.domain.usecases.interfaces.SaveConfig;

public class SaveConfigUseCase implements SaveConfig {
    private final ConfigRepository configRepository;

    public SaveConfigUseCase(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    public void execute(Config config) {
        configRepository.saveNotificationsReminderTime(config.getNotificationsReminderTime());
    }
}
