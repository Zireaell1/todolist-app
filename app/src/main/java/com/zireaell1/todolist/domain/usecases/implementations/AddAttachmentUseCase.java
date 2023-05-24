package com.zireaell1.todolist.domain.usecases.implementations;

import android.util.Log;

import com.zireaell1.todolist.domain.entities.Attachment;
import com.zireaell1.todolist.domain.repositories.ToDoRepository;
import com.zireaell1.todolist.domain.usecases.interfaces.AddAttachment;

import java.util.concurrent.CompletableFuture;

public class AddAttachmentUseCase implements AddAttachment {
    private final ToDoRepository toDoRepository;

    public AddAttachmentUseCase(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @Override
    public CompletableFuture<Void> execute(Attachment attachment) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        new Thread(() -> {
            toDoRepository.saveAttachment(attachment);
            Log.d("AddAttachmentUseCase", "Successfully added attachment");
            future.complete(null);
        }).start();

        return future;
    }
}
