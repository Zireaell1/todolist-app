package com.zireaell1.todolist.presentation.settings;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.zireaell1.todolist.data.config.ConfigDataSource;
import com.zireaell1.todolist.data.todo.ToDoDataSource;
import com.zireaell1.todolist.domain.repositories.ConfigRepository;
import com.zireaell1.todolist.domain.repositories.ToDoRepository;
import com.zireaell1.todolist.domain.usecases.implementations.AddCategoryUseCase;
import com.zireaell1.todolist.domain.usecases.implementations.DeleteCategoryUseCase;
import com.zireaell1.todolist.domain.usecases.implementations.GetCategoriesUseCase;
import com.zireaell1.todolist.domain.usecases.implementations.GetConfigUseCase;
import com.zireaell1.todolist.domain.usecases.implementations.SaveConfigUseCase;
import com.zireaell1.todolist.domain.usecases.interfaces.AddCategory;
import com.zireaell1.todolist.domain.usecases.interfaces.DeleteCategory;
import com.zireaell1.todolist.domain.usecases.interfaces.GetCategories;
import com.zireaell1.todolist.domain.usecases.interfaces.GetConfig;
import com.zireaell1.todolist.domain.usecases.interfaces.SaveConfig;

public class SettingsViewModel extends ViewModel {
    private final GetConfig getConfig;
    private final SaveConfig saveConfig;
    private final GetCategories getCategories;
    private final AddCategory addCategory;
    private final DeleteCategory deleteCategory;

    public SettingsViewModel(Context context) {
        ConfigRepository configRepository = new ConfigDataSource(context, "config.xml");
        ToDoRepository toDoRepository = new ToDoDataSource(context);

        getConfig = new GetConfigUseCase(configRepository);
        saveConfig = new SaveConfigUseCase(configRepository);
        getCategories = new GetCategoriesUseCase(toDoRepository);
        addCategory = new AddCategoryUseCase(toDoRepository);
        deleteCategory = new DeleteCategoryUseCase(toDoRepository);
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
}
