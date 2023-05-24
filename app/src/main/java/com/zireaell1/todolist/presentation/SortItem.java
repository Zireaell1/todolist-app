package com.zireaell1.todolist.presentation;

import com.zireaell1.todolist.domain.entities.Sort;

public class SortItem {
    private final int stringResourceId;
    private final Sort sortMethod;

    public SortItem(int stringResourceId, Sort sortMethod) {
        this.stringResourceId = stringResourceId;
        this.sortMethod = sortMethod;
    }

    public int getStringResourceId() {
        return stringResourceId;
    }

    public Sort getSortMethod() {
        return sortMethod;
    }
}
