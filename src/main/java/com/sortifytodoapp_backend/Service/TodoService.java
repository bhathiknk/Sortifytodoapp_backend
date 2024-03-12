package com.sortifytodoapp_backend.Service;

import com.sortifytodoapp_backend.DTO.TodoDTO;
import com.sortifytodoapp_backend.Model.Todo;
import com.sortifytodoapp_backend.Repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Todo saveTodo(TodoDTO todoDTO) {
        Todo todo = new Todo();
        todo.setUserId(todoDTO.getUserId());
        todo.setTodoTitle(todoDTO.getTodoTitle());
        todo.setTodoDescription(todoDTO.getTodoDescription());
        todo.setTodoTime(todoDTO.getTodoTime());
        return todoRepository.save(todo);
    }

    // Add other service methods as needed
}
