package com.example.ToDoList.service;

import com.example.ToDoList.dto.TodoDTO;
import com.example.ToDoList.dto.CategoryDTO;
import com.example.ToDoList.dto.CommentDTO;
import com.example.ToDoList.model.Todo;
import com.example.ToDoList.model.Category;
import com.example.ToDoList.model.Comment;
import com.example.ToDoList.repository.TodoRepository;
import com.example.ToDoList.repository.CategoryRepository;
import com.example.ToDoList.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CommentRepository commentRepository;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public List<TodoDTO> getAllTodos() {
        return todoRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public Optional<TodoDTO> getTodoById(Long id) {
        return todoRepository.findById(id).map(this::convertToDto);
    }

    public TodoDTO saveTodoDTO(TodoDTO todoDTO) {
        Todo todo = convertToEntity(todoDTO);
        Todo savedTodo = todoRepository.save(todo);
        return convertToDto(savedTodo);
    }

    public TodoDTO updateTodoDTO(Long id, TodoDTO updatedTodoDTO) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isPresent()) {
            Todo todo = optionalTodo.get();
            todo.setTitle(updatedTodoDTO.getTitle());
            todo.setDescription(updatedTodoDTO.getDescription());
            todo.setCompleted(updatedTodoDTO.isCompleted());
            try {
                todo.setDueDate(dateFormat.parse(updatedTodoDTO.getDueDate()));
            } catch (ParseException e) {
                throw new RuntimeException("Invalid date format");
            }
            if (updatedTodoDTO.getCategory() != null) {
                Category category = categoryRepository.findById(updatedTodoDTO.getCategory().getId())
                        .orElseThrow(() -> new RuntimeException("Category not found with id " + updatedTodoDTO.getCategory().getId()));
                todo.setCategory(category);
            }
            Todo updatedTodo = todoRepository.save(todo);
            return convertToDto(updatedTodo);
        } else {
            throw new RuntimeException("Todo not found with id " + id);
        }
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public CategoryDTO saveCategoryDTO(CategoryDTO categoryDTO) {
        Category category = convertToEntity(categoryDTO);
        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);
    }

    public CommentDTO saveCommentDTO(CommentDTO commentDTO) {
        Comment comment = convertToEntity(commentDTO);
        Comment savedComment = commentRepository.save(comment);
        return convertToDto(savedComment);
    }

    public List<CommentDTO> getCommentsByTodoId(Long id) {
        return commentRepository.findByTodoId(id).stream().map(this::convertToDto).collect(Collectors.toList());
    }
    public void deleteTodoById(Long id) {
        todoRepository.deleteById(id);
    }


    private TodoDTO convertToDto(Todo todo) {
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setId(todo.getId());
        todoDTO.setTitle(todo.getTitle());
        todoDTO.setDescription(todo.getDescription());
        todoDTO.setCompleted(todo.isCompleted());
        if (todo.getDueDate() != null) {
            todoDTO.setDueDate(dateFormat.format(todo.getDueDate()));
        }
        if (todo.getCategory() != null) {
            todoDTO.setCategory(convertToDto(todo.getCategory()));
        }
        if (todo.getComments() != null) {
            todoDTO.setComments(todo.getComments().stream().map(this::convertToDto).collect(Collectors.toList()));
        }
        return todoDTO;
    }

    private Todo convertToEntity(TodoDTO todoDTO) {
        Todo todo = new Todo();
        todo.setId(todoDTO.getId());
        todo.setTitle(todoDTO.getTitle());
        todo.setDescription(todoDTO.getDescription());
        todo.setCompleted(todoDTO.isCompleted());
        try {
            todo.setDueDate(dateFormat.parse(todoDTO.getDueDate()));
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format");
        }
        if (todoDTO.getCategory() != null) {
            Category category = categoryRepository.findById(todoDTO.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id " + todoDTO.getCategory().getId()));
            todo.setCategory(category);
        }
        return todo;
    }

    private CommentDTO convertToDto(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setText(comment.getText());
        commentDTO.setTodoId(comment.getTodo().getId());
        return commentDTO;
    }

    private Comment convertToEntity(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setId(commentDTO.getId());
        comment.setText(commentDTO.getText());
        Todo todo = todoRepository.findById(commentDTO.getTodoId()).orElseThrow(() -> new RuntimeException("Todo not found with id " + commentDTO.getTodoId()));
        comment.setTodo(todo);
        return comment;
    }

    private CategoryDTO convertToDto(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }

    private Category convertToEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        return category;
    }
}
