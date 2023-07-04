package com.zireaell1.todolist.data.todo;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.zireaell1.todolist.data.todo.dao.ToDoDao;
import com.zireaell1.todolist.data.todo.entities.AttachmentEntity;
import com.zireaell1.todolist.data.todo.entities.CategoryEntity;
import com.zireaell1.todolist.data.todo.entities.ToDoEntity;
import com.zireaell1.todolist.data.todo.views.ToDoView;

@Database(entities = {ToDoEntity.class, CategoryEntity.class, AttachmentEntity.class}, views = {ToDoView.class}, version = 9)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, AppDatabase.class, "app-database").fallbackToDestructiveMigration().build();
                    Log.d("AppDatabase", "Successfully created database");
                }
            }
        }
        return instance;
    }

    public abstract ToDoDao toDoDao();
}
