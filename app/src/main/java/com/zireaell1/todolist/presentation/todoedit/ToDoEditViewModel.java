package com.zireaell1.todolist.presentation.todoedit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.ViewModel;

import com.zireaell1.todolist.data.config.ConfigDataSource;
import com.zireaell1.todolist.data.todo.ToDoDataSource;
import com.zireaell1.todolist.domain.entities.Category;
import com.zireaell1.todolist.domain.entities.ToDo;
import com.zireaell1.todolist.domain.entities.ToDoState;
import com.zireaell1.todolist.domain.repositories.ConfigRepository;
import com.zireaell1.todolist.domain.repositories.ToDoRepository;
import com.zireaell1.todolist.domain.usecases.implementations.AddAttachmentUseCase;
import com.zireaell1.todolist.domain.usecases.implementations.DeleteAttachmentUseCase;
import com.zireaell1.todolist.domain.usecases.implementations.DeleteAttachmentsUseCase;
import com.zireaell1.todolist.domain.usecases.implementations.DeleteToDoUseCase;
import com.zireaell1.todolist.domain.usecases.implementations.EditToDoUseCase;
import com.zireaell1.todolist.domain.usecases.implementations.GetAttachmentsUseCase;
import com.zireaell1.todolist.domain.usecases.implementations.GetCategoriesUseCase;
import com.zireaell1.todolist.domain.usecases.implementations.GetConfigUseCase;
import com.zireaell1.todolist.domain.usecases.implementations.GetToDoUseCase;
import com.zireaell1.todolist.domain.usecases.interfaces.AddAttachment;
import com.zireaell1.todolist.domain.usecases.interfaces.DeleteAttachment;
import com.zireaell1.todolist.domain.usecases.interfaces.DeleteAttachments;
import com.zireaell1.todolist.domain.usecases.interfaces.DeleteToDo;
import com.zireaell1.todolist.domain.usecases.interfaces.EditToDo;
import com.zireaell1.todolist.domain.usecases.interfaces.GetAttachments;
import com.zireaell1.todolist.domain.usecases.interfaces.GetCategories;
import com.zireaell1.todolist.domain.usecases.interfaces.GetConfig;
import com.zireaell1.todolist.domain.usecases.interfaces.GetToDo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ToDoEditViewModel extends ViewModel {
    private final DeleteAttachment deleteAttachment;
    private final GetToDo getToDo;
    private final EditToDo editToDo;
    private final DeleteToDo deleteToDo;
    private final GetCategories getCategories;
    private final AddAttachment addAttachment;
    private final GetAttachments getAttachments;
    private final DeleteAttachments deleteAttachments;
    private final GetConfig getConfig;
    public ToDo toDoObj;
    public int id;
    public String title;
    public String description;
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

    public ToDoEditViewModel(Context context, int toDoId) {
        ToDoRepository toDoRepository = new ToDoDataSource(context);
        ConfigRepository configRepository = new ConfigDataSource(context, "config.xml");

        getToDo = new GetToDoUseCase(toDoRepository);
        editToDo = new EditToDoUseCase(toDoRepository);
        deleteToDo = new DeleteToDoUseCase(toDoRepository);
        getCategories = new GetCategoriesUseCase(toDoRepository);
        addAttachment = new AddAttachmentUseCase(toDoRepository);
        deleteAttachment = new DeleteAttachmentUseCase(toDoRepository);
        getAttachments = new GetAttachmentsUseCase(toDoRepository);
        deleteAttachments = new DeleteAttachmentsUseCase(toDoRepository);
        getConfig = new GetConfigUseCase(configRepository);

        fileUris = new ArrayList<>();

        CompletableFuture<ToDo> futureToDo = getToDo().execute(toDoId);
        futureToDo.thenAccept(toDo -> {
            toDoObj = toDo;
            id = toDo.getId();
            title = toDo.getTitle();
            description = toDo.getDescription();
            categoryId = toDo.getCategoryId();
            state = toDo.getState();
            notifications = toDo.isNotifications();
            stateChipIsChecked = toDo.getState() == ToDoState.DONE;
            notificationsChipIsChecked = toDo.isNotifications();
            dayOfMonth = toDo.getCompletionDate().getDayOfMonth();
            month = toDo.getCompletionDate().getMonthValue();
            year = toDo.getCompletionDate().getYear();
            hourOfDay = toDo.getCompletionDate().getHour();
            minute = toDo.getCompletionDate().getMinute();
            isTimeSet = true;
        });

        futureToDo.join();

        CompletableFuture<List<Category>> futureCategories = getCategories.execute();
        futureCategories.thenAccept(categories -> {
            if (categoryId == -1) {
                categoryDropdownSelectedItemId = 0;
            } else {
                for (Category category : categories) {
                    if (categoryId == category.getId()) {
                        categoryDropdownSelectedItemId = categories.indexOf(category);
                        break;
                    }
                }
            }
        });

        futureCategories.join();
    }

    public GetToDo getToDo() {
        return getToDo;
    }

    public EditToDo getEditToDo() {
        return editToDo;
    }

    public DeleteToDo getDeleteToDo() {
        return deleteToDo;
    }

    public GetCategories getCategories() {
        return getCategories;
    }

    public AddAttachment getAddAttachment() {
        return addAttachment;
    }

    public DeleteAttachment getDeleteAttachment() {
        return deleteAttachment;
    }

    public GetAttachments getAttachments() {
        return getAttachments;
    }

    public DeleteAttachments getDeleteAttachments() {
        return deleteAttachments;
    }

    public GetConfig getConfig() {
        return getConfig;
    }
}
