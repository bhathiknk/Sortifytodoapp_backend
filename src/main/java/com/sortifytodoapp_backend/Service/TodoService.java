package com.sortifytodoapp_backend.Service;

import com.sortifytodoapp_backend.DTO.TodoDTO;
import com.sortifytodoapp_backend.Model.Todo;
import com.sortifytodoapp_backend.Repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    //this logic save todo data as this given parameter
    public Todo saveTodo(TodoDTO todoDTO) {
        Todo todo = new Todo();
        todo.setUserId(todoDTO.getUserId());
        todo.setTodoTitle(todoDTO.getTodoTitle());
        todo.setTodoDescription(todoDTO.getTodoDescription());
        todo.setTodoTime(todoDTO.getTodoTime());
        return todoRepository.save(todo);
    }

    // this logic get all saved todo from database
    public List<Todo> getAllTodosByUserId(int userId) {
        return todoRepository.findAllByUserId(userId);
    }

    //this logic delete all todo based on the todo Id
    public void deleteTodoById(int id) {
        todoRepository.deleteById(id);
    }
}
