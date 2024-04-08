package com.sortifytodoapp_backend.Repository;

import com.sortifytodoapp_backend.Model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Integer> {
    List<Todo> findAllByUserId(int userId);

}
