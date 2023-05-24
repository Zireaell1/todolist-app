package com.zireaell1.todolist.domain.usecases.implementations;

import android.util.Log;

import com.zireaell1.todolist.domain.entities.Category;
import com.zireaell1.todolist.domain.repositories.ToDoRepository;
import com.zireaell1.todolist.domain.usecases.interfaces.DeleteCategory;

import java.util.concurrent.CompletableFuture;

public class DeleteCategoryUseCase implements DeleteCategory {
    private final ToDoRepository toDoRepository;

    public DeleteCategoryUseCase(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @Override
    public CompletableFuture<Integer> execute(Category category) {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        new Thread(() -> {
            int result = toDoRepository.deleteCategory(category);
            Log.d("DeleteCategoryUseCase", "Successfully deleted category");
            future.complete(result);
        }).start();

        return future;
    }
}
