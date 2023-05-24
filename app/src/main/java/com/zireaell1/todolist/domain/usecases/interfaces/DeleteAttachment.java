package com.zireaell1.todolist.domain.usecases.interfaces;

import com.zireaell1.todolist.domain.entities.Attachment;

import java.util.concurrent.CompletableFuture;

public interface DeleteAttachment {
    CompletableFuture<Void> execute(Attachment attachment);
}
