package com.sortifytodoapp_backend.Repository;

import com.sortifytodoapp_backend.Model.File;
import com.sortifytodoapp_backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Integer> {
    List<File> findByUser(User user);
}