// src/main/java/com/example/ToDoList/repository/CategoryRepository.java
package com.example.ToDoList.repository;

import com.example.ToDoList.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
