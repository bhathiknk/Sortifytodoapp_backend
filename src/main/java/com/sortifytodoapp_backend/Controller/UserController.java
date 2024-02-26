package com.sortifytodoapp_backend.Controller;

import com.sortifytodoapp_backend.Model.User;
import com.sortifytodoapp_backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register", consumes = "application/x-www-form-urlencoded;charset=utf-8")
    public ResponseEntity<String> registerUser(@RequestBody MultiValueMap<String, String> formData) {
        String username = formData.getFirst("username");
        String password = formData.getFirst("password");

        User user = new User(username, password);

        if (userService.isUsernameTaken(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already taken");
        }

        User registeredUser = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.OK).body("User registered successfully");
    }

    @PostMapping(value = "/login", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<String> loginUser(@RequestBody MultiValueMap<String, String> formData) {
        String username = formData.getFirst("username");
        String password = formData.getFirst("password");

        User existingUser = userService.getUserByUsernameAndPassword(username, password);

        if (existingUser != null) {
            // User logged in successfully
            return ResponseEntity.status(HttpStatus.OK).body("User logged in successfully");
        } else {
            // Invalid username or password
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

}

