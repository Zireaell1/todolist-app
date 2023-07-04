package com.zireaell1.todolist.presentation.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zireaell1.todolist.R;
import com.zireaell1.todolist.domain.entities.ToDo;
import com.zireaell1.todolist.domain.entities.ToDoState;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListViewHolder> {
    public final List<ToDo> items;
    private final View.OnClickListener onClickListener;

    public ToDoListAdapter(List<ToDo> items, View.OnClickListener onClickListener) {
        this.items = items;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ToDoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View toDoView = inflater.inflate(R.layout.to_do_list_item, parent, false);
        toDoView.setOnClickListener(onClickListener);
        return new ToDoListViewHolder(toDoView);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoListViewHolder holder, int position) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy | HH:mm");

        ToDo toDo = items.get(position);

        holder.title.setText(toDo.getTitle());
        holder.category.setText(toDo.getCategoryName());
        LocalDateTime completionDate = toDo.getCompletionDate();
        String formattedCompletionDate = completionDate.format(formatter);
        holder.completionDate.setText(formattedCompletionDate);
        if (toDo.getState() == ToDoState.IN_PROGRESS) {
            holder.completionState.setVisibility(View.INVISIBLE);
        } else {
            holder.completionState.setVisibility(View.VISIBLE);
        }
        holder.description.setText(toDo.getDescription());
        LocalDateTime createDate = toDo.getCreateDate();
        String formattedCreateDate = createDate.format(formatter);
        holder.createDate.setText(formattedCreateDate);
        holder.attachments.setText(String.format(Locale.getDefault(), "%d", toDo.getAttachmentCount()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
