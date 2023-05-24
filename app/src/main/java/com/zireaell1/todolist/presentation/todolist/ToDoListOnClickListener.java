package com.zireaell1.todolist.presentation.todolist;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.zireaell1.todolist.domain.entities.ToDo;

import java.util.List;

public class ToDoListOnClickListener implements View.OnClickListener {
    private final RecyclerView recyclerView;
    private final List<ToDo> items;
    private final ListItemClickCallback listItemClickCallback;

    public ToDoListOnClickListener(RecyclerView recyclerView, List<ToDo> items, ListItemClickCallback listItemClickCallback) {
        this.recyclerView = recyclerView;
        this.items = items;
        this.listItemClickCallback = listItemClickCallback;
    }

    @Override
    public void onClick(View v) {
        int position = recyclerView.getChildLayoutPosition(v);
        ToDo item = items.get(position);

        listItemClickCallback.onClick(position, item);
    }
}
