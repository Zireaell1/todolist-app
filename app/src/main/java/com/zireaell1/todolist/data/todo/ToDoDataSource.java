package com.zireaell1.todolist.data.todo;

import android.content.Context;

import com.zireaell1.todolist.data.todo.dao.ToDoDao;
import com.zireaell1.todolist.data.todo.entities.AttachmentEntity;
import com.zireaell1.todolist.data.todo.entities.CategoryEntity;
import com.zireaell1.todolist.data.todo.entities.ToDoEntity;
import com.zireaell1.todolist.data.todo.views.ToDoView;
import com.zireaell1.todolist.domain.entities.Attachment;
import com.zireaell1.todolist.domain.entities.Category;
import com.zireaell1.todolist.domain.entities.Sort;
import com.zireaell1.todolist.domain.entities.ToDo;
import com.zireaell1.todolist.domain.entities.ToDoState;
import com.zireaell1.todolist.domain.repositories.ToDoRepository;

import java.util.ArrayList;
import java.util.List;

public class ToDoDataSource implements ToDoRepository {
    private final ToDoDao toDoDao;

    public ToDoDataSource(Context context) {
        toDoDao = AppDatabase.getInstance(context).toDoDao();
    }

    @Override
    public long saveToDo(ToDo toDo) {
        return toDoDao.insertToDo(toToDoEntity(toDo));
    }

    @Override
    public int updateToDo(ToDo toDo) {
        return toDoDao.updateToDo(toToDoEntity(toDo));
    }

    @Override
    public int deleteToDo(ToDo toDo) {
        return toDoDao.deleteToDo(toToDoEntity(toDo));
    }

    @Override
    public void saveCategory(Category category) {
        toDoDao.insertCategory(toCategoryEntity(category));
    }

    @Override
    public int updateCategory(Category category) {
        return toDoDao.updateCategory(toCategoryEntity(category));
    }

    @Override
    public int deleteCategory(Category category) {
        return toDoDao.deleteCategory(toCategoryEntity(category));
    }

    @Override
    public void saveAttachment(Attachment attachment) {
        toDoDao.insertAttachment(toAttachmentEntity(attachment));
    }

    @Override
    public void saveAttachments(List<Attachment> attachments) {
        List<AttachmentEntity> attachmentEntities = new ArrayList<>();
        for (Attachment attachment : attachments) {
            attachmentEntities.add(toAttachmentEntity(attachment));
        }
        toDoDao.insertAttachments(attachmentEntities);
    }

    @Override
    public int deleteAttachment(Attachment attachment) {
        return toDoDao.deleteAttachment(toAttachmentEntity(attachment));
    }

    @Override
    public int deleteAttachments(List<Attachment> attachments) {
        List<AttachmentEntity> attachmentEntities = new ArrayList<>();
        for (Attachment attachment : attachments) {
            attachmentEntities.add(toAttachmentEntity(attachment));
        }
        return toDoDao.deleteAttachments(attachmentEntities);
    }

    @Override
    public List<ToDo> getToDos(Sort sort, int categoryId, ToDoState state, String searchQuery) {
        int sortValue = sort == Sort.DESC ? 0 : 1;

        List<ToDoView> toDoViews = toDoDao.getToDos(sortValue, categoryId, state, searchQuery);
        List<ToDo> toDos = new ArrayList<>();

        for (ToDoView toDoView : toDoViews) {
            toDos.add(toToDoDomain(toDoView));
        }

        return toDos;
    }

    @Override
    public List<Category> getCategories() {
        List<CategoryEntity> categoryEntities = toDoDao.getCategories();
        List<Category> categories = new ArrayList<>();

        for (CategoryEntity categoryEntity : categoryEntities) {
            categories.add(toCategoryDomain(categoryEntity));
        }

        return categories;
    }

    @Override
    public ToDo getToDo(int toDoId) {
        ToDoView toDoView = toDoDao.getToDo(toDoId);
        return toToDoDomain(toDoView);
    }

    @Override
    public List<Attachment> getAttachments(int toDoId) {
        List<AttachmentEntity> attachmentEntities = toDoDao.getAttachments(toDoId);
        List<Attachment> attachments = new ArrayList<>();

        for (AttachmentEntity attachmentEntity : attachmentEntities) {
            attachments.add(toAttachmentDomain(attachmentEntity));
        }

        return attachments;
    }

    private ToDoEntity toToDoEntity(ToDo toDo) {
        return new ToDoEntity(toDo.getId(), toDo.getTitle(), toDo.getDescription(), toDo.getCreateDate(), toDo.getCompletionDate(), toDo.getState(), toDo.isNotifications(), toDo.getCategoryId());
    }

    private ToDo toToDoDomain(ToDoEntity toDoEntity) {
        return new ToDo(toDoEntity.id, toDoEntity.title, toDoEntity.description, toDoEntity.createDate, toDoEntity.completionDate, toDoEntity.state, toDoEntity.notifications, toDoEntity.categoryId);
    }

    private ToDo toToDoDomain(ToDoView toDoView) {
        return new ToDo(toDoView.id, toDoView.title, toDoView.description, toDoView.createDate, toDoView.completionDate, toDoView.state, toDoView.notifications, toDoView.categoryId, toDoView.categoryName, toDoView.attachmentCount);
    }

    private CategoryEntity toCategoryEntity(Category category) {
        return new CategoryEntity(category.getId(), category.getName());
    }

    private Category toCategoryDomain(CategoryEntity categoryEntity) {
        return new Category(categoryEntity.getId(), categoryEntity.getName());
    }

    private AttachmentEntity toAttachmentEntity(Attachment attachment) {
        return new AttachmentEntity(attachment.getId(), attachment.getToDoId(), attachment.getFilePath());
    }

    private Attachment toAttachmentDomain(AttachmentEntity attachmentEntity) {
        return new Attachment(attachmentEntity.getId(), attachmentEntity.getToDoId(), attachmentEntity.getFilePath());
    }
}
