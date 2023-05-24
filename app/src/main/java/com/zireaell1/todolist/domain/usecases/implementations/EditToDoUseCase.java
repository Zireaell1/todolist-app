package com.zireaell1.todolist.domain.usecases.implementations;

import android.util.Log;

import com.zireaell1.todolist.domain.entities.ToDo;
import com.zireaell1.todolist.domain.repositories.ToDoRepository;
import com.zireaell1.todolist.domain.usecases.interfaces.EditToDo;

import java.util.concurrent.CompletableFuture;

public class EditToDoUseCase implements EditToDo {
    private final ToDoRepository toDoRepository;

    public EditToDoUseCase(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @Override
    public CompletableFuture<Integer> execute(ToDo toDo) {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        new Thread(() -> {
            int result = toDoRepository.updateToDo(toDo);
            Log.d("EditToDoUseCase", "Successfully edited ToDo");
            future.complete(result);
        }).start();

        return future;
    }
}
