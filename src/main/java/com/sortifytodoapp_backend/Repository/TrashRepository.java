package com.sortifytodoapp_backend.Repository;
import com.sortifytodoapp_backend.Model.Trash;
import com.sortifytodoapp_backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrashRepository extends JpaRepository<Trash, Integer> {
    // Define any custom methods if needed
    List<Trash> findByUser(User user);
}
