package com.zireaell1.todolist.presentation.todolist;

import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zireaell1.todolist.R;
import com.zireaell1.todolist.data.todo.ToDoDataSource;
import com.zireaell1.todolist.domain.entities.Category;
import com.zireaell1.todolist.domain.entities.Sort;
import com.zireaell1.todolist.domain.entities.ToDo;
import com.zireaell1.todolist.domain.entities.ToDoState;
import com.zireaell1.todolist.domain.repositories.ToDoRepository;
import com.zireaell1.todolist.domain.usecases.implementations.GetCategoriesUseCase;
import com.zireaell1.todolist.domain.usecases.implementations.GetToDosUseCase;
import com.zireaell1.todolist.domain.usecases.interfaces.GetCategories;
import com.zireaell1.todolist.domain.usecases.interfaces.GetToDos;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ToDoListViewModel extends ViewModel {
    private final GetToDos getToDos;
    private final GetCategories getCategories;
    private final MutableLiveData<List<ToDo>> toDosState = new MutableLiveData<>();
    private final MutableLiveData<List<Category>> categoriesState = new MutableLiveData<>();
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

    public LiveData<List<ToDo>> getToDosState() {
        return toDosState;
    }

    public LiveData<List<Category>> getCategoriesState() {
        return categoriesState;
    }

    public void loadToDos() {
        CompletableFuture<List<ToDo>> futureToDos = getToDos.execute(sortMethod, categoryId, state, searchQuery);
        futureToDos.thenAccept(toDosState::postValue);
    }

    public void loadCategories(Context context) {
        CompletableFuture<List<Category>> futureCategories = getCategories.execute();
        futureCategories.thenAccept(categories -> {
            categories.add(0, new Category(-1, context.getString(R.string.category_none)));
            categoriesState.postValue(categories);
        });
    }
}
