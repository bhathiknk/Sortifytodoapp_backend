package com.sortifytodoapp_backend.Repository;

import com.sortifytodoapp_backend.Model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Integer> {
    // Add custom query methods if needed
}
