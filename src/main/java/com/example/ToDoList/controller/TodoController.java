// src/main/java/com/example/ToDoList/controller/TodoController.java
package com.example.ToDoList.controller;

import com.example.ToDoList.model.Todo;
import com.example.ToDoList.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:3000")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }

    @GetMapping("/{id}")
    public Optional<Todo> getTodoById(@PathVariable Long id) {
        return todoService.getTodoById(id);
    }

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        validateDueDate(todo.getDueDate());
        return todoService.saveTodo(todo);
    }

    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable Long id, @RequestBody Todo updatedTodo) {
        validateDueDate(updatedTodo.getDueDate());
        return todoService.updateTodo(id, updatedTodo);
    }

    @PutMapping("/{id}/complete")
    public Todo markTodoAsCompleted(@PathVariable Long id) {
        Optional<Todo> optionalTodo = todoService.getTodoById(id);
        if (optionalTodo.isPresent()) {
            Todo todo = optionalTodo.get();
            todo.setCompleted(true);
            return todoService.saveTodo(todo);
        } else {
            throw new RuntimeException("Todo not found with id " + id);
        }
    }

    private void validateDueDate(Date dueDate) {
        if (dueDate != null && dueDate.before(new Date())) {
            throw new IllegalArgumentException("Due date cannot be in the past");
        }
    }
}
