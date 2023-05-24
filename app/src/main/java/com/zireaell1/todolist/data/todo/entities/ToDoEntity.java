package com.zireaell1.todolist.data.todo.entities;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.zireaell1.todolist.domain.entities.ToDoState;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(tableName = "todos")
public class ToDoEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "createDate")
    public LocalDateTime createDate;

    @ColumnInfo(name = "completionDate")
    public LocalDateTime completionDate;

    @ColumnInfo(name = "state")
    public ToDoState state;

    @ColumnInfo(name = "notifications")
    public boolean notifications;

    @ColumnInfo(name = "categoryId")
    public int categoryId;

    public ToDoEntity(int id, String title, String description, LocalDateTime createDate, LocalDateTime completionDate, ToDoState state, boolean notifications, int categoryId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createDate = createDate;
        this.completionDate = completionDate;
        this.state = state;
        this.notifications = notifications;
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        ToDoEntity toDo = (ToDoEntity) obj;
        return id == toDo.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
