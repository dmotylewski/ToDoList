// src/main/java/com/example/ToDoList/controller/TodoController.java
package com.example.ToDoList.controller;

import com.example.ToDoList.dto.TodoDTO;
import com.example.ToDoList.dto.CategoryDTO;
import com.example.ToDoList.dto.CommentDTO;
import com.example.ToDoList.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:3000")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    public List<TodoDTO> getAllTodos() {
        return todoService.getAllTodos();
    }

    @GetMapping("/{id}")
    public Optional<TodoDTO> getTodoById(@PathVariable Long id) {
        return todoService.getTodoById(id);
    }

    @PostMapping
    public TodoDTO createTodo(@RequestBody TodoDTO todoDTO) {
        return todoService.saveTodoDTO(todoDTO);
    }

    @PutMapping("/{id}")
    public TodoDTO updateTodo(@PathVariable Long id, @RequestBody TodoDTO updatedTodoDTO) {
        return todoService.updateTodoDTO(id, updatedTodoDTO);
    }

    @PutMapping("/{id}/complete")
    public TodoDTO markTodoAsCompleted(@PathVariable Long id) {
        Optional<TodoDTO> optionalTodoDTO = todoService.getTodoById(id);
        if (optionalTodoDTO.isPresent()) {
            TodoDTO todoDTO = optionalTodoDTO.get();
            todoDTO.setCompleted(true);
            return todoService.saveTodoDTO(todoDTO);
        } else {
            throw new RuntimeException("Todo not found with id " + id);
        }
    }

    @GetMapping("/categories")
    public List<CategoryDTO> getAllCategories() {
        return todoService.getAllCategories();
    }

    @PostMapping("/categories")
    public CategoryDTO createCategory(@RequestBody CategoryDTO categoryDTO) {
        return todoService.saveCategoryDTO(categoryDTO);
    }

    @PostMapping("/{id}/comments")
    public CommentDTO addComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {
        Optional<TodoDTO> optionalTodoDTO = todoService.getTodoById(id);
        if (optionalTodoDTO.isPresent()) {
            commentDTO.setTodoId(id);
            return todoService.saveCommentDTO(commentDTO);
        } else {
            throw new RuntimeException("Todo not found with id " + id);
        }
    }

    @GetMapping("/{id}/comments")
    public List<CommentDTO> getCommentsByTodoId(@PathVariable Long id) {

        return todoService.getCommentsByTodoId(id);
    }
    @DeleteMapping("/{id}")
    public void deleteTodoById(@PathVariable Long id) {
        todoService.deleteTodoById(id);
    }

}