package com.zireaell1.todolist.data.todo.views;

import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;

import com.zireaell1.todolist.domain.entities.ToDoState;

import java.time.LocalDateTime;

@DatabaseView(value = "SELECT t.id, t.title, t.description, t.createDate, t.completionDate, t.state, t.notifications, t.categoryId, c.name AS categoryName, count(a.id) AS attachmentCount FROM todos AS t LEFT JOIN categories AS c ON t.categoryId = c.id LEFT JOIN attachments AS a ON t.id = a.toDoId GROUP BY t.id", viewName = "todoview")
public class ToDoView {
    @ColumnInfo(name = "id")
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
    @ColumnInfo(name = "categoryName")
    public String categoryName;
    @ColumnInfo(name = "attachmentCount")
    public int attachmentCount;
}
