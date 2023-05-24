package com.zireaell1.todolist.data.todo.entities;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "attachments")
public class AttachmentEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "toDoId")
    private int toDoId;
    @ColumnInfo(name = "filePath")
    private String filePath;

    public AttachmentEntity(int id, int toDoId, String filePath) {
        this.id = id;
        this.toDoId = toDoId;
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getToDoId() {
        return toDoId;
    }

    public void setToDoId(int toDoId) {
        this.toDoId = toDoId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        AttachmentEntity attachment = (AttachmentEntity) obj;
        return id == attachment.id && toDoId == attachment.toDoId && Objects.equals(filePath, attachment.filePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
