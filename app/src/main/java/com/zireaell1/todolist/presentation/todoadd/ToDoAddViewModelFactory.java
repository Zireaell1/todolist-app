package com.zireaell1.todolist.presentation.todoadd;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ToDoAddViewModelFactory implements ViewModelProvider.Factory {
    private final Context mParam;

    public ToDoAddViewModelFactory(Context param) {
        mParam = param;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ToDoAddViewModel(mParam);
    }
}
