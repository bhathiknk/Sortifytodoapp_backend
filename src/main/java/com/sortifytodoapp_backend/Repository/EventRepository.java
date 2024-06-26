package com.sortifytodoapp_backend.Repository;

import com.sortifytodoapp_backend.Model.Event;
import com.sortifytodoapp_backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {

    //event get using UserId ,event model relationship with the event table many to one
    List<Event> findByUser(User user);

}