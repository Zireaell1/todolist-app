package com.zireaell1.todolist.domain.usecases.interfaces;

import com.zireaell1.todolist.domain.entities.ToDo;

import java.util.concurrent.CompletableFuture;

public interface AddToDo {
    CompletableFuture<Long> execute(ToDo toDo);
}
