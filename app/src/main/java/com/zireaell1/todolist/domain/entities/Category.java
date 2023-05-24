package com.zireaell1.todolist.domain.entities;

public class Category {
    private final int id;

    private final String name;

    public Category(String name) {
        id = 0;
        this.name = name;
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
