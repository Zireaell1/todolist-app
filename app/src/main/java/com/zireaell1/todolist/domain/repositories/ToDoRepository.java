package com.zireaell1.todolist.domain.repositories;

import com.zireaell1.todolist.domain.entities.Attachment;
import com.zireaell1.todolist.domain.entities.Category;
import com.zireaell1.todolist.domain.entities.Sort;
import com.zireaell1.todolist.domain.entities.ToDo;
import com.zireaell1.todolist.domain.entities.ToDoState;

import java.util.List;

public interface ToDoRepository {
    long saveToDo(ToDo toDo);

    int updateToDo(ToDo toDo);

    int deleteToDo(ToDo toDo);

    void saveCategory(Category category);

    int updateCategory(Category category);

    int deleteCategory(Category category);

    void saveAttachment(Attachment attachment);

    void saveAttachments(List<Attachment> attachments);

    int deleteAttachment(Attachment attachment);

    int deleteAttachments(List<Attachment> attachments);

    List<ToDo> getToDos(Sort sort, int categoryId, ToDoState state, String searchQuery);

    List<Category> getCategories();

    ToDo getToDo(int toDoId);

    List<Attachment> getAttachments(int toDoId);
}
