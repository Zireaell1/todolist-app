package com.zireaell1.todolist.domain.usecases.implementations;

import android.util.Log;

import com.zireaell1.todolist.domain.entities.ToDo;
import com.zireaell1.todolist.domain.repositories.ToDoRepository;
import com.zireaell1.todolist.domain.usecases.interfaces.AddToDo;

import java.util.concurrent.CompletableFuture;

public class AddToDoUseCase implements AddToDo {
    private final ToDoRepository toDoRepository;

    public AddToDoUseCase(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @Override
    public CompletableFuture<Long> execute(ToDo toDo) {
        CompletableFuture<Long> future = new CompletableFuture<>();

        new Thread(() -> {
            long id = toDoRepository.saveToDo(toDo);
            Log.d("AddToDoUseCase", "Successfully added new todo");
            future.complete(id);
        }).start();

        return future;
    }
}
