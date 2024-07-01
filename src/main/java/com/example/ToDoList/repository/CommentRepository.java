// src/main/java/com/example/ToDoList/repository/CommentRepository.java
package com.example.ToDoList.repository;

import com.example.ToDoList.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTodoId(Long todoId);
}
