package com.zireaell1.todolist.domain.usecases.implementations;

import android.util.Log;

import com.zireaell1.todolist.domain.entities.Attachment;
import com.zireaell1.todolist.domain.repositories.ToDoRepository;
import com.zireaell1.todolist.domain.usecases.interfaces.GetAttachments;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GetAttachmentsUseCase implements GetAttachments {
    private final ToDoRepository toDoRepository;

    public GetAttachmentsUseCase(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @Override
    public CompletableFuture<List<Attachment>> execute(int toDoId) {
        CompletableFuture<List<Attachment>> future = new CompletableFuture<>();

        new Thread(() -> {
            List<Attachment> attachments = toDoRepository.getAttachments(toDoId);
            Log.d("GetAttachmentsUseCase", "Successfully retrieved attachments");
            future.complete(attachments);
        }).start();

        return future;
    }
}
