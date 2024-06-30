// src/main/java/com/example/ToDoList/repository/TodoRepository.java
package com.example.ToDoList.repository;

import com.example.ToDoList.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
}
