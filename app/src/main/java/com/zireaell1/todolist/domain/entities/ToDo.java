package com.zireaell1.todolist.domain.entities;

import java.time.LocalDateTime;

public class ToDo {

    private final int id;
    private final String title;
    private final String description;
    private final LocalDateTime createDate;
    private final LocalDateTime completionDate;
    private final ToDoState state;
    private final boolean notifications;
    private final int categoryId;
    private final String categoryName;
    private final int attachmentCount;

    public ToDo(String title, String description, LocalDateTime createDate, LocalDateTime completionDate, ToDoState state, boolean notifications, int categoryId) {
        id = 0;
        this.title = title;
        this.description = description;
        this.createDate = createDate;
        this.completionDate = completionDate;
        this.state = state;
        this.notifications = notifications;
        this.categoryId = categoryId;
        this.categoryName = "";
        this.attachmentCount = 0;
    }

    public ToDo(int id, String title, String description, LocalDateTime createDate, LocalDateTime completionDate, ToDoState state, boolean notifications, int categoryId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createDate = createDate;
        this.completionDate = completionDate;
        this.state = state;
        this.notifications = notifications;
        this.categoryId = categoryId;
        this.categoryName = "";
        this.attachmentCount = 0;
    }

    public ToDo(int id, String title, String description, LocalDateTime createDate, LocalDateTime completionDate, ToDoState state, boolean notifications, int categoryId, String categoryName, int attachmentCount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createDate = createDate;
        this.completionDate = completionDate;
        this.state = state;
        this.notifications = notifications;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.attachmentCount = attachmentCount;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public ToDoState getState() {
        return state;
    }

    public boolean isNotifications() {
        return notifications;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getAttachmentCount() {
        return attachmentCount;
    }
}
