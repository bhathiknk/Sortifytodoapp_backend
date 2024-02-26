package com.sortifytodoapp_backend.Service;

import com.sortifytodoapp_backend.Model.User;
import com.sortifytodoapp_backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User registerUser(User user) {
        if (isUsernameTaken(user.getUsername())) {
            // Throw a custom exception indicating that the username is already taken
            throw new RuntimeException("Username is already taken");
        }

        // Additional logic before saving the user, e.g., validation
        return userRepository.save(user);
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }


}
