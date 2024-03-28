package com.sortifytodoapp_backend.Repository;


import com.sortifytodoapp_backend.Model.Trash;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrashRepository extends JpaRepository<Trash, Integer> {
    // Define any custom methods if needed
}
