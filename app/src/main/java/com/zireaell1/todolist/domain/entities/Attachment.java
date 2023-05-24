package com.zireaell1.todolist.domain.entities;

public class Attachment {
    private final int id;
    private final int toDoId;
    private final String filePath;

    public Attachment(int toDoId, String filePath) {
        id = 0;
        this.toDoId = toDoId;
        this.filePath = filePath;
    }

    public Attachment(int id, int toDoId, String filePath) {
        this.id = id;
        this.toDoId = toDoId;
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public int getToDoId() {
        return toDoId;
    }

    public String getFilePath() {
        return filePath;
    }
}
