package com.zireaell1.todolist.domain.usecases.interfaces;

import com.zireaell1.todolist.domain.entities.Config;

public interface SaveConfig {
    void execute(Config config);
}
