package com.zireaell1.todolist.domain.usecases.implementations;

import android.util.Log;

import com.zireaell1.todolist.domain.entities.ToDo;
import com.zireaell1.todolist.domain.repositories.ToDoRepository;
import com.zireaell1.todolist.domain.usecases.interfaces.GetToDo;

import java.util.concurrent.CompletableFuture;

public class GetToDoUseCase implements GetToDo {
    private final ToDoRepository toDoRepository;

    public GetToDoUseCase(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @Override
    public CompletableFuture<ToDo> execute(int toDoId) {
        CompletableFuture<ToDo> future = new CompletableFuture<>();

        new Thread(() -> {
            Log.d("GetToDoUseCase", "Successfully loaded todo");
            future.complete(toDoRepository.getToDo(toDoId));
        }).start();

        return future;
    }
}
