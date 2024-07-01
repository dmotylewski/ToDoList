// src/main/java/com/example/ToDoList/initializer/DataInitializer.java
package com.example.ToDoList.initializer;

import com.example.ToDoList.model.Category;
import com.example.ToDoList.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public DataInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            Category work = new Category();
            work.setName("Work");
            categoryRepository.save(work);

            Category home = new Category();
            home.setName("Home");
            categoryRepository.save(home);

            Category training = new Category();
            training.setName("Training");
            categoryRepository.save(training);

            Category others = new Category();
            others.setName("Others");
            categoryRepository.save(others);

            Category inLine = new Category();
            inLine.setName("In line");
            categoryRepository.save(inLine);
        }
    }
}
