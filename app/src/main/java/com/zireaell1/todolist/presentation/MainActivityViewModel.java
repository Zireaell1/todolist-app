package com.zireaell1.todolist.presentation;

import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {
    private int fragment;

    public MainActivityViewModel() {
        fragment = 0;
    }

    public int getFragment() {
        return fragment;
    }

    public void setFragment(int fragment) {
        this.fragment = fragment;
    }
}
