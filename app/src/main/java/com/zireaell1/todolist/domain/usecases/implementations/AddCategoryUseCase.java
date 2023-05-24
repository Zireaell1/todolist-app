package com.zireaell1.todolist.domain.usecases.implementations;

import com.zireaell1.todolist.domain.entities.Category;
import com.zireaell1.todolist.domain.repositories.ToDoRepository;
import com.zireaell1.todolist.domain.usecases.interfaces.AddCategory;

import java.util.concurrent.CompletableFuture;

public class AddCategoryUseCase implements AddCategory {
    private final ToDoRepository toDoRepository;

    public AddCategoryUseCase(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @Override
    public CompletableFuture<Void> execute(Category category) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        new Thread(() -> {
            toDoRepository.saveCategory(category);
            future.complete(null);
        }).start();

        return future;
    }
}
