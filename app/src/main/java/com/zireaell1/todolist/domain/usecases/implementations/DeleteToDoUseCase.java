package com.zireaell1.todolist.domain.usecases.implementations;

import android.util.Log;

import com.zireaell1.todolist.domain.entities.ToDo;
import com.zireaell1.todolist.domain.repositories.ToDoRepository;
import com.zireaell1.todolist.domain.usecases.interfaces.DeleteToDo;

import java.util.concurrent.CompletableFuture;

public class DeleteToDoUseCase implements DeleteToDo {
    private final ToDoRepository toDoRepository;

    public DeleteToDoUseCase(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @Override
    public CompletableFuture<Integer> execute(ToDo toDo) {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        new Thread(() -> {
            int result = toDoRepository.deleteToDo(toDo);
            Log.d("DeleteToDoUseCase", "Successfully deleted ToDo");
            future.complete(result);
        }).start();

        return future;
    }
}
