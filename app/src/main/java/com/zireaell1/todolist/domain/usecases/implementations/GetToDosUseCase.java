package com.zireaell1.todolist.domain.usecases.implementations;

import android.util.Log;

import com.zireaell1.todolist.domain.entities.Sort;
import com.zireaell1.todolist.domain.entities.ToDo;
import com.zireaell1.todolist.domain.entities.ToDoState;
import com.zireaell1.todolist.domain.repositories.ToDoRepository;
import com.zireaell1.todolist.domain.usecases.interfaces.GetToDos;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GetToDosUseCase implements GetToDos {
    private final ToDoRepository toDoRepository;

    public GetToDosUseCase(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @Override
    public CompletableFuture<List<ToDo>> execute(Sort sort, int categoryId, ToDoState state, String searchQuery) {
        CompletableFuture<List<ToDo>> future = new CompletableFuture<>();

        new Thread(() -> {
            Log.d("GetToDosUseCase", "Successfully loaded todos");
            future.complete(toDoRepository.getToDos(sort, categoryId, state, searchQuery));
        }).start();

        return future;
    }
}
