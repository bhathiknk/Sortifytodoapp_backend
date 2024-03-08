package com.sortifytodoapp_backend.DTO;

import java.time.LocalDate;

public class EventDTO {
    private Integer userId;
    private String title;
    private String date;


    // Constructors, getters, and setters

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}