package com.zireaell1.todolist.domain.usecases.interfaces;

import com.zireaell1.todolist.domain.entities.Attachment;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface GetAttachments {
    CompletableFuture<List<Attachment>> execute(int toDoId);
}
