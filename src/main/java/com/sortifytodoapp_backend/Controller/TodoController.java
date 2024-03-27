package com.sortifytodoapp_backend.Controller;

import com.sortifytodoapp_backend.DTO.TodoDTO;
import com.sortifytodoapp_backend.Model.Todo;
import com.sortifytodoapp_backend.Service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Todo> addTodo(@RequestParam("userId") int userId,
                                        @RequestParam("title") String title,
                                        @RequestParam("description") String description,
                                        @RequestParam("time") String time) {
        TodoDTO todoDTO = new TodoDTO(userId, title, description, time);
        Todo savedTodo = todoService.saveTodo(todoDTO);
        return new ResponseEntity<>(savedTodo, HttpStatus.CREATED);
    }

    // Add other controller methods as needed
    @GetMapping
    public ResponseEntity<List<Todo>> getTodosByUserId(@RequestParam int userId) {
        List<Todo> todos = todoService.getAllTodosByUserId(userId);
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoById(@PathVariable int id) {
        todoService.deleteTodoById(id);
        return ResponseEntity.ok().build();
    }
}

