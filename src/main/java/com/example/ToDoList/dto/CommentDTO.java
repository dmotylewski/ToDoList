// src/main/java/com/example/ToDoList/dto/CommentDTO.java
package com.example.ToDoList.dto;

public class CommentDTO {
    private Long id;
    private String text;
    private Long todoId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getTodoId() {
        return todoId;
    }

    public void setTodoId(Long todoId) {
        this.todoId = todoId;
    }
}
