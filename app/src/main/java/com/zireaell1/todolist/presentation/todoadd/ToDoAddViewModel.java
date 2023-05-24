package com.zireaell1.todolist.presentation.todoadd;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.ViewModel;

import com.zireaell1.todolist.data.config.ConfigDataSource;
import com.zireaell1.todolist.data.todo.ToDoDataSource;
import com.zireaell1.todolist.domain.entities.ToDoState;
import com.zireaell1.todolist.domain.repositories.ConfigRepository;
import com.zireaell1.todolist.domain.repositories.ToDoRepository;
import com.zireaell1.todolist.domain.usecases.implementations.AddAttachmentUseCase;
import com.zireaell1.todolist.domain.usecases.implementations.AddToDoUseCase;
import com.zireaell1.todolist.domain.usecases.implementations.GetCategoriesUseCase;
import com.zireaell1.todolist.domain.usecases.implementations.GetConfigUseCase;
import com.zireaell1.todolist.domain.usecases.interfaces.AddAttachment;
import com.zireaell1.todolist.domain.usecases.interfaces.AddToDo;
import com.zireaell1.todolist.domain.usecases.interfaces.GetCategories;
import com.zireaell1.todolist.domain.usecases.interfaces.GetConfig;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ToDoAddViewModel extends ViewModel {
    private final AddToDo addToDo;
    private final GetCategories getCategories;
    private final AddAttachment addAttachment;
    private final GetConfig getConfig;
    public int categoryId;
    public ToDoState state;
    public boolean notifications;
    public int categoryDropdownSelectedItemId;
    public boolean stateChipIsChecked;
    public boolean notificationsChipIsChecked;
    public int dayOfMonth;
    public int month;
    public int year;
    public int hourOfDay;
    public int minute;
    public boolean isTimeSet;
    public ActivityResultLauncher<Intent> chooseFileLauncher;
    public ActivityResultLauncher<Intent> saveFileLauncher;
    public List<Uri> fileUris;
    public Uri fileToSave;

    public ToDoAddViewModel(Context context) {
        ToDoRepository toDoRepository = new ToDoDataSource(context);
        ConfigRepository configRepository = new ConfigDataSource(context, "config.xml");

        addToDo = new AddToDoUseCase(toDoRepository);
        getCategories = new GetCategoriesUseCase(toDoRepository);
        addAttachment = new AddAttachmentUseCase(toDoRepository);
        getConfig = new GetConfigUseCase(configRepository);

        categoryId = -1;
        state = ToDoState.IN_PROGRESS;
        notifications = false;
        categoryDropdownSelectedItemId = 0;
        stateChipIsChecked = false;
        notificationsChipIsChecked = false;

        LocalDateTime localDateTime = LocalDateTime.now();
        dayOfMonth = localDateTime.getDayOfMonth();
        month = localDateTime.getMonthValue();
        year = localDateTime.getYear();
        hourOfDay = localDateTime.getHour();
        minute = localDateTime.getMinute();
        isTimeSet = false;

        fileUris = new ArrayList<>();
    }

    public AddToDo getAddToDo() {
        return addToDo;
    }

    public GetCategories getCategories() {
        return getCategories;
    }

    public AddAttachment getAddAttachment() {
        return addAttachment;
    }

    public GetConfig getConfig() {
        return getConfig;
    }
}
