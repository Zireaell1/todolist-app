package com.zireaell1.todolist.presentation.todolist;

import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.ViewModel;

import com.zireaell1.todolist.data.todo.ToDoDataSource;
import com.zireaell1.todolist.domain.entities.Sort;
import com.zireaell1.todolist.domain.entities.ToDoState;
import com.zireaell1.todolist.domain.repositories.ToDoRepository;
import com.zireaell1.todolist.domain.usecases.implementations.GetCategoriesUseCase;
import com.zireaell1.todolist.domain.usecases.implementations.GetToDosUseCase;
import com.zireaell1.todolist.domain.usecases.interfaces.GetCategories;
import com.zireaell1.todolist.domain.usecases.interfaces.GetToDos;

public class ToDoListViewModel extends ViewModel {
    private final GetToDos getToDos;
    private final GetCategories getCategories;
    public String searchQuery;
    public Sort sortMethod;
    public int categoryId;
    public ToDoState state;
    public int categoryDropdownSelectedItemId;
    public int sortDropdownSelectedItemId;
    public boolean stateChipIsChecked;
    public ActivityResultLauncher<Intent> toDoAddActivityLauncher;
    public ActivityResultLauncher<Intent> toDoEditActivityLauncher;

    public ToDoListViewModel(Context context) {
        ToDoRepository toDoRepository = new ToDoDataSource(context);

        getToDos = new GetToDosUseCase(toDoRepository);
        getCategories = new GetCategoriesUseCase(toDoRepository);

        searchQuery = "";
        sortMethod = Sort.DESC;
        categoryId = -1;
        state = ToDoState.IN_PROGRESS;
        categoryDropdownSelectedItemId = 0;
        sortDropdownSelectedItemId = 0;
        stateChipIsChecked = false;
    }

    public GetToDos getToDos() {
        return getToDos;
    }

    public GetCategories getCategories() {
        return getCategories;
    }
}
