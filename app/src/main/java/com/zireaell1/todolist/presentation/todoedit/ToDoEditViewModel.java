package com.zireaell1.todolist.presentation.todoedit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zireaell1.todolist.R;
import com.zireaell1.todolist.data.config.ConfigDataSource;
import com.zireaell1.todolist.data.todo.ToDoDataSource;
import com.zireaell1.todolist.domain.entities.Attachment;
import com.zireaell1.todolist.domain.entities.Category;
import com.zireaell1.todolist.domain.entities.ToDo;
import com.zireaell1.todolist.domain.entities.ToDoState;
import com.zireaell1.todolist.domain.repositories.ConfigRepository;
import com.zireaell1.todolist.domain.repositories.ToDoRepository;
import com.zireaell1.todolist.domain.usecases.implementations.AddAttachmentUseCase;
import com.zireaell1.todolist.domain.usecases.implementations.DeleteAttachmentByFilePathUseCase;
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
import com.zireaell1.todolist.domain.usecases.interfaces.DeleteAttachmentByFilePath;
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
    public final List<Uri> fileUris;
    public final List<Uri> filesToDelete;
    public final List<Uri> newFiles;
    public final List<Uri> originalFiles;
    private final DeleteAttachment deleteAttachment;
    private final GetToDo getToDo;
    private final EditToDo editToDo;
    private final DeleteToDo deleteToDo;
    private final GetCategories getCategories;
    private final AddAttachment addAttachment;
    private final GetAttachments getAttachments;
    private final DeleteAttachments deleteAttachments;
    private final GetConfig getConfig;
    private final DeleteAttachmentByFilePath deleteAttachmentByFilePath;
    private final MutableLiveData<List<Category>> categoriesState = new MutableLiveData<>();
    private final MutableLiveData<List<Attachment>> attachmentsState = new MutableLiveData<>();
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
        deleteAttachmentByFilePath = new DeleteAttachmentByFilePathUseCase(toDoRepository);

        fileUris = new ArrayList<>();
        filesToDelete = new ArrayList<>();
        newFiles = new ArrayList<>();
        originalFiles = new ArrayList<>();

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

        loadAttachments(toDoId);
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

    public DeleteAttachmentByFilePath getDeleteAttachmentByFilePath() {
        return deleteAttachmentByFilePath;
    }

    public LiveData<List<Category>> getCategoriesState() {
        return categoriesState;
    }

    public void loadCategories(Context context) {
        CompletableFuture<List<Category>> future = getCategories.execute();
        future.thenAccept(categoriesState -> {
            categoriesState.add(0, new Category(-1, context.getString(R.string.category_none)));
            this.categoriesState.postValue(categoriesState);
        });
    }

    public LiveData<List<Attachment>> getAttachmentsState() {
        return attachmentsState;
    }

    public void loadAttachments(int toDoId) {
        CompletableFuture<List<Attachment>> future = getAttachments.execute(toDoId);
        future.thenAccept(this.attachmentsState::postValue);
    }
}
