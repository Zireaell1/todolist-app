package com.zireaell1.todolist.domain.usecases.implementations;

import android.util.Log;

import com.zireaell1.todolist.domain.entities.Category;
import com.zireaell1.todolist.domain.repositories.ToDoRepository;
import com.zireaell1.todolist.domain.usecases.interfaces.EditCategory;

import java.util.concurrent.CompletableFuture;

public class EditCategoryUseCase implements EditCategory {
    private final ToDoRepository toDoRepository;

    public EditCategoryUseCase(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @Override
    public CompletableFuture<Integer> execute(Category category) {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        new Thread(() -> {
            int result = toDoRepository.updateCategory(category);
            Log.d("EditCategoryUseCase", "Successfully edited category");
            future.complete(result);
        }).start();

        return future;
    }
}
