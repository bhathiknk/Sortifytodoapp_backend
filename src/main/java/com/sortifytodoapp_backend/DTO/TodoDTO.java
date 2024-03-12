package com.sortifytodoapp_backend.DTO;

public class TodoDTO {
    private Integer userId;
    private String todoTitle;
    private String todoDescription;
    private String todoTime;

    // Getters and setters
    // Constructor with parameters
    public TodoDTO(Integer userId, String todoTitle, String todoDescription, String todoTime) {
        this.userId = userId;
        this.todoTitle = todoTitle;
        this.todoDescription = todoDescription;
        this.todoTime = todoTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTodoTitle() {
        return todoTitle;
    }

    public void setTodoTitle(String todoTitle) {
        this.todoTitle = todoTitle;
    }

    public String getTodoDescription() {
        return todoDescription;
    }

    public void setTodoDescription(String todoDescription) {
        this.todoDescription = todoDescription;
    }

    public String getTodoTime() {
        return todoTime;
    }

    public void setTodoTime(String todoTime) {
        this.todoTime = todoTime;
    }
}
