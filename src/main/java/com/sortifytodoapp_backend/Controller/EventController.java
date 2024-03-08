package com.sortifytodoapp_backend.Controller;

import com.sortifytodoapp_backend.DTO.EventDTO;
import com.sortifytodoapp_backend.Model.Event;
import com.sortifytodoapp_backend.Model.User;
import com.sortifytodoapp_backend.Service.EventService;
import com.sortifytodoapp_backend.Service.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<String> createEvent(@RequestBody EventDTO eventDTO) {
        try {
            Event event = new Event();
            User user = userService.getUserById(eventDTO.getUserId());
            event.setUser(user);
            event.setTitle(eventDTO.getTitle());

            // Ensure that the date is formatted correctly (e.g., yyyy-MM-dd)
            event.setEventDate(eventDTO.getDate());

            eventService.saveEvent(event);
            return ResponseEntity.ok("Event created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating event");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EventDTO>> getEventsByUserId(@PathVariable int userId) throws NotFoundException {
        List<Event> userEvents = eventService.getEventsByUserId(userId);
        List<EventDTO> eventDTOList = new ArrayList<>();

        // Convert Event entities to EventDTOs for better control over serialization
        userEvents.forEach(event -> {
            EventDTO eventDTO = new EventDTO();
            eventDTO.setId(event.getId()); // Include the event ID
            eventDTO.setTitle(event.getTitle());
            eventDTO.setDate(event.getEventDate()); // Use the formatted date string
            eventDTOList.add(eventDTO);
        });

        return new ResponseEntity<>(eventDTOList, HttpStatus.OK);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable int eventId) {
        try {
            eventService.deleteEvent(eventId);
            return ResponseEntity.ok("Event deleted successfully");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting event");
        }
    }
}