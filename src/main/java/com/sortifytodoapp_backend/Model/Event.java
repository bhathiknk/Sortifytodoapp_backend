package com.sortifytodoapp_backend.Model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "event_date")
    private String eventDate;

    @Transient
    private String eventDateStr;

    // Constructors, getters, and setters


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEventDate() {
        return String.valueOf(eventDate);
    }

    public void setEventDateStr(String eventDateStr) {
        this.eventDateStr = eventDateStr;
    }

    public void setEventDate(String eventDateStr) {
        if (eventDateStr != null && !eventDateStr.isEmpty()) {
            this.eventDate = String.valueOf(LocalDate.parse(eventDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
    }
}