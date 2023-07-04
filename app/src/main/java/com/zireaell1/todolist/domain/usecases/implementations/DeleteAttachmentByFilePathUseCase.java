package com.zireaell1.todolist.domain.usecases.implementations;

import com.zireaell1.todolist.domain.repositories.ToDoRepository;
import com.zireaell1.todolist.domain.usecases.interfaces.DeleteAttachmentByFilePath;

import java.util.concurrent.CompletableFuture;

public class DeleteAttachmentByFilePathUseCase implements DeleteAttachmentByFilePath {
    private final ToDoRepository toDoRepository;

    public DeleteAttachmentByFilePathUseCase(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @Override
    public CompletableFuture<Void> execute(String filePath) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        new Thread(() -> {
            toDoRepository.deleteAttachmentByFilePath(filePath);
            future.complete(null);
        }).start();

        return future;
    }
}
