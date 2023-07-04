package com.zireaell1.todolist.domain.usecases.interfaces;

import java.util.concurrent.CompletableFuture;

public interface DeleteAttachmentByFilePath {
    CompletableFuture<Void> execute(String filePath);
}
