package com.zireaell1.todolist.domain.usecases.implementations;

import android.util.Log;

import com.zireaell1.todolist.domain.entities.Category;
import com.zireaell1.todolist.domain.repositories.ToDoRepository;
import com.zireaell1.todolist.domain.usecases.interfaces.GetCategories;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GetCategoriesUseCase implements GetCategories {
    private final ToDoRepository toDoRepository;

    public GetCategoriesUseCase(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @Override
    public CompletableFuture<List<Category>> execute() {
        CompletableFuture<List<Category>> future = new CompletableFuture<>();

        new Thread(() -> {
            Log.d("GetCategoriesUseCase", "Successfully loaded categories");
            future.complete(toDoRepository.getCategories());
        }).start();

        return future;
    }
}
