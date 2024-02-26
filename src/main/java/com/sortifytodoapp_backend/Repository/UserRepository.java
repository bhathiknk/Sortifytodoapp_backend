package com.sortifytodoapp_backend.Repository;


import com.sortifytodoapp_backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}