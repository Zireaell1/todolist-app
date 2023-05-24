package com.zireaell1.todolist.presentation.todoedit;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ToDoEditViewModelFactory implements ViewModelProvider.Factory {
    private final Context mParam;
    private final int toDoId;

    public ToDoEditViewModelFactory(Context param, int toDoId) {
        mParam = param;
        this.toDoId = toDoId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ToDoEditViewModel(mParam, toDoId);
    }
}
