package com.zireaell1.todolist.domain.usecases.implementations;

import android.util.Log;

import com.zireaell1.todolist.domain.entities.Attachment;
import com.zireaell1.todolist.domain.repositories.ToDoRepository;
import com.zireaell1.todolist.domain.usecases.interfaces.DeleteAttachment;

import java.util.concurrent.CompletableFuture;

public class DeleteAttachmentUseCase implements DeleteAttachment {
    private final ToDoRepository toDoRepository;

    public DeleteAttachmentUseCase(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @Override
    public CompletableFuture<Void> execute(Attachment attachment) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        new Thread(() -> {
            toDoRepository.deleteAttachment(attachment);
            Log.d("DeleteAttachmentUseCase", "Successfully deleted attachment");
            future.complete(null);
        }).start();

        return future;
    }
}
