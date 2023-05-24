package com.zireaell1.todolist.presentation.todolist;

import com.zireaell1.todolist.domain.entities.ToDo;

public interface ListItemClickCallback {
    void onClick(int position, ToDo item);
}
