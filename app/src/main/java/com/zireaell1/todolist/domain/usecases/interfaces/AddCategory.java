package com.zireaell1.todolist.domain.usecases.interfaces;

import com.zireaell1.todolist.domain.entities.Category;

import java.util.concurrent.CompletableFuture;

public interface AddCategory {
    CompletableFuture<Void> execute(Category category);
}
