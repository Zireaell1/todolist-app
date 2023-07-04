package com.zireaell1.todolist.domain.usecases.interfaces;

import java.util.concurrent.CompletableFuture;

public interface RemoveCategoryFromToDo {
    CompletableFuture<Void> execute(int categoryId);
}
