// src/main/java/com/example/ToDoList/dto/CategoryDTO.java
package com.example.ToDoList.dto;

import java.util.List;

public class CategoryDTO {
    private Long id;
    private String name;
    private List<TodoDTO> todos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TodoDTO> getTodos() {
        return todos;
    }

    public void setTodos(List<TodoDTO> todos) {
        this.todos = todos;
    }
}

