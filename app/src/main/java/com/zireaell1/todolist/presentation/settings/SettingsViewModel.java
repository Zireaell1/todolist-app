package com.zireaell1.todolist.presentation.settings;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zireaell1.todolist.data.config.ConfigDataSource;
import com.zireaell1.todolist.data.todo.ToDoDataSource;
import com.zireaell1.todolist.domain.entities.Category;
import com.zireaell1.todolist.domain.repositories.ConfigRepository;
import com.zireaell1.todolist.domain.repositories.ToDoRepository;
import com.zireaell1.todolist.domain.usecases.implementations.AddCategoryUseCase;
import com.zireaell1.todolist.domain.usecases.implementations.DeleteCategoryUseCase;
import com.zireaell1.todolist.domain.usecases.implementations.GetCategoriesUseCase;
import com.zireaell1.todolist.domain.usecases.implementations.GetConfigUseCase;
import com.zireaell1.todolist.domain.usecases.implementations.RemoveCategoryFromToDoUseCase;
import com.zireaell1.todolist.domain.usecases.implementations.SaveConfigUseCase;
import com.zireaell1.todolist.domain.usecases.interfaces.AddCategory;
import com.zireaell1.todolist.domain.usecases.interfaces.DeleteCategory;
import com.zireaell1.todolist.domain.usecases.interfaces.GetCategories;
import com.zireaell1.todolist.domain.usecases.interfaces.GetConfig;
import com.zireaell1.todolist.domain.usecases.interfaces.RemoveCategoryFromToDo;
import com.zireaell1.todolist.domain.usecases.interfaces.SaveConfig;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SettingsViewModel extends ViewModel {
    private static final String CONFIG_FILE_NAME = "config.xml";
    private final GetConfig getConfig;
    private final SaveConfig saveConfig;
    private final GetCategories getCategories;
    private final AddCategory addCategory;
    private final DeleteCategory deleteCategory;
    private final RemoveCategoryFromToDo removeCategoryFromToDo;
    private final MutableLiveData<List<Category>> categoriesState = new MutableLiveData<>();

    public SettingsViewModel(Context context) {
        ConfigRepository configRepository = new ConfigDataSource(context, CONFIG_FILE_NAME);
        ToDoRepository toDoRepository = new ToDoDataSource(context);

        getConfig = new GetConfigUseCase(configRepository);
        saveConfig = new SaveConfigUseCase(configRepository);
        getCategories = new GetCategoriesUseCase(toDoRepository);
        addCategory = new AddCategoryUseCase(toDoRepository);
        deleteCategory = new DeleteCategoryUseCase(toDoRepository);
        removeCategoryFromToDo = new RemoveCategoryFromToDoUseCase(toDoRepository);
    }

    public GetConfig getConfig() {
        return getConfig;
    }

    public SaveConfig getSaveConfig() {
        return saveConfig;
    }

    public GetCategories getCategories() {
        return getCategories;
    }

    public AddCategory getAddCategory() {
        return addCategory;
    }

    public DeleteCategory getDeleteCategory() {
        return deleteCategory;
    }

    public RemoveCategoryFromToDo getRemoveCategoryFromToDo() {
        return removeCategoryFromToDo;
    }

    public LiveData<List<Category>> getCategoriesState() {
        return categoriesState;
    }

    public void loadCategories() {
        CompletableFuture<List<Category>> futureCategories = getCategories.execute();
        futureCategories.thenAccept(categoriesState::postValue);
    }
}
