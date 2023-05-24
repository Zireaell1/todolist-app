package com.zireaell1.todolist.presentation.todolist;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zireaell1.todolist.R;

public class ToDoListViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    TextView category;
    TextView completionDate;
    ImageView completionState;
    TextView description;
    TextView createDate;
    TextView attachments;

    public ToDoListViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        category = itemView.findViewById(R.id.category);
        completionDate = itemView.findViewById(R.id.completion_date);
        completionState = itemView.findViewById(R.id.completion_state);
        description = itemView.findViewById(R.id.description);
        createDate = itemView.findViewById(R.id.create_date);
        attachments = itemView.findViewById(R.id.attachments);
    }
}
