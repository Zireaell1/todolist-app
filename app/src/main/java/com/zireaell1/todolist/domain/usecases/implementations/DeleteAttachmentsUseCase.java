package com.zireaell1.todolist.domain.usecases.implementations;

import android.util.Log;

import com.zireaell1.todolist.domain.entities.Attachment;
import com.zireaell1.todolist.domain.repositories.ToDoRepository;
import com.zireaell1.todolist.domain.usecases.interfaces.DeleteAttachments;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DeleteAttachmentsUseCase implements DeleteAttachments {
    private final ToDoRepository toDoRepository;

    public DeleteAttachmentsUseCase(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @Override
    public CompletableFuture<Integer> execute(List<Attachment> attachments) {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        new Thread(() -> {
            int deletedAttachments = toDoRepository.deleteAttachments(attachments);
            Log.d("DeleteAttachmentsUseCase", "Successfully deleted attachments");
            future.complete(deletedAttachments);
        }).start();

        return future;
    }
}
