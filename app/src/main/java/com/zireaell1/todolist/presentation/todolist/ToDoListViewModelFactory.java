package com.zireaell1.todolist.presentation.todolist;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ToDoListViewModelFactory implements ViewModelProvider.Factory {
    private final Context mParam;

    public ToDoListViewModelFactory(Context param) {
        mParam = param;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ToDoListViewModel(mParam);
    }
}
