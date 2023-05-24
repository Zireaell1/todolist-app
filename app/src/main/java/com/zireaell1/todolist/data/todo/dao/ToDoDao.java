package com.zireaell1.todolist.data.todo.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.zireaell1.todolist.data.todo.views.ToDoView;
import com.zireaell1.todolist.data.todo.entities.AttachmentEntity;
import com.zireaell1.todolist.data.todo.entities.CategoryEntity;
import com.zireaell1.todolist.data.todo.entities.ToDoEntity;
import com.zireaell1.todolist.domain.entities.ToDoState;

import java.util.List;

@Dao
public interface ToDoDao {

    @Insert
    long insertToDo(ToDoEntity toDo);

    @Update
    int updateToDo(ToDoEntity toDo);

    @Delete
    int deleteToDo(ToDoEntity toDo);

    @Insert
    void insertCategory(CategoryEntity category);

    @Update
    int updateCategory(CategoryEntity category);

    @Delete
    int deleteCategory(CategoryEntity category);

    @Insert
    void insertAttachment(AttachmentEntity attachment);

    @Insert
    void insertAttachments(List<AttachmentEntity> attachments);

    @Delete
    int deleteAttachment(AttachmentEntity attachment);

    @Delete
    int deleteAttachments(List<AttachmentEntity> attachments);

    @Query("SELECT * FROM todoview " +
            "WHERE (:categoryId = -1 OR categoryId = :categoryId) " +
            "AND state = :state " +
            "AND (title LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%')" +
            "ORDER BY " +
            "CASE WHEN :sort = 0 THEN createDate END DESC, " +
            "CASE WHEN :sort = 1 THEN createDate END ASC")
    List<ToDoView> getToDos(int sort, int categoryId, ToDoState state, String searchQuery);

    @Query("SELECT * FROM categories")
    List<CategoryEntity> getCategories();

    @Query("SELECT * FROM todoview WHERE id = :toDoId")
    ToDoView getToDo(int toDoId);

    @Query("SELECT * FROM attachments WHERE toDoId = :toDoId")
    List<AttachmentEntity> getAttachments(int toDoId);
}
