package com.zireaell1.todolist.domain.usecases.implementations;

import com.zireaell1.todolist.domain.repositories.ToDoRepository;
import com.zireaell1.todolist.domain.usecases.interfaces.RemoveCategoryFromToDo;

import java.util.concurrent.CompletableFuture;

public class RemoveCategoryFromToDoUseCase implements RemoveCategoryFromToDo {
    private final ToDoRepository toDoRepository;

    public RemoveCategoryFromToDoUseCase(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @Override
    public CompletableFuture<Void> execute(int categoryId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        new Thread(() -> {
            toDoRepository.removeCategoryFromToDo(categoryId);
            future.complete(null);
        }).start();

        return future;
    }
}
