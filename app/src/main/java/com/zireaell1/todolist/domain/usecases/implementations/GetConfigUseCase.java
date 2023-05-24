package com.zireaell1.todolist.domain.usecases.implementations;

import com.zireaell1.todolist.domain.entities.Config;
import com.zireaell1.todolist.domain.repositories.ConfigRepository;
import com.zireaell1.todolist.domain.usecases.interfaces.GetConfig;

public class GetConfigUseCase implements GetConfig {
    private final ConfigRepository configRepository;

    public GetConfigUseCase(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    public Config execute() {
        return new Config(configRepository.getNotificationsReminderTime());
    }
}
