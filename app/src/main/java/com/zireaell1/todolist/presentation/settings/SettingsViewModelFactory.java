package com.zireaell1.todolist.presentation.settings;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SettingsViewModelFactory implements ViewModelProvider.Factory {
    private final Context mParam;

    public SettingsViewModelFactory(Context param) {
        mParam = param;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SettingsViewModel(mParam);
    }
}
