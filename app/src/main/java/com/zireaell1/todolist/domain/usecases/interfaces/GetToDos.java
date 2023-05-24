package com.zireaell1.todolist.domain.usecases.interfaces;

import com.zireaell1.todolist.domain.entities.Sort;
import com.zireaell1.todolist.domain.entities.ToDo;
import com.zireaell1.todolist.domain.entities.ToDoState;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface GetToDos {
    CompletableFuture<List<ToDo>> execute(Sort sort, int categoryId, ToDoState state, String searchQuery);
}
